# -*- coding: utf-8 -*-
# Generated by Django 1.11 on 2019-02-23 05:01
from __future__ import unicode_literals

from django.db import migrations, models
import django.db.models.deletion
import jsonfield.fields


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Article',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('title', models.CharField(max_length=64, verbose_name='title')),
                ('thumb_media_id', models.CharField(max_length=64, verbose_name='thumb_media_id')),
                ('show_cover_pic', models.BooleanField(default=True, verbose_name='show cover')),
                ('author', models.CharField(blank=True, default='', max_length=24, null=True, verbose_name='author')),
                ('digest', models.CharField(blank=True, default='', max_length=256, null=True, verbose_name='digest')),
                ('content', models.TextField(verbose_name='content')),
                ('url', models.CharField(max_length=256, verbose_name='url')),
                ('content_source_url', models.CharField(max_length=256, verbose_name='content source url')),
                ('need_open_comment', models.NullBooleanField(default=None, verbose_name='need open comment')),
                ('only_fans_can_comment', models.NullBooleanField(default=None, verbose_name='only fans can comment')),
                ('index', models.PositiveSmallIntegerField(verbose_name='index')),
                ('_thumb_url', models.CharField(db_column='thumb_url', default=None, max_length=256, null=True)),
                ('synced_at', models.DateTimeField(auto_now_add=True, verbose_name='synchronized at')),
            ],
            options={
                'verbose_name': 'article',
                'verbose_name_plural': 'articles',
                'ordering': ('material', 'index'),
            },
        ),
        migrations.CreateModel(
            name='Material',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('type', models.CharField(choices=[('image', 'IMAGE'), ('news', 'NEWS'), ('video', 'VIDEO'), ('voice', 'VOICE')], max_length=5, verbose_name='type')),
                ('media_id', models.CharField(max_length=64, verbose_name='media_id')),
                ('name', models.CharField(blank=True, max_length=64, null=True, verbose_name='name')),
                ('url', models.CharField(editable=False, max_length=512, null=True, verbose_name='url')),
                ('update_time', models.IntegerField(editable=False, null=True, verbose_name='update time')),
                ('comment', models.TextField(blank=True, verbose_name='comment')),
                ('created_at', models.DateTimeField(auto_now_add=True, verbose_name='created at')),
                ('updated_at', models.DateTimeField(auto_now=True, verbose_name='updated at')),
            ],
            options={
                'verbose_name': 'material',
                'verbose_name_plural': 'materials',
                'ordering': ('app', '-update_time'),
            },
        ),
        migrations.CreateModel(
            name='Menu',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=16, verbose_name='name')),
                ('menuid', models.IntegerField(blank=True, null=True, verbose_name='menuid')),
                ('type', models.CharField(blank=True, choices=[('click', 'CLICK'), ('miniprogram', 'MINIPROGRAM'), ('view', 'VIEW')], max_length=20, null=True, verbose_name='type')),
                ('content', jsonfield.fields.JSONField()),
                ('weight', models.IntegerField(default=0, verbose_name='weight')),
                ('created_at', models.DateTimeField(auto_now_add=True, verbose_name='created at')),
                ('updated_at', models.DateTimeField(auto_now=True, verbose_name='updated at')),
            ],
            options={
                'verbose_name': 'menu',
                'verbose_name_plural': 'menus',
                'ordering': ('app', '-weight', 'id'),
            },
        ),
        migrations.CreateModel(
            name='MessageHandler',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=60, verbose_name='name')),
                ('src', models.PositiveSmallIntegerField(choices=[(2, 'wechat'), (0, 'self'), (1, 'menu')], default=0, editable=False)),
                ('strategy', models.CharField(choices=[('none', 'NONE'), ('random_one', 'RANDOM'), ('reply_all', 'REPLYALL')], default='reply_all', max_length=10, verbose_name='strategy')),
                ('flags', models.IntegerField(default=False, verbose_name='flags')),
                ('starts', models.DateTimeField(blank=True, null=True, verbose_name='starts')),
                ('ends', models.DateTimeField(blank=True, null=True, verbose_name='ends')),
                ('enabled', models.BooleanField(default=True, verbose_name='enabled')),
                ('weight', models.IntegerField(default=0, verbose_name='weight')),
                ('created_at', models.DateTimeField(auto_now_add=True, verbose_name='created_at')),
                ('updated_at', models.DateTimeField(auto_now=True, verbose_name='updated_at')),
            ],
            options={
                'verbose_name': 'message handler',
                'verbose_name_plural': 'message handlers',
                'ordering': ('-weight', '-created_at', '-id'),
            },
        ),
        migrations.CreateModel(
            name='MessageLog',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('msg_id', models.BigIntegerField(null=True, verbose_name='msgid')),
                ('type', models.CharField(choices=[('event', 'EVENT'), ('image', 'IMAGE'), ('link', 'LINK'), ('location', 'LOCATION'), ('shortvideo', 'SHORTVIDEO'), ('text', 'TEXT'), ('video', 'VIDEO'), ('voice', 'VOICE')], max_length=24, verbose_name='message type')),
                ('content', jsonfield.fields.JSONField(verbose_name='content')),
                ('direct', models.BooleanField(default=False, verbose_name='direct')),
                ('raw', models.TextField(blank=True, default=None, null=True)),
                ('created_at', models.DateTimeField(auto_now_add=True, verbose_name='created at')),
            ],
            options={
                'verbose_name': 'message log',
                'verbose_name_plural': 'message logs',
                'ordering': ('app', '-created_at'),
            },
        ),
        migrations.CreateModel(
            name='Reply',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('type', models.CharField(choices=[('custom', 'CUSTOM'), ('forward', 'FORWARD'), ('image', 'IMAGE'), ('music', 'MUSIC'), ('news', 'NEWS'), ('text', 'TEXT'), ('video', 'VIDEO'), ('voice', 'VOICE')], db_column='type', max_length=16, verbose_name='type')),
                ('_content', jsonfield.fields.JSONField(db_column='content', default=dict)),
                ('weight', models.IntegerField(default=0, verbose_name='weight')),
                ('created_at', models.DateTimeField(auto_now_add=True, verbose_name='created_at')),
                ('updated_at', models.DateTimeField(auto_now=True, verbose_name='updated_at')),
                ('handler', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='replies', to='wechat_django.MessageHandler')),
            ],
            options={
                'verbose_name': 'reply',
                'verbose_name_plural': 'replies',
                'ordering': ('-weight', 'id'),
            },
        ),
        migrations.CreateModel(
            name='Rule',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('type', models.CharField(choices=[('all', 'ALL'), ('contain', 'CONTAIN'), ('equal', 'EQUAL'), ('event', 'EVENT'), ('eventkey', 'EVENTKEY'), ('msg_type', 'MSGTYPE'), ('regex', 'REGEX')], max_length=16, verbose_name='type')),
                ('_content', jsonfield.fields.JSONField(blank=True, db_column='content')),
                ('weight', models.IntegerField(default=0, verbose_name='weight')),
                ('created_at', models.DateTimeField(auto_now_add=True, verbose_name='created_at')),
                ('updated_at', models.DateTimeField(auto_now=True, verbose_name='updated_at')),
                ('handler', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='rules', to='wechat_django.MessageHandler')),
            ],
            options={
                'verbose_name': 'rule',
                'verbose_name_plural': 'rules',
                'ordering': ('-weight', 'id'),
            },
        ),
        migrations.CreateModel(
            name='WeChatApp',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('title', models.CharField(help_text='公众号名称,用于后台辨识公众号', max_length=16, verbose_name='title')),
                ('name', models.CharField(help_text='公众号唯一标识,可采用微信号,设定后不可修改,用于程序辨识', max_length=16, unique=True, verbose_name='name')),
                ('desc', models.TextField(blank=True, default='', verbose_name='description')),
                ('appid', models.CharField(max_length=32, verbose_name='AppId')),
                ('appsecret', models.CharField(max_length=64, null=True, verbose_name='AppSecret')),
                ('type', models.PositiveSmallIntegerField(choices=[(3, 'MINIPROGRAM'), (1, 'SERVICEAPP'), (2, 'SUBSCRIBEAPP')], default=1, verbose_name='type')),
                ('token', models.CharField(blank=True, max_length=32, null=True)),
                ('encoding_aes_key', models.CharField(blank=True, max_length=43, null=True, verbose_name='EncodingAESKey')),
                ('encoding_mode', models.PositiveSmallIntegerField(choices=[(0, 'PLAIN'), (2, 'SAFE')], default=0, verbose_name='encoding mode')),
                ('flags', models.IntegerField(default=0, verbose_name='flags')),
                ('ext_info', jsonfield.fields.JSONField(db_column='ext_info', default={})),
                ('configurations', jsonfield.fields.JSONField(db_column='configurations', default={})),
                ('created_at', models.DateTimeField(auto_now_add=True, verbose_name='created at')),
                ('updated_at', models.DateTimeField(auto_now=True, verbose_name='updated at')),
            ],
            options={
                'verbose_name': 'WeChat app',
                'verbose_name_plural': 'WeChat apps',
            },
        ),
        migrations.CreateModel(
            name='WeChatUser',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('openid', models.CharField(max_length=36, verbose_name='openid')),
                ('unionid', models.CharField(max_length=36, null=True, verbose_name='unionid')),
                ('nickname', models.CharField(max_length=24, null=True, verbose_name='nickname')),
                ('sex', models.SmallIntegerField(choices=[(2, 'FEMALE'), (1, 'MALE'), (0, 'UNKNOWN')], null=True, verbose_name='gender')),
                ('headimgurl', models.CharField(max_length=256, null=True, verbose_name='avatar')),
                ('city', models.CharField(max_length=24, null=True, verbose_name='city')),
                ('province', models.CharField(max_length=24, null=True, verbose_name='province')),
                ('country', models.CharField(max_length=24, null=True, verbose_name='country')),
                ('language', models.CharField(max_length=24, null=True, verbose_name='language')),
                ('subscribe', models.NullBooleanField(verbose_name='is subscribed')),
                ('subscribe_time', models.IntegerField(null=True, verbose_name='subscribe time')),
                ('subscribe_scene', models.CharField(choices=[('ADD_SCENE_ACCOUNT_MIGRATION', 'ADD_SCENE_ACCOUNT_MIGRATION'), ('ADD_SCENE_OTHERS', 'ADD_SCENE_OTHERS'), ('ADD_SCENE_PAID', 'ADD_SCENE_PAID'), ('ADD_SCENE_PROFILE_CARD', 'ADD_SCENE_PROFILE_CARD'), ('ADD_SCENE_PROFILE_ITEM', 'ADD_SCENE_PROFILE_ITEM'), ('ADD_SCENEPROFILE LINK', 'ADD_SCENE_PROFILE_LINK'), ('ADD_SCENE_QR_CODE', 'ADD_SCENE_QR_CODE'), ('ADD_SCENE_SEARCH', 'ADD_SCENE_SEARCH')], max_length=32, null=True, verbose_name='subscribe scene')),
                ('qr_scene', models.IntegerField(null=True, verbose_name='qr scene')),
                ('qr_scene_str', models.CharField(max_length=64, null=True, verbose_name='qr_scene_str')),
                ('remark', models.CharField(blank=True, max_length=30, null=True, verbose_name='WeChat remark')),
                ('comment', models.TextField(blank=True, null=True, verbose_name='remark')),
                ('groupid', models.IntegerField(null=True, verbose_name='group id')),
                ('created_at', models.DateTimeField(auto_now_add=True, verbose_name='created at')),
                ('updated_at', models.DateTimeField(auto_now=True, verbose_name='updated at')),
                ('synced_at', models.DateTimeField(default=None, null=True, verbose_name='synchronized at')),
                ('app', models.ForeignKey(editable=False, on_delete=django.db.models.deletion.CASCADE, related_name='users', to='wechat_django.WeChatApp')),
            ],
            options={
                'verbose_name': 'user',
                'verbose_name_plural': 'users',
                'ordering': ('app', '-created_at'),
            },
        ),
        migrations.AddField(
            model_name='messagelog',
            name='app',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='wechat_django.WeChatApp'),
        ),
        migrations.AddField(
            model_name='messagelog',
            name='user',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='wechat_django.WeChatUser'),
        ),
        migrations.AddField(
            model_name='messagehandler',
            name='app',
            field=models.ForeignKey(editable=False, on_delete=django.db.models.deletion.CASCADE, related_name='message_handlers', to='wechat_django.WeChatApp'),
        ),
        migrations.AddField(
            model_name='menu',
            name='app',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='menus', to='wechat_django.WeChatApp'),
        ),
        migrations.AddField(
            model_name='menu',
            name='parent',
            field=models.ForeignKey(blank=True, null=True, on_delete=django.db.models.deletion.CASCADE, related_name='sub_button', to='wechat_django.Menu'),
        ),
        migrations.AddField(
            model_name='material',
            name='app',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='materials', to='wechat_django.WeChatApp'),
        ),
        migrations.AddField(
            model_name='article',
            name='material',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='articles', to='wechat_django.Material'),
        ),
        migrations.AlterUniqueTogether(
            name='wechatuser',
            unique_together=set([('app', 'unionid'), ('app', 'openid')]),
        ),
        migrations.AlterIndexTogether(
            name='messagelog',
            index_together=set([('app', 'created_at')]),
        ),
        migrations.AlterIndexTogether(
            name='messagehandler',
            index_together=set([('app', 'weight', 'created_at')]),
        ),
        migrations.AlterUniqueTogether(
            name='material',
            unique_together=set([('app', 'media_id')]),
        ),
        migrations.AlterUniqueTogether(
            name='article',
            unique_together=set([('material', 'index')]),
        ),
    ]
