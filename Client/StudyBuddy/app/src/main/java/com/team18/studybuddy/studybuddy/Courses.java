package com.team18.studybuddy.studybuddy;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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


        final DelayAutoCompleteTextView subjectTextView = (DelayAutoCompleteTextView) findViewById(R.id.subjectComplete);
        subjectTextView.setThreshold(2);
        subjectTextView.setAdapter(new SuggestionAdapter(this, R.layout.simple_dropdown_item_2line, R.id.text1)); // 'this' is Activity instance
        subjectTextView.setLoadingIndicator(
                (android.widget.ProgressBar) findViewById(R.id.pb_loading_indicator));
        subjectTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                SuggestGetSet courseName = (SuggestGetSet) adapterView.getItemAtPosition(position);
                if (JsonParse.isAlpha(courseName.getId())) {
                    subjectTextView.setText((courseName.getId()) + " ");
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            subjectTextView.showDropDown();
                        }
                    }, 1000);
                } else {
                    subjectTextView.setText((courseName.getId()));

                }
                subjectTextView.setSelection(subjectTextView.getText().length());
            }
        });
        //
        /*final AutoCompleteTextView subjectTextView = (AutoCompleteTextView) findViewById(R.id.subjectComplete);
        subjectTextView.setAdapter(new SuggestionAdapter(this, R.layout.simple_dropdown_item_2line, R.id.text1));*/


    }


}
