package com.bby.yishijie.member.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bby.yishijie.R;
import com.bby.yishijie.member.entity.CartItem;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2017/4/21.
 */

public class OrderConfirmProAdapter extends BaseAdapter {

    private Context mContext;
    private List<CartItem> dataSet=new ArrayList<>();
    private int appwidth;

    public OrderConfirmProAdapter(Context context, List<CartItem> datas) {
        mContext = context;
        dataSet = datas;
        appwidth = mContext.getResources().getDisplayMetrics().widthPixels;
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
        ViewHolder listHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_order_confirm_product, null);
            listHolder = new ViewHolder(convertView);
            convertView.setTag(listHolder);
        } else {
            listHolder = (ViewHolder) convertView.getTag();
        }
        CartItem item=dataSet.get(position);
        if (!TextUtils.isEmpty(item.getImage())){
            Glide.with(mContext)
                    .load(item.getImage())
                    .placeholder(R.mipmap.ic_default)
                    .error(R.mipmap.ic_default)
                    .into(listHolder.productImg);
        }
        listHolder.productNum.setText("X"+item.getNum());
        listHolder.productTitle.setText(item.getProductName());
        listHolder.productSpec.setText("规格:"+item.getElements());
        listHolder.productPrice.setText(String.format("¥%s",item.getPrice().setScale(2, RoundingMode.HALF_UP)));


        return convertView;
    }



    static class ViewHolder {
        @Bind(R.id.product_img)
        ImageView productImg;
        @Bind(R.id.product_title)
        TextView productTitle;
        @Bind(R.id.product_spec)
        TextView productSpec;
        @Bind(R.id.product_price)
        TextView productPrice;
        @Bind(R.id.product_num)
        TextView productNum;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
