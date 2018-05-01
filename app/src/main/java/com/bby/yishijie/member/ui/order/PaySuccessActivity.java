package com.bby.yishijie.member.ui.order;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.sunday.common.base.BaseActivity;
import com.bby.yishijie.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 刘涛 on 2017/5/31.
 */

public class PaySuccessActivity extends BaseActivity {


    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.total_money)
    TextView totalMoney;
    @Bind(R.id.user_name)
    TextView userName;
    @Bind(R.id.user_addr)
    TextView userAddr;
    @Bind(R.id.btn_check)
    TextView btnCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);
        ButterKnife.bind(this);
        titleView.setText("支付完成");
        String total_Money = getIntent().getStringExtra("totalMoney");
        String address = getIntent().getStringExtra("address");
        String name = getIntent().getStringExtra("name");
        String mobile = getIntent().getStringExtra("mobile");
        totalMoney.setText("订单金额：¥"+ total_Money);
        userName.setText("收货人："+ name +"  "+ mobile);
        userAddr.setText("收货地址："+ address);
    }

    @OnClick(R.id.btn_check)
    void checkOrder(){
        intent=new Intent(mContext,OrderListActivity.class);
        startActivity(intent);
        finish();
    }


}
