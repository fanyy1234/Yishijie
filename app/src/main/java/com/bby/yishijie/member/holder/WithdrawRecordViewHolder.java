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
import com.bby.yishijie.member.model.WithdrawRecord;
import com.bby.yishijie.member.ui.product.ProductDetailActivity;


/**
 * Created by yq05481 on 2017/1/3.
 */

public class WithdrawRecordViewHolder extends BaseRecyleViewHolder<WithdrawRecord> {
    public WithdrawRecordViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setUpView(final WithdrawRecord model, int position,final CommonRecycleAdapter adapter) {
        final View rootView = getView(R.id.rootView);
        final TextView time = (TextView) getView(R.id.time);
        final TextView money = (TextView) getView(R.id.money);

        time.setText(model.getTime());
        money.setText(model.getMoney());
    }
}
