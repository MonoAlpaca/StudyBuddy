# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations
import datetime


class Migration(migrations.Migration):

    dependencies = [
        ('studybuddy', '0007_course_title'),
    ]

    operations = [
        migrations.AlterField(
            model_name='interest',
            name='name',
            field=models.CharField(unique=True, max_length=50),
        ),
        migrations.AlterField(
            model_name='message',
            name='timestamp',
            field=models.DateTimeField(default=datetime.datetime(2015, 11, 4, 23, 22, 58, 972317)),
        ),
        migrations.AlterUniqueTogether(
            name='course',
            unique_together=set([('name', 'title')]),
        ),
    ]
