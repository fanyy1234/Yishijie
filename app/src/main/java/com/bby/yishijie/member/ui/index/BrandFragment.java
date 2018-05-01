package com.bby.yishijie.member.ui.index;

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
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.utils.PixUtils;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.ptr.PtrClassicFrameLayout;
import com.sunday.common.widgets.ptr.PtrDefaultHandler;
import com.sunday.common.widgets.ptr.PtrFrameLayout;
import com.sunday.common.widgets.ptr.PtrHandler;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;
import com.bby.yishijie.R;
import com.bby.yishijie.member.adapter.BrandListAdapter;
import com.bby.yishijie.member.common.Constant;
import com.bby.yishijie.member.entity.IndexProductBrand;
import com.bby.yishijie.member.entity.ProductBrand;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.base.BaseLazyFragment;
import com.bby.yishijie.member.ui.product.BrandProductListActivity;

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
 * 首页品牌团
 */

public class BrandFragment extends BaseLazyFragment {


    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.ptr_frame)
    PtrClassicFrameLayout ptrFrame;

    BrandListAdapter adapter;
    private int lastVisibleItem;
    private boolean isCanloadMore;
    private LinearLayoutManager layoutManager;
    private List<IndexProductBrand> dataSet = new ArrayList<>();

    public static BrandFragment newInstance() {
        BrandFragment fragment = new BrandFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isPrepared = true;
        isVisible = true;
        lazyLoad();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        if (!isPrepared || !isVisible) {
            return;
        }
        initView();
        isPrepared = false;
        // memberId= BaseApplication.getInstance().getMember().getId();
        //memberId = Constants.TEST_MEMBERID;
        ptrFrame.autoRefresh();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setPadding(0, 0, 0, PixUtils.dip2px(mContext, 40));
    }

    private void initView() {
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new BrandListAdapter(mContext, dataSet);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
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
                IndexProductBrand productBrand = (IndexProductBrand) v.getTag();
                intent = new Intent(mContext, BrandProductListActivity.class);
                intent.putExtra("type", productBrand.getType());
                intent.putExtra("brandName", String.format("%s", productBrand.getName()));
                intent.putExtra("brandId", productBrand.getId());
                intent.putExtra("brandImg", productBrand.getBigImage());
                startActivity(intent);
            }
        });
        //getData();
    }

    private int pageNo = 1;

    private void getData() {
        Call<ResultDO<List<IndexProductBrand>>> call = ApiClient.getApiAdapter().getProductBrand(pageNo, Constants.PAGE_SIZE);
        call.enqueue(new Callback<ResultDO<List<IndexProductBrand>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<IndexProductBrand>>> call, Response<ResultDO<List<IndexProductBrand>>> response) {
                if (isFinish) {
                    return;
                }
                ptrFrame.refreshComplete();
                if (response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    if (pageNo == 1) {
                        dataSet.clear();
                    }
                    dataSet.addAll(response.body().getResult());
                    if (response.body().getResult().size() == Constants.PAGE_SIZE) {
                        pageNo++;
                        isCanloadMore = true;
                    } else {
                        isCanloadMore = false;
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showToast(mContext, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<IndexProductBrand>>> call, Throwable t) {
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
