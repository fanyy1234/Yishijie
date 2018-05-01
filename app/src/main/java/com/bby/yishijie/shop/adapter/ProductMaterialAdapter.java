package com.bby.yishijie.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.ui.base.ShowBigImage;
import com.bby.yishijie.shop.entity.ProductMaterial;
import com.bumptech.glide.Glide;
import com.sunday.common.utils.DeviceUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2017/5/5.
 */

public class ProductMaterialAdapter extends RecyclerView.Adapter {



    private List<ProductMaterial> dataSet;
    private Context mContext;
    private View.OnClickListener onClickListener;
    private int type;
    private int appWidth;

    public ProductMaterialAdapter(Context context, List<ProductMaterial> datas, int type) {
        mContext = context;
        dataSet = datas;
        this.type=type;
        appWidth= DeviceUtils.getDisplay(mContext).widthPixels;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_material_item, null);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListHolder listHolder= (ListHolder) holder;
        ProductMaterial item=dataSet.get(position);
        listHolder.content.setText(item.getText());
        if (item.getImages()!=null&&item.getImages().size()>0){
            listHolder.adapter.setDataSet(item.getImages());
            listHolder.adapter.notifyDataSetChanged();
        }
        if (type==0){
            listHolder.btnShare.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.share,0,0,0);
            listHolder.btnShare.setText("分享");
        }else {
            listHolder.btnShare.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.delete,0,0,0);
            listHolder.btnShare.setText("删除");
        }

        listHolder.btnSave.setTag(item);
        listHolder.btnCopy.setTag(item);
        listHolder.btnShare.setTag(item);

        listHolder.btnSave.setOnClickListener(onClickListener);
        listHolder.btnCopy.setOnClickListener(onClickListener);
        listHolder.btnShare.setOnClickListener(onClickListener);

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    class ListHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.content)
        TextView content;
        @Bind(R.id.recycler_view)
        RecyclerView recyclerView;
        @Bind(R.id.btn_save)
        TextView btnSave;
        @Bind(R.id.btn_copy)
        TextView btnCopy;
        @Bind(R.id.btn_share)
        TextView btnShare;
        private LinearLayoutManager linearLayoutManager;
        private ImageAdapter adapter;
        public ListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            linearLayoutManager=new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            adapter=new ImageAdapter();
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setAdapter(adapter);
        }
    }

    class ImageAdapter extends RecyclerView.Adapter {

        private List<String> dataSet=new ArrayList<>();

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(mContext).inflate(R.layout.layout_image,null);
            return new ImgHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ImgHolder imgHolder= (ImgHolder) holder;
            if (!TextUtils.isEmpty(dataSet.get(position))){
                Glide.with(mContext)
                        .load(dataSet.get(position))
                        .placeholder(R.mipmap.ic_default)
                        .error(R.mipmap.ic_default)
                        .into(imgHolder.imageView);
            }
            imgHolder.imageView.setTag(R.id.image,dataSet.get(position));
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }

        public void setDataSet(List<String> dataSet) {
            this.dataSet = dataSet;
        }
        class ImgHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView imageView;
            public ImgHolder(View itemView) {
                super(itemView);
                imageView= (ImageView) itemView.findViewById(R.id.image);
                imageView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                String img= (String) v.getTag(R.id.image);
                Intent intent = new Intent(mContext, ShowBigImage.class);
                intent.putStringArrayListExtra("data", (ArrayList<String>) dataSet);
                mContext.startActivity(intent);
            }
        }
    }
}
