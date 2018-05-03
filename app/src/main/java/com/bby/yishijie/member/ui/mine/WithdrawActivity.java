package com.bby.yishijie.member.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.shop.entity.Account;
import com.bby.yishijie.shop.entity.ProfitAll;
import com.bby.yishijie.shop.event.UpdateProfit;
import com.bby.yishijie.shop.ui.AccountListActivity;
import com.bby.yishijie.shop.ui.WithDrawListActivity;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.event.EventBus;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.CircleImageView;

import java.math.BigDecimal;
import java.math.RoundingMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 刘涛 on 2017/4/26.
 */

public class WithdrawActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.rightTxt)
    TextView rightTxt;
    @Bind(R.id.bank_logo)
    CircleImageView bankLogo;
    @Bind(R.id.bank_name)
    TextView bankName;
    @Bind(R.id.bank_account)
    TextView bankAccount;
    @Bind(R.id.select_account)
    RelativeLayout selectAccount;
    @Bind(R.id.tv_count)
    EditText tvCount;
    @Bind(R.id.tv_usable_count)
    TextView tvUsableCount;
    @Bind(R.id.btn_getcash)
    Button btnGetcash;

    private Account account;
    private long memberId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        ButterKnife.bind(this);
        memberId = BaseApp.getInstance().getMember().getId();
        initView();
        getProfit();
    }

    private void initView() {
        rightTxt.setText("提现记录");
        titleView.setText("提现");
    }

    private String totalMoney;
    ProfitAll profitAll;

    private void getProfit() {
        Call<ResultDO<ProfitAll>> call = ApiClient.getApiAdapter().getMyProfit(memberId);
        call.enqueue(new Callback<ResultDO<ProfitAll>>() {
            @Override
            public void onResponse(Call<ResultDO<ProfitAll>> call, Response<ResultDO<ProfitAll>> response) {
                if (isFinish) {
                    return;
                }
                if (response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    if (response.body().getResult() == null) {
                        return;
                    }
                    profitAll = response.body().getResult();
                    totalMoney = String.format("可用余额￥%s", profitAll.getWithdrawAmount().setScale(2, RoundingMode.HALF_UP));
                    tvUsableCount.setText(totalMoney);
                }
            }

            @Override
            public void onFailure(Call<ResultDO<ProfitAll>> call, Throwable t) {

            }
        });
    }

    private final int REQUEST_ACCOUNT = 0x111;

    @OnClick({R.id.select_account, R.id.btn_getcash, R.id.rightTxt})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.rightTxt:
                intent = new Intent(mContext, WithDrawListActivity.class);
                startActivity(intent);
                break;
            case R.id.select_account:
                intent = new Intent(mContext, AccountListActivity.class);
                intent.putExtra("isSelect", true);
                startActivityForResult(intent, REQUEST_ACCOUNT);
                break;
            case R.id.btn_getcash:
                if (account == null) {
                    ToastUtils.showToast(mContext, "请选择账户");
                    return;
                }
                String money = tvCount.getText().toString().trim();
                if (TextUtils.isEmpty(money)) {
                    ToastUtils.showToast(mContext, "请填写提现金额");
                    return;
                }
                if (profitAll.getWithdrawAmount().compareTo(BigDecimal.ZERO) < 1) {
                    ToastUtils.showToast(mContext, "暂无可提现金额");
                    return;
                }
                if (Double.parseDouble(money) > Double.parseDouble(
                        String.format("%s", profitAll.getWithdrawAmount().setScale(2, RoundingMode.HALF_UP)))) {
                    ToastUtils.showToast(mContext, "超出最大提现金额！");
                    return;
                }
                Call<ResultDO> call = ApiClient.getApiAdapter().withDraw(memberId, money, account.getId());
                call.enqueue(new Callback<ResultDO>() {
                    @Override
                    public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                        if (response.body() == null) {
                            return;
                        }
                        if (response.body().getCode() == 0) {
                            ToastUtils.showToast(mContext, "申请成功");
                            EventBus.getDefault().post(new UpdateProfit());
                            finish();
                        } else {
                            ToastUtils.showToast(mContext, response.body().getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultDO> call, Throwable t) {
                        ToastUtils.showToast(mContext, R.string.network_error);
                    }
                });

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_ACCOUNT) {
            account = (Account) data.getSerializableExtra("account");
            updateView();
        }
    }

    private void updateView() {
        if (account == null) {
            return;
        }
        bankLogo.setImageResource(account.getBankName().equals("支付宝") ? R.mipmap.alipay : R.mipmap.bank);
        bankName.setText(account.getBankName());
        bankAccount.setText(account.getAccCode());
    }
}
