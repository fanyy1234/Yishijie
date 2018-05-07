package com.bby.yishijie.member.ui.main;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bby.yishijie.member.ui.MainActivity;
import com.bby.yishijie.member.ui.index.IndexClasssifyShopFragment;
import com.sunday.common.base.BaseFragment;
import com.sunday.common.model.ResultDO;
import com.sunday.common.widgets.IndexViewPager;
import com.bby.yishijie.R;
import com.bby.yishijie.member.adapter.ProductClassifyChildAdapter;
import com.bby.yishijie.member.entity.ProductClassify;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.index.BrandFragment;
import com.bby.yishijie.member.ui.index.GlobalBuyFragment;
import com.bby.yishijie.member.ui.index.IndexClasssifyFragment;
import com.bby.yishijie.member.ui.index.TodayFragment;
import com.bby.yishijie.member.ui.product.SearchProductActivity;
import com.bby.yishijie.member.ui.product.SearchProductListActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2017/4/11.
 */

public class IndexFragment extends BaseFragment {

    @Bind(R.id.layout_search)
    LinearLayout layoutSearch;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.appbar_layout)
    AppBarLayout appbarLayout;
    @Bind(R.id.view_pager)
    IndexViewPager viewPager;
    @Bind(R.id.right_btn)
    ImageView rightBtn;

    private List<String> titles = new ArrayList<>();
    private List<ProductClassify> productClassifyList = new ArrayList<>();
    private boolean isShop;
    public static IndexFragment newInstance() {
        IndexFragment fragment = new IndexFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rightBtn.setImageResource(R.mipmap.nav_classify);
        isShop = MainActivity.isShop;
        getTabTitles();
    }


    private void getTabTitles() {
        //
        titles.clear();
        titles.add("今日特卖");
//        titles.add("品牌团");
//        titles.add("跨境商城");
        showLoadingDialog(0);
        Call<ResultDO<List<ProductClassify>>> call = ApiClient.getApiAdapter().getProCat();
        call.enqueue(new Callback<ResultDO<List<ProductClassify>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<ProductClassify>>> call, Response<ResultDO<List<ProductClassify>>> response) {
                if (isFinish) {
                    return;
                }
                dissMissDialog();
                if (response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    if (response.body().getResult() == null) {
                        return;
                    }
                    for (ProductClassify item : response.body().getResult()) {
                        titles.add(item.getName());
                    }
                    productClassifyList.clear();
                    productClassifyList.addAll(response.body().getResult());
                    initFragments();
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<ProductClassify>>> call, Throwable t) {
                dissMissDialog();
            }
        });

    }


    private void initFragments() {
        viewPager.setAdapter(new IndexViewPagerAdapter(getChildFragmentManager()));
        viewPager.setOffscreenPageLimit(titles.size());
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOnCheckCanScrollListener(new IndexViewPager.OnCheckCanScrollListener() {
            @Override
            public boolean isCanScroll() {
                return !checkTabLayoutVisible();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public class IndexViewPagerAdapter extends FragmentPagerAdapter {

        private Fragment[] fragments;

        public IndexViewPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new Fragment[titles.size()];
        }

        @Override
        public Fragment getItem(int position) {
            if (fragments[position] == null) {
                if (position == 0) {
                    if (isShop){
                        fragments[position] = com.bby.yishijie.shop.ui.TodayFragment.newInstance();
                    }
                    else {
                        fragments[position] = TodayFragment.newInstance();
                    }
                }
//                else if (position == 1) {
//                    fragments[position] = BrandFragment.newInstance();
//                }else if (position==2){
//                    fragments[position]= GlobalBuyFragment.newInstance();
//                }
                else {
                    if (isShop){
                        fragments[position] = IndexClasssifyShopFragment.newInstance(productClassifyList.get(position - 1).getId(), productClassifyList.get(position - 1).getName());
                    }
                    else {
                        fragments[position] = IndexClasssifyFragment.newInstance(productClassifyList.get(position - 1).getId(), productClassifyList.get(position - 1).getName());
                    }

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


    public boolean checkTabLayoutVisible() {
        boolean cover = false;
        Rect rect = new Rect();
        cover = tabLayout.getGlobalVisibleRect(rect);
        if (cover) {
            if (rect.width() >= tabLayout.getMeasuredWidth() && rect.height() >= tabLayout.getMeasuredHeight()) {
                return !cover;
            }
        }
        return true;
    }

    @OnClick(R.id.layout_search)
    void onClick() {
        if (MainActivity.isShop){
            intent = new Intent(mContext, com.bby.yishijie.shop.ui.SearchProductListActivity.class);
        }
        else {
            intent = new Intent(mContext, SearchProductListActivity.class);
        }
        startActivity(intent);
    }

    @OnClick(R.id.right_btn)
    void onClick1() {
        intent = new Intent(mContext, SearchProductActivity.class);
        startActivity(intent);
    }

}
