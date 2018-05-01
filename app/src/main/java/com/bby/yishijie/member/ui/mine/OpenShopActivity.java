package com.bby.yishijie.member.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.bby.yishijie.R;
import com.bby.yishijie.member.entity.Gift;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.WebViewActivity;
import com.bby.yishijie.member.widgets.SelectGiftWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2017/4/28.
 */

public class OpenShopActivity extends BaseActivity {


    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.rightTxt)
    TextView rightTxt;
    @Bind(R.id.web_view)
    WebView webView;

    private SelectGiftWindow selectGiftWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_shop);
        ButterKnife.bind(this);
        titleView.setText("我要开店");
        rightTxt.setVisibility(View.GONE);
        int type=1;
        String url = String.format(ApiClient.GIFT_URL + "?type=%d", type);
        //启用支持javascript
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(false);
        settings.setSupportZoom(false);
        settings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!TextUtils.isEmpty(url)) {
                    view.loadUrl(url);
                } else {
                    dissMissDialog();
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dissMissDialog();
            }
        });
        webView.loadUrl(url);
        getGifts();


    }

    @OnClick(R.id.custom_srvice)
    void callService() {
        intent = new Intent(mContext, WebViewActivity.class);
        intent.putExtra("url",ApiClient.CUSTOMER_URL);
        intent.putExtra("title", "客服");
        startActivity(intent);
    }

    @OnClick(R.id.btn_buy)
    void buy() {
     if (selectGiftWindow==null){
         selectGiftWindow=new SelectGiftWindow(mContext,dataSet);
     }
        selectGiftWindow.showPopupWindow(titleView);
    }

    List<Gift> dataSet=new ArrayList<>();
    private void getGifts() {
        Call<ResultDO<List<Gift>>> call = ApiClient.getApiAdapter().getGifts();
        call.enqueue(new Callback<ResultDO<List<Gift>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<Gift>>> call, Response<ResultDO<List<Gift>>> response) {
                ResultDO<List<Gift>> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    if (resultDO.getResult() == null) {
                        return;
                    }
                    dataSet.clear();
                    dataSet.addAll(resultDO.getResult());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<Gift>>> call, Throwable t) {

            }
        });
    }

}
