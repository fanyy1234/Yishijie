package com.bby.yishijie.member.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.entity.Voucher;

import java.math.RoundingMode;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by 刘涛 on 2017/4/27.
 */

public class VoucherListAdapter extends RecyclerView.Adapter {


    private Context mContext;
    private List<Voucher> dataSet;
    private View.OnClickListener onClickListener;

    public VoucherListAdapter(Context context, List<Voucher> datas) {
        mContext = context;
        dataSet = datas;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_voucher_item, null);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListHolder listHolder = (ListHolder) holder;
        Voucher item = dataSet.get(position);
        if (item.getStatus() == 1) {
            //已使用
        } else if (item.getStatus() == 2) {
            //已过期
            listHolder.relativeLayout.setBackgroundResource(R.mipmap.profile_coupon_bg_useless);
            listHolder.txt1.setTextColor(Color.parseColor("#808080"));
            listHolder.voucherMoney.setTextColor(Color.parseColor("#666666"));
            listHolder.voucherExtra.setTextColor(Color.parseColor("#666666"));
            listHolder.txtUse.setTextColor(Color.parseColor("#999999"));
            listHolder.txtUse.setText("已过期");
            listHolder.voucherFlag.setTextColor(ContextCompat.getColor(mContext,R.color._666));
            listHolder.voucherFlag.setBackgroundResource(R.drawable.shape_radius_gray);
        } else {
            listHolder.relativeLayout.setBackgroundResource(R.mipmap.profile_coupon_bg);
            listHolder.txt1.setTextColor(Color.parseColor("#000000"));
            listHolder.voucherMoney.setTextColor(Color.parseColor("#000000"));
            listHolder.voucherExtra.setTextColor(Color.parseColor("#333333"));
            listHolder.txtUse.setTextColor(Color.parseColor("#ffffff"));
            listHolder.txtUse.setText("立即\n使用");
            listHolder.voucherFlag.setTextColor(ContextCompat.getColor(mContext,R.color.main_color));
            listHolder.voucherFlag.setBackgroundResource(R.drawable.shape_red_stroke);

        }
        listHolder.voucherFlag.setText(item.getUseType()==1?"折扣券":"邮费券");

        //listHolder.voucherName.setText(item.getName());
        listHolder.voucherMoney.setText(String.format("%s", item.getMoney().setScale(0, RoundingMode.HALF_UP)));
        listHolder.voucherExtra.setText(String.format("满%s元使用", item.getAmount().setScale(0, RoundingMode.HALF_UP)));
        listHolder.voucherTime.setText(String.format("%1$s至%2$s", item.getStartDate(), item.getEndDate()));
        listHolder.relativeLayout.setOnClickListener(onClickListener);
        listHolder.relativeLayout.setTag(item);

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    class ListHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.txt_1)
        TextView txt1;
        @Bind(R.id.voucher_money)
        TextView voucherMoney;
        @Bind(R.id.voucher_time)
        TextView voucherTime;
        @Bind(R.id.voucher_extra)
        TextView voucherExtra;
        @Bind(R.id.txt_use)
        TextView txtUse;
        @Bind(R.id.relative_layout)
        RelativeLayout relativeLayout;
        @Bind(R.id.voucher_flag)
        TextView voucherFlag;

        public ListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
