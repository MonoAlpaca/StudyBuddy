package com.team18.studybuddy.studybuddy;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Alpaca on 11/13/2015.
 */

public class InterestAdapter extends BaseAdapter implements Filterable {

    protected static final String TAG = "INTERESTADAPTER";
    private List<String> interests;
    Context mContext;

    public InterestAdapter(Context context, int resource, int textViewResourceId) {
        mContext = context;
        interests = new ArrayList<String>();
    }

    @Override
    public int getCount() {
        return interests.size();
    }

    @Override
    public String getItem(int index) {
        return interests.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(android.R.layout.simple_list_item_1 , parent, false);
        }
        ((TextView) convertView.findViewById(android.R.id.text1)).setText(getItem(position));
        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                InterestMotto interestMotto = new InterestMotto();
                String constraintTemp;
                if (constraint != null) {
                    // A class that queries a web API, parses the data and
                    // returns an ArrayList<GoEuroGetSet>
                    Object params[] = new Object[1];
                    params[0] = "getInterestList";

                    List<String> new_suggestions = null;
                    try {
                        new_suggestions = interestMotto.execute(params).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, constraint.toString());
                    interests.clear();
                    for (int i = 0; i < new_suggestions.size(); i++) {
                        if (JsonParse.isAlpha(constraint.toString())) {
                            if (new_suggestions.get(i).toLowerCase().contains(constraint.toString().toLowerCase())) {
                                interests.add(new_suggestions.get(i));
                            }
                        } else {
                            constraintTemp = constraint.toString().replaceAll("[a-zA-Z]", "");
                            if (new_suggestions.get(i).toLowerCase().contains(constraintTemp.toLowerCase())) {

                                interests.add(new_suggestions.get(i));
                            }
                        }
                    }
                    // Now assign the values and count to the FilterResults
                    // object
                    filterResults.values = interests;
                    filterResults.count = interests.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence contraint,
                                          FilterResults results) {
                /*
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }*/
                notifyDataSetChanged();

            }
        };
        return myFilter;
    }


}
