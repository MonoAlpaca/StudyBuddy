from django.db import models
from datetime import datetime
from django.utils import timezone

class CourseManager(models.Manager):
    def get_by_natural_key(self, name, title):
        return self.get(name=name, title=title)

class Course(models.Model):
    objects = CourseManager()

    #name is full name, title is abbr name
    name = models.CharField(max_length=20)
    title = models.CharField(max_length=50)
    #get and set for this field as well
    def natural_key(self):
        return (self.name, self.title)
    class Meta:
        unique_together = (('name', 'title'),)
    def __str__(self):
        return self.name

class InterestManager(models.Manager):
    def get_by_natural_key(self, name):
        return self.get(name=name)
class Interest(models.Model):
    name = models.CharField(max_length=50, unique=True)
    def natural_key(self):
        return (self.name,)
    def __str__(self):
        return self.name

class User(models.Model):
    year_choices = (
            ('FR', 'Freshman'),
            ('SO', 'Sophomore'),
            ('JR', 'Junior'),
            ('SR', 'Senior'),
            ('GR', 'Graduate'),
        )
    username = models.CharField(max_length=50)
    year = models.CharField(default='FR', max_length=2, choices=year_choices)
    bio = models.CharField(default='', max_length=500)
    courses = models.ManyToManyField(Course)
    interests = models.ManyToManyField(Interest)
    def __str__(self):
        return self.username

class Message(models.Model):
    sender = models.ForeignKey(User, related_name='+')
    receiver = models.ForeignKey(User, related_name='+')
    timestamp = models.DateField(default = timezone.now)
    content = models.CharField(max_length=500)
    class Meta:
        ordering = ['timestamp']
    def __str__(self):
        return "FROM: {0} TO: {1} TIME: {2} {3}".format(self.sender, self.receiver, self.timestamp, self.content)
