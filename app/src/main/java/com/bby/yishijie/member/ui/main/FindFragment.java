package com.bby.yishijie.member.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunday.common.base.BaseFragment;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.widgets.FeedRootRecyclerView;
import com.sunday.common.widgets.ptr.PtrClassicFrameLayout;
import com.sunday.common.widgets.ptr.PtrDefaultHandler;
import com.sunday.common.widgets.ptr.PtrFrameLayout;
import com.sunday.common.widgets.ptr.PtrHandler;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;
import com.bby.yishijie.R;
import com.bby.yishijie.member.adapter.FindListAdapter;
import com.bby.yishijie.member.entity.FindItem;
import com.bby.yishijie.member.http.ApiClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2017/4/11.
 */

public class FindFragment extends BaseFragment {


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

    private FindListAdapter adapter;
    private LinearLayoutManager layoutManager;
    private List<FindItem> dataSet=new ArrayList<>();
    public static FindFragment newInstance() {
        FindFragment fragment = new FindFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }


    private boolean isCanLoadMore;
    private int pageNo=1,lastVisibleItem;
    private void initView() {
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter=new FindListAdapter(mContext,dataSet);
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.header_find, recyclerView, false);
        adapter.setHeaderView(header);
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


    private void getData(){
        Call<ResultDO<List<FindItem>>> call= ApiClient.getApiAdapter().findList(pageNo, Constants.PAGE_SIZE);
        call.enqueue(new Callback<ResultDO<List<FindItem>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<FindItem>>> call, Response<ResultDO<List<FindItem>>> response) {
                if (isFinish){return;}
                ptrFrame.refreshComplete();
                ResultDO<List<FindItem>> resultDO=response.body();
                if (resultDO==null){return;}
                if (resultDO.getCode()==0){
                    if (resultDO.getResult()==null){return;}
                    if (pageNo==1){
                        dataSet.clear();
                    }
                    if (resultDO.getResult().size()== Constants.PAGE_SIZE){
                        pageNo++;
                        isCanLoadMore=true;
                    }else {
                        isCanLoadMore=false;
                    }
                    dataSet.addAll(resultDO.getResult());
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<ResultDO<List<FindItem>>> call, Throwable t) {
                ptrFrame.refreshComplete();
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
