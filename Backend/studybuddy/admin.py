from django.contrib import admin

from .models import User
from .models import Course 
from .models import Interest
from .models import Message

admin.site.register(User)

admin.site.register(Course)

admin.site.register(Interest)

admin.site.register(Message)
