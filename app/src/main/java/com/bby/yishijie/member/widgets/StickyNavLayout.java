package com.bby.yishijie.member.widgets;

import android.content.Context;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.bby.yishijie.R;


public class StickyNavLayout extends LinearLayout implements NestedScrollingParent, NestedScrollingChild, ScrollingView {
    private static final String TAG = "StickyNavLayout";

    private final NestedScrollingParentHelper mParentHelper = new NestedScrollingParentHelper(this);





    //NestedScrollingParent 收到child  startNestedScroll的通知来回调
    //Parent 收到 onStartNestedScroll() 回调，决定是否需要配合 Child 一起进行处理滑动，如果需要配合，
    // 还会回调 onNestedScrollAccepted()。
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        Log.e(TAG, "onStartNestedScroll");
        //return true;
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        Log.e(TAG, "onNestedScrollAccepted");
        mParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
        //通知parent
        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);

    }

    @Override
    public void onStopNestedScroll(View target) {
        Log.e(TAG, "onStopNestedScroll");
        mParentHelper.onStopNestedScroll(target);
        stopNestedScroll();
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.e(TAG, "onNestedScroll");
        //child 滑动之后 this的回调
        final int oldScrollY = getScrollY();
        scrollBy(0, dyUnconsumed);
        final int myConsumed = getScrollY() - oldScrollY;
        final int myUnconsumed = dyUnconsumed - myConsumed;
        //调用this.parent
        dispatchNestedScroll(0, myConsumed, 0, myUnconsumed, null);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        Log.e(TAG, "onNestedPreScroll");
        //每次滑动前，Child 先询问 Parent 是否需要滑动，即 dispatchNestedPreScroll()，
        // 这就回调到 Parent 的 onNestedPreScroll()，Parent 可以在这个回调中“劫持”掉 Child 的滑动，也就是先于 Child 滑动。
        //这里先询问this.parent

        /*dispatchNestedPreScroll(dx, dy, consumed, null);*/

        boolean hiddenTop = dy > 0 && getScrollY() < mTopViewHeight;
        boolean showTop = dy < 0 && getScrollY() >= 0&&getScrollY()<mTopViewHeight && !ViewCompat.canScrollVertically(target, -1);

        if (hiddenTop || showTop) {
            scrollBy(0, dy);
            consumed[1] = dy;
        } else {
            dispatchNestedPreScroll(dx, dy, consumed, null);
        }
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        Log.e(TAG, "onNestedFling");
        //return false;
        if (!consumed) {
            flingWithNestedDispatch((int) velocityY);
            return true;
        }
        return false;
    }

    private void flingWithNestedDispatch(int velocityY) {
        final int scrollY = getScrollY();
        final boolean canFling = (scrollY > 0 || velocityY > 0)
                && (scrollY < getScrollRange() || velocityY < 0);
        if (!dispatchNestedPreFling(0, velocityY)) {
            dispatchNestedFling(0, velocityY, canFling);
            if (canFling) {
                fling(velocityY);
            }
        }
    }

    int getScrollRange() {
        int scrollRange = 0;
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            scrollRange = Math.max(0,
                    child.getHeight() - (getHeight() - getPaddingBottom() - getPaddingTop()));
        }
        return scrollRange;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        Log.e(TAG, "onNestedPreFling");
        //down - //up+
        if (getScrollY() >= mTopViewHeight) return false;
        fling((int) velocityY);
        return true;
    }

    @Override
    public int getNestedScrollAxes() {
        Log.e(TAG, "getNestedScrollAxes");
        return mParentHelper.getNestedScrollAxes();
    }

    private View mTop;
    private View mNav;
    private RecyclerView mViewPager;

    private int mTopViewHeight;

    private OverScroller mScroller;
    private VelocityTracker mVelocityTracker;
    private int mTouchSlop;
    private int mMaximumVelocity, mMinimumVelocity;

    private float mLastY;
    private boolean mDragging;

    public StickyNavLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);

        mScroller = new OverScroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMaximumVelocity = ViewConfiguration.get(context)
                .getScaledMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.get(context)
                .getScaledMinimumFlingVelocity();

    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }


    /*@Override
    public boolean onTouchEvent(MotionEvent event) {
        initVelocityTrackerIfNotExists();
        mVelocityTracker.addMovement(event);
        int action = event.getAction();
        float y = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished())
                    mScroller.abortAnimation();
                mLastY = y;
                return true;
            case MotionEvent.ACTION_MOVE:
                float dy = y - mLastY;

                if (!mDragging && Math.abs(dy) > mTouchSlop) {
                    mDragging = true;
                }
                if (mDragging) {
                    scrollBy(0, (int) -dy);
                }

                mLastY = y;
                break;
            case MotionEvent.ACTION_CANCEL:
                mDragging = false;
                recycleVelocityTracker();
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_UP:
                mDragging = false;
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int velocityY = (int) mVelocityTracker.getYVelocity();
                if (Math.abs(velocityY) > mMinimumVelocity) {
                    fling(-velocityY);
                }
                recycleVelocityTracker();
                break;
        }

        return super.onTouchEvent(event);
    }*/


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTop = findViewById(R.id.id_stickynavlayout_topview);
        mNav = findViewById(R.id.id_stickynavlayout_indicator);
        View view = findViewById(R.id.id_stickynavlayout_viewpager);
        if (!(view instanceof RecyclerView)) {
            throw new RuntimeException(
                    "id_stickynavlayout_viewpager show used by ViewPager !");
        }
        mViewPager = (RecyclerView) view;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //不限制顶部的高度
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        getChildAt(0).measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
        params.height = getMeasuredHeight() - mNav.getMeasuredHeight();
        mViewPager.setLayoutParams(params);
        final int measureSpec = getChildMeasureSpec(heightMeasureSpec, mViewPager.getPaddingTop() + mViewPager.getPaddingBottom(), params.height);
        mViewPager.measure(widthMeasureSpec, measureSpec);
        setMeasuredDimension(getMeasuredWidth(), mTop.getMeasuredHeight() + mNav.getMeasuredHeight() + mViewPager.getMeasuredHeight());

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTopViewHeight = mTop.getMeasuredHeight();
    }


    public void fling(int velocityY) {
        mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, mTopViewHeight);
        invalidate();
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > mTopViewHeight) {
            y = mTopViewHeight;
        }
        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }


    private final static String Tag = "StickyNavLayout";

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        Log.i(Tag, "setNestedScrollingEnabled:" + enabled);
        getScrollingChildHelper().setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        Log.i(Tag, "isNestedScrollingEnabled");
        return getScrollingChildHelper().isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        //告诉parent 准备进入滑动状态
        return getScrollingChildHelper().startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        Log.i(Tag, "stopNestedScroll");
        //结束整个流程。
        getScrollingChildHelper().stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        Log.i(Tag, "hasNestedScrollingParent");
        return getScrollingChildHelper().hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow) {
        Log.i(Tag, "dispatchNestedScroll:dxConsumed:" + dxConsumed + "," +
                "dyConsumed:" + dyConsumed + ",dxUnconsumed:" + dxUnconsumed + ",dyUnconsumed:" +
                dyUnconsumed + ",offsetInWindow:" + offsetInWindow);
        //如果滑动距离还有剩余 询问parent是否需要在继续滑动剩下的距离
        //向父view汇报滚动情况，包括子view消费的部分和子view没有消费的部分。
        //如果父view接受了它的滚动参数，进行了部分消费，则这个函数返回true，否则为false。
        //这个函数一般在子view处理scroll后调用。
        return getScrollingChildHelper().dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        //询问parent 是否需要滑动 在子View的onInterceptTouchEvent或者onTouch中(一般在MontionEvent.ACTION_MOVE事件里)，
        // 调用该方法通知父View滑动的距离。该方法的第三第四个参数返回父view消费掉的scroll长度和子View的窗体偏移量。
        // 如果这个scroll没有被消费完，则子view进行处理剩下的一些距离，由于窗体进行了移动，如果你记录了手指最后的位置，
        // 需要根据第四个参数offsetInWindow计算偏移量，才能保证下一次的touch事件的计算是正确的。
        //如果父view接受了它的滚动参数，进行了部分消费，则这个函数返回true，否则为false。
        //这个函数一般在子view处理scroll前调用。

        Log.i(Tag, "dispatchNestedPreScroll:dx" + dx + ",dy:" + dy + ",consumed:" + consumed + ",offsetInWindow:" + offsetInWindow);
        return getScrollingChildHelper().dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        Log.i(Tag, "dispatchNestedFling:velocityX:" + velocityX + ",velocityY:" + velocityY + ",consumed:" + consumed);
        return getScrollingChildHelper().dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        Log.i(Tag, "dispatchNestedPreFling:velocityX:" + velocityX + ",velocityY:" + velocityY);
        return getScrollingChildHelper().dispatchNestedPreFling(velocityX, velocityY);
    }


    private NestedScrollingChildHelper mScrollingChildHelper;

    private NestedScrollingChildHelper getScrollingChildHelper() {
        if (mScrollingChildHelper == null) {
            mScrollingChildHelper = new NestedScrollingChildHelper(this);
            mScrollingChildHelper.setNestedScrollingEnabled(true);
        }
        return mScrollingChildHelper;
    }

    @Override
    public int computeHorizontalScrollRange() {
        return super.computeHorizontalScrollRange();
    }

    @Override
    public int computeHorizontalScrollOffset() {
        return super.computeHorizontalScrollOffset();
    }

    @Override
    public int computeHorizontalScrollExtent() {
        return super.computeHorizontalScrollExtent();
    }

    @Override
    public int computeVerticalScrollRange() {
        return super.computeHorizontalScrollRange();
    }

    @Override
    public int computeVerticalScrollOffset() {
        return Math.max(0, super.computeVerticalScrollOffset());
    }

    @Override
    public int computeVerticalScrollExtent() {
        return super.computeVerticalScrollExtent();
    }


}
