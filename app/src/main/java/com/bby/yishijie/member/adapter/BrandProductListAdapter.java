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

import com.bby.yishijie.member.entity.Product;
import com.bby.yishijie.member.ui.product.ProductDetailActivity;
import com.bumptech.glide.Glide;
import com.sunday.common.utils.DeviceUtils;
import com.bby.yishijie.R;

import java.math.RoundingMode;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2017/6/1.
 */

public class BrandProductListAdapter extends RecyclerView.Adapter {


    private Context mContext;
    private List<Product> dataSet;
    private String brandImg;
    private final static int TYPE_HEAD = 0;
    private final static int TYPE_LIST = 1;
    private LayoutInflater layoutInflater;

    public BrandProductListAdapter(Context context, List<Product> datas, String brandImg) {
        this.mContext = context;
        this.dataSet = datas;
        this.brandImg = brandImg;
        layoutInflater=LayoutInflater.from(mContext);
        int appWidth = DeviceUtils.getDisplay(mContext).widthPixels;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEAD:
                View view=layoutInflater.inflate(R.layout.layout_brand_head,null);
                RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(params);
                return new HeadHolder(view);
            case TYPE_LIST:
                view = LayoutInflater.from(mContext).inflate(R.layout.layout_product_list_item, null);
                return new ProductHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEAD:
                HeadHolder headHolder = (HeadHolder) holder;
                if (!TextUtils.isEmpty(brandImg)) {
                    Glide.with(mContext)
                            .load(brandImg)
                            //.resize(appWidth*3/5, appWidth*3/5)
                            .error(R.mipmap.ic_default)
                            .into(headHolder.brandImg);
                    headHolder.brandImg.setVisibility(View.VISIBLE);
                } else {
                    headHolder.brandImg.setVisibility(View.GONE);
                }
                break;
            case TYPE_LIST:
                ProductHolder productHolder = (ProductHolder) holder;
                Product item = dataSet.get(position - 1);
                if (!TextUtils.isEmpty(item.getDetailImage())) {
                    Glide.with(mContext)
                            .load(item.getDetailImage())
                            //.resize(appWidth*4/5, appWidth*4/5)
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
                productHolder.priceNow.setText(String.format("¥%s", item.getCurrentPrice().setScale(2, RoundingMode.HALF_UP)));
                productHolder.priceOld.setText(String.format("¥%s", item.getPrice().setScale(2, RoundingMode.HALF_UP)));
                productHolder.priceOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                productHolder.totalLayout.setTag(item);
                break;
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
        @Bind(R.id.brand_img)
        ImageView brandImg;

        public HeadHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
            ButterKnife.bind(this, itemView);
            totalLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Product item = (Product) v.getTag();
            Intent intent = new Intent(mContext, ProductDetailActivity.class);
            intent.putExtra("type", item.getType());
            intent.putExtra("limitId",item.getLimitId()!=null?item.getLimitId():0);
            intent.putExtra("productId", item.getId());
            mContext.startActivity(intent);

        }
    }
}
