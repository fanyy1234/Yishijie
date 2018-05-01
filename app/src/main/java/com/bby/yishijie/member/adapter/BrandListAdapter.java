package com.bby.yishijie.member.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bby.yishijie.member.entity.IndexProductBrand;
import com.bumptech.glide.Glide;
import com.bby.yishijie.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2017/4/26.
 */

public class BrandListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<IndexProductBrand> dataSet;
    private View.OnClickListener onClickListener;

    public BrandListAdapter(Context context, List<IndexProductBrand> datas) {
        mContext = context;
        dataSet = datas;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_product__brand_item, null);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListHolder listHolder= (ListHolder) holder;
        IndexProductBrand item=dataSet.get(position);
        if (!TextUtils.isEmpty(item.getBigImage())){
            Glide.with(mContext)
                    .load(item.getBigImage())
                   .error(R.mipmap.ic_default)
                    .into(listHolder.mainImg);
        }
        if (!TextUtils.isEmpty(item.getImage())){
            Glide.with(mContext)
                    .load(item.getImage())
                    .error(R.mipmap.ic_default)
                    .into(listHolder.brandImg);
        }
        listHolder.brandTitle.setText(String.format("%s",item.getName()));

        listHolder.totalLayout.setOnClickListener(onClickListener);
        listHolder.totalLayout.setTag(item);

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    class ListHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.main_img)
        ImageView mainImg;
        @Bind(R.id.brand_img)
        ImageView brandImg;
        @Bind(R.id.brand_title)
        TextView brandTitle;
        @Bind(R.id.brand_star)
        TextView brandStar;
        @Bind(R.id.btn_buy)
        TextView btnBuy;
        @Bind(R.id.total_layout)
        RelativeLayout totalLayout;
        public ListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
