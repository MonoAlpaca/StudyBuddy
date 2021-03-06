package com.team18.studybuddy.studybuddy;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alpaca on 12/11/2015.
 */
public class FeedListAdapter extends ArrayAdapter<Message> {
    private String mUserId;
    private List<Message> messages;

    public FeedListAdapter(Context context, String userId, List<Message> messages) {
        super(context, 0, messages);
        this.messages = messages;
        this.mUserId = userId;
    }

    public Message getItem(int position){
        return messages.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.feed_item, parent, false);
            final ViewHolder holder = new ViewHolder();
            holder.imageLeft = (ImageView)convertView.findViewById(R.id.feedImage);
            holder.feedName = (TextView)convertView.findViewById(R.id.feedUserID);;
            holder.body = (TextView)convertView.findViewById(R.id.feedUserMessage);
            convertView.setTag(holder);
        }
        final Message message = getItem(position);
        final ViewHolder holder = (ViewHolder)convertView.getTag();
        // Show-hide image based on the logged-in user.
        // Display the profile image to the right for our user, left for other users.
        final ImageView profileView = holder.imageLeft;
        Picasso.with(getContext()).load(getProfileUrl(message.getUserId())).into(profileView);
        holder.feedName.setText(message.getUserId());
        holder.body.setText(message.getBody());
        return convertView;
    }

    // Create a gravatar image based on the hash value obtained from userId
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

    final class ViewHolder {
        public ImageView imageLeft;
        public TextView feedName;
        public TextView body;
    }
}
