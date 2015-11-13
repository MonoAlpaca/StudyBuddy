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
public class AddInterestMotto extends AsyncTask<Object, Void, Boolean> {

    private static final String TAG = "ADDINTERESTMOTTO";
    public String token;
    public String courseID;
    public String courseName;


    private String removeUserInterestBuilder() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("gmmotto.ddns.net")
                .appendPath("app")
                .appendPath(token)
                .appendQueryParameter("username", courseID)
                .appendQueryParameter("interest_name", courseName);

        Log.d(TAG, "URI: " + builder.toString());
        return builder.toString();

    }

    private String addInterestBuilder() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("gmmotto.ddns.net")
                .appendPath("app")
                .appendPath(token)
                .appendQueryParameter("interest_name", courseID);

        Log.d(TAG, "URI: " + builder.toString());
        return builder.toString();

    }

    private String addUserInterestBuilder() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("gmmotto.ddns.net")
                .appendPath("app")
                .appendPath(token)
                .appendQueryParameter("username", courseID)
                .appendQueryParameter("interest_name", courseName);

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
        if(token.equals("addUserInterest") || token.equals("removeUserInterest")) {

            courseName = (String) params[2];
        }

        StringBuilder content = new StringBuilder();
        Log.d(TAG, "In Retrieve Motto");
        URL js = null;
        try {
            if (token.equals("addUserInterest")) {
                js = new URL(addUserInterestBuilder());

            } else if (token.equals("addInterest")) {
                js = new URL(addInterestBuilder());
            } else if (token.equals("removeUserInterest")) {
                js = new URL(removeUserInterestBuilder());


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

        if (content.toString().equals("Successfully edited user")) {
            Log.d(TAG, "true");

            return true;
        }
        return false;
    }


}
