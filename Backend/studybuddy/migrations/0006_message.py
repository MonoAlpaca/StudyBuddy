# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations
import django.utils.timezone


class Migration(migrations.Migration):

    dependencies = [
        ('studybuddy', '0005_remove_user_picture_path'),
    ]

    operations = [
        migrations.CreateModel(
            name='Message',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('timestamp', models.DateField(default=django.utils.timezone.now)),
                ('content', models.CharField(max_length=500)),
                ('receiver', models.ForeignKey(related_name='+', to='studybuddy.User')),
                ('sender', models.ForeignKey(related_name='+', to='studybuddy.User')),
            ],
            options={
                'ordering': ['timestamp'],
            },
        ),
    ]
