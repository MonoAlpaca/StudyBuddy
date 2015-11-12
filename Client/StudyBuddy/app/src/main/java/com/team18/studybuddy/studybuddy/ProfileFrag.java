package com.team18.studybuddy.studybuddy;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
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
        bioField.setText("Is this going to work????");
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
    }

    @Override
    public void showInterestText(String[] testToShow) {
        //interestField.setAdapter();

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
    }


}
