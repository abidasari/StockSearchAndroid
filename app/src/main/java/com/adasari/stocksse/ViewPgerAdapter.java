package com.adasari.stocksse;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;

/**
 * Created by adasari on 5/2/16.
 */
public class ViewPgerAdapter extends FragmentPagerAdapter{

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
