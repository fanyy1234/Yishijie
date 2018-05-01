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
import com.bby.yishijie.shop.adapter.ShopBrandListAdapter;
import com.bby.yishijie.shop.entity.ProductBrand;
import com.bby.yishijie.shop.widgets.ShareWindow;
import com.sunday.common.base.BaseFragment;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2017/5/10.
 */

public class ShopBrandFragment extends BaseFragment {


    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;


    ShopBrandListAdapter adapter;

    private List<ProductBrand> dataSet = new ArrayList<>();
    private int pageNo = 1;
    private boolean isCanloadMore;
    private int lastVisibleItem;
    private LinearLayoutManager layoutManager;
    private Long memberId;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_recycler_list_shop, container, false);
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
        adapter = new ShopBrandListAdapter(mContext, dataSet);
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
                switch (v.getId()) {
                    case R.id.btn_share:
                        ProductBrand item2 = (ProductBrand) v.getTag();
                         /* http://weixin.zj-yunti.com/authorizationPage1.html?param=7(type)*用户id(有上级传上级id，
                     没上级传当前用户id)*品牌id*品牌大图链接(需要URL编码)*/
                        String shareUrl = String.format("%1$s%2$d*%3$d*%4$d*%5$s", ApiClient.SHARE_URL1, 7, memberId, item2.getId(), item2.getBigImage());
                        ShareWindow shareWindow = new ShareWindow(mContext, shareUrl, item2.getName(), item2.getBigImage(),
                                null, mContext.getResources().getString(R.string.share_product_desc), null,null);
                        shareWindow.showPopupWindow(recyclerView);
                        break;
                    case R.id.btn_add_product:
                        int p = (int) v.getTag();
                        manageBrand(p);
                        break;
                }
            }
        });

        getData();
    }



    private void getData() {
        Call<ResultDO<List<ProductBrand>>> call = ApiClient.getApiAdapter().getShopBrands(memberId, pageNo,  Constants.PAGE_SIZE);
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
                    pageNo = 1;
                    getData();
                } else {
                    ToastUtils.showToast(mContext, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Integer>> call, Throwable t) {
                    dissMissDialog();
                ToastUtils.showToast(mContext,R.string.network_error);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        pageNo=1;
        getData();
    }

}
