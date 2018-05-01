package com.bby.yishijie.member.ui.index;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bby.yishijie.R;
import com.bby.yishijie.member.adapter.IndexProductListShopAdapter;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.base.BaseLazyFragment;
import com.bby.yishijie.shop.entity.Product;
import com.bby.yishijie.member.entity.ProductClassify;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.ptr.PtrClassicFrameLayout;
import com.sunday.common.widgets.ptr.PtrDefaultHandler;
import com.sunday.common.widgets.ptr.PtrFrameLayout;
import com.sunday.common.widgets.ptr.PtrHandler;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;
import com.sunday.common.widgets.signcalendar.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2017/9/24.
 */

public class IndexClasssifyShopFragment extends BaseLazyFragment {


    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.ptr_frame)
    PtrClassicFrameLayout ptrFrame;


    private long pCatId;
    private int pageNo = 1;
    private boolean isCanloadMore;
    private List<Product> dataSet = new ArrayList<>();
    private List<ProductClassify> productClassifyList = new ArrayList<>();
    private int lastVisibleItem;
    private LinearLayoutManager layoutManager;
    private IndexProductListShopAdapter adapter;
    private long memberId;
    private String parentName;

    public static IndexClasssifyShopFragment newInstance(long pCatId,String parentName) {
        IndexClasssifyShopFragment fragment = new IndexClasssifyShopFragment();
        Bundle args = new Bundle();
        args.putLong("pCatId", pCatId);
        args.putString("parentName", parentName);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pCatId = getArguments().getLong("pCatId");
            parentName=getArguments().getString("parentName");
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
        rootView = inflater.inflate(R.layout.fragment_product_list, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        initRefreshLayout();
        isPrepared = false;
        memberId= BaseApp.getInstance().getMember().getId();
        ptrFrame.autoRefresh();
    }

    private void initRefreshLayout() {
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setPadding(0,0,0, DisplayUtil.dip2px(mContext,40));
        adapter = new IndexProductListShopAdapter(mContext, dataSet, productClassifyList,parentName,pCatId);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .drawable(R.drawable.shape_divider)
                .build());
        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pageNo = 1;
                getCategoryList();
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

    private void getCategoryList() {
        Call<ResultDO<List<ProductClassify>>> call = ApiClient.getApiAdapter().getSubCat(pCatId);
        call.enqueue(new Callback<ResultDO<List<ProductClassify>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<ProductClassify>>> call, Response<ResultDO<List<ProductClassify>>> response) {
                if (isFinish || response.body() == null) {
                    return;
                }
                ResultDO<List<ProductClassify>> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    productClassifyList.clear();
                    if (resultDO.getResult().size() > 7) {
                        productClassifyList.addAll(resultDO.getResult().subList(0,7));
                        ProductClassify productCategory=new ProductClassify();
                        productCategory.setName("查看更多");
                        productCategory.setId(0);
                        productClassifyList.add(productCategory);
                    } else {
                        productClassifyList.addAll(resultDO.getResult());
                    }
                    getData();
                } else {
                    ToastUtils.showToast(mContext, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<ProductClassify>>> call, Throwable t) {
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }

    private void getData() {
//        Call<ResultDO<List<Product>>> call= ApiClient.getApiAdapter().getProductList(1,pCatId,null,null,null,pageNo, Constants.PAGE_SIZE,memberId,null);
        Call<ResultDO<List<Product>>> call= ApiClient.getApiAdapter().getProductList2(1,0,null,
                Long.valueOf(pCatId).intValue(),null,
                null,null,pageNo, Constants.PAGE_SIZE);
        call.enqueue(new Callback<ResultDO<List<Product>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<Product>>> call, Response<ResultDO<List<Product>>> response) {
                if (isFinish) {
                    return;
                }
                ptrFrame.refreshComplete();
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
                    if (resultDO.getResult().size() ==  Constants.PAGE_SIZE) {
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
            public void onFailure(Call<ResultDO<List<Product>>> call, Throwable t) {
                ptrFrame.refreshComplete();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

