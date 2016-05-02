package com.adasari.stocksse;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;


public class SuggestionAdapter extends ArrayAdapter<SuggestGetSet> {
    protected static final String TAG = "SuggestionAdapter";
    private List<SuggestGetSet> suggestions;
    private int viewResourceId;

    public SuggestionAdapter(Activity context, int viewResourceId, String nameFilter) {
        super(context, viewResourceId);
        suggestions = new ArrayList<SuggestGetSet>();
        this.viewResourceId = viewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        if (v == null){
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }
        SuggestGetSet suggestion = getItem(position);
        if(suggestion != null){
            TextView stockOptionSymbol = (TextView) v.findViewById(R.id.stockSymbol);
            TextView stockOptionName = (TextView) v.findViewById(R.id.stockName);
            if(stockOptionName!=null && stockOptionSymbol!=null){
                stockOptionName.setText(suggestion.getName());
                stockOptionSymbol.setText(suggestion.getId());
            }
        }
        return v;
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
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                JsonParse jp=new JsonParse();
                if (constraint != null) {
                    // A class that queries a web API, parses the data and
                    // returns an ArrayList<GoEuroGetSet>
                    List<SuggestGetSet> new_suggestions = jp.getParseJsonWCF(constraint.toString());
                    suggestions.clear();
                    for (int i=0;i<new_suggestions.size();i++) {
                        suggestions.add(new_suggestions.get(i));
                    }

                    // Now assign the values and count to the FilterResults
                    // object
                    filterResults.values = suggestions;
                    filterResults.count = suggestions.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return myFilter;
    }
}
