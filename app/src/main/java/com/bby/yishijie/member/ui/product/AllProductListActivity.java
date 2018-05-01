package com.bby.yishijie.member.ui.product;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bby.yishijie.member.ui.MainActivity;
import com.bby.yishijie.member.ui.index.ProductListShopFragment;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.ToastUtils;
import com.bby.yishijie.R;
import com.bby.yishijie.member.entity.ProductClassify;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.index.ProductListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.design.widget.TabLayout.MODE_SCROLLABLE;

/**
 * Created by 刘涛 on 2017/9/19.
 */

public class AllProductListActivity extends BaseActivity {


    @Bind(R.id.left_btn)
    ImageView leftBtn;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.right_btn)
    ImageView rightBtn;
    @Bind(R.id.common_header)
    RelativeLayout commonHeader;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.rightTxt)
    TextView rightTxt;
    private String parentName;
    private long parentId;//一级分类id
    private long catId;//二级分类id


    private List<String> titles = new ArrayList<>();
    private List<ProductClassify> productClassifyList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        ButterKnife.bind(this);
        parentName = getIntent().getStringExtra("parentName");
        parentId = getIntent().getLongExtra("parentId", 0);
        catId = getIntent().getLongExtra("catId", 0);
        titleView.setText(parentName);
        initView();
        getTitles();
    }

    private void getTitles() {
        Call<ResultDO<List<ProductClassify>>> call = ApiClient.getApiAdapter().getSubCat(parentId);
        call.enqueue(new Callback<ResultDO<List<ProductClassify>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<ProductClassify>>> call, Response<ResultDO<List<ProductClassify>>> response) {
                if (isFinish || response.body() == null) {
                    return;
                }
                ResultDO<List<ProductClassify>> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    productClassifyList.clear();
                    productClassifyList.addAll(resultDO.getResult());
                    if (!productClassifyList.isEmpty())
                        for (ProductClassify category : productClassifyList) {
                            titles.add(category.getName());
                            initView();
                        }

                } else {
                    ToastUtils.showToast(mContext, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<ProductClassify>>> call, Throwable t) {
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }

    private void initView() {
        tabLayout.setTabMode(MODE_SCROLLABLE);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(MODE_SCROLLABLE);
        int page = 0;
        if (catId == 0) {
            page = 0;
        } else {
            for (int i = 0; i < productClassifyList.size(); i++) {
                if (productClassifyList.get(i).getId() == catId) {
                    page = i;
                    break;
                }
            }
        }
        viewPager.setCurrentItem(page, false);


    }

    public class PagerAdapter extends FragmentPagerAdapter {

        private Fragment[] fragments;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new Fragment[titles.size()];
        }

        @Override
        public Fragment getItem(int position) {
            if (fragments[position] == null) {
                if (MainActivity.isShop){
                    fragments[position] = ProductListShopFragment.newInstance(productClassifyList.get(position).getId());
                }
                else {
                    fragments[position] = ProductListFragment.newInstance(productClassifyList.get(position).getId());
                }

            }
            return fragments[position];
        }

        @Override
        public int getCount() {
            return titles.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}
