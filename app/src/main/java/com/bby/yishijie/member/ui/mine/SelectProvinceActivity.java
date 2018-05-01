package com.bby.yishijie.member.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;
import com.bby.yishijie.R;
import com.bby.yishijie.member.adapter.CommonAdapter;
import com.bby.yishijie.member.adapter.ViewHolder;
import com.bby.yishijie.member.entity.City;
import com.bby.yishijie.member.http.ApiClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



/**
 * Created by 刘涛 on 2017/5/16.
 * 选择省市区  包含地址选择和个人中心地区选择
 */

public class SelectProvinceActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;


    private CommonAdapter adapter;
    private List<City> provinceList = new ArrayList<>();
    private Integer provinceId, cityId, districtId, addressId = null;
    private String provinceName, cityName, districtName;
    private int type;
    private City selctItem;
    private boolean fromUserInfo;//区分收货地址选择和个人地区选择
    private City province, city, district;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_recycler_list);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", 0);
        selctItem = (City) getIntent().getSerializableExtra("selctItem");
        province= (City) getIntent().getSerializableExtra("province");
        fromUserInfo = getIntent().getBooleanExtra("fromUserInfo", false);
        initView();
        initRecyclerView();
    }

    private void initView() {
        if (type == 1) {
            titleView.setText("选择省份");
            getProvinceList();
        } else if (type == 2) {
            titleView.setText("选择城市");
            getCityList();
        } else {
            titleView.setText("选择地区");
            getDistrictList();
        }

    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext).drawable(R.drawable.shape_divider1).build());
        adapter = new CommonAdapter<City>(mContext, R.layout.list_city_item, provinceList) {
            @Override
            public void convert(ViewHolder holder, final City cityItem) {
                ((TextView) holder.getView(R.id.city_name)).setText(cityItem.getValue());
                holder.setOnClickListener(R.id.city_name, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent = new Intent(mContext, SelectProvinceActivity.class);
                        intent.putExtra("selctItem", cityItem);
                        switch (type) {
                            case 1:
                                province = cityItem;
                                intent.putExtra("type", 2);
                                intent.putExtra("fromUserInfo", fromUserInfo);
                                startActivityForResult(intent,0x1111);
                                break;
                            case 2:
                                city = cityItem;
                                if (fromUserInfo) {
                                    Intent intent=new Intent();
                                    intent.putExtra("province",selctItem);
                                    intent.putExtra("city",cityItem);
                                    setResult(RESULT_OK,intent);
                                    finish();
                                } else {
                                    intent.putExtra("type", 3);
                                    intent.putExtra("province",selctItem);
                                    startActivityForResult(intent,0x1112);
                                }
                                break;
                            case 3:
                                district = cityItem;
                                Intent intent = new Intent();
                                intent.putExtra("province", province);
                                intent.putExtra("city", selctItem);
                                intent.putExtra("district", cityItem);
                                setResult(RESULT_OK, intent);
                                finish();
                                break;
                        }
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode!=RESULT_OK){
            return;
        }
        switch (requestCode){
            case 0x1111:
                Intent intent=new Intent();
                province= (City) data.getSerializableExtra("province");
                city= (City) data.getSerializableExtra("city");
                district= (City) data.getSerializableExtra("district");
                intent.putExtra("province", province);
                intent.putExtra("city", city);
                if (district!=null){
                    intent.putExtra("district", district);
                }
                setResult(RESULT_OK,intent);
                finish();
                break;
            case 0x1112:
                Intent intent1=new Intent();
                province= (City) data.getSerializableExtra("province");
                city= (City) data.getSerializableExtra("city");
                district= (City) data.getSerializableExtra("district");
                intent1.putExtra("province", province);
                intent1.putExtra("city", city);
                intent1.putExtra("district", district);
                setResult(RESULT_OK,intent1);
                finish();
                break;
        }

    }

    private void getProvinceList() {
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
                    provinceList.clear();
                    provinceList.addAll(response.body().getResult());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<City>>> call, Throwable t) {
                dissMissDialog();
            }
        });

    }


    private void getCityList() {
        showLoadingDialog(0);
        Call<ResultDO<List<City>>> call = ApiClient.getApiAdapter().getCitys(selctItem.getId());
        call.enqueue(new Callback<ResultDO<List<City>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<City>>> call, Response<ResultDO<List<City>>> response) {
                dissMissDialog();
                if (response.body() == null || isFinish) {
                    return;
                }
                if (response.body().getCode() == 0 && response.body().getResult() != null) {
                    provinceList.clear();
                    provinceList.addAll(response.body().getResult());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<City>>> call, Throwable t) {
                dissMissDialog();
            }
        });
    }

    private void getDistrictList() {
        showLoadingDialog(0);
        Call<ResultDO<List<City>>> call = ApiClient.getApiAdapter().getDistricts(selctItem.getId());
        call.enqueue(new Callback<ResultDO<List<City>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<City>>> call, Response<ResultDO<List<City>>> response) {
                dissMissDialog();
                if (response.body() == null || isFinish) {
                    return;
                }
                if (response.body().getCode() == 0 && response.body().getResult() != null) {
                    provinceList.clear();
                    provinceList.addAll(response.body().getResult());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<City>>> call, Throwable t) {
                dissMissDialog();
            }
        });
    }

}
