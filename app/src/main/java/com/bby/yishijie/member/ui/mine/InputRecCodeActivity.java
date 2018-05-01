package com.bby.yishijie.member.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.PasswordInputEdt;
import com.sunday.common.widgets.PswText;
import com.bby.yishijie.R;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.WebViewActivity;
import com.bby.yishijie.member.ui.login.RegisterActivity;
import com.bby.yishijie.member.ui.login.RegisterCompleteActivity;
import com.bby.yishijie.member.widgets.PayEdit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2017/5/5.
 */

public class InputRecCodeActivity extends BaseActivity {


    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.rightTxt)
    TextView rightTxt;
    @Bind(R.id.edt_password)
    PasswordInputEdt editCode;
    @Bind(R.id.btn_register)
    TextView btnRegister;
    @Bind(R.id.btn_call)
    TextView btnCall;

    private String code;
    private String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_code);
        ButterKnife.bind(this);
        mobile = getIntent().getStringExtra("mobile");
        initView();
    }

    private void initView() {
        rightTxt.setVisibility(View.GONE);
        titleView.setText("填写邀请码");
        final InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editCode, InputMethodManager.SHOW_IMPLICIT);

//        editCode.setInputCallBack(new PswText.InputCallBack() {
//            @Override
//            public void onInputFinish(String password) {
//                code = password;
//                imm.hideSoftInputFromWindow(editCode.getWindowToken(), 0);
//            }
//        });

        editCode.setOnInputOverListener(new PasswordInputEdt.onInputOverListener() {
            @Override
            public void onInputOver(String text) {
                code = text;
            }
        });

    }

    @OnClick(R.id.btn_register)
    void onClick() {
        if (TextUtils.isEmpty(code)) {
            ToastUtils.showToast(mContext, "请输入邀请码");
            return;
        }
        if (code.length() < 6) {
            ToastUtils.showToast(mContext, "请输入正确的邀请码");
            return;
        }
        intent = new Intent(mContext, RegisterCompleteActivity.class);
        intent.putExtra("mobile", mobile);
        intent.putExtra("code", code);
        startActivity(intent);
    }

    @OnClick(R.id.btn_call)
    void onCall() {
        intent = new Intent(mContext, WebViewActivity.class);
        intent.putExtra("url", ApiClient.CUSTOMER_URL);
        intent.putExtra("title", "客服");
        startActivity(intent);
    }


}
