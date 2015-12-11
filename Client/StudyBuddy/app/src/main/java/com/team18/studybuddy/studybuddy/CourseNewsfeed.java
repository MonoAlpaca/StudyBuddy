package com.team18.studybuddy.studybuddy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

/**
 * Created by Alpaca on 12/11/2015.
 */


public class CourseNewsfeed extends Activity {
    String CUR_COURSE, CUR_USERNAME;
    ListView lvFeed;
    ArrayList<Message> mFeeds;
    private FeedListAdapter mAdapter;
    Context mContext;
    private EditText etMessage;
    private TextView feedCouseName;
    private Button btSend;
    private SwipeRefreshLayout swipeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mContext = this;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            CUR_COURSE = extras.getString("course");
            CUR_USERNAME = extras.getString("username");
        }
        setContentView(R.layout.tab_newfeed);
        feedCouseName = (TextView) findViewById(R.id.feedCouseName);
        lvFeed = (ListView) findViewById(R.id.lvFeed);
        mFeeds = new ArrayList<Message>();

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeResources(R.color.black_color, R.color.old_gold);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
                swipeLayout.setRefreshing(false);
            }
        });
        refreshContent();


        feedCouseName.setText(CUR_COURSE);

        etMessage = (EditText) findViewById(R.id.etMessage);
        btSend = (Button) findViewById(R.id.btSend);
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
                params[2] = CUR_COURSE;
                params[3] = message.getBody();
                try {
                    Log.d("ChatSessionFrag", "Esta AQUIIIIIII");
                    new ChatMotto().execute(params).get();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                etMessage.setText("");
                swipeLayout.setRefreshing(true);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshContent();
                        swipeLayout.setRefreshing(false);
                    }

                }, 1000);
            }


        });

    }

    public void refreshContent() {
        Object params[] = new Object[3];
        params[0] = "getGroupMessages";
        params[1] = CUR_COURSE;
        params[2] = "newsfeed";
        try {
            mFeeds = new ChatMotto().execute(params).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mAdapter = new FeedListAdapter(mContext, "neverExisting", mFeeds);

                lvFeed.setAdapter(mAdapter);

                lvFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Message currentMessage = (Message) parent.getItemAtPosition(position);
                        Intent j = new Intent(mContext, Profile.class);
                        j.putExtra("other", currentMessage.getUserId());
                        startActivity(j);
                    }

                });
            }
        }, 2000);
    }
}
