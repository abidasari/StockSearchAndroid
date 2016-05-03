package com.adasari.stocksse;

import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.util.Log;
import java.util.ArrayList;
import android.view.MenuItem;

public class StockInfo extends AppCompatActivity {
    private static final String TAG = "adasari";

    String selectedStock;
    ListView tableList;
    StocksDataAdapter stocksDataAdapter;
    ArrayList<StocksData> stocksDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_info);

        Bundle launcherData = getIntent().getExtras();
        if(launcherData == null){
            return;
        }
        String JSONObjectString = launcherData.getString("JSONStockInfo");

        Log.i(TAG, "ACT2-onCreate");
        Log.i(TAG, "ACT2-JSON from intent ---- " + JSONObjectString);

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


        tableList = (ListView) findViewById(R.id.stockTable);
        stocksDatas = new ArrayList<StocksData>();



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
