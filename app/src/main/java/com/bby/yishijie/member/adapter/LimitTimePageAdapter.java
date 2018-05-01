package com.bby.yishijie.member.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bby.yishijie.R;
import com.bby.yishijie.member.entity.LimitTime;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by 刘涛 on 2017/5/15.
 */

public class LimitTimePageAdapter extends PagerAdapter {

    private List<LimitTime> mItems = new ArrayList<>();
    private Context mContext;

    public LimitTimePageAdapter(Context context,List<LimitTime> datas) {

        mContext=context;
        mItems=datas;
    }

    public Context getContext(){
        return mContext;
    }


    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
      View view= LayoutInflater.from(mContext).inflate(R.layout.time_tab_item,container,false);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mItems.get(position).getTime();
    }

    public LimitTime getLimitTime(int position){
        return mItems.get(position);
    }
}
