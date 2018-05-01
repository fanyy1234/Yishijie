package com.bby.yishijie.member.holder;


import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import com.bby.yishijie.member.adapter.CommonRecycleAdapter;


/**
 * Created by yq05481 on 2017/1/3.
 */

public abstract class BaseRecyleViewHolder<T> extends RecyclerView.ViewHolder {
    private SparseArray<View> views;
    private View mItemView;
    public BaseRecyleViewHolder(View itemView) {
        super(itemView);
        views = new SparseArray<>();
        this.mItemView = itemView;
    }

    public View getView(int resID) {
        View view = views.get(resID);

        if (view == null) {
            view = mItemView.findViewById(resID);
            views.put(resID,view);
        }

        return view;
    }

    public abstract void setUpView(T model, int position, CommonRecycleAdapter adapter);
}
