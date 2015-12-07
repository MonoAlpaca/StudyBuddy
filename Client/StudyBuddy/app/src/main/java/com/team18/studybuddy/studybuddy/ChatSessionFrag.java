package com.team18.studybuddy.studybuddy;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by Nathan on 10/16/2015.
 */

public class ChatSessionFrag extends Activity {
    private ListView lvChat;
    private ArrayList<Message> mMessages;
    private ChatListAdapter mAdapter;
    private EditText etMessage;
    private Button btSend;
    // Keep track of initial load to scroll to the bottom of the ListView
    private boolean mFirstLoad;
    private String CUR_USERNAME;
    private String CUR_OTHER;


    private Handler handler = new Handler();
    private void scrollMyListViewToBottom() {
        lvChat.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                lvChat.setSelection(lvChat.getCount() - 1);
            }
        });
    }

    private void setupMessagePosting() {


        etMessage = (EditText) findViewById(R.id.etMessage);
        btSend = (Button) findViewById(R.id.btSend);
        lvChat = (ListView) findViewById(R.id.lvChat);
        mMessages = new ArrayList<Message>();
        // Automatically scroll to the bottom when a data set change notification is received and only if the last item is already visible on screen. Don't scroll to the bottom otherwise.
        lvChat.setTranscriptMode(1);
        mFirstLoad = true;
        mAdapter = new ChatListAdapter(ChatSessionFrag.this, CUR_USERNAME, mMessages);
        lvChat.setAdapter(mAdapter);

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String body = etMessage.getText().toString();
                // Use Message model to create new messages now
                Message message = new Message();
                message.setUserId(CUR_USERNAME);
                message.setBody(body);
                Object params[] = new Object[4];
                params[0] = "addMessage";
                params[1] = CUR_USERNAME;
                params[2] = CUR_OTHER;
                params[3] = message.getBody();
                try {
                    Log.d("ChatSessionFrag", "Esta AQUIIIIIII");
                    new ChatMotto().execute(params).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                etMessage.setText("");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            CUR_USERNAME = extras.getString("me");
            CUR_OTHER = extras.getString("person");
        }
        setContentView(R.layout.fragment_chat_session);
        setupMessagePosting();
        handler.postDelayed(runnable, 2333);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            refreshMessages();
            handler.postDelayed(this, 2333);
        }
    };

    protected void onStart() {
        super.onStart();
        handler = new Handler();
        handler.post(runnable);

    }
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }
    private void refreshMessages() {
        receiveMessage();
    }

    private void receiveMessage() {


        if(mMessages != null) {
            mMessages.clear();
        }
        Object params[];
        if(CUR_OTHER.contains(",")) {
            params = new Object[2];
            params[0] = "getGroupMessages";
            params[1] = CUR_OTHER;
        }else {
            params = new Object[3];
            params[0] = "getMessages";
            params[1] = CUR_USERNAME;
            params[2] = CUR_OTHER;
        }
        try {
            ArrayList<Message> newMessages = new ChatMotto().execute(params).get();
            scrollMyListViewToBottom();
            if(newMessages != null) {
                mMessages.addAll(newMessages);
                if(newMessages.size()-1 > 0) {
                    mAdapter.notifyDataSetChanged();
                    String name = newMessages.get(newMessages.size() - 1).getUserId();
                    if (!name.equals(CUR_USERNAME)) {
                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                        scrollMyListViewToBottom();
                        if (pref.getBoolean("notificationSettings", true)) {
                            Intent intent = new Intent(this, Chat.class);
                            PendingIntent pending = PendingIntent.getActivity(this, 0, intent, 0);

                            NotificationCompat.Builder notifications = new NotificationCompat.Builder(this)
                                    .setContentTitle("StudyBuddy")
                                    .setContentText("New Message")
                                    .setSmallIcon(R.drawable.mail_icon)
                                    .setContentIntent(pending);

                            NotificationManager notice = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                            notice.notify(0, notifications.build());
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
