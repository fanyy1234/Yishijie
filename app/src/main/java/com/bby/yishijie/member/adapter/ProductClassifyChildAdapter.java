package com.bby.yishijie.member.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bby.yishijie.R;
import com.bby.yishijie.member.entity.ProductClassify;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2017/5/15.
 */

public class ProductClassifyChildAdapter extends BaseAdapter {


    private List<ProductClassify> dataSet;
    private Context mContext;

    public ProductClassifyChildAdapter(Context context, List<ProductClassify> datas) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_product_category_item, null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        ProductClassify item=dataSet.get(position);
        viewHolder.name.setText(item.getName());
        if (!TextUtils.isEmpty(item.getImage())){
            Glide.with(mContext)
                    .load(item.getImage())
                    .error(R.mipmap.ic_default)
                    .into(viewHolder.img);
        }
        return convertView;
    }



    static class ViewHolder {
        @Bind(R.id.category_img)
        ImageView img;
        @Bind(R.id.category_name)
        TextView name;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
