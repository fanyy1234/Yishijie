package com.bby.yishijie.member.holder;

import android.view.View;

import com.bby.yishijie.member.adapter.CommonRecycleAdapter;
import com.bby.yishijie.member.model.IntegralMall;


/**
 * Created by yq05481 on 2017/1/3.
 */

public class IntegralMallHeaderViewHolder extends BaseRecyleViewHolder<IntegralMall> {
    public IntegralMallHeaderViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setUpView(final IntegralMall model, int position, CommonRecycleAdapter adapter) {
//        final TextView nameTv = (TextView) getView(R.id.recycler_view_test_item_person_name_tv);
//        final TextView ageTv = (TextView) getView(R.id.recycler_view_test_item_person_age_tv);
//        final View rootView = getView(R.id.root_view);
//        nameTv.setText(model.getName());
//        ageTv.setText(model.getAge());
    }
}
