package com.bby.yishijie.shop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.shop.entity.ProductBrand;
import com.bumptech.glide.Glide;
import com.sunday.common.widgets.swipe.SwipeMenuAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Author by Damon,  on 2017/12/17.
 */

public class BrandSortAdapter extends SwipeMenuAdapter {


    private Context mContext;
    private List<ProductBrand> dataSet;
    private View.OnClickListener onClickListener;
    private long memberId;

    public BrandSortAdapter(Context context, List<ProductBrand> datas) {
        mContext = context;
        dataSet = datas;
        memberId = BaseApp.getInstance().getMember().getId();
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.list_brand_sort_item, parent, false);
    }

    @Override
    public RecyclerView.ViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new ViewHolderItem(realContentView);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderItem productHolder = (ViewHolderItem) holder;
        ProductBrand item = dataSet.get(position);
        Glide.with(mContext)
                .load(item.getImage())
                .error(R.mipmap.ic_default)
                .into(productHolder.productImg);

        productHolder.totalLayout.setTag(item);
        productHolder.totalLayout.setOnClickListener(onClickListener);
        productHolder.btnDel.setTag(position);
        productHolder.btnDel.setOnClickListener(onClickListener);

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    public class ViewHolderItem extends RecyclerView.ViewHolder {
        @Bind(R.id.brand_img)
        ImageView productImg;
        @Bind(R.id.btn_del)
        ImageView btnDel;
        @Bind(R.id.total_layout)
        FrameLayout totalLayout;

        public ViewHolderItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
