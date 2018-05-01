package com.bby.yishijie.shop.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.shop.adapter.CommonAdapter;
import com.bby.yishijie.shop.adapter.ViewHolder;
import com.bby.yishijie.shop.entity.FansProfit;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.utils.SpannalbeStringUtils;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;

import java.math.RoundingMode;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by 刘涛 on 2017/5/11.
 *
 * 客户总收入
 */

public class CustomerProfitActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.rightTxt)
    TextView rightTxt;
    @Bind(R.id.customer_total_profit)
    TextView customerTotalProfit;
    @Bind(R.id.customer_total_num)
    TextView customerTotalNum;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private FansProfit fansProfit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profit);
        ButterKnife.bind(this);
        fansProfit= (FansProfit) getIntent().getSerializableExtra("fansProfit");
        initView();
        initRecyclerview();
    }

    private void initView(){
        rightTxt.setVisibility(View.GONE);
        titleView.setText("客户总收入");
        if (fansProfit==null){return;}
        customerTotalProfit.setText(SpannalbeStringUtils.setTextColor("客户总收入：",getResources().getColor(R.color.black_3)));
        customerTotalProfit.append(SpannalbeStringUtils.setTextColor(String.format("¥%s",fansProfit.getTotal().setScale(2,RoundingMode.HALF_UP)),
                getResources().getColor(R.color.colorPrimary)));
        customerTotalNum.setText(String.format("客户人数: %d人",fansProfit.getCount()));
    }

    private void initRecyclerview(){
        if (fansProfit==null){return;}
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext).
                drawable(R.drawable.shape_divider).build());
        CommonAdapter adapter = new CommonAdapter<FansProfit.Fans>(mContext, R.layout.customer_profit_item, fansProfit.getList()) {
            @Override
            public void convert(ViewHolder holder, FansProfit.Fans fans) {
                ((TextView) holder.getView(R.id.name)).setText(fans.getName());
                ((TextView) holder.getView(R.id.identify)).setText(fans.getLevelName());
                ((TextView) holder.getView(R.id.profit)).setText(String.format("¥%s", fans.getTotal().setScale(2, RoundingMode.HALF_UP)));
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
