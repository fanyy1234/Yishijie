package com.bby.yishijie.member.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.entity.FindItem;
import com.bby.yishijie.member.ui.base.ShowBigImage;
import com.bby.yishijie.member.ui.find.FindItenDetailActivity;
import com.bumptech.glide.Glide;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.widgets.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2017/4/17.
 */

public class FindListAdapter extends RecyclerView.Adapter {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    private View mHeaderView;

    private Context mContext;
    private List<FindItem> dataSet;
    private int appWidth;

    public FindListAdapter(Context context, List<FindItem> datas) {
        mContext = context;
        dataSet = datas;
        appWidth = DeviceUtils.getDisplay(mContext).widthPixels;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mHeaderView != null && viewType == TYPE_HEADER) return new Holder(mHeaderView);
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_find_item, null);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_HEADER) return;
        ListHolder listHolder = (ListHolder) holder;
        int pos = getRealPosition(holder);


        FindItem item = dataSet.get(pos);
        if (!TextUtils.isEmpty(item.getImage())) {
            Glide.with(mContext)
                    .load(item.getImage())
                    .error(R.mipmap.ic_default)
                    .into(listHolder.circleImg);
        }


        listHolder.title.setText(item.getName());
        listHolder.time.setText(item.getCreateTime());
        listHolder.contentTitle.setText(item.getTitle());
        listHolder.content.setText(item.getSummary());

        ArrayList<ImageInfo> imageInfo = new ArrayList<>();
        List<String> imageDetails = item.getImages();
        if (imageDetails != null) {
            for (String imageDetail : imageDetails) {
                ImageInfo info = new ImageInfo();
                info.setThumbnailUrl(imageDetail);
                info.setBigImageUrl(imageDetail);
                imageInfo.add(info);
            }
        }
        listHolder.imgGridView.setAdapter(new NineGridViewClickAdapter(mContext, imageInfo));
        listHolder.imgGridView.setSingleImageSize(appWidth);
        listHolder.imgGridView.setSingleImageRatio(2);


        if (item.getStatus() == 0) {
            listHolder.titleFrom.setText("平台");
            listHolder.titleFrom.setBackgroundResource(R.drawable.shape_blue_stroke);
            listHolder.titleFrom.setTextColor(Color.parseColor("#399efb"));
        } else {
            listHolder.titleFrom.setText("用户精选");
            listHolder.titleFrom.setBackgroundResource(R.drawable.shape_red_stroke);
            listHolder.titleFrom.setTextColor(Color.parseColor("#ef0022"));
        }
        listHolder.totalLayout.setTag(item);

    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? dataSet.size():dataSet.size()+1;
    }


    class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.circle_img)
        CircleImageView circleImg;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.title_from)
        TextView titleFrom;
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.layout_head)
        RelativeLayout layoutHead;
        @Bind(R.id.content_title)
        TextView contentTitle;
        @Bind(R.id.content)
        TextView content;
        @Bind(R.id.total_layout)
        RelativeLayout totalLayout;
        @Bind(R.id.img_grid_view)
        NineGridView imgGridView;


        public ListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            totalLayout.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            FindItem item = (FindItem) v.getTag();
            Intent intent = new Intent(mContext, FindItenDetailActivity.class);
            intent.putExtra("findItem", item);
            mContext.startActivity(intent);
        }
    }

    class ImageAdapter extends RecyclerView.Adapter {

        private List<String> dataSet = new ArrayList<>();

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.list_find_img_item, null);
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(appWidth * 5 / 6, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(layoutParams);
            return new ImgHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ImgHolder imgHolder = (ImgHolder) holder;
            if (!TextUtils.isEmpty(dataSet.get(position))) {
                Glide.with(mContext)
                        .load(dataSet.get(position))
                        .placeholder(R.mipmap.ic_default)
                        .error(R.mipmap.ic_default)
                        .into(imgHolder.imageView);
            }
            imgHolder.imageView.setTag(R.id.image, dataSet.get(position));
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
            LinearLayout layout;

            public ImgHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.image);
                layout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
                imageView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                String img = (String) v.getTag(R.id.image);
                Intent intent = new Intent(mContext, ShowBigImage.class);
                intent.putStringArrayListExtra("data", (ArrayList<String>) dataSet);
                mContext.startActivity(intent);
            }
        }
    }



    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }
    @Override
    public int getItemViewType(int position) {
        if(mHeaderView == null) return TYPE_NORMAL;
        if(position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }
    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    class Holder extends RecyclerView.ViewHolder {

        TextView text;

        public Holder(View itemView) {
            super(itemView);
            if(itemView == mHeaderView) return;
//            text = (TextView) itemView.findViewById(R.id.text);
        }
    }
}
