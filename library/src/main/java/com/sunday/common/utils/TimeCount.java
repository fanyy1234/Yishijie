package com.sunday.common.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by Wangran on 2015/7/14.
 */
public class TimeCount extends CountDownTimer {
    private TextView getCodeBtn;
    private OnTimeCountFinish onTimeCountFinish;
    public TimeCount(long millisInFuture, long countDownInterval, TextView getCodeBtn) {
        super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        this.getCodeBtn = getCodeBtn;
    }

    @Override
    public void onFinish() {//计时完毕时触发
        getCodeBtn.setText("重新发送");
        getCodeBtn.setClickable(true);
        getCodeBtn.setTag(1);
        if(onTimeCountFinish!=null){
            onTimeCountFinish.onFinish();
        }
    }

    @Override
    public void onTick(long millisUntilFinished) {//计时过程显示
        getCodeBtn.setClickable(false);
        getCodeBtn.setText(millisUntilFinished / 1000 + "秒");
    }

    public void setOnTimeCountFinish(OnTimeCountFinish onTimeCountFinish) {
        this.onTimeCountFinish = onTimeCountFinish;
    }

    public interface OnTimeCountFinish{
        void onFinish();
    }
}
