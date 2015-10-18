package com.team18.studybuddy.studybuddy;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alpaca on 10/16/2015.
 */


public class JsonParse {

    private static final String TAG = "JSONPARSE";

    double current_latitude, current_longitude;

    public JsonParse() {
    }

    public JsonParse(double current_latitude, double current_longitude) {
        this.current_latitude = current_latitude;
        this.current_longitude = current_longitude;
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    public static boolean isAlpha(String str) {
        if(str.isEmpty()) {
            return true;
        }
        return str.matches("[a-zA-Z]+");
    }

    public List<SuggestGetSet> getParseJsonWCF(String sName) {
        List<SuggestGetSet> ListData = new ArrayList<SuggestGetSet>();
        StringBuilder content = new StringBuilder();
        String subject = "";

        try {
            //String temp=sName.replace(" ", "%20");
            URL js = new URL("http://api.purdue.io/odata/Subjects");
            if (!isAlpha(sName)) {
                subject = sName.replaceAll("[\\s+0-9]","");
                js = new URL("http://api.purdue.io/odata/Courses?%24filter=Subject/Abbreviation%20eq%20%27" + subject + "%27&%24orderby=Number%20asc");



            }
            URLConnection jc = js.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                Log.d(TAG, line);

                content.append(line);
            }
            reader.close();
            Log.d(TAG, content.toString());

            JSONObject jsonResponse = new JSONObject(content.toString());
            JSONArray jsonArray = jsonResponse.getJSONArray("value");

            String courseFull = subject;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject r = jsonArray.getJSONObject(i);
                if (!isAlpha(sName)) {
                    courseFull = subject.toUpperCase();
                    courseFull = courseFull + " " + r.getString("Number");
                    ListData.add(new SuggestGetSet(courseFull, r.getString("Title")));

                } else {

                    ListData.add(new SuggestGetSet(r.getString("Abbreviation"), r.getString("Name")));


                }
            }
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return ListData;

    }

}
