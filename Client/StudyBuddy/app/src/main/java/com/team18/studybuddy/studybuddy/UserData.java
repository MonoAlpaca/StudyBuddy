package com.team18.studybuddy.studybuddy;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Zeb on 10/22/2015.
 */
public class UserData {

    String username, bio, year;
    String[] interests;
    String[][] courses;
    public UserData (String username, String bio, String[] interests, String[][] courses, String year) {
        this.username = username;
        this.bio = bio;
        this.year = year;
        this.interests = interests;
        this.courses = courses;

    }


}
