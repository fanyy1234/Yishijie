package com.bby.yishijie.member.ui.order;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.sunday.common.alipay.OrderInfoUtil2_0;
import com.sunday.common.alipay.PayResult;
import com.sunday.common.alipay.SignUtils;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.UIAlertView;
import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.common.Constant;
import com.bby.yishijie.member.entity.GroupBuyOrder;
import com.bby.yishijie.member.entity.Order;
import com.bby.yishijie.member.entity.PayInfo;
import com.bby.yishijie.member.http.ApiClient;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bby.yishijie.member.common.Constant.RSA_PRIVATE;


/**
 * Created by 刘涛 on 2017/4/21.
 */

public class OrderPayActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.rightTxt)
    TextView rightTxt;
    @Bind(R.id.order_no)
    TextView orderNo;
    @Bind(R.id.pro_money)
    TextView proMoney;
    @Bind(R.id.coupon_money)
    TextView couponMoney;
    @Bind(R.id.post_money)
    TextView postMoney;
    @Bind(R.id.total_money)
    TextView totalMoney;
    @Bind(R.id.select_pay_way)
    TextView selectPayWay;
    @Bind(R.id.wechat_pay)
    CheckBox wechatPay;
    @Bind(R.id.alipay)
    CheckBox alipay;

    private Order order;
    private int payType = 1;
    private String money;



    private String orderNumber;
    private String payTitle;
    private String address;
    private String reciverName, recieverMobile;
    private long orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_order);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        rightTxt.setVisibility(View.GONE);
        titleView.setText("订单支付");
        order = (Order) getIntent().getSerializableExtra("order");
        if (order == null) {
            return;
        }
        orderNo.setText(order.getOrderNo());
        proMoney.setText(String.format("¥%s", order.getTotalFee().setScale(2, RoundingMode.HALF_UP)));
        couponMoney.setText(String.format("¥%s", order.getVoucherFee().setScale(2, RoundingMode.HALF_UP)));
        postMoney.setText(String.format("¥%s", order.getExpressFee().setScale(2, RoundingMode.HALF_UP)));
        totalMoney.setText(String.format("¥%s", order.getTotalMoney().setScale(2, RoundingMode.HALF_UP)));
        money = String.format("%s", order.getTotalMoney().setScale(2, RoundingMode.HALF_UP));
        orderNumber = order.getOrderNo();
        if (order.getList() != null && !order.getList().isEmpty()) {
            payTitle = order.getList().get(0).getProductName();
        }
        address = order.getAddress();
        reciverName = order.getName();
        recieverMobile = order.getMobile();
        orderId = order.getId();

        CheckChangedListener checkChangedListener = new CheckChangedListener();
        alipay.setOnCheckedChangeListener(checkChangedListener);
        wechatPay.setOnCheckedChangeListener(checkChangedListener);
        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID);
        api.registerApp(Constant.APP_ID);
    }

    @OnClick(R.id.left_btn)
    void back() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("温馨提示");
        builder.setMessage("订单会保留20分钟,请尽快支付");
        builder.setNegativeButton("确认离开", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                intent = new Intent(mContext, OrderListActivity.class);
                startActivity(intent);
                dialog.dismiss();
                finish();
            }
        });
        builder.setPositiveButton("继续支付", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();


            }
        });
        builder.show();

    }

    @OnClick(R.id.btn_pay)
    void pay() {
        if (payType == 1) {
            getPayInfo();
        } else if (payType == 2) {
            aliPay();
        }
    }

    private class CheckChangedListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                alipay.setChecked(false);
                wechatPay.setChecked(false);
                switch (buttonView.getId()) {
                    case R.id.wechat_pay:
                        wechatPay.setChecked(true);
                        payType = 1;
                        break;
                    case R.id.alipay:
                        alipay.setChecked(true);
                        payType = 2;
                        break;
                }
            } else {
                payType = 0;
            }
        }
    }

    private IWXAPI api;
    private PayInfo payInfo;

    private void wechatPay() {
        PayReq req = new PayReq();
        req.appId = payInfo.getAppId();
        req.partnerId = Constant.PARTER_ID;
        req.prepayId = payInfo.getPrepayId();
        req.nonceStr = payInfo.getNonceStr();
        req.timeStamp = payInfo.getTimeStamp();
        req.packageValue = "Sign=WXPay";
        req.sign = payInfo.getPaySign();
        req.extData = "app data";
        api.sendReq(req);
    }

    private void getPayInfo() {
        Call<ResultDO<PayInfo>> call = ApiClient.getApiAdapter().getNormalPayInfo(orderId);
        BaseApp.getInstance().setPayType(Constant.TYPE_NORMAL);
        BaseApp.getInstance().setOrder(order);
        showLoadingDialog(0);
        call.enqueue(new Callback<ResultDO<PayInfo>>() {
            @Override
            public void onResponse(Call<ResultDO<PayInfo>> call, Response<ResultDO<PayInfo>> response) {
                dissMissDialog();
                if (isFinish || response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    if (response.body().getResult() == null) {
                        return;
                    }
                    payInfo = response.body().getResult();
                    wechatPay();
                } else {
                    ToastUtils.showToast(mContext, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<PayInfo>> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
//                    PayResult payResult = new PayResult((String) msg.obj);
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    //Log.e("支付宝信息",payResult.toString());
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(mContext, "支付成功",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = null;
                        intent = new Intent(mContext, PaySuccessActivity.class);//支付成功界面
                        intent.putExtra("totalMoney", money);
                        intent.putExtra("address", address);
                        intent.putExtra("name", reciverName);
                        intent.putExtra("mobile", recieverMobile);
                        startActivity(intent);
                        finish();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(mContext, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(mContext, "支付失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(mContext, "检查结果为：" + msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        }


    };


    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;

    private void aliPay() {
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(Constant.ALI_APP_ID,orderNumber,payTitle, "订单信息", money,Constant.payNotifyUrl2);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = Constant.RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, true);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(OrderPayActivity.this);
                // 调用支付接口，获取支付结果
//                String result = alipay.pay(payInfo,true);
                Map<String,String> result = alipay.payV2(orderInfo,true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

}
