package com.example.phamvietlap.touchbuy.Activity.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by phamvietlap on 04/10/2017.
 */

public class Viewpager extends FragmentPagerAdapter{
    private ArrayList<Fragment> arrayFragment = new ArrayList<>();
    private ArrayList<String> arrayTitle = new ArrayList<>();
    public Viewpager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return arrayFragment.get(position);
    }

    @Override
    public int getCount() {
        return arrayFragment.size();
    }
    public void addFragment(Fragment fragment,String title){
        arrayFragment.add(fragment);
        arrayTitle.add(title);
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

}
