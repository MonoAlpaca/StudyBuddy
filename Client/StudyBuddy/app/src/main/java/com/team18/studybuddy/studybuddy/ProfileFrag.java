package com.team18.studybuddy.studybuddy;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Zeb on 10/15/2015.
 */
public class ProfileFrag extends Fragment implements ISetTextInFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    TextView bioField;
    TextView nameField;
    ListView interestField;
    ListView courseField;
    ProgressBar loadingBar;


    View rootView;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
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

    public static ProfileFrag newInstance(int sectionNumber) {
        ProfileFrag fragment = new ProfileFrag();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        bioField = (TextView) rootView.findViewById(R.id.profileBio);
        courseField = (ListView)rootView.findViewById(R.id.list);
        nameField = (TextView) rootView.findViewById(R.id.profileName);
        interestField = (ListView) rootView.findViewById(R.id.profileInterests);
        loadingBar = (ProgressBar) rootView.findViewById(R.id.loading);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void enableProgess() {
        loadingBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void disableProgess() {
        loadingBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showBioText(String testToShow) {
        bioField.setText(testToShow);
    }

    @Override
    public void showNameText(String testToShow) {
        nameField.setText(testToShow);
        ImageView profileView = (ImageView) rootView.findViewById(R.id.profilePicture);
        Picasso.with(rootView.getContext()).load(getProfileUrl(testToShow)).into(profileView);
    }

    @Override
    public void showInterestText(String[] testToShow) {
        ArrayList<String> items = new ArrayList<String>();
        for (int i = 0; i < testToShow.length; i++) {
            items.add(i, testToShow[i]);
        }
        ArrayAdapter<String> adap = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, items);
        interestField.setAdapter(adap);
    }

    @Override
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
        courseField.setAdapter(new MyAdapter(rootView.getContext(), uc));
        courseField.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent coursePage = new Intent(rootView.getContext(), ChatTab.class);
                UserClasses selectedCourse = (UserClasses) parent.getItemAtPosition(position);
                coursePage.putExtra("course", selectedCourse.getName().toString());
                coursePage.putExtra("username", nameField.getText().toString());

                startActivity(coursePage);
            }
        });

    }


}
