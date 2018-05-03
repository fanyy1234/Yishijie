package com.bby.yishijie.member.ui.product;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bby.yishijie.member.entity.CartPay;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.AutoLinefeedLayout;
import com.sunday.common.widgets.banner.ConvenientBanner;
import com.sunday.common.widgets.banner.holder.ViewHolderCreator;
import com.sunday.common.widgets.banner.listener.OnItemClickListener;
import com.bby.yishijie.R;
import com.bby.yishijie.member.adapter.IndexAdHolder;
import com.bby.yishijie.member.adapter.ProductDetailRecAdapter;
import com.bby.yishijie.member.adapter.StringBannerHolder;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.common.Constant;
import com.bby.yishijie.member.common.DividerGridItemDecoration;
import com.bby.yishijie.member.entity.ProductDetail;
import com.bby.yishijie.member.entity.TabEntity;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.base.ShowBigImage;
import com.bby.yishijie.member.ui.cart.CartActivity;
import com.bby.yishijie.member.ui.login.LoginActivity;
import com.bby.yishijie.member.ui.order.OrderConfirmActivity;
import com.bby.yishijie.member.widgets.CountDownView.CountdownView;
import com.bby.yishijie.member.widgets.SelectProSpecWindow;
import com.bby.yishijie.member.widgets.ShareWindow;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 刘涛 on 2017/4/19.
 */

public class ProductDetailActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.banner)
    ConvenientBanner banner;
    @Bind(R.id.product_title)
    TextView productTitle;
    @Bind(R.id.product_price)
    TextView productPrice;
    @Bind(R.id.product_old_price)
    TextView productOldPrice;
    @Bind(R.id.product_store_num)
    TextView productStoreNum;
    @Bind(R.id.select_spec)
    TextView selectSpec;
    @Bind(R.id.tabs)
    TabLayout tabs;
    //    @Bind(R.id.viewpager)
//    ViewPager viewpager;
    @Bind(R.id.img_cart)
    ImageView imgCart;
    @Bind(R.id.add_cart)
    TextView addCart;
    @Bind(R.id.buy_now)
    TextView buyNow;
    @Bind(R.id.bottom_layout)
    LinearLayout bottomLayout;
    @Bind(R.id.nested_scroll_webview1)
    WebView webView1;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.tag_layout)
    AutoLinefeedLayout tagLayout;
    @Bind(R.id.group_num)
    TextView groupNum;
    @Bind(R.id.right_btn)
    ImageView rightBtn;
    @Bind(R.id.view_stub)
    ViewStub viewStub;
    @Bind(R.id.title_tag)
    TextView titleTag;
    @Bind(R.id.view)
    View view;
    @Bind(R.id.pro_ware_house_name)
    TextView houseName;

    private int type=1;//(1-普通商品 2-限时购 3-跨境 4-满减)
    private long productId;
    private long memberId;
    private Long limitId;

    private ProductDetail productDetail;
    private boolean isLogin;
    private int integralFlag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_product_detail);
        ButterKnife.bind(this);
        initParams();
        getProductDetail();

    }

    private void initParams() {
        if (BaseApp.getInstance().getMember() != null) {
            memberId = BaseApp.getInstance().getMember().getId();
            isLogin = true;
        }
        productId = getIntent().getLongExtra("productId", 0);
        type = getIntent().getIntExtra("type", 1);//(1-普通商品 2-限时购 3-跨境 4-满减)
        integralFlag = getIntent().getIntExtra("integralFlag", 0);//1表示积分商城的商品
        limitId = getIntent().getLongExtra("limitId", 0);
        titleView.setText("详情");
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setImageResource(R.mipmap.share);
        banner.getLayoutParams().width = DeviceUtils.getDisplay(mContext).widthPixels;
        banner.getLayoutParams().height = DeviceUtils.getDisplay(mContext).widthPixels;
    }

    private String url;
    private int detailType = 1;

    private void initView() {
        final List<String> bannerImgs = new ArrayList<>();
        url = String.format(ApiClient.DETAIL_URL + "?id=%1$d&type=%2$d", productId, detailType);
        if (productDetail.getImages() != null && productDetail.getImages().size() > 0) {
            bannerImgs.addAll(productDetail.getImages());
        }
        else {
            bannerImgs.add(productDetail.getDetailImage());
        }
        if (bannerImgs.size() > 1) {
            banner.startTurning(3000);
        }
        final StringBannerHolder stringBannerHolder = new StringBannerHolder();
        banner.setPages(new ViewHolderCreator<StringBannerHolder>() {
            @Override
            public StringBannerHolder createHolder() {
                return stringBannerHolder;
            }
        }, bannerImgs)
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused});
        banner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(mContext, ShowBigImage.class);
                intent.putExtra("isShowSave", true);
                intent.putStringArrayListExtra("data", (ArrayList<String>) bannerImgs);
                intent.putExtra("index", position);
                mContext.startActivity(intent);
            }
        });
//        stringBannerHolder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int p = (int) v.getTag();
//                Intent intent = new Intent(mContext, ShowBigImage.class);
//                intent.putStringArrayListExtra("data", (ArrayList<String>) bannerImgs);
//                intent.putExtra("index", p);
//                mContext.startActivity(intent);
//            }
//        });
        banner.notifyDataSetChanged();


        TabLayout.Tab detail = tabs.newTab().setText("商品介绍");
        TabLayout.Tab comment = tabs.newTab().setText("购买须知");
        tabs.addTab(detail);
        tabs.addTab(comment);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    detailType = 1;
                    url = String.format(ApiClient.DETAIL_URL + "?id=%1$d&type=%2$d", productId, detailType);
                    Log.i("-----URL", url);
                    webView1.loadUrl(url);
                } else {
                    detailType = 2;
                    url = String.format(ApiClient.DETAIL_URL + "?id=%1$d&type=%2$d", productId, detailType);
                    Log.i("-----URL", url);
                    webView1.loadUrl(url);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //启用支持javascript
        WebSettings settings = webView1.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(false);
        settings.setSupportZoom(false);
        settings.setDomStorageEnabled(true);
        webView1.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!TextUtils.isEmpty(url)) {
                    view.loadUrl(url);
                } else {
                    dissMissDialog();
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dissMissDialog();
            }
        });
        webView1.loadUrl(url);

        if (productDetail.getProductList() != null && productDetail.getProductList().size() > 0) {
            ProductDetailRecAdapter adapter = new ProductDetailRecAdapter(mContext, productDetail.getProductList());
            GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position == 0) {
                        return 2;
                    } else {
                        return 1;
                    }
                }
            });

            recyclerView.setLayoutManager(layoutManager);
            recyclerView.addItemDecoration(new DividerGridItemDecoration(5));
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        if (productDetail.getType() == 2) {
            viewStub.setLayoutResource(R.layout.layout_prodetail_time_buy);
            viewStub.inflate();
            TextView priceNow1 = (TextView) findViewById(R.id.product_ms_price_now);
            priceNow1.setText(String.format("¥%s", productDetail.getCurrentPrice().setScale(2, RoundingMode.HALF_UP)));
            TextView priceMarket1 = (TextView) findViewById(R.id.product_ms_price_market);
            priceMarket1.setText(String.format("¥%s", productDetail.getPrice().setScale(2, RoundingMode.HALF_UP)));
            priceMarket1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            CountdownView mCvCountdownView = (CountdownView) findViewById(R.id.countdown_view);
            TextView mCvCountdownTextView = (TextView) findViewById(R.id.countdown_txt);
            long currentTime = System.currentTimeMillis();
            Date date = new Date();
            //限时购 未开卖 预热商品可以加入购物车
            if (productDetail.getStatus() != 0) {
                long time = productDetail.getStatus() * 60 * 60 * 1000 - date.getHours() * 60 * 60 * 1000 - date.getMinutes() * 60 * 1000 - date.getSeconds() * 1000;
                //buyNow.setText(productDetail.getStatus() + "点开卖");
                //buyNow.setEnabled(false);
                buyNow.setVisibility(View.GONE);
                addCart.setBackgroundColor(ContextCompat.getColor(mContext, R.color.main_color));
                addCart.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                mCvCountdownTextView.setText("距离开卖：");
                mCvCountdownView.start(time); // Millisecond
            } else {
                long time = 24 * 60 * 60 * 1000 - date.getHours() * 60 * 60 * 1000 - date.getMinutes() * 60 * 1000 - date.getSeconds() * 1000;
                mCvCountdownTextView.setText("距离结束：");
                mCvCountdownView.start(time); // Millisecond
            }
            titleTag.setVisibility(View.VISIBLE);
            titleTag.setText("特卖");
            titleTag.setBackgroundResource(R.drawable.shape_red_stroke);
            titleTag.setTextColor(ContextCompat.getColor(mContext, R.color.main_color));
            productPrice.setVisibility(View.GONE);
            productOldPrice.setVisibility(View.GONE);
            view.setVisibility(View.GONE);

        } else {
            titleTag.setVisibility(View.GONE);
            productPrice.setVisibility(View.VISIBLE);
            productOldPrice.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
            if (integralFlag==1){
                productPrice.setText(productDetail.getScore()+"积分");
                productOldPrice.setText(String.format("¥%s", productDetail.getPrice().setScale(2, RoundingMode.HALF_UP)));
            }
            else{
                productPrice.setText(String.format("¥%s", productDetail.getCurrentPrice().setScale(2, RoundingMode.HALF_UP)));
                productOldPrice.setText(String.format("¥%s", productDetail.getPrice().setScale(2, RoundingMode.HALF_UP)));
                productOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            }
        }
        productTitle.setText(productDetail.getName());
        productStoreNum.setText(String.format("库存%d", productDetail.getProductStore()));

        if (productDetail.getTags() != null && productDetail.getTags().size() > 0) {
            for (String tag : productDetail.getTags()) {
                TextView textView = new TextView(mContext);
                textView.setText(tag);
                textView.setTextColor(ContextCompat.getColor(mContext, R.color.black_3));
                textView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.select, 0, 0, 0);
                textView.setCompoundDrawablePadding(10);
                textView.setPadding(0, 0, 30, 0);
                tagLayout.addView(textView);
            }
        }

        if (productDetail.getProductStore() == 0) {
            productStoreNum.setText("求补货");
            productStoreNum.setBackgroundResource(R.drawable.shape_red_stroke);
            productStoreNum.setTextColor(ContextCompat.getColor(mContext, R.color.main_color));
        }

        houseName.setVisibility(TextUtils.isEmpty(productDetail.getWareHouseName()) ? View.GONE : View.VISIBLE);
        houseName.setText(productDetail.getWareHouseName());

    }


    private void getProductDetail() {
        showLoadingDialog(0);
        Call<ResultDO<ProductDetail>> call = ApiClient.getApiAdapter().getProductDetail(type, productId, limitId == 0 ? null : limitId);
        call.enqueue(new Callback<ResultDO<ProductDetail>>() {
            @Override
            public void onResponse(Call<ResultDO<ProductDetail>> call, Response<ResultDO<ProductDetail>> response) {
                if (isFinish) {
                    return;
                }
                dissMissDialog();
                ResultDO<ProductDetail> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    if (resultDO.getResult() == null) {
                        return;
                    }
                    productDetail = resultDO.getResult();
                    initView();
                } else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                    builder1.setCancelable(false);
                    builder1.setTitle("提示");
                    builder1.setMessage(resultDO.getMessage());
                    builder1.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            dialog.dismiss();
                        }
                    });
                    builder1.show();
                }
            }

            @Override
            public void onFailure(Call<ResultDO<ProductDetail>> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }


    private SelectProSpecWindow specWindow;

    @OnClick({R.id.right_btn, R.id.select_spec, R.id.add_cart, R.id.buy_now, R.id.img_cart, R.id.product_store_num})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_btn:
                if (!isLogin) {
                    showLoginDialog();
                    return;
                }
                showWindow();
                break;
            case R.id.select_spec:
                //产品规格
                if (specWindow == null) {
                    specWindow = new SelectProSpecWindow(mContext, productDetail, type, Constant.TYPE_SELECT);
                }
                specWindow.setBuyType(Constant.TYPE_SELECT);
                specWindow.showPopupWindow(selectSpec);
                break;
            case R.id.add_cart:
                if (!isLogin) {
                    showLoginDialog();
                    return;
                }
                if (specWindow == null) {
                    specWindow = new SelectProSpecWindow(mContext, productDetail, type, Constant.TYPE_ADDCART);
                }

                specWindow.setBuyType(Constant.TYPE_ADDCART);
                specWindow.showPopupWindow(addCart);
                break;
            case R.id.buy_now:
                if (!isLogin) {
                    showLoginDialog();
                    return;
                }
                if (specWindow == null) {
                    specWindow = new SelectProSpecWindow(mContext, productDetail, type, Constant.TYPE_BUY_NOW);
                }
                specWindow.setBuyType(Constant.TYPE_BUY_NOW);
                specWindow.showPopupWindow(buyNow);
                break;
            case R.id.img_cart:
                intent = new Intent(mContext, CartActivity.class);
                startActivity(intent);
                break;
            case R.id.product_store_num:
                if (!isLogin) {
                    showLoginDialog();
                    return;
                }
                if (productDetail.getProductStore() == 0) {
                    applyAddProduct();
                }
                break;
        }
        if (specWindow != null)
            specWindow.setOnSelectListener(new SelectProSpecWindow.OnSelectListener() {
                @Override
                public void onSelect(long paramId, int number, int type) {
                    if (!isLogin) {
                        showLoginDialog();
                        return;
                    }
                    if (type == 1) {
                        addCart(paramId, number);
                    } else if (type == 2) {
                        buyNow(paramId, number);
                    }
                }
            });
    }

    private void applyAddProduct() {
        showLoadingDialog(0);
        Call<ResultDO> call = ApiClient.getApiAdapter().replenishment(productId, BaseApp.getInstance().getMember().getId(),
                1);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                dissMissDialog();
                if (isFinish || response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    ToastUtils.showToast(mContext, "申请补货成功，请耐心等待");
                } else {
                    ToastUtils.showToast(mContext, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                dissMissDialog();
            }
        });
    }

    public void showWindow() {
         /*1我要开店2分享店铺3普通商品4限时购商品)-memberId(会员Id)-productId(商品Id)-limitId(限时购时间段Id)
                    这四个参数没有值的传0
                    例如普通商品分享：http://weixin.zj-yunti.com/authorizationPage.html?param=3-1-14-0*/
        String shareUrl = String.format("%1$s%2$d-%3$d-%4$d-%5$d", ApiClient.SHARE_URL, limitId == 0 ? 3 : 4, BaseApp.getInstance().getMember().getId(), productId, limitId);
        ShareWindow shareWindow = new ShareWindow(mContext, shareUrl, productDetail.getName(), productDetail.getDetailImage(),
                mContext.getResources().getString(R.string.share_product_desc),
                String.format("%s", productDetail.getCurrentPrice().setScale(2, RoundingMode.HALF_UP)),productDetail.getShareImage());
        shareWindow.showPopupWindow(rightBtn);
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

    private void addCart(long paramId, int num) {
        Call<ResultDO> call = ApiClient.getApiAdapter().addCart(productDetail.getType(), productId, paramId, memberId, num);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                if (isFinish) {
                    return;
                }
                if (response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    ToastUtils.showToast(mContext, "加入购物车成功");
                } else {
                    ToastUtils.showToast(mContext, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }

    private void buyNow(long paramId, int num) {
        showLoadingDialog(0);
        Call<ResultDO<CartPay>> call = ApiClient.getApiAdapter().buyNowNew(productDetail.getType(), (int)productId, (int)memberId,(int)paramId,  num);
        call.enqueue(new Callback<ResultDO<CartPay>>() {
            @Override
            public void onResponse(Call<ResultDO<CartPay>> call, Response<ResultDO<CartPay>> response) {
                if (isFinish) {
                    return;
                }
                dissMissDialog();
                if (response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    intent = new Intent(mContext, OrderConfirmActivity.class);
                    intent.putExtra("cartPay", response.body().getResult());
                    intent.putExtra("isActive", productDetail.getType() == 4 ? 1 : 0);
                    startActivity(intent);
                } else {
                    ToastUtils.showToast(mContext, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<CartPay>> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }


    @Override
    protected void onDestroy() {
        banner.stopTurning();
        super.onDestroy();
    }
}
