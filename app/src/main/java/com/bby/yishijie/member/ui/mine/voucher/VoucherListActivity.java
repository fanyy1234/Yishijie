package com.bby.yishijie.member.ui.mine.voucher;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.sunday.common.adapter.MainFragmentPagerAdapter;
import com.sunday.common.base.BaseActivity;
import com.bby.yishijie.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.bby.yishijie.R.id.fill;
import static com.bby.yishijie.R.id.rightTxt;

/**
 * Created by 刘涛 on 2017/4/27.
 * 优惠券/云币
 */

public class VoucherListActivity extends BaseActivity {


    @Bind(R.id.left_btn)
    ImageView leftBtn;

    @Bind(R.id.right_btn)
    ImageView rightBtn;
    @Bind(R.id.common_header)
    RelativeLayout commonHeader;
    @Bind(R.id.tab_layout)
    SlidingTabLayout tabLayout;
    @Bind(R.id.view_pager)
    ViewPager viewPager;

    private int type = 1;//类型(1-优惠券 2-)
    private ArrayList<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_list);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", 1);
        initView();
    }

    private void initView() {
        initFragment();
    }

    private void initFragment() {
        fragments.add(GetVoucherListFragment.newInstance());//领券中心
        fragments.add(VoucherListFragment.newInstance(0,type));//
        fragments.add(VoucherListFragment.newInstance(2,type));//已过期
        viewPager.setAdapter(new MainFragmentPagerAdapter(getSupportFragmentManager(), mContext,
                fragments, R.array.voucher_title));
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setViewPager(viewPager);
        int page = getIntent().getIntExtra("page", 0);
        viewPager.setCurrentItem(page, false);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position==0){

                }else {
                    ((VoucherListFragment) fragments.get(position)).getData();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}


