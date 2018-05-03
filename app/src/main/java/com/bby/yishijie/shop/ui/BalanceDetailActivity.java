package com.bby.yishijie.shop.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.mine.WithdrawActivity;
import com.bby.yishijie.shop.adapter.CommonAdapter;
import com.bby.yishijie.shop.adapter.ViewHolder;
import com.bby.yishijie.shop.entity.BalanceDetail;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 刘涛 on 2017/6/9.
 */

public class BalanceDetailActivity extends BaseActivity {

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
    @Bind(R.id.rightTxt)
    TextView rightTxt;

    private long memberId;
    private LinearLayoutManager layoutManager;
    private CommonAdapter<BalanceDetail> adapter;
    private List<BalanceDetail> dataSet= new ArrayList<>();
    private String balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_integral);
        ButterKnife.bind(this);
        balance = getIntent().getStringExtra("money");
        imgProfit.setImageResource(R.mipmap.store_performance_coin);
        memberId = BaseApp.getInstance().getMember().getId();
        totalMoney.setText(balance);
        initView();
        handlerRecyclerView();
        getData();

    }

    private void initView() {
        rightTxt.setVisibility(View.VISIBLE);
        rightTxt.setText("提现");
        titleView.setText("我的余额");
        text1.setText("我的余额(元)");
        memberId = BaseApp.getInstance().getMember().getId();
    }

    private int pageNo = 1;
    private int lastVisibleItem;
    private boolean isCanloadMore;

    private void handlerRecyclerView() {
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CommonAdapter<BalanceDetail>(mContext, R.layout.item_balance_detail, dataSet) {
            @Override
            public void convert(ViewHolder holder, BalanceDetail balanceDetail) {
                bind(holder, balanceDetail);
            }
        };
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .drawable(R.drawable.shape_divider)
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

    private void bind(ViewHolder holder, BalanceDetail balanceDetail) {
        if (balanceDetail.getMoney().compareTo(BigDecimal.ZERO) > 0){
            if (balanceDetail.getObjType()==6){
                ((TextView) holder.getView(R.id.title)).setText("退款");
            }else {
                ((TextView) holder.getView(R.id.title)).setText("零售利润或奖励分红");
            }
        }
        if (balanceDetail.getObjType()==5){
            ((TextView) holder.getView(R.id.title)).setText("购物抵现");
        }else if (balanceDetail.getObjType()==4){
            ((TextView) holder.getView(R.id.title)).setText("提现");
        }
        ((TextView) holder.getView(R.id.time)).setText(balanceDetail.getCreateTime());
        holder.getView(R.id.number).setVisibility(balanceDetail.getOrderNo() == null ? View.GONE : View.VISIBLE);
        ((TextView) holder.getView(R.id.number)).setText("订单编号：" + balanceDetail.getOrderNo());
        ((TextView) holder.getView(R.id.money)).setText(balanceDetail.getMoney().compareTo(BigDecimal.ZERO) > 0 ?
                String.format("+%s", balanceDetail.getMoney().setScale(2, RoundingMode.HALF_UP)) : String.format("%s", balanceDetail.getMoney().setScale(2, RoundingMode.HALF_UP)));

        ((TextView) holder.getView(R.id.money)).setTextColor(balanceDetail.getMoney().compareTo(BigDecimal.ZERO) > 0 ?
                mContext.getResources().getColor(R.color.main_color) : mContext.getResources().getColor(R.color.black_3));
    }


    private void getData() {
        showLoadingDialog(0);
        Call<ResultDO<List<BalanceDetail>>> call = ApiClient.getApiAdapter().getBalanceDetail(memberId, pageNo, Constants.PAGE_SIZE);
        call.enqueue(new Callback<ResultDO<List<BalanceDetail>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<BalanceDetail>>> call, Response<ResultDO<List<BalanceDetail>>> response) {
                if (isFinish) {
                    return;
                }
                dissMissDialog();
                ResultDO<List<BalanceDetail>> resultDO = response.body();
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
            public void onFailure(Call<ResultDO<List<BalanceDetail>>> call, Throwable t) {
                dissMissDialog();
            }
        });
    }

    @OnClick(R.id.rightTxt)
    void withDraw(){
        intent=new Intent(mContext, WithdrawActivity.class);
        startActivity(intent);
    }


}
