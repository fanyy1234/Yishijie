package com.bby.yishijie.member.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bby.yishijie.member.entity.ProductClassify;
import com.bumptech.glide.Glide;
import com.bby.yishijie.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.widget.ImageView.ScaleType.CENTER_INSIDE;
import static android.widget.ImageView.ScaleType.FIT_XY;

/**
 * Created by 刘涛 on 2017/9/13.
 */

public class CategoryAdapter extends BaseAdapter {

    private List<ProductClassify> dataSet;
    private Context mContext;

    public CategoryAdapter(Context context, List<ProductClassify> datas) {
        dataSet = datas;
        mContext = context;
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_product_category_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ProductClassify item = dataSet.get(position);
        if (item.getId()==0){
            viewHolder.categoryImg.setImageResource(R.mipmap.more);
            viewHolder.categoryImg.setScaleType(CENTER_INSIDE);
        }else {
            if (!TextUtils.isEmpty(item.getImage())) {
                Glide.with(mContext)
                        .load(item.getImage())
                        .error(R.mipmap.ic_default)
                        .into(viewHolder.categoryImg);
                viewHolder.categoryImg.setScaleType(FIT_XY);
            }
        }
        viewHolder.categoryName.setText(item.getName());
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.category_img)
        ImageView categoryImg;
        @Bind(R.id.category_name)
        TextView categoryName;
        @Bind(R.id.linearlayout)
        LinearLayout layout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

}

