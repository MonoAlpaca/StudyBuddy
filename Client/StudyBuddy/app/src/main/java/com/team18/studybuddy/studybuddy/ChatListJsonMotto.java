package com.team18.studybuddy.studybuddy;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Alpaca on 11/12/2015.
 */
public class ChatListJsonMotto {

    public ArrayList<Message> getMessages(String content) throws JSONException {
        ArrayList<Message> mMessages = new ArrayList<Message>();

        JSONArray chatArray = new JSONArray(content);
        for (int j = 0; j < chatArray.length(); j++) {
            Message nMessage = new Message();
            nMessage.setBody(chatArray.getJSONObject(j).getJSONObject("fields").getString("username"));
            nMessage.setUserId(chatArray.getJSONObject(j).getJSONObject("fields").getString("username"));
            mMessages.add(nMessage);

        }
        return mMessages;

    }

}

