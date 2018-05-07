package com.bby.yishijie.member.ui.order;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.sunday.common.alipay.OrderInfoUtil2_0;
import com.sunday.common.alipay.PayResult;
import com.sunday.common.alipay.SignUtils;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.utils.SharePerferenceUtils;
import com.sunday.common.utils.SpannalbeStringUtils;
import com.sunday.common.utils.ToastUtils;
import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.common.Constant;
import com.bby.yishijie.member.entity.Address;
import com.bby.yishijie.member.entity.Gift;
import com.bby.yishijie.member.entity.GiftOrder;
import com.bby.yishijie.member.entity.Member;
import com.bby.yishijie.member.entity.PayInfo;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.MainActivity;
import com.bby.yishijie.member.ui.mine.AddressListActivity;
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

/**
 * Created by 刘涛 on 2017/4/28.
 */

public class GiftOrderConfirmActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.rightTxt)
    TextView rightTxt;
    @Bind(R.id.user_name)
    TextView userName;
    @Bind(R.id.user_mobile)
    TextView userMobile;
    @Bind(R.id.linearlayout)
    LinearLayout linearlayout;
    @Bind(R.id.user_addr)
    TextView userAddr;
    @Bind(R.id.select_img)
    ImageView selectImg;
    @Bind(R.id.address_layout)
    RelativeLayout addressLayout;
    @Bind(R.id.product_img)
    ImageView productImg;
    @Bind(R.id.product_title)
    TextView productTitle;
    @Bind(R.id.product_spec)
    TextView productSpec;
    @Bind(R.id.product_price)
    TextView productPrice;
    @Bind(R.id.product_num)
    TextView productNum;
    @Bind(R.id.pro_money)
    TextView proMoney;
    @Bind(R.id.total_money)
    TextView totalMoney;
    @Bind(R.id.select_pay_way)
    TextView selectPayWay;
    @Bind(R.id.wechat_pay)
    CheckBox wechatPay;
    @Bind(R.id.alipay)
    CheckBox alipay;
    @Bind(R.id.total_money_bottom)
    TextView totalMoneyBottom;
    @Bind(R.id.total_money_bottom_extra)
    TextView totalMoneyBottomExtra;
    @Bind(R.id.product_total_num)
    TextView productTotalNum;
    @Bind(R.id.confirm_pay)
    Button confirmPay;

    private Gift gift;
    private Address address;
    private long memberId;
    private CheckChangedListener checkChangedListener;
    private int payType = 0;
    private GiftOrder order;
    private String money;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_order_confirm);
        ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        titleView.setText("确认订单");
        rightTxt.setVisibility(View.GONE);
        memberId = BaseApp.getInstance().getMember().getId();
        gift = (Gift) getIntent().getSerializableExtra("gift");
        if (gift == null) {
            return;
        }
        if (!TextUtils.isEmpty(gift.getImage())) {
            Glide.with(mContext)
                    .load(gift.getImage())
                    .error(R.mipmap.ic_default)
                    .into(productImg);
        }
        productPrice.setText(String.format("¥%s",gift.getAmount().setScale(0,RoundingMode.HALF_UP)));
        totalMoney.setText(String.format("%s元",gift.getAmount().setScale(0,RoundingMode.HALF_UP)));
        productNum.setText("X1");
        productTitle.setText(gift.getName());
        productTotalNum.setText("共1件");
        totalMoneyBottom.setText(SpannalbeStringUtils.setTextColor("总金额:", getResources().getColor(R.color.black_6)));
        totalMoneyBottom.append(SpannalbeStringUtils.setTextColor(String.format("¥%s",gift.getAmount().setScale(2,RoundingMode.HALF_UP)), getResources().getColor(R.color.colorPrimary)));
        totalMoneyBottomExtra.setText(SpannalbeStringUtils.setTextColor("(含运费", getResources().getColor(R.color.black_6)));
        totalMoneyBottomExtra.append(SpannalbeStringUtils.setTextColor("￥0.00", getResources().getColor(R.color.colorPrimary)));
        totalMoneyBottomExtra.append(SpannalbeStringUtils.setTextColor(")", getResources().getColor(R.color.black_6)));


        checkChangedListener = new CheckChangedListener();
        alipay.setOnCheckedChangeListener(checkChangedListener);
        wechatPay.setOnCheckedChangeListener(checkChangedListener);
        api = WXAPIFactory.createWXAPI(getApplicationContext(), null);
        api.registerApp(Constant.APP_ID);
        getDefaultAddr();
    }

    private void getDefaultAddr() {
        showLoadingDialog(0);
        Call<ResultDO<Address>> call = ApiClient.getApiAdapter().getDefaultAddr(memberId);
        call.enqueue(new Callback<ResultDO<Address>>() {
            @Override
            public void onResponse(Call<ResultDO<Address>> call, Response<ResultDO<Address>> response) {
                if (isFinish) {
                    return;
                }
                dissMissDialog();
                ResultDO<Address> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    address = resultDO.getResult();
                    setAddr(address);
                } else {
                    ToastUtils.showToast(mContext, resultDO.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Address>> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });

    }

    private void setAddr(Address address) {
        if (address == null) {
            userName.setText("暂无收货地址");
            userMobile.setText("");
            userAddr.setText("请添加收货地址");
        } else {
            userName.setText("姓名:" + address.getName());
            userMobile.setText(address.getMobile());
            userAddr.setText(String.format("收货地址:%1s%2s", address.getCityDetail(), address.getAddress()));
        }
    }

    private final static int REQUEST_ADDR = 0x1111;


    @OnClick({R.id.address_layout, R.id.confirm_pay})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_pay:
                if (address == null) {
                    ToastUtils.showToast(mContext, "请选择收货地址");
                    return;
                }
                if (gift == null) {
                    return;
                }
                createOrder();
                break;
            case R.id.address_layout:
                intent = new Intent(mContext, AddressListActivity.class);
                intent.putExtra("isSelectMode", true);
                startActivityForResult(intent, REQUEST_ADDR);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_ADDR:
                address = (Address) data.getSerializableExtra("address");
                setAddr(address);
                break;
        }
    }

    private void createOrder() {
        showLoadingDialog(0);
        Call<ResultDO<GiftOrder>> call = ApiClient.getApiAdapter().createGiftOrder(address.getId(), gift.getId());
        call.enqueue(new Callback<ResultDO<GiftOrder>>() {
            @Override
            public void onResponse(Call<ResultDO<GiftOrder>> call, Response<ResultDO<GiftOrder>> response) {
                dissMissDialog();
                if (isFinish) {
                    return;
                }
                ResultDO<GiftOrder> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    order = resultDO.getResult();
                    money = order.getTotalFee().setScale(0, RoundingMode.HALF_UP).toString();
                    if (payType == 1) {
                        if (DeviceUtils.isWeixinAvilible(mContext)) {
                            getPayInfo();
                        } else {
                            ToastUtils.showToast(mContext, "请安装微信客户端");
                        }
                    } else if (payType==2){
                            aliPay();
                    } else {
                        ToastUtils.showToast(mContext, "请选择支付方式");
                    }

                }
            }

            @Override
            public void onFailure(Call<ResultDO<GiftOrder>> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }


    private void getPayInfo() {
        Call<ResultDO<PayInfo>> call = ApiClient.getApiAdapter().getOpenShopPayInfo(order.getId());
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
                }
            }

            @Override
            public void onFailure(Call<ResultDO<PayInfo>> call, Throwable t) {
                dissMissDialog();
            }
        });
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

    private Handler mHandler = new
            Handler() {
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
//                        Toast.makeText(mContext, "支付成功",
//                                Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("恭喜您");
                        builder.setMessage("开店成功，您的等级升级为店主，可使用该手机号登录店主版，享受店主权益！");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                getMemberInfo();
                            }
                        });
                        builder.show();

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

    private void getMemberInfo() {
        showLoadingDialog(0);
        Call<ResultDO<Member>> call = ApiClient.getApiAdapter().getMemberInfo(BaseApp.getInstance().getMember().getId());
        call.enqueue(new Callback<ResultDO<Member>>() {
            @Override
            public void onResponse(Call<ResultDO<Member>> call, Response<ResultDO<Member>> response) {
                dissMissDialog();
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
                    SharePerferenceUtils.getIns(mContext).saveOAuth(response.body().getResult());
                    BaseApp.getInstance().setMember(response.body().getResult());
                    intent = new Intent(mContext, MainActivity.class);//支付成功界面
                    intent.putExtra("pageNo", 4);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Member>> call, Throwable t) {
                dissMissDialog();
            }
        });
    }


    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;

    private void aliPay() {
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(Constant.ALI_APP_ID,order.getOrderNo(),"易饰界", "店主年费大礼包", money,Constant.payNotifyUrl1);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = Constant.RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, true);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(GiftOrderConfirmActivity.this);
                // 调用支付接口，获取支付结果
                Map<String,String> result = alipay.payV2(orderInfo,true);
//                String result = alipay.pay(payInfo,true);
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
