package com.bby.yishijie.member.ui.product;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.widgets.EmptyLayout;
import com.sunday.common.widgets.ptr.PtrClassicFrameLayout;
import com.sunday.common.widgets.ptr.PtrDefaultHandler;
import com.sunday.common.widgets.ptr.PtrFrameLayout;
import com.sunday.common.widgets.ptr.PtrHandler;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;
import com.bby.yishijie.R;
import com.bby.yishijie.member.adapter.BrandProductListAdapter;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.entity.Member;
import com.bby.yishijie.member.entity.Product;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.login.LoginActivity;
import com.bby.yishijie.member.widgets.ShareWindow;

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

public class BrandProductListActivity extends BaseActivity {

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
    private String brandImg, brandName;
    private long brandId;
    private LinearLayoutManager layoutManager;
    private BrandProductListAdapter adapter;
    private List<Product> dataSet = new ArrayList<>();
    private int pageNo = 1, lastVisibleItem;
    private boolean isCanloadMore;
    private Member member;
    private boolean isLogin;


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
        rightBtn.setImageResource(R.mipmap.share);
        titleView.setText(brandName);
        emptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        if (BaseApp.getInstance().getMember() != null) {
            member = BaseApp.getInstance().getMember();
            isLogin = true;
        }
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
        showLoadingDialog(0);
        Call<ResultDO<List<Product>>> call = null;
        if (type==1||type==2) {
            call = ApiClient.getApiAdapter().getProList(type, brandId, pageNo, Constants.PAGE_SIZE);
        } else {
            call = ApiClient.getApiAdapter().getProductList(normalType, 0,null, null,null, Long.valueOf(brandId).intValue(), null,pageNo, Constants.PAGE_SIZE);
        }

        call.enqueue(new Callback<ResultDO<List<Product>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<Product>>> call, Response<ResultDO<List<Product>>> response) {
                if (isFinish) {
                    return;
                }
                dissMissDialog();
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
            public void onFailure(Call<ResultDO<List<Product>>> call, Throwable t) {
                dissMissDialog();
                ptrFrame.refreshComplete();
                emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
            }
        });
    }

    @OnClick(R.id.right_btn)
    void onShare() {
        if (!isLogin) {
            showLoginDialog();
            return;
        }
        showWindow();


    }

    private void showLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("温馨提示");
        builder.setMessage("您暂未登录！请点击确认登录");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            }
        });
        builder.show();
    }

    public void showWindow() {
      /* http://weixin.zj-yunti.com/authorizationPage1.html?param=7(type)*用户id(有上级传上级id，
          没上级传当前用户id)*品牌id*品牌大图链接(需要URL编码)*/
        String shareUrl = String.format("%1$s%2$d*%3$d*%4$d*%5$s", ApiClient.SHARE_URL1, 7,
                member.getRecId() == 0 ? member.getId() : member.getRecId(), brandId, brandImg);
        ShareWindow shareWindow = new ShareWindow(mContext, shareUrl, brandName, brandImg,
                mContext.getResources().getString(R.string.share_product_desc), null,null);
        shareWindow.showPopupWindow(rightBtn);
    }


}
