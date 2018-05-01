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

import com.bby.yishijie.member.http.ApiClient;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.MD5Utils;
import com.sunday.common.utils.StringUtils;
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
    @Bind(R.id.et_pwd)
    ClearEditText etPwd;
    @Bind(R.id.iv_reset_pwd_visible)
    ImageButton ivResetPwdVisible;
    @Bind(R.id.btn_register)
    TextView btnRegister;

    private String mobile,code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pwd);
        ButterKnife.bind(this);
        titleView.setText("设置密码");
        mobile=getIntent().getStringExtra("mobile");
        code=getIntent().getStringExtra("code");

    }

    private boolean isPressed = true;
    @OnClick({R.id.btn_register,R.id.iv_reset_pwd_visible})
    void onClick(View v){
        switch (v.getId()){
            case R.id.btn_register:
                String pwd = etPwd.getText().toString().trim();
                if (TextUtils.isEmpty(pwd)) {
                    ToastUtils.showToast(mContext, "请输入密码");
                    return;
                }
                Call<ResultDO<Integer>> call= ApiClient.getApiAdapter().forgetPwd(mobile,code, MD5Utils.MD5(pwd));
                call.enqueue(new Callback<ResultDO<Integer>>() {
                    @Override
                    public void onResponse(Call<ResultDO<Integer>> call, Response<ResultDO<Integer>> response) {
                        if (isFinish||response.body()==null){return;}
                        if (response.body().getCode()==0){
                            ToastUtils.showToast(mContext,"设置成功");
                            intent=new Intent(mContext,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            ToastUtils.showToast(mContext,response.body().getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultDO<Integer>> call, Throwable t) {
                        ToastUtils.showToast(mContext,R.string.network_error);
                    }
                });

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


}
