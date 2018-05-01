package com.bby.yishijie.member.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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

import static com.bby.yishijie.R.id.product_params;
import static com.bby.yishijie.R.id.view;

/**
 * Created by 刘涛 on 2017/9/20.
 */

public class IndexBrandProductAdapter extends RecyclerView.Adapter {


    private List<Product> dataSet;
    private Context mContext;

    public IndexBrandProductAdapter(Context context) {

        this.mContext = context;
    }

    public void setDataSet(List<Product> datas){
        this.dataSet=datas;
    }

    public IndexBrandProductAdapter(Context context, List<Product> datas) {
        this.dataSet = datas;
        this.mContext = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_index_brand_product, null);
        RecyclerView.LayoutParams layoutParams= new RecyclerView.LayoutParams(DeviceUtils.getDisplay(mContext).widthPixels*2/7, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Product product = dataSet.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        Glide.with(mContext)
                .load(product.getDetailImage())
                .error(R.mipmap.ic_default)
                .into(viewHolder.productImg);
        viewHolder.productFlag.setVisibility(product.getType() == 2 ? View.VISIBLE : View.GONE);
        viewHolder.productPrice.setText(String.format("¥%s", product.getCurrentPrice().setScale(2, RoundingMode.HALF_UP)));
        viewHolder.productOldPrice.setText(String.format("¥%s", product.getPrice().setScale(2, RoundingMode.HALF_UP)));
        viewHolder.productOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        viewHolder.productLayout.setTag(product);

    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.product_img)
        ImageView productImg;
        @Bind(R.id.product_flag)
        TextView productFlag;
        @Bind(R.id.frame_layout)
        FrameLayout frameLayout;
        @Bind(R.id.product_price)
        TextView productPrice;
        @Bind(R.id.product_old_price)
        TextView productOldPrice;
        @Bind(R.id.product_layout)
        RelativeLayout productLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            productLayout.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Product item = (Product) v.getTag();
            Intent intent = new Intent(mContext, ProductDetailActivity.class);
            intent.putExtra("type", item.getType());
            intent.putExtra("productId", item.getId());
            //intent.putExtra("limitId", timeLimitId);
            mContext.startActivity(intent);
        }
    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
