package com.bby.yishijie.member.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bby.yishijie.member.ui.main.ShopFragment;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.SharePerferenceUtils;
import com.sunday.common.utils.StringUtils;
import com.sunday.common.utils.TimeCount;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.ClearEditText;
import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.entity.Member;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.MainActivity;
import com.bby.yishijie.member.ui.mine.InputRecCodeActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2017/4/19.
 */

public class MobileLoginActivity extends BaseActivity {


    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.rightTxt)
    TextView rightTxt;
    @Bind(R.id.et_phone)
    ClearEditText etPhone;
    @Bind(R.id.et_code)
    ClearEditText etCode;
    @Bind(R.id.send_code)
    TextView sendCode;
    @Bind(R.id.btn_submit)
    TextView btnSubmit;

    private TimeCount timeCount;
    private long memberId;
    public static MobileLoginActivity mobileLoginActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_login);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mobileLoginActivity = this;
        titleView.setText("邀请店铺");
        btnSubmit.setText("下一步");
        memberId = getIntent().getLongExtra("memberId", 0);
        rightTxt.setVisibility(View.GONE);
        timeCount = new TimeCount(60000, 1000, sendCode);
    }

    @OnClick({R.id.send_code, R.id.btn_submit})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_code:
                String mobileNo = etPhone.getText().toString().trim();
                if (!StringUtils.isMobileNO(mobileNo)) {
                    ToastUtils.showToast(mContext, R.string.desc2);
                    return;
                }
                showLoadingDialog(0);
                Call<ResultDO> call = ApiClient.getApiAdapter().sendCode(mobileNo, 1);
                call.enqueue(new Callback<ResultDO>() {
                    @Override
                    public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                        dissMissDialog();
                        ResultDO resultDO = response.body();
                        if (resultDO == null || isFinish) {
                            return;
                        }
                        if (resultDO.getCode() == 0) {
                            ToastUtils.showToast(mContext, R.string.send_code_success);
                            timeCount.start();
                        } else {
                            ToastUtils.showToast(mContext, resultDO.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultDO> call, Throwable t) {
                        dissMissDialog();
                        ToastUtils.showToast(mContext, R.string.send_code_failed);
                    }
                });
                break;
            case R.id.btn_submit:
                mobileNo = etPhone.getText().toString().trim();
                String code = etCode.getText().toString().trim();
                if (!StringUtils.isMobileNO(mobileNo)) {
                    ToastUtils.showToast(mContext, R.string.desc2);
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    ToastUtils.showToast(mContext, R.string.desc3);
                    return;
                }
                showLoadingDialog(0);
                Call<ResultDO<Member>> call1 = ApiClient.getApiAdapter().checkCode(mobileNo, code,3, memberId);
                call1.enqueue(new Callback<ResultDO<Member>>() {
                    @Override
                    public void onResponse(Call<ResultDO<Member>> call, Response<ResultDO<Member>> response) {
                        dissMissDialog();
                        ResultDO<Member> resultDO = response.body();
                        if (resultDO == null || isFinish) {
                            return;
                        }
                        if (resultDO.getCode() == 0) {
                            if (resultDO.getResult() == null) {
                                return;
                            }
                            intent = new Intent(mContext, BindShopActivity.class);
                            intent.putExtra("memberId",memberId);
                            startActivity(intent);
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
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timeCount != null) {
            timeCount.cancel();
        }
    }
}
