package com.bby.yishijie.member.common;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bby.yishijie.member.entity.GroupBuyOrder;
import com.bby.yishijie.member.entity.Member;
import com.bby.yishijie.member.entity.Order;
import com.bumptech.glide.Glide;
import com.lzy.ninegrid.NineGridView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.bby.yishijie.R;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;


/**
 * Created by 刘涛 on 2017/4/21.
 */

public class BaseApp extends Application implements Thread.UncaughtExceptionHandler {
    private static BaseApp instance;

    public synchronized static BaseApp getInstance() {
        return instance;
    }


    private Member member;
    private com.bby.yishijie.shop.entity.Member shopMember;

    //public LocationService locationService;

    private Boolean isCanUpload = true;
    private boolean isLogin;
    private int payType;//微信支付
    private int wxPayFlag;//微信支付应用场景1.orderConfirmActivity页面2。OrderPayActivity页面
    private Order order;
    private com.bby.yishijie.shop.entity.Order shopOrder;
    private GroupBuyOrder groupBuyOrder;
    public static int screenWidth;//屏幕宽度
    public static int screenHeight;//屏幕高度

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        WindowManager wm = (WindowManager) getBaseContext().getSystemService(
                Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        screenHeight = wm.getDefaultDisplay().getHeight();
        if (getCurProcessName(this) != null && getCurProcessName(this).equals("com.bby.yishijie")) {
            /***
             * 初始化定位sdk，建议在Application中创建
             */
            UMShareAPI.get(getApplicationContext());
            Thread.setDefaultUncaughtExceptionHandler(this);
            PlatformConfig.setWeixin(Constant.APP_ID, Constant.APPSecret);
            Config.DEBUG = true;
            Config.isJumptoAppStore = true;
        }

        NineGridView.setImageLoader(new GlideImageLoader());
    }

    /**
     * Glide 加载
     */
    private class GlideImageLoader implements NineGridView.ImageLoader {

        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            Glide.with(context).load(url)//
                    .placeholder(R.mipmap.ic_default)
                    .error(R.mipmap.ic_default)//
                    .into(imageView);
        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }
    }

    private String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    public com.bby.yishijie.shop.entity.Member getShopMember() {
        return shopMember;
    }

    public void setShopMember(com.bby.yishijie.shop.entity.Member shopMember) {
        this.shopMember = shopMember;
    }

    public Member getMember() {
        if (member!=null){
            return member;
        }
        else {
            return com.bby.yishijie.shop.entity.Member.transferMember(shopMember);
        }
    }

    public void setMember(Member member) {
        this.member = member;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
        System.exit(0);
    }


    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getWxPayFlag() {
        return wxPayFlag;
    }

    public void setWxPayFlag(int wxPayFlag) {
        this.wxPayFlag = wxPayFlag;
    }

    public GroupBuyOrder getGroupBuyOrder() {
        return groupBuyOrder;
    }

    public void setGroupBuyOrder(GroupBuyOrder groupBuyOrder) {
        this.groupBuyOrder = groupBuyOrder;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public com.bby.yishijie.shop.entity.Order getShopOrder() {
        return shopOrder;
    }

    public void setShopOrder(com.bby.yishijie.shop.entity.Order shopOrder) {
        this.shopOrder = shopOrder;
    }

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.main_color, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}