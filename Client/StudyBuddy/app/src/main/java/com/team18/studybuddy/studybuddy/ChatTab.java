package com.team18.studybuddy.studybuddy;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * Created by Alpaca on 12/11/2015.
 */
public class ChatTab extends TabActivity {
    String CUR_COURSE,CUR_USERNAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            CUR_COURSE = extras.getString("course");
            CUR_USERNAME = extras.getString("username");
        }

        TabHost.TabSpec tab1 = tabHost.newTabSpec("Students");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Newsfeed");

        tab1.setIndicator("Students");
        Intent coursePage = new Intent(this,CoursePage.class);
        coursePage.putExtra("course", CUR_COURSE);
        coursePage.putExtra("username", CUR_USERNAME);
        tab1.setContent(coursePage);

        tab2.setIndicator("Newsfeed");
        Intent courseNewsfeed = new Intent(this,CourseNewsfeed.class);
        courseNewsfeed.putExtra("course", CUR_COURSE);
        courseNewsfeed.putExtra("username", CUR_USERNAME);
        tab2.setContent(courseNewsfeed);


        tabHost.addTab(tab1);
        tabHost.addTab(tab2);


    }
}
