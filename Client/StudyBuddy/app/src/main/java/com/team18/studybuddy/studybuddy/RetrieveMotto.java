package com.team18.studybuddy.studybuddy;

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


public class RetrieveMotto extends AsyncTask<Object, Void, UserData> {

    private static final String TAG = "RETRIEVEMOTTO";
    public String token;
    public String username;
    MainActivity main;


    private String URLBuilder(String token, String username){
        Uri.Builder builder  = new Uri.Builder();
        builder.scheme("http")
                .authority("llama.bot.nu")
                .appendPath(token)
                .appendQueryParameter("username", username);
        Log.d(TAG, "URI: " + builder.toString());
        return builder.toString();

    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected UserData doInBackground(Object... params) {
        token = (String) params[0];
        username = (String) params[1];

        StringBuilder content = new StringBuilder();
        Log.d(TAG, "In Retrieve Motto");
        URL js = null;
        try {
            js = new URL(URLBuilder(token,username));

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


        content.deleteCharAt(0);
        content.deleteCharAt(content.length()-1);
        JsonMotto getUserData = new JsonMotto();
        Log.d(TAG, content.toString());
        UserData currentUser = null;

        try {
            currentUser = getUserData.getInfo(content.toString());
        } catch (JSONException e) {
            Log.d(TAG, "Json Inccorrect");
            e.printStackTrace();
        }


        return currentUser;
    }

}
