package com.sunday.common.adapter;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2015/9/21.
 */
public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragmentsList;
    private TabLayout tabLayout;
    private List<String> titles=new ArrayList<>();

    public MainFragmentPagerAdapter(FragmentManager fm, Context mContext, ArrayList<Fragment> fragments, int resId) {
        super(fm);
        this.fragmentsList = fragments;
        titles=new ArrayList<>(Arrays.asList(mContext.getResources().getStringArray(resId)));
    }

    public MainFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragmentsList = fragments;
    }

    public MainFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments,TabLayout tabLayout) {
        super(fm);
        this.fragmentsList = fragments;
        this.tabLayout = tabLayout;
    }

    public MainFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments,List<String> titles){
        super(fm);
        this.fragmentsList = fragments;
        this.titles=titles;
    }


    @Override
    public int getCount() {
        return fragmentsList.size();
    }

    @Override
    public Fragment getItem(int arg0) {
        return fragmentsList.get(arg0);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);

    }
}

