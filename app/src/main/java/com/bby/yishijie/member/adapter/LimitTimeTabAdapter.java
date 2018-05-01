package com.bby.yishijie.member.adapter;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.widgets.RecyclerTabLayout;
import com.bby.yishijie.R;
import com.bby.yishijie.member.entity.LimitTime;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2017/5/15.
 */

public class LimitTimeTabAdapter extends RecyclerTabLayout.Adapter {


    private LimitTimePageAdapter pageAdapter;
    private int appWidth;

    public LimitTimeTabAdapter(ViewPager viewPager) {
        super(viewPager);
        pageAdapter = (LimitTimePageAdapter) viewPager.getAdapter();
        appWidth = DeviceUtils.getDisplay(pageAdapter.getContext()).widthPixels;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_tab_item, parent, false);
        RecyclerTabLayout.LayoutParams params = new RecyclerView.LayoutParams(appWidth / 5
                , RecyclerView.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);

        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LimitTime item = pageAdapter.getLimitTime(position);
        ListHolder listHolder = (ListHolder) holder;
        if (item.getType() == 1) {
            listHolder.timeTitle.setText("昨日");
            listHolder.timeExtra.setText("别错过");
        } else if (item.getType() == 3) {
            listHolder.timeTitle.setText("明日");
            listHolder.timeExtra.setText("预告");
        } else {
            listHolder.timeTitle.setText(item.getStartTime() + ":00");
            listHolder.timeExtra.setText(item.getTime());
        }
        if (position == getCurrentIndicatorPosition()) {
            listHolder.timeTitle.setTextColor(Color.parseColor("#ef0022"));
            listHolder.timeExtra.setTextColor(Color.parseColor("#ef0022"));
        } else {
            listHolder.timeTitle.setTextColor(Color.parseColor("#494949"));
            listHolder.timeExtra.setTextColor(Color.parseColor("#494949"));
        }
    }

    @Override
    public int getItemCount() {
        return pageAdapter.getCount();
    }

    class ListHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.time_title)
        TextView timeTitle;
        @Bind(R.id.time_extra)
        TextView timeExtra;

        public ListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getViewPager().setCurrentItem(getAdapterPosition());
                }
            });
        }
    }
}
