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
import java.util.List;

/**
 * Created by Alpaca on 11/12/2015.
 */
public class ChatMotto extends AsyncTask<Object, Void, ArrayList<Message>> {

    private static final String TAG = "RETRIEVEMOTTO";
    public String token;
    public String username;
    public String othername;
    public String groupname;
    public String message;
    public List<String> groupMembers;

    private String getChatBuilder(){
        Uri.Builder builder  = new Uri.Builder();
        builder.scheme("http")
                .authority("llama.bot.nu")
                .appendPath(token)
                .appendQueryParameter("user", username);
        Log.d(TAG, "URI: " + builder.toString());
        return builder.toString();

    }
    private String getUserByCourseBuilder(){
        Uri.Builder builder  = new Uri.Builder();
        builder.scheme("http")
                .authority("llama.bot.nu")
                .appendPath(token)
                .appendQueryParameter("course_list", username);
        Log.d(TAG, "URI: " + builder.toString());
        return builder.toString();

    }


    private String getBuilder(){
        Uri.Builder builder  = new Uri.Builder();
        builder.scheme("http")
                .authority("llama.bot.nu")
                .appendPath(token)
                .appendQueryParameter("p1", username)
                .appendQueryParameter("p2", othername);
        Log.d(TAG, "URI: " + builder.toString());
        return builder.toString();

    }
    private String addBuilder(){
        Uri.Builder builder  = new Uri.Builder();
        builder.scheme("http")
                .authority("llama.bot.nu")
                .appendPath(token)
                .appendQueryParameter("sender", username)
                .appendQueryParameter("receiver", othername)
                .appendQueryParameter("content", message);

        Log.d(TAG, "URI: " + builder.toString());
        return builder.toString();

    }

    private String createGroupBuilder(){
        Uri.Builder builder  = new Uri.Builder();
        builder.scheme("http")
                .authority("llama.bot.nu")
                .appendPath(token)
                .appendQueryParameter("name", groupname)
                .appendQueryParameter("user", username);

        Log.d(TAG, "URI: " + builder.toString());
        return builder.toString();
    }

    private String addUserToGroupBuilder(String member){
        Uri.Builder builder  = new Uri.Builder();
        builder.scheme("http")
                .authority("llama.bot.nu")
                .appendPath("addUserToGroup")
                .appendQueryParameter("name", groupname)
                .appendQueryParameter("user", member);

        Log.d(TAG, "URI: " + builder.toString());
        return builder.toString();
    }

    private String getGroupChatBuilder(){
        Uri.Builder builder  = new Uri.Builder();
        builder.scheme("http")
                .authority("llama.bot.nu")
                .appendPath("getGroupMessages")
                .appendQueryParameter("name", groupname);

        Log.d(TAG, "URI: " + builder.toString());
        return builder.toString();
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected ArrayList<Message> doInBackground(Object... params) {
        token = (String) params[0];
        if(token.equals("createGroup")) {
            username = (String) params[2];
            groupname = (String) params[1];
            groupMembers = (List<String>) params[3];

        }else if(token.equals("getGroupMessages")){
            groupname = (String) params[1];
        }
        else {
            username = (String) params[1];
            if (token.equals("getMessages") || token.equals("addMessage")) {
                othername = (String) params[2];
                if (token.equals("addMessage")) {
                    message = (String) params[3];

                }
            }
        }

        StringBuilder content = new StringBuilder();
        Log.d(TAG, "In Retrieve Motto");
        URL js = null;
        try {
            if(token.equals("addMessage")) {
                js = new URL(addBuilder());
            }else if(token.equals("getMessages")) {
                js = new URL(getBuilder());

            }else if(token.equals("getChatList")) {
                js = new URL(getChatBuilder());
            }else if(token.equals("createGroup")) {
                js = new URL(createGroupBuilder());
                URLConnection jc = js.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));

                // iterate via "for loop"
                for (int i = 0; i < groupMembers.size(); i++) {
                    js = new URL(addUserToGroupBuilder(groupMembers.get(i)));
                    reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
                    jc = js.openConnection();
                }
                return null;
            }else if(token.equals("getGroupMessages")){
                js = new URL(getGroupChatBuilder());
            }else if(token.equals("getUserByCourse")){
                js = new URL(getUserByCourseBuilder());
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
        ArrayList<Message> currentUser = new ArrayList<>();

        if(token.equals("getChatList") || token.equals("getUserByCourse")) {
             ChatListJsonMotto getMessages = new ChatListJsonMotto();
            Log.d(TAG, content.toString());

            try {
                currentUser = getMessages.getMessages(content.toString());
            } catch (JSONException e) {
                Log.d(TAG, "Json Inccorrect");
                e.printStackTrace();
            }

        }else {
            ChatJsonMotto getMessages = new ChatJsonMotto();
            Log.d(TAG, content.toString());

            try {
                currentUser = getMessages.getMessages(content.toString());
            } catch (JSONException e) {
                Log.d(TAG, "Json Inccorrect");
                e.printStackTrace();
            }

        }

        return currentUser;
    }

}
