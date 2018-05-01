package com.bby.yishijie.member.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.widgets.banner.holder.Holder;
import com.bby.yishijie.R;

/**
 * Created by 刘涛 on 2017/4/19.
 */

public class StringBannerHolder implements Holder<String> {

    private Context mContext;
    private int width ;
    private ImageView imageView;
   // private View.OnClickListener onClickListener;
    @Override
    public View createView(Context context) {
        this.mContext = context;
        width = DeviceUtils.getDisplay(mContext).widthPixels;
        imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        Glide.with(context)
                .load(data)
                .placeholder(R.mipmap.ic_default)
                .error(R.mipmap.ic_default)
                .into(imageView);
      //  imageView.setTag(position);
        //imageView.setOnClickListener(onClickListener);
    }

//    public void setOnClickListener(View.OnClickListener onClickListener) {
//        this.onClickListener = onClickListener;
//    }
}
