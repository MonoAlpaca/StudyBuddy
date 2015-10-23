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
        JSONObject r = jsonResponse.getJSONObject("fields");

        UserData ud = null;

        JSONArray interestArray = r.getJSONArray("interests");
        JSONArray coursesArray = r.getJSONArray("courses");
        String[] inter = new String[interestArray.length()];
        String[][] cour = new String[coursesArray.length()][2];

        for (int j = 0; j < interestArray.length(); j++) {
            inter[j] = interestArray.getString(j);
        }
        for (int k = 0; k < coursesArray.length(); k++) {
            JSONArray rcourses = coursesArray.getJSONArray(k);

            cour[k][0] = rcourses.getString(0);
            cour[k][1] = rcourses.getString(1);
        }

        ud = new UserData(r.getString("username"), r.getString("bio"), inter, cour, r.getString("year"));
        return ud;
    }

}
