package com.bby.yishijie.member.holder;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.adapter.CommonRecycleAdapter;
import com.bby.yishijie.member.model.IntegralMall;
import com.bby.yishijie.shop.ui.MyIntegralActivity;


/**
 * Created by yq05481 on 2017/1/3.
 */

public class IntegralMallHeaderViewHolder extends BaseRecyleViewHolder<IntegralMall> {
    public IntegralMallHeaderViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setUpView(final IntegralMall model, int position, final CommonRecycleAdapter adapter) {
        final TextView myScore = (TextView) getView(R.id.my_score);
        final View jifenView = getView(R.id.jifen_view);
//        final TextView ageTv = (TextView) getView(R.id.recycler_view_test_item_person_age_tv);
//        final View rootView = getView(R.id.root_view);
//        nameTv.setText(model.getName());
//        ageTv.setText(model.getAge());
        String jifen = model.getJifen();

        myScore.setText(Double.valueOf(jifen).intValue()+"");
        jifenView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(adapter.getmContext(), MyIntegralActivity.class);
                intent.putExtra("flag",1);
                adapter.getmContext().startActivity(intent);
            }
        });
    }
}
