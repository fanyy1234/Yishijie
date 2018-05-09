package com.bby.yishijie.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.shop.entity.Order;
import com.bby.yishijie.shop.ui.OrderDetailActivity;
import com.sunday.common.utils.SpannalbeStringUtils;
import com.sunday.common.widgets.NoScrollListview;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by 刘涛 on 2017/4/24.
 */

public class OrderAdapter extends RecyclerView.Adapter {


    private Context mContext;
    private List<Order> dataSet;
    private View.OnClickListener onClickListener;

    public OrderAdapter(Context context, List<Order> datas) {
        mContext = context;
        dataSet = datas;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.order_list_item_shop, null);
        return new ListHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListHolder listHolder = (ListHolder) holder;
        final Order item = dataSet.get(position);

        boolean isUsedVoucher = false;//区分是否用了优惠券
        boolean isUsedScore = false;//区分是否用了积分支付
        isUsedVoucher = item.getVoucherFee().compareTo(BigDecimal.ZERO) > 0;
        isUsedScore = item.getScoreFee().compareTo(BigDecimal.ZERO) > 0;

        if (!item.getDatas().isEmpty()) {
            OrdetItemListAdapter adapter = new OrdetItemListAdapter(mContext, item.getDatas(), isUsedVoucher, isUsedScore, item.getIsBuy() == 1 ? 0 : 1, item);
            listHolder.listview.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }


        listHolder.shopName.setText(item.getCreateTime());
        listHolder.totalMoney.setText(SpannalbeStringUtils.setTextColor("实付款:", mContext.getResources().getColor(R.color.black_3)));
        listHolder.totalMoney.append(SpannalbeStringUtils.setTextColor(String.format("¥%s", item.getTotalMoney().setScale(2, RoundingMode.HALF_UP)),
                mContext.getResources().getColor(R.color.voucher_blue)));
        if (item.getExpressFee().compareTo(BigDecimal.ZERO) >= 0) {
            listHolder.expressMoney.setText(String.format("(运费￥%s)", item.getExpressFee()));
        }

        if (item.getIsBuy() == 1) {
            listHolder.btnLayout.setVisibility(View.VISIBLE);
            listHolder.buyFlag.setText("买");
            listHolder.buyFlag.setBackgroundColor(mContext.getResources().getColor(R.color.voucher_blue));
            switch (item.getStatus()) {
                case -1:
                    listHolder.tvStatus.setText("已取消");
                    listHolder.orderBtn1.setVisibility(View.VISIBLE);
                    listHolder.orderBtn2.setVisibility(View.GONE);
                    listHolder.orderBtn3.setVisibility(View.GONE);
                    listHolder.orderBtn1.setText("删除订单");
                    break;
                case 0:
                    listHolder.tvStatus.setText("待付款");
                    listHolder.orderBtn1.setVisibility(View.VISIBLE);
                    listHolder.orderBtn2.setVisibility(View.VISIBLE);
                    listHolder.orderBtn3.setVisibility(View.GONE);
                    listHolder.orderBtn1.setText("立即支付");
                    listHolder.orderBtn2.setText("取消订单");
                    break;
                case 1:
                    listHolder.tvStatus.setText("待发货");
//                    boolean isShowBtn = false;
//                    for (OrderListBean listBean : item.getList()) {
//                        if (listBean.getStatus() == 1 && (listBean.getIsScore() != 1
//                                || listBean.getIsScore() == 1 && item.getScoreFee().compareTo(BigDecimal.ZERO) <= 0)) {
//                            isShowBtn = true;
//                            break;
//                        }
//                    }
                    if ((item.getRealPay().compareTo(BigDecimal.ZERO) == 0 &&
                            item.getMoneyFee().compareTo(BigDecimal.ZERO) == 0)
                            ) {
                        listHolder.btnLayout.setVisibility(View.GONE);
                    } else {
                        listHolder.orderBtn1.setVisibility(View.VISIBLE);
                        listHolder.orderBtn2.setVisibility(View.GONE);
                        listHolder.orderBtn3.setVisibility(View.GONE);
                        listHolder.orderBtn1.setText("申请退款");
                    }
                    break;
                case 2:
                    listHolder.tvStatus.setText("待收货");
                    if (item.getRealPay().compareTo(BigDecimal.ZERO) == 0 &&
                            item.getMoneyFee().compareTo(BigDecimal.ZERO) == 0) {
                        listHolder.orderBtn3.setVisibility(View.GONE);
                    } else {
                        listHolder.orderBtn3.setVisibility(View.VISIBLE);
                        listHolder.orderBtn3.setText("申请退货");
                    }
                    listHolder.orderBtn1.setVisibility(View.VISIBLE);
                    listHolder.orderBtn2.setVisibility(View.VISIBLE);
                    listHolder.orderBtn1.setText("确认收货");
                    listHolder.orderBtn2.setText("查看物流");
                    break;
                case 5:
                    listHolder.tvStatus.setText("退款中");
                    listHolder.btnLayout.setVisibility(View.VISIBLE);
                    listHolder.orderBtn1.setVisibility(View.VISIBLE);
                    listHolder.orderBtn2.setVisibility(View.GONE);
                    listHolder.orderBtn3.setVisibility(View.GONE);
                    listHolder.orderBtn1.setText("退款详情");
                    break;
                case 7://已退款
                    listHolder.tvStatus.setText("已退款");
                    listHolder.btnLayout.setVisibility(View.GONE);
                    break;
                case 8://正退货
                    listHolder.tvStatus.setText("退货中");
                    listHolder.btnLayout.setVisibility(View.VISIBLE);
                    listHolder.orderBtn1.setVisibility(View.VISIBLE);
                    listHolder.orderBtn2.setVisibility(View.GONE);
                    listHolder.orderBtn3.setVisibility(View.GONE);
                    listHolder.orderBtn1.setText("退货详情");
                    break;
                case 9://已退货
                    listHolder.tvStatus.setText("已退货");
                    listHolder.btnLayout.setVisibility(View.GONE);
                    break;
                default:
                    listHolder.tvStatus.setText("已完成");
                    listHolder.btnLayout.setVisibility(View.GONE);
            }
        } else {
            listHolder.btnLayout.setVisibility(View.GONE);
            switch (item.getStatus()) {
                case -1:
                    listHolder.tvStatus.setText("已取消");
                    break;
                case 0:
                    listHolder.tvStatus.setText("待付款");
                    break;
                case 1:
                    listHolder.tvStatus.setText("待发货");
                    break;
                case 2:
                    listHolder.tvStatus.setText("待收货");
                    break;
                case 5://正在退款
                    listHolder.tvStatus.setText("退款中");
//                    listHolder.btnLayout.setVisibility(View.VISIBLE);
//                    listHolder.orderBtn1.setVisibility(View.VISIBLE);
//                    listHolder.orderBtn2.setVisibility(View.GONE);
//                    listHolder.orderBtn3.setVisibility(View.GONE);
//                    listHolder.orderBtn1.setText("退款详情");
                    break;
                case 6:
                    //listHolder.tvStatus.setText(item.getTempStatus()==1?"退款成功":"退货成功");
                    //listHolder.btnLayout.setVisibility(View.GONE);
                    break;
                case 7://已退款
                    listHolder.tvStatus.setText("已退款");
                    listHolder.btnLayout.setVisibility(View.GONE);
                    break;
                case 8://正退货
                    listHolder.tvStatus.setText("退货中");
//                    listHolder.btnLayout.setVisibility(View.VISIBLE);
//                    listHolder.orderBtn1.setVisibility(View.VISIBLE);
//                    listHolder.orderBtn2.setVisibility(View.GONE);
//                    listHolder.orderBtn3.setVisibility(View.GONE);
//                    listHolder.orderBtn1.setText("退货详情");
                    break;
                case 9://已退货
                    listHolder.tvStatus.setText("已退货");
                    listHolder.btnLayout.setVisibility(View.GONE);
                    break;
                default:
                    listHolder.tvStatus.setText("已完成");
            }
            listHolder.buyFlag.setText("卖");
            listHolder.buyFlag.setBackgroundColor(mContext.getResources().getColor(R.color.main_color));
        }

        listHolder.itemView.setOnClickListener(onClickListener);
        listHolder.orderBtn1.setOnClickListener(onClickListener);
        listHolder.orderBtn2.setOnClickListener(onClickListener);
        listHolder.orderBtn3.setOnClickListener(onClickListener);
        listHolder.itemView.setTag(position);
        listHolder.orderBtn1.setTag(position);
        listHolder.orderBtn2.setTag(position);
        listHolder.orderBtn3.setTag(position);
        listHolder.listview.setTag(item);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    class ListHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.shop_name)
        TextView shopName;
        @Bind(R.id.tvStatus)
        TextView tvStatus;
        @Bind(R.id.no_scroll_list_view)
        NoScrollListview listview;
        @Bind(R.id.total_money)
        TextView totalMoney;
        @Bind(R.id.express_money)
        TextView expressMoney;
        @Bind(R.id.rl_total_layout)
        RelativeLayout rlTotalLayout;
        @Bind(R.id.order_btn2)
        Button orderBtn2;
        @Bind(R.id.order_btn1)
        Button orderBtn1;
        @Bind(R.id.item_view)
        LinearLayout itemView;
        @Bind(R.id.buy_flag)
        TextView buyFlag;
        @Bind(R.id.order_btn3)
        Button orderBtn3;
        @Bind(R.id.btn_layout)
        RelativeLayout btnLayout;

        public ListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Order orderItem = (Order) listview.getTag();
                    Intent intent = new Intent(mContext, OrderDetailActivity.class);
                    intent.putExtra("order", orderItem);
                    mContext.startActivity(intent);
                }
            });
        }
    }

}
