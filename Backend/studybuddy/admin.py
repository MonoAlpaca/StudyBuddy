from django.contrib import admin

from .models import User
from .models import Course 
from .models import Interest

admin.site.register(User)

admin.site.register(Course)

admin.site.register(Interest)
