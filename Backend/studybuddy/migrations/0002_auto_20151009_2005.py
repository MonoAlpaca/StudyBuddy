# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('studybuddy', '0001_initial'),
    ]

    operations = [
        migrations.CreateModel(
            name='Course',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('name', models.CharField(max_length=20)),
            ],
        ),
        migrations.CreateModel(
            name='Interest',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('name', models.CharField(max_length=50)),
            ],
        ),
        migrations.AddField(
            model_name='user',
            name='bio',
            field=models.CharField(default=b'', max_length=500),
        ),
        migrations.AddField(
            model_name='user',
            name='picture_path',
            field=models.CharField(default=b'', max_length=200),
        ),
        migrations.AddField(
            model_name='user',
            name='year',
            field=models.CharField(default=b'FR', max_length=2, choices=[(b'FR', b'Freshman'), (b'SO', b'Sophomore'), (b'JR', b'Junior'), (b'SR', b'Senior'), (b'GR', b'Graduate')]),
        ),
        migrations.AddField(
            model_name='user',
            name='courses',
            field=models.ManyToManyField(to='studybuddy.Course'),
        ),
        migrations.AddField(
            model_name='user',
            name='interests',
            field=models.ManyToManyField(to='studybuddy.Interest'),
        ),
    ]
