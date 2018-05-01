package com.bby.yishijie.shop.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.shop.adapter.BrandProductListAdapter;
import com.bby.yishijie.shop.entity.Product;
import com.bby.yishijie.shop.event.UpdateProList;
import com.bby.yishijie.shop.widgets.ShareWindow;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.widgets.EmptyLayout;
import com.sunday.common.widgets.ptr.PtrClassicFrameLayout;
import com.sunday.common.widgets.ptr.PtrDefaultHandler;
import com.sunday.common.widgets.ptr.PtrFrameLayout;
import com.sunday.common.widgets.ptr.PtrHandler;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 刘涛 on 2017/4/26.
 */

public class BrandProductListShopActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.rightTxt)
    TextView rightTxt;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.ptr_frame)
    PtrClassicFrameLayout ptrFrame;
    @Bind(R.id.empty_layout)
    EmptyLayout emptyLayout;
    @Bind(R.id.right_btn)
    ImageView rightBtn;


    private static int normalType = 1;//非满减产品固定传1；
    private int type = 0;//0:普通品牌 1：组合产品（混合品牌） 2：满减品牌
    private String brandImg;
    private String brandName;
    private long brandId;
    private LinearLayoutManager layoutManager;
    private BrandProductListAdapter adapter;
    private List<Product> dataSet = new ArrayList<>();
    private int pageNo = 1, lastVisibleItem;
    private boolean isCanloadMore;
    private Long memberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        ButterKnife.bind(this);
        initView();
        handlerRecyclerView();
        getData();
    }

    private void initView() {
        type = getIntent().getIntExtra("type", 0);
        brandName = getIntent().getStringExtra("brandName");
        brandId = getIntent().getLongExtra("brandId", 0);
        brandImg = getIntent().getStringExtra("brandImg");
        rightTxt.setVisibility(View.GONE);
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setImageResource(R.mipmap.product_share);
        titleView.setText(brandName);
        memberId = BaseApp.getInstance().getMember().getId();
        emptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
    }

    private void handlerRecyclerView() {
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new BrandProductListAdapter(mContext, dataSet, brandImg);
        recyclerView.setAdapter(adapter);
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
        emptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNo = 1;
                getData();
            }
        });

    }


    private void getData() {
        Call<ResultDO<List<com.bby.yishijie.shop.entity.Product>>> call = null;
        if (type==1||type==2) {
            call = ApiClient.getApiAdapter().getProList2(type, brandId, pageNo, Constants.PAGE_SIZE,memberId);
        } else {
            call = ApiClient.getApiAdapter().getProductList2(normalType, null,memberId.intValue(),null, null, (int)brandId, null, pageNo, Constants.PAGE_SIZE);
        }
        // Call<ResultDO<List<Product>>> call = ApiClient.getApiAdapter().getProductList(normalType, null, null, brandId, null, pageNo, Constants.PAGE_SIZE, memberId,null);
        call.enqueue(new Callback<ResultDO<List<com.bby.yishijie.shop.entity.Product>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<com.bby.yishijie.shop.entity.Product>>> call, Response<ResultDO<List<com.bby.yishijie.shop.entity.Product>>> response) {
                if (isFinish) {
                    return;
                }
                ptrFrame.refreshComplete();
                ResultDO<List<com.bby.yishijie.shop.entity.Product>> resultDO = response.body();
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
                        isCanloadMore = true;
                        pageNo++;
                    } else {
                        isCanloadMore = false;
                    }
                    dataSet.addAll(resultDO.getResult());
                    if (dataSet.size() > 0) {
                        emptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                    } else {
                        emptyLayout.setErrorType(EmptyLayout.NODATA);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<com.bby.yishijie.shop.entity.Product>>> call, Throwable t) {
                ptrFrame.refreshComplete();
                emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
            }
        });
    }

    public void onEvent(UpdateProList updateProList) {
        pageNo = 1;
        getData();
    }

    @OnClick(R.id.right_btn)
    void onShare() {
        /* http://weixin.zj-yunti.com/authorizationPage1.html?param=7(type)*用户id(有上级传上级id，
          没上级传当前用户id)*品牌id*品牌大图链接(需要URL编码)*/
        String shareUrl = String.format("%1$s%2$d*%3$d*%4$d*%5$s", ApiClient.SHARE_URL1, 7, memberId, brandId, brandImg);
        ShareWindow shareWindow = new ShareWindow(mContext, shareUrl, brandName,brandImg,
                null, mContext.getResources().getString(R.string.share_product_desc), null,null);
        shareWindow.showPopupWindow(rightBtn);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


}
