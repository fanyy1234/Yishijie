package com.bby.yishijie.shop.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.shop.adapter.CommonAdapter;
import com.bby.yishijie.shop.adapter.ViewHolder;
import com.bby.yishijie.shop.entity.FansProfit;
import com.bby.yishijie.shop.entity.Member;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;

import java.math.RoundingMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2017/5/11.
 * <p>
 * 业绩管理
 */

public class PerformanceActivity extends BaseActivity {


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
    @Bind(R.id.text1)
    TextView text1;
    @Bind(R.id.total_money)
    TextView totalMoney;
    @Bind(R.id.fans_num)
    TextView fansNum;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private Member member;
    private FansProfit fansProfit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);
        ButterKnife.bind(this);
        member = BaseApp.getInstance().getShopMember();
        initView();
        getCustomerProfit();

    }

    private void initView() {
        rightTxt.setVisibility(View.GONE);
        titleView.setText("业绩管理");
    }

    private void getCustomerProfit() {
        Call<ResultDO<FansProfit>> call = ApiClient.getApiAdapter().getSaleRecord(member.getId());
        call.enqueue(new Callback<ResultDO<FansProfit>>() {
            @Override
            public void onResponse(Call<ResultDO<FansProfit>> call, Response<ResultDO<FansProfit>> response) {
                if (isFinish) {
                    return;
                }
                if (response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    fansProfit = response.body().getResult();
                    updateView();
                }
            }

            @Override
            public void onFailure(Call<ResultDO<FansProfit>> call, Throwable t) {

            }
        });
    }

    private void updateView() {
        if (fansProfit == null) {
            return;
        }
        totalMoney.setText(String.format("¥%s", fansProfit.getTotal().setScale(2, RoundingMode.HALF_UP)));
        fansNum.setText("用户("+fansProfit.getList().size()+")");
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext).
                drawable(R.drawable.shape_divider).build());
        CommonAdapter adapter = new CommonAdapter<FansProfit.Fans>(mContext, R.layout.customer_profit_item, fansProfit.getList()) {
            @Override
            public void convert(ViewHolder holder, FansProfit.Fans fans) {
                ((TextView) holder.getView(R.id.name)).setText(fans.getName());
                ((TextView) holder.getView(R.id.identify)).setText(fans.getLevelName());
                ((TextView) holder.getView(R.id.profit)).setText(String.format("¥%s", fans.getTotal().setScale(2, RoundingMode.HALF_UP)));
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


}
