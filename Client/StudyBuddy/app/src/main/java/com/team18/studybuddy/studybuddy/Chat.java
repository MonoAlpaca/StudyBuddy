package com.team18.studybuddy.studybuddy;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Nathan on 10/15/2015.
 */
public class Chat extends Fragment{
    private ListView chatList;
    private ArrayList<Message> mMessages;
    private ChatListAdapter mAdapter;
    private String OTHER_USERNAME;
    AlertDialog alertDialog;

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

    @TargetApi(Build.VERSION_CODES.M)
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
        LayoutInflater li = LayoutInflater.from(rootView.getContext());
        View promptsView = li.inflate(R.layout.prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                rootView.getContext());
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                OTHER_USERNAME = userInput.getText().toString();

                                if (OTHER_USERNAME.contains(",")) {
                                    List<String> groupOfPeople = Arrays.asList(OTHER_USERNAME.split("\\s*,\\s*"));

                                    Object params[] = new Object[4];
                                    params[0] = "createGroup";
                                    params[1] = OTHER_USERNAME;
                                    params[2] = CUR_USERNAME;
                                    params[3] = groupOfPeople;
                                    try {
                                        mMessages = new ChatMotto().execute(params).get();

                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    }
                                }

                                Intent j = new Intent(rootView.getContext(), ChatSessionFrag.class);
                                j.putExtra("me", CUR_USERNAME);

                                j.putExtra("person", OTHER_USERNAME);
                                startActivity(j);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        alertDialog = alertDialogBuilder.create();

        // show it

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                chatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Message currentMessage = (Message) parent.getItemAtPosition(position);
                        if(currentMessage.getUserId().equals("createNewChat")) {
                            alertDialog.show();
                        }else if (currentMessage.getUserId().equals("createNewGroupChat")) {
                            alertDialog.show();
                        }else{
                            OTHER_USERNAME = currentMessage.getUserId();
                            Intent j = new Intent(rootView.getContext(), ChatSessionFrag.class);
                            j.putExtra("me", CUR_USERNAME);

                            j.putExtra("person", OTHER_USERNAME);
                            startActivity(j);
                        }
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
                Message createGroupMessage = new Message();
                createGroupMessage.setUserId("createNewGroupChat");
                createGroupMessage.setBody("Click to create new group chat");
                mMessages.add(createGroupMessage);

                Message createMessage = new Message();
                createMessage.setUserId("createNewChat");
                createMessage.setBody("Click to create new chat");
                mMessages.add(createMessage);
                mAdapter = new ChatListAdapter(rootView.getContext(), CUR_USERNAME, mMessages);

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
