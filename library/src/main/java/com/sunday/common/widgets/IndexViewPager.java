package com.sunday.common.widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 刘涛 on 2017/7/13.
 */

public class IndexViewPager extends ViewPager {


    private OnCheckCanScrollListener onCheckCanScrollListener;

    public IndexViewPager(Context context) {
        super(context);
    }

    public IndexViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        // TODO Auto-generated method stub
        if (checkCanScroll()) {
            return super.onTouchEvent(arg0);
        } else {
            return false;
        }

    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        // TODO Auto-generated method stub
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        // TODO Auto-generated method stub
        super.setCurrentItem(item);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        // TODO Auto-generated method stub
        if (checkCanScroll()) {
            return super.onInterceptTouchEvent(arg0);
        } else {
            return false;
        }

    }

    private boolean checkCanScroll(){
        return onCheckCanScrollListener!=null&&onCheckCanScrollListener.isCanScroll();
    }

    public void setOnCheckCanScrollListener(OnCheckCanScrollListener onCheckCanScrollListener) {
        this.onCheckCanScrollListener = onCheckCanScrollListener;
    }

    public interface OnCheckCanScrollListener{
        boolean isCanScroll();
    }

}
