package com.bby.yishijie.member.ui.cart;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.sunday.common.base.BaseActivity;
import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.ui.main.CartFragment1;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2017/4/19.
 */

public class CartActivity extends BaseActivity {

    @Bind(R.id.cart)
    FrameLayout cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);
        boolean isLogin= BaseApp.getInstance().getMember()!=null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.cart, CartFragment1.newInstance(isLogin,true)).commit();
    }
}
