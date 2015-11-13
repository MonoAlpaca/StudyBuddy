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
 * Created by Alpaca on 11/12/2015.
 */
public class ChatMotto extends AsyncTask<Object, Void, ArrayList<Message>> {

    private static final String TAG = "RETRIEVEMOTTO";
    public String token;
    public String username;
    public String othername;
    public String message;


    private String getBuilder(){
        Uri.Builder builder  = new Uri.Builder();
        builder.scheme("http")
                .authority("gmmotto.ddns.net")
                .appendPath("app")
                .appendPath(token)
                .appendQueryParameter("p1", username)
                .appendQueryParameter("p2", othername);
        Log.d(TAG, "URI: " + builder.toString());
        return builder.toString();

    }
    private String addBuilder(){
        Uri.Builder builder  = new Uri.Builder();
        builder.scheme("http")
                .authority("gmmotto.ddns.net")
                .appendPath("app")
                .appendPath(token)
                .appendQueryParameter("sender", username)
                .appendQueryParameter("receiver", othername)
                .appendQueryParameter("content", message);

        Log.d(TAG, "URI: " + builder.toString());
        return builder.toString();

    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected ArrayList<Message> doInBackground(Object... params) {
        token = (String) params[0];
        username = (String) params[1];
        othername = (String) params[2];
        if(token.equals("addMessage")) {
            message = (String) params[3];

        }

        StringBuilder content = new StringBuilder();
        Log.d(TAG, "In Retrieve Motto");
        URL js = null;
        try {
            if(token.equals("addMessage")) {
                js = new URL(addBuilder());
            }else if(token.equals("getMessages")) {
                js = new URL(getBuilder());

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

        ChatJsonMotto getMessages = new ChatJsonMotto();
        Log.d(TAG, content.toString());
        ArrayList<Message> currentUser = null;

        try {
            currentUser = getMessages.getMessages(content.toString());
        } catch (JSONException e) {
            Log.d(TAG, "Json Inccorrect");
            e.printStackTrace();
        }


        return currentUser;
    }

}
