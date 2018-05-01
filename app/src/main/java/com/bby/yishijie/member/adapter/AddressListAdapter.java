package com.bby.yishijie.member.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bby.yishijie.R;
import com.bby.yishijie.member.entity.Address;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2017/4/21.
 */

public class AddressListAdapter extends RecyclerView.Adapter {


    private Context mContext;
    private List<Address> dataSet;
    private View.OnClickListener onClickListener;


    public AddressListAdapter(Context context, List<Address> datas) {
        mContext = context;
        dataSet = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_address_item, null);
        return new ListHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListHolder listHolder= (ListHolder) holder;
        Address item=dataSet.get(position);
        listHolder.name.setText(item.getName());
        listHolder.phoneNumber.setText(item.getMobile());
        listHolder.address.setText(String.format("%1s %2s",item.getCityDetail(),item.getAddress()));
        if (item.getIsDeleted()==2){
            listHolder.choice.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.addr_select,0,0,0);
        }else{
            listHolder.choice.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_select_no,0,0,0);
        }

        listHolder.edite.setOnClickListener(onClickListener);
        listHolder.delete.setOnClickListener(onClickListener);
        listHolder.choice.setOnClickListener(onClickListener);
        listHolder.addressLayout.setOnClickListener(onClickListener);
        listHolder.edite.setTag(position);
        listHolder.delete.setTag(position);
        listHolder.choice.setTag(position);
        listHolder.addressLayout.setTag(item);

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    class ListHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.phone_number)
        TextView phoneNumber;
        @Bind(R.id.address)
        TextView address;
        @Bind(R.id.address_layout)
        RelativeLayout addressLayout;
        @Bind(R.id.choice)
        TextView choice;
        @Bind(R.id.edite)
        TextView edite;
        @Bind(R.id.delete)
        TextView delete;
        @Bind(R.id.default_layout)
        RelativeLayout defaultLayout;
        public ListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
