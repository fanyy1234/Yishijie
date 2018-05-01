package com.bby.yishijie.member.ui.product;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.PixUtils;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.EmptyLayout;
import com.sunday.common.widgets.ptr.PtrClassicFrameLayout;
import com.sunday.common.widgets.ptr.PtrDefaultHandler;
import com.sunday.common.widgets.ptr.PtrFrameLayout;
import com.sunday.common.widgets.ptr.PtrHandler;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;
import com.bby.yishijie.R;
import com.bby.yishijie.member.adapter.GlobalProRecAdapter;
import com.bby.yishijie.member.adapter.ProductDetailRecAdapter;
import com.bby.yishijie.member.adapter.ProductListAdapter;
import com.bby.yishijie.member.common.DividerGridItemDecoration;
import com.bby.yishijie.member.entity.Product;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.base.BaseLazyFragment;
import com.bby.yishijie.member.ui.index.ProductListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Author by Damon,  on 2017/12/14.
 */

public class GlobalProListFragment extends BaseLazyFragment {


    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;


    private int pageNo = 1, lastVisibleItem;

    private List<Product> dataSet = new ArrayList<>();
    private GlobalProRecAdapter adapter;
    private boolean isCanloadMore;
    private int catId;

    public static GlobalProListFragment newInstance(int catId) {
        GlobalProListFragment fragment = new GlobalProListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("catId", catId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            catId = getArguments().getInt("catId");
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
        rootView = inflater.inflate(R.layout.fragment_recyclerview, container, false);
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
        pageNo = 1;
        getData();
    }

    private void initRefreshLayout() {
        adapter = new GlobalProRecAdapter(mContext, dataSet);
        final GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerGridItemDecoration(5));
        recyclerView.setPadding(0, 0, 0, PixUtils.dip2px(mContext, 40));
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

    }


    private void getData() {
        Call<ResultDO<List<Product>>> call = ApiClient.getApiAdapter().getGlobalProList(catId, null, pageNo, Constants.PAGE_SIZE);
        call.enqueue(new Callback<ResultDO<List<Product>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<Product>>> call, Response<ResultDO<List<Product>>> response) {
                if (isFinish) {
                    return;
                }
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
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<Product>>> call, Throwable t) {
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }


}
