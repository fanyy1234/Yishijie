package com.bby.yishijie.member.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bby.yishijie.R;
import com.bby.yishijie.member.entity.Product;
import com.bby.yishijie.member.ui.product.ProductDetailActivity;

import java.math.RoundingMode;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2017/4/12.
 */

public class ProductListAdapter extends RecyclerView.Adapter {


    private Context mContext;
    private List<Product> dataSet;


    public ProductListAdapter(Context context) {
        this.mContext = context;
    }
    public ProductListAdapter(Context context,List<Product> datas) {
        this.mContext = context;
        this.dataSet=datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_product_list_item, null);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ProductHolder productHolder= (ProductHolder) holder;
        Product item=dataSet.get(position);
        if (!TextUtils.isEmpty(item.getDetailImage())){
            Glide.with(mContext)
                    .load(item.getDetailImage())
                    .error(R.mipmap.ic_default)
                    .into(productHolder.productImg);
        }
        if (item.getType() == 2) {
            productHolder.productTag1.setVisibility(View.VISIBLE);
            productHolder.productTitle.setText("\u3000\u3000  "+item.getName());
        } else {
            productHolder.productTag1.setVisibility(View.GONE);
            productHolder.productTitle.setText(item.getName());
        }
        productHolder.priceNow.setText(String.format("¥%s",item.getCurrentPrice().setScale(2,RoundingMode.HALF_UP)));
        productHolder.priceOld.setText(String.format("¥%s",item.getPrice().setScale(2, RoundingMode.HALF_UP)));
        productHolder.priceOld.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);

        productHolder.totalLayout.setTag(item);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }



    class ProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @Bind(R.id.product_img)
        ImageView productImg;
        @Bind(R.id.product_title)
        TextView productTitle;
        @Bind(R.id.price_now)
        TextView priceNow;
        @Bind(R.id.price_old)
        TextView priceOld;
        @Bind(R.id.btn_buy)
        TextView btnBuy;
        @Bind(R.id.total_layout)
        RelativeLayout totalLayout;
        @Bind(R.id.product_tag1)
        TextView productTag1;

        public ProductHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            totalLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Product item= (Product) v.getTag();
            Intent intent=new Intent(mContext, ProductDetailActivity.class);
            intent.putExtra("type",item.getType());
            intent.putExtra("limitId",item.getLimitId()!=null?item.getLimitId():0);
            intent.putExtra("productId",item.getId());
            mContext.startActivity(intent);

        }
    }
}
