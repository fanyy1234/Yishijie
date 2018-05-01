package com.bby.yishijie.member.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunday.common.base.BaseFragment;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;
import com.bby.yishijie.R;
import com.bby.yishijie.member.adapter.BrandListAdapter;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.entity.IndexProductBrand;
import com.bby.yishijie.member.entity.ProductBrand;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.product.BrandProductListActivity;


import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2017/9/12.
 */

public class ShopBrandFragment extends BaseFragment {


    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;


    BrandListAdapter adapter;

    private List<IndexProductBrand> dataSet = new ArrayList<>();
    private int pageNo = 1;
    private boolean isCanloadMore;
    private int lastVisibleItem;
    private LinearLayoutManager layoutManager;
    private Long memberId;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        memberId = BaseApp.getInstance().getMember().getId();
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new BrandListAdapter(mContext, dataSet);
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
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IndexProductBrand productBrand= (IndexProductBrand) v.getTag();
                intent=new Intent(mContext, BrandProductListActivity.class);
                intent.putExtra("type",productBrand.getType());
                intent.putExtra("brandName", String.format("%s",productBrand.getName()));
                intent.putExtra("brandId",productBrand.getId());
                intent.putExtra("brandImg",productBrand.getBigImage());
                startActivity(intent);
            }
        });

        getData();
    }

    public void refresh(){
        pageNo=1;
        getData();
    }


    public void getData() {
        Call<ResultDO<List<IndexProductBrand>>> call = ApiClient.getApiAdapter().getShopBrand(memberId,pageNo,Constants.PAGE_SIZE);
        call.enqueue(new Callback<ResultDO<List<IndexProductBrand>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<IndexProductBrand>>> call, Response<ResultDO<List<IndexProductBrand>>> response) {
                if (isFinish) {
                    return;
                }
                ResultDO<List<IndexProductBrand>> resultDO = response.body();
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
                    if (resultDO.getResult().size() ==  Constants.PAGE_SIZE) {
                        pageNo++;
                        isCanloadMore = true;
                    } else {
                        isCanloadMore = false;
                    }
                    dataSet.addAll(resultDO.getResult());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<IndexProductBrand>>> call, Throwable t) {

            }
        });
    }



}
