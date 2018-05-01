package com.bby.yishijie.member.ui.mine;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.EmptyLayout;
import com.sunday.common.widgets.ptr.PtrClassicFrameLayout;
import com.sunday.common.widgets.ptr.PtrDefaultHandler;
import com.sunday.common.widgets.ptr.PtrFrameLayout;
import com.sunday.common.widgets.ptr.PtrHandler;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;
import com.bby.yishijie.R;
import com.bby.yishijie.member.adapter.CommonAdapter;
import com.bby.yishijie.member.adapter.ViewHolder;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.entity.Message;
import com.bby.yishijie.member.http.ApiClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2017/6/16.
 */

public class MessageListActivity extends BaseActivity {

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

    private CommonAdapter<Message> adapter;
    private List<Message> dataSet=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        ButterKnife.bind(this);
        titleView.setText("消息盒子");
        initRecyclerView();
        getData();

    }

    private void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setPadding(20,20,20,0);
        adapter = new CommonAdapter<Message>(mContext,R.layout.list_item_message,dataSet) {
            @Override
            public void convert(ViewHolder holder, Message message) {
                bind(holder,message);
            }
        };
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .margin(20)
                .build());
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

        emptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });

    }


    private void getData(){
        Call<ResultDO<List<Message>>> call= ApiClient.getApiAdapter().getMessageList(BaseApp.getInstance().getMember().getId(),1,1,0);
        call.enqueue(new Callback<ResultDO<List<Message>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<Message>>> call, Response<ResultDO<List<Message>>> response) {
                if (isFinish||response.body()==null){return;}
                ptrFrame.refreshComplete();
                if (response.body().getCode()==0){
                    dataSet.clear();
                    dataSet.addAll(response.body().getResult());
                    adapter.notifyDataSetChanged();
                    if (dataSet.size()>0){
                        emptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                    }else {
                        emptyLayout.setErrorType(EmptyLayout.NODATA);
                    }
                }else {
                    ToastUtils.showToast(mContext,response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<Message>>> call, Throwable t) {
                ptrFrame.refreshComplete();
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });

    }

    private void bind(ViewHolder holder, Message message){
//        ((TextView)(holder.getView(R.id.message_title))).setText(message.getType()==null?"系统通知":
//                message.getType()==1?"订单支付成功通知":"订单发货通知");
        ((TextView) (holder.getView(R.id.message_title))).setText( "系统通知");
        ((TextView)(holder.getView(R.id.message_time))).setText(message.getTime());
        ((TextView)(holder.getView(R.id.message_content))).setText(message.getMessage());
    }

}
