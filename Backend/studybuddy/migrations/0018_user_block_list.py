# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('studybuddy', '0017_user_is_group'),
    ]

    operations = [
        migrations.AddField(
            model_name='user',
            name='block_list',
            field=models.ManyToManyField(to='studybuddy.User'),
        ),
    ]
