package com.bby.yishijie.member.ui.order;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.NoScrollListview;
import com.bby.yishijie.R;
import com.bby.yishijie.member.adapter.OrderProductAdapter;
import com.bby.yishijie.member.adapter.OrdetItemListAdapter;
import com.bby.yishijie.member.entity.Order;
import com.bby.yishijie.member.entity.OrderListItem;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.WebViewActivity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bby.yishijie.R.id.tvStatus;


/**
 * Created by 刘涛 on 2017/4/25.
 */

public class OrderDetailActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.order_status)
    TextView orderStatus;
    @Bind(R.id.user_name)
    TextView userName;
    @Bind(R.id.user_addr)
    TextView userAddr;
    @Bind(R.id.shop_name)
    TextView shopName;
    @Bind(R.id.list_view)
    NoScrollListview listView;
    @Bind(R.id.express_money)
    TextView expressMoney;
    @Bind(R.id.coupon_money)
    TextView couponMoney;
    @Bind(R.id.total_money)
    TextView totalMoney;
    @Bind(R.id.order_no)
    TextView orderNo;
    @Bind(R.id.order_create_time)
    TextView orderCreateTime;
    @Bind(R.id.order_pay_time)
    TextView orderPayTime;
    @Bind(R.id.order_send_time)
    TextView orderSendTime;
    @Bind(R.id.order_btn1)
    TextView orderBtn1;
    @Bind(R.id.order_btn2)
    TextView orderBtn2;
    @Bind(R.id.btn_layout)
    RelativeLayout btnLayout;
    @Bind(R.id.order_btn3)
    TextView orderBtn3;

    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        order = (Order) getIntent().getSerializableExtra("order");
        titleView.setText("订单详情");
        initView();
    }

    private void initView() {
        if (order == null) {
            return;
        }
        btnLayout.setVisibility(View.VISIBLE);
        switch (order.getStatus()) {
            case -1:
                orderStatus.setText("已取消");
                orderBtn1.setText("删除订单");
                orderBtn2.setVisibility(View.GONE);
                break;
            case 0:
                orderStatus.setText("待付款");
                orderBtn1.setText("立即支付");
                orderBtn2.setText("取消订单");
                break;
            case 1:
                orderStatus.setText("待发货");
                orderBtn1.setVisibility(View.VISIBLE);
                orderBtn1.setText("申请退款");
                orderBtn2.setVisibility(View.GONE);
                break;
            case 2:
                orderStatus.setText("待收货");
                orderBtn1.setText("确认收货");
                orderBtn2.setText("查看物流");
                orderBtn3.setVisibility(View.VISIBLE);
                orderBtn3.setText("申请退货");
                break;
            case 5://正在退款
                orderStatus.setText("退款中");
                btnLayout.setVisibility(View.VISIBLE);
                orderBtn1.setVisibility(View.VISIBLE);
                orderBtn2.setVisibility(View.GONE);
                orderBtn3.setVisibility(View.GONE);
                orderBtn1.setText("退款详情");
                break;
            case 6:
                //listHolder.tvStatus.setText(item.getTempStatus()==1?"退款成功":"退货成功");
                //listHolder.btnLayout.setVisibility(View.GONE);
                break;
            case 7://已退款
                orderStatus.setText("已退款");
                btnLayout.setVisibility(View.GONE);
                break;
            case 8://正退货
                orderStatus.setText("退货中");
                btnLayout.setVisibility(View.VISIBLE);
                orderBtn1.setVisibility(View.VISIBLE);
                orderBtn2.setVisibility(View.GONE);
                orderBtn3.setVisibility(View.GONE);
                orderBtn1.setText("退货详情");
                break;
            case 9://已退货
                orderStatus.setText("已退货");
                btnLayout.setVisibility(View.GONE);
                break;
            default:
                orderStatus.setText("已完成");
                btnLayout.setVisibility(View.GONE);
        }
        userName.setText(String.format("%1$s  %2$s", order.getName(), order.getMobile()));
        userAddr.setText(order.getAddress());
        shopName.setText(order.getShopName());
        setList();
//        if (order.getList() != null) {
//            int type = 0;//区分订单行是否需要隐藏退款
//            if (order.getVoucherFee().compareTo(BigDecimal.ZERO) >= 0 || order.getDiscountFee().compareTo(BigDecimal.ZERO) >= 0) {
//                type = 0;
//            } else {
//                type = 1;
//            }
//            OrderProductAdapter adapter = new OrderProductAdapter(mContext, order.getList(), type);
//            listView.setAdapter(adapter);
//            adapter.notifyDataSetChanged();
//        }
        expressMoney.setText(String.format("¥%s", order.getExpressFee().setScale(2, RoundingMode.HALF_UP)));
        couponMoney.setText(String.format("¥%s", order.getVoucherFee().setScale(2, RoundingMode.HALF_UP)));
        totalMoney.setText(String.format("¥%s", order.getTotalFee().setScale(2, RoundingMode.HALF_UP)));
        orderNo.setText(order.getOrderNo());
        orderCreateTime.setText(order.getCreateTime());
        orderPayTime.setText(order.getPayTime());
        orderSendTime.setText(order.getSendTime());

    }

    private void setList() {
        boolean isUsedVoucher = false;//区分订单行是否需要隐藏退款  区分是否用了优惠券
//        if (item.getVoucherFee().compareTo(BigDecimal.ZERO) > 0 || item.getDiscountFee().compareTo(BigDecimal.ZERO) > 0) {
//            type = 0;
//        } else {
//            type = 1;
//        }
        isUsedVoucher = order.getVoucherFee().compareTo(BigDecimal.ZERO) > 0;
        if (!order.getDatas().isEmpty()) {
            OrdetItemListAdapter adapter = new OrdetItemListAdapter(mContext, order.getDatas(), isUsedVoucher, order);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @OnClick({R.id.order_btn1, R.id.order_btn2, R.id.order_btn3})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_btn1:
                switch (order.getStatus()) {
                    case -1://删除订单
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("温馨提示");
                        builder.setMessage("确认删除订单吗？");
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                delOrder();

                            }
                        });
                        builder.show();
                        break;
                    case 0://支付订单
                        getOrderPay();
                        break;
                    case 1://申请退款
                        intent = new Intent(mContext, ApplyRefundActivity.class);
                        intent.putExtra("order", order);
                        intent.putExtra("type", 1);//申请退款
                        startActivity(intent);
                        break;
                    case 2://确认收货
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                        builder1.setTitle("温馨提示");
                        builder1.setMessage("确认收货吗？");
                        builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder1.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                confirmRecieve();

                            }
                        });
                        builder1.show();

                        break;
                    case 5:
                        //查看退款详情
                        intent = new Intent(mContext, RefundDetailActivity.class);
                        intent.putExtra("order", order);
                        startActivity(intent);
                        break;
                    case 8:
                        //查看退货详情
                        intent = new Intent(mContext, RefundDetailActivity.class);
                        intent.putExtra("order", order);
                        startActivity(intent);
                        break;
                }
                break;
            case R.id.order_btn2:
                switch (order.getStatus()) {
                    case 0://取消订单
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                        builder1.setTitle("温馨提示");
                        builder1.setMessage("确认取消订单吗？");
                        builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder1.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                cancelOrder();

                            }
                        });
                        builder1.show();
                        break;
                    case 2://查看物流
                        intent = new Intent(mContext, WebViewActivity.class);
                        String url = String.format("https://m.kuaidi100.com/index_all.html?type=%1$s&postid=%2$s", order.getExpressEng(), order.getExpressNo());
                        intent.putExtra("url", url);
                        intent.putExtra("title", "物流查询");
                        startActivity(intent);
                        break;
                }
                break;
            case R.id.order_btn3:
                //申请退货
                intent = new Intent(mContext, ApplyRefundActivity.class);
                intent.putExtra("order", order);
                intent.putExtra("type", 2);//申请退货
                startActivity(intent);
                break;
        }
    }

    private void delOrder() {
        showLoadingDialog(0);
        Call<ResultDO> call = ApiClient.getApiAdapter().delOrder(order.getId());
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                if (isFinish) {
                    return;
                }
                dissMissDialog();
                if (response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    finish();
                } else {
                    ToastUtils.showToast(mContext, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }

    private void getOrderPay() {
        Call<ResultDO<Order>> call = ApiClient.getApiAdapter().getOrderPay(order.getId());
        call.enqueue(new Callback<ResultDO<Order>>() {
            @Override
            public void onResponse(Call<ResultDO<Order>> call, Response<ResultDO<Order>> response) {
                if (isFinish) {
                    return;
                }
                if (response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    Order item = response.body().getResult();
                    intent = new Intent(mContext, OrderPayActivity.class);
                    intent.putExtra("order", item);
                    intent.putExtra("isGroupBuy", false);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Order>> call, Throwable t) {

            }
        });
    }

    private void confirmRecieve() {
        showLoadingDialog(0);
        Call<ResultDO> call = ApiClient.getApiAdapter().confirmRecieve(order.getId());
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                if (isFinish) {
                    return;
                }
                dissMissDialog();
                if (response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    finish();
                } else {
                    ToastUtils.showToast(mContext, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }

    private void cancelOrder() {
        showLoadingDialog(0);
        Call<ResultDO> call = ApiClient.getApiAdapter().cancelOrder(order.getId());
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                if (isFinish) {
                    return;
                }
                dissMissDialog();
                if (response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    finish();
                } else {
                    ToastUtils.showToast(mContext, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }

}
