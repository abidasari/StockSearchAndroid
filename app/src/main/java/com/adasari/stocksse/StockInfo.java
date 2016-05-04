package com.adasari.stocksse;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.ImageView;
import android.widget.ListView;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class StockInfo extends AppCompatActivity {
    private static final String TAG = "adasari";
    private static final String TAG_JSON = "json";
    public String JSONObjectString;

    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();

    String selectedStock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_info);

        Bundle launcherData = getIntent().getExtras();
        if(launcherData == null){
            return;
        }
        JSONObjectString = launcherData.getString("JSONStockInfo");

        Log.i(TAG, "ACT2-onCreate");

        Bundle receivedStock = getIntent().getExtras();
        if(receivedStock == null){
            return;
        }
        selectedStock = receivedStock.getString("Selected");
        setTitle(selectedStock);
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        ViewPgerAdapter viewPgerAdapter = new ViewPgerAdapter(getSupportFragmentManager());
        viewPgerAdapter.addFragments(new CurrentFragment(), "Current");
        viewPgerAdapter.addFragments(new HighChartsFragment(), "Historical");
        viewPgerAdapter.addFragments(new NewsFeedFragment(), "News");
        assert pager != null;
        pager.setAdapter(viewPgerAdapter);
        assert tabs != null;
        tabs.setupWithViewPager(pager);
    }

    public class ViewPgerAdapter extends FragmentPagerAdapter {

        ArrayList<Fragment> fragments = new ArrayList<>();
        ArrayList<String> tabTitles = new ArrayList<>();

        public void addFragments(Fragment fragment, String title){
            this.fragments.add(fragment);
            this.tabTitles.add(title);
        }

        public ViewPgerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
        @Override
        public CharSequence getPageTitle(int position){
            return tabTitles.get(position);
        }
    }

    @SuppressLint("ValidFragment")
    public class CurrentFragment extends Fragment {

        public CurrentFragment() {
            // Required empty public constructor
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View rootView = inflater.inflate(R.layout.fragment_current, container, false);
            Log.i(TAG, "ACT2 CurrentFragment.onCreateView");
            CurrentStockTask ctask = new CurrentStockTask();
            ctask.execute("");
            return rootView;
        }

        private class CurrentStockTask extends AsyncTask<String, Integer, String>{

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... strings) {
                String data = "";
                try {
                    data = JSONObjectString;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return data;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try{
                    ListView listView = (ListView) findViewById(R.id.stockTable);
                    JSONObject jsonObject = new JSONObject(s);
                    Log.i(TAG_JSON, "ACT2-JSON from intent  - --- " + jsonObject);
//                    TextView currentStocks = (TextView) findViewById(R.id.currentStocks);
//                    currentStocks.setText(jsonObject.getString("Name"));
                    ArrayList<StockVal> list = new ArrayList<StockVal>();
                    StockVal row1 = new StockVal("NAME", jsonObject.getString("Name"), "");
                    StockVal row2 = new StockVal("SYMBOL", jsonObject.getString("Symbol"), "");
                    StockVal row3 = new StockVal("LASTPRICE", jsonObject.getString("LastPrice"), "");
                    StockVal row4; //= new StockVal("CHANGE", jsonObject.getString("Change"), "");
                    double change = Double.valueOf(jsonObject.getString("Change"));
                    String c = String.format("%.2f", change);
                    double changepercent = Double.valueOf(jsonObject.getString("ChangePercent"));
                    String cp = String.format("%.2f", changepercent);
                    if (change > 0)
                        row4 = new StockVal("CHANGE", c + "(" + cp + "%)", "UP");
                    else if (change < 0)
                        row4 = new StockVal("CHANGE", c + "(" + cp + "%)", "DOWN");
                    else
                        row4 = new StockVal("CHANGE", c + "(" + cp + "%)", "");
                    StockVal row5 = new StockVal("TIMESTAMP", jsonObject.getString("Timestamp"), "");
                    StockVal row6;
                    double marketcap = Double.valueOf(jsonObject.getString("MarketCap"));
                    if (marketcap > 9999999) {
                        marketcap = marketcap / 1000000000;
                        String marketcapdisplay = String.format("%.2f", marketcap);
                        row6 = new StockVal("MARKETCAP", marketcapdisplay + " Billion", "");
                    } else {
                        marketcap = marketcap / 1000000;
                        String marketcapdisplay = String.format("%.2f", marketcap);
                        row6 = new StockVal("MARKETCAP", marketcapdisplay + " Million", "");
                    }
                    StockVal row7 = new StockVal("VOLUME", jsonObject.getString("Volume"), "");
                    StockVal row8;
                    change = Double.valueOf(jsonObject.getString("Change"));
                    c = String.format("%.2f", change);
                    changepercent = Double.valueOf(jsonObject.getString("ChangePercent"));
                    cp = String.format("%.2f", changepercent);
                    if (change > 0)
                        row8 = new StockVal("CHANGE YTD", c + "(" + cp + "%)" , "UP");
                    else if (change < 0)
                        row8 = new StockVal("CHANGE YTD", c + "(" + cp + "%)" , "DOWN");
                    else
                        row8 = new StockVal("CHANGE YTD", c + "(" + cp + "%)" , "");
                    StockVal row9 = new StockVal("HIGH", jsonObject.getString("High"), "");
                    StockVal row10 = new StockVal("LOW", jsonObject.getString("Low"), "");
                    StockVal row11 = new StockVal("OPEN", jsonObject.getString("Open"), "");
                    StockVal row12 = new StockVal("IMAGE","","");


                    list.add(row1);
                    list.add(row2);
                    list.add(row3);
                    list.add(row4);
                    list.add(row5);
                    list.add(row6);
                    list.add(row7);
                    list.add(row8);
                    list.add(row9);
                    list.add(row10);
                    list.add(row11);
                    list.add(row12);

                    StockInfoAdapter adapter = new StockInfoAdapter(getApplicationContext(), R.layout.stock_table, list);
                    assert listView != null;
                    listView.setAdapter(adapter);


                }
                catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }
    }

    public class StockVal{
        String _Name;
        String _Val;
        String _Img;


        public StockVal(String _Name, String _Val, String _Img) {
            this._Name = _Name;
            this._Val = _Val;
            this._Img = _Img;
        }

        public String get_Name() {
            return _Name;
        }

        public void set_Name(String _Name) {
            this._Name = _Name;
        }

        public String get_Val() {
            return _Val;
        }

        public void set_Val(String _Val) {
            this._Val = _Val;
        }

        public String get_Img() {
            return _Img;
        }

        public void set_Img(String _Img) {
            this._Img = _Img;
        }
    }

    public class StockInfoAdapter extends ArrayAdapter<StockVal>{

        ArrayList<StockVal> StockInfoList;
        int Resource;
        Context context;
        LayoutInflater vi;

        public StockInfoAdapter(Context context, int resource, ArrayList<StockVal> objects) {
            super(context, resource, objects);

            StockInfoList = objects;
            Resource = resource;
            this.context = context;
            vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            ViewHolder holder;
            convertView = vi.inflate(Resource, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView)convertView.findViewById(R.id.stockTrendImage);
            holder.tvName = (TextView)convertView.findViewById(R.id.stockAttribute);
            holder.tvValue = (TextView)convertView.findViewById(R.id.stockValue);
            convertView.setTag(holder);

            if(StockInfoList.get(position).get_Name().equals("IMAGE")) {

            }
            else {

                if (StockInfoList.get(position).get_Img().equals("")) {
                    holder.imageView.setImageResource(R.drawable.trans);
                    holder.tvName.setText(StockInfoList.get(position).get_Name());
                    holder.tvValue.setText(StockInfoList.get(position).get_Val());
                } else if (StockInfoList.get(position).get_Img().equals("UP")) {
                    holder.imageView.setImageResource(R.drawable.up);
                    holder.tvName.setText(StockInfoList.get(position).get_Name());
                    holder.tvValue.setText(StockInfoList.get(position).get_Val());
                } else if (StockInfoList.get(position).get_Img().equals("DOWN")) {
                    holder.imageView.setImageResource(R.drawable.down);
                    holder.tvName.setText(StockInfoList.get(position).get_Name());
                    holder.tvValue.setText(StockInfoList.get(position).get_Val());
                }
            }



            return convertView;
        }

        class ViewHolder {
            public ImageView imageView;
            public TextView tvName;
            public TextView tvValue;
        }


    }

    public class HighChartsFragment extends Fragment {
        public HighChartsFragment() {
            // Required empty public constructor
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            Log.i(TAG, "ACT2 HighChartsFragment.onCreateView");

            return inflater.inflate(R.layout.fragment_high_charts, container, false);
        }
    }

    public class NewsFeedFragment extends Fragment {
        public NewsFeedFragment() {
            // Required empty public constructor
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            Log.i(TAG, "ACT2 NewsFeedFragment.onCreateView");

            return inflater.inflate(R.layout.fragment_news_feed, container, false);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "ACT2-onPause");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "ACT2-onStop");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "ACT2-onRestart");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "ACT2-onDestroy");
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "ACT2-onSaveInstanceState");
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "ACT2-onRestoreInstsanceState");
    }

}
