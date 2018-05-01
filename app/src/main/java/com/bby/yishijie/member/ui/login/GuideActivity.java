package com.bby.yishijie.member.ui.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.sunday.common.base.BaseActivity;
import com.sunday.common.utils.SharePerferenceUtils;
import com.sunday.common.utils.StatusBarUtil;
import com.bby.yishijie.R;


import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2017/7/20.
 */

public class GuideActivity extends BaseActivity {


    @Bind(R.id.guide_pager)
    ViewPager viewPager;

    private View[] views = new View[3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else {
            getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        views[0] = layoutInflater.inflate(R.layout.layout_guide_one,null);
        views[1] = layoutInflater.inflate(R.layout.layout_guide_two,null);
        views[2]=layoutInflater.inflate(R.layout.layout_guide_three,null);
        views[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,SplashActivity.class);
                SharePerferenceUtils.getIns(mContext).putBoolean("is_first",false);
                startActivity(intent);
                finish();
            }
        });

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(views[position], 0);
                return views[position];
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(views[position]);
            }
        });
    }
}
