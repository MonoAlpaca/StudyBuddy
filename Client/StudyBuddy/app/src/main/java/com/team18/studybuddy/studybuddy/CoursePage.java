package com.team18.studybuddy.studybuddy;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Zeb on 10/15/2015.
 */
public class CoursePage extends Activity {

    String CUR_USERNAME;
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    String CUR_COURSE;
    private ArrayList<Message> mUsers;
    ListView courseUser;
    private ChatListAdapter mAdapter;
    String OTHER_USERNAME;
    Context mContext;



    protected void onCreate(Bundle b) {
        mContext = this;
        super.onCreate(b);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            CUR_COURSE = extras.getString("course");
            CUR_USERNAME = extras.getString("username");
        }
        setContentView(R.layout.activity_coursepage);
        final TextView courseName = (TextView) findViewById(R.id.courseName);
        courseName.setText(CUR_COURSE);


        courseUser = (ListView) findViewById(R.id.courseUser);
        mUsers = new ArrayList<Message>();
        Object params[] = new Object[2];
        params[0] = "getUserByCourse";
        params[1] = CUR_COURSE;
        try {
            mUsers = new ChatMotto().execute(params).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mAdapter = new ChatListAdapter(mContext, "neverExisting", mUsers);

                courseUser.setAdapter(mAdapter);
                courseUser.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        Message currentMessage = (Message) parent.getItemAtPosition(position);

                        OTHER_USERNAME = currentMessage.getUserId();
                        Intent j = new Intent(CoursePage.this, ChatSessionFrag.class);
                        j.putExtra("me", CUR_USERNAME);

                        j.putExtra("person", OTHER_USERNAME);
                        startActivity(j);
                        return true;
                    }

                });
                courseUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Message currentMessage = (Message) parent.getItemAtPosition(position);
                        Intent j = new Intent(CoursePage.this, Profile.class);
                        j.putExtra("other", currentMessage.getUserId());
                        startActivity(j);
                    }

                });
            }
        }, 2000);



    }

}
