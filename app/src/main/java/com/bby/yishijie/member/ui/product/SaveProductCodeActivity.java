package com.bby.yishijie.member.ui.product;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.utils.ImageUtils;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.utils.ZXingUtils;
import com.sunday.common.utils.download.ImgUtils;
import com.bby.yishijie.R;
import com.bby.yishijie.member.common.AppConfig;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 刘涛 on 2017/6/4.
 */

public class SaveProductCodeActivity extends BaseActivity {

    @Bind(R.id.product_title)
    TextView proTitle;
    @Bind(R.id.product_img)
    ImageView proImg;
    @Bind(R.id.product_code)
    ImageView proCode;
    @Bind(R.id.product_price)
    TextView proPrice;
    @Bind(R.id.layout_code)
    LinearLayout layoutCode;

    private String productName, productImg, productPrice, productShareUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_product_code);
        ButterKnife.bind(this);
        productName = getIntent().getStringExtra("productName");
        productImg = getIntent().getStringExtra("productImg");
        productPrice = getIntent().getStringExtra("productPrice");
        productShareUrl = getIntent().getStringExtra("productShareUrl");
        initView();
    }

    private void initView() {
        if (!TextUtils.isEmpty(productImg)) {
            Glide.with(mContext)
                    .load(productImg)
                    .error(R.mipmap.ic_default)
                    .into(proImg);
        }
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) proImg.getLayoutParams();
        params.height = DeviceUtils.getDisplay(mContext).widthPixels * 3 / 5;
        proImg.setLayoutParams(params);
        proTitle.setText(productName);
        Bitmap bitmap = ZXingUtils.createQRImage(productShareUrl, 90, 90);
        proCode.setImageBitmap(bitmap);
        proPrice.setText("¥"+productPrice);
    }

    @OnClick(R.id.btn_save)
    void save() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this,"没有外部权限", Toast.LENGTH_SHORT).show();
            return;
        }
        new Handler().post(runnable);
    }

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            viewSaveToImage(layoutCode);
        }
    };

    private void viewSaveToImage(View view) {
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        view.setDrawingCacheBackgroundColor(Color.WHITE);

        // 把一个View转换成图片
        Bitmap cachebmp = loadBitmapFromView(view);

        // 添加水印
//        Bitmap bitmap = Bitmap.createBitmap(createWatermarkBitmap(cachebmp,
//                "@ 云缇微店"));
        Bitmap bitmap = Bitmap.createBitmap(cachebmp);
        final String filePath = AppConfig.DEFAULT_SAVE_IMAGE_PATH+ ImageUtils.getFileNameForDate();
        try {
            ImgUtils.saveImageToSD(mContext,filePath,bitmap,100);
            ToastUtils.showToast(mContext,"图片保存成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
//        String savePath=ImageUtils.saveBitmap(bitmap);
//        ImgUtils.scanPhoto(mContext,savePath);

        view.destroyDrawingCache();
    }

    private String getFileName(String imgUrl) {
        int index = imgUrl.lastIndexOf('/') + 1;
        if (index == -1) {
            return System.currentTimeMillis() + ".jpeg";
        }
        return imgUrl.substring(index);
    }
    private Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        int h = v.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);

        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */
        int appWidth=DeviceUtils.getDisplay(mContext).widthPixels;
        int appHeight=DeviceUtils.getDisplay(mContext).heightPixels;
        v.layout((appWidth-w)/2, (int) layoutCode.getY(), w+(appWidth-w)/2, h+ (int) layoutCode.getY());

        v.draw(c);

        return bmp;
    }
    // 为图片target添加水印
    private Bitmap createWatermarkBitmap(Bitmap target, String str) {
        int w = target.getWidth();
        int h = target.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);

        Paint p = new Paint();

        // 水印的颜色
        p.setColor(Color.RED);

        // 水印的字体大小
        p.setTextSize(16);

        p.setAntiAlias(true);// 去锯齿

        canvas.drawBitmap(target, 0, 0, p);

        // 在中间位置开始添加水印
        canvas.drawText(str, w / 2, h / 2, p);

        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        return bmp;
    }
}
