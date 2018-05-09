package com.bby.yishijie.shop.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.base.BaseLazyFragment;
import com.bby.yishijie.shop.adapter.BrandListAdapter;
import com.bby.yishijie.shop.entity.ProductBrand;
import com.bby.yishijie.shop.widgets.ShareWindow;
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
 * Created by 刘涛 on 2017/4/11.
 * <p>
 * 首页品牌团
 */

public class BrandShopFragment extends BaseLazyFragment {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.ptr_frame)
    PtrClassicFrameLayout ptrFrame;

    BrandListAdapter adapter;

    private List<ProductBrand> dataSet = new ArrayList<>();
    private Long memberId;
    private int lastVisibleItem;
    private boolean isCanloadMore;
    private LinearLayoutManager layoutManager;

    public static BrandShopFragment newInstance() {
        BrandShopFragment fragment = new BrandShopFragment();
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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //initView();
    }

    private void initView() {
        if (BaseApp.getInstance().getShopMember()==null){
            memberId = 0l;
        }
        else {
            memberId = BaseApp.getInstance().getShopMember().getId();
        }

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setPadding(0, 0, 0, DisplayUtil.dip2px(mContext, 40));
        adapter = new BrandListAdapter(mContext, dataSet);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .drawable(R.drawable.horizontal_space_divider)
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
                pageNo = 1;
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
                switch (v.getId()) {
                    case R.id.total_layout:
                        ProductBrand productBrand = (ProductBrand) v.getTag();
                        intent = new Intent(mContext, BrandProductListShopActivity.class);
                        //intent.putExtra("type", 0);
                        intent.putExtra("brandName", String.format("%s", productBrand.getName()));
                        intent.putExtra("brandId", productBrand.getId());
                        intent.putExtra("brandImg", productBrand.getBigImage());
                        startActivity(intent);
                        break;
                    case R.id.btn_share:
                        //分享
                        if (memberId ==0){
                            ToastUtils.showToast(mContext,"请先登录");
                            return;
                        }
                        ProductBrand item2 = (ProductBrand) v.getTag();
                         /* http://weixin.zj-yunti.com/authorizationPage1.html?param=7(type)*用户id(有上级传上级id，
                     没上级传当前用户id)*品牌id*品牌大图链接(需要URL编码)*/
                        String shareUrl = String.format("%1$s%2$d*%3$d*%4$d*%5$s", ApiClient.SHARE_URL1, 7, memberId, item2.getId(), item2.getBigImage());
                        ShareWindow shareWindow = new ShareWindow(mContext, shareUrl, item2.getName(), item2.getBigImage(),
                                null, mContext.getResources().getString(R.string.share_product_desc), null, null);
                        shareWindow.showPopupWindow(recyclerView);
                        break;
                    case R.id.btn_add_product:
                        //上下架品牌
                        if (memberId ==0){
                            ToastUtils.showToast(mContext,"请先登录");
                            return;
                        }
                        int p = (int) v.getTag();
                        manageBrand(p);
                        break;

                }
            }
        });
        //ptrFrame.autoRefresh();
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
                    adapter.notifyItemChanged(p);
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

    private int pageNo = 1;

    private void getData() {
        Call<ResultDO<List<ProductBrand>>> call = ApiClient.getApiAdapter().getProductBrand2(memberId, pageNo, Constants.PAGE_SIZE);
        call.enqueue(new Callback<ResultDO<List<ProductBrand>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<ProductBrand>>> call, Response<ResultDO<List<ProductBrand>>> response) {
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
            public void onFailure(Call<ResultDO<List<ProductBrand>>> call, Throwable t) {
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
