package com.bby.yishijie.member.ui.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bby.yishijie.member.ui.MainActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.flyco.tablayout.SlidingTabLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sunday.common.adapter.MainFragmentPagerAdapter;
import com.sunday.common.base.BaseFragment;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.SharePerferenceUtils;
import com.sunday.common.widgets.CircleImageView;
import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.entity.Member;
import com.bby.yishijie.member.entity.ProductBrand;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.login.LoginActivity;
import com.bby.yishijie.member.ui.login.MobileLoginActivity;

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

public class ShopFragment extends BaseFragment {


    @Bind(R.id.login_not_dec)
    TextView loginNotDec;
    @Bind(R.id.text_login)
    TextView textLogin;
    @Bind(R.id.login_not_layout)
    RelativeLayout loginNotLayout;
    @Bind(R.id.shop_img)
    CircleImageView shopImg;
    @Bind(R.id.shop_title)
    TextView shopTitle;
    @Bind(R.id.shop_description)
    TextView shopDescription;
    @Bind(R.id.header_layout)
    RelativeLayout headerLayout;
    @Bind(R.id.slide_tab)
    SlidingTabLayout slideTab;
    @Bind(R.id.appbar_layout)
    AppBarLayout appbarLayout;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @Bind(R.id.left_btn)
    ImageView leftBtn;
    @Bind(R.id.title_view)
    TextView titleView;


    private ArrayList<Fragment> fragments = new ArrayList<>();
    private List<String> mTitles=new ArrayList<>();

    private boolean isLogin;
//    Member member;
    long memberId = 0;
    long recId = 0;
    public static boolean refreshFlag=false;
    public static ShopFragment newInstance(boolean isLogin) {
        ShopFragment shopFragment = new ShopFragment();
        Bundle args = new Bundle();
        args.putBoolean("isLogin", isLogin);
        shopFragment.setArguments(args);
        return shopFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isLogin = getArguments().getBoolean("isLogin");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        titleView.setText("店主精选");
        leftBtn.setVisibility(View.GONE);
        mTitles.add("单品");
        mTitles.add("品牌");
        initParams();


    }

    private void initParams() {
        if (!isLogin) {
            loginNotLayout.setVisibility(View.VISIBLE);
            refreshLayout.setVisibility(View.GONE);
        } else {
            if (MainActivity.isShop){
                memberId = BaseApp.getInstance().getShopMember().getId();
                recId = BaseApp.getInstance().getShopMember().getRecId();
            }
            else {
                memberId = BaseApp.getInstance().getMember().getId();
                recId = BaseApp.getInstance().getMember().getRecId();
            }

            if (recId!=0) {
                loginNotLayout.setVisibility(View.GONE);
                refreshLayout.setVisibility(View.VISIBLE);
                getRecShopInfo(recId);
                initView();
                //getShopBrand();
            } else {
                loginNotLayout.setVisibility(View.VISIBLE);
                refreshLayout.setVisibility(View.GONE);
                textLogin.setText("邀请店铺");
                loginNotDec.setText("您还未邀请店铺");
            }
        }
    }

    private void initView() {
        fragments.clear();
        fragments.add(new ShopProductFragment());
        fragments.add(new ShopBrandFragment());
        viewPager.setAdapter(new MainFragmentPagerAdapter(getChildFragmentManager(),fragments,mTitles));
        slideTab.setViewPager(viewPager);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
               // ((ShopProductFragment)fragments.get(0)).refresh();
                //((ShopBrandFragment)fragments.get(1)).refresh();
                getRecShopInfo(recId);
            }
        });
        ((ClassicsHeader)refreshLayout.getRefreshHeader()).setPrimaryColor(Color.parseColor("#f5f4f9"));
        ((ClassicsHeader)refreshLayout.getRefreshHeader()).setAccentColor(Color.parseColor("#666666"));
    }

    @OnClick(R.id.text_login)
    void onClick() {
        if (!isLogin) {
            intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
        } else {
            intent = new Intent(mContext, MobileLoginActivity.class);
            intent.putExtra("memberId",memberId);
            intent.putExtra("isFromRecShop", true);
            startActivity(intent);
        }
    }

    public void getRecShopInfo(long recId) {
        Call<ResultDO<Member>> call = ApiClient.getApiAdapter().getMemberInfo(recId);
        call.enqueue(new Callback<ResultDO<Member>>() {
            @Override
            public void onResponse(Call<ResultDO<Member>> call, Response<ResultDO<Member>> response) {
                if (isFinish) {
                    return;
                }
                refreshLayout.finishRefresh();
                if (response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    if (response.body().getResult() != null) {
                        Member member = response.body().getResult();
                        shopTitle.setText(TextUtils.isEmpty(member.getShopName()) ? "" : member.getShopName());
                        shopDescription.setText(TextUtils.isEmpty(member.getShopInfo()) ? "" : member.getShopInfo());
                        if (!TextUtils.isEmpty(member.getShopLogo())) {
                            Glide.with(mContext)
                                    .load(member.getShopLogo()).error(R.mipmap.ic_default).into(shopImg);
                        }
                        if (!TextUtils.isEmpty(member.getShopImage()))
                            Glide.with(mContext)
                                    .load(member.getShopImage()).asBitmap().into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                    Drawable drawable = new BitmapDrawable(resource);
                                    headerLayout.setBackground(drawable);
                                }
                            });
                        ((ShopProductFragment)fragments.get(0)).refresh();
                        ((ShopBrandFragment)fragments.get(1)).refresh();

                    }
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Member>> call, Throwable t) {
                refreshLayout.finishRefresh();
            }
        });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        if (refreshFlag){
            if (MainActivity.isShop){
                getShopMemberInfo();
            }
            else {
                getMemberInfo();
            }
            refreshFlag = false;
        }
    }

    public void getMemberInfo() {
        Call<ResultDO<Member>> call = ApiClient.getApiAdapter().getMemberInfo(memberId);
        call.enqueue(new Callback<ResultDO<Member>>() {
            @Override
            public void onResponse(Call<ResultDO<Member>> call, Response<ResultDO<Member>> response) {
                if (isFinish) {
                    return;
                }
                refreshLayout.finishRefresh();
                if (response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    if (response.body().getResult() != null) {
                        Member member = response.body().getResult();
                        SharePerferenceUtils.getIns(mContext).saveOAuth(member);
                        BaseApp.getInstance().setMember(member);
                        initParams();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Member>> call, Throwable t) {
                refreshLayout.finishRefresh();
            }
        });
    }
    private void getShopMemberInfo() {
        Call<ResultDO<com.bby.yishijie.shop.entity.Member>> call = ApiClient.getApiAdapter().getMemberInfo2(memberId);
        call.enqueue(new Callback<ResultDO<com.bby.yishijie.shop.entity.Member>>() {
            @Override
            public void onResponse(Call<ResultDO<com.bby.yishijie.shop.entity.Member>> call, Response<ResultDO<com.bby.yishijie.shop.entity.Member>> response) {
                if (isFinish || response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    if (response.body().getResult() != null) {
                        com.bby.yishijie.shop.entity.Member member = response.body().getResult();
                        SharePerferenceUtils.getIns(mContext).saveOAuth(member);
                        BaseApp.getInstance().setShopMember(member);
                        initParams();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultDO<com.bby.yishijie.shop.entity.Member>> call, Throwable t) {

            }
        });
    }
}
