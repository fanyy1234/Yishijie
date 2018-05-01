package com.bby.yishijie.member.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.UIAlertView;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;
import com.bby.yishijie.R;
import com.bby.yishijie.member.adapter.AddressListAdapter;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.entity.Address;
import com.bby.yishijie.member.http.ApiClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2017/4/20.
 */

public class AddressListActivity extends BaseActivity {


    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.rightTxt)
    TextView rightTxt;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private long memberId;
    private List<Address> dataSet=new ArrayList<>();
    private AddressListAdapter adapter;
    private boolean isSelectMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addr_list);
        ButterKnife.bind(this);
        initView();
        getData();
    }

    private void initView(){
        memberId= BaseApp.getInstance().getMember().getId();
        isSelectMode=getIntent().getBooleanExtra("isSelectMode",false);
        titleView.setText("我的收货地址");
        rightTxt.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter=new AddressListAdapter(mContext,dataSet);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .drawable(R.drawable.horizontal_space_divider)
                .build());
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.edite:
                        int p= (int) v.getTag();
                        Address address=dataSet.get(p);
                        intent=new Intent(mContext,AddAddressActivity.class);
                        intent.putExtra("address",address);
                        startActivity(intent);
                        break;
                    case R.id.delete:
                        final int p2= (int) v.getTag();
                        final Address address2=dataSet.get(p2);
                        final UIAlertView dialog=new UIAlertView(mContext,"温馨提示","确实删除此收货地址吗","取消","确定");
                        dialog.show();
                        dialog.setClicklistener(new UIAlertView.ClickListenerInterface() {
                            @Override
                            public void doLeft() {
                                dialog.dismiss();
                            }

                            @Override
                            public void doRight() {
                                delAddr(address2,p2);
                                dialog.dismiss();
                            }
                        });
                        break;
                    case R.id.choice:
                        int p3= (int) v.getTag();
                        Address address3=dataSet.get(p3);
                        if (address3.getIsDeleted()==2){
                            return;
                        }else {
                            setDefaultAddr(address3,p3);
                        }
                        break;
                    case R.id.address_layout:
                        if (isSelectMode){
                            Address address1= (Address) v.getTag();
                            Intent data=new Intent();
                            data.putExtra("address",address1);
                            setResult(RESULT_OK,data);
                            finish();
                        }
                        break;
                }
            }
        });
    }

    private void delAddr(Address address,final int p){
        showLoadingDialog(0);
        Call<ResultDO> call=ApiClient.getApiAdapter().delAddr(address.getId());
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                dissMissDialog();
                if (response.body()==null){
                    return;
                }
                if (response.body().getCode()==0){
                    dataSet.remove(p);
                    adapter.notifyDataSetChanged();
                    ToastUtils.showToast(mContext,"删除成功");
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext,R.string.network_error);

            }
        });
    }

    private void setDefaultAddr(Address address,final int p){
        showLoadingDialog(0);
        Call<ResultDO> call=ApiClient.getApiAdapter().setAddrDefault(memberId,address.getId());
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                dissMissDialog();
                if (response.body()==null){
                    return;
                }
                if (response.body().getCode()==0){
                    for (Address item:dataSet){
                        item.setIsDeleted(0);
                    }
                    dataSet.get(p).setIsDeleted(2);
                    adapter.notifyDataSetChanged();
                }else {
                    ToastUtils.showToast(mContext,response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                ToastUtils.showToast(mContext,R.string.network_error);
            }
        });

    }



    @OnClick(R.id.add_addr)
    void addAddr(){
        intent=new Intent(mContext,AddAddressActivity.class);
        startActivity(intent);
    }

    private void getData(){
        showLoadingDialog(0);
        Call<ResultDO<List<Address>>> call= ApiClient.getApiAdapter().getAddrList(memberId);
        call.enqueue(new Callback<ResultDO<List<Address>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<Address>>> call, Response<ResultDO<List<Address>>> response) {
                if (isFinish){return;}
                dissMissDialog();
                ResultDO<List<Address>> resultDO=response.body();
                if (resultDO==null){return;}
                if (resultDO.getCode()==0){
                    if (resultDO.getResult()==null){return;}
                    dataSet.clear();
                    dataSet.addAll(resultDO.getResult());
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<Address>>> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext,R.string.network_error);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }
}
