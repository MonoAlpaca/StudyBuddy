package com.team18.studybuddy.studybuddy;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Alpaca on 11/13/2015.
 */
public class AddCourseMotto extends AsyncTask<Object, Void, Boolean> {

    private static final String TAG = "ADDCOURSEMOTTO";
    public String token;
    public String courseID;
    public String courseName;


    private String removeUserCourseBuilder(){
        Uri.Builder builder  = new Uri.Builder();
        builder.scheme("http")
                .authority("llama.bot.nu")
                .appendPath(token)
                .appendQueryParameter("username", courseID)
                .appendQueryParameter("course_name", courseName);

        Log.d(TAG, "URI: " + builder.toString());
        return builder.toString();

    }
    private String addCourseBuilder(){
        Uri.Builder builder  = new Uri.Builder();
        builder.scheme("http")
                .authority("llama.bot.nu")
                .appendPath(token)
                .appendQueryParameter("course_name", courseID)
                .appendQueryParameter("course_title", courseName);

        Log.d(TAG, "URI: " + builder.toString());
        return builder.toString();

    }
    private String addUserCourseBuilder(){
        Uri.Builder builder  = new Uri.Builder();
        builder.scheme("http")
                .authority("llama.bot.nu")
                .appendPath(token)
                .appendQueryParameter("username", courseID)
                .appendQueryParameter("course_name", courseName);

        Log.d(TAG, "URI: " + builder.toString());
        return builder.toString();

    }
    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Boolean doInBackground(Object... params) {
        token = (String) params[0];
        courseID = (String) params[1];
        courseName = (String) params[2];

        StringBuilder content = new StringBuilder();
        Log.d(TAG, "In Retrieve Motto");
        URL js = null;
        try {
            if(token.equals("addUserCourse")) {
                js = new URL(addUserCourseBuilder());

            }else if(token.equals("addCourse")){
                js = new URL(addCourseBuilder());
            }else if (token.equals("removeUserCourse")) {
                js = new URL(removeUserCourseBuilder());


            }

            URLConnection jc = js.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                Log.d(TAG, line);
                content.append(line);
            }
            reader.close();


        } catch (MalformedURLException e) {
            Log.d(TAG, "Incorrect URL Builder");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, content.toString());

        if(content.toString().equals("Successfully edited user")) {
            Log.d(TAG, "true");

            return true;
        }
        return false;
    }

}
