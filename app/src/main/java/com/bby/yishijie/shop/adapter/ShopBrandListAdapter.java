package com.bby.yishijie.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.shop.entity.ProductBrand;
import com.bby.yishijie.shop.ui.BrandProductListShopActivity;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2017/5/10.
 */

public class ShopBrandListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<ProductBrand> dataSet;
    private View.OnClickListener onClickListener;


    public ShopBrandListAdapter(Context context) {
        this.mContext = context;
    }

    public ShopBrandListAdapter(Context context, List<ProductBrand> datas) {
        this.mContext = context;
        this.dataSet = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_brand_list_item, null);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ProductHolder productHolder = (ProductHolder) holder;
        ProductBrand item = dataSet.get(position);
        if (!TextUtils.isEmpty(item.getImage())) {
            Glide.with(mContext)
                    .load(item.getImage())
                    .error(R.mipmap.ic_default)
                    .into(productHolder.productImg);
        }
        productHolder.productTitle.setText(item.getName());
        productHolder.btnAdd.setImageResource(item.getStatus() == 0 ? R.mipmap.product_add : R.mipmap.sub_pro);

        productHolder.totalLayout.setTag(item);
        productHolder.btnAdd.setOnClickListener(onClickListener);
        productHolder.btnShare.setOnClickListener(onClickListener);
        productHolder.btnAdd.setTag(position);
        productHolder.btnShare.setTag(item);

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    class ProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.product_img)
        ImageView productImg;
        @Bind(R.id.product_title)
        TextView productTitle;
        @Bind(R.id.btn_share)
        ImageView btnShare;
        @Bind(R.id.btn_add_product)
        ImageView btnAdd;
        @Bind(R.id.total_layout)
        RelativeLayout totalLayout;
        public ProductHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            totalLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ProductBrand productBrand = (ProductBrand) v.getTag();
            Intent intent = new Intent(mContext, BrandProductListShopActivity.class);
            //intent.putExtra("type",1);
            intent.putExtra("brandName", String.format("%s",productBrand.getName()));
            intent.putExtra("brandId",productBrand.getId());
            mContext.startActivity(intent);
        }
    }
}
