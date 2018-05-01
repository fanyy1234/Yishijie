package com.bby.yishijie.member.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.widgets.RecyclerTabLayout;
import com.bby.yishijie.R;
import com.bby.yishijie.member.entity.GlobalCat;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Author by Damon,  on 2017/12/14.
 */

public class GlobalTabAdapter extends RecyclerTabLayout.Adapter {

    private int appWidth;
    private Context mContext;
    private List<GlobalCat> dataSet;

    public GlobalTabAdapter(ViewPager viewPager, Context context, List<GlobalCat> datas) {
        super(viewPager);
        this.dataSet = datas;
        this.mContext = context;
        appWidth = DeviceUtils.getDisplay(mContext).widthPixels;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_global_tab, parent, false);
        RecyclerTabLayout.LayoutParams params = new RecyclerView.LayoutParams(appWidth / 5
                , RecyclerView.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GlobalCat item = dataSet.get(position);
        ListHolder listHolder = (ListHolder) holder;
        listHolder.tabTitle.setText(item.getName());
        Glide.with(mContext)
                .load(item.getImg())
                .error(R.mipmap.ic_default)
                .into(listHolder.tabImg);
        if (position == getCurrentIndicatorPosition()) {
            listHolder.llGlobalTab.setBackgroundColor(ContextCompat.getColor(mContext, R.color._f1bcea));
            listHolder.tabTitle.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            Glide.with(mContext)
                    .load(item.getImg1())
                    .error(R.mipmap.ic_default)
                    .into(listHolder.tabImg);
        } else {
            listHolder.llGlobalTab.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
            listHolder.tabTitle.setTextColor(ContextCompat.getColor(mContext, R.color._666));
            Glide.with(mContext)
                    .load(item.getImg())
                    .error(R.mipmap.ic_default)
                    .into(listHolder.tabImg);
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class ListHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tab_img)
        ImageView tabImg;
        @Bind(R.id.tab_title)
        TextView tabTitle;
        @Bind(R.id.ll_global_tab)
        LinearLayout llGlobalTab;

        public ListHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getViewPager().setCurrentItem(getAdapterPosition());
                    itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color._f1bcea));
                    setCurrentIndicatorPosition(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }
    }
}
