package com.team18.studybuddy.studybuddy;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Alpaca on 11/13/2015.
 */
public class Interests extends Activity {

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    ListView interestField;
    String CUR_USERNAME;
    UserData currentUser;
    String interestName;


    public void updateCurrentUser() throws ExecutionException, InterruptedException {
        Object params[] = new Object[3];
        params[0] = "getUserInfo";
        params[1] = CUR_USERNAME;
        currentUser = new RetrieveMotto().execute(params).get();
    }
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_interests);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            CUR_USERNAME = extras.getString("Username");
        }

        interestField = (ListView) findViewById(R.id.interestList);
        try {
            updateCurrentUser();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        final DelayAutoCompleteTextView interestTextView = (DelayAutoCompleteTextView) findViewById(R.id.interestComplete);
        interestTextView.setThreshold(0);
        interestTextView.setAdapter(new InterestAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1)); // 'this' is Activity instance



        interestTextView.setLoadingIndicator((android.widget.ProgressBar) findViewById(R.id.pb_loading_indicator));

        interestTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                interestName = (String) adapterView.getItemAtPosition(position);
                interestTextView.setText(interestName);
            }
        });
        interestTextView.setText(" ");
        interestTextView.setText("");


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showInterestText(currentUser.getInterests());
            }
        }, 2000);

        Button addCourse = (Button) findViewById(R.id.addInterest);
        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interestName = interestTextView.getText().toString();
                Object params[] = new Object[2];
                params[0] = "addInterest";
                params[1] = interestName;

                Object params2[] = new Object[3];
                params2[0] = "addUserInterest";
                params2[1] = CUR_USERNAME;
                params2[2] = interestName;

                try {
                    boolean addCourseSuccess = new AddInterestMotto().execute(params).get();
                    boolean addUserCourseSuccess = new AddInterestMotto().execute(params2).get();
                    updateCurrentUser();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showInterestText(currentUser.getInterests());
                            interestTextView.setText(" ");

                        }
                    }, 2000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }

        });
        interestField.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final String selectedInterest = (String) parent.getItemAtPosition(position);
                Object params[] = new Object[3];
                params[0] = "removeUserInterest";
                params[1] = CUR_USERNAME;
                params[2] = selectedInterest;
                try {
                    final boolean removeUserCourseSuccess = new AddInterestMotto().execute(params).get();
                    updateCurrentUser();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.toast,(ViewGroup) findViewById(R.id.custom_toast_layout));
                            TextView text = (TextView) layout.findViewById(R.id.textToShow);
                            text.setText("Removed " + selectedInterest + " ");
                            Toast toast = new Toast(getApplicationContext());
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setView(layout);
                            toast.show();

                            showInterestText(currentUser.getInterests());
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

    }
    public void showInterestText(String[] testToShow) {
        ArrayList<String> items = new ArrayList<String>();
        for (int i = 0; i < testToShow.length; i++) {
            items.add(i, testToShow[i]);
        }
        ArrayAdapter<String> adap = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        interestField.setAdapter(adap);
    }

}
