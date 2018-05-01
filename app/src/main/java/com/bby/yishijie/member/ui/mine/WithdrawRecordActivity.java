package com.bby.yishijie.member.ui.mine;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.adapter.BrandProductListAdapter;
import com.bby.yishijie.member.adapter.CommonRecycleAdapter;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.entity.Member;
import com.bby.yishijie.member.entity.Product;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.model.RecycleVisitable;
import com.bby.yishijie.member.model.WithdrawRecord;
import com.bby.yishijie.member.ui.login.LoginActivity;
import com.bby.yishijie.member.widgets.ShareWindow;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.widgets.EmptyLayout;
import com.sunday.common.widgets.ptr.PtrClassicFrameLayout;
import com.sunday.common.widgets.ptr.PtrDefaultHandler;
import com.sunday.common.widgets.ptr.PtrFrameLayout;
import com.sunday.common.widgets.ptr.PtrHandler;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 刘涛 on 2017/4/26.
 */

public class WithdrawRecordActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.rightTxt)
    TextView rightTxt;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.ptr_frame)
    PtrClassicFrameLayout ptrFrame;
    @Bind(R.id.empty_layout)
    EmptyLayout emptyLayout;
    @Bind(R.id.right_btn)
    ImageView rightBtn;

    private LinearLayoutManager layoutManager;
    private CommonRecycleAdapter adapter;
    private List<RecycleVisitable> models = new ArrayList<>();
    private int pageNo = 1, lastVisibleItem;
    private boolean isCanloadMore;
    private Member member;
    private boolean isLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_record);
        ButterKnife.bind(this);
        initView();
        handlerRecyclerView();
        getData();
    }

    private void initView() {
        titleView.setText("提现记录");
        emptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        if (BaseApp.getInstance().getMember() != null) {
            member = BaseApp.getInstance().getMember();
            isLogin = true;
        }
    }

    private void handlerRecyclerView() {
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CommonRecycleAdapter(models,mContext);
        recyclerView.setAdapter(adapter);
//        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
//                .drawable(R.drawable.shape_divider)
//                .build());
        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pageNo = 1;
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
                        && lastVisibleItem + 1 == adapter.getItemCount() && isCanloadMore) {
                    getData();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
        emptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNo = 1;
                getData();
            }
        });

    }


    private void getData() {
        for (int i=0;i<10;i++){
            WithdrawRecord record = new WithdrawRecord();
            record.setMoney(i+"");
            record.setTime("2018-04-28");
            models.add(record);
        }
        adapter.notifyDataSetChanged();
    }

}
