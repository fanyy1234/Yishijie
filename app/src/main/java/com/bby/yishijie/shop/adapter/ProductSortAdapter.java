package com.bby.yishijie.shop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.product.ProductDetailsActivity;
import com.bby.yishijie.shop.entity.Product;
import com.bumptech.glide.Glide;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.swipe.SwipeMenuAdapter;

import java.math.RoundingMode;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Author by Damon,  on 2017/12/17.
 */

public class ProductSortAdapter extends SwipeMenuAdapter {

    private Context mContext;
    private List<Product> dataSet;
    private View.OnClickListener listener;
    private long memberId;

    public ProductSortAdapter(Context context, List<Product> datas) {
        mContext = context;
        dataSet = datas;
        memberId = BaseApp.getInstance().getMember().getId();
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.list_product_sort_item, parent, false);
    }

    @Override
    public RecyclerView.ViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new ViewHolderItem(realContentView);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderItem productHolder = (ViewHolderItem) holder;
        Product item = dataSet.get(position);
        if (!TextUtils.isEmpty(item.getDetailImage())) {
            Glide.with(mContext)
                    .load(item.getDetailImage())
                    .error(R.mipmap.ic_default)
                    .into(productHolder.productImg);
        }
        if (item.getType() == 2 && item.getIsScore() == 1) {
            productHolder.productTag1.setVisibility(View.VISIBLE);
            productHolder.productTag2.setVisibility(View.VISIBLE);
            productHolder.productTitle.setText("\u3000\u3000\u3000\u3000\u3000\u3000\u3000     " + item.getName());
        } else if (item.getType() == 2 && item.getIsScore() == 0) {
            productHolder.productTag1.setVisibility(View.VISIBLE);
            productHolder.productTag2.setVisibility(View.GONE);
            productHolder.productTitle.setText("\u3000\u3000  " + item.getName());
        } else if (item.getType() != 2 && item.getIsScore() == 1) {
            productHolder.productTag1.setVisibility(View.GONE);
            productHolder.productTag2.setVisibility(View.VISIBLE);
            productHolder.productTitle.setText("\u3000\u3000\u3000\u3000\u3000   " + item.getName());
        } else {
            productHolder.productTag1.setVisibility(View.GONE);
            productHolder.productTag2.setVisibility(View.GONE);
            productHolder.productTitle.setText(item.getName());
        }
        productHolder.priceNow.setText(String.format("¥%s", item.getCurrentPrice().setScale(2, RoundingMode.HALF_UP)));
        productHolder.priceOld.setText(String.format("%s", item.getScale().setScale(2, RoundingMode.HALF_UP)));
        productHolder.storeNum.setText(String.format("库存%d", item.getProductStore()));
        productHolder.totalLayout.setTag(item);
        productHolder.btnDel.setTag(position);

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }


    public class ViewHolderItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.product_img)
        ImageView productImg;
        @Bind(R.id.product_tag1)
        TextView productTag1;
        @Bind(R.id.product_tag2)
        TextView productTag2;
        @Bind(R.id.product_title)
        TextView productTitle;
        @Bind(R.id.product_title_layout)
        RelativeLayout productTitleLayout;
        @Bind(R.id.price_now)
        TextView priceNow;
        @Bind(R.id.price_tag)
        TextView priceTag;
        @Bind(R.id.price_old)
        TextView priceOld;
        @Bind(R.id.store_num)
        TextView storeNum;
        @Bind(R.id.total_layout)
        RelativeLayout totalLayout;
        @Bind(R.id.btn_del)
        ImageView btnDel;

        public ViewHolderItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            totalLayout.setOnClickListener(this);
            btnDel.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.total_layout:
                    Product item = (Product) v.getTag();
                    Intent intent = new Intent(mContext, ProductDetailsActivity.class);
                    intent.putExtra("productName", item.getName());
                    intent.putExtra("type", item.getType());
                    intent.putExtra("limitId", item.getLimitId() != null ? item.getLimitId() : 0);
                    intent.putExtra("productId", item.getId());
                    intent.putExtra("productImg", item.getDetailImage());
                    intent.putExtra("scale", String.format("%s", item.getScale().setScale(2, RoundingMode.HALF_UP)));
                    mContext.startActivity(intent);
                    break;
                case R.id.btn_del:
                    int p = (int) v.getTag();
                    manageProduct(p);
                    break;
            }
        }
    }

    private void manageProduct(final int p) {
        Product product = dataSet.get(p);
        final int type = product.getStatus() == 0 ? 1 : 2;
        Call<ResultDO<Integer>> call = ApiClient.getApiAdapter().addPro(memberId, type, product.getId(), null);
        call.enqueue(new Callback<ResultDO<Integer>>() {
            @Override
            public void onResponse(Call<ResultDO<Integer>> call, Response<ResultDO<Integer>> response) {

                if (response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    ToastUtils.showToast(mContext, type == 1 ? "上架成功" + "\n已上架产品数：" + response.body().getResult() : "下架成功" + "\n已上架产品数：" + response.body().getResult());
                    dataSet.remove(p);
                    notifyItemRemoved(p);
                    if(p != dataSet.size()){
                        notifyItemRangeChanged(p, dataSet.size() - p);
                    }
                } else {
                    ToastUtils.showToast(mContext, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Integer>> call, Throwable t) {

            }
        });
    }
}