package com.adasari.stocksse;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class CurrentFragment extends Fragment {

//    CurrentFragmentListener listener;
//    public interface CurrentFragmentListener{
//        public void createCurrentTab();
//    }
//
//    @Override
//    public void onAttach(Activity activity){
//        super.onAttach(activity);
//        try{
//            listener = (CurrentFragmentListener) activity;
//        }
//        catch (ClassCastException e){
//            throw new ClassCastException(activity.toString());
//        }
//    }


    public CurrentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current, container, false);
    }

}
