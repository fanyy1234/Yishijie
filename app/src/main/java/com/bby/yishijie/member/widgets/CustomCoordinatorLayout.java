package com.bby.yishijie.member.widgets;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 刘涛 on 2017/7/12.
 */

public class CustomCoordinatorLayout extends CoordinatorLayout {

    private OnCheckIsNestedScrollListener onCheckIsNestedScrollListener;

    public CustomCoordinatorLayout(Context context) {
        super(context);
    }

    public CustomCoordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onNestedPrePerformAccessibilityAction(View target, int action, Bundle args) {
        if (checkIsNestedScroll()){
            return super.onNestedPrePerformAccessibilityAction(target, action, args);
        }else {
            return false;
        }
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return checkIsNestedScroll() && super.onNestedFling(target, velocityX, velocityY, consumed);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return checkIsNestedScroll()&&super.onNestedPreFling(target, velocityX, velocityY);
    }


    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (checkIsNestedScroll())
            super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    private boolean checkIsNestedScroll(){
        return onCheckIsNestedScrollListener!=null&&onCheckIsNestedScrollListener.isNesetedScroll();
    }

    public void setOnCheckIsNestedScrollListener(OnCheckIsNestedScrollListener onCheckIsNestedScrollListener) {
        this.onCheckIsNestedScrollListener = onCheckIsNestedScrollListener;
    }


    public interface OnCheckIsNestedScrollListener{
        boolean isNesetedScroll();
    }


}
