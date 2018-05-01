package com.bby.yishijie.member.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bby.yishijie.member.entity.Ad;
import com.bby.yishijie.member.entity.GlobalBrand;
import com.bby.yishijie.member.entity.GlobalCat;
import com.bby.yishijie.member.entity.Product;
import com.bby.yishijie.member.ui.product.BrandProductListActivity;
import com.bby.yishijie.member.ui.product.GlobalProListFragment;
import com.bby.yishijie.member.ui.product.ProductDetailActivity;
import com.bumptech.glide.Glide;

import com.sunday.common.adapter.MainFragmentPagerAdapter;
import com.sunday.common.utils.PixUtils;
import com.sunday.common.utils.SpannalbeStringUtils;
import com.sunday.common.widgets.IndexViewPager;
import com.sunday.common.widgets.RecyclerTabLayout;
import com.sunday.common.widgets.banner.ConvenientBanner;
import com.sunday.common.widgets.banner.holder.ViewHolderCreator;
import com.bby.yishijie.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.BindInt;
import butterknife.ButterKnife;

/**
 * Author by Damon,  on 2017/12/5.
 */

public class GlobalBuyAdapter extends RecyclerView.Adapter {

    private Context mContext;

    private final static int TYPE_HEAD = 1;
    private final static int TYPE_LIST = 2;
    private final static int TYPE_END = 3;
    private List<GlobalBrand> brandList;
    private List<Ad> adList;
    private List<GlobalCat> catList;
    private List<Product> productList;
    private LayoutInflater layoutInflater;
    private View.OnClickListener onClickListener;
    private FragmentManager fm;

    public GlobalBuyAdapter(Context context, List<GlobalBrand> brandList,
                            List<Ad> adList, List<GlobalCat> catList, List<Product> productList, FragmentManager fragmentManager) {
        this.mContext = context;
        this.layoutInflater = LayoutInflater.from(mContext);
        this.brandList = brandList;
        this.adList = adList;
        this.catList = catList;
        this.productList = productList;
        this.fm = fragmentManager;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEAD;
        } else if (brandList != null && position == brandList.size() + 1) {
            return TYPE_END;
        } else {
            return TYPE_LIST;
        }

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEAD:
                View view = layoutInflater.inflate(R.layout.global_buy_head, null);
                RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(layoutParams);
                return new HeadHolder(view);
            case TYPE_LIST:
                view = layoutInflater.inflate(R.layout.layout_product__brand_item, null);
                return new ListHolder(view);
            case TYPE_END:
                view = layoutInflater.inflate(R.layout.global_buy_end, null);
                RecyclerView.LayoutParams layoutParams1 = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(layoutParams1);
                return new EndHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case TYPE_HEAD:
                HeadHolder headHolder = (HeadHolder) holder;
                if (adList != null && !adList.isEmpty()) {
                    headHolder.banner.setPages(headHolder.viewHolderCreator, adList)
                            .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                            .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused});
                    headHolder.banner.notifyDataSetChanged();
                    headHolder.banner.startTurning(3000);
                    if (adList.size() == 1) {
                        headHolder.banner.stopTurning();
                    }
                }
                if (productList != null && productList.size() > 0) {
                    DecimalFormat df = new DecimalFormat("#.00");
                    SpannableString str = SpannalbeStringUtils.setTextSizePx("¥", PixUtils.sp2px(mContext, 12));
                    Glide.with(mContext)
                            .load(productList.get(0).getDetailImage())
                            .error(R.mipmap.ic_default)
                            .into(headHolder.ivPro1);
                    headHolder.tvProName1.setText(productList.get(0).getName());
                    headHolder.tvProPrice1.setText(str);
                    headHolder.tvProPrice1.append(df.format(productList.get(0).getCurrentPrice().doubleValue()));
                    Glide.with(mContext)
                            .load(productList.get(1).getDetailImage())
                            .error(R.mipmap.ic_default)
                            .into(headHolder.ivPro2);
                    headHolder.tvProName2.setText(productList.get(1).getName());
                    headHolder.tvProPrice2.setText(str);
                    headHolder.tvProPrice2.append(df.format(productList.get(1).getCurrentPrice().doubleValue()));

                    Glide.with(mContext)
                            .load(productList.get(2).getDetailImage())
                            .error(R.mipmap.ic_default)
                            .into(headHolder.ivPro3);
                    headHolder.tvProName3.setText(productList.get(2).getName());
                    headHolder.tvProPrice3.setText(str);
                    headHolder.tvProPrice3.append(df.format(productList.get(2).getCurrentPrice().doubleValue()));

                    Glide.with(mContext)
                            .load(productList.get(3).getDetailImage())
                            .error(R.mipmap.ic_default)
                            .into(headHolder.ivPro4);
                    headHolder.tvProName4.setText(productList.get(3).getName());
                    headHolder.tvProPrice4.setText(str);
                    headHolder.tvProPrice4.append(df.format(productList.get(3).getCurrentPrice().doubleValue()));

                    Glide.with(mContext)
                            .load(productList.get(4).getDetailImage())
                            .error(R.mipmap.ic_default)
                            .into(headHolder.ivPro5);
                    headHolder.tvProName5.setText(productList.get(4).getName());
                    headHolder.tvProPrice5.setText(str);
                    headHolder.tvProPrice5.append(df.format(productList.get(4).getCurrentPrice().doubleValue()));

                    Glide.with(mContext)
                            .load(productList.get(5).getDetailImage())
                            .error(R.mipmap.ic_default)
                            .into(headHolder.ivPro6);
                    headHolder.tvProName6.setText(productList.get(5).getName());
                    headHolder.tvProPrice6.setText(str);
                    headHolder.tvProPrice6.append(df.format(productList.get(5).getCurrentPrice().doubleValue()));
                    headHolder.rlJingxuan1.setTag(productList.get(0));
                    headHolder.rlJingxuan2.setTag(productList.get(1));
                    headHolder.llJingxuan3.setTag(productList.get(2));
                    headHolder.llJingxuan4.setTag(productList.get(3));
                    headHolder.llJingxuan5.setTag(productList.get(4));
                    headHolder.llJingxuan6.setTag(productList.get(5));
                    headHolder.rlAllBrand.setOnClickListener(onClickListener);
                    headHolder.rlJingxuan1.setOnClickListener(onClickListener);
                    headHolder.rlJingxuan2.setOnClickListener(onClickListener);
                    headHolder.llJingxuan3.setOnClickListener(onClickListener);
                    headHolder.llJingxuan4.setOnClickListener(onClickListener);
                    headHolder.llJingxuan5.setOnClickListener(onClickListener);
                    headHolder.llJingxuan6.setOnClickListener(onClickListener);
                }

                break;
            case TYPE_LIST:
                ListHolder listHolder = (ListHolder) holder;
                GlobalBrand item = brandList.get(position - 1);
                if (!TextUtils.isEmpty(item.getImage())) {
                    Glide.with(mContext)
                            .load(item.getImage())
                            .error(R.mipmap.ic_default)
                            .into(listHolder.mainImg);
                }
                if (!TextUtils.isEmpty(item.getLogo())) {
                    Glide.with(mContext)
                            .load(item.getImage())
                            .error(R.mipmap.ic_default)
                            .into(listHolder.brandImg);
                }
                listHolder.brandTitle.setText(String.format("%s", item.getBrandName()));
                listHolder.totalLayout.setOnClickListener(onClickListener);
                break;
            case TYPE_END:
                EndHolder endHolder = (EndHolder) holder;

                break;
        }

    }

    @Override
    public int getItemCount() {
        return brandList == null ? 2 : brandList.size() + 2;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    class HeadHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.banner)
        ConvenientBanner banner;
        @Bind(R.id.tv_pro_name1)
        TextView tvProName1;
        @Bind(R.id.tv_pro_price1)
        TextView tvProPrice1;
        @Bind(R.id.iv_pro1)
        ImageView ivPro1;
        @Bind(R.id.rl_jingxuan1)
        RelativeLayout rlJingxuan1;
        @Bind(R.id.tv_pro_name2)
        TextView tvProName2;
        @Bind(R.id.tv_pro_price2)
        TextView tvProPrice2;
        @Bind(R.id.iv_pro2)
        ImageView ivPro2;
        @Bind(R.id.rl_jingxuan2)
        RelativeLayout rlJingxuan2;
        @Bind(R.id.tv_pro_name3)
        TextView tvProName3;
        @Bind(R.id.tv_pro_price3)
        TextView tvProPrice3;
        @Bind(R.id.iv_pro3)
        ImageView ivPro3;
        @Bind(R.id.tv_pro_name4)
        TextView tvProName4;
        @Bind(R.id.tv_pro_price4)
        TextView tvProPrice4;
        @Bind(R.id.iv_pro4)
        ImageView ivPro4;
        @Bind(R.id.tv_pro_name5)
        TextView tvProName5;
        @Bind(R.id.tv_pro_price5)
        TextView tvProPrice5;
        @Bind(R.id.iv_pro5)
        ImageView ivPro5;
        @Bind(R.id.tv_pro_name6)
        TextView tvProName6;
        @Bind(R.id.tv_pro_price6)
        TextView tvProPrice6;
        @Bind(R.id.iv_pro6)
        ImageView ivPro6;
        @Bind(R.id.rl_all_brand)
        RelativeLayout rlAllBrand;
        @Bind(R.id.global_head)
        LinearLayout globalHead;
        @Bind(R.id.ll_jingxuan3)
        LinearLayout llJingxuan3;
        @Bind(R.id.ll_jingxuan4)
        LinearLayout llJingxuan4;
        @Bind(R.id.ll_jingxuan5)
        LinearLayout llJingxuan5;
        @Bind(R.id.ll_jingxuan6)
        LinearLayout llJingxuan6;

        private IndexAdHolder indexAdHolder;
        private ViewHolderCreator viewHolderCreator;

        public HeadHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //banner.startTurning(3000);
            indexAdHolder = new IndexAdHolder();
            viewHolderCreator = new ViewHolderCreator<IndexAdHolder>() {

                @Override
                public IndexAdHolder createHolder() {
                    return indexAdHolder;
                }
            };
            indexAdHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Ad item = (Ad) v.getTag();
                    if (item.getDetailType() == 1) {
                        if (item.getDetailId() > 0) {
                            Intent intent = new Intent(mContext, ProductDetailActivity.class);
                            intent.putExtra("type", 1);
                            intent.putExtra("productId", item.getDetailId());
                            mContext.startActivity(intent);
                        }
                    } else {
                        Intent intent = new Intent(mContext, BrandProductListActivity.class);
                        intent.putExtra("brandName", String.format("%s", item.getName()));
                        intent.putExtra("brandId", item.getDetailId());
                        intent.putExtra("brandImg", item.getImage());
                        intent.putExtra("type", item.getType());
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }


    class ListHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.main_img)
        ImageView mainImg;
        @Bind(R.id.brand_img)
        ImageView brandImg;
        @Bind(R.id.brand_title)
        TextView brandTitle;
        @Bind(R.id.brand_star)
        TextView brandStar;
        @Bind(R.id.btn_buy)
        TextView btnBuy;
        @Bind(R.id.total_layout)
        RelativeLayout totalLayout;

        public ListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class EndHolder extends RecyclerView.ViewHolder {

        //        @Bind(R.id.tab_layout)
//        TabLayout tabLayout;
        @Bind(R.id.global_view_pager)
        IndexViewPager viewPager;
        @Bind(R.id.ll_global_end)
        LinearLayout llGlobalEnd;
        @Bind(R.id.tabs_layout)
        RecyclerTabLayout tabsLayout;

        private ArrayList<Fragment> fragments = new ArrayList<>();
        private List<String> titles = new ArrayList<>();
        //        private MainFragmentPagerAdapter adapter;
        private PagerAdapter adapter;

        public EndHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            for (int i = 0; i < catList.size(); i++) {
                fragments.add(GlobalProListFragment.newInstance(catList.get(i).getCatId()));
                titles.add(catList.get(i).getName());
            }
//            adapter = new MainFragmentPagerAdapter(fm, fragments, titles);
//            viewPager.setAdapter(adapter);
            //tabLayout.setupWithViewPager(viewPager);
            adapter = new PagerAdapter(fm);
            viewPager.setAdapter(adapter);
            viewPager.setOnCheckCanScrollListener(new IndexViewPager.OnCheckCanScrollListener() {
                @Override
                public boolean isCanScroll() {
                    return !checkTabLayoutVisible();
                }
            });

            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) tabsLayout.getLayoutParams();
            tabsLayout.setLayoutParams(params);
            tabsLayout.setUpWithAdapter(new GlobalTabAdapter(viewPager, mContext, catList));
            viewPager.setOffscreenPageLimit(catList.size());
        }

        public class PagerAdapter extends FragmentPagerAdapter {

            private Fragment[] fragments;

            public PagerAdapter(FragmentManager fm) {
                super(fm);
                fragments = new Fragment[catList.size()];
            }

            @Override
            public Fragment getItem(int position) {
                if (fragments[position] == null) {
                    fragments[position] = GlobalProListFragment.newInstance(catList.get(position).getCatId());
                }
                return fragments[position];
            }

            @Override
            public int getCount() {
                return catList.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return catList.get(position).getName();
            }
        }

        public boolean checkTabLayoutVisible(){
            boolean cover = false;
            Rect rect = new Rect();
            cover = tabsLayout.getGlobalVisibleRect(rect);
            if (cover) {
                if (rect.width() >= tabsLayout.getMeasuredWidth() && rect.height() >= tabsLayout.getMeasuredHeight()) {
                    return !cover;
                }
            }
            return true;
        }
    }


}
