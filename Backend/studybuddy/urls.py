"""studybuddy URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/1.8/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  url(r'^$', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  url(r'^$', Home.as_view(), name='home')
Including another URLconf
    1. Add an import:  from blog import urls as blog_urls
    2. Add a URL to urlpatterns:  url(r'^blog/', include(blog_urls))
"""


from django.conf.urls import include, url, patterns
from django.conf.urls.static import static
from django.conf import settings

from django.contrib import admin
from . import views

urlpatterns = patterns('',
    url(r'^$', views.index, name = 'index'),
    url(r'^admin/', include(admin.site.urls)),
    url(r'^addCourse$', views.addCourse),
    url(r'^addInterest$', views.addInterest),
    url(r'^addUser$', views.addUser),
    url(r'^editUser$', views.editUser),
    url(r'^addUserCourse$', views.addUserCourse),
    url(r'^removeUserCourse$', views.removeUserCourse),
    url(r'^addUserInterest$', views.addUserInterest),
    url(r'^removeUserInterest$', views.removeUserInterest),
    url(r'^getUserList$', views.getUserList),
    url(r'^getUserByCourse$', views.getUserByCourse),
    url(r'^getUserByInterest$', views.getUserByInterest),
    url(r'^getPersonalUserList$', views.getPersonalUserList),
    url(r'^getAllUsers$', views.getAllUsers),
    url(r'^getUserInfo$', views.getUserInfo),
    url(r'^getCourseList$', views.getCourseList),
    url(r'^getInterestList$', views.getInterestList),
    url(r'^addMessage$', views.addMessage),
    url(r'^getMessages$', views.getMessages),
    url(r'^getNewerMessages$', views.getNewerMessages),
    url(r'^getOlderMessages$', views.getOlderMessages),
    url(r'^getChatList$', views.getChatList),
    url(r'^createGroup$', views.createGroup),
    url(r'^addUserToGroup$', views.addUserToGroup),
    url(r'^removeUserFromGroup$', views.removeUserFromGroup),
    url(r'^getGroupMembers$', views.getGroupMembers),
    url(r'^getGroupMessages$', views.getGroupMessages),
    url(r'^sendMessageToGroup$', views.sendMessageToGroup),
    url(r'^addBlockedUser$', views.addBlockedUser),
    url(r'^removeBlockedUser$', views.removeBlockedUser),
    url(r'^whatsTheFeedbackEmail$', views.whatsTheFeedbackEmail),
) + static(settings.STATIC_URL, document_root=settings.STATIC_ROOT)

