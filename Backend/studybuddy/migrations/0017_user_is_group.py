# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('studybuddy', '0016_auto_20151104_2345'),
    ]

    operations = [
        migrations.AddField(
            model_name='user',
            name='is_group',
            field=models.BooleanField(default=False),
        ),
    ]
