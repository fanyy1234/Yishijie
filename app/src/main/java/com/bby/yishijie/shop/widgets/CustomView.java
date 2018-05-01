package com.bby.yishijie.shop.widgets;

import android.content.Context;
import android.view.View;
import android.widget.Scroller;

/**
 * Created by 刘涛 on 2017/7/11.
 */

public class CustomView extends View {
    public CustomView(Context context) {
        super(context);
    }

    Scroller scroller=new Scroller(getContext());

    private void smoothScrollTo(int destx,int destY){
        int scrollx=getScrollX();
        int delta=destx-scrollx;
        scroller.startScroll(scrollx,0,delta,0,1000);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
            postInvalidate();
        }
    }
}
