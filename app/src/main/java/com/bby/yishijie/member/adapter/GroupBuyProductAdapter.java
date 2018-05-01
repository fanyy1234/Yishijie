package com.bby.yishijie.member.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sunday.common.utils.DeviceUtils;
import com.bby.yishijie.R;
import com.bby.yishijie.member.entity.Product;
import com.bby.yishijie.member.ui.product.ProductDetailActivity;

import java.math.RoundingMode;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2017/5/8.
 */

public class GroupBuyProductAdapter extends RecyclerView.Adapter {


    private Context mContext;
    private List<Product> dataSet;
    private int appWidth;

    public GroupBuyProductAdapter(Context context, List<Product> datas) {
        this.mContext = context;
        this.dataSet = datas;
        appWidth= DeviceUtils.getDisplay(mContext).widthPixels;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_group_buy_item, null);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListHolder listHolder= (ListHolder) holder;
        Product item=dataSet.get(position);
        if (!TextUtils.isEmpty(item.getBigImage())){
            Glide.with(mContext)
                    .load(item.getBigImage())
                    .error(R.mipmap.ic_default)
                    .into(listHolder.mainImg);
        }
        listHolder.productTitle.setText(item.getName());
        listHolder.groupNum.setText(String.format("%d人团",item.getNum()));
        listHolder.priceNow.setText(String.format("¥%s",item.getCurrentPrice().setScale(2, RoundingMode.HALF_UP)));
        listHolder.priceOld.setText(String.format("单买价￥%s",item.getPrice().setScale(2, RoundingMode.HALF_UP)));
        listHolder.priceOld.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);

        listHolder.totalLayout.setTag(item);


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.main_img)
        ImageView mainImg;
        @Bind(R.id.product_title)
        TextView productTitle;
        @Bind(R.id.group_num)
        TextView groupNum;
        @Bind(R.id.price_now)
        TextView priceNow;
        @Bind(R.id.price_old)
        TextView priceOld;
        @Bind(R.id.btn_buy)
        TextView btnBuy;
        @Bind(R.id.total_layout)
        RelativeLayout totalLayout;
        public ListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            totalLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Product item= (Product) v.getTag();
            Intent intent=new Intent(mContext, ProductDetailActivity.class);
            intent.putExtra("type",3);
            intent.putExtra("productId",item.getId());
            mContext.startActivity(intent);
        }
    }
}
