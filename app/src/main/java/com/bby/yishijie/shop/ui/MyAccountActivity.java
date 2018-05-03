package com.bby.yishijie.shop.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.mine.voucher.VoucherListActivity;
import com.bby.yishijie.shop.entity.ProfitAll;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;

import java.math.RoundingMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 刘涛 on 2017/9/26.
 */

public class MyAccountActivity extends BaseActivity {


    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.blance_money)
    TextView blanceMoney;
    @Bind(R.id.total_money)
    TextView totalMoney;
    @Bind(R.id.available_profit)
    TextView availableProfit;
    @Bind(R.id.integral_num)
    TextView integralNum;
    @Bind(R.id.coupon_num)
    TextView couponNum;
    @Bind(R.id.account_num)
    TextView accountNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        ButterKnife.bind(this);
        titleView.setText("我的账户");
        getData();
    }
    ProfitAll profitAll;
    private void getData(){
        Call<ResultDO<ProfitAll>> call = ApiClient.getApiAdapter().getMyProfit(BaseApp.getInstance().getMember().getId());
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
                    profitAll= response.body().getResult();
                    blanceMoney.setText(String.format("%s", profitAll.getWithdrawAmount().setScale(2, RoundingMode.HALF_UP)));
                    totalMoney.setText(String.format("%s", profitAll.getTotalProfit().setScale(2, RoundingMode.HALF_UP)));
                    availableProfit.setText(String.format("%s", profitAll.getWaitAmount().setScale(2, RoundingMode.HALF_UP)));
                    couponNum.setText("" + profitAll.getVoucherCount());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<ProfitAll>> call, Throwable t) {

            }
        });
    }

    @OnClick({R.id.menu1,R.id.menu2,R.id.menu3,R.id.menu4,R.id.menu5,R.id.menu6})
    void onClick(View v){
        switch (v.getId()){
            case R.id.menu1:
                intent = new Intent(mContext, BalanceDetailActivity.class);
                intent.putExtra("money",String.format("%s", profitAll.getWithdrawAmount().setScale(2, RoundingMode.HALF_UP)));
                startActivity(intent);
                break;
            case R.id.menu2:
                intent = new Intent(mContext, TotalProfitActivity.class);
                startActivity(intent);
                break;
            case R.id.menu3:
                intent = new Intent(mContext, AvailableProfitActivity.class);
                intent.putExtra("money",availableProfit.getText().toString());
                startActivity(intent);
                break;
            case R.id.menu4:
                //积分
                intent = new Intent(mContext, MyIntegralActivity.class);
                intent.putExtra("score",profitAll.getScore());
                startActivity(intent);
                break;
            case R.id.menu5:
                intent = new Intent(mContext, VoucherListActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            case R.id.menu6:
                intent = new Intent(mContext, AccountListActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
        }
    }


}
