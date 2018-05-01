package com.bby.yishijie.shop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bby.yishijie.R;
import com.bby.yishijie.shop.entity.AvailableProfit;

import java.math.RoundingMode;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2017/6/27.
 */

public class AvailableProfitAdapter extends RecyclerView.Adapter {


    private Context mContext;
    private List<AvailableProfit> dataSet;
    private View.OnClickListener onClickListener;

    public AvailableProfitAdapter(Context context, List<AvailableProfit> datas) {
        mContext = context;
        dataSet = datas;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_available_profit_item, null);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListHolder listHolder = (ListHolder) holder;
        AvailableProfit item = dataSet.get(position);
       listHolder.scale.setText("+"+item.getScale().setScale(2,RoundingMode.HALF_UP));
        listHolder.orderNo.setText("订单号："+item.getOrderNo());
        listHolder.buyerName.setText("购买人："+item.getBuyerName());
        listHolder.orderTime.setText(item.getCreateTime());
        listHolder.relativeLayout.setTag(item.getOrderId());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @Bind(R.id.scale)
        TextView scale;
        @Bind(R.id.order_no)
        TextView orderNo;
        @Bind(R.id.buyer_name)
        TextView buyerName;
        @Bind(R.id.order_time)
        TextView orderTime;
        @Bind(R.id.relative_layout)
        RelativeLayout relativeLayout;

        public ListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            relativeLayout.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

        }
    }


}
