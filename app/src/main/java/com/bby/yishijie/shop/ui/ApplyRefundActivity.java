package com.bby.yishijie.shop.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.shop.adapter.OrderProductAdapter;
import com.bby.yishijie.shop.entity.Order;
import com.bby.yishijie.shop.entity.OrderListBean;
import com.bby.yishijie.shop.entity.OrderListItem;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.NoScrollListview;

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

/**
 * Created by 刘涛 on 2017/6/26.
 */

public class ApplyRefundActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.order_no)
    TextView orderNo;
    @Bind(R.id.list_view)
    NoScrollListview listView;
    @Bind(R.id.refund_money)
    TextView refundMoney;
    @Bind(R.id.edit_refund_money)
    EditText editRefundMoney;
    @Bind(R.id.txt_reason)
    TextView txtReason;
    @Bind(R.id.edit_reason)
    EditText editReason;
    @Bind(R.id.btn_submit)
    TextView btnSubmit;
    @Bind(R.id.refund_type)
    TextView refundType;

    private Order order;
    private String mobile;
    private int type;
    private int position = -1;
    private List<OrderListBean> refundList = new ArrayList<>();
    private boolean isHaveScore;
    private int parentP = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_refund);
        ButterKnife.bind(this);
        order = (Order) getIntent().getSerializableExtra("order");
        type = getIntent().getIntExtra("type", 1);
        position = getIntent().getIntExtra("position", -1);
        parentP = getIntent().getIntExtra("parentP", -1);
        titleView.setText("申请售后");
        orderNo.setText("订单编号：" + order.getOrderNo());

        if (!order.getDatas().isEmpty()) {
            if (order.getScoreFee().compareTo(BigDecimal.ZERO) > 0) {
                isHaveScore = true;
            }
            List<OrderListBean> dataSet = new ArrayList<>();
            if (parentP == -1) {
                for (OrderListItem item : order.getDatas()) {
                    for (OrderListBean listBean : item.getOrderListBeanList()) {
                        if (listBean.getStatus() == order.getStatus() && (listBean.getIsScore() != 1 || (listBean.getIsScore() == 1 && !isHaveScore))) {
                            dataSet.add(listBean);
                        }
                        if (listBean.getStatus() > 4) {
                            refundList.add(listBean);
                        }
                    }
                }
            } else {
                dataSet.add(order.getDatas().get(parentP).getOrderListBeanList().get(position));
            }
            OrderProductAdapter adapter = new OrderProductAdapter(mContext, dataSet, 1, 0, isHaveScore);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }


//        if (order.getList() != null) {
//            List<OrderListBean> dataSet = new ArrayList<>();
//            if (position == -1) {
//                for (OrderListBean item : order.getList()) {
//                    if (item.getIsScore() == 1 && order.getScoreFee().compareTo(BigDecimal.ZERO) > 0) {
//                        isHaveScore = true;
//                    }
//                    if (item.getStatus() == order.getStatus() && (item.getIsScore() != 1 || (item.getIsScore() == 1 && order.getScoreFee().compareTo(BigDecimal.ZERO) <= 0))) {
//                        dataSet.add(item);
//                    }
//                    if (item.getStatus() > 4) {
//                        refundList.add(item);
//                    }
//                }
//            } else {
//                dataSet.add(order.getList().get(position));
//            }
//            OrderProductAdapter adapter = new OrderProductAdapter(mContext, dataSet, 1, 0,
//                    order.getScoreFee().compareTo(BigDecimal.ZERO) > 0);
//            listView.setAdapter(adapter);
//            adapter.notifyDataSetChanged();
//        }
        refundType.setText(type == 1 ? "退款" : "退货");

        BigDecimal refunMoney;
        if (position == -1) {
            BigDecimal refundedMoney = BigDecimal.ZERO;
            if (!refundList.isEmpty()) {
                for (OrderListBean item : refundList) {
                    refundedMoney = refundedMoney.add(item.getPrice().multiply(new BigDecimal(item.getNum())));
                }
            }
            refunMoney = order.getRealPay().add(order.getMoneyFee()).subtract(refundedMoney);
        } else {
            refunMoney = order.getDatas().get(parentP).getOrderListBeanList().get(position).getPrice().
                    multiply(BigDecimal.valueOf(order.getDatas().get(parentP).getOrderListBeanList().get(position).getNum()));
        }
        if (type == 1 && !isHaveScore || (type == 2 && parentP != -1)) {
            refundMoney.setText(String.format("可退金额（退款总金额：%s）", refunMoney.setScale(2, RoundingMode.HALF_UP)));
            editRefundMoney.setText(String.format("%s", refunMoney.setScale(2, RoundingMode.HALF_UP)));
        } else {
            refundMoney.setText(String.format("可退金额（退款总金额：%s）", refunMoney.subtract(order.getExpressFee()).setScale(2, RoundingMode.HALF_UP)));
            editRefundMoney.setText(String.format("%s", refunMoney.subtract(order.getExpressFee()).setScale(2, RoundingMode.HALF_UP)));
        }

        editRefundMoney.setEnabled(false);
        mobile = BaseApp.getInstance().getShopMember().getMobile();

    }


    @OnClick(R.id.btn_submit)
    void save() {
        final String reason = editReason.getText().toString().trim();
        if (TextUtils.isEmpty(reason)) {
            ToastUtils.showToast(mContext, "请填写具体原因");
            return;
        }
        String amount = editRefundMoney.getText().toString().trim();
//        BigDecimal bd=new BigDecimal(amount);
//        if (bd.compareTo(order.getRealPay().add(order.getMoneyFee()))==1){
//            ToastUtils.showToast(mContext,"已超出可退款最大金额");
//            return;
//        }
        showLoadingDialog(0);
        Call<ResultDO> call = ApiClient.getApiAdapter().applyRefundOrder(order.getId(), type, reason, mobile, null, amount, null,
                position == -1 ? null : order.getList().get(position).getItemId());
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                if (isFinish || response.body() == null) {
                    return;
                }
                dissMissDialog();
                if (response.body().getCode() == 0) {
                    ToastUtils.showToast(mContext, "提交成功");
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
