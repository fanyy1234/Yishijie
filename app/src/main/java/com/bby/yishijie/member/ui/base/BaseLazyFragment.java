package com.bby.yishijie.member.ui.base;

import com.sunday.common.base.BaseFragment;

/**
 * Created by 刘涛 on 2017/8/7.
 */

public class BaseLazyFragment extends BaseFragment {

    // 标志位 标志已经初始化完成。
    protected boolean isPrepared;
    //标志位 fragment是否可见
    protected boolean isVisible;






    /**
     * Fragment数据的懒加载
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * fragment显示时才加载数据
     */
    protected void onVisible() {
        lazyLoad();
    }

    /**
     * fragment懒加载方法
     */
    protected void lazyLoad() {
    }

    /**
     * fragment隐藏
     */
    protected void onInvisible() {
    }

}
