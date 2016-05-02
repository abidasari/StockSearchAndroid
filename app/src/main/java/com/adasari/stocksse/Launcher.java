package com.adasari.stocksse;

import android.support.v7.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.text.Editable;



import android.text.TextWatcher;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;


import android.widget.ArrayAdapter;


public class Launcher extends AppCompatActivity implements TextWatcher, OnItemClickListener {

    private static final String TAG = "adasari";
    private static final String ERR_TAG = "error";
    private static final String OPT_TAG = "testopt";
    private AutoCompleteTextView actextview;
    private boolean validtextselectedfromlist = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        Log.i(TAG, "onCreate");

        Button getQuotesButton = (Button)findViewById(R.id.getQuotesButton);
        getQuotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "getQuotesButton.setOnClickListener.onClick");
                if(validtextselectedfromlist){
                    Log.i(TAG, "getQuotesButton.setOnClickListener.onClick - valid selection");
                }
                else{
                    Log.i(TAG, "getQuotesButton.setOnClickListener.onClick - invalid selection");
                }
            }
        });

        Button clearButton = (Button)findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.i(TAG, "clearButton.setOnClickListener.onClick");
                actextview.setText("");
            }
        });

        actextview = (AutoCompleteTextView) findViewById(R.id.autoComplete);
        actextview.setOnItemClickListener(this);

        actextview.setAdapter(new SuggestionAdapter(this, actextview.getText().toString()));

    }

    @Override
    public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
        Log.i(TAG, "onTextChanged");
    }


    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        Log.i(TAG, "onItemClick");
        String selected = actextview.getText().toString();
        Log.i(TAG, "Grabbed: "+ selected);

        if (selected != "" && !selected.equals("No Results Found")){
            // An item has been selected from the list.
            validtextselectedfromlist = true;
            Log.i(TAG, "Grabbed Text: " + selected);
        }
        else{
            Log.i(TAG, "Validation Failed" );
        }

        //Item not selected from the list
    }

    @Override
    public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
        Log.i(TAG, "beforeTextChanged");
    }

    @Override
    public void afterTextChanged(final Editable s) {
        Log.i(TAG, "afterTextChanged");
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "onRestoreInstanceState");
    }
}
