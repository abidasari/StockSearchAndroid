package com.adasari.stocksse;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.widget.ListView;
import android.widget.TextView;


import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.senab.photoview.PhotoViewAttacher;

public class StockInfo extends AppCompatActivity {
    private static final String TAG = "adasari";
    private static final String TAG_JSON = "json";
    public String JSONObjectString;
    public String selectedStockSymbol;
    public String JSONResultNewsFeed;

    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();

    String selectedStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_info);
        JodaTimeAndroid.init(this);

        Bundle launcherData = getIntent().getExtras();
        if (launcherData == null) {
            return;
        }
        JSONObjectString = launcherData.getString("JSONStockInfo");
        try {
            JSONObject jsonObject = new JSONObject(JSONObjectString);
            setTitle(jsonObject.getString("Name"));
            selectedStockSymbol = jsonObject.getString("Symbol");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "ACT2-onCreate");

        Bundle receivedStock = getIntent().getExtras();
        if (receivedStock == null) {
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

        public void addFragments(Fragment fragment, String title) {
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
        public CharSequence getPageTitle(int position) {
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
            ctask.execute("http://chart.finance.yahoo.com/t?s=" + selectedStockSymbol + "&lang=en-US&width=900&height=630"); //YAHOO Link here
            mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
            return rootView;
        }


        private class CurrentStockTask extends AsyncTask<String, Integer, Bitmap> {
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
                imageViewYahoo.setOnClickListener(new ImageButton.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i(TAG, "HAHAHAHAHAHA");
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        final AlertDialog dialog = builder.create();
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View dialogLayout = inflater.inflate(R.layout.alert_dialog, null);
                        dialog.setView(dialogLayout);
//                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.show();
                        PhotoViewAttacher mAttacher;
                        ImageView image = (ImageView) dialog.findViewById(R.id.expanded_image);
                        mAttacher = new PhotoViewAttacher(image);
                        image.setImageBitmap(t);

                        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface d) {
                                Log.i(TAG_JSON, "ACT2-GOT DIALOGGGGGGGGGGGG");

                                ImageView image = (ImageView) dialog.findViewById(R.id.expanded_image);
                                image.setImageBitmap(t);
                            }
                        });
                    }
                });

                String s = JSONObjectString;
                try {

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
                        test.setText(marketcapdisplay + "Billion");
                    } else {
                        marketcap = marketcap / 1000000;
                        String marketcapdisplay = String.format("%.2f", marketcap);
                        test.setText(marketcapdisplay + "Million");
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


                } catch (JSONException e) {
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
            Log.i(TAG, "ACT2 HighChartsFragment.onCreateView " + selectedStockSymbol);
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
            NewsFeedTask newsFeedTask = new NewsFeedTask();
            newsFeedTask.execute("http://stocksearch-1276.appspot.com/stocksearch.php?newsfeed=" + selectedStockSymbol);
            return inflater.inflate(R.layout.fragment_news_feed, container, false);
        }

        private class NewsFeedTask extends AsyncTask<String, Integer, String> {

            @Override
            protected String doInBackground(String... strings) {
                String link = strings[0];
                String data = "";
                try {
                    URL url = new URL(link);
                    HttpURLConnection conn = null;
                    conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    data = reader.readLine();

                    StockInfo.this.JSONResultNewsFeed = data;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return data;
            }

            @Override
            protected void onPostExecute(String s) {
                try {
                    Log.i(TAG, s);
                    JSONObject jsonObject = new JSONObject(JSONResultNewsFeed);
                    JSONArray res = jsonObject.getJSONArray("results");
                    ListView newslistView = (ListView) findViewById(R.id.newsFeed);
                    Log.i(TAG, JSONResultNewsFeed);


                    ArrayList<Spanned> newslist = new ArrayList<Spanned>();
                    int i;
                    for (i = 0; i < res.length(); i++) {
                        JSONObject obj = res.getJSONObject(i);
                        String url = obj.getString("Url");
                        String title = obj.getString("Title");
                        String description = obj.getString("Description");
                        String publisher = obj.getString("Source");
                        String date = obj.getString("Date");
                        DateTime dt = DateTime.parse(date);
                        String news_date = dt.toString("dd MMMM YYYY, HH:mm:ss");
                        newslist.add(Html.fromHtml("<b><big><big><a href='" + url + "'>" + title + "</a></big></big></b><br>" + description + "<br><h5>Publisher:" + publisher + "<br>Date:" + news_date + "</h5>"));
                    }//end of for
                    ArrayAdapter<Spanned> adapter = new ArrayAdapter<Spanned>(StockInfo.this, R.layout.newsfeed, newslist) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                            TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
                            tv.setMovementMethod(LinkMovementMethod.getInstance());
                            return super.getView(position, convertView, parent);
                        }
                    };
                    newslistView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onPause() {
            super.onPause();
            Log.i(TAG, "ACT2-onPause");
        }


        @Override
        public void onStop() {
            super.onStop();
            Log.i(TAG, "ACT2-onStop");
        }


        @Override
        public void onDestroy() {
            super.onDestroy();
            Log.i(TAG, "ACT2-onDestroy");
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            Log.i(TAG, "ACT2-onSaveInstanceState");
        }

    }
}
