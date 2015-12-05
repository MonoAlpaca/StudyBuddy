package com.team18.studybuddy.studybuddy;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ch.boye.httpclientandroidlib.client.HttpClient;

/**
 * Created by Zeb on 10/15/2015.
 */
public class Courses extends Activity {

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    ListView courseField;
    String CUR_USERNAME;
    UserData currentUser;
    SuggestGetSet courseName;


    public void updateCurrentUser() throws ExecutionException, InterruptedException {
        Object params[] = new Object[3];
        params[0] = "getUserInfo";
        params[1] = CUR_USERNAME;
        currentUser = new RetrieveMotto().execute(params).get();
    }
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_courses);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            CUR_USERNAME = extras.getString("Username");
        }

        courseField = (ListView) findViewById(R.id.courseList);
        try {
            updateCurrentUser();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        final DelayAutoCompleteTextView subjectTextView = (DelayAutoCompleteTextView) findViewById(R.id.subjectComplete);
        subjectTextView.setThreshold(0);
        subjectTextView.setAdapter(new SuggestionAdapter(this, R.layout.simple_dropdown_item_2line, R.id.text1)); // 'this' is Activity instance



        subjectTextView.setLoadingIndicator((android.widget.ProgressBar) findViewById(R.id.pb_loading_indicator));

        subjectTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                courseName = (SuggestGetSet) adapterView.getItemAtPosition(position);
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
        subjectTextView.setText(" ");
        subjectTextView.setText("");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showCourseText(currentUser.getCourses());
            }
        }, 2000);

        Button addCourse = (Button) findViewById(R.id.addCourse);
        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object params[] = new Object[3];
                params[0] = "addCourse";
                params[1] = courseName.getId();
                params[2] = courseName.getName();

                Object params2[] = new Object[3];
                params2[0] = "addUserCourse";
                params2[1] = CUR_USERNAME;
                params2[2] = courseName.getId();

                try {
                    boolean addCourseSuccess = new AddCourseMotto().execute(params).get();
                    boolean addUserCourseSuccess = new AddCourseMotto().execute(params2).get();
                    updateCurrentUser();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showCourseText(currentUser.getCourses());
                            subjectTextView.setText(" ");

                        }
                    }, 2000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }

        });
        courseField.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final UserClasses selectedCourse = (UserClasses) parent.getItemAtPosition(position);
                Object params[] = new Object[3];
                params[0] = "removeUserCourse";
                params[1] = CUR_USERNAME;
                params[2] = selectedCourse.getName();
                try {
                    final boolean removeUserCourseSuccess = new AddCourseMotto().execute(params).get();
                    updateCurrentUser();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.toast,(ViewGroup) findViewById(R.id.custom_toast_layout));
                            TextView text = (TextView) layout.findViewById(R.id.textToShow);
                            text.setText("Removed " + selectedCourse.getName() + " ");
                            Toast toast = new Toast(getApplicationContext());
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setView(layout);
                            toast.show();
                            //Toast.makeText(Courses.this, "Course removed!: " + selectedCourse.getName(), Toast.LENGTH_SHORT).show();

                            showCourseText(currentUser.getCourses());
                        }
                    }, 2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }



                return true;
            }
        });

        //
        /*final AutoCompleteTextView subjectTextView = (AutoCompleteTextView) findViewById(R.id.subjectComplete);
        subjectTextView.setAdapter(new SuggestionAdapter(this, R.layout.simple_dropdown_item_2line, R.id.text1));*/
    }
    public void showCourseText(String[][] testToShow) {
        ArrayList<UserClasses> uc = new ArrayList<UserClasses>();

        UserClasses uc2;

        for (int i = 0; i < testToShow.length; i++) {
            uc2 = new UserClasses(testToShow[i][0], testToShow[i][1]);
            uc.add(uc2);
        }
        if(courseField == null) {
            Log.d("PROFILEFRAG", "courseField empty");
        }
        courseField.setAdapter(new MyAdapter(this, uc));
    }


}
