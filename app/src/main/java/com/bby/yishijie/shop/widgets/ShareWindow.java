package com.bby.yishijie.shop.widgets;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.ui.product.SaveProductCodeActivity;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.utils.SpannalbeStringUtils;
import com.sunday.common.utils.ToastUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.CLIPBOARD_SERVICE;


/**
 * Created by 刘涛 on 2017/5/18.
 */

public class ShareWindow extends PopupWindow {

    @Bind(R.id.money)
    TextView money;
    @Bind(R.id.share_desc)
    TextView shareDesc;
    @Bind(R.id.menu_copy)
    LinearLayout menuCopy;
    @Bind(R.id.menu_code)
    LinearLayout menuCode;
    @Bind(R.id.menu_weixin)
    LinearLayout menuWeixin;
    @Bind(R.id.menu_weixin_circle)
    LinearLayout menuWeixinCircle;
    @Bind(R.id.menu_layout)
    LinearLayout menu_layout;
    private Context mContext;
    private String shareUrl, productName, productImg, scale, productDesc,productPrice;
    private ClipboardManager myClipboard;
    private UMWeb web;
    private String qrCodeImg="";
    public ShareWindow(Context context, String shareUrl, String productName, String productImg, String scale,
                       String productDesc, String productPrice, String qrcodeImg) {
        this.mContext = context;
        View windowView = LayoutInflater.from(mContext).inflate(R.layout.layout_share_window_shop, null);
        int appWidth = DeviceUtils.getDisplay(mContext).widthPixels;
        this.shareUrl = shareUrl;
        this.productName = productName;
        this.productImg = productImg;
        this.scale = scale;//商品可以赚的金额
        this.productDesc = productDesc;
        this.productPrice=productPrice;
        this.qrCodeImg=qrcodeImg;
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
    }

    private void initView() {
        UMShareAPI umShareAPI = UMShareAPI.get(mContext);
        UMImage umImage=null;
        if (!TextUtils.isEmpty(productImg)) {
            umImage = new UMImage(mContext, productImg);
        } else {
            umImage=new UMImage(mContext,R.mipmap.logo);
        }
        myClipboard = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
        web = new UMWeb(shareUrl);
        web.setTitle(productName);
        web.setThumb(umImage);
        web.setDescription(productDesc);
        if (!TextUtils.isEmpty(scale)) {
            money.setText("赚" + scale);
        }
        if (TextUtils.isEmpty(scale)) {
            shareDesc.setVisibility(View.GONE);
            money.setVisibility(View.GONE);
            menuCode.setVisibility(View.GONE);
            menu_layout.setWeightSum(3.0f);
        }
        shareDesc.setText(SpannalbeStringUtils.setTextColor("只要你的好友通过你的链接购买此商品，你就能赚到至少",
                mContext.getResources().getColor(R.color.black_5)));
        shareDesc.append(SpannalbeStringUtils.setTextColor(scale, mContext.getResources().getColor(R.color.colorPrimary)));
        shareDesc.append(SpannalbeStringUtils.setTextColor("元利润", mContext.getResources().getColor(R.color.black_5)));
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

    @OnClick({R.id.menu_copy, R.id.menu_code, R.id.menu_weixin, R.id.menu_weixin_circle, R.id.btn_cancel})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_copy:
                ClipData myClip = ClipData.newPlainText("text", shareUrl);
                myClipboard.setPrimaryClip(myClip);
                ToastUtils.showToast(mContext, "已复制到粘贴板");
                break;
            case R.id.menu_code:
                Intent intent=new Intent(mContext, SaveProductCodeActivity.class);
                intent.putExtra("productImg",qrCodeImg);
                intent.putExtra("productName",productName);
                intent.putExtra("productShareUrl",shareUrl);
                intent.putExtra("productPrice",productPrice);
                mContext.startActivity(intent);
                dismiss();
                break;
            case R.id.menu_weixin:
                new ShareAction((Activity) mContext).setPlatform(SHARE_MEDIA.WEIXIN)
                        .withMedia(web)
                        .share();
                dismiss();
                break;
            case R.id.menu_weixin_circle:
                new ShareAction((Activity) mContext).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                        .withMedia(web)
                        .share();
                dismiss();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
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





}
