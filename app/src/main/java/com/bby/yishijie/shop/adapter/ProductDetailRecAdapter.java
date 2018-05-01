package com.bby.yishijie.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.ui.product.ProductDetailsActivity;
import com.bby.yishijie.shop.entity.Product;
import com.bumptech.glide.Glide;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.utils.PixUtils;

import java.math.RoundingMode;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by 刘涛 on 2017/4/24.
 * <p>
 * 产品详情推荐商品适配
 */

public class ProductDetailRecAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Product> dataSet;

    private final static int TYPE_HEAD = 0;
    private final static int TYPE_LIST = 1;
    private int appWidth;

    public ProductDetailRecAdapter(Context context, List<Product> datas) {
        dataSet = datas;
        mContext = context;
        appWidth= DeviceUtils.getDisplay(mContext).widthPixels;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEAD:
                View view = LayoutInflater.from(mContext).inflate(R.layout.pro_detail_list_head, null);
                RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, PixUtils.dip2px(mContext,45));
                view.setLayoutParams(layoutParams);
                return new HeadHolder(view);
            case TYPE_LIST:
                view = LayoutInflater.from(mContext).inflate(R.layout.product_list_grid_item, null);
                return new ListHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
       if (position==0){
           HeadHolder headHolder= (HeadHolder) holder;
           if (dataSet.size()==0){
           headHolder.headLayout.setVisibility(View.GONE);}
           else {
               headHolder.headLayout.setVisibility(View.VISIBLE);
           }
       }else {
           ListHolder listHolder= (ListHolder) holder;
           Product item=dataSet.get(position-1);
           if (!TextUtils.isEmpty(item.getDetailImage())){
               Glide.with(mContext)
                       .load(item.getDetailImage())
                       .error(R.mipmap.ic_default)
                       .into(listHolder.productImg);
           }
           listHolder.productPrice.setText(String.format("¥%s",item.getCurrentPrice().setScale(2, RoundingMode.HALF_UP)));
           listHolder.productOldPrice.setText(String.format("%s",item.getScale().setScale(2,RoundingMode.HALF_UP)));
           listHolder.productTitle.setText(item.getName());

           listHolder.productLayout.setTag(item);

       }
    }

    @Override
    public int getItemCount() {
        return dataSet.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEAD;
        } else {
            return TYPE_LIST;
        }
    }

    class HeadHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.header_layout)
        LinearLayout headLayout;
        public HeadHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.product_img)
        ImageView productImg;
        @Bind(R.id.product_price)
        TextView productPrice;
        @Bind(R.id.product_old_price)
        TextView productOldPrice;
        @Bind(R.id.product_title)
        TextView productTitle;
        @Bind(R.id.product_layout)
        RelativeLayout productLayout;
        public ListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            productLayout.setOnClickListener(this);
            RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) productImg.getLayoutParams();
            params.height=appWidth/2-productLayout.getPaddingLeft()-productLayout.getPaddingRight();
            productImg.setLayoutParams(params);
        }

        @Override
        public void onClick(View v) {
            Product item = (Product) v.getTag();
            Intent intent = new Intent(mContext, ProductDetailsActivity.class);
            intent.putExtra("productName",item.getName());
            intent.putExtra("type", item.getType());
            intent.putExtra("productId", item.getId());
            intent.putExtra("productImg",item.getDetailImage());
            intent.putExtra("scale",String.format("%s", item.getScale().setScale(2, RoundingMode.HALF_UP)));
            intent.putExtra("productPrice",String.format("%s", item.getCurrentPrice().setScale(2, RoundingMode.HALF_UP)));
            mContext.startActivity(intent);
        }
    }
}
