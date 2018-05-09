package com.bby.yishijie.shop.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.shop.entity.Member;
import com.bby.yishijie.shop.widgets.ShareWindow;
import com.bumptech.glide.Glide;
import com.flyco.tablayout.SlidingTabLayout;
import com.sunday.common.adapter.MainFragmentPagerAdapter;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.widgets.CircleImageView;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by 刘涛 on 2017/5/9.
 */

public class ShopManageActivity extends BaseActivity {


    @Bind(R.id.shop_img)
    CircleImageView shopImg;
    @Bind(R.id.shop_title)
    TextView shopTitle;
    @Bind(R.id.shop_description)
    TextView shopDescription;
    @Bind(R.id.share_shop)
    TextView shareShop;
    @Bind(R.id.header_layout)
    RelativeLayout headerLayout;
    //    @Bind(R.id.toolbar)
//    Toolbar toolbar;
    @Bind(R.id.collapse_toolbar)
    CollapsingToolbarLayout collapseToolbar;
    @Bind(R.id.appbar_layout)
    AppBarLayout appbarLayout;
    //    @Bind(R.id.tabs)
//    TabLayout tabs;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.slide_tab)
    SlidingTabLayout slideTab;
    //    @Bind(R.id.title)
//    TextView title;

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.right_btn)
    ImageView rightBtn;

    Member member;

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private final String[] mTitles = {
            "单品", "品牌"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_manage);
        ButterKnife.bind(this);
        member = BaseApp.getInstance().getShopMember();
        initView();
        setUpToolBar();
        //setUpCollapsingToolBar();
        initFragments();
    }

    private void initView() {
        titleView.setText("店铺管理");
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setImageResource(R.mipmap.store_manage_sort);
//        if (!TextUtils.isEmpty(member.getShopLogo())) {
            Glide.with(mContext)
                    .load(member.getShopLogo())
                    .error(R.mipmap.ic_default)
                    .into(shopImg);
//        }
        shopTitle.setText(member.getShopName());
        shopDescription.setText(member.getShopInfo());
        shopDescription.setVisibility(View.GONE);


    }

    private void setUpToolBar() {
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //toolbar.setOnMenuItemClickListener();

    }

   /* private void setUpCollapsingToolBar() {
//        collapseToolbar.setTitleEnabled(true);
//        collapseToolbar.setTitle("");
//        collapseToolbar.setCollapsedTitleGravity(Gravity.CENTER);
//        collapseToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        appbarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    //collapseToolbar.setTitle("");
                    title.setText("");
                } else if (state == State.COLLAPSED) {
                    //collapseToolbar.setTitle("我的店铺");
                    title.setText("我的店铺");
                } else {
                    //collapseToolbar.setTitle("");
                    title.setText("");
                }
            }
        });
    }*/

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        //getMenuInflater().inflate(R.menu.menu_shop, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                break;
////            case R.id.action_preview:
////                break;
////            case R.id.action_sort:
////                break;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//        return true;
//    }

    private void initFragments() {
        fragments.add(new ShopProductFragment());
        fragments.add(new ShopBrandFragment());
        viewPager.setAdapter(new MainFragmentPagerAdapter(getSupportFragmentManager(), mContext, fragments, R.array.shop_manage_title));
        slideTab.setViewPager(viewPager, mTitles, this, fragments);

    }

    @OnClick(R.id.share_shop)
    void shareShop() {
        /*1我要开店2分享店铺3普通商品4限时购商品)-memberId(会员Id)-productId(商品Id)-limitId(限时购时间段Id)
                    这四个参数没有值的传0
                    例如普通商品分享：http://weixin.zj-yunti.com/authorizationPage.html?param=3-1-14-0*/
        String shareUrl = String.format("%1$s%2$d-%3$d-%4$d-%5$d", ApiClient.SHARE_URL, 2, member.getId(), 0, 0);
        ShareWindow shareWindow = new ShareWindow(mContext, shareUrl, member.getShopName(), member.getShopLogo(),
                null, mContext.getResources().getString(R.string.share_shop_desc), null,null);
        shareWindow.showPopupWindow(shareShop);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    @OnClick(R.id.right_btn)
    void onManage(){
        intent=new Intent(mContext,ShopSortAcitivity.class);
        startActivity(intent);
    }


}
