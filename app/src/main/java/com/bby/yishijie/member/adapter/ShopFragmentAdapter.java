package com.bby.yishijie.member.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.utils.PixUtils;

import com.bby.yishijie.R;
import com.bby.yishijie.member.entity.IndexProductBrand;
import com.bby.yishijie.member.entity.Product;
import com.bby.yishijie.member.entity.ProductBrand;
import com.bby.yishijie.member.ui.product.ProductDetailActivity;
import com.bby.yishijie.member.ui.product.BrandProductListActivity;

import java.math.RoundingMode;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by 刘涛 on 2017/4/17.
 */

public class ShopFragmentAdapter extends RecyclerView.Adapter {

    private final static int TYPE_HEAD = 0;
    private final static int TYPE_LIST = 1;


    private Context mContext;
    private List<IndexProductBrand> brandList;
    private List<Product> productList;
    private int appWidth;

    public ShopFragmentAdapter(Context context) {
        mContext = context;
    }

    public ShopFragmentAdapter(Context context, List<IndexProductBrand> brands, List<Product> products) {
        mContext = context;
        brandList = brands;
        productList = products;
        appWidth= DeviceUtils.getDisplay(mContext).widthPixels;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEAD:
                View view = LayoutInflater.from(mContext).inflate(R.layout.layout_img_gallery, null);
                return new HeaderHolder(view);
            case TYPE_LIST:
                view = LayoutInflater.from(mContext).inflate(R.layout.layout_shop_fragment_item, null);
                return new ListHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEAD:
                final HeaderHolder headerHolder = (HeaderHolder) holder;
                if (brandList != null && brandList.size() > 0) {
                    headerHolder.adapter.setData(brandList);
                    headerHolder.adapter.notifyDataSetChanged();
                }
                break;
            case TYPE_LIST:
                ListHolder listHolder = (ListHolder) holder;
                Product item = productList.get(position - 1);
                if (position == 1) {
                    listHolder.linearlayout.setVisibility(View.VISIBLE);
                } else {
                    listHolder.linearlayout.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(item.getBigImage())) {
                    Glide.with(mContext)
                            .load(item.getBigImage())
                            .error(R.mipmap.ic_default)
                            .into(listHolder.productImg);
                }
                if (item.getType() == 2) {
                    String str="特卖"+" "+item.getName();
                    SpannableStringBuilder style=new SpannableStringBuilder(str);
                    style.setSpan(new BackgroundColorSpan(ContextCompat.getColor(mContext,R.color.colorPrimary)),0,2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    style.setSpan(new ForegroundColorSpan(Color.WHITE),0,2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    listHolder.productTitle.setText(style);
                } else {
                    listHolder.productTitle.setText(item.getName());
                }
                listHolder.productPrice.setText(String.format("¥%s", item.getCurrentPrice().setScale(2, RoundingMode.HALF_UP)));
                listHolder.productOldPrice.setText(String.format("¥%s", item.getPrice().setScale(2, RoundingMode.HALF_UP)));
                listHolder.productOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

                listHolder.totalLayout.setTag(item);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return productList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEAD;
        } else {
            return TYPE_LIST;
        }
    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.view_pager)
        ViewPager viewPager;

        private CardViewAdapter adapter;

        public HeaderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            adapter = new CardViewAdapter(mContext);
            viewPager.setClipToPadding(false);
            viewPager.setAdapter(adapter);
            viewPager.setPadding(0,0, PixUtils.dip2px(mContext,40),0);
            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if (position==adapter.getCount()-1){
                        viewPager.setPadding( PixUtils.dip2px(mContext,40),0,0,0);
                    }else {
                        viewPager.setPadding(0,0, PixUtils.dip2px(mContext,40),0);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            adapter.setOnCardItemClickListener(new CardViewAdapter.OnCardItemClickListener() {
                @Override
                public void onClick(int position) {
                    IndexProductBrand productBrand=brandList.get(position);
                    Intent intent=new Intent(mContext, BrandProductListActivity.class);
                    intent.putExtra("type",1);
                    intent.putExtra("brandName", String.format("%s",productBrand.getName()));
                    intent.putExtra("brandId",productBrand.getId());
                    mContext.startActivity(intent);
                }
            });

        }
    }

    class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.linearlayout)
        LinearLayout linearlayout;
        @Bind(R.id.product_img)
        ImageView productImg;
        @Bind(R.id.product_title)
        TextView productTitle;
        @Bind(R.id.product_price)
        TextView productPrice;
        @Bind(R.id.product_old_price)
        TextView productOldPrice;
        @Bind(R.id.btn_buy)
        TextView btnBuy;
        @Bind(R.id.total_layout)
        LinearLayout totalLayout;


        public ListHolder(View itemView) {
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
            mContext.startActivity(intent);
        }
    }
}
