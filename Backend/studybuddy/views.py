from django.shortcuts import render
from django.core import serializers
from django.shortcuts import render, get_object_or_404, render_to_response
from django.http import HttpResponse, Http404, HttpResponseRedirect
from django.db.models import Q
from django.db.models import F
from django.utils import timezone
from .models import *
import dateutil.parser
import time
import logging

logger = logging.getLogger(__name__)

def index(request):
    return HttpResponse("hello");

def addCourse(request):
    name = request.GET.get('course_name')
    title = request.GET.get('course_title')
    course = Course()
    course.name = name
    course.title = title
    course.save()
    return HttpResponse('Successfully added course')

def addInterest(request):
    name = request.GET.get('interest_name')
    interest = Interest()
    interest.name = name
    interest.save()
    return HttpResponse('Successfully added interest')

def addUser(request):
    try:
        user = User.objects.get(username=request.GET.get('username'))
        return HttpResponse('User already exists')
    except(User.DoesNotExist):
        logger.error('adding user')
    user = User()
    user.username = request.GET.get('username') 
    user.save()
    return HttpResponse('Successfully added user')

def editUser(request):
    user = get_object_or_404(User, username = request.GET.get('username'))
    year = request.GET.get('year')
    if not year==None:
        user.year = year
        user.save()
    bio = request.GET.get('bio')
    if not bio==None:
        user.bio = bio
        user.save()
    return HttpResponse('Successfully edited user')

def getAllUsers(request):
    return HttpResponse(serializers.serialize('json', User.objects.all(), use_natural_foreign_keys=True))

def getUserList(request):
    return HttpResponse(serializers.serialize('json', User.objects.exclude(is_group=True), use_natural_foreign_keys=True))

def getPersonalUserList(request):
    user = get_object_or_404(User, username = request.GET.get('username'))
    return HttpResponse(serializers.serialize('json', User.objects.exclude(Q(is_group=True) | Q(id__in=user.block_list.all())), use_natural_foreign_keys=True))

def getUserInfo(request):
    return HttpResponse(serializers.serialize('json', User.objects.filter(username=request.GET.get('username')), use_natural_foreign_keys=True))


def addUserCourse(request):
    user = get_object_or_404(User, username = request.GET.get('username'))
    course = get_object_or_404(Course, name = request.GET.get('course_name'))
    user.courses.add(course)
    user.save()
    return HttpResponse('Successfully added user course')

def removeUserCourse(request):
    user = get_object_or_404(User, username = request.GET.get('username'))
    course = get_object_or_404(Course, name = request.GET.get('course_name'))
    user.courses.remove(course)
    user.save()
    return HttpResponse('Successfully removed user course')

def getCourseList(request):
    return HttpResponse(serializers.serialize('json', Course.objects.all(), use_natural_foreign_keys=True))

def addUserInterest(request):
    user = get_object_or_404(User, username = request.GET.get('username'))
    try:
        interest = Interest.objects.get(name = request.GET.get('interest_name'))
    except(Interest.DoesNotExist):
        interest = Interest()
        interest.name = request.GET.get('interest_name')
        interest.save()
    user.interests.add(interest)
    user.save()
    return HttpResponse('Successfully added user interest')

def removeUserInterest(request):
    user = get_object_or_404(User, username = request.GET.get('username'))
    interest = Interest.objects.get(name = request.GET.get('interest_name'))
    user.interests.remove(interest)
    user.save()
    return HttpResponse('Successfully removed user interest')

def getInterestList(request):
    return HttpResponse(serializers.serialize('json', Interest.objects.all(), use_natural_foreign_keys=True))

def addMessage(request):
    message = Message()
    message.sender = get_object_or_404(User, username = request.GET.get('sender'))
    message.receiver = get_object_or_404(User, username = request.GET.get('receiver'))
    message.timestamp = timezone.now()
    if message.sender == message.receiver:
        return HttpResponse(status=400, reason='sender and receiver cannot be the same')
    message.content = request.GET.get('content')
    message.save()
    return HttpResponse('Successfully added message')

def getMessages(request):
    p1 = get_object_or_404(User, username = request.GET.get('p1'))
    p2 = get_object_or_404(User, username = request.GET.get('p2'))
    return HttpResponse(serializers.serialize('json', Message.objects.filter(Q(sender=p1.id) | Q(sender=p2.id), Q(receiver=p1.id) | Q(receiver=p2.id)), use_natural_foreign_keys=True))

def getOlderMessages(request):
    p1 = get_object_or_404(User, username = request.GET.get('p1'))
    p2 = get_object_or_404(User, username = request.GET.get('p2'))
    argtimestamp = dateutil.parser.parse(request.GET.get('timestamp'))
    messages = Message.objects.filter(Q(sender=p1.id) | Q(sender=p2.id), Q(receiver=p1.id) | Q(receiver=p2.id), Q(timestamp__lt=argtimestamp))
    messagecount = messages.count()
    if messagecount > 15:
        messages = messages[messagecount-15:messagecount]
    return HttpResponse(serializers.serialize('json',messages, use_natural_foreign_keys=True))

def getNewerMessages(request):
    p1 = get_object_or_404(User, username = request.GET.get('p1'))
    p2 = get_object_or_404(User, username = request.GET.get('p2'))
    argtimestamp = dateutil.parser.parse(request.GET.get('timestamp'))
    messages = Message.objects.filter(Q(sender=p1.id) | Q(sender=p2.id), Q(receiver=p1.id) | Q(receiver=p2.id), Q(timestamp__gt=argtimestamp))
    return HttpResponse(serializers.serialize('json',messages, use_natural_foreign_keys=True))

def getChatList(request):
    user = get_object_or_404(User, username = request.GET.get('user'))
    allMessages = Message.objects.filter(Q(sender=user.id)|Q(receiver=user.id)).values()
    
    participants = set(map(lambda x: x['sender_id'], Message.objects.filter(Q(sender=user.id)|Q(receiver=user.id)).values()) +
                       map(lambda x: x['receiver_id'], Message.objects.filter(Q(sender=user.id)|Q(receiver=user.id)).values()))
    participants.discard(user.id)
    return HttpResponse(serializers.serialize('json', User.objects.filter(id__in=participants).exclude(id__in=user.block_list.all()), use_natural_foreign_keys=True))

def createGroup(request):
    group, created = User.objects.get_or_create(username=request.GET.get('name'))
    if not created:
        if not group.is_group:
            return HttpResponse(status=400, reason='user exists with the same name')
        else:
            return HttpResponse(status=400, reason='group already exists')
    user = get_object_or_404(User, username=request.GET.get('user'))
    if user.is_group:
        return HttpResponse(status=400, reason='user argument specified a group name')
    group.is_group = True
    group.save()

    message = Message()
    message.sender = user
    message.receiver = group
    message.timestamp = timezone.now()
    message.content = ''
    message.save()

    return HttpResponse('successfully created group')

def addUserToGroup(request):
    group = get_object_or_404(User, username=request.GET.get('name'))
    if not group.is_group:
        return HttpResponse(status=400, reason='name argument specified a user name')
    user = get_object_or_404(User, username=request.GET.get('user'))
    if user.is_group:
        return HttpResponse(status=400, reason='user argument specified a group name')
    
    messageCount = Message.objects.filter(Q(sender = user), Q(receiver = group), Q(content='')).count()

    if messageCount != 0:
        return HttpResponse(status=400, reason='user is already a member of this group')

    message = Message()
    message.sender = user
    message.receiver = group
    message.timestamp = timezone.now()
    message.content = ''
    message.save()

    return HttpResponse('Successfully added user to group')

def removeUserFromGroup(request):
    group = get_object_or_404(User, username=request.GET.get('name'))
    if not group.is_group:
        return HttpResponse(status=400, reason='name argument specified a user name')
    user = get_object_or_404(User, username=request.GET.get('user'))
    if user.is_group:
        return HttpResponse(status=400, reason='user argument specified a group name')

    message = Message.objects.filter(Q(sender = user), Q(receiver = group), Q(content=''))

    if message.count() == 0:
        return HttpResponse(status=400, reason='user is not a member of this group')

    message.delete()

    return HttpResponse('Successfully removed user from group')

def getGroupMembers(request):
    group = get_object_or_404(User, username=request.GET.get('name'))
    if not group.is_group:
        return HttpResponse(status=400, reason='name argument specified a user name')

    member_messages = Message.objects.filter(Q(receiver = group), Q(content=''))

    participants = map(lambda x: x['sender_id'], member_messages.values())

    return HttpResponse(serializers.serialize('json', User.objects.filter(id__in=participants), use_natural_foreign_keys=True))

def sendMessageToGroup(request):
    group = get_object_or_404(User, username=request.GET.get('name'))
    if not group.is_group:
        return HttpResponse(status=400, reason='name argument specified a user name')
    user = get_object_or_404(User, username=request.GET.get('user'))
    if user.is_group:
        return HttpResponse(status=400, reason='user argument specified a group name')

    messageCount = Message.objects.filter(Q(sender = user), Q(receiver = group), Q(content='')).count()
    if messageCount == 0:
        return HttpResponse(status=400, reason='user is not a member of the group')

    message = Message()
    message.sender = user
    message.receiver = group
    message.timestamp = timezone.now()
    message.content=request.GET.get('content')
    message.save()

    return HttpResponse('successfully sent message to group')

def getGroupMessages(request):
    group = get_object_or_404(User, username=request.GET.get('name'))
    if not group.is_group:
        return HttpResponse(status=400, reason='name argument specified a username')
    messages = Message.objects.filter(Q(receiver = group))
    for message in messages:
        if message.content == '':
            message.content = 'joined the group'

    return HttpResponse(serializers.serialize('json', messages, use_natural_foreign_keys=True))

def whatsTheFeedbackEmail(request):
    return HttpResponse('<h1>the email is studybuddy307@gmail.com and the password is studybuddy307#</h1>')

def addBlockedUser(request):
    user = get_object_or_404(User, username = request.GET.get('username'))
    block = get_object_or_404(User, username = request.GET.get('block'))
    user.block_list.add(block)
    user.save()
    return HttpResponse('Successfully blocked user')

def removeBlockedUser(request):
    user = get_object_or_404(User, username = request.GET.get('username'))
    block = get_object_or_404(User, username = request.GET.get('block'))
    user.block_list.remove(block)
    user.save()
    return HttpResponse('Successfully unblocked user')
