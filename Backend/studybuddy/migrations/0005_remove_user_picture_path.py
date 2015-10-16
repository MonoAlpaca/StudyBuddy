# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('studybuddy', '0004_auto_20151009_2011'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='user',
            name='picture_path',
        ),
    ]
