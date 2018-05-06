package com.bby.yishijie.member.ui.product;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.shop.entity.ProductDetail;
import com.bby.yishijie.shop.widgets.ShareWindow;
import com.sunday.common.adapter.MainFragmentPagerAdapter;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.ViewPager;

import java.math.RoundingMode;
import java.util.ArrayList;
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

public class ProductDetailsActivity extends BaseActivity {

    @Bind({R.id.tab1, R.id.tab2})
    List<TextView> tabViews;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.right_menu1)
    ImageView rightMenu1;


    private int type = 1;//(1-普通商品 2-限时购 3-跨境 4-满减)
    private long productId;
    private Long limitId;
    private String productName, productDetailImg, scale, productPrice;
    private int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        ButterKnife.bind(this);
        initParams();
        initView();
    }

    private void initParams() {
        productId = getIntent().getLongExtra("productId", 0);
        type = getIntent().getIntExtra("type", 1);
        limitId = getIntent().getLongExtra("limitId", 0);//限时购id
        productName = getIntent().getStringExtra("productName");
        productDetailImg = getIntent().getStringExtra("productImg");
        scale = getIntent().getStringExtra("scale");
        productPrice = getIntent().getStringExtra("productPrice");
        page = getIntent().getIntExtra("page", 0);
        getProductDetail();
    }

    private void initView() {
        ArrayList<Fragment> fragments = new ArrayList<>(2);
        fragments.add(ProductDetailFragment.newInstance(productId, type, limitId));
        fragments.add(ProductMaterialFragment.newInstance(productId, productName));
        tabViews.get(page).setSelected(true);
        viewPager.setAdapter(new MainFragmentPagerAdapter(getSupportFragmentManager(), fragments));
        viewPager.setCurrentItem(page, false);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < tabViews.size(); i++) {
                    tabViews.get(i).setSelected(false);
                }
                tabViews.get(position).setSelected(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    ProductDetail productDetail;

    private void getProductDetail() {
        Call<ResultDO<ProductDetail>> call = ApiClient.getApiAdapter().getProductDetail2(type, productId, limitId == 0 ? null : limitId);
        call.enqueue(new Callback<ResultDO<ProductDetail>>() {
            @Override
            public void onResponse(Call<ResultDO<ProductDetail>> call, Response<ResultDO<ProductDetail>> response) {
                if (isFinish) {
                    return;
                }
                ResultDO<ProductDetail> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    if (resultDO.getResult() == null) {
                        return;
                    }
                    productDetail = resultDO.getResult();
                    productDetailImg = resultDO.getResult().getDetailImage();
                    productName = resultDO.getResult().getName();
                    productPrice = String.format("%s", resultDO.getResult().getCurrentPrice().setScale(2, RoundingMode.HALF_UP));
                    scale = String.format("%s", resultDO.getResult().getScale().setScale(2, RoundingMode.HALF_UP));
                } else {
                    ToastUtils.showToast(mContext, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<ProductDetail>> call, Throwable t) {
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }


    @OnClick({R.id.tab1, R.id.tab2, R.id.left_menu, R.id.right_menu1})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab1:
                viewPager.setCurrentItem(0, false);
                break;
            case R.id.tab2:
                viewPager.setCurrentItem(1, false);
                break;
            case R.id.right_menu1:
                 /*1我要开店2分享店铺3普通商品4限时购商品)-memberId(会员Id)-productId(商品Id)-limitId(限时购时间段Id)
                    这四个参数没有值的传0
                    例如普通商品分享：http://weixin.zj-yunti.com/authorizationPage.html?param=3-1-14-0*/
                 if (BaseApp.getInstance().getShopMember()==null){
                     ToastUtils.showToast(mContext,"请先登录");
                     return;
                 }
                showWindow();
                break;
//            case R.id.right_menu2:
//                intent=new Intent(mContext,EditProductMaterialActivity.class);
//                intent.putExtra("productId",productId);
//                intent.putExtra("productName",productName);
//                startActivity(intent);
//                break;
            case R.id.left_menu:
                finish();
                break;
        }
    }

    public void showWindow() {
        String shareUrl = String.format("%1$s%2$d-%3$d-%4$d-%5$d", ApiClient.SHARE_URL, limitId == 0 ? 3 : 4, BaseApp.getInstance().getShopMember().getId(), productId, limitId);
        ShareWindow shareWindow = new ShareWindow(mContext, shareUrl, productName, productDetailImg, scale,
                mContext.getResources().getString(R.string.share_product_desc), productPrice, productDetail.getShareImage());
        shareWindow.showPopupWindow(rightMenu1);
    }


}
