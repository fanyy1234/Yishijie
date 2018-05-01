package com.bby.yishijie.member.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bby.yishijie.member.entity.IndexProductBrand;
import com.bumptech.glide.Glide;
import com.sunday.common.utils.DeviceUtils;
import com.bby.yishijie.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 刘涛 on 2017/5/9.
 */

public class CardViewAdapter extends PagerAdapter {

    private List<IndexProductBrand> mData=new ArrayList<>();
    private Context mContext;
    private List<View> mViewList=new ArrayList<>();
    private int appWidth;


    public CardViewAdapter(Context context) {
        this.mContext = context;
        appWidth= DeviceUtils.getDisplay(mContext).widthPixels;
    }

    public void setData(List<IndexProductBrand> data) {
        mData = data;
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view=null;
        if (mViewList.isEmpty()){
            view = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_card_view_item, container, false);
        }else {
            view=mViewList.remove(0);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardItemClickListener != null) {
                    cardItemClickListener.onClick(position);
                }
            }
        });
        container.addView(view);
        IndexProductBrand item = mData.get(position);
        bind(item, view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view= (View) object;
        container.removeView(view);
        mViewList.add(view);
    }

    private void bind(IndexProductBrand item, View view) {
        ImageView imgView = (ImageView) view.findViewById(R.id.item_iv);
        if (!TextUtils.isEmpty(item.getBigImage())) {
            Glide.with(mContext)
                    .load(item.getBigImage())
                    .error(R.mipmap.ic_default)
                    .into(imgView);
        }
        TextView textView= (TextView) view.findViewById(R.id.item_text);
        textView.setText(item.getName());

    }

    private OnCardItemClickListener cardItemClickListener;

    public interface OnCardItemClickListener {
        void onClick(int position);
    }

    public void setOnCardItemClickListener(OnCardItemClickListener cardItemClickListener) {
        this.cardItemClickListener = cardItemClickListener;
    }

}

