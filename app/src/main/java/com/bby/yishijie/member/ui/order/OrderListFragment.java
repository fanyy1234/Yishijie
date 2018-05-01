package com.bby.yishijie.member.ui.order;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bby.yishijie.member.ui.MainActivity;
import com.sunday.common.base.BaseFragment;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.EmptyLayout;
import com.sunday.common.widgets.ptr.PtrClassicFrameLayout;
import com.sunday.common.widgets.ptr.PtrDefaultHandler;
import com.sunday.common.widgets.ptr.PtrFrameLayout;
import com.sunday.common.widgets.ptr.PtrHandler;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;
import com.bby.yishijie.R;
import com.bby.yishijie.member.adapter.OrderAdapter;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.entity.Order;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.WebViewActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2017/4/24.
 */

public class OrderListFragment extends BaseFragment {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.ptr_frame)
    PtrClassicFrameLayout ptrFrame;
    @Bind(R.id.empty_layout)
    EmptyLayout emptyLayout;

    private Integer status;
    private long memberId;
    private int pageNo = 1,lastVisibleItem;

    private List<Order> dataSet = new ArrayList<>();
    private OrderAdapter orderAdapter;
    private boolean isCanloadMore;
    private LinearLayoutManager layoutManager;
    private int orderType;
    public static OrderListFragment newInstance(int status) {
        OrderListFragment fragment = new OrderListFragment();
        Bundle args = new Bundle();
        args.putInt("status", status);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order_list, container, false);
        ButterKnife.bind(this, rootView);
        if (MainActivity.isShop){
            orderType = 2;
        }
        else {
            orderType = 1;
        }
        if (getArguments()!=null){
            status = getArguments().getInt("status");
            if(status==-1){
                status = null;
            }
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        memberId=BaseApp.getInstance().getMember().getId();

        orderAdapter = new OrderAdapter(mContext,dataSet);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(orderAdapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .drawable(R.drawable.horizontal_space_divider)
                .build());
        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pageNo = 1;
                getOrderList();
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
                        && lastVisibleItem + 1== orderAdapter.getItemCount() && isCanloadMore) {
                    getOrderList();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });

        orderAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int p= (int) v.getTag();
                Order item=dataSet.get(p);
                switch (v.getId()){
                    case R.id.item_view:
                        //订单详情
                        intent=new Intent(mContext,OrderDetailActivity.class);
                        intent.putExtra("order",item);
                        startActivity(intent);
                        break;
                    case R.id.order_btn1:
                       switch (item.getStatus()){
                           case -1:
                               //删除订单
                               AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                               builder.setTitle("温馨提示");
                               builder.setMessage("确认删除订单吗？");
                               builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                       dialog.dismiss();
                                   }
                               });
                               builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                       dialog.dismiss();
                                       delOrder(p);

                                   }
                               });
                               builder.show();
                               break;
                           case 0:
                               //支付订单 先调用订单支付获取订单
                               getOrderPay(item.getId());
                               break;
                           case 1:
                               //申请退款
                               intent=new Intent(mContext,ApplyRefundActivity.class);
                               intent.putExtra("order",item);
                               intent.putExtra("type",1);//申请退款
                               startActivity(intent);
                               break;
                           case 2:
                               //确认收货
                               AlertDialog.Builder builder2=new AlertDialog.Builder(mContext);
                               builder2.setTitle("温馨提示");
                               builder2.setMessage("确认收货吗？");
                               builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                       dialog.dismiss();
                                   }
                               });
                               builder2.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                       dialog.dismiss();
                                       confirmRecieve(p);

                                   }
                               });
                               builder2.show();
                               break;
                           case 5:
                               //查看退款详情
                                intent=new Intent(mContext,RefundDetailActivity.class);
                               intent.putExtra("order",item);
                               startActivity(intent);
                               break;
                           case 8:
                               //查看退货详情
                               intent=new Intent(mContext,RefundDetailActivity.class);
                               intent.putExtra("order",item);
                               startActivity(intent);
                               break;
                       }
                        break;
                    case R.id.order_btn2:
                        switch (item.getStatus()){
                            case 0:
                                //取消订单
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                                builder1.setTitle("温馨提示");
                                builder1.setMessage("确认取消订单吗？");
                                builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder1.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        cancelOrder(p);

                                    }
                                });
                                builder1.show();
                                break;
                            case 2:
                                //查看物流
                                intent = new Intent(mContext, WebViewActivity.class);
                                String url=String.format("https://m.kuaidi100.com/index_all.html?type=%1$s&postid=%2$s",item.getExpressEng(),item.getExpressNo());
                                intent.putExtra("url",url);
                                intent.putExtra("title", "物流查询");
                                startActivity(intent);
                                break;

                        }
                        break;
                    case R.id.order_btn3:
                        //申请退货
                        intent=new Intent(mContext,ApplyRefundActivity.class);
                        intent.putExtra("order",item);
                        intent.putExtra("type",2);//申请退货
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    private void getOrderPay(long orderId){
        Call<ResultDO<Order>>call=ApiClient.getApiAdapter().getOrderPay(orderId);
        call.enqueue(new Callback<ResultDO<Order>>() {
            @Override
            public void onResponse(Call<ResultDO<Order>> call, Response<ResultDO<Order>> response) {
                if (isFinish){return;}
                if (response.body()==null){return;}
                if (response.body().getCode()==0){
                    Order item=response.body().getResult();
                    intent=new Intent(mContext,OrderPayActivity.class);
                    intent.putExtra("order",item);
                    intent.putExtra("isGroupBuy",false);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Order>> call, Throwable t) {

            }
        });
    }


    private void cancelOrder(int p){
        showLoadingDialog(0);
        Call<ResultDO> call=ApiClient.getApiAdapter().cancelOrder(dataSet.get(p).getId());
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                if (isFinish){return;}
                dissMissDialog();
                if (response.body()==null){
                    return;
                }
                if (response.body().getCode()==0){
                    refresh();
                }else {
                    ToastUtils.showToast(mContext,response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext,R.string.network_error);
            }
        });
    }

    private void confirmRecieve(int p){
        showLoadingDialog(0);
        Call<ResultDO> call=ApiClient.getApiAdapter().confirmRecieve(dataSet.get(p).getId());
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                if (isFinish){return;}
                dissMissDialog();
                if (response.body()==null){
                    return;
                }
                if (response.body().getCode()==0){
                    refresh();
                }else {
                    ToastUtils.showToast(mContext,response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext,R.string.network_error);
            }
        });
    }

    private void delOrder(int p){
        showLoadingDialog(0);
        Call<ResultDO> call=ApiClient.getApiAdapter().delOrder(dataSet.get(p).getId());
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                if (isFinish){return;}
                dissMissDialog();
                if (response.body()==null){
                    return;
                }
                if (response.body().getCode()==0){
                    refresh();
                }else {
                    ToastUtils.showToast(mContext,response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext,R.string.network_error);
            }
        });
    }




    public void refresh(){
        ptrFrame.autoRefresh();
    }


    @Override
    public void onResume() {
        super.onResume();
        ptrFrame.autoRefresh();
        pageNo = 1;
        getOrderList();
    }

    private void getOrderList(){
        Call<ResultDO<List<Order>>> call = ApiClient.getApiAdapter().getOrderListNew(memberId,
                status,pageNo, Constants.PAGE_SIZE,orderType);
        call.enqueue(new Callback<ResultDO<List<Order>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<Order>>> call, Response<ResultDO<List<Order>>> response) {
                if(isFinish){
                    return ;
                }
                ptrFrame.refreshComplete();
                ResultDO<List<Order>> resultDO = response.body();
                if(resultDO!=null){
                    if(resultDO.getCode()==0){
                        if(pageNo==1){
                            dataSet.clear();
                        }
                        if(resultDO.getResult().size()== Constants.PAGE_SIZE){
                            isCanloadMore = true;
                            pageNo++;
                        }else{
                            isCanloadMore = false;
                        }
                        dataSet.addAll(resultDO.getResult());
                        if(dataSet.size()==0){
                            emptyLayout.setErrorType(EmptyLayout.NODATA);
                            emptyLayout.setNoDataContent("暂无订单");
                        }else{
                            emptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                        }
                        orderAdapter.notifyDataSetChanged();
                    }else{
                        emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                    }
                }else{
                    emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<Order>>> call, Throwable t) {
                ptrFrame.refreshComplete();
                emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
            }
        });
    }

}
