package com.bby.yishijie.member.ui.index;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.adapter.CommonRecycleAdapter;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.entity.IndexAd;
import com.bby.yishijie.member.entity.Member;
import com.bby.yishijie.member.entity.Product;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.model.IntegralMall;
import com.bby.yishijie.member.model.IntegralMallItem;
import com.bby.yishijie.member.model.RecycleVisitable;
import com.bby.yishijie.member.ui.base.BaseLazyFragment;
import com.bby.yishijie.member.utils.GridSpacingItemDecoration;
import com.bby.yishijie.shop.entity.IntegralDetail;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.widgets.FeedRootRecyclerView;
import com.sunday.common.widgets.ptr.PtrClassicFrameLayout;
import com.sunday.common.widgets.ptr.PtrDefaultHandler;
import com.sunday.common.widgets.ptr.PtrFrameLayout;
import com.sunday.common.widgets.ptr.PtrHandler;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 刘涛 on 2017/4/11.
 * <p>
 * 首页今日特卖
 */


public class IntegralMallFragment extends BaseLazyFragment {


    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.right_menu)
    ImageView rightMenu;
    @Bind(R.id.left_menu)
    ImageView leftMenu;
    @Bind(R.id.recycler_view)
    FeedRootRecyclerView recyclerView;
    @Bind(R.id.ptr_frame)
    PtrClassicFrameLayout ptrFrame;

    private CommonRecycleAdapter adapter;
    List<RecycleVisitable> models = new ArrayList<RecycleVisitable>();
    GridLayoutManager layoutManager;

    public static IntegralMallFragment newInstance() {
        IntegralMallFragment fragment = new IntegralMallFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_integral_mall, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        title.setText("积分商城");
        initView();
        myScore();
    }


    private boolean isCanLoadMore;
    private int pageNo=1,lastVisibleItem;
    private void initView() {
        IntegralMall integralMall = new IntegralMall();
        integralMall.setJifen("0");
        models.add(integralMall);
        layoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new CommonRecycleAdapter(models,mContext);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .drawable(R.drawable.horizontal_space_divider)
                .build());
        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pageNo=1;
                getData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, recyclerView, header);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1== adapter.getItemCount() && isCanLoadMore) {
                    getData();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
        getData();
    }



    private void getData() {
        Call<ResultDO<List<Product>>> call = ApiClient.getApiAdapter().getProductList(1,1,null,null,null,null,null,pageNo, Constants.PAGE_SIZE);
        call.enqueue(new Callback<ResultDO<List<Product>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<Product>>> call, Response<ResultDO<List<Product>>> response) {
                if (isFinish) {
                    return;
                }
                ptrFrame.refreshComplete();
                ResultDO<List<Product>> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    if (resultDO.getResult()==null){return;}
                    if (pageNo==1){
                        RecycleVisitable recycleVisitable = models.get(0);
                        models.clear();
                        models.add(recycleVisitable);
                    }
                    if (resultDO.getResult().size()== Constants.PAGE_SIZE){
                        pageNo++;
                        isCanLoadMore=true;
                    }else {
                        isCanLoadMore=false;
                    }
                    List<Product> productList = resultDO.getResult();
                    int length = productList.size();
                    for (int i=0;i<length;i++){
                        Product product = productList.get(i);
                        IntegralMallItem integralMallItem = new IntegralMallItem();
                        integralMallItem.setId(product.getId());
                        integralMallItem.setName(product.getName());
                        integralMallItem.setImg(product.getDetailImage());
                        integralMallItem.setPrice(String.format("¥%s", product.getPrice().setScale(2, RoundingMode.HALF_UP)));
                        integralMallItem.setScore(product.getScore()+"");
                        models.add(integralMallItem);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<Product>>> call, Throwable t) {
                    ptrFrame.refreshComplete();
            }
        });

    }
    private void myScore() {
        Member member = BaseApp.getInstance().getMember();
        if (member==null){
            IntegralMall integralMall = new IntegralMall();
            integralMall.setJifen("");
            models.set(0,integralMall);
            adapter.notifyDataSetChanged();
            return;
        }
        Call<ResultDO> call = ApiClient.getApiAdapter().getScore(member.getId());
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                if (isFinish) {
                    return;
                }
                ResultDO resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    IntegralMall integralMall = new IntegralMall();
                    integralMall.setJifen(resultDO.getResult().toString());
                    models.set(0,integralMall);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        //banner.stopTurning();
        super.onDestroy();

    }
}
