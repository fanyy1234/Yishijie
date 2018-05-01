package com.bby.yishijie.member.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.entity.Gift;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2017/4/28.
 */

public class GiftAdapter extends BaseAdapter {

    private Context mContext;
    private List<Gift> dataSet;

    public GiftAdapter(Context context,List<Gift> datas){
        mContext=context;
        dataSet=datas;
    }


    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_spec_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Gift item = dataSet.get(position);
        viewHolder.txtName.setText(item.getName());
        if (item.isSelect()){
            viewHolder.txtName.setBackgroundResource(R.drawable.shape_red_spec);
            viewHolder.txtName.setTextColor(convertView.getResources().getColor(R.color.white));
        }else {
            viewHolder.txtName.setBackgroundResource(R.drawable.shape_unselect_spec);
            viewHolder.txtName.setTextColor(convertView.getResources().getColor(R.color.black_6));
        }

        return convertView;

    }

    static class ViewHolder {
        @Bind(R.id.txtName)
        TextView txtName;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
