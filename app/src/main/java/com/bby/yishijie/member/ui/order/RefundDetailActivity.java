package com.bby.yishijie.member.ui.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.ToastUtils;
import com.bby.yishijie.R;
import com.bby.yishijie.member.entity.Order;
import com.bby.yishijie.member.entity.RefundDetail;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.WebViewActivity;

import java.math.RoundingMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2017/6/26.
 */

public class RefundDetailActivity extends BaseActivity {


    @Bind(R.id.title_view)
    TextView titleView;

    @Bind(R.id.money)
    TextView money;
    @Bind(R.id.reason)
    TextView reason;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.txt)
    TextView txt;
    @Bind(R.id.txt_detail)
    TextView txtDetail;
    @Bind(R.id.txt_reason)
    TextView txtReason;

    private Order order;
    private RefundDetail refundDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_refund_detail);
        ButterKnife.bind(this);
        order = (Order) getIntent().getSerializableExtra("order");
        getDetail();
        titleView.setText(order.getStatus() == 5 ? "退款详情" : "退货详情");
        txt.setText(order.getStatus() == 5 ? "退款申请正在审核中" : "退货申请正在审核中");
        txtDetail.setText(order.getStatus() == 1 ? "退款明细" : "退货明细");
        txtReason.setText(order.getStatus() == 1 ? "退款原因：" : "退货原因：");


    }

    private void getDetail() {
        Call<ResultDO<RefundDetail>> call = ApiClient.getApiAdapter().getCustomerServiceInfo(order.getId());
        call.enqueue(new Callback<ResultDO<RefundDetail>>() {
            @Override
            public void onResponse(Call<ResultDO<RefundDetail>> call, Response<ResultDO<RefundDetail>> response) {
                if (isFinish || response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    refundDetail = response.body().getResult();
                    reason.setText(refundDetail.getReason());
                    time.setText(refundDetail.getTime());
                    money.setText(String.format("¥%s", refundDetail.getAmount() == null ? "0.00" : refundDetail.getAmount().setScale(2, RoundingMode.HALF_UP)));
                }

            }

            @Override
            public void onFailure(Call<ResultDO<RefundDetail>> call, Throwable t) {

            }
        });
    }


    @OnClick({R.id.btn_1, R.id.btn_2})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1:
                //撤销申请
                showLoadingDialog(0);
                Call<ResultDO> call = ApiClient.getApiAdapter().cancelRefundOrder(refundDetail.getCustomerServiceId());
                call.enqueue(new Callback<ResultDO>() {
                    @Override
                    public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                        if (isFinish || response.body() == null) {
                            return;
                        }
                        dissMissDialog();
                        if (response.body().getCode() == 0) {
                            ToastUtils.showToast(mContext, "已撤销申请");
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
                break;
            case R.id.btn_2:
                //
                intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url", ApiClient.CUSTOMER_URL);
                intent.putExtra("title", "客服");
                startActivity(intent);
                break;
        }
    }


}
