# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('studybuddy', '0010_auto_20151104_2329'),
    ]

    operations = [
        migrations.AlterField(
            model_name='interest',
            name='name',
            field=models.CharField(unique=True, max_length=50),
        ),
    ]
