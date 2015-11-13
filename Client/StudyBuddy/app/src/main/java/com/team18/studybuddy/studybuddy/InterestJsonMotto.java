package com.team18.studybuddy.studybuddy;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Alpaca on 11/13/2015.
 */
public class InterestJsonMotto {
    public ArrayList<String> getInterests(String content) throws JSONException {
        ArrayList<String> mMessages = new ArrayList<String>();

        JSONArray chatArray = new JSONArray(content);
        for (int j = 0; j < chatArray.length(); j++) {
            String interestName =  (chatArray.getJSONObject(j).getJSONObject("fields").getString("name"));
            mMessages.add(interestName);
        }
        return mMessages;

    }
}
