package com.team18.studybuddy.studybuddy;

import java.io.*;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;


import ch.boye.httpclientandroidlib.client.HttpClient;
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;
import ch.boye.httpclientandroidlib.impl.client.DefaultRedirectStrategy;
import ch.boye.httpclientandroidlib.impl.client.HttpClientBuilder;

/**
 * Created by Alpaca on 10/15/2015.
 */
public class CASClient extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication);
        HttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new DefaultRedirectStrategy()).build();


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        Authentication c = new Authentication(httpClient,"https://www.purdue.edu/apps/account/cas/");
        try {
            c.login("", "chen1370", "12589Bar");
        } catch (CasAuthenticationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (CasProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
