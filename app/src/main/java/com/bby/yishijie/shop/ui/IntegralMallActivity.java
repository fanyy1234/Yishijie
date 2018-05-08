package com.bby.yishijie.shop.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.MainActivity;
import com.bby.yishijie.member.ui.index.IntegralMallFragment;
import com.bby.yishijie.member.utils.EntityUtil;
import com.bby.yishijie.shop.adapter.CommonAdapter;
import com.bby.yishijie.shop.adapter.ViewHolder;
import com.bby.yishijie.shop.entity.IntegralDetail;
import com.bby.yishijie.shop.entity.Member;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.SharePerferenceUtils;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 刘涛 on 2017/9/29.
 */

public class IntegralMallActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_mall);
        ButterKnife.bind(this);
        titleView.setText("积分商城");
        IntegralMallFragment integralMallFragment = IntegralMallFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_container, integralMallFragment)
                .show(integralMallFragment).commit();
    }

}
