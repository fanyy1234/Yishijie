package com.bby.yishijie.member.ui.index;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.DesignViewUtils;
import com.sunday.common.utils.PixUtils;
import com.sunday.common.utils.SpannalbeStringUtils;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.IndexViewPager;
import com.sunday.common.widgets.RecyclerTabLayout;
import com.sunday.common.widgets.banner.ConvenientBanner;
import com.sunday.common.widgets.banner.holder.ViewHolderCreator;
import com.sunday.common.widgets.ptr.PtrClassicFrameLayout;
import com.sunday.common.widgets.ptr.PtrDefaultHandler;
import com.sunday.common.widgets.ptr.PtrFrameLayout;
import com.sunday.common.widgets.ptr.PtrHandler;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;
import com.bby.yishijie.R;
import com.bby.yishijie.member.adapter.BrandListAdapter;
import com.bby.yishijie.member.adapter.GlobalBuyAdapter;
import com.bby.yishijie.member.adapter.GlobalTabAdapter;
import com.bby.yishijie.member.adapter.IndexAdHolder;
import com.bby.yishijie.member.entity.Ad;
import com.bby.yishijie.member.entity.GlobalBrand;
import com.bby.yishijie.member.entity.GlobalCat;
import com.bby.yishijie.member.entity.GlobalIndex;
import com.bby.yishijie.member.entity.IndexProductBrand;
import com.bby.yishijie.member.entity.Product;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.base.BaseLazyFragment;
import com.bby.yishijie.member.ui.product.BrandProductListActivity;
import com.bby.yishijie.member.ui.product.GlobalProListFragment;
import com.bby.yishijie.member.ui.product.ProductDetailActivity;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Author by Damon,  on 2017/12/5.
 */

public class GlobalBuyFragment extends BaseLazyFragment {


    @Bind(R.id.banner)
    ConvenientBanner banner;
    @Bind(R.id.tv_pro_name1)
    TextView tvProName1;
    @Bind(R.id.tv_pro_price1)
    TextView tvProPrice1;
    @Bind(R.id.iv_pro1)
    ImageView ivPro1;
    @Bind(R.id.rl_jingxuan1)
    RelativeLayout rlJingxuan1;
    @Bind(R.id.tv_pro_name2)
    TextView tvProName2;
    @Bind(R.id.tv_pro_price2)
    TextView tvProPrice2;
    @Bind(R.id.iv_pro2)
    ImageView ivPro2;
    @Bind(R.id.rl_jingxuan2)
    RelativeLayout rlJingxuan2;
    @Bind(R.id.tv_pro_name3)
    TextView tvProName3;
    @Bind(R.id.tv_pro_price3)
    TextView tvProPrice3;
    @Bind(R.id.iv_pro3)
    ImageView ivPro3;
    @Bind(R.id.tv_pro_name4)
    TextView tvProName4;
    @Bind(R.id.tv_pro_price4)
    TextView tvProPrice4;
    @Bind(R.id.iv_pro4)
    ImageView ivPro4;
    @Bind(R.id.ll_jingxuan4)
    LinearLayout llJingxuan4;
    @Bind(R.id.tv_pro_name5)
    TextView tvProName5;
    @Bind(R.id.tv_pro_price5)
    TextView tvProPrice5;
    @Bind(R.id.iv_pro5)
    ImageView ivPro5;
    @Bind(R.id.ll_jingxuan5)
    LinearLayout llJingxuan5;
    @Bind(R.id.tv_pro_name6)
    TextView tvProName6;
    @Bind(R.id.tv_pro_price6)
    TextView tvProPrice6;
    @Bind(R.id.iv_pro6)
    ImageView ivPro6;
    @Bind(R.id.ll_jingxuan6)
    LinearLayout llJingxuan6;
    @Bind(R.id.ll_jingxuan3)
    LinearLayout llJingxuan3;
    @Bind(R.id.rl_all_brand)
    RelativeLayout rlAllBrand;
    @Bind(R.id.global_head)
    LinearLayout globalHead;
    @Bind(R.id.brand_recyclerView)
    RecyclerView brandRecyclerView;
    @Bind(R.id.tabs_layout)
    RecyclerTabLayout tabsLayout;
    @Bind(R.id.global_view_pager)
    IndexViewPager viewPager;
    @Bind(R.id.ptr_frame)
    PtrClassicFrameLayout ptrFrame;
    @Bind(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.appbar_layout)
    AppBarLayout appBarLayout;

    private List<IndexProductBrand> brandList = new ArrayList<>();
    private List<Ad> adList = new ArrayList<>();
    private List<GlobalCat> catList = new ArrayList<>();
    private List<Product> productList = new ArrayList<>();
    private BrandListAdapter adapter;


    public static GlobalBuyFragment newInstance() {
        GlobalBuyFragment fragment = new GlobalBuyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isPrepared = true;
        lazyLoad();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_global_buy, container, false);
        ButterKnife.bind(this, view);
        return view;
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
        getData();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //recyclerView.setPadding(0, 0, 0, PixUtils.dip2px(mContext, 40));
    }

    private boolean isAppBarLayoutOpen = true;
    private boolean isAppBarLayoutClose;

    private void initView() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                isAppBarLayoutOpen = DesignViewUtils.isAppBarLayoutOpen(verticalOffset);
                isAppBarLayoutClose = DesignViewUtils.isAppBarLayoutClose(appBarLayout, verticalOffset);
                //dispatchScroll();
            }
        });
        ptrFrame.disableWhenHorizontalMove(true);
        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                //return PtrDefaultHandler.checkContentCanBePulledDown(frame, coordinatorLayout, coordinatorLayout);
                return isAppBarLayoutOpen && PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });

        viewPager.setOnCheckCanScrollListener(new IndexViewPager.OnCheckCanScrollListener() {
            @Override
            public boolean isCanScroll() {
                return !checkTabLayoutVisible();
            }
        });


    }


    public boolean checkTabLayoutVisible() {
        boolean cover = false;
        Rect rect = new Rect();
        cover = tabsLayout.getGlobalVisibleRect(rect);
        if (cover) {
            if (rect.width() >= tabsLayout.getMeasuredWidth() && rect.height() >= tabsLayout.getMeasuredHeight()) {
                return !cover;
            }
        }
        return true;
    }

    private IndexAdHolder indexAdHolder;
    private ViewHolderCreator viewHolderCreator;

    private void updateView() {
        //banner
        indexAdHolder = new IndexAdHolder();
        viewHolderCreator = new ViewHolderCreator<IndexAdHolder>() {
            @Override
            public IndexAdHolder createHolder() {
                return indexAdHolder;
            }
        };
        if (adList != null && !adList.isEmpty()) {
            banner.setPages(viewHolderCreator, adList)
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                    .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused});
            banner.notifyDataSetChanged();
            banner.startTurning(3000);
            if (adList.size() == 1) {
                banner.stopTurning();
            }
        }
        indexAdHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ad item = (Ad) v.getTag();
                if (item.getDetailType() == 1) {
                    if (item.getDetailId() > 0) {
                        Intent intent = new Intent(mContext, ProductDetailActivity.class);
                        intent.putExtra("type", 1);
                        intent.putExtra("productId", item.getDetailId());
                        mContext.startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(mContext, BrandProductListActivity.class);
                    intent.putExtra("brandName", String.format("%s", item.getName()));
                    intent.putExtra("brandId", item.getDetailId());
                    intent.putExtra("brandImg", item.getImage());
                    intent.putExtra("type", item.getType());
                    mContext.startActivity(intent);
                }
            }
        });

        //全球精选
        if (productList != null && productList.size() > 0) {
            DecimalFormat df = new DecimalFormat("#.00");
            SpannableString str = SpannalbeStringUtils.setTextSizePx("¥", PixUtils.sp2px(mContext, 12));
            Glide.with(mContext)
                    .load(productList.get(0).getDetailImage())
                    .error(R.mipmap.ic_default)
                    .into(ivPro1);
            tvProName1.setText(productList.get(0).getName());
            tvProPrice1.setText(str);
            tvProPrice1.append(df.format(productList.get(0).getCurrentPrice().doubleValue()));
            Glide.with(mContext)
                    .load(productList.get(1).getDetailImage())
                    .error(R.mipmap.ic_default)
                    .into(ivPro2);
            tvProName2.setText(productList.get(1).getName());
            tvProPrice2.setText(str);
            tvProPrice2.append(df.format(productList.get(1).getCurrentPrice().doubleValue()));

            Glide.with(mContext)
                    .load(productList.get(2).getDetailImage())
                    .error(R.mipmap.ic_default)
                    .into(ivPro3);
            tvProName3.setText(productList.get(2).getName());
            tvProPrice3.setText(str);
            tvProPrice3.append(df.format(productList.get(2).getCurrentPrice().doubleValue()));

            Glide.with(mContext)
                    .load(productList.get(3).getDetailImage())
                    .error(R.mipmap.ic_default)
                    .into(ivPro4);
            tvProName4.setText(productList.get(3).getName());
            tvProPrice4.setText(str);
            tvProPrice4.append(df.format(productList.get(3).getCurrentPrice().doubleValue()));

            Glide.with(mContext)
                    .load(productList.get(4).getDetailImage())
                    .error(R.mipmap.ic_default)
                    .into(ivPro5);
            tvProName5.setText(productList.get(4).getName());
            tvProPrice5.setText(str);
            tvProPrice5.append(df.format(productList.get(4).getCurrentPrice().doubleValue()));

            Glide.with(mContext)
                    .load(productList.get(5).getDetailImage())
                    .error(R.mipmap.ic_default)
                    .into(ivPro6);
            tvProName6.setText(productList.get(5).getName());
            tvProPrice6.setText(str);
            tvProPrice6.append(df.format(productList.get(5).getCurrentPrice().doubleValue()));

            rlJingxuan1.setTag(productList.get(0));
            rlJingxuan2.setTag(productList.get(1));
            llJingxuan3.setTag(productList.get(2));
            llJingxuan4.setTag(productList.get(3));
            llJingxuan5.setTag(productList.get(4));
            llJingxuan6.setTag(productList.get(5));

        }

        //品牌特卖
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        brandRecyclerView.setLayoutManager(layoutManager);
        brandRecyclerView.setNestedScrollingEnabled(false);
        adapter = new BrandListAdapter(mContext, brandList);
        brandRecyclerView.setAdapter(adapter);
        brandRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .drawable(R.drawable.shape_divider)
                .build());
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IndexProductBrand productBrand = (IndexProductBrand) v.getTag();
                intent = new Intent(mContext, BrandProductListActivity.class);
                intent.putExtra("type", productBrand.getType());
                intent.putExtra("brandName", String.format("%s", productBrand.getName()));
                intent.putExtra("brandId", productBrand.getId());
                intent.putExtra("brandImg", productBrand.getBigImage());
                startActivity(intent);
            }
        });

        //产品推荐
        tabsLayout.removeAllViews();
        viewPager.removeAllViews();
        ArrayList<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        PagerAdapter pageAdapter;
        for (int i = 0; i < catList.size(); i++) {
            fragments.add(GlobalProListFragment.newInstance(catList.get(i).getCatId()));
            titles.add(catList.get(i).getName());
        }
        pageAdapter = new PagerAdapter(getChildFragmentManager());
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) tabsLayout.getLayoutParams();
        tabsLayout.setLayoutParams(params);
        viewPager.setAdapter(pageAdapter);
        tabsLayout.setUpWithAdapter(new GlobalTabAdapter(viewPager, mContext, catList));
        //viewPager.setOffscreenPageLimit(catList.size());


        CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        appBarLayout.setLayoutParams(layoutParams);

    }

    @OnClick({R.id.rl_jingxuan1, R.id.rl_jingxuan2, R.id.ll_jingxuan3, R.id.ll_jingxuan4, R.id.ll_jingxuan5, R.id.ll_jingxuan6})
    void onClick(View v) {
        Product item = (Product) v.getTag();
        switch (v.getId()) {
            case R.id.rl_jingxuan1:
            case R.id.rl_jingxuan2:
            case R.id.ll_jingxuan3:
            case R.id.ll_jingxuan4:
            case R.id.ll_jingxuan5:
            case R.id.ll_jingxuan6:
                if (item.getId() != 0) {
                    intent = new Intent(mContext, ProductDetailActivity.class);
                    intent.putExtra("type", item.getType());
                    intent.putExtra("productId", item.getId());
                    mContext.startActivity(intent);
                }
                break;
        }
    }


    public class PagerAdapter extends FragmentPagerAdapter {

        private Fragment[] fragments;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new Fragment[catList.size()];
        }

        @Override
        public Fragment getItem(int position) {
            if (fragments[position] == null) {
                fragments[position] = GlobalProListFragment.newInstance(catList.get(position).getCatId());
            }
            return fragments[position];
        }

        @Override
        public int getCount() {
            return catList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return catList.get(position).getName();
        }
    }

    private void getData() {
        Call<ResultDO<GlobalIndex>> call = ApiClient.getApiAdapter().getGlobalIndex();
        call.enqueue(new Callback<ResultDO<GlobalIndex>>() {
            @Override
            public void onResponse(Call<ResultDO<GlobalIndex>> call, Response<ResultDO<GlobalIndex>> response) {
                if (isFinish) {
                    return;
                }
                ptrFrame.refreshComplete();
                if (response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    GlobalIndex resultDO = response.body().getResult();
                    brandList.clear();
                    adList.clear();
                    catList.clear();
                    productList.clear();
                    brandList.addAll(resultDO.getBrands());
                    adList.addAll(resultDO.getAdvs());
                    catList.addAll(resultDO.getCats());
                    productList.addAll(resultDO.getProducts());
                    if (productList.size() < 6) {
                        switch (productList.size()) {
                            case 0:
                                rlJingxuan1.setVisibility(View.GONE);
                                rlJingxuan2.setVisibility(View.GONE);
                                llJingxuan3.setVisibility(View.GONE);
                                llJingxuan4.setVisibility(View.GONE);
                                llJingxuan5.setVisibility(View.GONE);
                                llJingxuan6.setVisibility(View.GONE);
                                break;
                            case 1:
                                rlJingxuan1.setVisibility(View.VISIBLE);
                                rlJingxuan2.setVisibility(View.GONE);
                                llJingxuan3.setVisibility(View.GONE);
                                llJingxuan4.setVisibility(View.GONE);
                                llJingxuan5.setVisibility(View.GONE);
                                llJingxuan6.setVisibility(View.GONE);
                                break;
                            case 2:
                                rlJingxuan1.setVisibility(View.VISIBLE);
                                rlJingxuan2.setVisibility(View.VISIBLE);
                                llJingxuan3.setVisibility(View.GONE);
                                llJingxuan4.setVisibility(View.GONE);
                                llJingxuan5.setVisibility(View.GONE);
                                llJingxuan6.setVisibility(View.GONE);
                                break;
                            case 3:
                                rlJingxuan1.setVisibility(View.VISIBLE);
                                rlJingxuan2.setVisibility(View.VISIBLE);
                                llJingxuan3.setVisibility(View.VISIBLE);
                                llJingxuan4.setVisibility(View.GONE);
                                llJingxuan5.setVisibility(View.GONE);
                                llJingxuan6.setVisibility(View.GONE);
                                break;
                            case 4:
                                rlJingxuan1.setVisibility(View.VISIBLE);
                                rlJingxuan2.setVisibility(View.VISIBLE);
                                llJingxuan3.setVisibility(View.VISIBLE);
                                llJingxuan4.setVisibility(View.VISIBLE);
                                llJingxuan5.setVisibility(View.GONE);
                                llJingxuan6.setVisibility(View.GONE);
                                break;
                            case 5:
                                rlJingxuan1.setVisibility(View.VISIBLE);
                                rlJingxuan2.setVisibility(View.VISIBLE);
                                llJingxuan3.setVisibility(View.VISIBLE);
                                llJingxuan4.setVisibility(View.VISIBLE);
                                llJingxuan5.setVisibility(View.VISIBLE);
                                llJingxuan6.setVisibility(View.GONE);
                                break;
                        }
                        for (int i = 0; i < 6 - resultDO.getProducts().size(); i++) {
                            Product product = new Product();
                            product.setId(0);
                            product.setDetailImage("");
                            product.setCurrentPrice(BigDecimal.ZERO);
                            product.setName("");
                            productList.add(product);
                        }
                    }
                    updateView();
                } else {
                    ToastUtils.showToast(mContext, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<GlobalIndex>> call, Throwable t) {
                ptrFrame.refreshComplete();
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
