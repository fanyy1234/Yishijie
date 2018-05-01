package com.sunday.common.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sunday.common.event.EventBus;
import com.sunday.common.event.ExitApp;
import com.sunday.common.widgets.dialog.ACProgressFlower;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/2/23.
 */
public class BaseActivity extends AppCompatActivity {
    protected String TAG = getClass().getSimpleName();
    /**
     * 常用成员变量
     */
    protected Intent intent;
    protected Context mContext;
    protected ACProgressFlower progressDialog;
    //标识是否显示对话框
    protected boolean progressShow;
    protected boolean isFinish = false;

    ArrayList<Activity> activities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        EventBus.getDefault().register(this);
        MobclickAgent.openActivityDurationTrack(false);
        activities.add(this);

    }

    public void showLoadingDialog(int res) {
        if(progressDialog!=null  &&  progressDialog.isShowing()){
            progressDialog.cancel();
        }
        progressDialog = new ACProgressFlower.Builder(this)
                .text(res == 0 ? "正在加载中..." : getString(res))
                .build();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                progressShow = false;
            }
        });
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public void dissMissDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(mContext);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
    }

    public void back(View v) {
        finish();
    }

    public void onEvent(ExitApp exitApp) {
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);//反注册EventBus
        }
        ButterKnife.unbind(this);
        isFinish = true;
        activities.remove(this);
    }

   public  void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
