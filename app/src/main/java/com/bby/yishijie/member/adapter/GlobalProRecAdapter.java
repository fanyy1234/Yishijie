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
 * Author by Damon,  on 2017/12/19.
 */

public class GlobalProRecAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Product> dataSet;
    private int appWidth;


    public GlobalProRecAdapter(Context context, List<Product> datas) {
        dataSet = datas;
        mContext = context;
        appWidth = DeviceUtils.getDisplay(mContext).widthPixels;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.product_list_grid_item, null);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListHolder listHolder = (ListHolder) holder;
        Product item = dataSet.get(position);
        if (!TextUtils.isEmpty(item.getDetailImage())) {
            Glide.with(mContext)
                    .load(item.getDetailImage())
                    // .resize(appWidth*3/5,appWidth*3/5)
                    .error(R.mipmap.ic_default)
                    .into(listHolder.productImg);
        }
        listHolder.productPrice.setText(String.format("¥%s", item.getCurrentPrice().setScale(2, RoundingMode.HALF_UP)));
        listHolder.productOldPrice.setText(String.format("¥%s", item.getPrice().setScale(2, RoundingMode.HALF_UP)));
        listHolder.productOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        listHolder.productTitle.setText(item.getName());

        listHolder.productLayout.setTag(item);
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
            ButterKnife.bind(this, itemView);
            productLayout.setOnClickListener(this);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) productImg.getLayoutParams();
            params.height = appWidth / 2 - productLayout.getPaddingLeft() - productLayout.getPaddingRight();
            productImg.setLayoutParams(params);

        }

        @Override
        public void onClick(View v) {
            Product item = (Product) v.getTag();
            Intent intent = new Intent(mContext, ProductDetailActivity.class);
            intent.putExtra("type", item.getType());
            intent.putExtra("productId", item.getId());
            mContext.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
