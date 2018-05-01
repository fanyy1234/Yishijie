package com.bby.yishijie.member.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.entity.ProductClassify;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2017/5/6.
 */

public class ProductClassifyParentAdapter extends BaseAdapter {

    private List<ProductClassify> dataSet;
    private Context mContext;
    private int selectPosition;

    public ProductClassifyParentAdapter(Context context, List<ProductClassify> datas) {
        mContext = context;
        dataSet = datas;
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
        ViewHolder viewHolder=null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_parent_classify_item, null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        ProductClassify item=dataSet.get(position);
        viewHolder.name.setText(item.getName());
        if (selectPosition==position){
            viewHolder.selectedBg.setVisibility(View.VISIBLE);
            viewHolder.name.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        }else {
            viewHolder.selectedBg.setVisibility(View.GONE);
            viewHolder.name.setTextColor(mContext.getResources().getColor(R.color.black_3));
        }



        return convertView;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }


    static class ViewHolder {
        @Bind(R.id.selected_bg)
        ImageView selectedBg;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.linearlayout)
        LinearLayout linearLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
