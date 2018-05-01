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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sunday.common.widgets.NoScrollGridView;
import com.sunday.common.widgets.banner.ConvenientBanner;
import com.sunday.common.widgets.banner.holder.ViewHolderCreator;
import com.bby.yishijie.R;
import com.bby.yishijie.member.entity.CatImage;
import com.bby.yishijie.member.entity.Product;
import com.bby.yishijie.member.entity.ProductClassify;
import com.bby.yishijie.member.ui.product.AllProductListActivity;
import com.bby.yishijie.member.ui.product.ProductDetailActivity;
import com.bby.yishijie.member.ui.product.ProductListActivity;

import java.math.RoundingMode;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2017/6/1.
 */

public class IndexProductListAdapter extends RecyclerView.Adapter {


    private Context mContext;
    private List<Product> dataSet;
    private List<ProductClassify> productClassifyList;
    private final static int TYPE_HEAD = 0;
    private final static int TYPE_LIST = 1;
    private LayoutInflater inflater;
    CategoryAdapter categoryAdapter;
    private View.OnClickListener onClickListener;
    private String parentName;
    private long parentId;

    public IndexProductListAdapter(Context context, List<Product> datas,
                                   List<ProductClassify> productClassifies,String parentName, long parentId) {
        this.mContext = context;
        this.dataSet = datas;
        this.productClassifyList = productClassifies;
        this.parentName=parentName;
        this.parentId=parentId;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEAD:
                View view = inflater.inflate(R.layout.index_product_category_list, null);
                RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                //params.setMargins(0,0,0,3);
                view.setLayoutParams(params);
                return new ProductCategortyHolder(view);
            case TYPE_LIST:
                view = inflater.inflate(R.layout.layout_product_list_item, null);
                return new ProductHolder(view);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEAD:
                ProductCategortyHolder productCategortyHolder = (ProductCategortyHolder) holder;
                if (productClassifyList != null && productClassifyList.size() > 0) {
                    ((ProductCategortyHolder) holder).gridView.setVisibility(View.VISIBLE);
                    categoryAdapter = new CategoryAdapter(productClassifyList);
                    productCategortyHolder.gridView.setAdapter(categoryAdapter);
                    categoryAdapter.notifyDataSetChanged();
                    productCategortyHolder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ProductClassify productBrand = (ProductClassify) parent.getAdapter().getItem(position);
                                Intent intent = new Intent(mContext, AllProductListActivity.class);
                                intent.putExtra("parentName",parentName);
                                intent.putExtra("parentId",parentId);
                                intent.putExtra("catId", productBrand.getId());
                                mContext.startActivity(intent);
                        }
                    });
                } else {
                    ((ProductCategortyHolder) holder).gridView.setVisibility(View.GONE);
                }
                break;
            case TYPE_LIST:
                ProductHolder productHolder = (ProductHolder) holder;
                Product item = dataSet.get(position - 1);
                if (!TextUtils.isEmpty(item.getDetailImage())) {
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

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    class ProductCategortyHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.banner)
        ConvenientBanner banner;
        @Bind(R.id.grid_view)
        NoScrollGridView gridView;

        private ImgBannerHolder bannerViewHolder;
        private ViewHolderCreator viewHolderCreator;

        public ProductCategortyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            banner.startTurning(3000);
            bannerViewHolder = new ImgBannerHolder();
            viewHolderCreator = new ViewHolderCreator<ImgBannerHolder>() {

                @Override
                public ImgBannerHolder createHolder() {
                    return bannerViewHolder;
                }
            };
            bannerViewHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //产品详情
                    CatImage item = (CatImage) v.getTag();
                    if (item.getRefId() > 0) {
                        Intent intent = new Intent(mContext, ProductDetailActivity.class);
                        intent.putExtra("type", 1);
                        intent.putExtra("productId", item.getRefId());
                        mContext.startActivity(intent);
                    }
                }
            });
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
            intent.putExtra("productId", item.getId());
            mContext.startActivity(intent);

        }
    }

    class CategoryAdapter extends BaseAdapter {

        private List<ProductClassify> dataSet;

        public CategoryAdapter(List<ProductClassify> datas) {
            dataSet = datas;
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
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_product_category_item, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ProductClassify item = dataSet.get(position);
            if (!TextUtils.isEmpty(item.getImage())) {
                Glide.with(mContext)
                        .load(item.getImage())
                        .error(R.mipmap.ic_default)
                        .into(viewHolder.categoryImg);
            }
            if (item.getId() == 0) {
                viewHolder.categoryImg.setImageResource(R.mipmap.index_classify_more);
            }
            viewHolder.categoryName.setText(item.getName());
            return convertView;
        }

        class ViewHolder {
            @Bind(R.id.category_img)
            ImageView categoryImg;
            @Bind(R.id.category_name)
            TextView categoryName;
            @Bind(R.id.linearlayout)
            LinearLayout layout;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }

        }
    }
}
