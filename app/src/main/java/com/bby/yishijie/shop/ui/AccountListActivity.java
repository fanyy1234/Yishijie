package com.bby.yishijie.shop.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.shop.adapter.AccountListAdapter;
import com.bby.yishijie.shop.entity.Account;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.EmptyLayout;
import com.sunday.common.widgets.ptr.PtrClassicFrameLayout;
import com.sunday.common.widgets.ptr.PtrDefaultHandler;
import com.sunday.common.widgets.ptr.PtrFrameLayout;
import com.sunday.common.widgets.ptr.PtrHandler;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;
import com.sunday.common.widgets.swipe.Closeable;
import com.sunday.common.widgets.swipe.OnSwipeMenuItemClickListener;
import com.sunday.common.widgets.swipe.SwipeMenu;
import com.sunday.common.widgets.swipe.SwipeMenuCreator;
import com.sunday.common.widgets.swipe.SwipeMenuItem;
import com.sunday.common.widgets.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 刘涛 on 2017/5/12.
 */

public class AccountListActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.rightTxt)
    TextView rightTxt;
    @Bind(R.id.right_btn)
    ImageView rightBtn;
    @Bind(R.id.common_header)
    RelativeLayout commonHeader;
    @Bind(R.id.recycler_view)
    SwipeMenuRecyclerView recyclerView;
    @Bind(R.id.ptr_frame)
    PtrClassicFrameLayout ptrFrame;
    @Bind(R.id.empty_layout)
    EmptyLayout emptyLayout;

    private boolean isSelect;
    private long memberId;

    private AccountListAdapter adapter;
    private List<Account> dataSet = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_list);
        ButterKnife.bind(this);
        isSelect = getIntent().getBooleanExtra("isSelect", false);
        memberId = BaseApp.getInstance().getMember().getId();
        initView();
        getData();

    }

    private void initView() {
        rightTxt.setText("添加账户");
        titleView.setText(isSelect ? "选择账户" : "账户列表");
        emptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        adapter = new AccountListAdapter(mContext, dataSet);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext).
                drawable(R.drawable.horizontal_space_divider).build());
        //右滑菜单
        recyclerView.setSwipeMenuCreator(swipeMenuCreator);
        recyclerView.setSwipeMenuItemClickListener(menuItemClickListener);
        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, recyclerView, header);
            }
        });
        ptrFrame.disableWhenHorizontalMove(true);


        adapter.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Account item = (Account) v.getTag();
                if (isSelect) {
                    Intent data = new Intent();
                    data.putExtra("account", item);
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });

        emptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });

    }

    /**
     * 菜单创建器。在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.d60);
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            SwipeMenuItem editItem = new SwipeMenuItem(mContext).setBackgroundDrawable(R.color.text_pressed)
                    .setText("删除")
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(editItem);
        }
    };


    /**
     * 菜单点击监听。
     */
    private OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
        @Override
        public void onItemClick(Closeable closeable, final int adapterPosition, int menuPosition, @SwipeMenuRecyclerView.DirectionMode int direction) {
            switch (direction) {
                case SwipeMenuRecyclerView.LEFT_DIRECTION:
                    break;
                case SwipeMenuRecyclerView.RIGHT_DIRECTION:
                    closeable.smoothCloseMenu();//关闭被点击的菜单
                    AlertDialog dialog = new AlertDialog.Builder(mContext).setTitle("温馨提示")
                            .setMessage("确认删除吗")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteAccount(dataSet.get(adapterPosition).getId());
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                    break;
            }
        }
    };

    private void deleteAccount(int id){
     Call<ResultDO> call= ApiClient.getApiAdapter().delAccount(id);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                if (isFinish){return;}
                if (response.body()==null){return;}
                if (response.body().getCode()==0){
                    ToastUtils.showToast(mContext,"删除成功");
                    getData();
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {

            }
        });
    }


    private void getData() {
        showLoadingDialog(0);
        Call<ResultDO<List<Account>>> call = ApiClient.getApiAdapter().getAccountList(memberId);
        call.enqueue(new Callback<ResultDO<List<Account>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<Account>>> call, Response<ResultDO<List<Account>>> response) {
                if (isFinish) {
                    return;
                }
                dissMissDialog();
                ptrFrame.refreshComplete();
                if (response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    dataSet.clear();
                    dataSet.addAll(response.body().getResult());
                    adapter.notifyDataSetChanged();
                    if (dataSet.size()>0){
                        emptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                    }else {
                        emptyLayout.setErrorType(EmptyLayout.NODATA);
                    }
                } else {
                    ToastUtils.showToast(mContext, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<Account>>> call, Throwable t) {
                dissMissDialog();
                ptrFrame.refreshComplete();
                emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
            }
        });
    }


    @OnClick({R.id.rightTxt})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.rightTxt:
                intent = new Intent(mContext, AddAccountActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }
}
