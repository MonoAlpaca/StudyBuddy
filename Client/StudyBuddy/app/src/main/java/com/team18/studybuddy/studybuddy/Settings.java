package com.team18.studybuddy.studybuddy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Nathan on 10/10/2015.
 */
public class Settings extends PreferenceActivity {
    String CUR_USERNAME;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_settings);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            CUR_USERNAME = extras.getString("username");
        }
        final EditTextPreference dialog = (EditTextPreference) findPreference("blockList");
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
                                Toast.makeText(Settings.this, "Block Success: " + blockedName, Toast.LENGTH_SHORT).show();

                            }else {
                                Toast.makeText(Settings.this, "Block Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },2000);
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
