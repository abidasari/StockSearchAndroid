package com.adasari.stocksse;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.text.Editable;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Filter;
import android.widget.Switch;

import android.text.TextWatcher;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;

import android.widget.ImageButton;
import android.content.Intent;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Launcher extends AppCompatActivity implements TextWatcher, OnItemClickListener {

    private static final String TAG = "adasari";
    private AutoCompleteTextView actextview;
    private boolean validtextselectedfromlist = false;
    private String selectedStock;
    public List<String> JSONResult = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher);
        Log.i(TAG, "ACT1-onCreate");

        Button getQuotesButton = (Button)findViewById(R.id.getQuotesButton);

        actextview = (AutoCompleteTextView) findViewById(R.id.autoComplete);
        SuggestionAdapter suggestionAdapter = new SuggestionAdapter(this, R.layout.autocomplete_view, actextview.getText().toString());
        actextview.setAdapter(suggestionAdapter);
        actextview.setOnItemClickListener(this);

        assert getQuotesButton != null;
        getQuotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validtextselectedfromlist) {
                    Log.i(TAG, "ACT1-getQuotesButton.setOnClickListener.onClick - valid selection");
                    selectedStock = actextview.getText().toString();
                    String currentstocklink = "http://stocksearch-1276.appspot.com/stocksearch.php?stockselect=" + selectedStock;
                    String currentNewsFeedLink = "http://stocksearch-1276.appspot.com/stocksearch.php?newsfeed=" + selectedStock;
                    Communicator ctask = new Communicator();
                    ctask.execute(currentstocklink, currentNewsFeedLink);
                }
                else{
                    Log.i(TAG, "ACT1-getQuotesButton.setOnClickListener.onClick - invalid selection");
                }
            }
        }); // Get Quotes Button Listener

        ImageButton refreshButton = (ImageButton)findViewById(R.id.refreshButton);
        assert refreshButton != null;
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "ACT1-refreshButton.setOnClickListener.onClick");
            }
        }); // Refresh Button Listener

        Switch autoRefreshToggle = (Switch)findViewById(R.id.autorefreshSwitch);
        autoRefreshToggle.setChecked(false); // is set to unchecked by default
        autoRefreshToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Log.i(TAG, "ACT1-autoRefreshToggle.setOnCheckedChangeListener.isChecked");
                }else{
                    Log.i(TAG, "ACT1-autoRefreshToggle.setOnCheckedChangeListener.isUnChecked");
                }
            }
        }); // Auto Refresh Button Listener

        Button clearButton = (Button)findViewById(R.id.clearButton);
        assert clearButton != null;
        clearButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.i(TAG, "ACT1-clearButton.setOnClickListener.onClick");
                actextview.setText("");
            }
        }); // Clear Button Listener
    }

    @Override
    public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
        Log.i(TAG, "ACT1-onTextChanged");
    }


    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        Log.i(TAG, "ACT1-onItemClick");
        String selected = actextview.getText().toString();
        if (!selected.equals("") && !selected.equals("No Results Found")){
            // An item has been selected from the list.
            validtextselectedfromlist = true;
            Log.i(TAG, "ACT1-Grabbed Text: " + selected);
        }
        else{
            Log.i(TAG, "ACT1-Validation Failed" );
        }
    }

    class Communicator extends AsyncTask<String, String, List<String>> {

        @Override
        protected List<String> doInBackground(String... params) {
            String link = params[0];
            String newsLink = params[1];
            List<String> data = new ArrayList<String>();
            try {
                URL url = new URL(link);
                HttpURLConnection conn = null;
                conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                data.add(reader.readLine());
//                Launcher.this.JSONResult = data;
            } catch (IOException e) {
                e.printStackTrace();
            }

            Launcher.this.JSONResult = data;
            return data;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            try{
                Intent inten = new Intent(getApplicationContext(), StockInfo.class);
                inten.putExtra("SelectedStockName", selectedStock);
                inten.putExtra("JSONStockInfo", result.get(0));
                Log.i(TAG, "ACT1-JSON RESULT - - - - - " + JSONResult);


                Launcher.this.startActivity(inten);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(String... text) {

        }
    }


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

                        List<SuggestGetSet> new_suggestions = jp.getParseJsonWCF(constraint.toString());
                        suggestions.clear();
                        for (int i=0;i<new_suggestions.size();i++) {
                            suggestions.add(new_suggestions.get(i));
                        }
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

    @Override
    public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
        Log.i(TAG, "ACT1-beforeTextChanged");
    }

    @Override
    public void afterTextChanged(final Editable s) {
        Log.i(TAG, "ACT1-afterTextChanged");
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "ACT1-onPause");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "ACT1-onStop");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "ACT1-onRestart");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "ACT1-onDestroy");
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "ACT1-onSaveInstanceState");
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "ACT1-onRestoreInstanceState");
    }


}
