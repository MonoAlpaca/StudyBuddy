package com.team18.studybuddy.studybuddy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Nathan on 10/10/2015.
 */
public class Settings extends PreferenceActivity {
    String CUR_USERNAME;
    Context mContext;
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_settings);

        final ListPreference themePreference = (ListPreference) findPreference("themeSettings");
        themePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Toast toast = Toast.makeText(mContext, "App will reset!", Toast.LENGTH_SHORT);
                toast.show();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {


                        Intent mStartActivity = new Intent(mContext, CASClient.class);
                        int mPendingIntentId = 123456;
                        PendingIntent mPendingIntent = PendingIntent.getActivity(mContext, mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                        AlarmManager mgr = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                        System.exit(0);
                    }
                }, 1000);

                return true;
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            CUR_USERNAME = extras.getString("username");
        }
        final EditTextPreference dialog = (EditTextPreference) findPreference("blockList");
        dialog.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Button positive = (Button) dialog.getDialog().findViewById(android.R.id.button1);
                Button negative = (Button) dialog.getDialog().findViewById(android.R.id.button2);
                positive.setTextColor(Color.BLACK);
                negative.setTextColor(Color.BLACK);

                return false;
            }
        });

        dialog.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                final String blockedName = (String) newValue;


                Object param[] = new Object[3];
                param[0] = "addBlockedUser";
                param[1] = CUR_USERNAME;
                param[2] = blockedName;
                try {
                    final boolean blockedThings = new BlockMotto().execute(param).get();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(blockedThings){
                                LayoutInflater inflater = getLayoutInflater();
                                View layout = inflater.inflate(R.layout.toast,(ViewGroup) findViewById(R.id.custom_toast_layout));
                                TextView text = (TextView) layout.findViewById(R.id.textToShow);
                                text.setText("Block Success: " + blockedName);
                                Toast toast = new Toast(getApplicationContext());
                                toast.setDuration(Toast.LENGTH_SHORT);
                                toast.setView(layout);
                                toast.show();

                            }else {
                                LayoutInflater inflater = getLayoutInflater();
                                View layout = inflater.inflate(R.layout.toast,(ViewGroup) findViewById(R.id.custom_toast_layout));
                                TextView text = (TextView) layout.findViewById(R.id.textToShow);
                                text.setText("Block Error");
                                Toast toast = new Toast(getApplicationContext());
                                toast.setDuration(Toast.LENGTH_SHORT);
                                toast.setView(layout);
                                toast.show();

                            }
                        }
                    }, 2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                return true;

            }
        });

        /*
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String blockedName = pref.getString("blockList", "Error");
        ArrayList<String> allUsers = new ArrayList<String>(userData);

        pref.edit();*/


    }


}
