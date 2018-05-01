package com.bby.yishijie.member.ui.mine.voucher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bby.yishijie.member.ui.MainActivity;
import com.sunday.common.base.BaseFragment;
import com.sunday.common.model.ResultDO;
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

public class VoucherListFragment extends BaseFragment {


    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.ptr_frame)
    PtrClassicFrameLayout ptrFrame;
    @Bind(R.id.empty_layout)
    EmptyLayout emptyLayout;

    private Integer status, type;
    private long memberId;

    private List<Voucher> dataSet = new ArrayList<>();
    private VoucherListAdapter adapter;

    public static VoucherListFragment newInstance(int status, int type) {
        VoucherListFragment fragment = new VoucherListFragment();
        Bundle args = new Bundle();
        args.putInt("status", status);
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order_list, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (MainActivity.isShop){
            memberId = BaseApp.getInstance().getShopMember().getId();
        }
        else {
            memberId = BaseApp.getInstance().getMember().getId();
        }
        if (getArguments() != null) {
            status = getArguments().getInt("status");
            type = getArguments().getInt("type");
        }
        adapter = new VoucherListAdapter(mContext, dataSet);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setPadding(12, 12, 12, 12);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .drawable(R.drawable.horizontal_space_divider)
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
        getData();

    }


    @Override
    public void onResume() {
        super.onResume();
    }

    public void getData() {
        Call<ResultDO<List<Voucher>>> call = ApiClient.getApiAdapter().getVoucherList(memberId, status, null, null,null);
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
                        } else {
                            emptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                    }
                } else {
                    emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<Voucher>>> call, Throwable t) {
                ptrFrame.refreshComplete();
                emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
            }
        });
    }


}
