package com.bby.yishijie.member.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.entity.OrderNums;
import com.bby.yishijie.member.event.UpdateBaseInfo;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.login.LoginActivity;
import com.bby.yishijie.member.ui.mine.AddressListActivity;
import com.bby.yishijie.member.ui.mine.UserInfoActivity;
import com.bby.yishijie.shop.ui.OrderListActivity;
import com.bby.yishijie.shop.entity.Member;
import com.bby.yishijie.shop.entity.Statistic;
import com.bby.yishijie.shop.ui.CustomerManageActivity;
import com.bby.yishijie.shop.ui.PerformanceActivity;
import com.bby.yishijie.shop.ui.SalesActivity;
import com.bby.yishijie.shop.ui.ShopManageActivity;
import com.bumptech.glide.Glide;
import com.sunday.common.base.BaseFragment;
import com.sunday.common.event.EventBus;
import com.sunday.common.model.ResultDO;
import com.sunday.common.widgets.BoundScrollView;
import com.sunday.common.widgets.CircleImageView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.math.RoundingMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 刘涛 on 2017/4/11.
 */

public class ShopMngFragment extends BaseFragment {


    @Bind(R.id.user_logo)
    CircleImageView userLogo;
    @Bind(R.id.user_nickname)
    TextView userNickname;
    @Bind(R.id.user_mobile)
    TextView userMobile;
    @Bind(R.id.menu_order_1)
    TextView menuOrder1;
    @Bind(R.id.menu_order_2)
    TextView menuOrder2;
    @Bind(R.id.menu_order_3)
    TextView menuOrder3;
    @Bind(R.id.menu_order_4)
    TextView menuOrder4;
    @Bind(R.id.scrollView)
    BoundScrollView scrollView;
    @Bind(R.id.login_not_img)
    ImageView loginNotImg;
    @Bind(R.id.login_not_dec)
    TextView loginNotDec;
    @Bind(R.id.text_login)
    TextView textLogin;
    @Bind(R.id.login_not_layout)
    RelativeLayout loginNotLayout;
    @Bind(R.id.order_num)
    TextView orderNum;
    @Bind(R.id.sale_num)
    TextView saleNum;
    @Bind(R.id.profit_num)
    TextView profitNum;
    @Bind(R.id.fans_num)
    TextView fansNum;
    private boolean isLogin;
    private long memberId;

    public static ShopMngFragment newInstance(boolean isLogin) {
        ShopMngFragment fragment = new ShopMngFragment();
        Bundle args = new Bundle();
        args.putBoolean("isLogin", isLogin);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopmng, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            isLogin = getArguments().getBoolean("isLogin");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        EventBus.getDefault().register(this);


    }

    private void initView() {
        if (!isLogin) {
            loginNotLayout.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        } else {
            loginNotLayout.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
            memberId = BaseApp.getInstance().getShopMember().getId();
//            getOrderNums();
            updateView();
            getStatistics();
        }
    }


    public void getOrderNums() {
        memberId = BaseApp.getInstance().getShopMember().getId();
        Call<ResultDO<OrderNums>> call = ApiClient.getApiAdapter().getOrderCount(memberId, 1);
        call.enqueue(new Callback<ResultDO<OrderNums>>() {
            @Override
            public void onResponse(Call<ResultDO<OrderNums>> call, Response<ResultDO<OrderNums>> response) {
                if (isFinish || response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    updateOrderNums(response.body().getResult());
                }

            }

            @Override
            public void onFailure(Call<ResultDO<OrderNums>> call, Throwable t) {

            }
        });
    }

    Badge badge1, badge2;

    private void updateOrderNums(OrderNums orderNums) {
        if (orderNums.getWaitPay() >= 0) {
            if (badge1 == null) {
                badge1 = new QBadgeView(mContext).bindTarget(menuOrder1).setBadgeNumber(orderNums.getWaitPay());
                badge1.setShowShadow(false);
                badge1.setBadgeBackground(ContextCompat.getDrawable(mContext, R.color.colorPrimary), true);
                badge1.setGravityOffset(16, true);
                badge1.setBadgeTextSize(10, true);
            } else {
                badge1.setBadgeNumber(orderNums.getWaitPay());
            }

        }
        if (orderNums.getWaitSend() >= 0) {
            if (badge2 == null) {
                badge2 = new QBadgeView(mContext).bindTarget(menuOrder2).setBadgeNumber(orderNums.getWaitReceive());
                badge2.setShowShadow(false);
                badge2.setGravityOffset(16, true);
                badge2.setBadgeBackground(ContextCompat.getDrawable(mContext, R.color.colorPrimary), true);
                badge2.setBadgeTextSize(10, true);
            } else {
                badge2.setBadgeNumber(orderNums.getWaitReceive());
            }

        }
        /*if (orderNums.getWaitReceive() > 0) {
            Badge badge3 = new QBadgeView(mContext).bindTarget(menuOrder3).setBadgeNumber(orderNums.getWaitReceive());
            badge3.setShowShadow(false);
            badge3.setGravityOffset(16, true);
            badge3.setBadgeBackground(ContextCompat.getDrawable(mContext, R.color.colorPrimary), true);
            badge3.setBadgeTextSize(10, true);
        }*/
    }

    private void updateView() {
        Member member = BaseApp.getInstance().getShopMember();
        if (!TextUtils.isEmpty(member.getLogo())) {
            Glide.with(mContext)
                    .load(member.getLogo())
                    .error(R.mipmap.ic_logo_default)
                    .into(userLogo);
        }
        userMobile.setText("店铺邀请码: "+member.getInitCode());
        userNickname.setText(member.getShopName());


    }


    private void getMemberInfo() {

    }


    @OnClick({R.id.menu_order_4, R.id.shop_mng, R.id.address,R.id.share_shop,
            R.id.text_login, R.id.fans_manage, R.id.yeji_manage, R.id.sales_manage,
            R.id.menu_order_1, R.id.menu_order_2, R.id.menu_order_3})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_shop:
                UMImage umImage = new UMImage(mContext, R.mipmap.logo);
                String shareUrl = ApiClient.APK_URL;
//                String shareUrl = String.format("http://weixin.haowukongtou.com/authorizationPage.html?param=5-%d-0-0", BaseApp.getInstance().getShopMember().getId());
                UMWeb web = new UMWeb(shareUrl);
                web.setTitle(getString(R.string.invite_title));
                web.setThumb(umImage);
                web.setDescription(getString(R.string.invite_content));
                new ShareAction((Activity) mContext).setPlatform(SHARE_MEDIA.WEIXIN)
                        .withMedia(web)
                        .share();
                break;
            case R.id.fans_manage:
                intent = new Intent(mContext, CustomerManageActivity.class);
                startActivity(intent);
                break;
            case R.id.yeji_manage:
                intent = new Intent(mContext, PerformanceActivity.class);
                startActivity(intent);
                break;
            case R.id.sales_manage:
                intent = new Intent(mContext, SalesActivity.class);
                startActivity(intent);
                break;
            case R.id.shop_mng:
                intent = new Intent(mContext, ShopManageActivity.class);
                startActivity(intent);
                break;
            case R.id.address:
                intent = new Intent(mContext, AddressListActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_order_4:
                intent = new Intent(mContext, OrderListActivity.class);
                intent.putExtra("page", 0);
                startActivity(intent);
                break;
            case R.id.menu_order_1:
                intent = new Intent(mContext, OrderListActivity.class);
                intent.putExtra("page", 1);
                startActivity(intent);
                break;
            case R.id.menu_order_2:
                intent = new Intent(mContext, OrderListActivity.class);
                intent.putExtra("page", 3);
                startActivity(intent);
                break;
            case R.id.menu_order_3:
                intent = new Intent(mContext, OrderListActivity.class);
                intent.putExtra("page", 4);
                startActivity(intent);
                break;
            case R.id.text_login:
                //未登录
                intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.user_logo:
            case R.id.user_nickname:
            case R.id.user_mobile:
                intent = new Intent(mContext, UserInfoActivity.class);
                startActivity(intent);
                break;
//            case R.id.img_msg:
//                intent = new Intent(mContext, MessageListActivity.class);
//                startActivity(intent);
//                break;
        }
    }

    public void onEvent(UpdateBaseInfo updateBaseInfo) {
        updateView();
    }

    public void getStatistics() {
        Call<ResultDO<Statistic>> call = ApiClient.getApiAdapter().getStatistic(BaseApp.getInstance().getMember().getId());
        call.enqueue(new Callback<ResultDO<Statistic>>() {
            @Override
            public void onResponse(Call<ResultDO<Statistic>> call, Response<ResultDO<Statistic>> response) {
                if (isFinish) {
                    return;
                }
                if (response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    if (response.body().getResult() == null) {
                        return;
                    }
                    Statistic statistic = response.body().getResult();
                    orderNum.setText(String.valueOf(statistic.getTodayOrder()));
                    saleNum.setText(String.format("%s", statistic.getMonthSale().setScale(2, RoundingMode.HALF_UP)));
                    profitNum.setText(String.format("%s", statistic.getMonthTotalScale().setScale(2, RoundingMode.HALF_UP)));
                    fansNum.setText(String.valueOf(statistic.getMemberSize()));
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Statistic>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
