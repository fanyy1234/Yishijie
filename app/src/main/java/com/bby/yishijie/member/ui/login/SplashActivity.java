package com.bby.yishijie.member.ui.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.sunday.common.base.BaseActivity;
import com.sunday.common.utils.SharePerferenceUtils;
import com.sunday.common.utils.StatusBarUtil;
import com.bby.yishijie.R;
import com.bby.yishijie.member.ui.MainActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2017/7/31.
 */

public class SplashActivity extends BaseActivity {

    @Bind(R.id.splash_image)
    ImageView splashView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(2000);
        splashView.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                boolean isFirst = SharePerferenceUtils.getIns(mContext).getBoolean("is_first", true);
                if (isFirst) {
                    intent = new Intent(mContext, GuideActivity.class);
                } else {
                    intent = new Intent(mContext, MainActivity.class);
                }
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //getData();
    }


}
