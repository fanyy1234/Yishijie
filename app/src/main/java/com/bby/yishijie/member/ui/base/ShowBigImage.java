
package com.bby.yishijie.member.ui.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.utils.ImageUtils;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.utils.download.ImgUtils;
import com.sunday.common.widgets.photoview.HackyViewPager;
import com.sunday.common.widgets.photoview.PhotoView;
import com.sunday.common.widgets.photoview.PhotoViewAttacher;
import com.bby.yishijie.R;
import com.bby.yishijie.member.common.AppConfig;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 下载显示大图
 */
public class ShowBigImage extends BaseActivity {
    private static final String TAG = "ShowBigImage";

    private static final String ISLOCKED_ARG = "isLocked";
    private HackyViewPager mViewPager;

    private List<String> imagePath = new ArrayList<>();
    private Context mContext;
    private boolean isShowSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_big_image);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        isShowSave = getIntent().getBooleanExtra("isShowSave", false);
        mContext = this;
        mViewPager = (HackyViewPager) findViewById(R.id.view_page_show);
        TextView saveImage = (TextView) findViewById(R.id.save_img);
        saveImage.setVisibility(isShowSave ? View.VISIBLE : View.GONE);
        MyPagerAdapter mAdapter;
        mViewPager.setAdapter(mAdapter = new MyPagerAdapter(mContext, imagePath));

        ArrayList<String> data = getIntent().getStringArrayListExtra("data");
        if (data != null) {
            imagePath.addAll(data);
        }
        int index = getIntent().getIntExtra("index", 0);
        String path = getIntent().getStringExtra("imgPath");
        if (!TextUtils.isEmpty(path)) {
            imagePath.add(path);

        }

        mAdapter.notifyDataSetChanged();
        if (savedInstanceState != null) {
            boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG, false);
            mViewPager.setLocked(isLocked);
        }
        mViewPager.setCurrentItem(index);
        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoView photoView = (PhotoView) mViewPager.findViewWithTag("photo_view");
                Bitmap image = ((BitmapDrawable) photoView.getDrawable()).getBitmap();
                final String filePath = AppConfig.DEFAULT_SAVE_IMAGE_PATH+ ImageUtils.getFileNameForDate();
                try {
                    ImgUtils.saveImageToSD(mContext,filePath,image,100);
                    ToastUtils.showToast(mContext,"图片保存至"+AppConfig.DEFAULT_SAVE_IMAGE_PATH);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                if (image != null) {
//                    final String filePath = AppConfig.DEFAULT_SAVE_IMAGE_PATH;
//                    String path = ImageUtils.savBitmap(image,filePath);
//                    if (TextUtils.isEmpty(path)) {
//                        ToastUtils.showToast(mContext, "下载失败");
//                    } else {
//                        ToastUtils.showToast(mContext, "/" + path);
//                        intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                        intent.setData(Uri.fromFile(new File(path)));
//                        mContext.sendBroadcast(intent);
//                    }
//                }

            }
        });
    }

    static class MyPagerAdapter extends PagerAdapter {

        private Context mContext;
        private List<String> dataList;

        public MyPagerAdapter(Context context, List<String> data) {
            mContext = context;
            dataList = data;
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            photoView.setTag("photo_view");
            String path = dataList.get(position);
            File file = new File(path);
            if (file.exists()) {
                Picasso.with(mContext)
                        .load(file)
                        .placeholder(R.drawable.default_error)
                        .into(photoView);
            } else {


                Picasso.with(mContext)
                        .load(path)
                        .placeholder(R.drawable.default_error)
                        .into(photoView);
            }


            photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    ((Activity) mContext).finish();
                    // ViewManager.getInstance().finishActivity(ShowBigImage.class);
                }
            });
            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    private void toggleViewPagerScrolling() {
        if (isViewPagerActive()) {
            ((HackyViewPager) mViewPager).toggleLock();
        }
    }

    private boolean isViewPagerActive() {
        return (mViewPager != null && mViewPager instanceof HackyViewPager);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (isViewPagerActive()) {
            outState.putBoolean(ISLOCKED_ARG, ((HackyViewPager) mViewPager).isLocked());
        }
        super.onSaveInstanceState(outState);
    }
}
