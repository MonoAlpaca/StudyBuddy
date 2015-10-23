from django.shortcuts import render
from django.core import serializers
from django.shortcuts import render, get_object_or_404, render_to_response
from django.http import HttpResponse, Http404, HttpResponseRedirect
from .models import *
import logging

logger = logging.getLogger(__name__)

def index(request):
    return HttpResponse("hello");

def addCourse(request):
    name = request.GET.get('name')
    course = Course()
    course.name = name
    course.save()
    return HttpResponse('Successfully added course')

def addInterest(request):
    name = request.GET.get('name')
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

def getUserList(request):
    return HttpResponse(serializers.serialize('json', User.objects.all()))

def getUserInfo(request):
    return HttpResponse(serializers.serialize('json', User.objects.filter(username=request.GET.get('username'))))

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
    return HttpResponse(serializers.serialize('json', Course.objects.all()))

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
    return HttpResponse(serializers.serialize('json', Interest.objects.all()))
