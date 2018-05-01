package com.bby.yishijie.member.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bby.yishijie.R;
import com.bby.yishijie.member.entity.GroupBuyOrder;

import java.math.RoundingMode;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2017/5/8.
 */

public class GroupBuyOrderAdapter extends RecyclerView.Adapter {


    private Context mContext;
    private List<GroupBuyOrder> dataSet;
    private View.OnClickListener onClickListener;

    public GroupBuyOrderAdapter(Context context, List<GroupBuyOrder> datas) {
        mContext = context;
        dataSet = datas;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.group_order_list_item, null);
        return new ListHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListHolder listHolder = (ListHolder) holder;
        GroupBuyOrder item = dataSet.get(position);
        listHolder.shopName.setText(item.getShopName());
        if (!TextUtils.isEmpty(item.getProductImage())) {
            Glide.with(mContext)
                    .load(item.getProductImage())
                    .error(R.mipmap.ic_default)
                    .into(listHolder.productImage);
        }
        listHolder.productName.setText(item.getProductName());
        listHolder.productParams.setText("规格:" + item.getElements());
        listHolder.groupNum.setText(String.format("%d人团", item.getNum()));
        listHolder.groupPrice.setText(String.format("¥%s", item.getTotalFee().setScale(2, RoundingMode.HALF_UP)));
        switch (item.getTeamStatus()) {
            case 3:
                listHolder.tvStatus.setText("拼团失败");
                listHolder.btnLayout.setVisibility(View.GONE);
                break;
            case 1:
                listHolder.tvStatus.setText("拼团中");
                listHolder.btnLayout.setVisibility(View.GONE);
                break;
            case 2:
                listHolder.tvStatus.setText("拼团成功");
                if (item.getStatus() == 2) {
                    listHolder.btnLayout.setVisibility(View.VISIBLE);
                    listHolder.orderBtn1.setText("确认收货");
                    listHolder.orderBtn2.setVisibility(View.VISIBLE);
                    listHolder.orderBtn2.setText("查看物流");
                    listHolder.orderBtn3.setVisibility(View.VISIBLE);
                    listHolder.orderBtn3.setText("申请退货");
                }else if (item.getStatus()==5){
                    listHolder.btnLayout.setVisibility(View.VISIBLE);
                    listHolder.orderBtn1.setText("退货详情");
                    listHolder.orderBtn2.setVisibility(View.GONE);
                    listHolder.orderBtn3.setVisibility(View.GONE);
                }else{
                    listHolder.btnLayout.setVisibility(View.GONE);
                }
                break;
            default:
                listHolder.tvStatus.setText("拼团失败");
                listHolder.btnLayout.setVisibility(View.GONE);

        }

        listHolder.itemView.setOnClickListener(onClickListener);
        listHolder.orderBtn1.setOnClickListener(onClickListener);
        listHolder.orderBtn2.setOnClickListener(onClickListener);
        listHolder.orderBtn3.setOnClickListener(onClickListener);

        listHolder.itemView.setTag(position);
        listHolder.orderBtn1.setTag(position);
        listHolder.orderBtn2.setTag(position);
        listHolder.orderBtn3.setTag(position);


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
        @Bind(R.id.product_image)
        ImageView productImage;
        @Bind(R.id.product_name)
        TextView productName;
        @Bind(R.id.product_params)
        TextView productParams;
        @Bind(R.id.group_num)
        TextView groupNum;
        @Bind(R.id.group_price)
        TextView groupPrice;
        @Bind(R.id.pro_layout)
        RelativeLayout proLayout;
        @Bind(R.id.order_btn2)
        Button orderBtn2;
        @Bind(R.id.order_btn1)
        Button orderBtn1;
        @Bind(R.id.order_btn3)
        Button orderBtn3;
        @Bind(R.id.item_view)
        LinearLayout itemView;
        @Bind(R.id.btn_layout)
        RelativeLayout btnLayout;

        public ListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
