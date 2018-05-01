package com.bby.yishijie.member.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bby.yishijie.R;
import com.bby.yishijie.member.entity.Order;
import com.bby.yishijie.member.entity.OrderListBean;

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
    private int type;//0:不允许订单行单独退款 1：允许单独订单行退款
    private View.OnClickListener onClickListener;
    int canRefundNum = 0;

    public OrderProductAdapter(Context context, List<OrderListBean> datas, int type) {
        mContext = context;
        dataSet = datas;
        this.type = type;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.order_product_item, null);
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

        if (dataSet.size() <= 1 || type == 0) {
            viewHolder.btnRefund.setVisibility(View.GONE);
        }
        //viewHolder.productName.setText(item.getProductName());
        viewHolder.productParams.setText(item.getElements());
        viewHolder.productPrice.setText(String.format("¥%s", item.getPrice().setScale(2, RoundingMode.HALF_UP)));
        viewHolder.productNum.setText("X" + item.getNum());
        viewHolder.productNameFlag.setVisibility(item.getType() == 2 ? View.VISIBLE : View.GONE);
        viewHolder.productName.setText(item.getType() == 2 ? "\t\t\t\t\t" + item.getProductName() : item.getProductName());
        viewHolder.btnRefund.setTag(position);
        viewHolder.btnRefund.setOnClickListener(onClickListener);

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
        @Bind(R.id.product_num)
        TextView productNum;
        @Bind(R.id.pro_layout)
        RelativeLayout proLayout;
        @Bind(R.id.product_name_flag)
        TextView productNameFlag;
        @Bind(R.id.btn_refund)
        TextView btnRefund;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
