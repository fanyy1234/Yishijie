package com.bby.yishijie.shop.ui;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.shop.entity.Account;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2017/5/12.
 */

public class AddAccountActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.rightTxt)
    TextView rightTxt;
    @Bind(R.id.right_btn)
    ImageView rightBtn;
    @Bind(R.id.common_header)
    RelativeLayout commonHeader;
    @Bind(R.id.select_bank)
    RadioButton selectBank;
    @Bind(R.id.select_alipay)
    RadioButton selectAlipay;
    @Bind(R.id.radio_group)
    RadioGroup radioGroup;
    @Bind(R.id.edit_name)
    EditText editName;
    @Bind(R.id.edit_account_no)
    EditText editAccountNo;
    @Bind(R.id.edit_bank_name)
    EditText editBankName;
    @Bind(R.id.layout_bank1)
    LinearLayout layoutBank1;
    @Bind(R.id.edit_bank_detail)
    EditText editBankDetail;
    @Bind(R.id.layout_bank2)
    LinearLayout layoutBank2;

    private int type = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        titleView.setText("绑定账户");
        selectAlipay.setChecked(true);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                type = checkedId == R.id.select_bank ? 2 : 1;
                layoutBank1.setVisibility(checkedId == R.id.select_bank ? View.VISIBLE : View.GONE);
                layoutBank2.setVisibility(checkedId == R.id.select_bank ? View.VISIBLE : View.GONE);
            }
        });
    }

    @OnClick(R.id.btn_submit)
    void submit() {
        long memberId = BaseApp.getInstance().getMember().getId();
        String name = editName.getText().toString().trim();
        String accountNo = editAccountNo.getText().toString().trim();
        String bankName = editBankName.getText().toString().trim();
        String bankDetail = editBankDetail.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showToast(mContext, "请填写账户名");
            return;
        }
        if (TextUtils.isEmpty(accountNo)) {
            ToastUtils.showToast(mContext, "请填写账号");
            return;
        }
        if (type == 2) {
            if (TextUtils.isEmpty(bankName) || TextUtils.isEmpty(bankDetail)) {
                ToastUtils.showToast(mContext, "请填写开户银行");
                return;
            }
        }
        Call<ResultDO<Account>> call = null;
        if (type == 2) {
            call = ApiClient.getApiAdapter().addAccount(memberId, type, accountNo, name, bankName, bankDetail);
        } else {
            call = ApiClient.getApiAdapter().addAccount(memberId, type, accountNo, name, null, null);
        }
        call.enqueue(new Callback<ResultDO<Account>>() {
            @Override
            public void onResponse(Call<ResultDO<Account>> call, Response<ResultDO<Account>> response) {
                if (isFinish) {
                    return;
                }
                if (response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    ToastUtils.showToast(mContext,"绑定成功");
                    finish();
                }else {
                    ToastUtils.showToast(mContext,response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Account>> call, Throwable t) {

            }
        });
    }


}
