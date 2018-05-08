package com.bby.yishijie.shop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.shop.entity.OrderListBean;
import com.bumptech.glide.Glide;
import com.sunday.common.utils.SpannalbeStringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2017/4/24.
 */

public class OrderProductAdapter extends BaseAdapter {

    private Context mContext;
    private List<OrderListBean> dataSet;
    private int orderType;//0:买 1：卖
    private int type;//0:不允许订单行单独退款 1：允许单独订单行退款
    private View.OnClickListener onClickListener;
    int canRefundNum = 0;
    private boolean isUsedScore;


    public OrderProductAdapter(Context context, List<OrderListBean> datas, int orderType, int type, boolean isUsedScore) {
        mContext = context;
        dataSet = datas;
        this.orderType = orderType;
        this.type=type;
        this.isUsedScore=isUsedScore;
        for (OrderListBean listBean : datas) {
            if (listBean.getStatus() < 4) {
                canRefundNum++;
            }
        }
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.order_product_item_shop, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        OrderListBean item = dataSet.get(position);
        if (!TextUtils.isEmpty(item.getProductImg())) {
            Glide.with(mContext)
                    .load(item.getProductImg())
                    .error(R.mipmap.ic_default)
                    .into(viewHolder.productImage);
        }

        if (item.getType()==2&&item.getIsScore()==1){
            viewHolder.productTag1.setVisibility(View.VISIBLE);
            viewHolder.productTag2.setVisibility(View.VISIBLE);
            viewHolder.productName.setText("\u3000\u3000\u3000\u3000\u3000\u3000\u3000   "+item.getProductName());
        }else if (item.getType()==2&&item.getIsScore()==0){
            viewHolder.productTag1.setVisibility(View.VISIBLE);
            viewHolder.productTag2.setVisibility(View.GONE);
            viewHolder.productName.setText("\u3000\u3000  "+item.getProductName());
        }else if (item.getType()!=2&&item.getIsScore()==1){
            viewHolder.productTag1.setVisibility(View.GONE);
            viewHolder.productTag2.setVisibility(View.VISIBLE);
            viewHolder.productName.setText("\u3000\u3000\u3000\u3000\u3000  "+item.getProductName());
        }else {
            viewHolder.productTag1.setVisibility(View.GONE);
            viewHolder.productTag2.setVisibility(View.GONE);
            viewHolder.productName.setText(item.getProductName());
        }

        viewHolder.productParams.setText("规格" + item.getElements());
        viewHolder.productPrice.setText(String.format("¥%s", item.getPrice().setScale(2, RoundingMode.HALF_UP)));
        viewHolder.productNum.setText("X" + item.getNum());
        if (orderType == 0) {
            viewHolder.extraMoney.setText(SpannalbeStringUtils.setTextColor("省", Color.parseColor("#ff9409")));
            viewHolder.extraMoney.append(SpannalbeStringUtils.setTextColor(String.format("%s", (item.getScale().
                            multiply(BigDecimal.valueOf(item.getNum())).setScale(2,RoundingMode.HALF_UP))),
                    Color.parseColor("#333333")));

            switch (item.getStatus()) {
                case 1:
                    if (canRefundNum > 1) {
                        viewHolder.btnRefund.setVisibility(View.VISIBLE);
                        viewHolder.btnRefund.setText("申请退款");
                    }else {
                        viewHolder.btnRefund.setVisibility(View.GONE);
                    }
                    break;
                case 2:
                    if (canRefundNum > 1) {
                        viewHolder.btnRefund.setVisibility(View.VISIBLE);
                        viewHolder.btnRefund.setText("申请退货");
                    }else {
                        viewHolder.btnRefund.setVisibility(View.GONE);
                    }
                    break;
                case 5:
                    viewHolder.btnRefund.setVisibility(View.VISIBLE);
                    viewHolder.btnRefund.setText("退款中");
                    break;
                case 7:
                    viewHolder.btnRefund.setVisibility(View.VISIBLE);
                    viewHolder.btnRefund.setText("已退款");
                    break;
                case 8:
                    viewHolder.btnRefund.setVisibility(View.VISIBLE);
                    viewHolder.btnRefund.setText("退货中");
                    break;
                case 9:
                    viewHolder.btnRefund.setVisibility(View.VISIBLE);
                    viewHolder.btnRefund.setText("已退货");
                    break;
                default:
                    viewHolder.btnRefund.setVisibility(View.GONE);
            }
            if (dataSet.size() <= 1||type==0||(item.getIsScore()==1&&isUsedScore)) {
                viewHolder.btnRefund.setVisibility(View.GONE);
            }
            viewHolder.btnRefund.setTag(position);
            viewHolder.btnRefund.setOnClickListener(onClickListener);


        } else {
            viewHolder.btnRefund.setVisibility(View.GONE);
            viewHolder.extraMoney.setText(SpannalbeStringUtils.setTextColor("赚", Color.parseColor("#ef0022")));
            viewHolder.extraMoney.append(SpannalbeStringUtils.setTextColor(String.format("%s", item.getScale().setScale(2, RoundingMode.HALF_UP)),
                    Color.parseColor("#333333")));
        }

        return convertView;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    static class ViewHolder {
        @Bind(R.id.product_image)
        ImageView productImage;
        @Bind(R.id.product_name)
        TextView productName;
        @Bind(R.id.product_price)
        TextView productPrice;
        @Bind(R.id.product_params)
        TextView productParams;
        @Bind(R.id.text_sales)
        TextView textSales;
        @Bind(R.id.product_num)
        TextView productNum;
        @Bind(R.id.pro_layout)
        RelativeLayout proLayout;
        @Bind(R.id.extra_money)
        TextView extraMoney;
        @Bind(R.id.product_tag1)
        TextView productTag1;
        @Bind(R.id.product_tag2)
        TextView productTag2;
        @Bind(R.id.btn_refund)
        TextView btnRefund;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
