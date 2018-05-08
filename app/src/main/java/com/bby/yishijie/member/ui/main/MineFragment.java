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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.entity.Member;
import com.bby.yishijie.member.entity.OrderNums;
import com.bby.yishijie.member.event.UpdateBaseInfo;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.MainActivity;
import com.bby.yishijie.member.ui.WebViewActivity;
import com.bby.yishijie.member.ui.login.LoginActivity;
import com.bby.yishijie.member.ui.mine.AddressListActivity;
import com.bby.yishijie.member.ui.mine.OpenShopActivity;
import com.bby.yishijie.member.ui.mine.SetRecCodeActivity;
import com.bby.yishijie.member.ui.mine.SettingActivity;
import com.bby.yishijie.member.ui.mine.UserInfoActivity;
import com.bby.yishijie.member.ui.mine.WithdrawActivity;
import com.bby.yishijie.member.ui.mine.voucher.VoucherListActivity;
import com.bby.yishijie.member.ui.order.OrderListActivity;
import com.bby.yishijie.shop.entity.ProfitAll;
import com.bby.yishijie.shop.ui.AvailableProfitActivity;
import com.bby.yishijie.shop.ui.IntegralMallActivity;
import com.bby.yishijie.shop.ui.MyIntegralActivity;
import com.bby.yishijie.shop.ui.TotalProfitActivity;
import com.bumptech.glide.Glide;
import com.sunday.common.base.BaseFragment;
import com.sunday.common.event.EventBus;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.SharePerferenceUtils;
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

public class MineFragment extends BaseFragment {


    @Bind(R.id.user_logo1)
    CircleImageView userLogo1;
    @Bind(R.id.user_nickname1)
    TextView userNickname1;
    @Bind(R.id.user_mobile1)
    TextView userMobile1;
    @Bind(R.id.user_logo2)
    CircleImageView userLogo2;
    @Bind(R.id.user_nickname2)
    TextView userNickname2;
    @Bind(R.id.user_mobile2)
    TextView userMobile2;
    @Bind(R.id.menu_order_1)
    TextView menuOrder1;
    @Bind(R.id.menu_order_2)
    TextView menuOrder2;
    @Bind(R.id.menu_order_3)
    TextView menuOrder3;
    @Bind(R.id.menu_order_4)
    TextView menuOrder4;
    @Bind(R.id.menu_counpon)
    LinearLayout menuCounpon;
    @Bind(R.id.menu_addr)
    LinearLayout menuAddr;
    @Bind(R.id.shop_name)
    TextView shopName;
    @Bind(R.id.menu_rec_shop)
    LinearLayout menuRecShop;
    @Bind(R.id.menu_open_shop)
    LinearLayout menuOpenShop;
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
    @Bind(R.id.shop_view)
    View shopView;
    @Bind(R.id.shop_header)
    View shopHeader;
    @Bind(R.id.member_header)
    View memberHeader;
    @Bind(R.id.available_profit)
    LinearLayout availableProfit;
    @Bind(R.id.total_profit)
    LinearLayout totalProfit;
    @Bind(R.id.my_integral)
    LinearLayout myIntegral;
    @Bind(R.id.my_coupon)
    LinearLayout myCoupon;
    @Bind(R.id.my_score)
    TextView myScore;
    @Bind(R.id.withdraw_num)
    TextView withdrawNum;
    @Bind(R.id.wait_profit_num)
    TextView waitProfitNum;
    @Bind(R.id.total_profit_num)
    TextView totalProfitNum;
    @Bind(R.id.counpon_num)
    TextView counponNum;
    @Bind(R.id.shop_counpon_num)
    TextView shopCounponNum;
    @Bind(R.id.menu_share_shop)
    LinearLayout menuShareShop;
    @Bind(R.id.menu_jifen)
    LinearLayout menuJifen;
    @Bind(R.id.menu_integral_mall)
    LinearLayout menuIntegralMall;
    private boolean isLogin;
    private boolean isShop;
    private long memberId;

    public static MineFragment newInstance(boolean isLogin) {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        args.putBoolean("isLogin", isLogin);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
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
        isShop = SharePerferenceUtils.getIns(mContext).getBoolean(Constants.IS_SHOP, false);

        if (!isLogin) {
            loginNotLayout.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        } else {
            loginNotLayout.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
            if (MainActivity.isShop) {
                memberId = BaseApp.getInstance().getShopMember().getId();
                getProfits();
            } else {
                memberId = BaseApp.getInstance().getMember().getId();
            }
//            getOrderNums();
            updateView();
        }
        if (!isShop) {
            shopView.setVisibility(View.GONE);
            shopHeader.setVisibility(View.GONE);
            memberHeader.setVisibility(View.VISIBLE);
            menuShareShop.setVisibility(View.GONE);
            menuJifen.setVisibility(View.VISIBLE);
            menuCounpon.setVisibility(View.VISIBLE);
            menuIntegralMall.setVisibility(View.GONE);
        } else {
            shopView.setVisibility(View.VISIBLE);
            shopHeader.setVisibility(View.VISIBLE);
            memberHeader.setVisibility(View.GONE);
            menuShareShop.setVisibility(View.VISIBLE);
            menuJifen.setVisibility(View.GONE);
            menuCounpon.setVisibility(View.GONE);
            menuIntegralMall.setVisibility(View.VISIBLE);
        }
    }


    public void getOrderNums() {
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

    Badge badge1, badge2, badge3;

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
                badge2 = new QBadgeView(mContext).bindTarget(menuOrder2).setBadgeNumber(orderNums.getWaitSend());
                badge2.setShowShadow(false);
                badge2.setGravityOffset(16, true);
                badge2.setBadgeBackground(ContextCompat.getDrawable(mContext, R.color.colorPrimary), true);
                badge2.setBadgeTextSize(10, true);
            } else {
                badge2.setBadgeNumber(orderNums.getWaitSend());
            }

        }
        if (orderNums.getWaitReceive() > 0) {
            if (badge3 == null) {
                badge3 = new QBadgeView(mContext).bindTarget(menuOrder3).setBadgeNumber(orderNums.getWaitReceive());
                badge3.setShowShadow(false);
                badge3.setGravityOffset(16, true);
                badge3.setBadgeBackground(ContextCompat.getDrawable(mContext, R.color.colorPrimary), true);
                badge3.setBadgeTextSize(10, true);
            } else {
                badge3.setBadgeNumber(orderNums.getWaitReceive());
            }
        }
    }

    private void updateView() {
        if (!isShop) {
            Member member = BaseApp.getInstance().getMember();
            if (!TextUtils.isEmpty(member.getLogo())) {
                Glide.with(mContext)
                        .load(member.getLogo())
                        .error(R.mipmap.ic_logo_default)
                        .into(userLogo1);
            }
            userMobile1.setText(member.getMobile());
            userNickname1.setText(member.getNickName());
            shopName.setText(member.getRecId() != 0 ? member.getRecName() : "");
            if (member.getShopStatus() == 1) {
                menuOpenShop.setVisibility(View.GONE);
            }
        } else {
            com.bby.yishijie.shop.entity.Member member = BaseApp.getInstance().getShopMember();
            if (!TextUtils.isEmpty(member.getLogo())) {
                Glide.with(mContext)
                        .load(member.getLogo())
                        .error(R.mipmap.ic_logo_default)
                        .into(userLogo2);
            }
            userMobile2.setText(member.getMobile());
            userNickname2.setText(member.getNickName());
            shopName.setText(member.getRecId() != 0 ? member.getRecName() : "");
            if (member.getShopStatus() == 1) {
                menuOpenShop.setVisibility(View.GONE);
            }
        }
    }


    private void getMemberInfo() {

    }


    @OnClick({R.id.menu_order_4, R.id.menu_counpon, R.id.menu_addr, R.id.order_all, R.id.set_rec_code,
            R.id.menu_open_shop, R.id.menu_customer_service, R.id.text_login, R.id.menu_setting, R.id.menu_share_shop,
            R.id.menu_order_1, R.id.menu_order_2, R.id.menu_order_3, R.id.withdraw_btn, R.id.menu_jifen,R.id.menu_integral_mall,
            R.id.my_integral, R.id.available_profit, R.id.total_profit, R.id.my_coupon})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_share_shop:
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
            case R.id.menu_integral_mall:
                intent = new Intent(mContext, IntegralMallActivity.class);
                startActivity(intent);
                break;
            case R.id.my_integral:
                intent = new Intent(mContext, MyIntegralActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_jifen:
                intent = new Intent(mContext, MyIntegralActivity.class);
                startActivity(intent);
                break;
            case R.id.available_profit:
                intent = new Intent(mContext, AvailableProfitActivity.class);
                startActivity(intent);
                break;
            case R.id.total_profit:
                intent = new Intent(mContext, TotalProfitActivity.class);
                startActivity(intent);
                break;
            case R.id.my_coupon:
                intent = new Intent(mContext, VoucherListActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            case R.id.withdraw_btn:
                intent = new Intent(mContext, WithdrawActivity.class);
                startActivity(intent);
                break;
            case R.id.order_all:
                intent = new Intent(mContext, OrderListActivity.class);
                intent.putExtra("page", 0);
                startActivity(intent);
                break;
            case R.id.menu_order_4:
                intent = new Intent(mContext, OrderListActivity.class);
                intent.putExtra("page", 4);
                startActivity(intent);
                break;
            case R.id.menu_order_1:
                intent = new Intent(mContext, OrderListActivity.class);
                intent.putExtra("page", 1);
                startActivity(intent);
                break;
            case R.id.menu_order_2:
                intent = new Intent(mContext, OrderListActivity.class);
                intent.putExtra("page", 2);
                startActivity(intent);
                break;
            case R.id.menu_order_3:
                intent = new Intent(mContext, OrderListActivity.class);
                intent.putExtra("page", 3);
                startActivity(intent);
                break;
            case R.id.menu_counpon:
                intent = new Intent(mContext, VoucherListActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            case R.id.menu_addr:
                intent = new Intent(mContext, AddressListActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_open_shop:
                //开店
                intent = new Intent(mContext, OpenShopActivity.class);
                startActivity(intent);
                break;
            case R.id.text_login:
                //未登录
                intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.menu_setting:
                intent = new Intent(mContext, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.user_logo:
            case R.id.user_nickname:
            case R.id.user_mobile:
                intent = new Intent(mContext, UserInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.set_rec_code:
                intent = new Intent(mContext, SetRecCodeActivity.class);
                startActivity(intent);
                break;
//            case R.id.img_msg:
//                intent = new Intent(mContext, MessageListActivity.class);
//                startActivity(intent);
//                break;
            case R.id.menu_customer_service:
                //客服
                intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url", ApiClient.CUSTOMER_URL);
                intent.putExtra("title", "客服");
                startActivity(intent);
                break;
        }
    }

    public void onEvent(UpdateBaseInfo updateBaseInfo) {
        updateView();
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

    private void myScore() {
        Call<ResultDO> call = ApiClient.getApiAdapter().getScore(memberId);
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
                    myScore.setText(resultDO.getResult().toString());
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
            }
        });
    }

    public void getProfits() {
        Call<ResultDO<ProfitAll>> call = ApiClient.getApiAdapter().getMyProfit(BaseApp.getInstance().getMember().getId());
        call.enqueue(new Callback<ResultDO<ProfitAll>>() {
            @Override
            public void onResponse(Call<ResultDO<ProfitAll>> call, Response<ResultDO<ProfitAll>> response) {
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
                    ProfitAll profitAll = response.body().getResult();
                    withdrawNum.setText(String.format("%s", profitAll.getWithdrawAmount().setScale(2, RoundingMode.HALF_UP)));
                    waitProfitNum.setText(String.format("%s", profitAll.getWaitAmount().setScale(2, RoundingMode.HALF_UP)));
                    totalProfitNum.setText(String.format("%s", profitAll.getTotalProfit().setScale(2, RoundingMode.HALF_UP)));
                    shopCounponNum.setText("" + profitAll.getVoucherCount());
                    myScore.setText("" + profitAll.getScore());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<ProfitAll>> call, Throwable t) {

            }
        });
    }
}
