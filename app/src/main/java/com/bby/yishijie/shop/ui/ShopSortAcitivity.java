package com.bby.yishijie.shop.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.shop.entity.Product;
import com.bby.yishijie.shop.entity.ProductBrand;
import com.flyco.tablayout.SlidingTabLayout;
import com.sunday.common.adapter.MainFragmentPagerAdapter;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.StringUtils;
import com.sunday.common.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Author by Damon,  on 2017/12/17.
 */

public class ShopSortAcitivity extends BaseActivity {


    @Bind(R.id.left_btn)
    ImageView leftBtn;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.tab_layout)
    SlidingTabLayout slideTab;


    private ArrayList<Fragment> fragments = new ArrayList<>();
    private final String[] mTitles = {
            "单品", "品牌"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_sort);
        ButterKnife.bind(this);
        initFragments();
    }

    private void initFragments() {
        fragments.add(new ShopProductSortFragment());
        fragments.add(new ShopBrandSortFragment());
        viewPager.setAdapter(new MainFragmentPagerAdapter(getSupportFragmentManager(), mContext, fragments, R.array.shop_manage_title));
        slideTab.setViewPager(viewPager, mTitles, this, fragments);
    }

    @OnClick(R.id.left_btn)
    void onSave() {
        List<Product> products = ((ShopProductSortFragment) fragments.get(0)).getProSet();
        List<ProductBrand> brands = ((ShopBrandSortFragment) fragments.get(1)).getBrandSet();
        List<String> proIds = new ArrayList<>();
        List<String> brandIds = new ArrayList<>();
        if (products != null && !products.isEmpty()) {
            for (Product product : products) {
                proIds.add(String.valueOf(product.getId()));
            }
        }
        if (brands != null && !brands.isEmpty()) {
            for (ProductBrand brand : brands) {
                brandIds.add(String.valueOf(brand.getId()));
            }
        }

        showLoadingDialog(0);
        Call<ResultDO<Integer>> call = ApiClient.getApiAdapter().shopSort(BaseApp.getInstance().getMember().getId(),
                StringUtils.listToString(proIds), StringUtils.listToString(brandIds));
        call.enqueue(new Callback<ResultDO<Integer>>() {
            @Override
            public void onResponse(Call<ResultDO<Integer>> call, Response<ResultDO<Integer>> response) {
                dissMissDialog();
                if (isFinish || response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    finish();
                } else {
                    ToastUtils.showToast(mContext, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Integer>> call, Throwable t) {
                dissMissDialog();
            }
        });
    }


    @Override
    public void onBackPressed() {
        onSave();
        //super.onBackPressed();
    }
}
