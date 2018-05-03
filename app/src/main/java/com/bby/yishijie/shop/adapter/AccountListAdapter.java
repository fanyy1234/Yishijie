package com.bby.yishijie.shop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.shop.entity.Account;
import com.sunday.common.widgets.swipe.SwipeMenuAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2017/5/12.
 */

public class AccountListAdapter extends SwipeMenuAdapter {


    private Context mContext;
    private List<Account> dataSet;
    private View.OnClickListener listener;


    public AccountListAdapter(Context context, List<Account> datas) {
        mContext = context;
        dataSet = datas;
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.list_account_item, parent, false);
    }

    @Override
    public RecyclerView.ViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new ViewHolderItem(realContentView);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderItem listHolder = (ViewHolderItem) holder;
        Account item = dataSet.get(position);
        listHolder.bankLogo.setImageResource(item.getBankName().equals("支付宝")?R.mipmap.alipay:R.mipmap.bank);
        listHolder.bankName.setText(item.getBankName());
        listHolder.bankNo.setText(item.getAccCode());
        listHolder.viewItem.setTag(item);
        listHolder.viewItem.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }



    public class ViewHolderItem extends RecyclerView.ViewHolder {

        @Bind(R.id.bank_logo)
        ImageView bankLogo;
        @Bind(R.id.bank_name)
        TextView bankName;
        @Bind(R.id.bank_no)
        TextView bankNo;
        @Bind(R.id.viewItem)
        RelativeLayout viewItem;

        public ViewHolderItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}