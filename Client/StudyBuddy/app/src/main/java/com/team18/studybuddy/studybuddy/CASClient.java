package com.team18.studybuddy.studybuddy;

import java.io.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


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
        final Authentication c = new Authentication(httpClient,"https://www.purdue.edu/apps/account/cas/");

        StrictMode.setThreadPolicy(policy);
        final Button button = (Button) findViewById(R.id.loginButton);
        final EditText username = (EditText) findViewById(R.id.purdueEmail);
        final EditText password = (EditText) findViewById(R.id.purduePassword);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {

                    String serviceTag = c.login("https://wl.mypurdue.purdue.edu", username.getText().toString(),password.getText().toString());
                    if(!serviceTag.isEmpty()) {
                        Intent main = new Intent(CASClient.this, MainActivity.class);
                        startActivity(main);
                    }
                } catch (CasAuthenticationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (CasProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

    }
}
