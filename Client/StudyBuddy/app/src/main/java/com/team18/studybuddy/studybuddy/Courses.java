package com.team18.studybuddy.studybuddy;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import ch.boye.httpclientandroidlib.client.HttpClient;

/**
 * Created by Zeb on 10/15/2015.
 */
public class Courses extends Activity {

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */

    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_courses);



        //
        final AutoCompleteTextView subjectTextView = (AutoCompleteTextView) findViewById(R.id.subjectComplete);
        subjectTextView.setAdapter(new SuggestionAdapter(this, R.layout.simple_dropdown_item_2line, R.id.text1));

        subjectTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                SuggestGetSet courseName = (SuggestGetSet) adapterView.getItemAtPosition(position);
                subjectTextView.setText((courseName.getId()));
            }
        });
    }


}
