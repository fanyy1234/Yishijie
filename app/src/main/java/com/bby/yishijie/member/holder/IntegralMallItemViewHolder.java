package com.bby.yishijie.member.holder;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.adapter.CommonRecycleAdapter;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.model.IntegralMallItem;
import com.bby.yishijie.member.ui.product.ProductDetailActivity;
import com.bumptech.glide.Glide;


/**
 * Created by yq05481 on 2017/1/3.
 */

public class IntegralMallItemViewHolder extends BaseRecyleViewHolder<IntegralMallItem> {
    public IntegralMallItemViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setUpView(final IntegralMallItem model, int position,final CommonRecycleAdapter adapter) {
        final View rootView = getView(R.id.rootView);
        final TextView name = (TextView) getView(R.id.name);
        final TextView score = (TextView) getView(R.id.score);
        final TextView price = (TextView) getView(R.id.price);
        final ImageView img = (ImageView) getView(R.id.img);

        ViewGroup.LayoutParams layoutParams = img.getLayoutParams();
        layoutParams.width = (BaseApp.screenWidth-40)/2;
        layoutParams.height = (BaseApp.screenWidth-40)/2;
        img.setLayoutParams(layoutParams);

        name.setText(model.getName());
        score.setText(model.getScore()+"积分");
        price.setText(model.getPrice());
        Glide.with(adapter.getmContext()).load(model.getImg()).into(img);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(adapter.getmContext(), ProductDetailActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("productId", model.getId());
                intent.putExtra("integralFlag",1);
                adapter.getmContext().startActivity(intent);
            }
        });

    }
}
