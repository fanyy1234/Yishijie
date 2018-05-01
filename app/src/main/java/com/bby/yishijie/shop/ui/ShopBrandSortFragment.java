package com.bby.yishijie.shop.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.shop.adapter.BrandSortAdapter;
import com.bby.yishijie.shop.entity.ProductBrand;
import com.sunday.common.base.BaseFragment;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.swipe.SwipeMenuRecyclerView;
import com.sunday.common.widgets.swipe.touch.OnItemMoveListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Author by Damon,  on 2017/12/17.
 */

public class ShopBrandSortFragment extends BaseFragment {


    @Bind(R.id.recycler_view)
    SwipeMenuRecyclerView recyclerView;


    BrandSortAdapter adapter;

    private List<ProductBrand> dataSet = new ArrayList<>();
    private int pageNo = 1;
    private boolean isCanloadMore;
    private int lastVisibleItem;
    private LinearLayoutManager layoutManager;
    private Long memberId;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_shop_sort, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    public List<ProductBrand> getBrandSet() {
        return dataSet;
    }

    private void initView() {
        memberId = BaseApp.getInstance().getMember().getId();
        layoutManager = new GridLayoutManager(mContext,4);
        recyclerView.setLongPressDragEnabled(true); // 拖拽排序，默认关闭
        recyclerView.setLayoutManager(layoutManager);
        adapter = new BrandSortAdapter(mContext, dataSet);
        recyclerView.setAdapter(adapter);
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

        recyclerView.setOnItemMoveListener(mItemMoveListener);// 监听拖拽，更新UI。

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.total_layout:
                        ProductBrand productBrand = (ProductBrand) v.getTag();
                        Intent intent = new Intent(mContext, BrandProductListShopActivity.class);
                        intent.putExtra("type", productBrand.getType());
                        intent.putExtra("brandName", String.format("%s", productBrand.getName()));
                        intent.putExtra("brandId", productBrand.getId());
                        mContext.startActivity(intent);
                        break;
                    case R.id.btn_del:
                        int p = (int) v.getTag();
                        manageBrand(p);
                        break;
                }
            }
        });
        getData();
    }

    OnItemMoveListener mItemMoveListener = new OnItemMoveListener() {
        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            // Item被拖拽时，交换数据，并更新adapter。
            if (fromPosition < toPosition)
                for (int i = fromPosition; i < toPosition; i++)
                    Collections.swap(dataSet, i, i + 1);
            else
                for (int i = fromPosition; i > toPosition; i--)
                    Collections.swap(dataSet, i, i - 1);

            adapter.notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onItemDismiss(int position) {

        }
    };

    private void getData() {
        Call<ResultDO<List<ProductBrand>>> call = ApiClient.getApiAdapter().getShopBrands(memberId, pageNo, Constants.PAGE_SIZE);
        call.enqueue(new Callback<ResultDO<List<ProductBrand>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<ProductBrand>>> call, Response<ResultDO<List<ProductBrand>>> response) {
                if (isFinish) {
                    return;
                }
                ResultDO<List<ProductBrand>> resultDO = response.body();
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
            public void onFailure(Call<ResultDO<List<ProductBrand>>> call, Throwable t) {

            }
        });
    }

    private void manageBrand(final int p) {
        ProductBrand productBrand = dataSet.get(p);
        final int type = productBrand.getStatus() == 0 ? 1 : 2;
        showLoadingDialog(0);
        Call<ResultDO<Integer>> call = ApiClient.getApiAdapter().addPro(memberId, type, null, productBrand.getId());
        call.enqueue(new Callback<ResultDO<Integer>>() {
            @Override
            public void onResponse(Call<ResultDO<Integer>> call, Response<ResultDO<Integer>> response) {
                if (isFinish) {
                    return;
                }
                dissMissDialog();
                if (response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    ToastUtils.showToast(mContext, type == 1 ? "上架成功" + "\n已上架品牌数：" + response.body().getResult() : "下架成功" + "\n已上架品牌数：" + response.body().getResult());
                    dataSet.get(p).setStatus(type == 1 ? 1 : 0);
                    dataSet.remove(p);
                    adapter.notifyItemRemoved(p);
                    if(p != dataSet.size()){
                        adapter.notifyItemRangeChanged(p, dataSet.size() - p);
                    }
                } else {
                    ToastUtils.showToast(mContext, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Integer>> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }

}
