package com.team18.studybuddy.studybuddy;

import java.io.*;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import ch.boye.httpclientandroidlib.client.HttpClient;
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;
import ch.boye.httpclientandroidlib.impl.client.DefaultRedirectStrategy;
import ch.boye.httpclientandroidlib.impl.client.HttpClientBuilder;

/**
 * Created by Alpaca on 10/15/2015.
 */
public class CASClient extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/OpenSans-Light.ttf");
        TypefaceUtil.overrideFont(getApplicationContext(), "MONOSPACE", "fonts/OpenSans-Light.ttf");
        TypefaceUtil.overrideFont(getApplicationContext(), "SANS", "fonts/OpenSans-Light.ttf");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication);
        HttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new DefaultRedirectStrategy()).build();


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        final Authentication c = new Authentication(httpClient, "https://www.purdue.edu/apps/account/cas/");

        StrictMode.setThreadPolicy(policy);
        final Button button = (Button) findViewById(R.id.loginButton);

        final EditText username = (EditText) findViewById(R.id.purdueEmail);
        final EditText password = (EditText) findViewById(R.id.purduePassword);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.loginLoad);
        button.setOnClickListener(new View.OnClickListener() {
                                      public void onClick(View v) {
                                                  progressBar.setVisibility(View.VISIBLE);
                                                  button.setVisibility(View.INVISIBLE);
                                          InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                          imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                                          new Handler().postDelayed(new Runnable() {

                                              @Override
                                              public void run() {
                                                  try {
                                                      String serviceTag = c.login("https://wl.mypurdue.purdue.edu", username.getText().toString(), password.getText().toString());
                                                      if (!serviceTag.isEmpty()) {
                                                          finish();

                                                          Intent main = new Intent(CASClient.this, MainActivity.class);
                                                          main.putExtra("Username", username.getText().toString());
                                                          main.putExtra("ServiceTag", serviceTag);
                                                          startActivity(main);
                                                      }

                                                  } catch (CasAuthenticationException e) {
                                                      progressBar.setVisibility(View.GONE);
                                                      button.setVisibility(View.VISIBLE);
                                                      Toast.makeText(CASClient.this, "Incorrect Credentials, Check Username and Password again", Toast.LENGTH_SHORT).show();
                                                      // TODO Auto-generated catch block
                                                      e.printStackTrace();
                                                  } catch (CasProtocolException e) {
                                                      progressBar.setVisibility(View.GONE);
                                                      button.setVisibility(View.VISIBLE);
                                                      Toast.makeText(CASClient.this, "Network Error" + e.getMessage(), Toast.LENGTH_LONG).show();


                                                      // TODO Auto-generated catch block
                                                      e.printStackTrace();
                                                  }

                                              }

                                          }, 1000);

                                      }

                                  }

        );


        final Context mContext = this;
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {

                                               @Override
                                               public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                                   if (actionId == EditorInfo.IME_ACTION_SEND) {
                                                       InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                       imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                                                       button.performClick();

                                                       return true;
                                                   }
                                                   return false;
                                               }
                                           }

        );

    }

}
