package com.bby.yishijie.shop.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.shop.entity.TotalProfit;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;

import java.math.RoundingMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2017/5/12.
 * <p>
 * 累计收益
 */

public class TotalProfitActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.rightTxt)
    TextView rightTxt;
    @Bind(R.id.total_profit)
    TextView totalProfit;
    @Bind(R.id.today_profit)
    TextView todayProfit;
    @Bind(R.id.week_profit)
    TextView weekProfit;
    @Bind(R.id.moth_profit)
    TextView mothProfit;
    @Bind(R.id.last_month_profit)
    TextView lastMonthProfit;

    private long memberId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_profit);
        ButterKnife.bind(this);
        rightTxt.setVisibility(View.GONE);
        titleView.setText("累计收益");
        memberId= BaseApp.getInstance().getShopMember().getId();
        getData();

    }

    private void getData(){
        Call<ResultDO<TotalProfit>> call= ApiClient.getApiAdapter().getTotalProfit(memberId);
        call.enqueue(new Callback<ResultDO<TotalProfit>>() {
            @Override
            public void onResponse(Call<ResultDO<TotalProfit>> call, Response<ResultDO<TotalProfit>> response) {
                    if (isFinish||response.body()==null){return;}
                if (response.body().getCode()==0){
                    if (response.body().getResult()==null){return;}
                    updateView(response.body().getResult());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<TotalProfit>> call, Throwable t) {

            }
        });
    }

    private void updateView(TotalProfit totalProfits){
        totalProfit.setText(String.format("%s",totalProfits.getTotal().setScale(2, RoundingMode.HALF_UP)));
        todayProfit.setText(String.format("%s",totalProfits.getDay().setScale(2, RoundingMode.HALF_UP)));
        weekProfit.setText(String.format("%s",totalProfits.getWeek().setScale(2, RoundingMode.HALF_UP)));
        mothProfit.setText(String.format("%s",totalProfits.getMonth().setScale(2, RoundingMode.HALF_UP)));
        lastMonthProfit.setText(String.format("%s",totalProfits.getPreMonth().setScale(2, RoundingMode.HALF_UP)));
    }

}
