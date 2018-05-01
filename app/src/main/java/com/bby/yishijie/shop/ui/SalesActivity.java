package com.bby.yishijie.shop.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.shop.adapter.CommonAdapter;
import com.bby.yishijie.shop.adapter.ViewHolder;
import com.bby.yishijie.shop.entity.IncomeRecord;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.EmptyLayout;
import com.sunday.common.widgets.pickerview.TimePickerView;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;
import com.sunday.common.widgets.signcalendar.DateUtil;

import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 刘涛 on 2017/5/14.
 * <p>
 * 销售管理/销售统计
 */

public class SalesActivity extends BaseActivity {


    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.rightTxt)
    TextView rightTxt;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.empty_layout)
    EmptyLayout emptyLayout;
    @Bind(R.id.start_time)
    TextView start_Time;
    @Bind(R.id.end_time)
    TextView end_Time;


    private long memberId;
    private String startTime, endTime;
    private CommonAdapter adapter;
    private List<IncomeRecord> dataSet = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_statistic);
        ButterKnife.bind(this);
        rightTxt.setVisibility(View.GONE);
        titleView.setText("销售统计");
        memberId = BaseApp.getInstance().getMember().getId();
        emptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        initRecylerView();
        getData();
    }

    private boolean isStart;//是否选择开始时间
    private TimePickerView timePickerView;
    private Date start, end;

    @OnClick({R.id.start_time, R.id.end_time})
    void onClick(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        switch (v.getId()) {
            case R.id.start_time:
                isStart = true;
                break;
            case R.id.end_time:
                isStart = false;
                break;
        }
        timePickerView = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);
        timePickerView.setTime(new Date());
        timePickerView.setCyclic(true);
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
                if (isStart) {
                    startTime = sdf.format(date);
                    start = date;
                    if (!TextUtils.isEmpty(endTime) && !DateUtil.compareDate(start, end)) {
                        ToastUtils.showToast(mContext, "请选择正确时间段");
                    } else if (!TextUtils.isEmpty(endTime) && DateUtil.compareDate(start, end)) {
                        start_Time.setText(startTime);
                        getData();
                        timePickerView.dismiss();
                    } else {
                        start_Time.setText(startTime);
                        timePickerView.dismiss();
                    }
                } else {
                    endTime = sdf.format(date);
                    end = date;
                    if (!TextUtils.isEmpty(startTime) && !DateUtil.compareDate(start, end)) {
                        ToastUtils.showToast(mContext, "请选择正确时间段");

                    } else if (!TextUtils.isEmpty(startTime) && DateUtil.compareDate(start, end)) {
                        timePickerView.dismiss();
                        end_Time.setText(endTime);
                        getData();
                    } else {
                        end_Time.setText(endTime);
                        timePickerView.dismiss();
                    }
                }

            }
        });
        timePickerView.show();
    }

    private void getData() {
        showLoadingDialog(0);
        Call<ResultDO<List<IncomeRecord>>> call = ApiClient.getApiAdapter().getIncomeRecord(memberId, startTime, endTime);
        call.enqueue(new Callback<ResultDO<List<IncomeRecord>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<IncomeRecord>>> call, Response<ResultDO<List<IncomeRecord>>> response) {
                if (isFinish || response.body() == null) {
                    return;
                }
                dissMissDialog();
                if (response.body().getCode() == 0) {
                    dataSet.clear();
                    dataSet.addAll(response.body().getResult());
                    if (dataSet.size() > 0) {
                        emptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                    } else {
                        emptyLayout.setErrorType(EmptyLayout.NODATA);
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<ResultDO<List<IncomeRecord>>> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }

    private void initRecylerView() {
        emptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext).
                drawable(R.drawable.shape_divider).build());
        adapter = new CommonAdapter<IncomeRecord>(mContext, R.layout.customer_profit_item, dataSet) {
            @Override
            public void convert(ViewHolder holder, IncomeRecord item) {
                ((TextView) holder.getView(R.id.name)).setText(item.getTime());
                ((TextView) holder.getView(R.id.identify)).setText(String.format("%s", item.getOrderFee().setScale(2, RoundingMode.HALF_UP)));
                ((TextView) holder.getView(R.id.profit)).setText(String.format("%s", item.getScale().setScale(2, RoundingMode.HALF_UP)));
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
