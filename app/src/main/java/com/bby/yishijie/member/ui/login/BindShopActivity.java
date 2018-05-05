package com.bby.yishijie.member.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.entity.Member;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.MainActivity;
import com.bby.yishijie.member.ui.main.ShopFragment;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
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
 * Created by 刘涛 on 2017/4/19.
 */

public class BindShopActivity extends BaseActivity {


    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.rightTxt)
    TextView rightTxt;
    @Bind(R.id.et_phone)
    ClearEditText etPhone;
    @Bind(R.id.btn_submit)
    TextView btnSubmit;

    private long memberId;
    private String initCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_shop);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        titleView.setText("邀请店铺");
        memberId = getIntent().getLongExtra("memberId", 0);
        rightTxt.setVisibility(View.GONE);
    }

    @OnClick({ R.id.btn_submit})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                initCode = etPhone.getText().toString().trim();
                if (initCode.equals("")) {
                    ToastUtils.showToast(mContext, "请输入店铺邀请码");
                    return;
                }
                showLoadingDialog(0);
                Call<ResultDO> call1 = ApiClient.getApiAdapter().bindShop(memberId,initCode);
                call1.enqueue(new Callback<ResultDO>() {
                    @Override
                    public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                        dissMissDialog();
                        ResultDO<Member> resultDO = response.body();
                        if (resultDO == null || isFinish) {
                            return;
                        }
                        if (resultDO.getCode() == 0) {
                            ShopFragment.refreshFlag=true;
                            MobileLoginActivity.mobileLoginActivity.finish();
                            finish();
//                            if (resultDO.getResult() == null) {
//                                return;
//                            }
                        } else {
                            ToastUtils.showToast(mContext, resultDO.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultDO> call, Throwable t) {
                        dissMissDialog();
                        ToastUtils.showToast(mContext, R.string.network_error);
                    }
                });
                break;
        }
    }

}
