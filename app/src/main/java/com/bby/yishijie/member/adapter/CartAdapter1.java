package com.bby.yishijie.member.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bby.yishijie.member.entity.CartItem;
import com.bby.yishijie.member.entity.CartListItem;
import com.bby.yishijie.member.ui.product.ProductDetailActivity;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.sunday.common.widgets.NoScrollListview;
import com.bby.yishijie.R;


import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2017/9/17.
 */

public class CartAdapter1 extends RecyclerView.Adapter {


    private Context mContext;
    private List<CartListItem> dataSet;
    private View.OnClickListener onClickListener;
    private boolean editType = false;

    public CartAdapter1(Context context, List<CartListItem> datas) {
        mContext = context;
        dataSet = datas;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_cartlist_item, null);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListHolder listHolder = (ListHolder) holder;
        CartListItem item = dataSet.get(position);
        listHolder.listTitle.setText(item.getTypeName());
        listHolder.adapter.setParentType(item.getType());
        if (item.getType() == 1) {
            //预热商品
            if (item.isSelected()) {
                listHolder.listSelectAll.setImageResource(editType ? R.mipmap.ic_select : R.mipmap.check_unse);
            } else {
                listHolder.listSelectAll.setImageResource(editType ? R.mipmap.ic_select_no : R.mipmap.check_unse);
            }
        } else {
            if (item.isSelected()) {
                listHolder.listSelectAll.setImageResource(R.mipmap.ic_select);
            } else {
                listHolder.listSelectAll.setImageResource(R.mipmap.ic_select_no);
            }
        }

        listHolder.listSelectAll.setOnClickListener(onClickListener);
        listHolder.listSelectAll.setTag(position);
        listHolder.adapter.setParentPosition(position);
        listHolder.adapter.setList(dataSet.get(position).getCartItemList());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        listHolder.listView.setLayoutParams(params);
        listHolder.adapter.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setEditType(boolean editType) {
        this.editType = editType;
    }


    class ListHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.list_select_all)
        ImageView listSelectAll;
        @Bind(R.id.list_title)
        TextView listTitle;
        @Bind(R.id.list_view)
        NoScrollListview listView;

        private CartListAdapter adapter;

        public ListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            adapter = new CartListAdapter();
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mContext, ProductDetailActivity.class);
                    CartItem cartItem = (CartItem) adapter.getItem(position);
                    intent.putExtra("type", cartItem.getType());
                    intent.putExtra("productId", cartItem.getProductId());
                    mContext.startActivity(intent);
                }
            });

        }
    }

    class CartListAdapter extends BaseAdapter {

        private List<CartItem> list = new ArrayList<>();
        private int parentPosition;
        private int parentType;
//        public CartListAdapter(List<CartItem> datas){
//            list=datas;
//        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CartHolder cartHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_cart_item, null);
                cartHolder = new CartHolder(convertView);
                convertView.setTag(cartHolder);
            } else {
                cartHolder = (CartHolder) convertView.getTag();
            }

            CartItem cartItem = list.get(position);
            if (!TextUtils.isEmpty(cartItem.getImage())) {
                Picasso.with(mContext)
                        .load(cartItem.getImage())
                        .placeholder(R.mipmap.ic_default)
                        .error(R.mipmap.ic_default)
                        .into(cartHolder.ivGoods);
            }
            cartHolder.ivCheckGood.setVisibility(View.VISIBLE);
            cartHolder.llGoodLeft.setVisibility(View.VISIBLE);
            cartHolder.deleteImg.setVisibility(View.VISIBLE);
            cartHolder.productTitle.setText(cartItem.getProductName());
            cartHolder.productSpec.setText(String.format("%s", cartItem.getElements()));
            cartHolder.productPrice.setText(String.format("¥%s", cartItem.getPrice().setScale(2, RoundingMode.HALF_UP)));
            cartHolder.tvNum2.setText(String.valueOf(cartItem.getNum()));
            cartHolder.productNum.setText(String.valueOf("X" + cartItem.getNum()));
            if (cartItem.getTypeName().equals("积分商城")){
                cartHolder.productScore.setVisibility(View.VISIBLE);
                cartHolder.productScore.setText(cartItem.getScore()+"积分");
            }
            else {
                cartHolder.productScore.setVisibility(View.GONE);
            }

            if (parentType == 1) {
                //预热商品
                if (cartItem.isSelect()) {
                    cartHolder.ivCheckGood.setImageResource(editType ? R.mipmap.ic_select : R.mipmap.check_unse);
                } else {
                    cartHolder.ivCheckGood.setImageResource(editType ? R.mipmap.ic_select_no : R.mipmap.check_unse);
                }
            } else {
                if (cartItem.isSelect()) {
                    cartHolder.ivCheckGood.setImageResource(R.mipmap.ic_select);
                } else {
                    cartHolder.ivCheckGood.setImageResource(R.mipmap.ic_select_no);
                }
            }


            //售罄
            if (cartItem.getProductStore() == 0) {
                cartHolder.ivCheckGood.setVisibility(View.INVISIBLE);
                cartHolder.llGoodLeft.setVisibility(View.INVISIBLE);
                cartHolder.deleteImg.setVisibility(editType?View.VISIBLE:View.GONE);
            } else if (!editType) {
                //cartHolder.ivCheckGood.setVisibility(View.INVISIBLE);
                cartHolder.llGoodLeft.setVisibility(View.INVISIBLE);
                cartHolder.deleteImg.setVisibility(View.INVISIBLE);
            }

            cartHolder.imgFlag.setVisibility(cartItem.getProductStore() == 0 || cartItem.getIsInvalid() == 1 ? View.VISIBLE : View.GONE);
            cartHolder.imgFlag.setImageResource(cartItem.getProductStore() == 0 ? R.mipmap.sell_out : R.mipmap.invalid);


            cartHolder.deleteImg.setOnClickListener(onClickListener);
            cartHolder.ivCheckGood.setOnClickListener(onClickListener);
            cartHolder.ivAdd.setOnClickListener(onClickListener);
            cartHolder.ivReduce.setOnClickListener(onClickListener);

            cartHolder.deleteImg.setTag(position);
            cartHolder.ivCheckGood.setTag(position);
            cartHolder.ivAdd.setTag(position);
            cartHolder.ivReduce.setTag(position);
            cartHolder.deleteImg.setTag(R.id.delete, parentPosition);
            cartHolder.ivCheckGood.setTag(R.id.ivCheckGood, parentPosition);
            cartHolder.ivAdd.setTag(R.id.ivAdd, parentPosition);
            cartHolder.ivReduce.setTag(R.id.ivReduce, parentPosition);

            return convertView;
        }

        public void setList(List<CartItem> list) {
            this.list = list;
        }

        public void setParentPosition(int parentPosition) {
            this.parentPosition = parentPosition;
        }

        public int getParentPosition() {
            return parentPosition;
        }

        public int getParentType() {
            return parentType;
        }

        public void setParentType(int parentType) {
            this.parentType = parentType;
        }

        class CartHolder {
            @Bind(R.id.ivCheckGood)
            ImageView ivCheckGood;
            @Bind(R.id.ivGoods)
            ImageView ivGoods;
            @Bind(R.id.img_flag)
            ImageView imgFlag;
            @Bind(R.id.product_title)
            TextView productTitle;
            @Bind(R.id.product_spec)
            TextView productSpec;
            @Bind(R.id.ivReduce)
            ImageView ivReduce;
            @Bind(R.id.tvNum2)
            TextView tvNum2;
            @Bind(R.id.ivAdd)
            ImageView ivAdd;
            @Bind(R.id.llGoodLeft)
            LinearLayout llGoodLeft;
            @Bind(R.id.product_price)
            TextView productPrice;
            @Bind(R.id.delete)
            ImageView deleteImg;
            @Bind(R.id.product_num)
            TextView productNum;
            @Bind(R.id.product_score)
            TextView productScore;

            CartHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

}