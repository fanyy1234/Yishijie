package com.bby.yishijie.member.ui.product;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.adapter.StringBannerHolder;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.common.DividerGridItemDecoration;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.base.ShowBigImage;
import com.bby.yishijie.member.ui.cart.CartActivity;
import com.bby.yishijie.shop.adapter.ProductDetailRecAdapter;
import com.bby.yishijie.shop.entity.CartPay;
import com.bby.yishijie.shop.entity.ProductDetail;
import com.bby.yishijie.shop.ui.OrderConfirmShopActivity;
import com.bby.yishijie.shop.widgets.SelectProSpecWindow;
import com.bby.yishijie.shop.widgets.ShareWindow;
import com.sunday.common.base.BaseFragment;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.AutoLinefeedLayout;
import com.sunday.common.widgets.banner.ConvenientBanner;
import com.sunday.common.widgets.banner.holder.ViewHolderCreator;
import com.sunday.common.widgets.banner.listener.OnItemClickListener;
import com.sunday.common.widgets.countdownview.CountdownView;

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
 * Created by 刘涛 on 2017/5/3.
 */

public class ProductDetailFragment extends BaseFragment {


    @Bind(R.id.banner)
    ConvenientBanner banner;
    @Bind(R.id.product_title)
    TextView productTitle;
    @Bind(R.id.product_price)
    TextView productPrice;
    @Bind(R.id.product_old_price)
    TextView productOldPrice;
    @Bind(R.id.product_sale_num)
    TextView productSaleNum;
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
    @Bind(R.id.btn_buy)
    TextView btnBuy;
    @Bind(R.id.btn_sell)
    TextView btnSell;
    @Bind(R.id.bottom_layout)
    LinearLayout bottomLayout;
    @Bind(R.id.nested_scroll_webview1)
    WebView webView1;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.tag_layout)
    AutoLinefeedLayout tagLayout;
    @Bind(R.id.product_selling_num)
    TextView proSellingNum;
    @Bind(R.id.view_stub)
    ViewStub viewStub;
    @Bind(R.id.title_tag)
    TextView titleTag;
    @Bind(R.id.price_layout)
    LinearLayout priceLayout;
    @Bind(R.id.title_tag1)
    TextView titleTag1;
    @Bind(R.id.pro_ware_house_name)
    TextView houseName;

    private long productId, memberId;
    private int type;//(1-普通商品 2-限时购 4-钻石专享)
    private long limitId;
    private ProductDetail productDetail;

    public static ProductDetailFragment newInstance(long productId, int type, long limitId) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putLong("productId", productId);
        args.putInt("type", type);
        args.putLong("limitId", limitId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_product_detail, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initParams();
        getProductDetail();
    }

    private void initParams() {
        if (getArguments() != null) {
            productId = getArguments().getLong("productId");
            type = getArguments().getInt("type");
            limitId = getArguments().getLong("limitId");
        }
        memberId = BaseApp.getInstance().getMember().getId();
        banner.getLayoutParams().width = DeviceUtils.getDisplay(mContext).widthPixels;
        banner.getLayoutParams().height = DeviceUtils.getDisplay(mContext).widthPixels;
    }


    private String url;
    private int detailType = 1;//1：商品介绍  2：购买须知

    private void initView() {
        if (productDetail == null) {
            return;
        }
        type = productDetail.getType();
        final List<String> bannerImgs = new ArrayList<>();
        url = String.format(ApiClient.DETAIL_URL + "id=%1$d&type=%2$d", productId, detailType);
        if (productDetail.getImages() != null && productDetail.getImages().size() > 0) {
            bannerImgs.addAll(productDetail.getImages());
        }
        else {
            bannerImgs.add(productDetail.getDetailImage());
        }
        banner.startTurning(3000);
        banner.setPages(new ViewHolderCreator<StringBannerHolder>() {
            @Override
            public StringBannerHolder createHolder() {
                return new StringBannerHolder();
            }
        }, bannerImgs)
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused});

        banner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(mContext, ShowBigImage.class);
                intent.putExtra("isShowSave",true);
                intent.putStringArrayListExtra("data", (ArrayList<String>) bannerImgs);
                intent.putExtra("index", position);
                mContext.startActivity(intent);
            }
        });
        banner.notifyDataSetChanged();

        productPrice.setText(String.format("¥%s", productDetail.getCurrentPrice().setScale(2, RoundingMode.HALF_UP)));
        productOldPrice.setText(String.format("%s", productDetail.getScale().setScale(2, RoundingMode.HALF_UP)));
        productSaleNum.setText(String.format("已售%d", productDetail.getSaleNum()));
        productStoreNum.setText(String.format("库存%d", productDetail.getProductStore()));
        proSellingNum.setText(String.format("在售人数%d", productDetail.getSaleNum()));
        TabLayout.Tab detail = tabs.newTab().setText("商品介绍");
        TabLayout.Tab comment = tabs.newTab().setText("购买须知");
        tabs.addTab(detail);
        tabs.addTab(comment);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    detailType = 1;
                    url = String.format(ApiClient.DETAIL_URL + "id=%1$d&type=%2$d", productId, detailType);
                    Log.i("-----URL", url);
                    webView1.loadUrl(url);
                } else {
                    detailType = 2;
                    url = String.format(ApiClient.DETAIL_URL + "id=%1$d&type=%2$d", productId, detailType);
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
        //限时购 区分是否开卖
//        if (productDetail.getStatus() != 0) {
//            btnBuy.setVisibility(View.GONE);
//            btnSell.setText(productDetail.getStatus()+"点开卖");
//            btnSell.setEnabled(false);
//        }

        if (productDetail.getType() == 2) {
            viewStub.setLayoutResource(R.layout.layout_prodetail_time_buy);
            viewStub.inflate();
            TextView priceNow1 = (TextView) getActivity().findViewById(R.id.product_ms_price_now);
            priceNow1.setText(String.format("¥%s", productDetail.getCurrentPrice().setScale(2, RoundingMode.HALF_UP)));
            TextView priceMarket1 = (TextView) getActivity().findViewById(R.id.product_ms_price_market);
            priceMarket1.setText(String.format("赚%s", productDetail.getScale().setScale(2, RoundingMode.HALF_UP)));
            CountdownView mCvCountdownView = (CountdownView) getActivity().findViewById(R.id.countdown_view);
            TextView mCvCountdownTextView = (TextView) getActivity().findViewById(R.id.countdown_txt);
            long currentTime = System.currentTimeMillis();
            Date date = new Date();
            //限时购 未开卖 预热商品可以加入购物车
            if (productDetail.getStatus() != 0) {
                long time = productDetail.getStatus() * 60 * 60 * 1000 - date.getHours() * 60 * 60 * 1000 - date.getMinutes() * 60 * 1000 - date.getSeconds() * 1000;
                //buyNow.setText(productDetail.getStatus() + "点开卖");
                //buyNow.setEnabled(false);
                mCvCountdownTextView.setText("距离开卖：");
                mCvCountdownView.start(time); // Millisecond
            } else {
                long time = 24 * 60 * 60 * 1000 - date.getHours() * 60 * 60 * 1000 - date.getMinutes() * 60 * 1000 - date.getSeconds() * 1000;
                mCvCountdownTextView.setText("距离结束：");
                mCvCountdownView.start(time); // Millisecond
            }
            titleTag.setVisibility(View.VISIBLE);
            priceLayout.setVisibility(View.GONE);
            if (productDetail.getIsScore() == 1) {
                titleTag1.setVisibility(View.VISIBLE);
                productTitle.setText("\u3000\u3000\u3000\u3000\u3000\u3000\u3000" + productDetail.getName());
            } else {
                titleTag1.setVisibility(View.GONE);
                productTitle.setText("\u3000\u3000 " + productDetail.getName());
            }

        } else {
            if (productDetail.getIsScore() == 1) {
                titleTag1.setVisibility(View.VISIBLE);
                productTitle.setText("\u3000\u3000\u3000\u3000\u3000" + productDetail.getName());
            } else {
                titleTag1.setVisibility(View.GONE);
                productTitle.setText(productDetail.getName());
            }
            titleTag.setVisibility(View.GONE);
            priceLayout.setVisibility(View.VISIBLE);
        }

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

        if (productDetail.getProductStore()==0){
            productStoreNum.setText("求补货");
            productStoreNum.setBackgroundResource(R.drawable.shape_red_stroke);
            productStoreNum.setTextColor(ContextCompat.getColor(mContext,R.color.main_color));
        }

        houseName.setVisibility(TextUtils.isEmpty(productDetail.getWareHouseName()) ? View.GONE : View.VISIBLE);
        houseName.setText(productDetail.getWareHouseName());


    }

    private void getProductDetail() {
        showLoadingDialog(0);
        Call<ResultDO<ProductDetail>> call = ApiClient.getApiAdapter().getProductDetail2(type, productId, limitId == 0 ? null : limitId, memberId);
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
                    ToastUtils.showToast(mContext, resultDO.getMessage());
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

    @OnClick({R.id.img_cart, R.id.btn_buy, R.id.btn_sell, R.id.select_spec,R.id.product_store_num})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_cart:
                intent = new Intent(mContext, CartActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_buy:
                //立即购买和加入购物车 type区分普通商品和限时购
                if (specWindow == null) {
                    specWindow = new SelectProSpecWindow(mContext, productDetail, type);
                }
                specWindow.showPopupWindow(selectSpec);

                break;
            case R.id.btn_sell:
                //显示分享面板
                 /*1我要开店2分享店铺3普通商品4限时购商品)-memberId(会员Id)-productId(商品Id)-limitId(限时购时间段Id)
                    这四个参数没有值的传0
                    例如普通商品分享：http://weixin.zj-yunti.com/authorizationPage.html?param=3-1-14-0*/
                if (productDetail == null) {
                    return;
                }
                String shareUrl = String.format("%1$s%2$d-%3$d-%4$d-%5$d", ApiClient.SHARE_URL, limitId == 0 ? 3 : 4, memberId, productId, limitId);
                ShareWindow shareWindow = new ShareWindow(mContext, shareUrl, productDetail.getName(), productDetail.getDetailImage(),
                        String.format("%s", productDetail.getScale().setScale(2, RoundingMode.HALF_UP)),
                        mContext.getResources().getString(R.string.share_product_desc), String.format("%s", productDetail.getCurrentPrice().setScale(2, RoundingMode.HALF_UP)),
                        productDetail.getShareImage());
                shareWindow.showPopupWindow(btnSell);
                break;
            case R.id.select_spec:
                //产品规格
                if (specWindow == null) {
                    specWindow = new SelectProSpecWindow(mContext, productDetail, type);
                }
                specWindow.showPopupWindow(selectSpec);
                break;
            case R.id.product_store_num:
                if (productDetail.getProductStore()==0){
                    applyAddProduct();
                }
                break;
        }
        if (specWindow != null)
            specWindow.setOnSelectListener(new SelectProSpecWindow.OnSelectListener() {
                @Override
                public void onSelect(long paramId, int number, int buyType) {
                    if (buyType == 1) {
                        addCart(paramId, number);
                    } else {
                        buyNow(paramId, number);
                    }
                }
            });
    }

    private void applyAddProduct(){
        showLoadingDialog(0);
        Call<ResultDO>call=ApiClient.getApiAdapter().replenishment(productId,BaseApp.getInstance().getMember().getId(),
                2);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                dissMissDialog();
                if (isFinish||response.body()==null){
                    return;
                }
                if (response.body().getCode()==0){
                    ToastUtils.showToast(mContext,"申请补货成功，请耐心等待");
                }else{
                    ToastUtils.showToast(mContext,response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                dissMissDialog();
            }
        });
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
                }else {
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
        Call<ResultDO<CartPay>> call = ApiClient.getApiAdapter().buyNowNew2(productDetail.getType(), (int)productId, (int)memberId,(int)paramId,  num);
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
                    intent = new Intent(mContext, OrderConfirmShopActivity.class);
                    intent.putExtra("cartPay", response.body().getResult());
                    intent.putExtra("isActive",productDetail.getType()==4?1:0);
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
    public void onDestroy() {
        banner.stopTurning();
        super.onDestroy();
    }
}
