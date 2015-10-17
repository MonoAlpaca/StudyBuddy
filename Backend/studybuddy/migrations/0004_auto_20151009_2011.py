# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('studybuddy', '0003_auto_20151009_2008'),
    ]

    operations = [
        migrations.AlterField(
            model_name='user',
            name='courses',
            field=models.ManyToManyField(to='studybuddy.Course'),
        ),
        migrations.AlterField(
            model_name='user',
            name='interests',
            field=models.ManyToManyField(to='studybuddy.Interest'),
        ),
    ]
