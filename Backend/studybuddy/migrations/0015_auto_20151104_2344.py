# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations
import datetime
from django.utils.timezone import utc


class Migration(migrations.Migration):

    dependencies = [
        ('studybuddy', '0014_auto_20151104_2342'),
    ]

    operations = [
        migrations.AlterField(
            model_name='message',
            name='timestamp',
            field=models.DateTimeField(default=datetime.datetime(2015, 11, 4, 23, 44, 25, 97072, tzinfo=utc)),
        ),
    ]
