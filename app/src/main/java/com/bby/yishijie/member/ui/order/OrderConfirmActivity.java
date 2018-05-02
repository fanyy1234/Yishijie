package com.bby.yishijie.member.ui.order;

import android.annotation.SuppressLint;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bby.yishijie.member.entity.CartItem;
import com.sunday.common.alipay.OrderInfoUtil2_0;
import com.sunday.common.alipay.PayResult;
import com.sunday.common.alipay.SignUtils;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.SpannalbeStringUtils;
import com.sunday.common.utils.StringUtils;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.NoScrollListview;
import com.bby.yishijie.R;
import com.bby.yishijie.member.adapter.OrderConfirmAdapter;
import com.bby.yishijie.member.adapter.OrderConfirmProAdapter;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.common.Constant;
import com.bby.yishijie.member.entity.Address;
import com.bby.yishijie.member.entity.CartListItem;
import com.bby.yishijie.member.entity.CartPay;
import com.bby.yishijie.member.entity.Order;
import com.bby.yishijie.member.entity.PayInfo;
import com.bby.yishijie.member.entity.PostFee;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.mine.AddressListActivity;
import com.bby.yishijie.member.ui.mine.voucher.SelectVoucherListActivity;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 刘涛 on 2017/4/20.
 */

public class OrderConfirmActivity extends BaseActivity {

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
    @Bind(R.id.list_view)
    NoScrollListview listView;
    @Bind(R.id.coupon_num)
    TextView couponNum;

    @Bind(R.id.express_money)
    TextView expressMoney;
    @Bind(R.id.total_money)
    TextView totalMoney;
    @Bind(R.id.total_money_bottom)
    TextView totalMoneyBottom;
    @Bind(R.id.total_score)
    TextView totalScore;
    @Bind(R.id.product_num)
    TextView productNum;
    @Bind(R.id.confirm_pay)
    Button confirmPay;
    @Bind(R.id.edit_remark)
    EditText editRemark;
    @Bind(R.id.select_pay_way)
    TextView selectPayWay;
    @Bind(R.id.wechat_pay)
    CheckBox wechatPay;
    @Bind(R.id.alipay)
    CheckBox alipay;
    @Bind(R.id.select_img)
    ImageView selectImg;
    @Bind(R.id.address_layout)
    RelativeLayout addressLayout;
    @Bind(R.id.edit_real_name)
    EditText editRealName;
    @Bind(R.id.ll_real_name)
    LinearLayout llRealName;
    @Bind(R.id.edit_id_no)
    EditText editIdNo;
    @Bind(R.id.ll_id)
    LinearLayout llId;
    @Bind(R.id.select_coupon)
    LinearLayout selectCoupon;
    @Bind(R.id.total_money_bottom_extra)
    TextView totalMoneyBottomExtra;
    @Bind(R.id.bottom_layout)
    RelativeLayout bottomLayout;
    @Bind(R.id.select_send_method)
    TextView selectSendMethod;

    private CartPay cartPay;
    private String cartIds;
    private long memberId;
    private Address address;
    private Integer voucherId, moneyId;
    private String message, realName, idNo;
    private BigDecimal productMoney = BigDecimal.ZERO, postFee = BigDecimal.ZERO;


    private int payType = 1;
    private IWXAPI api;
    private Order order;
    private int isActive;
    private List<CartListItem> dataSet = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);
        ButterKnife.bind(this);
        initView();
    }

    @SuppressLint("DefaultLocale")
    private void initView() {
        memberId = BaseApp.getInstance().getMember().getId();
        cartPay = (CartPay) getIntent().getSerializableExtra("cartPay");
        cartIds = getIntent().getStringExtra("cartIds");
        isActive = getIntent().getIntExtra("isActive", 0);
        rightTxt.setVisibility(View.GONE);
        titleView.setText("确认订单");
        if (cartPay != null) {
            productMoney = cartPay.getAmount();
            postFee = cartPay.getPostFee();
            totalMoney.setText(String.format("¥%s", cartPay.getAmount().add(cartPay.getPostFee()).setScale(2, RoundingMode.HALF_UP)));
            productNum.setText(String.format("共%d件", cartPay.getCount()));
            expressMoney.setText(String.format("¥%s", cartPay.getPostFee().setScale(2, RoundingMode.HALF_UP)));
            totalMoneyBottom.setText(SpannalbeStringUtils.setTextColor("总金额:", getResources().getColor(R.color.black_6)));
            totalMoneyBottom.append(SpannalbeStringUtils.setTextColor("¥" + cartPay.getAmount().add(cartPay.getPostFee()).setScale(2, RoundingMode.HALF_UP), getResources().getColor(R.color.colorPrimary)));

            int totalScoreInt = 0 ;
            List<CartItem> cartItems = cartPay.getList();
            for (CartItem cartItem : cartItems){
                totalScoreInt += cartItem.getScore();
            }
            totalScore.setText(SpannalbeStringUtils.setTextColor("总积分:", getResources().getColor(R.color.black_6)));
            totalScore.append(SpannalbeStringUtils.setTextColor( totalScoreInt+"", getResources().getColor(R.color.appcolor)));
            //totalMoneyBottomExtra.setText(SpannalbeStringUtils.setTextColor("(含运费", getResources().getColor(R.color.black_6)));
            //totalMoneyBottomExtra.append(SpannalbeStringUtils.setTextColor("¥" + cartPay.getPostFee(), getResources().getColor(R.color.colorPrimary)));
            //totalMoneyBottomExtra.append(SpannalbeStringUtils.setTextColor(")", getResources().getColor(R.color.black_6)));
            getTotalCartList();
        }
        wechatPay.setChecked(true);
        CheckChangedListener checkChangedListener = new CheckChangedListener();
        alipay.setOnCheckedChangeListener(checkChangedListener);
        wechatPay.setOnCheckedChangeListener(checkChangedListener);
        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID);
        api.registerApp(Constant.APP_ID);
        getDefaultAddr();
        llId.setVisibility(cartPay.getIsCross() == 1 ? View.VISIBLE : View.GONE);
        llRealName.setVisibility(cartPay.getIsCross() == 1 ? View.VISIBLE : View.GONE);

        if(cartPay.getIsOut()==1||cartPay.getIsCross()==1){
            sendType=1;
            selectSendMethod.setText("邮寄");
        }

    }

    private void getTotalCartList() {
        if (cartPay.getList() != null && !cartPay.getList().isEmpty()) {
            CartListItem cartListItem = new CartListItem();
            cartListItem.setTypeName("好物购物");
            cartListItem.setType(0);
            cartListItem.setCartItemList(cartPay.getList());
            dataSet.add(cartListItem);
        }

        if (cartPay.getActiveList() != null && !cartPay.getActiveList().isEmpty()) {
            CartListItem cartListItem = new CartListItem();
            cartListItem.setTypeName("满减商品");
            cartListItem.setType(2);
            cartListItem.setCartItemList(cartPay.getActiveList());
            dataSet.add(cartListItem);
        }
        if (cartPay.getCrossList() != null && !cartPay.getCrossList().isEmpty()) {
            CartListItem cartListItem = new CartListItem();
            cartListItem.setTypeName("跨境商品");
            cartListItem.setType(3);
            cartListItem.setCartItemList(cartPay.getCrossList());
            dataSet.add(cartListItem);
        }
        if (cartPay.getLimitList() != null && !cartPay.getLimitList().isEmpty()) {
            CartListItem cartListItem = new CartListItem();
            cartListItem.setTypeName("限时抢购");
            cartListItem.setType(4);
            cartListItem.setCartItemList(cartPay.getLimitList());
            dataSet.add(cartListItem);
        }

        OrderConfirmAdapter adapter = new OrderConfirmAdapter(mContext, dataSet);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
    private final static int REQUEST_COUNPON = 0x1112;

    @OnClick({R.id.select_coupon, R.id.confirm_pay, R.id.address_layout, R.id.ll_select_send_method})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_coupon:
                intent = new Intent(mContext, SelectVoucherListActivity.class);
                intent.putExtra("money", cartPay.getAmount().doubleValue());
                intent.putExtra("postFee", postFee.doubleValue());
                startActivityForResult(intent, REQUEST_COUNPON);
                break;
            case R.id.confirm_pay:
                if (address == null) {
                    ToastUtils.showToast(mContext, "请选择收货地址");
                    return;
                }
                if (cartPay == null) {
                    return;
                }
                if (payType == 0) {
                    ToastUtils.showToast(mContext, "请选择支付方式");
                    return;
                }
                if (sendType == 0) {
                    ToastUtils.showToast(mContext, "请选择配送方式");
                    return;
                }
                realName = editRealName.getText().toString().trim();
                idNo = editIdNo.getText().toString().trim();
                if (cartPay.getIsCross() == 1) {
                    if (TextUtils.isEmpty(realName) || TextUtils.isEmpty(idNo)) {
                        ToastUtils.showToast(mContext, "保税商品请填写真实姓名和身份证号");
                        return;
                    }
                    try {
                        if (!TextUtils.isEmpty(StringUtils.IDCardValidate(idNo))) {
                            ToastUtils.showToast(mContext, "请输入正确身份证号");
                            return;
                        }
                    } catch (ParseException e) {
                        ToastUtils.showToast(mContext, "请输入正确身份证号");
                        e.printStackTrace();
                    }
                }

                createOrder();
                break;
            case R.id.address_layout:
                intent = new Intent(mContext, AddressListActivity.class);
                intent.putExtra("isSelectMode", true);
                startActivityForResult(intent, REQUEST_ADDR);
                break;
            case R.id.ll_select_send_method:
                //含外地仓或者跨境无法选择同城自提
                if (cartPay.getIsOut() == 1||cartPay.getIsCross()==1) {
                    return;
                } else {
                    setSendMethod();
                }
                break;
        }
    }

    private int sendType;

    private void setSendMethod() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("配送方式");
        final String[] methods = {"邮寄", "同城自提"};
        //    设置一个单项选择下拉框
        /**
         * 第一个参数指定我们要显示的一组下拉单选框的数据集合
         * 第二个参数代表索引，指定默认哪一个单选框被勾选上
         * 第三个参数给每一个单选项绑定一个监听器
         */
        builder.setSingleChoiceItems(methods, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectSendMethod.setText(methods[which]);
                sendType = which + 1;
                if (sendType == 1 && cartPay.getIsFreePost() == 0) {
                    getPostFee();
                } else {
                    postFee = BigDecimal.ZERO;
                    voucherId = null;
                    couponMoney = 0;
                    couponNum.setText("未选择");
                    reCountTotalMoney();
                }
                dialog.dismiss();
            }
        });
        builder.setCancelable(true);
        builder.show();
    }


    double couponMoney;

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
                if (cartPay.getIsFreePost() == 0 && sendType != 2) {
                    getPostFee();
                }
                break;
            case REQUEST_COUNPON:
                voucherId = data.getIntExtra("id", 0);
                couponMoney = data.getDoubleExtra("money", 0);
                reCountTotalMoney();
                break;
        }
    }

    private void getPostFee() {
        showLoadingDialog(0);
        Call<ResultDO<PostFee>> call = ApiClient.getApiAdapter().getPostFee(address.getId(), String.valueOf(cartPay.getAmount()));
        call.enqueue(new Callback<ResultDO<PostFee>>() {
            @Override
            public void onResponse(Call<ResultDO<PostFee>> call, Response<ResultDO<PostFee>> response) {
                dissMissDialog();
                if (isFinish || response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    postFee = response.body().getResult().getPostFee();
                    reCountTotalMoney();
                }
            }

            @Override
            public void onFailure(Call<ResultDO<PostFee>> call, Throwable t) {
                dissMissDialog();
            }
        });
    }

    private void reCountTotalMoney() {
        BigDecimal coupMoney = new BigDecimal(Double.toString(couponMoney));
        if (coupMoney.compareTo(BigDecimal.ZERO) > 0) {
            couponNum.setText(String.format("¥%s", coupMoney.setScale(2, RoundingMode.HALF_UP)));
        }
        expressMoney.setText(String.format("¥%s", postFee.setScale(2, RoundingMode.HALF_UP)));
        BigDecimal allMoney = productMoney.add(postFee).subtract(coupMoney);
        totalMoney.setText(String.format("¥%s", allMoney.setScale(2, RoundingMode.HALF_UP)));
        totalMoneyBottom.setText(String.format("¥%s", allMoney.setScale(2, RoundingMode.HALF_UP)));
    }

    private void createOrder() {
        showLoadingDialog(0);
        message = editRemark.getText().toString().trim();
        if (!TextUtils.isEmpty(cartIds)) {
            //购物车提交订单
            Call<ResultDO<Order>> call = ApiClient.getApiAdapter().createOrderNew(address.getId(), cartIds, voucherId,message, 1, idNo, realName, isActive == 0 ? null : 1, sendType);
            call.enqueue(new Callback<ResultDO<Order>>() {
                @Override
                public void onResponse(Call<ResultDO<Order>> call, Response<ResultDO<Order>> response) {
                    dissMissDialog();
                    if (isFinish) {
                        return;
                    }
                    ResultDO<Order> resultDO = response.body();
                    if (resultDO == null) {
                        return;
                    }
                    if (resultDO.getCode() == 0) {
                        if (resultDO.getResult() == null) {
                            return;
                        }
                        order = resultDO.getResult();
                        if (payType == 1) {
                            getPayInfo();
                        } else if (payType == 2) {
                            aliPay();
                        }
//                        intent = new Intent(mContext, OrderPayActivity.class);
//                        intent.putExtra("order", resultDO.getResult());
//                        intent.putExtra("isGroupBuy", isGroupBuy);
//                        startActivity(intent);
                    } else {
                        ToastUtils.showToast(mContext, resultDO.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResultDO<Order>> call, Throwable t) {
                    dissMissDialog();
                    ToastUtils.showToast(mContext, R.string.network_error);
                }
            });
        } else {
            //立即购买提交订单
            long cartId = 0;
            if (!cartPay.getList().isEmpty()) {
                cartId = cartPay.getList().get(0).getId();
            } else if (!cartPay.getCrossList().isEmpty()) {
                cartId = cartPay.getCrossList().get(0).getId();
            } else if (!cartPay.getActiveList().isEmpty()) {
                cartId = cartPay.getActiveList().get(0).getId();
            } else if (!cartPay.getLimitList().isEmpty()) {
                cartId = cartPay.getLimitList().get(0).getId();
            }
            createOrderBuyNow(cartId);
        }

    }


    private void createOrderBuyNow(long cartId) {
        showLoadingDialog(0);
        Call<ResultDO<Order>> call = ApiClient.getApiAdapter().createOrderBuyNow(address.getId(), cartId, voucherId, moneyId, message, 1, null, idNo, realName, sendType);
        call.enqueue(new Callback<ResultDO<Order>>() {
            @Override
            public void onResponse(Call<ResultDO<Order>> call, Response<ResultDO<Order>> response) {
                dissMissDialog();
                if (isFinish) {
                    return;
                }
                ResultDO<Order> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    if (resultDO.getResult() == null) {
                        return;
                    }
                    order = resultDO.getResult();
                    if (payType == 1) {
                        getPayInfo();
                    } else if (payType == 2) {
                        aliPay();
                    }
//                    intent = new Intent(mContext, OrderPayActivity.class);
//                    intent.putExtra("order", resultDO.getResult());
//                    intent.putExtra("isGroupBuy", isGroupBuy);
//                    startActivity(intent);
                } else {
                    ToastUtils.showToast(mContext, resultDO.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Order>> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }


    private PayInfo payInfo;

    private void getPayInfo() {
        Call<ResultDO<PayInfo>> call = ApiClient.getApiAdapter().getNormalPayInfo(order.getId());
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
                        Intent intent = new Intent(mContext, OrderListActivity.class);//支付成功界面
//                            intent.putExtra("totalMoney", money);
//                            intent.putExtra("address", address);
//                            intent.putExtra("name", reciverName);
//                            intent.putExtra("mobile", recieverMobile);
                        intent.putExtra("page", 2);
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
                            intent = new Intent(mContext, OrderListActivity.class);
                            intent.putExtra("page", 1);
                            startActivity(intent);
                            finish();
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
    private String payTitle;

    private void aliPay() {
        payTitle = order.getList().get(0).getProductName();
        String money = String.format("%s", order.getTotalMoney().setScale(2, RoundingMode.HALF_UP));
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(Constant.ALI_APP_ID,order.getOrderNo(),payTitle, "订单信息", money,Constant.payNotifyUrl2);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = Constant.RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, true);
        final String orderInfo = orderParam + "&" + sign;
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(OrderConfirmActivity.this);
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
