package com.team18.studybuddy.studybuddy;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Alpaca on 11/13/2015.
 */

public class InterestMotto extends AsyncTask<Object, Void, ArrayList<String>> {

    private static final String TAG = "INTERESTMOTTO";
    public String token;
    public String username;
    public String othername;
    public String message;

    private String getInterestBuilder() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("llama.bot.nu")
                .appendPath(token);
        Log.d(TAG, "URI: " + builder.toString());
        return builder.toString();

    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected ArrayList<String> doInBackground(Object... params) {
        token = (String) params[0];

        StringBuilder content = new StringBuilder();
        Log.d(TAG, "In Retrieve Motto");
        URL js = null;
        try {
            js = new URL(getInterestBuilder());

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

        ArrayList<String> currentUser = new ArrayList<>();

        InterestJsonMotto getInterest = new InterestJsonMotto();
        Log.d(TAG, content.toString());

        try {
            currentUser = getInterest.getInterests(content.toString());
        } catch (JSONException e) {
            Log.d(TAG, "Json Inccorrect");
            e.printStackTrace();
        }


        return currentUser;
    }

}

