package com.bby.yishijie.shop.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.shop.adapter.CommonAdapter;
import com.bby.yishijie.shop.adapter.ViewHolder;
import com.bby.yishijie.shop.entity.WithDrawRecord;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.widgets.EmptyLayout;
import com.sunday.common.widgets.ptr.PtrClassicFrameLayout;
import com.sunday.common.widgets.ptr.PtrDefaultHandler;
import com.sunday.common.widgets.ptr.PtrFrameLayout;
import com.sunday.common.widgets.ptr.PtrHandler;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2017/6/1.
 */

public class WithDrawListActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.ptr_frame)
    PtrClassicFrameLayout ptrFrame;
    @Bind(R.id.empty_layout)
    EmptyLayout emptyLayout;

    private CommonAdapter<WithDrawRecord> adapter;
    private List<WithDrawRecord> dataSet = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        ButterKnife.bind(this);
        titleView.setText("提现记录");
        emptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        handlerRecyclerView();
        getData();
    }

    private void handlerRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CommonAdapter<WithDrawRecord>(mContext, R.layout.layout_withdraw_item, dataSet) {
            @Override
            public void convert(ViewHolder holder, WithDrawRecord withDrawRecord) {
                bind(holder,withDrawRecord);
            }
        };
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .drawable(R.drawable.shape_divider)
                .build());
        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, recyclerView, header);
            }
        });

        emptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    private void bind(ViewHolder holder,WithDrawRecord withDrawRecord){
        ((TextView)holder.getView(R.id.account_type)).setText(withDrawRecord.getBankName());
        ((TextView)holder.getView(R.id.account_no)).setText(withDrawRecord.getAccCode());
        ((TextView)holder.getView(R.id.withdraw_money)).setText(String.format("¥%s", withDrawRecord.getAmount().setScale(2, RoundingMode.HALF_UP)));
        ((TextView)holder.getView(R.id.withdraw_time)).setText(withDrawRecord.getCreateTime());
    }

    private void getData() {
        Call<ResultDO<List<WithDrawRecord>>> call = ApiClient.getApiAdapter().getWithDrawList(BaseApp.getInstance().getMember().getId());
        call.enqueue(new Callback<ResultDO<List<WithDrawRecord>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<WithDrawRecord>>> call, Response<ResultDO<List<WithDrawRecord>>> response) {
                if (isFinish || response.body() == null) {
                    return;
                }
                ptrFrame.refreshComplete();
                if (response.body().getCode() == 0){
                    if (response.body().getResult()==null){return;}
                    dataSet.clear();
                    dataSet.addAll(response.body().getResult());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<WithDrawRecord>>> call, Throwable t) {
                ptrFrame.refreshComplete();
            }
        });
    }
}
