package com.bby.yishijie.wxapi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.bby.yishijie.member.ui.MainActivity;
import com.bby.yishijie.member.ui.order.OrderConfirmActivity;
import com.bby.yishijie.member.ui.order.OrderPayActivity;
import com.bby.yishijie.shop.ui.OrderConfirmShopActivity;
import com.sunday.common.utils.ToastUtils;
import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.common.Constant;
import com.bby.yishijie.member.entity.Order;

import com.bby.yishijie.member.ui.order.OrderListActivity;
import com.bby.yishijie.member.ui.order.PaySuccessActivity;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.math.RoundingMode;


/**
 * Created by 刘涛 on 2017/4/28.
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent);
        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID);
        api.handleIntent(getIntent(), this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onResp(final BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
        switch (resp.errCode) {
            case 0:
                Intent intent = null;
                int orderType = BaseApp.getInstance().getPayType();
                if (orderType == Constant.TYPE_NORMAL) {
                    intent = new Intent(WXPayEntryActivity.this, PaySuccessActivity.class);//支付成功界面
                    Order order = BaseApp.getInstance().getOrder();
                    intent.putExtra("totalMoney", String.format("%s", order.getTotalMoney().setScale(2, RoundingMode.HALF_UP)));
                    intent.putExtra("address", order.getAddress());
                    intent.putExtra("name", order.getName());
                    intent.putExtra("mobile", order.getMobile());
                    if (BaseApp.getInstance().getWxPayFlag()==1){
                        if (MainActivity.isShop){
                            OrderConfirmShopActivity.orderConfirmShopActivity.finish();
                        }
                        else {
                            OrderConfirmActivity.orderConfirmActivity.finish();
                        }
                    }
                    else if(BaseApp.getInstance().getWxPayFlag()==2){
                        OrderPayActivity.orderPayActivity.finish();
                    }
                    startActivity(intent);
                    finish();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(WXPayEntryActivity.this);
                    builder.setTitle("恭喜您");
                    builder.setMessage("开店成功，您的等级升级为店主，可使用该手机号登录店主版，享受店主权益！");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                    builder.show();
                }
                break;
            case -1:
                ToastUtils.showToast(WXPayEntryActivity.this, "交易失败,请重试");
                intent = new Intent(WXPayEntryActivity.this, OrderListActivity.class);
                intent.putExtra("page", 1);
                startActivity(intent);
                finish();
                break;
            case -2:
                ToastUtils.showToast(WXPayEntryActivity.this, "交易取消");
                intent = new Intent(WXPayEntryActivity.this, OrderListActivity.class);
                intent.putExtra("page", 1);
                startActivity(intent);
                finish();
                break;
        }

    }

}