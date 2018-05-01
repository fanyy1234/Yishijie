package com.bby.yishijie.shop.adapter;

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
import com.bby.yishijie.shop.event.UpdateProList;
import com.bby.yishijie.shop.widgets.ShareWindow;
import com.bumptech.glide.Glide;
import com.sunday.common.event.EventBus;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.ToastUtils;

import java.math.RoundingMode;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 刘涛 on 2017/4/12.
 */

public class ProductListAdapter extends RecyclerView.Adapter {


    private Context mContext;
    private List<Product> dataSet;
    private long memberId;


    public ProductListAdapter(Context context) {
        this.mContext = context;
    }
    public ProductListAdapter(Context context, List<Product> datas) {
        this.mContext = context;
        this.dataSet=datas;
        memberId= BaseApp.getInstance().getMember().getId();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_product_list_item_shop, null);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ProductHolder productHolder= (ProductHolder) holder;
        Product item=dataSet.get(position);
        if (!TextUtils.isEmpty(item.getDetailImage())){
            Glide.with(mContext)
                    .load(item.getDetailImage())
                    .error(R.mipmap.ic_default)
                    .into(productHolder.productImg);
        }
        if (item.getType()==2&&item.getIsScore()==1){
            productHolder.productTag1.setVisibility(View.VISIBLE);
            productHolder.productTag2.setVisibility(View.VISIBLE);
            productHolder.productTitle.setText("\u3000\u3000\u3000\u3000\u3000\u3000\u3000     "+item.getName());
        }else if (item.getType()==2&&item.getIsScore()==0){
            productHolder.productTag1.setVisibility(View.VISIBLE);
            productHolder.productTag2.setVisibility(View.GONE);
            productHolder.productTitle.setText("\u3000\u3000  "+item.getName());
        }else if (item.getType()!=2&&item.getIsScore()==1){
            productHolder.productTag1.setVisibility(View.GONE);
            productHolder.productTag2.setVisibility(View.VISIBLE);
            productHolder.productTitle.setText("\u3000\u3000\u3000\u3000\u3000   "+item.getName());
        }else {
            productHolder.productTag1.setVisibility(View.GONE);
            productHolder.productTag2.setVisibility(View.GONE);
            productHolder.productTitle.setText(item.getName());
        }
        productHolder.priceNow.setText(String.format("¥%s",item.getCurrentPrice().setScale(2,RoundingMode.HALF_UP)));
        productHolder.priceOld.setText(String.format("%s", item.getScale().setScale(2, RoundingMode.HALF_UP)));
        productHolder.storeNum.setText(String.format("库存%d",item.getProductStore()));
        productHolder.btnAdd.setImageResource(item.getStatus()==0?R.mipmap.product_add:R.mipmap.sub_pro);
        productHolder.totalLayout.setTag(item);
        productHolder.btnAdd.setTag(position);
        productHolder.btnShare.setTag(item);
        productHolder.btnCheckMaterial.setTag(item);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }






    class ProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @Bind(R.id.product_img)
        ImageView productImg;
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
        @Bind(R.id.store_num)
        TextView storeNum;
        @Bind(R.id.total_layout)
        RelativeLayout totalLayout;
        @Bind(R.id.btn_check_material)
        ImageView btnCheckMaterial;
        @Bind(R.id.product_tag1)
        TextView productTag1;
        @Bind(R.id.product_tag2)
        TextView productTag2;

        public ProductHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            totalLayout.setOnClickListener(this);
            btnAdd.setOnClickListener(this);
            btnShare.setOnClickListener(this);
            btnCheckMaterial.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.total_layout:
                    Product item= (Product) v.getTag();
                    Intent intent=new Intent(mContext, ProductDetailsActivity.class);
                    intent.putExtra("productName",item.getName());
                    intent.putExtra("type",item.getType());
                    intent.putExtra("limitId",item.getLimitId()!=null?item.getLimitId():0);
                    intent.putExtra("productId",item.getId());
                    intent.putExtra("productImg",item.getDetailImage());
                    intent.putExtra("scale",String.format("%s", item.getScale().setScale(2, RoundingMode.HALF_UP)));
                    mContext.startActivity(intent);
                    break;
                case R.id.btn_add_product:
                    int p= (int) v.getTag();
                    manageProduct(p);
                    break;
                case R.id.btn_share:
                    /*1我要开店2分享店铺3普通商品4限时购商品)-memberId(会员Id)-productId(商品Id)-limitId(限时购时间段Id)
                    这四个参数没有值的传0
                    例如普通商品分享：http://weixin.zj-yunti.com/authorizationPage.html?param=3-1-14-0*/
                    Product item2 = (Product) v.getTag();
                    String shareUrl=String.format("%1$s%2$d-%3$d-%4$d-%5$d", ApiClient.SHARE_URL,3,memberId,item2.getId(),0);
                    ShareWindow shareWindow=new ShareWindow(mContext,shareUrl,item2.getName(),item2.getDetailImage(),
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
                    intent1.putExtra("limitId",item1.getLimitId()!=null?item1.getLimitId():0);
                    intent1.putExtra("productId", item1.getId());
                    intent1.putExtra("productImg",item1.getDetailImage());
                    intent1.putExtra("scale",String.format("%s", item1.getScale().setScale(2, RoundingMode.HALF_UP)));
                    intent1.putExtra("page",1);
                    mContext.startActivity(intent1);
                    break;
            }

        }
    }

    private void manageProduct(final int p){
        Product product=dataSet.get(p);
        final int type=product.getStatus()==0?1:2;
        Call<ResultDO<Integer>> call=ApiClient.getApiAdapter().addPro(memberId,type,product.getId(),null);
        call.enqueue(new Callback<ResultDO<Integer>>() {
            @Override
            public void onResponse(Call<ResultDO<Integer>> call, Response<ResultDO<Integer>> response) {

                if (response.body()==null){return;}
                if (response.body().getCode()==0){
                    ToastUtils.showToast(mContext,type==1?"上架成功"+"\n已上架产品数："+response.body().getResult():"下架成功"+"\n已上架产品数："+response.body().getResult());
                    EventBus.getDefault().post(new UpdateProList());
                }else {
                    ToastUtils.showToast(mContext,response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Integer>> call, Throwable t) {

            }
        });
    }
}
