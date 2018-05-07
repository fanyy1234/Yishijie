package com.bby.yishijie.shop.ui;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.shop.adapter.IndexAdHolder;
import com.bby.yishijie.shop.adapter.LimitTimeTabAdapter;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.MainActivity;
import com.bby.yishijie.member.ui.main.IndexFragment;
import com.bby.yishijie.member.ui.product.BrandActivity;
import com.bby.yishijie.member.ui.product.BrandProductListActivity;
import com.bby.yishijie.member.ui.product.JingxuanProductActivity;
import com.bby.yishijie.member.ui.product.ProductDetailsActivity;
import com.bby.yishijie.shop.adapter.IndexTodaysAdapter;
import com.bby.yishijie.shop.adapter.LimitTimePageAdapter;
import com.bby.yishijie.shop.entity.Ad;
import com.bby.yishijie.shop.entity.IndexAd;
import com.bby.yishijie.shop.entity.IndexProductBrand;
import com.bby.yishijie.shop.entity.LimitProduct;
import com.bby.yishijie.shop.entity.LimitTime;
import com.bby.yishijie.shop.entity.Product;
import com.sunday.common.base.BaseFragment;
import com.sunday.common.model.ResultDO;
import com.sunday.common.widgets.RecyclerTabLayout;
import com.sunday.common.widgets.banner.ConvenientBanner;
import com.sunday.common.widgets.banner.holder.ViewHolderCreator;
import com.sunday.common.widgets.ptr.PtrClassicFrameLayout;
import com.sunday.common.widgets.ptr.PtrFrameLayout;
import com.sunday.common.widgets.ptr.PtrHandler;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 刘涛 on 2017/4/11.
 * <p>
 * 首页今日特卖
 */


public class TodayFragment extends BaseFragment {


    @Bind(R.id.banner)
    ConvenientBanner banner;
    @Bind(R.id.id_stickynavlayout_topview)
    LinearLayout header;
    @Bind(R.id.recycler_tablayout)
    RecyclerTabLayout recyclerTabLayout;
    @Bind(R.id.recycler_tablayout_view_pager)
    ViewPager viewPager;
    @Bind(R.id.id_stickynavlayout_indicator)
    LinearLayout linearLayout;
    @Bind(R.id.id_stickynavlayout_viewpager)
    RecyclerView recyclerView;
    @Bind(R.id.ptr_frame)
    PtrClassicFrameLayout ptrFrame;
    @Bind(R.id.pinpaiguan_view)
    RelativeLayout pinpaiguanView;
    @Bind(R.id.today_gold_price)
    TextView todayGoldPrice;
    @Bind(R.id.shitidian_view)
    RelativeLayout shitidianView;
    @Bind(R.id.jinxuan_view)
    RelativeLayout jinxuanView;
    @Bind(R.id.shitidian_text)
    TextView shitidianText;
    @Bind(R.id.shitidian_text1)
    TextView shitidianText1;
    @Bind(R.id.jingxuan_text)
    TextView jingxuanText;
    @Bind(R.id.jingxuan_text1)
    TextView jingxuanText1;
    @Bind(R.id.shitidian_pic)
    ImageView shitidianPic;
    @Bind(R.id.jingxuan_pic)
    ImageView jingxuanPic;


    private LinearLayoutManager layoutManager;
    private IndexTodaysAdapter adapter;
    private List<IndexProductBrand> brandList = new ArrayList<>();
    private List<Product> productList = new ArrayList<>();
    private List<LimitTime> timeList = new ArrayList<>();
    private LimitTimePageAdapter pageAdapter;

    public static TodayFragment newInstance() {
        TodayFragment fragment = new TodayFragment();
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
        rootView = inflater.inflate(R.layout.fragment_today, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initRecylcerTab();
        todayGoldPrice();
        getHeadData();
        getLimitTimeList();
    }




    private void initView() {
        adapter = new IndexTodaysAdapter(mContext,brandList,productList);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext).drawable(R.drawable.shape_divider_width).build());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getHeadData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                //return PtrDefaultHandler.checkContentCanBePulledDown(frame,content , header);
                if (!((IndexFragment)getParentFragment()).checkTabLayoutVisible()&&!checkBannerVisible()){
                    return true;
                }
                return false;
            }
        });
        ptrFrame.disableWhenHorizontalMove(true);
        if (MainActivity.isShop) {
            shitidianText.setText("新品速递");
            shitidianText1.setText("新品抢先看");
            shitidianPic.setImageResource(R.mipmap.xinpinsudi);
            jingxuanText.setText("龙虎榜");
            jingxuanText1.setText("推荐店主龙虎榜");
            jingxuanPic.setImageResource(R.mipmap.longhubang);
        }
        pinpaiguanView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BrandActivity.class);
                startActivity(intent);
            }
        });
        shitidianView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.isShop) {
                    Intent intent = new Intent(getActivity(), JingxuanProductListActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), ShitidianActivity.class);
                    startActivity(intent);
                }
            }
        });
        jinxuanView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.isShop) {
                    Intent intent = new Intent(getActivity(), LonghubangActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), JingxuanProductActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public boolean checkBannerVisible(){
        boolean cover = false;
        Rect rect = new Rect();
        cover = banner.getGlobalVisibleRect(rect);
        if (cover) {
            if (rect.width() >= banner.getMeasuredWidth() && rect.height() >= banner.getMeasuredHeight()) {
                return !cover;
            }
        }
        return true;
    }

    private void initRecylcerTab() {
        pageAdapter = new LimitTimePageAdapter(mContext, timeList);
        viewPager.setAdapter(pageAdapter);
        recyclerTabLayout.setUpWithAdapter(new LimitTimeTabAdapter(viewPager));
        recyclerTabLayout.setAutoSelectionMode(true);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) recyclerTabLayout.getLayoutParams();
        recyclerTabLayout.setLayoutParams(params);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                timeLimitId = timeList.get(position).getId();
                getProducts();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void getHeadData() {
        Call<ResultDO<IndexAd>> call = ApiClient.getApiAdapter().getAdveList2();
        call.enqueue(new Callback<ResultDO<IndexAd>>() {
            @Override
            public void onResponse(Call<ResultDO<IndexAd>> call, Response<ResultDO<IndexAd>> response) {
                if (isFinish) {
                    return;
                }
                ptrFrame.refreshComplete();
                ResultDO<IndexAd> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    updateAds(resultDO.getResult());
                    getLimitTimeList();
                }
            }

            @Override
            public void onFailure(Call<ResultDO<IndexAd>> call, Throwable t) {
                ptrFrame.refreshComplete();
            }
        });

    }

    private void updateAds(IndexAd indexAd) {
        if (indexAd == null) {
            return;
        }
        List<Ad> bannerImgs = new ArrayList<>();
        if (indexAd.getImgList() != null && indexAd.getImgList().size() > 0) {
            bannerImgs.clear();
            bannerImgs.addAll(indexAd.getImgList());
        }
        banner.startTurning(3000);
        final IndexAdHolder indexAdHolder = new IndexAdHolder();
        banner.setPages(new ViewHolderCreator<IndexAdHolder>() {
            @Override
            public IndexAdHolder createHolder() {
                return indexAdHolder;
            }
        }, bannerImgs)
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused});
        banner.notifyDataSetChanged();
        indexAdHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ad item = (Ad) v.getTag();
                if (item.getDetailType() == 1) {
                    if (item.getDetailId() > 0) {
                        Intent intent = new Intent(mContext, ProductDetailsActivity.class);
                        intent.putExtra("type", 1);
                        intent.putExtra("productId", item.getDetailId());
                        mContext.startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(mContext, BrandProductListActivity.class);
                    intent.putExtra("brandName", String.format("%s", item.getName()));
                    intent.putExtra("brandId", item.getDetailId());
                    intent.putExtra("brandImg", item.getImage());
                    intent.putExtra("type", item.getBrandType()==null?0:item.getBrandType());
                    mContext.startActivity(intent);
                }
            }
        });

    }



    long timeLimitId;

    private void getLimitTimeList() {
        Call<ResultDO<List<LimitTime>>> call = ApiClient.getApiAdapter().getLimitTimeList2();
        call.enqueue(new Callback<ResultDO<List<LimitTime>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<LimitTime>>> call, Response<ResultDO<List<LimitTime>>> response) {
                if (isFinish) {
                    return;
                }
                ResultDO<List<LimitTime>> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    timeList.clear();
                    timeList.addAll(resultDO.getResult());
                    pageAdapter.notifyDataSetChanged();
                    if (timeList != null && timeList.size() > 0) {
                        for (int i = 0; i < timeList.size(); i++) {
                            if (timeList.get(i).getStatus() == 1) {
                                timeLimitId = timeList.get(i).getId();
                                viewPager.setCurrentItem(i, false);
                                break;
                            }
                        }
                    }
                    getProducts();
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<LimitTime>>> call, Throwable t) {

            }
        });
    }


    private void getProducts() {
        Call<ResultDO<LimitProduct>> call = ApiClient.getApiAdapter().getLimitTimeProList2(timeLimitId);
        call.enqueue(new Callback<ResultDO<LimitProduct>>() {
            @Override
            public void onResponse(Call<ResultDO<LimitProduct>> call, Response<ResultDO<LimitProduct>> response) {
                if (isFinish) {
                    return;
                }
                ResultDO<LimitProduct> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    if (resultDO.getResult().getBrandList() != null) {
                        brandList.clear();
                        brandList.addAll(resultDO.getResult().getBrandList());
                    }
                    if (resultDO.getResult().getProductList() != null) {
                        productList.clear();
                        productList.addAll(resultDO.getResult().getProductList());
                    }
                    adapter.setTimeLimitId(timeLimitId);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResultDO<LimitProduct>> call, Throwable t) {

            }
        });
    }
    private void todayGoldPrice() {
        Call<ResultDO> call = ApiClient.getApiAdapter().todayGoldPrice();
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                if (isFinish) {
                    return;
                }
                ResultDO resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    todayGoldPrice.setText(resultDO.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {

            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        //banner.stopTurning();
        super.onDestroy();

    }
}
