package com.team18.studybuddy.studybuddy;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alpaca on 10/16/2015.
 */
public class SuggestionAdapter extends BaseAdapter implements Filterable {

    protected static final String TAG = "SuggestionAdapter";
    private List<SuggestGetSet> suggestions;
    Context mContext;

    public SuggestionAdapter(Context context, int resource, int textViewResourceId) {
        mContext = context;
        suggestions = new ArrayList<SuggestGetSet>();
    }

    @Override
    public int getCount() {
        return suggestions.size();
    }

    @Override
    public SuggestGetSet getItem(int index) {
        return suggestions.get(index);
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
            convertView = inflater.inflate(R.layout.simple_dropdown_item_2line, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.text1)).setText("  " + getItem(position).getId());
        ((TextView) convertView.findViewById(R.id.text2)).setText("  " + getItem(position).getName());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                JsonParse jp=new JsonParse();
                String constraintTemp;
                if (constraint != null) {
                    // A class that queries a web API, parses the data and
                    // returns an ArrayList<GoEuroGetSet>

                    List<SuggestGetSet> new_suggestions =jp.getParseJsonWCF(constraint.toString());
                    Log.d(TAG, constraint.toString());
                    suggestions.clear();
                    for (int i=0;i<new_suggestions.size();i++) {
                        if(JsonParse.isAlpha(constraint.toString())) {
                            if (new_suggestions.get(i).getId().toLowerCase().contains(constraint.toString().toLowerCase())) {
                                suggestions.add(new_suggestions.get(i));
                            }
                        }else{
                            constraintTemp =constraint.toString().replaceAll("[a-zA-Z]", "");
                            if (new_suggestions.get(i).getId().toLowerCase().contains(constraintTemp.toLowerCase())) {

                                suggestions.add(new_suggestions.get(i));
                            }
                        }
                    }
                    // Now assign the values and count to the FilterResults
                    // object
                    filterResults.values = suggestions;
                    filterResults.count = suggestions.size();
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
