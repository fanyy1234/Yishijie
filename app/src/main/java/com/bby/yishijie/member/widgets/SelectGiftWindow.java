package com.bby.yishijie.member.widgets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.widgets.NoScrollGridView;
import com.bby.yishijie.R;
import com.bby.yishijie.member.adapter.GiftAdapter;
import com.bby.yishijie.member.entity.Gift;
import com.bby.yishijie.member.ui.mine.ShopProtocolActivity;
import com.bby.yishijie.member.ui.order.GiftOrderConfirmActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by 刘涛 on 2017/4/28.
 */

public class SelectGiftWindow extends PopupWindow {


    @Bind(R.id.txt_size)
    TextView txtSize;
    @Bind(R.id.size_gridview)
    NoScrollGridView sizeGridview;

    @Bind(R.id.popu_top_img)
    ImageView popuTopImg;

    @Bind(R.id.product_price)
    TextView productPrice;
    @Bind(R.id.product_store_num)
    TextView productStoreNum;

    private Context mContext;

    private GiftAdapter adapter;
    private Gift gift;
    private List<Gift> dataSet = new ArrayList<>();


    public SelectGiftWindow(Context context, List<Gift> datas) {
        this.mContext = context;
        this.dataSet = datas;
        View windowView = LayoutInflater.from(mContext).inflate(R.layout.layout_select_gift, null);
        int appWidth = DeviceUtils.getDisplay(mContext).widthPixels;
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
        adapter = new GiftAdapter(mContext, dataSet);
        sizeGridview.setAdapter(adapter);
        if (dataSet.size() > 0) {
            dataSet.get(0).setSelect(true);
            gift = dataSet.get(0);
            updateView();
        }
        adapter.notifyDataSetChanged();
        sizeGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < dataSet.size(); i++) {
                    dataSet.get(i).setSelect(false);
                }
                dataSet.get(position).setSelect(true);
                gift = dataSet.get(position);
                updateView();
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void updateView() {
        if (!TextUtils.isEmpty(gift.getImage())) {
            Glide.with(mContext)
                    .load(gift.getImage())
                    .error(R.mipmap.ic_default)
                    .into(popuTopImg);
        }
        productPrice.setText(gift.getName());
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


    @OnClick({R.id.buy_now, R.id.btnCancel})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.buy_now:
                if (gift == null) {
                    return;
                }
                Intent intent = new Intent(mContext, ShopProtocolActivity.class);
                intent.putExtra("gift", gift);
                mContext.startActivity(intent);
                dismiss();
                break;
            case R.id.btnCancel:
                this.dismiss();
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
