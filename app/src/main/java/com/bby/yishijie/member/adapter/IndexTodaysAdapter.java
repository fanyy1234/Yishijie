package com.bby.yishijie.member.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sunday.common.adapter.CommenAdapter;
import com.sunday.common.utils.DeviceUtils;
import com.bby.yishijie.R;
import com.bby.yishijie.member.entity.IndexProductBrand;
import com.bby.yishijie.member.entity.Product;
import com.bby.yishijie.member.entity.ProductBrand;
import com.bby.yishijie.member.ui.product.BrandProductListActivity;
import com.bby.yishijie.member.ui.product.ProductDetailActivity;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2017/5/1.
 */

public class IndexTodaysAdapter extends RecyclerView.Adapter {


    private Context mContext;
    private final static int TYPE_LIST1 = 2;
    private final static int TYPE_LIST2 = 3;
    private List<IndexProductBrand> brandList;
    private List<Product> productList;
    private long timeLimitId;
    private int appWidth;

    public IndexTodaysAdapter(Context context, List<IndexProductBrand> brands, List<Product> products) {
        mContext = context;
        brandList = brands;
        productList = products;
        appWidth = DeviceUtils.getDisplay(mContext).widthPixels;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_LIST1:
                View view = LayoutInflater.from(mContext).inflate(R.layout.index_product_brand_item, null);
                RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(layoutParams);
                return new ListHolder1(view);
            case TYPE_LIST2:
                view = LayoutInflater.from(mContext).inflate(R.layout.layout_product_item, null);
                return new ListHolder2(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_LIST1:
                ListHolder1 listHolder1 = (ListHolder1) holder;
                IndexProductBrand item = brandList.get(position);
                if (!TextUtils.isEmpty(item.getBigImage())) {
                    Glide.with(mContext)
                            .load(item.getBigImage())
                            .error(R.mipmap.ic_default)
                            .into(listHolder1.brandImg);
                }
                if (item.getProductList() != null) {
                    listHolder1.adapter.setDataSet(item.getProductList());
                    listHolder1.adapter.notifyDataSetChanged();
                }
                listHolder1.brandImg.setTag(R.id.brand_img, item);
                break;
            case TYPE_LIST2:
                ListHolder2 productHolder = (ListHolder2) holder;
                Product item1 = productList.get(position - brandList.size());
                if (!TextUtils.isEmpty(item1.getBigImage())) {
                    Glide.with(mContext)
                            .load(item1.getBigImage())
                            .error(R.mipmap.ic_default)
                            .into(productHolder.mainImg);
                }
                productHolder.productTitle.setText("\u3000\u3000 "+item1.getName());
                productHolder.priceNow.setText(String.format("¥%s", item1.getCurrentPrice().setScale(2, RoundingMode.HALF_UP)));
                productHolder.priceOld.setText(String.format("¥%s", item1.getPrice().setScale(2, RoundingMode.HALF_UP)));
                productHolder.priceOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                productHolder.totalLayout.setTag(item1);


                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (brandList.size() > 0) {
            if (position < brandList.size()) {
                return TYPE_LIST1;
            } else {
                return TYPE_LIST2;
            }
        } else return TYPE_LIST2;
    }

    @Override
    public int getItemCount() {
        return brandList.size() + productList.size();
    }

    public void setTimeLimitId(long timeLimitId) {
        this.timeLimitId = timeLimitId;
    }


    class ListHolder1 extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.brand_img)
        ImageView brandImg;
        @Bind(R.id.brand_product_list_view)
        RecyclerView brandProductListView;

        LinearLayoutManager layoutManager;
        private IndexBrandProductAdapter adapter;

        public ListHolder1(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            brandProductListView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            brandProductListView.setLayoutManager(layoutManager);
           adapter=new IndexBrandProductAdapter(mContext);
            brandProductListView.setAdapter(adapter);
            brandImg.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            IndexProductBrand item = (IndexProductBrand) v.getTag(R.id.brand_img);
            Intent intent = new Intent(mContext, BrandProductListActivity.class);
            intent.putExtra("brandName", String.format("%s", item.getName()));
            intent.putExtra("brandId", item.getId());
            intent.putExtra("brandImg", item.getBigImage());
            intent.putExtra("type",item.getType());
            mContext.startActivity(intent);
        }
    }

    class ListHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.main_img)
        ImageView mainImg;
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

        public ListHolder2(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            totalLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Product item = (Product) v.getTag();
            Intent intent = new Intent(mContext, ProductDetailActivity.class);
            intent.putExtra("type", item.getType());
            intent.putExtra("productId", item.getId());
            intent.putExtra("limitId", timeLimitId);
            mContext.startActivity(intent);
        }
    }
}
