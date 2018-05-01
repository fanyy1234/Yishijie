package com.bby.yishijie.member.ui.product;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bby.yishijie.R;
import com.sunday.common.adapter.MainFragmentPagerAdapter;
import com.sunday.common.base.BaseFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 刘涛 on 2017/5/3.
 */

public class ProductMaterialFragment extends BaseFragment {


    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    private long productId;
    private String productName;

    private ArrayList<Fragment> fragments = new ArrayList<>();

    public static ProductMaterialFragment newInstance(long productId,String productName) {
        ProductMaterialFragment fragment = new ProductMaterialFragment();
        Bundle args = new Bundle();
        args.putLong("productId", productId);
        args.putString("productName",productName);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_product_material, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initParams();
        initFragments();
    }

    private void initParams() {
        if (getArguments() != null) {
            productId = getArguments().getLong("productId");
            productName=getArguments().getString("productName");
        }
    }

    private void initFragments() {
        fragments.add(OfficialMaterialFragment.newInstance(productId, 0));
        fragments.add(OfficialMaterialFragment.newInstance(productId, 1));
        viewPager.setAdapter(new MainFragmentPagerAdapter(getChildFragmentManager(), mContext,
                fragments, R.array.material_title));
        tabLayout.setupWithViewPager(viewPager);
    }

    @OnClick(R.id.floating_button)
    void onClick() {
        intent = new Intent(mContext, EditProductMaterialActivity.class);
        intent.putExtra("productId", productId);
        intent.putExtra("productName", productName);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
