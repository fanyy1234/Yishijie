package com.bby.yishijie.shop.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.shop.adapter.ProductSortAdapter;
import com.bby.yishijie.shop.entity.Product;
import com.bby.yishijie.shop.event.UpdateProList;
import com.sunday.common.base.BaseFragment;
import com.sunday.common.event.EventBus;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;
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

public class ShopProductSortFragment extends BaseFragment {

    @Bind(R.id.recycler_view)
    SwipeMenuRecyclerView recyclerView;


    ProductSortAdapter adapter;

    private List<Product> dataSet = new ArrayList<>();
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
        EventBus.getDefault().register(this);
    }


    public List<Product> getProSet() {
        return dataSet;
    }

    private void initView() {
        memberId = BaseApp.getInstance().getMember().getId();
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLongPressDragEnabled(true); // 拖拽排序，默认关闭
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ProductSortAdapter(mContext, dataSet);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .drawable(R.drawable.shape_shop_sort_divider)
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

        recyclerView.setOnItemMoveListener(mItemMoveListener);// 监听拖拽，更新UI。
        getData();
    }

    OnItemMoveListener mItemMoveListener = new OnItemMoveListener() {
        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            // Item被拖拽时，交换数据，并更新adapter。
            Collections.swap(dataSet, fromPosition, toPosition);
            adapter.notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onItemDismiss(int position) {

        }
    };

    private void getData() {
        Call<ResultDO<List<Product>>> call = ApiClient.getApiAdapter().getShopProducts2(memberId, pageNo, Constants.PAGE_SIZE);
        call.enqueue(new Callback<ResultDO<List<Product>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<Product>>> call, Response<ResultDO<List<Product>>> response) {
                if (isFinish) {
                    return;
                }

                ResultDO<List<Product>> resultDO = response.body();
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
            public void onFailure(Call<ResultDO<List<Product>>> call, Throwable t) {

            }
        });
    }

    public void onEvent(UpdateProList updateProList) {
        pageNo = 1;
        getData();
    }

}
