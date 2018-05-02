package com.bby.yishijie.shop.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bby.yishijie.R;
import com.bby.yishijie.member.ui.MainActivity;
import com.bby.yishijie.member.utils.EntityUtil;
import com.bby.yishijie.shop.adapter.ViewHolder;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.shop.adapter.CommonAdapter;
import com.bby.yishijie.shop.entity.IntegralDetail;
import com.bby.yishijie.shop.entity.Member;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.SharePerferenceUtils;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 刘涛 on 2017/9/29.
 */

public class MyIntegralActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.rightTxt)
    TextView rightTxt;
    @Bind(R.id.right_btn)
    ImageView rightBtn;
    @Bind(R.id.text1)
    TextView text1;
    @Bind(R.id.total_money)
    TextView totalMoney;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private int score;
    private List<IntegralDetail> dataSet = new ArrayList<>();
    private long memberId;
    private CommonAdapter<IntegralDetail> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_integral);
        ButterKnife.bind(this);
        score = getIntent().getIntExtra("score", 0);
        titleView.setText("我的积分");
        totalMoney.setText("" + score);
        if (MainActivity.isShop){
            memberId = BaseApp.getInstance().getShopMember().getId();
        }
        else {
            memberId = BaseApp.getInstance().getMember().getId();
        }

        initRecyclerView();
        getMember();
        myScoreRecord();
    }

    private void getMember() {
        Call<ResultDO<Member>> call = ApiClient.getApiAdapter().getMemberInfo2(memberId);
        call.enqueue(new Callback<ResultDO<Member>>() {
            @Override
            public void onResponse(Call<ResultDO<Member>> call, Response<ResultDO<Member>> response) {
                if (isFinish || response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    if (response.body().getResult() != null) {
                        Member member = response.body().getResult();
                        SharePerferenceUtils.getIns(mContext).saveOAuth(member);
                        BaseApp.getInstance().setShopMember(member);
                        totalMoney.setText(member.getScore()+"");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Member>> call, Throwable t) {

            }
        });
    }


    private LinearLayoutManager layoutManager;

    private void initRecyclerView() {
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CommonAdapter<IntegralDetail>(mContext, R.layout.item_balance_detail, dataSet) {
            @Override
            public void convert(com.bby.yishijie.shop.adapter.ViewHolder holder, IntegralDetail balanceDetail) {
                bind(holder, balanceDetail);
            }
        };
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .drawable(R.drawable.shape_divider_width)
                .build());

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == adapter.getItemCount() && isCanloadMore) {
//                    getData();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
    }


    private void bind(ViewHolder holder, IntegralDetail balanceDetail) {

        ((TextView) holder.getView(R.id.title)).setText(balanceDetail.getForm());
        ((TextView) holder.getView(R.id.time)).setText(balanceDetail.getTime());
        holder.getView(R.id.number).setVisibility(balanceDetail.getOrderNo() == null ? View.GONE : View.VISIBLE);
        ((TextView) holder.getView(R.id.number)).setText("订单编号：" + balanceDetail.getOrderNo());
        ((TextView) holder.getView(R.id.money)).setText(balanceDetail.getNum());
        ((TextView) holder.getView(R.id.money)).setTextColor(Integer.valueOf(balanceDetail.getNum()) > 0 ?
                mContext.getResources().getColor(R.color.main_color) : mContext.getResources().getColor(R.color.black_3));
    }

    private int pageNo = 1;
    private int lastVisibleItem;
    private boolean isCanloadMore;

    private void getData() {
        showLoadingDialog(0);
        Call<ResultDO<List<IntegralDetail>>> call = ApiClient.getApiAdapter().getScoreDetail(memberId, pageNo, Constants.PAGE_SIZE);
        call.enqueue(new Callback<ResultDO<List<IntegralDetail>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<IntegralDetail>>> call, Response<ResultDO<List<IntegralDetail>>> response) {
                if (isFinish) {
                    return;
                }
                dissMissDialog();
                ResultDO<List<IntegralDetail>> resultDO = response.body();
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
            public void onFailure(Call<ResultDO<List<IntegralDetail>>> call, Throwable t) {
                dissMissDialog();
            }
        });
    }

    private void myScoreRecord() {
        Call<ResultDO> call = ApiClient.getApiAdapter().getScoreRecord(memberId);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                if (isFinish) {
                    return;
                }
                ResultDO resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    JSONObject jsonObject = EntityUtil.ObjectToJson2(resultDO);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    int length = jsonArray.size();

                    for (int i=0;i<length;i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        IntegralDetail integralDetail = new IntegralDetail();
                        integralDetail.setNum(object.get("score").toString());
                        integralDetail.setTime(object.getString("strTime"));
                        integralDetail.setForm(object.getInteger("type").toString());
                        dataSet.add(integralDetail);
                    }
                    adapter.notifyDataSetChanged();
//                    myScore.setText(resultDO.getResult().toString());
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
            }
        });
    }
}
