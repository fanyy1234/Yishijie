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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.product.BrandProductListActivity;
import com.bby.yishijie.member.ui.product.ProductDetailsActivity;
import com.bby.yishijie.shop.entity.IndexProductBrand;
import com.bby.yishijie.shop.entity.Product;
import com.bby.yishijie.shop.widgets.ShareWindow;
import com.bumptech.glide.Glide;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.utils.ToastUtils;

import java.math.RoundingMode;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 刘涛 on 2017/5/1.
 */

public class IndexTodaysAdapter extends RecyclerView.Adapter {


    private Context mContext;
    private final static int TYPE_LIST1 = 2;
    private final static int TYPE_LIST2 = 3;
    private List<IndexProductBrand> brandList;
    private List<Product> productList;
    private long timeLimitId;
    private long memberId;
    private int appWidth;

    public IndexTodaysAdapter(Context context, List<IndexProductBrand> brands, List<Product> products) {
        mContext = context;
        brandList = brands;
        productList = products;
        if (BaseApp.getInstance().getShopMember()==null){
            memberId = 0;
        }
        else {
            memberId = BaseApp.getInstance().getShopMember().getId();
        }
        appWidth = DeviceUtils.getDisplay(mContext).widthPixels;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_LIST1:
                View view = LayoutInflater.from(mContext).inflate(R.layout.index_product_brand_item, null);
                RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(layoutParams);
                return new ListHolder1(view);
            case TYPE_LIST2:
                view = LayoutInflater.from(mContext).inflate(R.layout.layout_product_item_shop, null);
                return new ListHolder2(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_LIST1:
                ListHolder1 listHolder1 = (ListHolder1) holder;
                IndexProductBrand item = brandList.get(position);
                if (!TextUtils.isEmpty(item.getBigImage())) {
                    Glide.with(mContext)
                            .load(item.getBigImage())
                            .error(R.mipmap.ic_default)
                            .into(listHolder1.brandImg);
                }
                if (item.getProductList() != null) {
                    listHolder1.adapter.setDataSet(item.getProductList());
                    listHolder1.adapter.notifyDataSetChanged();
                }
                listHolder1.brandImg.setTag(R.id.brand_img, item);
                break;
            case TYPE_LIST2:
                ListHolder2 productHolder = (ListHolder2) holder;
                Product item1 = productList.get(position - brandList.size());
                if (!TextUtils.isEmpty(item1.getBigImage())) {
                    Glide.with(mContext)
                            .load(item1.getBigImage())
                            .error(R.mipmap.ic_default)
                            .into(productHolder.mainImg);
                }
                productHolder.productTag1.setVisibility(View.VISIBLE);
                if (item1.getIsScore()==1){
                    productHolder.productTag2.setVisibility(View.VISIBLE);
                    productHolder.productTitle.setText(String.format("\u3000\u3000\u3000\u3000\u3000\u3000\u3000  %s", item1.getName()));
                }else if (item1.getIsScore()==0){
                    productHolder.productTag2.setVisibility(View.GONE);
                    productHolder.productTitle.setText(String.format("\u3000\u3000 %s", item1.getName()));
                }
                productHolder.priceNow.setText(String.format("¥%s", item1.getCurrentPrice().setScale(2, RoundingMode.HALF_UP)));
                productHolder.priceOld.setText(String.format("%s", item1.getScale().setScale(2, RoundingMode.HALF_UP)));
                productHolder.storeNum.setText(String.format("库存%d", item1.getProductStore()));
                productHolder.btnAdd.setImageResource(item1.getStatus() == 0 ? R.mipmap.product_add : R.mipmap.sub_pro);
                productHolder.totalLayout.setTag(item1);
                productHolder.btnAdd.setTag(position - brandList.size());
                productHolder.btnShare.setTag(item1);
                productHolder.btnCheckMaterial.setTag(item1);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (brandList.size() > 0) {
            if (position < brandList.size()) {
                return TYPE_LIST1;
            } else {
                return TYPE_LIST2;
            }
        } else return TYPE_LIST2;
    }

    @Override
    public int getItemCount() {
        return brandList.size() + productList.size();
    }

    public void setTimeLimitId(long timeLimitId) {
        this.timeLimitId = timeLimitId;
    }


    class ListHolder1 extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.brand_img)
        ImageView brandImg;
        @Bind(R.id.brand_product_list_view)
        RecyclerView brandProductListView;

        LinearLayoutManager layoutManager;
        private IndexBrandProductAdapter adapter;

        public ListHolder1(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            brandProductListView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            brandProductListView.setLayoutManager(layoutManager);
            adapter = new IndexBrandProductAdapter(mContext);
            brandProductListView.setAdapter(adapter);
            brandImg.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            IndexProductBrand item = (IndexProductBrand) v.getTag(R.id.brand_img);
            Intent intent = new Intent(mContext, BrandProductListActivity.class);
            intent.putExtra("brandName", String.format("%s", item.getName()));
            intent.putExtra("brandId", item.getId());
            intent.putExtra("brandImg", item.getBigImage());
            intent.putExtra("type", item.getType());
            mContext.startActivity(intent);
        }

    }


    class ListHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.main_img)
        ImageView mainImg;
        @Bind(R.id.product_title)
        TextView productTitle;
        @Bind(R.id.price_now)
        TextView priceNow;
        @Bind(R.id.price_old)
        TextView priceOld;
        @Bind(R.id.btn_share)
        ImageView btnShare;
        @Bind(R.id.btn_add_product)
        ImageView btnAdd;
        @Bind(R.id.btn_check_material)
        ImageView btnCheckMaterial;
        @Bind(R.id.store_num)
        TextView storeNum;
        @Bind(R.id.total_layout)
        RelativeLayout totalLayout;
        @Bind(R.id.product_tag1)
        TextView productTag1;
        @Bind(R.id.product_tag2)
        TextView productTag2;

        public ListHolder2(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            totalLayout.setOnClickListener(this);
            btnAdd.setOnClickListener(this);
            btnShare.setOnClickListener(this);
            btnCheckMaterial.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.total_layout:
                    Product item = (Product) v.getTag();
                    Intent intent = new Intent(mContext, ProductDetailsActivity.class);
                    intent.putExtra("productName", item.getName());
                    intent.putExtra("type", item.getType());
                    intent.putExtra("productId", item.getId());
                    intent.putExtra("productImg", item.getDetailImage());
                    intent.putExtra("scale", String.format("%s", item.getScale().setScale(2, RoundingMode.HALF_UP)));
                    intent.putExtra("limitId", timeLimitId);
                    intent.putExtra("productPrice", String.format("%s", item.getCurrentPrice().setScale(2, RoundingMode.HALF_UP)));
                    mContext.startActivity(intent);
                    break;
                case R.id.btn_add_product:
                    if (memberId==0){
                        ToastUtils.showToast(mContext,"请先登录");
                        return;
                    }
                    int p = (int) v.getTag();
                    manageProduct(p);
                    break;
                case R.id.btn_share:
                    /*1我要开店2分享店铺3普通商品4限时购商品)-memberId(会员Id)-productId(商品Id)-limitId(限时购时间段Id)
                    这四个参数没有值的传0
                    例如普通商品分享：http://weixin.zj-yunti.com/authorizationPage.html?param=3-1-14-0*/
                    if (memberId==0){
                        ToastUtils.showToast(mContext,"请先登录");
                        return;
                    }
                    Product item2 = (Product) v.getTag();
                    String shareUrl = String.format("%1$s%2$d-%3$d-%4$d-%5$d", ApiClient.SHARE_URL, 4, memberId, item2.getId(), timeLimitId);
                    ShareWindow shareWindow = new ShareWindow(mContext, shareUrl, item2.getName(), item2.getDetailImage(),
                            String.format("%s", item2.getScale().setScale(2, RoundingMode.HALF_UP)),
                            mContext.getResources().getString(R.string.share_product_desc), String.format("%s", item2.getCurrentPrice().setScale(2, RoundingMode.HALF_UP)),
                            item2.getShareImage());
                    shareWindow.showPopupWindow(btnShare);
                    break;
                case R.id.btn_check_material:
                    Product item1 = (Product) v.getTag();
                    Intent intent1 = new Intent(mContext, ProductDetailsActivity.class);
                    intent1.putExtra("productName", item1.getName());
                    intent1.putExtra("type", item1.getType());
                    intent1.putExtra("productId", item1.getId());
                    intent1.putExtra("limitId", timeLimitId);
                    intent1.putExtra("productImg", item1.getDetailImage());
                    intent1.putExtra("scale", String.format("%s", item1.getScale().setScale(2, RoundingMode.HALF_UP)));
                    intent1.putExtra("productPrice", String.format("%s", item1.getCurrentPrice().setScale(2, RoundingMode.HALF_UP)));
                    intent1.putExtra("page", 1);
                    mContext.startActivity(intent1);
                    break;
            }
        }
    }

    private void manageBrand(final int p) {
        IndexProductBrand productBrand = brandList.get(p);
        final int type = productBrand.getStatus() == 0 ? 1 : 2;
        Call<ResultDO<Integer>> call = ApiClient.getApiAdapter().addPro(memberId, type, null, productBrand.getId());
        call.enqueue(new Callback<ResultDO<Integer>>() {
            @Override
            public void onResponse(Call<ResultDO<Integer>> call, Response<ResultDO<Integer>> response) {
                if (response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    ToastUtils.showToast(mContext, type == 1 ? "上架成功" + "\n已上架品牌数：" + response.body().getResult() : "下架成功" + "\n已上架品牌数：" + response.body().getResult());
                    brandList.get(p).setStatus(type == 1 ? 1 : 0);
                    notifyItemChanged(p);
                } else {
                    ToastUtils.showToast(mContext, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Integer>> call, Throwable t) {

            }
        });

    }

    private void manageProduct(final int p) {
        Product product = productList.get(p);
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
                    productList.get(p).setStatus(type == 1 ? 1 : 0);
                    notifyDataSetChanged();
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
