package com.bby.yishijie.member.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.entity.Member;
import com.bby.yishijie.member.http.ApiClient;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.MD5Utils;
import com.sunday.common.utils.SharePerferenceUtils;
import com.sunday.common.utils.StringUtils;
import com.sunday.common.utils.TimeCount;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.ClearEditText;
import com.bby.yishijie.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2017/9/20.
 */

public class ForgetPwdActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.et_phone)
    ClearEditText etPhone;
    @Bind(R.id.et_code)
    ClearEditText etCode;
    @Bind(R.id.send_code)
    TextView sendCode;
    @Bind(R.id.btn_register)
    TextView btnRegister;
    @Bind(R.id.password)
    ClearEditText password;
    @Bind(R.id.repeat_pwd)
    ClearEditText repeatPwd;

    private TimeCount timeCount;
    private int type;

    private String phoneStr, codeStr, pwdStr,pwdRepeatStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pwd);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", 0);
        titleView.setText("忘记密码");
        timeCount = new TimeCount(60000, 1000, sendCode);

    }

    @OnClick({R.id.send_code, R.id.btn_register})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_code:
                String mobile = etPhone.getText().toString().trim();
                if (TextUtils.isEmpty(mobile) || !StringUtils.isMobileNO(mobile)) {
                    ToastUtils.showToast(mContext, "请输入正确的手机号");
                    return;
                }
                sendCode(mobile);
                break;
            case R.id.btn_register:
                phoneStr = etPhone.getText().toString().trim();
                codeStr = etCode.getText().toString().trim();
                pwdStr = password.getText().toString().trim();
                pwdRepeatStr = repeatPwd.getText().toString().trim();
                if (TextUtils.isEmpty(phoneStr) || !StringUtils.isMobileNO(phoneStr)) {
                    ToastUtils.showToast(mContext, "请输入正确的手机号");
                    return;
                }
                if (TextUtils.isEmpty(codeStr)) {
                    ToastUtils.showToast(mContext, "请输入验证码");
                    return;
                }
                if (TextUtils.isEmpty(pwdStr)) {
                    ToastUtils.showToast(mContext, "请输入密码");
                    return;
                }
                if (!pwdStr.equals(pwdRepeatStr)) {
                    ToastUtils.showToast(mContext, "密码与确认密码不一致");
                    return;
                }
                register();
                break;

        }
    }

    private void sendCode(String mobile) {
        Call<ResultDO> call = ApiClient.getApiAdapter().sendCode(mobile, 5);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                if (isFinish || response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    ToastUtils.showToast(mContext, "验证码已发送到手机");
                    timeCount.start();
                } else {
                    ToastUtils.showToast(mContext, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }

    private void register() {
        showLoadingDialog(0);
        Call<ResultDO<Integer>> call = ApiClient.getApiAdapter().forgetPwd(phoneStr,codeStr, MD5Utils.MD5(pwdStr));
        call.enqueue(new Callback<ResultDO<Integer>>() {
            @Override
            public void onResponse(Call<ResultDO<Integer>> call, Response<ResultDO<Integer>> response) {
                if (isFinish || response.body() == null) {
                    showLoadingDialog(0);
                    return;
                }
                dissMissDialog();
                ResultDO<Integer> resultDO = response.body();
                if (response.body().getCode() == 0) {

                    ToastUtils.showToast(mContext, "修改成功");
                    finish();
                } else {
                    ToastUtils.showToast(mContext, resultDO.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Integer>> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timeCount != null) {
            timeCount.cancel();
        }
    }
}
