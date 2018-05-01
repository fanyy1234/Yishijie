package com.bby.yishijie.member.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bby.yishijie.member.ui.index.IntegralMallFragment;
import com.bby.yishijie.member.ui.main.ShopMngFragment;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.event.EventBus;
import com.sunday.common.event.ExitApp;
import com.sunday.common.model.ResultDO;
import com.sunday.common.model.Version;
import com.sunday.common.update.UpdateDialogActivity;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.utils.SharePerferenceUtils;
import com.sunday.common.utils.StatusBarUtil;
import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.entity.Member;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.main.CartFragment1;
import com.bby.yishijie.member.ui.main.FindFragment;
import com.bby.yishijie.member.ui.main.IndexFragment;
import com.bby.yishijie.member.ui.main.MineFragment;
import com.bby.yishijie.member.ui.main.ShopFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;


public class MainActivity extends BaseActivity {

    @Bind({R.id.tab1, R.id.tab2, R.id.tab3, R.id.tab4, R.id.tab5})
    List<TextView> tabViews;
    @Bind(R.id.tab4)
    TextView textView4;

    private Member member;
    private int pageNo;
    private Fragment[] fragments;
    private IndexFragment indexFragment;
    private int index;
    private int currentTabIndex;
    boolean login;
    public static boolean isShop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        checkLogin();
        checkAppPermission();
        checkUpdate();
        initFragments();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else {
            getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void checkLogin() {
        login = SharePerferenceUtils.getIns(mContext).getBoolean(Constants.IS_LOGIN, false);
        isShop = SharePerferenceUtils.getIns(mContext).getBoolean(Constants.IS_SHOP, false);
        if (!login) {
            /*intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
            finish();*/
        } else {
            if (isShop){
                com.bby.yishijie.shop.entity.Member member1 = (com.bby.yishijie.shop.entity.Member) SharePerferenceUtils.getIns(mContext).getOAuth();
                if (member1 != null) {
                    BaseApp.getInstance().setShopMember(member1);
                }
            }
            else {
                member = (Member) SharePerferenceUtils.getIns(mContext).getOAuth();
                if (member != null) {
                    BaseApp.getInstance().setMember(member);
                }
            }

        }

    }

    private void initFragments() {
        indexFragment = IndexFragment.newInstance();
        FindFragment mClassifyFragment = FindFragment.newInstance();
        ShopFragment mLiveCircleFragment = ShopFragment.newInstance(login);
        CartFragment1 mCartFragment = CartFragment1.newInstance(login, false);
        IntegralMallFragment integralMallFragment = IntegralMallFragment.newInstance();
        ShopMngFragment shopMngFragment = ShopMngFragment.newInstance(login);
        MineFragment mineFragment = MineFragment.newInstance(login);
        if (isShop){
            textView4.setText("店铺");
            fragments = new Fragment[]{
                    indexFragment,
                    mClassifyFragment,
                    mLiveCircleFragment,
                    shopMngFragment,
                    mineFragment
            };
        }
        else {
            textView4.setText("积分商城");
            fragments = new Fragment[]{
                    indexFragment,
                    mClassifyFragment,
                    mLiveCircleFragment,
                    integralMallFragment,
                    mineFragment
            };
        }

        // 添加显示第一个fragment
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_container, indexFragment)
                .show(indexFragment).commit();
        tabViews.get(0).setSelected(true);

    }


    /**
     * 切换Fragment的下标
     */
    private void changeFragmentIndex(int currentIndex) {
        index = currentIndex;
        switchFragment();
        for (int i = 0; i < tabViews.size(); i++) {
            tabViews.get(i).setSelected(false);
        }
        tabViews.get(currentIndex).setSelected(true);
    }

    /**
     * Fragment切换
     */
    private void switchFragment() {
        FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
        trx.hide(fragments[currentTabIndex]);
        if (!fragments[index].isAdded()) {
            trx.add(R.id.main_container, fragments[index]);
        }
        trx.show(fragments[index]).commit();
        currentTabIndex = index;
        switch (index) {
            case 0:
                break;
            case 2:
                if (isShop){
//                    ((ShopFragment)fragments[2]).getStatistics();
                    if (member != null && member.getRecId() != 0) {
                        ((ShopFragment) fragments[2]).getRecShopInfo(member.getRecId());
                    }
                }
                else {
                    if (member != null && member.getRecId() != 0) {
                        ((ShopFragment) fragments[2]).getRecShopInfo(member.getRecId());
                    }
                }
                break;
            case 3:
//                if (member != null) {
//                    ((CartFragment1) fragments[3]).getData();
//                }

                break;
            case 4:
                if (member != null) {
                    ((MineFragment) fragments[4]).getOrderNums();
                }
                break;

        }
    }


    @OnClick({R.id.tab1, R.id.tab2, R.id.tab3, R.id.tab4, R.id.tab5})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab1:
                changeFragmentIndex(0);
                break;
            case R.id.tab2:
                changeFragmentIndex(1);
                break;
            case R.id.tab3:
                changeFragmentIndex(2);
                break;
            case R.id.tab4:
                changeFragmentIndex(3);
                break;
            case R.id.tab5:
                changeFragmentIndex(4);
                break;
        }
    }


    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(mContext, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                EventBus.getDefault().post(new ExitApp());
                System.exit(1);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void checkAppPermission() {
        PackageManager pkgManager = getPackageManager();
        //读写 sd card 权限非常重要, android6.0默认禁止的, 建议初始化之前就弹窗让用户赋予该权限
        boolean sdCardWritePermission =
                pkgManager.checkPermission(WRITE_EXTERNAL_STORAGE, getPackageName()) == PERMISSION_GRANTED;

        //read phone state用于获取 imei 设备信息
        boolean phoneStatePermission =
                pkgManager.checkPermission(READ_PHONE_STATE, getPackageName()) == PERMISSION_GRANTED;

        //获取位置权限
        boolean locationWifiPermission = pkgManager.checkPermission(ACCESS_COARSE_LOCATION, getPackageName()) == PERMISSION_GRANTED;
        boolean locationGPSPermission = pkgManager.checkPermission(ACCESS_FINE_LOCATION, getPackageName()) == PERMISSION_GRANTED;

        if (Build.VERSION.SDK_INT >= 23 && !sdCardWritePermission || !phoneStatePermission || !locationWifiPermission || !locationGPSPermission) {
            ActivityCompat.requestPermissions(this, new String[]{
                    WRITE_EXTERNAL_STORAGE, READ_PHONE_STATE, ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION
            }, 0);
        } else {
            // SDK初始化，第三方程序启动时，都要进行SDK初始化工作
            // SDK初始化，第三方程序启动时，都要进行SDK初始化工作
//            PushManager.getInstance().initialize(this.getApplicationContext(), PushService.class);
//            // com.getui.demo.DemoIntentService 为第三方自定义的推送服务事件接收类
//            PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), PushIntentService.class);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0: {
                //If request is cancelled, the request arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                    // SDK初始化，第三方程序启动时，都要进行SDK初始化工作
//                    PushManager.getInstance().initialize(this.getApplicationContext(), PushService.class);
//                    // com.getui.demo.DemoIntentService 为第三方自定义的推送服务事件接收类
//                    PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), PushIntentService.class);
                } else {

                }
            }
        }
    }

    private void checkUpdate() {
        Call<ResultDO<Version>> call = ApiClient.getApiAdapter().checkVersion(1, DeviceUtils.getVersionCode(getApplicationContext()));
        call.enqueue(new Callback<ResultDO<Version>>() {
            @Override
            public void onResponse(Call<ResultDO<Version>> call, Response<ResultDO<Version>> response) {
                ResultDO<Version> resultDO = response.body();
                if (isFinish) {
                    return;
                }
                if (resultDO != null && resultDO.getCode() == 0) {
                    final Version version = resultDO.getResult();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(mContext, UpdateDialogActivity.class);
                            intent.putExtra("version", version);
                            startActivity(intent);
                        }
                    }, 2000);
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Version>> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isShop){
            com.bby.yishijie.shop.entity.Member member1 = (com.bby.yishijie.shop.entity.Member) SharePerferenceUtils.getIns(mContext).getOAuth();
            if (member1 != null) {
                BaseApp.getInstance().setShopMember(member1);
            }
        }
        else {
            member = (Member) SharePerferenceUtils.getIns(mContext).getOAuth();
            if (member != null) {
                BaseApp.getInstance().setMember(member);
            }
        }
    }
}
