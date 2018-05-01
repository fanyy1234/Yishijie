package com.bby.yishijie.shop.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * Created by 刘涛 on 2017/9/26.
 */

public class MyProgressBar extends ProgressBar {

    String text;
    Paint mPaint;

    public MyProgressBar(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        initText();
    }

    public MyProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        initText();
    }


    public MyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        initText();
    }

    @Override
    public synchronized void setProgress(int progress) {
        // TODO Auto-generated method stub
        setText(progress);
        super.setProgress(progress);

    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        //this.setText();
        Rect rect = new Rect();
        this.mPaint.getTextBounds(this.text, 0, this.text.length(), rect);
        int x = getWidth() - rect.centerX();
        int y = (getHeight() / 2) - rect.centerY();
        canvas.drawText(this.text, x, y, this.mPaint);
    }

    //初始化，画笔
    private void initText(){
        this.mPaint = new Paint();
        this.mPaint.setColor(Color.parseColor("#d1e9ff"));
        this.mPaint.setTextSize(sp2px(getContext(),12));

    }

    private void setText(){
        setText(this.getProgress());
    }

    //设置文字内容
    private void setText(int progress){
//        int i = (progress * 100)/this.getMax();
//        this.text = String.valueOf(i) + "%";
        this.text=String.format("限量%1$d件，剩余%2$d件",this.getMax(),this.getMax()-this.getProgress());
    }

    /**
     * 将sp值转换为px值，保证文字大小不变          *           * @param spValue          * @param fontScale          *            （DisplayMetrics类中属性scaledDensity）          * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}

