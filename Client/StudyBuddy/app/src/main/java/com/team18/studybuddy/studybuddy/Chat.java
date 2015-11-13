package com.team18.studybuddy.studybuddy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Nathan on 10/15/2015.
 */
public class Chat extends Fragment{
    private ListView chatList;
    private ArrayList<Message> mMessages;
    private ChatListAdapter mAdapter;

    String CUR_USERNAME;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Chat newInstance(int sectionNumber, String username) {
        Chat fragment = new Chat();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putString("username", username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView;
        Bundle args = getArguments();
        CUR_USERNAME = args.getString("username");
        rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        chatList = (ListView) rootView.findViewById(R.id.chatList);
        Object params[] = new Object[2];
        params[0] = "getChatList";
        params[1] = CUR_USERNAME;
        try {
            mMessages = new ChatMotto().execute(params).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter = new ChatListAdapter(rootView.getContext(), CUR_USERNAME, mMessages);
                chatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Message currentMessage = (Message) parent.getItemAtPosition(position);
                        Intent j = new Intent(rootView.getContext(), ChatSessionFrag.class);
                        j.putExtra("me", CUR_USERNAME);
                        j.putExtra("person", currentMessage.getUserId());
                        startActivity(j);
                    }
                });
                chatList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                                        @Override
                                                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                                            Message currentMessage = (Message) parent.getItemAtPosition(position);
                                                            Intent j = new Intent(rootView.getContext(), Profile.class);
                                                            j.putExtra("other", currentMessage.getUserId());
                                                            startActivity(j);
                                                            return true;
                                                        }
                                                    }
                );
                chatList.setAdapter(mAdapter);
            }
        }, 2000);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}
