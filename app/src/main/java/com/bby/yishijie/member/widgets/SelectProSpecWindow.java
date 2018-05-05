package com.bby.yishijie.member.widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bby.yishijie.member.ui.MainActivity;
import com.bumptech.glide.Glide;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.NoScrollGridView;
import com.bby.yishijie.R;
import com.bby.yishijie.member.adapter.ListSpecAdapter;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.common.Constant;
import com.bby.yishijie.member.entity.ProductDetail;
import com.bby.yishijie.member.entity.ProductSpec;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.product.ProductDetailActivity;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bby.yishijie.member.common.Constant.TYPE_BUY_NOW;


/**
 * Created by 刘涛 on 2017/4/25.
 */

public class SelectProSpecWindow extends PopupWindow {


    @Bind(R.id.show_num)
    TextView showNum;
    @Bind(R.id.popu_top_img)
    ImageView popuTopImg;
    @Bind(R.id.product_price)
    TextView productPrice;
    @Bind(R.id.product_store_num)
    TextView productStoreNum;
    @Bind(R.id.select_spec)
    TextView selectSpec;
    @Bind(R.id.txt_size)
    TextView txtSize;
    @Bind(R.id.size_gridview)
    NoScrollGridView sizeGridview;
    @Bind(R.id.txt_color)
    TextView txtColor;
    @Bind(R.id.color_gridview)
    NoScrollGridView colorGridview;
    @Bind(R.id.buy_layout)
    LinearLayout buyLayout;
    @Bind(R.id.buy_now)
    TextView buyNow;
    @Bind(R.id.add_cart)
    TextView addCart;
    @Bind(R.id.linearLayout)
    LinearLayout linearLayout;

    private Context mContext;
    private ProductDetail productDetail;
    private ListSpecAdapter colorAdapter;
    private ListSpecAdapter sizeAdapter;
    private Integer colorId, sizeId;
    private long productId, specId;
    private String colorText = "", sizeText = "";
    private int buyNum = 1;
    private int storeNum;
    private OnSelectListener onSelectListener;
    private int type = 1;//(1-普通商品 2-限时购 3)
    private final static int TYPE_ADD = 1;
    private final static int TYPE_BUY = 2;//普通购买

    private int buyType;//1：选择规格，2：加入购物车 3：立即购买


    public SelectProSpecWindow(Context context, final ProductDetail productDetail, int type, int buyType) {
        this.mContext = context;
        this.productDetail = productDetail;
        this.type = type;
        this.buyType = buyType;
        View windowView = LayoutInflater.from(mContext).inflate(R.layout.select_spec_window, null);
        int appWidth = DeviceUtils.getDisplay(mContext).widthPixels;
        int appHeight = DeviceUtils.getDisplay(mContext).heightPixels;
        setContentView(windowView);
        ButterKnife.bind(this, windowView);
        this.setWidth(appWidth);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        this.setOnDismissListener(new PoponDismissListener());
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setAnimationStyle(R.style.popuwindow);
        initView();
        handlerAdapters();
    }

    public void setBuyType(int buyType) {
        this.buyType = buyType;
        updateView();
    }

    public void setType(int type) {
        this.type = type;
        updateNumLayout();
    }

    private void updateNumLayout() {
        //linearLayout.setVisibility(type == 3 ? View.GONE : View.VISIBLE);
        buyNum = 1;
        showNum.setText(String.valueOf(buyNum));
    }

    private void initView() {
        if (productDetail == null) {
            return;
        }
        if (!TextUtils.isEmpty(productDetail.getDetailImage())) {
            Glide.with(mContext)
                    .load(productDetail.getDetailImage())
                    .error(R.mipmap.ic_default)
                    .into(popuTopImg);
        }
        productId = productDetail.getId();
        productPrice.setText(String.format("¥%s", productDetail.getCurrentPrice()));
        productStoreNum.setText("库存" + productDetail.getProductStore());
        if (productDetail.getProductStore() == 0) {
            productStoreNum.setText("求补货");
            productStoreNum.setBackgroundResource(R.drawable.shape_red_stroke);
            productStoreNum.setTextColor(ContextCompat.getColor(mContext, R.color.main_color));
        }

        storeNum = productDetail.getProductStore();
        showNum.setText(String.valueOf(buyNum));
        if (productDetail.getStatus() != 0) {
            buyNow.setVisibility(View.GONE);
            addCart.setVisibility(View.VISIBLE);

        } else {
            buyNow.setVisibility(View.VISIBLE);
            addCart.setVisibility(View.VISIBLE);
            updateView();
        }
        //linearLayout.setVisibility(type == 3 ? View.GONE : View.VISIBLE);
    }


    private void updateView() {
        if (buyType == TYPE_BUY_NOW) {
            buyNow.setText("确定");
            buyNow.setVisibility(View.VISIBLE);
            addCart.setVisibility(View.GONE);
        } else if (buyType == Constant.TYPE_ADDCART) {
            addCart.setText("确定");
            addCart.setVisibility(View.VISIBLE);
            addCart.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
            addCart.setTextColor(mContext.getResources().getColor(R.color.white));
            buyNow.setVisibility(View.GONE);
        } else {
            addCart.setVisibility(View.VISIBLE);
            buyNow.setVisibility(View.VISIBLE);
            addCart.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            addCart.setTextColor(mContext.getResources().getColor(R.color.black_6));
            addCart.setText("加入购物车");
            buyNow.setText("立即购买");
        }
    }

    private void handlerAdapters() {
        if (productDetail.getColors() != null && productDetail.getColors().size() > 0) {
            txtColor.setVisibility(View.VISIBLE);
            colorGridview.setVisibility(View.VISIBLE);
            colorAdapter = new ListSpecAdapter(mContext, productDetail.getColors());
            colorGridview.setAdapter(colorAdapter);
            colorAdapter.notifyDataSetChanged();
            colorGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    for (int i = 0; i < productDetail.getColors().size(); i++) {
                        productDetail.getColors().get(i).setSelect(false);
                    }
                    productDetail.getColors().get(position).setSelect(true);
                    colorText = productDetail.getColors().get(position).getText();
                    colorId = productDetail.getColors().get(position).getId();
                    colorAdapter.notifyDataSetChanged();
                    queryStock();

                }
            });
        } else {
            txtColor.setVisibility(View.GONE);
            colorGridview.setVisibility(View.GONE);
        }
        if (productDetail.getSizes() != null && productDetail.getSizes().size() > 0) {
            txtSize.setVisibility(View.VISIBLE);
            sizeGridview.setVisibility(View.VISIBLE);
            sizeAdapter = new ListSpecAdapter(mContext, productDetail.getSizes());
            sizeGridview.setAdapter(sizeAdapter);
            sizeGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    for (int i = 0; i < productDetail.getSizes().size(); i++) {
                        productDetail.getSizes().get(i).setSelect(false);
                    }
                    productDetail.getSizes().get(position).setSelect(true);
                    sizeText = productDetail.getSizes().get(position).getText();
                    sizeId = productDetail.getSizes().get(position).getId();
                    sizeAdapter.notifyDataSetChanged();
                    queryStock();


                }
            });
        } else {
            txtSize.setVisibility(View.GONE);
            sizeGridview.setVisibility(View.GONE);
        }
    }

    private void queryStock() {
        selectSpec.setText(String.format("已选%1$s%2$s", sizeText, colorText));
        if (productDetail.getColors().size() > 0 && productDetail.getSizes().size() > 0) {
            if (sizeId == null) {
                return;
            }
        }
        Call<ResultDO<ProductSpec>> call = ApiClient.getApiAdapter().getSpecInfo(productId, colorId, sizeId);
        call.enqueue(new Callback<ResultDO<ProductSpec>>() {
            @Override
            public void onResponse(Call<ResultDO<ProductSpec>> call, Response<ResultDO<ProductSpec>> response) {
                ResultDO<ProductSpec> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    productPrice.setText(type == 2 ? String.format("¥%s", resultDO.getResult().getLimitPrice()) :
                            String.format("¥%s", resultDO.getResult().getPrice()));
                    productStoreNum.setText(type == 2 ? String.format("库存:%s", resultDO.getResult().getLimitValue()) :
                            String.format("库存:%s", resultDO.getResult().getValue()));
                    storeNum = (type == 2 ? resultDO.getResult().getLimitValue() : resultDO.getResult().getValue());
                    if (storeNum == 0) {
                        productStoreNum.setText("求补货");
                        productStoreNum.setBackgroundResource(R.drawable.shape_red_stroke);
                        productStoreNum.setTextColor(ContextCompat.getColor(mContext, R.color.main_color));
                    }
                    specId = resultDO.getResult().getId();
                    buyNum = 1;
                    showNum.setText(String.valueOf(buyNum));
                } else {
                    ToastUtils.showToast(mContext, resultDO.getMessage());
                }

            }

            @Override
            public void onFailure(Call<ResultDO<ProductSpec>> call, Throwable t) {
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }


    //显示
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            backgroundAlpha(0.5f);
        } else {
            this.dismiss();
        }
    }


    @OnClick({R.id.add_cart, R.id.buy_now, R.id.add, R.id.sub, R.id.btnCancel, R.id.product_store_num})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.sub:
                if (buyNum == 1) {
                    return;
                }
                buyNum--;
                showNum.setText(String.valueOf(buyNum));
                break;
            case R.id.add:
               /* if (storeNum == 0) {
                    ToastUtils.showToast(mContext, "暂无库存!");
                    return;
                }*/
                if (buyNum == storeNum && storeNum > 0) {
                    ToastUtils.showToast(mContext, "已达到最大库存");
                    return;
                } else {
                    buyNum++;
                }
                showNum.setText(String.valueOf(buyNum));
                break;
            case R.id.add_cart:
                /*if (buyNum > storeNum) {
                    ToastUtils.showToast(mContext, "超过最大库存");
                    return;
                }*/

                if (MainActivity.isShop&&BaseApp.getInstance().getShopMember()==null){
                    ToastUtils.showToast(mContext, "请先登录");
                    return;
                }
                if (!MainActivity.isShop&&BaseApp.getInstance().getMember()==null){
                    ToastUtils.showToast(mContext, "请先登录");
                    return;
                }
                if (specId == 0) {
                    ToastUtils.showToast(mContext, "请选择规格");
                    return;
                }
                if (onSelectListener != null && specId != 0) {
                    onSelectListener.onSelect(specId, buyNum, TYPE_ADD);
                }
                this.dismiss();
                break;
            case R.id.buy_now:
                /*if (buyNum > storeNum) {
                    ToastUtils.showToast(mContext, "超过最大库存");
                    return;
                }*/
                if (MainActivity.isShop&&BaseApp.getInstance().getShopMember()==null){
                    ToastUtils.showToast(mContext, "请先登录");
                    return;
                }
                if (!MainActivity.isShop&&BaseApp.getInstance().getMember()==null){
                    ToastUtils.showToast(mContext, "请先登录");
                    return;
                }
                if (specId == 0) {
                    ToastUtils.showToast(mContext, "请选择规格");
                    return;
                }
                if (onSelectListener != null && specId != 0) {
                    onSelectListener.onSelect(specId, buyNum, TYPE_BUY);
                }
                this.dismiss();
                break;
            case R.id.btnCancel:
                this.dismiss();
                break;
            case R.id.product_store_num:
                if (storeNum == 0) {
                    applyAddProduct();
                }
                break;
        }
    }

    private void applyAddProduct() {
        ((ProductDetailActivity) (mContext)).showLoadingDialog(0);
        Call<ResultDO> call = ApiClient.getApiAdapter().replenishment(productId, BaseApp.getInstance().getMember().getId(),
                1);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                ((ProductDetailActivity) (mContext)).dissMissDialog();
                if (response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    ToastUtils.showToast(mContext, "申请补货成功，请耐心等待");
                } else {
                    ToastUtils.showToast(mContext, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                ((ProductDetailActivity) (mContext)).dissMissDialog();
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }


    /**
     * 弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     */
    class PoponDismissListener implements OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            //Log.v("List_noteTypeActivity:", "我是关闭事件");
            backgroundAlpha(1f);
            dismiss();
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    public interface OnSelectListener {
        void onSelect(long paramId, int number, int type);
    }


}
