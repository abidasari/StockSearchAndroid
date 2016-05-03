package com.adasari.stocksse;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by adasari on 5/3/16.
 */
public class StocksDataAdapter extends ArrayAdapter<StocksData> {

    public StocksDataAdapter(Context context, int resource, ArrayList<StocksData> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    static class ViewHolder{
        public TextView heading;
        public TextView details;
    }
}
