package com.bby.yishijie.member.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.entity.Member;
import com.bby.yishijie.member.utils.EdittextUtil;
import com.sunday.common.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by 刘涛 on 2017/4/26.
 */

public class WithdrawActivity extends BaseActivity {

    @Bind(R.id.left_btn)
    ImageView leftBtn;
    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.rightTxt)
    TextView rightTxt;
    @Bind(R.id.right_btn)
    ImageView rightBtn;
    @Bind(R.id.common_header)
    RelativeLayout commonHeader;
    @Bind(R.id.withdraw_edittext)
    EditText withdrawEdittext;
    private Member member;
    private boolean isLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        titleView.setText("提现");
        rightTxt.setText("提现记录");
        rightTxt.setVisibility(View.VISIBLE);
        rightTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WithdrawActivity.this, WithdrawRecordActivity.class);
                startActivity(intent);
            }
        });
        EdittextUtil.priceFormat(withdrawEdittext);
        if (BaseApp.getInstance().getMember() != null) {
            member = BaseApp.getInstance().getMember();
            isLogin = true;
        }
    }


}
