package com.team18.studybuddy.studybuddy;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

/**
 * Created by Zeb on 10/15/2015.
 */
public class ProfileFrag extends Fragment implements ISetTextInFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    TextView bioField;
    TextView nameField;
    TextView interestField;
    TextView courseField;
    ProgressBar loadingBar;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ProfileFrag newInstance(int sectionNumber) {
        ProfileFrag fragment = new ProfileFrag();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;


    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;

        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        bioField = (TextView) rootView.findViewById(R.id.profileBio);
        bioField.setText("Is this going to work????");
        nameField = (TextView) rootView.findViewById(R.id.profileName);
        interestField = (TextView) rootView.findViewById(R.id.profileInterests);
        courseField = (TextView) rootView.findViewById(R.id.profileCourses);
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
    }

    @Override
    public void showInterestText(String testToShow) {
        interestField.setText(testToShow);
    }

    @Override
    public void showCourseText(String testToShow) {
        courseField.setText(testToShow);
    }
}
