package com.team18.studybuddy.studybuddy;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Zeb on 10/22/2015.
 */
public class JsonMotto {

    public UserData getInfo(String content) throws JSONException {

        JSONObject jsonResponse = new JSONObject(content);
        JSONArray jsonArray = jsonResponse.getJSONArray("fields");

        UserData ud = null;

        for (int i = 0; i < jsonArray.length(); i++) {
           JSONObject r = jsonArray.getJSONObject(i);
            JSONArray interestArray = r.getJSONArray("interests");
            JSONArray coursesArray = r.getJSONArray("courses");
            String [] inter = new String[interestArray.length()];
            String [][] cour = new String[coursesArray.length()][2];

            for (int j = 0; j < interestArray.length(); j++) {
                JSONObject rinterests = jsonArray.getJSONObject(j);
                inter[j] = rinterests.getString("name");
            }
            for (int k = 0; k < coursesArray.length(); k++) {
                JSONObject rcourses = jsonArray.getJSONObject(k);
                cour[k][0] = rcourses.getString("name");
                cour[k][1] = rcourses.getString("title");
            }

           ud = new UserData(r.getString("Username"), r.getString("Bio"), inter, cour,r.getString("year"));
        }
        return ud;
    }

}
