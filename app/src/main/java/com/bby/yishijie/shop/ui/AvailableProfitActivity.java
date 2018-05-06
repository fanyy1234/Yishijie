package com.bby.yishijie.shop.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.MainActivity;
import com.bby.yishijie.shop.adapter.AvailableProfitAdapter;
import com.bby.yishijie.shop.entity.AvailableProfit;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2017/6/23.
 * 待收收益
 */

public class AvailableProfitActivity extends BaseActivity {


    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.text1)
    TextView text1;
    @Bind(R.id.total_money)
    TextView totalMoney;
    @Bind(R.id.img_profit)
    ImageView imgProfit;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    private List<AvailableProfit> dataSet = new ArrayList<>();
    private AvailableProfitAdapter adapter;
    private long memberId;
    private String money;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_integral);
        ButterKnife.bind(this);
        titleView.setText("待收收益");
        text1.setText("待收收益(元)");
        money=getIntent().getStringExtra("money");
        imgProfit.setImageResource(R.mipmap.store_performance_coin);
        if (MainActivity.isShop){
            memberId = BaseApp.getInstance().getShopMember().getId();
        }
        else {
            memberId = BaseApp.getInstance().getMember().getId();
        }
        totalMoney.setText(money);
        initRecyclerView();
        getData();
    }


    private LinearLayoutManager layoutManager;

    private void initRecyclerView() {
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AvailableProfitAdapter(mContext, dataSet);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .drawable(R.drawable.shape_divider_width1)
                .build());

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == adapter.getItemCount() && isCanloadMore) {
                    getData();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
    }

    private int pageNo = 1;
    private int lastVisibleItem;
    private boolean isCanloadMore;

    private void getData() {
        showLoadingDialog(0);
        Call<ResultDO<List<AvailableProfit>>> call = ApiClient.getApiAdapter().getAvailableProfit(memberId, pageNo, Constants.PAGE_SIZE);
        call.enqueue(new Callback<ResultDO<List<AvailableProfit>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<AvailableProfit>>> call, Response<ResultDO<List<AvailableProfit>>> response) {
                if (isFinish) {
                    return;
                }
                dissMissDialog();
                ResultDO<List<AvailableProfit>> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    if (resultDO.getResult() == null) {
                        return;
                    }
                    if (pageNo == 1) {
                        dataSet.clear();
                    }
                    if (resultDO.getResult().size() == Constants.PAGE_SIZE) {
                        isCanloadMore = true;
                        pageNo++;
                    } else {
                        isCanloadMore = false;
                    }
                    dataSet.addAll(resultDO.getResult());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<AvailableProfit>>> call, Throwable t) {
                dissMissDialog();
            }
        });
    }


}
