package com.bby.yishijie.member.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.StringUtils;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.ClearEditText;
import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.entity.Address;
import com.bby.yishijie.member.entity.City;
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
 * Created by 刘涛 on 2017/4/21.
 */

public class AddAddressActivity extends BaseActivity {


    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.rightTxt)
    TextView rightTxt;
    @Bind(R.id.tv_name)
    ClearEditText tvName;
    @Bind(R.id.tv_phone)
    ClearEditText tvPhone;
    @Bind(R.id.tv_place)
    TextView tvPlace;
    @Bind(R.id.tv_finalAddress)
    ClearEditText tvFinalAddress;

    private List<City> list = new ArrayList<>();//省份
    private String provinces[];

    private Integer provinceId,cityId,districtId,addressId=null;

    private long memberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        ButterKnife.bind(this);
        initView();
       // getProvinceList();

    }

    private void initView() {
        memberId= BaseApp.getInstance().getMember().getId();
        Address address = (Address) getIntent().getSerializableExtra("address");
        if (address ==null) {
            titleView.setText("添加地址");
        } else {
            titleView.setText("编辑地址");
            tvName.setText(address.getName());
            tvPlace.setText(address.getCityDetail());
            tvPhone.setText(address.getMobile());
            tvFinalAddress.setText(address.getAddress());
            provinceId= address.getProvinceId();
            cityId= address.getCityId();
            districtId= address.getDistrictId();
            addressId= address.getId();
        }
        rightTxt.setText("确认");
    }

    private void getProvinceList(){
        showLoadingDialog(0);
        Call<ResultDO<List<City>>> call = ApiClient.getApiAdapter().getProvinces();
        call.enqueue(new Callback<ResultDO<List<City>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<City>>> call, Response<ResultDO<List<City>>> response) {
                dissMissDialog();
                if (response.body() == null || isFinish) {
                    return;
                }
                if (response.body().getCode() == 0 && response.body().getResult() != null) {
                    list.addAll(response.body().getResult());
                    provinces = new String[list.size()];
                    for(int i=0 ;i<list.size();i++){
                        provinces[i] = list.get(i).getValue();
                    }
                    if(list.size() >0){
                        provinceId = list.get(0).getId();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<City>>> call, Throwable t) {
                dissMissDialog();
            }
        });

    }

    @OnClick({R.id.rightTxt, R.id.tv_place})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.rightTxt:
                saveAddr();
                break;
            case R.id.tv_place:
                intent=new Intent(mContext,SelectProvinceActivity.class);
                intent.putExtra("type",1);
                intent.putExtra("fromUserInfo",false);
                startActivityForResult(intent,0x112);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode!=RESULT_OK){
            return;
        }
        City provinceItem = (City) data.getSerializableExtra("province");
        provinceId= provinceItem.getId();
        String provinceName = provinceItem.getValue();
        City cityItem = (City) data.getSerializableExtra("city");
        cityId= cityItem.getId();
        String cityName = cityItem.getValue();
        City districtItem = (City) data.getSerializableExtra("district");
        districtId= districtItem.getId();
        String districtName = districtItem.getValue();
        tvPlace.setText(provinceName +" "+ cityName +" "+ districtName);
    }

    private void saveAddr(){
        String userName=tvName.getText().toString().trim();
        String mobileNo=tvPhone.getText().toString().trim();
        String addrDetail=tvFinalAddress.getText().toString().trim();
        String addr=tvPlace.getText().toString().trim();
        if (TextUtils.isEmpty(userName)){
            ToastUtils.showToast(mContext,"请输入收货人姓名");
            return;
        }
        if (!StringUtils.isMobileNO(mobileNo)){
            ToastUtils.showToast(mContext,"请输入正确手机号");
            return;
        }
        if (TextUtils.isEmpty(addrDetail)||TextUtils.isEmpty(addr)){
            ToastUtils.showToast(mContext,"请填写具体收货地址");
            return;
        }
        showLoadingDialog(0);
        Call<ResultDO> call=ApiClient.getApiAdapter().saveAddr(memberId,provinceId,cityId,districtId,userName
        ,mobileNo,addrDetail,addr,addressId);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                if (isFinish){return;}
                dissMissDialog();
                if (response.body()==null){return;}
                if (response.body().getCode()==0){
                    ToastUtils.showToast(mContext,"保存成功");
                    finish();
                }else{
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



}
