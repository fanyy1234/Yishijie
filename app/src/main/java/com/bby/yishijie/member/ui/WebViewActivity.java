package com.bby.yishijie.member.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.sunday.common.base.BaseActivity;
import com.bby.yishijie.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2017/5/23.
 */

public class WebViewActivity extends BaseActivity {

    @Bind(R.id.webview)
    WebView webview;
    @Bind(R.id.title_view)
    TextView titleView;

    private String title;
    private boolean isFromNewSpace = false;
    private static final int FILE_SELECT_CODE = 0;

    private ValueCallback<Uri> mUploadMessage;//回调图片选择，4.4以下
    private ValueCallback<Uri[]> mUploadCallbackAboveL;//回调图片选择，5.0以上


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        String url = getIntent().getStringExtra("url");
        titleView.setText(getIntent().getStringExtra("title"));
        WebSettings webSettings = webview.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(false);

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        webSettings.setLoadWithOverviewMode(true);

        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);


        //加载需要显示的网页
        webview.loadUrl(url);
        //设置Web视图
        webview.setWebViewClient(new webViewClient());
        webview.setWebChromeClient(new MyWebChromeClient());
    }

    //    设置回退
//    覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack(); //goBack()表示返回WebView的上一页面
            return true;
        }
        finish();//结束退出程序
        return false;
    }

    //Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private class MyWebChromeClient extends WebChromeClient {

        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {

            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            startActivityForResult(Intent.createChooser(i, "File Chooser"), FILE_SELECT_CODE);

        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            startActivityForResult(Intent.createChooser(i, "File Browser"), FILE_SELECT_CODE);
        }

        // For Android 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            startActivityForResult(Intent.createChooser(i, "File Chooser"), FILE_SELECT_CODE);

        }

        // For Android 5.0+
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            mUploadCallbackAboveL = filePathCallback;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            startActivityForResult(
                    Intent.createChooser(i, "File Browser"),
                    FILE_SELECT_CODE);
            return true;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            if (Build.VERSION.SDK_INT >= 21) {//5.0以上版本处理
                mUploadCallbackAboveL.onReceiveValue(null);//回调给js
            } else {
                //4.4以下处理
                mUploadMessage.onReceiveValue(null);
            }
            return;
        }

        switch (requestCode) {
            case FILE_SELECT_CODE: {
                if (Build.VERSION.SDK_INT >= 21) {//5.0以上版本处理
                    Uri uri = data.getData();
                    Uri[] uris = new Uri[]{uri};
                   /* ClipData clipData = data.getClipData();  //选择多张
                    if (clipData != null) {
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            ClipData.Item item = clipData.getItemAt(i);
                            Uri uri = item.getUri();
                            uris[i]=uri;
                        }
                    }*/
                    mUploadCallbackAboveL.onReceiveValue(uris);//回调给js
                } else {//4.4以下处理
                    Uri uri = data.getData();
                    mUploadMessage.onReceiveValue(uri);
                }


            }
            break;
        }
    }


}
