package com.team18.studybuddy.studybuddy;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

/**
 * Created by Zeb on 10/15/2015.
 */
public class Courses extends Activity {

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    private static String[] courses = {"CS180", "CS250", "CS307", "EAPS100", "SPAN101"};

    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_courses);
        //
        ArrayAdapter<String> a = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, courses);
        AutoCompleteTextView tv = (AutoCompleteTextView) findViewById(R.id.courseView);
        tv.setAdapter(a);
        //
    }

}
