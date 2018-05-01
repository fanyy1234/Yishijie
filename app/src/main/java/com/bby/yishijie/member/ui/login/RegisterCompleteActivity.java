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

import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.MD5Utils;
import com.sunday.common.utils.SharePerferenceUtils;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.ClearEditText;
import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.entity.Member;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.MainActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2017/9/14.
 */

public class RegisterCompleteActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.et_pwd)
    ClearEditText etPwd;
    @Bind(R.id.iv_reset_pwd_visible)
    ImageButton ivResetPwdVisible;
    @Bind(R.id.btn_register)
    TextView btnRegister;

    private String mobile;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_complete);
        ButterKnife.bind(this);
        titleView.setText("设置密码");
        mobile=getIntent().getStringExtra("mobile");
        code=getIntent().getStringExtra("code");

    }

    private boolean isPressed = true;
    @OnClick({R.id.iv_reset_pwd_visible,R.id.btn_register})
    void onClick(View v){
        switch (v.getId()){
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
            case R.id.btn_register:
                String pwd = etPwd.getText().toString().trim();
                if (TextUtils.isEmpty(pwd)){
                    ToastUtils.showToast(mContext,"请输入密码");
                    return;
                }
                break;
        }
    }







}
