package com.adasari.stocksse;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebView;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.util.Log;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.view.MenuItem;
import android.widget.TextView;


import org.json.JSONException;
import org.json.JSONObject;

import uk.co.senab.photoview.PhotoViewAttacher;

public class StockInfo extends AppCompatActivity {
    private static final String TAG = "adasari";
    private static final String TAG_JSON = "json";
    public String JSONObjectString;
    public String selectedStockSymbol;

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
        try {
            JSONObject jsonObject = new JSONObject(JSONObjectString);
            setTitle(jsonObject.getString("Name"));
            selectedStockSymbol  = jsonObject.getString("Symbol");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.i(TAG, "ACT2-onCreate");

        Bundle receivedStock = getIntent().getExtras();
        if(receivedStock == null){
            return;
        }
        selectedStock = receivedStock.getString("Selected");

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
        private Animator mCurrentAnimator;
        private int mShortAnimationDuration;

        public CurrentFragment() {
            // Required empty public constructor
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View rootView = inflater.inflate(R.layout.fragment_current, container, false);
            Log.i(TAG, "ACT2 CurrentFragment.onCreateView");
            CurrentStockTask ctask = new CurrentStockTask();
            ctask.execute("http://chart.finance.yahoo.com/t?s="+selectedStockSymbol+"&lang=en-US&width=300&height=200"); //YAHOO Link here
            mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
            return rootView;
        }


        private class CurrentStockTask extends AsyncTask<String, Integer, Bitmap>{
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Bitmap doInBackground(String... strings) {
                String link = strings[0];
                Bitmap mIcon11 = null;
                try {
                    String urldisplay = link;
                    try {
                        InputStream in = new java.net.URL(urldisplay).openStream();
                        mIcon11 = BitmapFactory.decodeStream(in);
                        Log.i(TAG_JSON, "ACT2-GOT YAHOO IMAGE");
                    } catch (Exception e) {
                        Log.e("Error", e.getMessage());
                        e.printStackTrace();
                    }
                    return mIcon11;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return mIcon11;
            }

            @Override
            protected void onPostExecute(final Bitmap t) {
                super.onPostExecute(t);

                ImageButton imageViewYahoo = (ImageButton) findViewById(R.id.yahooImg);
                imageViewYahoo.setImageBitmap(t);
                imageViewYahoo.setOnClickListener(new ImageButton.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Log.i(TAG, "HAHAHAHAHAHA");

                    }
                });

                String s = JSONObjectString;
                try{

                    JSONObject jsonObject = new JSONObject(s);
                    Log.i(TAG_JSON, "ACT2-JSON from intent  - --- " + jsonObject);

                    TextView test = (TextView) findViewById(R.id.stockAttribute1);
                    test.setText("NAME");
                    test = (TextView) findViewById(R.id.stockValue1);
                    test.setText(jsonObject.getString("Name"));
                    ImageView iw = (ImageView) findViewById(R.id.stockTrendImage1);
                    iw.setImageResource(R.drawable.trans);

                    test = (TextView) findViewById(R.id.stockAttribute2);
                    test.setText("SYMBOL");
                    test = (TextView) findViewById(R.id.stockValue2);
                    test.setText(jsonObject.getString("Symbol"));
                    iw = (ImageView) findViewById(R.id.stockTrendImage2);
                    iw.setImageResource(R.drawable.trans);

                    test = (TextView) findViewById(R.id.stockAttribute3);
                    test.setText("LASTPRICE");
                    test = (TextView) findViewById(R.id.stockValue3);
                    test.setText(jsonObject.getString("LastPrice"));
                    iw = (ImageView) findViewById(R.id.stockTrendImage3);
                    iw.setImageResource(R.drawable.trans);

                    test = (TextView) findViewById(R.id.stockAttribute4);
                    test.setText("CHANGE");
                    test = (TextView) findViewById(R.id.stockValue4);
                    double change = Double.valueOf(jsonObject.getString("Change"));
                    String c = String.format("%.2f", change);
                    double changepercent = Double.valueOf(jsonObject.getString("ChangePercent"));
                    String cp = String.format("%.2f", changepercent);
                    test.setText("(" + cp + "%)");
                    iw = (ImageView) findViewById(R.id.stockTrendImage4);
                    if (change > 0)
                        iw.setImageResource(R.drawable.up);
                    if (change < 0)
                        iw.setImageResource(R.drawable.down);
                    if (change == 0)
                        iw.setImageResource(R.drawable.trans);

                    test = (TextView) findViewById(R.id.stockAttribute5);
                    test.setText("TIMESTAMP");
                    test = (TextView) findViewById(R.id.stockValue5);
                    test.setText(jsonObject.getString("Timestamp"));
                    iw = (ImageView) findViewById(R.id.stockTrendImage5);
                    iw.setImageResource(R.drawable.trans);

                    test = (TextView) findViewById(R.id.stockAttribute6);
                    test.setText("MARKETCAP");
                    test = (TextView) findViewById(R.id.stockValue6);
                    double marketcap = Double.valueOf(jsonObject.getString("MarketCap"));
                    if (marketcap > 9999999) {
                        marketcap = marketcap / 1000000000;
                        String marketcapdisplay = String.format("%.2f", marketcap);
                        test.setText(marketcapdisplay+"Billion");
                    } else {
                        marketcap = marketcap / 1000000;
                        String marketcapdisplay = String.format("%.2f", marketcap);
                        test.setText(marketcapdisplay+"Million");
                    }
                    iw = (ImageView) findViewById(R.id.stockTrendImage6);
                    iw.setImageResource(R.drawable.trans);

                    test = (TextView) findViewById(R.id.stockAttribute7);
                    test.setText("VOLUME");
                    test = (TextView) findViewById(R.id.stockValue7);
                    test.setText(jsonObject.getString("Volume"));
                    iw = (ImageView) findViewById(R.id.stockTrendImage7);
                    iw.setImageResource(R.drawable.trans);

                    test = (TextView) findViewById(R.id.stockAttribute8);
                    test.setText("CHANGETYD");
                    test = (TextView) findViewById(R.id.stockValue8);
                    change = Double.valueOf(jsonObject.getString("ChangeYTD"));
                    c = String.format("%.2f", change);
                    changepercent = Double.valueOf(jsonObject.getString("ChangePercentYTD"));
                    cp = String.format("%.2f", changepercent);
                    assert test != null;
                    test.setText(c + "(" + cp + "%)");
                    iw = (ImageView) findViewById(R.id.stockTrendImage8);
                    if (change > 0)
                        iw.setImageResource(R.drawable.up);
                    if (change < 0)
                        iw.setImageResource(R.drawable.down);
                    if (change == 0)
                        iw.setImageResource(R.drawable.trans);


                    test = (TextView) findViewById(R.id.stockAttribute9);
                    test.setText("HIGH");
                    test = (TextView) findViewById(R.id.stockValue9);
                    test.setText(jsonObject.getString("High"));
                    iw = (ImageView) findViewById(R.id.stockTrendImage9);
                    iw.setImageResource(R.drawable.trans);

                    test = (TextView) findViewById(R.id.stockAttribute10);
                    test.setText("LOW");
                    test = (TextView) findViewById(R.id.stockValue10);
                    test.setText(jsonObject.getString("Low"));
                    iw = (ImageView) findViewById(R.id.stockTrendImage10);
                    iw.setImageResource(R.drawable.trans);

                    test = (TextView) findViewById(R.id.stockAttribute11);
                    test.setText("OPEN");
                    test = (TextView) findViewById(R.id.stockValue11);
                    test.setText(jsonObject.getString("Open"));
                    iw = (ImageView) findViewById(R.id.stockTrendImage11);
                    iw.setImageResource(R.drawable.trans);



                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressLint("ValidFragment")
    public class HighChartsFragment extends Fragment {
        public HighChartsFragment() {
            // Required empty public constructor
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Log.i(TAG, "ACT2 HighChartsFragment.onCreateView "+selectedStockSymbol );
            View rootView = inflater.inflate(R.layout.fragment_high_charts, container, false);
            HighChartsTask highChartsTask = new HighChartsTask();
            highChartsTask.execute("");
            return rootView;
        }

        private class HighChartsTask extends AsyncTask<String, Integer, String> {
            @Override
            protected String doInBackground(String... strings) {
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                String url = "http://www-scf.usc.edu/~adasari/h0m3w0rk8/gethighchatrs.html?selectedStockSymbol=" + selectedStockSymbol;
                WebView webView = (WebView) findViewById(R.id.webView);
//            assert webView != null;
                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl(url);
            }
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
