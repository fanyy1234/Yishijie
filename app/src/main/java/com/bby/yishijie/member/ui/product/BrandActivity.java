package com.bby.yishijie.member.ui.product;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.ui.index.BrandFragment;
import com.sunday.common.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2017/5/15.
 */

public class BrandActivity extends BaseActivity {

    @Bind(R.id.main_container)
    FrameLayout mainContainer;
    @Bind(R.id.title_view)
    TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brands);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        titleView.setText("品牌馆");
        BrandFragment brandFragment = BrandFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_container, brandFragment)
                .show(brandFragment).commit();
    }

}
