package com.team18.studybuddy.studybuddy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Nathan on 11/12/2015.
 */
public class Profile extends Activity {

    private String CUR_USERNAME;
    private UserData currentUser;

    TextView bioField;
    TextView nameField;
    ListView interestField;
    ListView courseField;
    Button editBio;
    ProgressBar loadingBar;
    private static String getProfileUrl(final String userId) {
        String hex = "";
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] hash = digest.digest(userId.getBytes());
            final BigInteger bigInt = new BigInteger(hash);
            hex = bigInt.abs().toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "http://www.gravatar.com/avatar/" + hex + "?d=identicon";
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
        Bundle bundleOfJoy = getIntent().getExtras();
        if (bundleOfJoy != null) {
            CUR_USERNAME = bundleOfJoy.getString("other");
        }

        try {
            updateCurrentUser();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        editBio = (Button) findViewById(R.id.updateBio);
        editBio.setVisibility(View.GONE);
        bioField = (TextView) findViewById(R.id.profileBio);
        courseField = (ListView) findViewById(R.id.list);
        nameField = (TextView) findViewById(R.id.profileName);
        interestField = (ListView) findViewById(R.id.profileInterests);
        interestField.setEnabled(false);
        loadingBar = (ProgressBar) findViewById(R.id.loading);

        ImageView profileView = (ImageView) findViewById(R.id.profilePicture);
        Picasso.with(this).load(getProfileUrl(CUR_USERNAME)).into(profileView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bioField.setText(currentUser.getBio());
                showCourseText(currentUser.getCourses());
                nameField.setText(currentUser.getUsername());
                showInterestText(currentUser.getInterests());
            }
        }, 2000);
    }

    public void showInterestText(String[] testToShow) {
        ArrayList<String> items = new ArrayList<String>();
        for (int i = 0; i < testToShow.length; i++) {
            items.add(i, testToShow[i]);
        }
        ArrayAdapter<String> adap = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        interestField.setAdapter(adap);
    }

    public void showCourseText(String[][] testToShow) {
        ArrayList<UserClasses> uc = new ArrayList<UserClasses>();

        UserClasses uc2;

        for (int i = 0; i < testToShow.length; i++) {
            uc2 = new UserClasses(testToShow[i][0], testToShow[i][1]);
            uc.add(uc2);
        }
        if(courseField == null) {
            Log.d("PROFILEFRAG", "courseField empty");
        }
        courseField.setAdapter(new MyAdapter(this, uc));
    }

    public void updateCurrentUser() throws ExecutionException, InterruptedException {
        Object params[] = new Object[3];
        params[0] = "getUserInfo";
        params[1] = CUR_USERNAME;
        currentUser = new RetrieveMotto().execute(params).get();
    }
    public void onInterestsClick(View v){
        return;
    }

}