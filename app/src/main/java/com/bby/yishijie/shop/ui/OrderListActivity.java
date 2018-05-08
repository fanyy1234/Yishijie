package com.bby.yishijie.shop.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.sunday.common.adapter.MainFragmentPagerAdapter;
import com.sunday.common.base.BaseActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.support.design.widget.TabLayout.MODE_SCROLLABLE;


/**
 * Created by 刘涛 on 2017/4/24.
 */

public class OrderListActivity extends BaseActivity {


    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.rightTxt)
    TextView rightTxt;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.view_pager)
    ViewPager viewPager;

    private ArrayList<Fragment> commenOrderFragment = new ArrayList<>();
    private int type=1;//1:买 2：卖

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        rightTxt.setVisibility(View.GONE);
        type=getIntent().getIntExtra("type",1);
        titleView.setText(type==1?"我的订单":"营收订单");
        initFragments();
    }

    private void initFragments(){
        tabLayout.setTabMode(MODE_SCROLLABLE);
        commenOrderFragment.add(OrderListFragment.newInstance(-1,type));//全部
        commenOrderFragment.add(OrderListFragment.newInstance(0,type));//待付款
        commenOrderFragment.add(OrderListFragment.newInstance(1,type));//待发货
        commenOrderFragment.add(OrderListFragment.newInstance(2,type));//待收货
        commenOrderFragment.add(OrderListFragment.newInstance(4,type));//已完成

        viewPager.setAdapter(new MainFragmentPagerAdapter(getSupportFragmentManager(),mContext,
                commenOrderFragment,R.array.order_title));
        viewPager.setOffscreenPageLimit(5);
        tabLayout.setupWithViewPager(viewPager);
        int page = getIntent().getIntExtra("page",0);
        viewPager.setCurrentItem(page,false);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((OrderListFragment)commenOrderFragment.get(position)).refresh();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
