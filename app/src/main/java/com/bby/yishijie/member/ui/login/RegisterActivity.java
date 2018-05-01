package com.bby.yishijie.member.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.entity.Member;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.MainActivity;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.MD5Utils;
import com.sunday.common.utils.SharePerferenceUtils;
import com.sunday.common.utils.StringUtils;
import com.sunday.common.utils.TimeCount;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.ClearEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 刘涛 on 2017/8/14.
 */

public class RegisterActivity extends BaseActivity {


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
    @Bind(R.id.rec_code)
    ClearEditText recCode;
    @Bind(R.id.nick_name)
    ClearEditText nickName;
    @Bind(R.id.password)
    ClearEditText password;
    @Bind(R.id.repeat_pwd)
    ClearEditText repeatPwd;

    private TimeCount timeCount;
    private int type;

    private String phoneStr, codeStr, nicknameStr, recStr, pwdStr,pwdRepeatStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", 0);
        titleView.setText(type == 0 ? "注册" : "忘记密码");
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
                recStr = recCode.getText().toString().trim();
                nicknameStr = nickName.getText().toString().trim();
                phoneStr = etPhone.getText().toString().trim();
                codeStr = etCode.getText().toString().trim();
                pwdStr = password.getText().toString().trim();
                pwdRepeatStr = repeatPwd.getText().toString().trim();
                if (TextUtils.isEmpty(phoneStr) || !StringUtils.isMobileNO(phoneStr)) {
                    ToastUtils.showToast(mContext, "请输入正确的手机号");
                    return;
                }
                if (TextUtils.isEmpty(nicknameStr)) {
                    ToastUtils.showToast(mContext, "请输入昵称");
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
        Call<ResultDO> call = ApiClient.getApiAdapter().sendCode(mobile, type == 0 ? 4 : 3);
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
        Call<ResultDO<Member>> call = ApiClient.getApiAdapter().register(phoneStr, MD5Utils.MD5(pwdStr), nicknameStr, codeStr, recStr);
        call.enqueue(new Callback<ResultDO<Member>>() {
            @Override
            public void onResponse(Call<ResultDO<Member>> call, Response<ResultDO<Member>> response) {
                if (isFinish || response.body() == null) {
                    showLoadingDialog(0);
                    return;
                }
                dissMissDialog();
                ResultDO<Member> resultDO = response.body();
                if (response.body().getCode() == 0) {
                    SharePerferenceUtils.getIns(mContext).saveOAuth(resultDO.getResult());
                    SharePerferenceUtils.getIns(mContext).putBoolean(Constants.IS_LOGIN, true);
                    Member member = response.body().getResult();
                    BaseApp.getInstance().setMember(member);
//                    intent = new Intent(mContext, MainActivity.class);
//                    startActivity(intent);
                    ToastUtils.showToast(mContext, "注册成功");
                    finish();
                } else {
                    ToastUtils.showToast(mContext, resultDO.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Member>> call, Throwable t) {
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
