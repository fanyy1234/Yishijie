package com.bby.yishijie.member.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.ClearEditText;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2017/4/18.
 */

public class LoginActivity extends BaseActivity {


    @Bind(R.id.et_phone)
    ClearEditText etPhone;
    @Bind(R.id.et_pwd)
    ClearEditText etPwd;
    @Bind(R.id.iv_reset_pwd_visible)
    ImageButton ivResetPwdVisible;
    @Bind(R.id.btn_login)
    TextView btnLogin;
    @Bind(R.id.is_shop)
    CheckBox isShopCheckBox;
    private UMShareAPI umShareAPI;

    private String mobileStr;
    private String pwdStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        etPhone.addTextChangedListener(textWatcher);
        etPwd.addTextChangedListener(textWatcher);
        umShareAPI = UMShareAPI.get(this);
        umShareAPI.deleteOauth(LoginActivity.this,SHARE_MEDIA.WEIXIN,null);
        etPhone.setText("15267850711");
        etPwd.setText("111111");
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String userName = etPhone.getText().toString();
            String password = etPwd.getText().toString();
            if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
                btnLogin.setEnabled(true);
                btnLogin.setSelected(true);
            } else {
                btnLogin.setEnabled(false);
                btnLogin.setSelected(false);
            }
        }
    };


    private boolean isPressed = true;

    @OnClick({R.id.btn_login, R.id.btn_wechat_login, R.id.btn_register, R.id.btn_forget_pwd,
            R.id.iv_reset_pwd_visible})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
//                intent = new Intent(mContext, MainActivity.class);
//
//                startActivity(intent);
//                finish();
                mobileStr = etPhone.getText().toString().trim();
                if (!StringUtils.isMobileNO(mobileStr)) {
                    ToastUtils.showToast(mContext, "请输入正确手机号");
                }
                pwdStr = etPwd.getText().toString().trim();
                if (!isShopCheckBox.isChecked()){
                    login();
                }
                else {
                    login2();
                }
                break;
            case R.id.btn_wechat_login:
                umShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, umAuthInfoListener);
                break;
            case R.id.btn_register:
                intent = new Intent(mContext, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_forget_pwd:
                intent=new Intent(mContext,ForgetPwdActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_reset_pwd_visible:
                if (!isPressed) {
                    ivResetPwdVisible.setSelected(false);
                    etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivResetPwdVisible.setSelected(true);
                }
                isPressed = !isPressed;
                etPwd.postInvalidate();
                CharSequence text = etPwd.getText();
                if (text instanceof Spannable) {
                    Spannable spanText = (Spannable) text;
                    Selection.setSelection(spanText, text.length());
                }
                break;
        }
    }

    private String logo, nickName, unionid, openid;
    private UMAuthListener umAuthInfoListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }


        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            switch (share_media) {
                case WEIXIN:
                    openid = map.get("openid");
                    logo = map.get("iconurl");
                    nickName = map.get("name");
                    unionid = map.get("unionid");
                    thirdLogin();
                    break;
            }
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            ToastUtils.showToast(mContext, "授权失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            ToastUtils.showToast(mContext, "授权取消");
        }
    };


    private void thirdLogin() {
        Call<ResultDO<Member>> call = ApiClient.getApiAdapter().wxLogin(nickName,logo, unionid, openid);
        call.enqueue(new Callback<ResultDO<Member>>() {
            @Override
            public void onResponse(Call<ResultDO<Member>> call, Response<ResultDO<Member>> response) {
                ResultDO<Member> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    if (resultDO.getResult() == null || isFinish) {
                        return;
                    }
                    if (TextUtils.isEmpty(resultDO.getResult().getMobile())){
                        //绑定手机号
                        intent=new Intent(mContext,MobileLoginActivity.class);
                        intent.putExtra("memberId",resultDO.getResult().getId());
                        startActivity(intent);
                    }else{
                        SharePerferenceUtils.getIns(mContext).saveOAuth(resultDO.getResult());
                        SharePerferenceUtils.getIns(mContext).putBoolean(Constants.IS_LOGIN,true);
                        BaseApp.getInstance().setMember(resultDO.getResult());
                        intent = new Intent(mContext, MainActivity.class);
                        startActivity(intent);
                    }

                } else {
                    ToastUtils.showToast(mContext, resultDO.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Member>> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    private void login(){
        Call<ResultDO<Member>> call = ApiClient.getApiAdapter().login(mobileStr,MD5Utils.MD5(pwdStr));
        call.enqueue(new Callback<ResultDO<Member>>() {
            @Override
            public void onResponse(Call<ResultDO<Member>> call, Response<ResultDO<Member>> response) {
                if (isFinish || response.body() == null) {
                    return;
                }
                ResultDO<Member> resultDO = response.body();
                if (response.body().getCode() == 0) {
                    SharePerferenceUtils.getIns(mContext).saveOAuth(resultDO.getResult());
                    SharePerferenceUtils.getIns(mContext).putBoolean(Constants.IS_LOGIN, true);
                    SharePerferenceUtils.getIns(mContext).putBoolean(Constants.IS_SHOP, false);
                    Member member = response.body().getResult();
                    BaseApp.getInstance().setMember(member);
                    intent = new Intent(mContext, MainActivity.class);

                    startActivity(intent);
                    finish();
                } else {
                    ToastUtils.showToast(mContext, resultDO.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Member>> call, Throwable t) {
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }
    private void login2(){
        Call<ResultDO<com.bby.yishijie.shop.entity.Member>> call = ApiClient.getApiAdapter().login2(mobileStr,pwdStr);
        call.enqueue(new Callback<ResultDO<com.bby.yishijie.shop.entity.Member>>() {
            @Override
            public void onResponse(Call<ResultDO<com.bby.yishijie.shop.entity.Member>> call, Response<ResultDO<com.bby.yishijie.shop.entity.Member>> response) {
                if (isFinish || response.body() == null) {
                    return;
                }
                ResultDO<com.bby.yishijie.shop.entity.Member> resultDO = response.body();
                if (response.body().getCode() == 0) {
                    SharePerferenceUtils.getIns(mContext).saveOAuth(resultDO.getResult());
                    SharePerferenceUtils.getIns(mContext).putBoolean(Constants.IS_LOGIN, true);
                    SharePerferenceUtils.getIns(mContext).putBoolean(Constants.IS_SHOP, true);
                    com.bby.yishijie.shop.entity.Member member = response.body().getResult();
                    BaseApp.getInstance().setShopMember(member);
                    intent = new Intent(mContext, MainActivity.class);

                    startActivity(intent);
                    finish();
                } else {
                    ToastUtils.showToast(mContext, resultDO.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<com.bby.yishijie.shop.entity.Member>> call, Throwable t) {
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }

}
