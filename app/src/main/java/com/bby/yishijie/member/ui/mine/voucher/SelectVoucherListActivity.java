package com.bby.yishijie.member.ui.mine.voucher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.EmptyLayout;
import com.sunday.common.widgets.ptr.PtrClassicFrameLayout;
import com.sunday.common.widgets.ptr.PtrDefaultHandler;
import com.sunday.common.widgets.ptr.PtrFrameLayout;
import com.sunday.common.widgets.ptr.PtrHandler;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;
import com.bby.yishijie.R;
import com.bby.yishijie.member.adapter.VoucherListAdapter;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.entity.Voucher;
import com.bby.yishijie.member.http.ApiClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 刘涛 on 2017/4/27.
 */

public class SelectVoucherListActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.rightTxt)
    TextView rightTxt;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.ptr_frame)
    PtrClassicFrameLayout ptrFrame;
    @Bind(R.id.empty_layout)
    EmptyLayout emptyLayout;


    private long memberId;
    private VoucherListAdapter adapter;
    private List<Voucher> dataSet = new ArrayList<>();
    private double money,postFee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        ButterKnife.bind(this);
        initView();
        handlerRecyclerView();
        getData();
    }

    private void initView() {
        memberId = BaseApp.getInstance().getMember().getId();
        money = getIntent().getDoubleExtra("money", 0);
        postFee = getIntent().getDoubleExtra("postFee", 0);
        rightTxt.setVisibility(View.GONE);
        titleView.setText("选择优惠券");

    }

    private void handlerRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new VoucherListAdapter(mContext, dataSet);
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

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Voucher item = (Voucher) v.getTag();
                Intent data = new Intent();
                data.putExtra("money", item.getMoney().doubleValue());
                data.putExtra("id", item.getId());
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }


    private void getData() {
        Call<ResultDO<List<Voucher>>> call = ApiClient.getApiAdapter().getVoucherList(memberId, 0, null, money,postFee);
        call.enqueue(new Callback<ResultDO<List<Voucher>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<Voucher>>> call, Response<ResultDO<List<Voucher>>> response) {
                if (isFinish) {
                    return;
                }
                ptrFrame.refreshComplete();
                ResultDO<List<Voucher>> resultDO = response.body();
                if (resultDO != null) {
                    if (resultDO.getCode() == 0) {
                        dataSet.clear();
                        dataSet.addAll(resultDO.getResult());
                        if (dataSet.size() == 0) {
                            emptyLayout.setErrorType(EmptyLayout.NODATA);
                            emptyLayout.setVisibility(View.VISIBLE);
                            emptyLayout.setNoDataContent("暂无可用优惠券");
                        } else {
                            emptyLayout.setVisibility(View.GONE);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.showToast(mContext, resultDO.getMessage());
                    }

                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<Voucher>>> call, Throwable t) {
                ptrFrame.refreshComplete();
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }


}
