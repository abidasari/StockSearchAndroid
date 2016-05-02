package com.adasari.stocksse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonParse {
    private static final String TAG = "debug1";
    private static final String TAG1 = "debug2";


    public JsonParse(){}

    
    public List<SuggestGetSet> getParseJsonWCF(String sName)
    {
        List<SuggestGetSet> ListData = new ArrayList<SuggestGetSet>();
//        JSONParser parser = new JSONParser();
        try {
            String temp=sName.replace(" ", "%20");
            URL js = new URL("http://stocksearch-1276.appspot.com/stocksearch.php?droidterm="+temp);
            URLConnection jc = js.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
            String line = reader.readLine();
            Log.i(TAG, line);
//            JSONObject jsonResponse = new JSONObject(line);
//            JSONArray jsonArray = jsonResponse.getJSONArray("results");
            JSONArray jsonArray = new JSONArray(line);
            Log.i(TAG1, jsonArray.toString());
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject r = jsonArray.getJSONObject(i);
                ListData.add(new SuggestGetSet(r.getString("id"),r.getString("label")));
            }
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return ListData;
    }

}
