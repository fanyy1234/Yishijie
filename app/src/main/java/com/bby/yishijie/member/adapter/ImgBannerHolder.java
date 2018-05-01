package com.bby.yishijie.member.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.widgets.banner.holder.Holder;
import com.bby.yishijie.R;
import com.bby.yishijie.member.entity.CatImage;

/**
 * Created by 刘涛 on 2017/6/1.
 */

public class ImgBannerHolder implements Holder<CatImage> {

    private ImageView imageView;
    private View.OnClickListener onClickListener;

    @Override
    public View createView(Context context) {
        Context mContext = context;
        int width = DeviceUtils.getDisplay(mContext).widthPixels;
        imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, CatImage data) {
        if (!TextUtils.isEmpty(data.getImage())) {
            Glide.with(context)
                    .load(data.getImage())
                    .placeholder(R.mipmap.ic_default)
                    .error(R.mipmap.ic_default)
                    .into(imageView);
            imageView.setTag(data);
            imageView.setOnClickListener(onClickListener);
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
