package com.bby.yishijie.member.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunday.common.base.BaseActivity;
import com.sunday.common.utils.ToastUtils;
import com.bby.yishijie.R;
import com.bby.yishijie.member.entity.Gift;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.order.GiftOrderConfirmActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author by Damon,  on 2018/1/19.
 */

public class ShopProtocolActivity extends BaseActivity {


    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.left_txt)
    TextView leftTxt;
    @Bind(R.id.right_menu)
    ImageView rightMenu;
    @Bind(R.id.left_menu)
    ImageView leftMenu;
    @Bind(R.id.total_layout)
    RelativeLayout totalLayout;
    @Bind(R.id.web_view)
    WebView webView;
    @Bind(R.id.protocol_check)
    ImageButton protocolCheck;
    @Bind(R.id.btn_confirm)
    TextView btnConfirm;

    private Gift gift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_protocol);
        ButterKnife.bind(this);
        title.setText("开店协议");
        gift = (Gift) getIntent().getSerializableExtra("gift");
        int type=3;
        String url = String.format(ApiClient.PROTOCOL_URL + "?type=%d", type);
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
    }


    @OnClick(R.id.btn_confirm)
    void confirmBuy() {
        if (!isAgree) {
            ToastUtils.showToast(mContext, "请阅读并同意用户协议");
            return;
        }
        if (gift == null) {
            return;
        }
        Intent intent = new Intent(mContext, GiftOrderConfirmActivity.class);
        intent.putExtra("gift", gift);
        mContext.startActivity(intent);

    }

    private boolean isAgree = false;

    @OnClick(R.id.protocol_check)
    void check() {
        if (!isAgree) {
            protocolCheck.setSelected(true);
        } else {
            protocolCheck.setSelected(false);
        }
        isAgree = !isAgree;
    }


}
