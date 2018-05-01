package com.bby.yishijie.member.ui.index;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bby.yishijie.R;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.base.BaseLazyFragment;
import com.bby.yishijie.shop.adapter.ProductListAdapter;
import com.bby.yishijie.shop.entity.Product;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.widgets.EmptyLayout;
import com.sunday.common.widgets.ptr.PtrClassicFrameLayout;
import com.sunday.common.widgets.ptr.PtrDefaultHandler;
import com.sunday.common.widgets.ptr.PtrFrameLayout;
import com.sunday.common.widgets.ptr.PtrHandler;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2017/4/11.
 * <p>
 * 首页具体产品分类列表
 */

public class ProductListShopFragment extends BaseLazyFragment {


    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.ptr_frame)
    PtrClassicFrameLayout ptrFrame;
    @Bind(R.id.empty_layout)
    EmptyLayout emptyLayout;


    private int pageNo = 1, lastVisibleItem;

    private List<Product> dataSet = new ArrayList<>();
    private ProductListAdapter orderAdapter;
    private boolean isCanloadMore;
    private LinearLayoutManager layoutManager;
    private long catId;

    public static ProductListShopFragment newInstance(long catId) {
        ProductListShopFragment fragment = new ProductListShopFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("catId",catId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            catId = getArguments().getLong("catId");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isPrepared = true;
        lazyLoad();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order_list, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        if (!isPrepared || !isVisible) {
            return;
        }
        initRefreshLayout();
        isPrepared = false;
        ptrFrame.autoRefresh();
    }

    private void initRefreshLayout() {
        orderAdapter = new ProductListAdapter(mContext, dataSet);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(orderAdapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .drawable(R.drawable.shape_divider)
                .build());
        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pageNo = 1;
                getData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, recyclerView, header);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == orderAdapter.getItemCount() && isCanloadMore) {
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


    private void getData() {
        Call<ResultDO<List<Product>>> call = ApiClient.getApiAdapter().getProductList2(1,0,null,null,Long.valueOf(catId).intValue(),null,null,pageNo, Constants.PAGE_SIZE);
        call.enqueue(new Callback<ResultDO<List<Product>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<Product>>> call, Response<ResultDO<List<Product>>> response) {
                if (isFinish) {
                    return;
                }
                ptrFrame.refreshComplete();
                ResultDO<List<Product>> resultDO = response.body();
                if (resultDO != null) {
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
                        if (dataSet.size() == 0) {
                            emptyLayout.setErrorType(EmptyLayout.NODATA);
                            emptyLayout.setNoDataContent("暂无数据");
                        } else {
                            emptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                        }
                        orderAdapter.notifyDataSetChanged();
                    } else {
                        emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                    }
                } else {
                    emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<Product>>> call, Throwable t) {
                ptrFrame.refreshComplete();
                emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
            }
        });
    }

}
