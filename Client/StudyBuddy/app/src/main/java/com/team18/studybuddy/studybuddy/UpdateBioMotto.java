package com.team18.studybuddy.studybuddy;

/**
 * Created by Zeb on 11/12/2015.
 */

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


import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import ch.boye.httpclientandroidlib.client.HttpClient;

/**
 * Created by Alpaca on 10/18/2015.
 */

//http://gmmotto.ddns.net/app/getUserInfo?username=mill1630


public class UpdateBioMotto extends AsyncTask<Object, Void, Boolean> {

    private static final String TAG = "UPDATEBIOMOTTO";
    public String token;
    public String username;
    public String content;


    private String URLBuilder(){
        Uri.Builder builder  = new Uri.Builder();
        builder.scheme("http")
                .authority("gmmotto.ddns.net")
                .appendPath("app")
                .appendPath(token)
                .appendQueryParameter("username", username)
                .appendQueryParameter("bio", content);

        Log.d(TAG, "URI: " + builder.toString());
        return builder.toString();

    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Boolean doInBackground(Object... params) {
        token = (String) params[0];
        username = (String) params[1];
        content = (String) params[2];

        StringBuilder content = new StringBuilder();
        Log.d(TAG, "In Retrieve Motto");
        URL js = null;
        try {
            js = new URL(URLBuilder());

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

