package com.bby.yishijie.member.ui.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.MD5Utils;
import com.sunday.common.utils.SharePerferenceUtils;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.ClearEditText;
import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.entity.Member;
import com.bby.yishijie.member.http.ApiClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2017/9/20.
 */

public class SettingPwdActivity extends BaseActivity {

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
    @Bind(R.id.et_pwd)
    ClearEditText etPwd;
    @Bind(R.id.iv_reset_pwd_visible)
    ImageButton ivResetPwdVisible;
    @Bind(R.id.et_confirm_pwd)
    ClearEditText etConfirmPwd;
    @Bind(R.id.iv_reset_pwd_visible1)
    ImageButton ivResetPwdVisible1;
    @Bind(R.id.btn_register)
    TextView btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_pwd);
        ButterKnife.bind(this);
        titleView.setText("设置密码");

    }

    @OnClick(R.id.btn_register)
    void onClick(){
        String oldPwd=etPwd.getText().toString().trim();
        String newPwd=etConfirmPwd.getText().toString().trim();
        if (TextUtils.isEmpty(oldPwd)||TextUtils.isEmpty(newPwd)){
            return;
        }

        Call<ResultDO<Integer>> call= ApiClient.getApiAdapter().updatePwd(BaseApp.getInstance().getMember().getId(), MD5Utils.MD5(oldPwd),MD5Utils.MD5(newPwd));
        call.enqueue(new Callback<ResultDO<Integer>>() {
            @Override
            public void onResponse(Call<ResultDO<Integer>> call, Response<ResultDO<Integer>> response) {
                if (response.body()==null||isFinish){return;}
                if (response.body().getCode()==0){
                    ToastUtils.showToast(mContext,"修改成功");
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
    }
}
