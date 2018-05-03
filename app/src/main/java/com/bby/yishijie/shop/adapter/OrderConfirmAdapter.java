package com.bby.yishijie.shop.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sunday.common.widgets.NoScrollListview;
import com.bby.yishijie.R;
import com.bby.yishijie.member.entity.CartItem;
import com.bby.yishijie.shop.entity.CartListItem;

import java.math.RoundingMode;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Author by Damon,  on 2018/1/22.
 */

public class OrderConfirmAdapter extends BaseAdapter {

    private List<CartListItem> dataSet;
    private Context mContext;

    public OrderConfirmAdapter(Context context, List<CartListItem> datas) {
        this.dataSet = datas;
        this.mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder listHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_order_cartlist, null);
            listHolder = new ViewHolder(convertView);
            convertView.setTag(listHolder);
        } else {
            listHolder = (ViewHolder) convertView.getTag();
        }
        CartListItem item = dataSet.get(position);
        listHolder.listTitle.setText(item.getTypeName());
        if (!item.getCartItemList().isEmpty()) {
            OrderConfirmProAdapter adapter = new OrderConfirmProAdapter(mContext, item.getCartItemList());
            listHolder.listView.setAdapter(adapter);
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
