package com.bby.yishijie.member.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sunday.common.widgets.NoScrollListview;
import com.bby.yishijie.R;
import com.bby.yishijie.member.entity.CartListItem;
import com.bby.yishijie.member.entity.Order;
import com.bby.yishijie.member.entity.OrderListItem;
import com.bby.yishijie.member.ui.order.ApplyRefundActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Author by Damon,  on 2018/1/22.
 */

public class OrdetItemListAdapter extends BaseAdapter {


    private List<OrderListItem> dataSet;
    private Context mContext;
    private boolean isUsedVoucher;//是否用了优惠券
    private View.OnClickListener onClickListener;
    private Order order;

    public OrdetItemListAdapter(Context context, List<OrderListItem> datas, boolean isUsedVoucher,Order order) {
        this.dataSet = datas;
        this.mContext = context;
        this.isUsedVoucher = isUsedVoucher;
        this.order=order;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder listHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_order_cartlist, null);
            listHolder = new ViewHolder(convertView);
            convertView.setTag(listHolder);
        } else {
            listHolder = (ViewHolder) convertView.getTag();
        }
        final OrderListItem item = dataSet.get(position);
        listHolder.listTitle.setText(item.getTypeName());
        int type = 0;//区分订单行是否显示退款按钮
        if (isUsedVoucher || item.getType() == 2) {
            type = 0;
        } else {
            type = 1;
        }
        if (!item.getOrderListBeanList().isEmpty()) {
            OrderProductAdapter adapter = new OrderProductAdapter(mContext, item.getOrderListBeanList(), type);
            listHolder.listView.setAdapter(adapter);
            adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int p = (int) v.getTag();
                    switch (item.getOrderListBeanList().get(p).getStatus()) {
                        case 1:
                            //申请退款
                            Intent intent = new Intent(mContext, ApplyRefundActivity.class);
                            intent.putExtra("order", order);
                            intent.putExtra("parentP",position);
                            intent.putExtra("position", p);
                            intent.putExtra("type", 1);//申请退款
                            mContext.startActivity(intent);
                            break;
                        case 2:
                            //申请退货
                            intent = new Intent(mContext, ApplyRefundActivity.class);
                            intent.putExtra("order", order);
                            intent.putExtra("parentP",position);
                            intent.putExtra("type", 2);//申请退货
                            intent.putExtra("position", p);
                            mContext.startActivity(intent);
                            break;
                    }
                }
            });
            adapter.notifyDataSetChanged();
        }
        // listHolder.adapter.setParentType(item.getType());
        return convertView;
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

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    static class ViewHolder {
        @Bind(R.id.list_title)
        TextView listTitle;
        @Bind(R.id.list_view)
        NoScrollListview listView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
