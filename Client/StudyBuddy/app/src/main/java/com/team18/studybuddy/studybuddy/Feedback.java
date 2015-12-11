package com.team18.studybuddy.studybuddy;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.net.Uri;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Nathan on 10/15/2015.
 */
public class Feedback extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private Activity activity1 = this.getActivity();

    View rootView;
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Feedback newInstance(int sectionNumber) {
        Feedback fragment = new Feedback();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_feedback, container, false);
        Button sendBtn = (Button) rootView.findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendEmail();
            }
        });

        return rootView;
    }


    protected void sendEmail() {

        String[] recipient = { "studybuddy307@gmail.com" };
        Intent email = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
        // prompts email clients only
        email.setType("message/rfc822");

        email.putExtra(Intent.EXTRA_EMAIL, recipient);
        email.putExtra(Intent.EXTRA_SUBJECT, "Feedback for StudyBuddy");

        try {
            // the user can choose the email client
            startActivity(Intent.createChooser(email, "Choose your email client"));

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(activity1, "No email client installed.",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

}
