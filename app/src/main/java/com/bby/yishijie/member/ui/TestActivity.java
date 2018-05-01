package com.bby.yishijie.member.ui;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.widgets.RecyclerTabLayout;
import com.bby.yishijie.R;
import com.bby.yishijie.member.entity.LimitTime;
import com.bby.yishijie.member.http.ApiClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2017/5/2.
 */

public class TestActivity extends BaseActivity {


    @Bind(R.id.recycler_tablayout)
    RecyclerTabLayout recyclerTablayout;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.view_pager)
    ViewPager viewPager;

    Tabadapter tabadapter;
    private List<LimitTime> timeList = new ArrayList<>();
    LimitTimeAdapter timeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        timeAdapter=new LimitTimeAdapter(timeList);
        viewPager.setAdapter(timeAdapter);
        recyclerTablayout.setAutoSelectionMode(true);
        recyclerTablayout.setUpWithAdapter(new Tabadapter(viewPager));
        getLimitTimes();

    }

    private void getLimitTimes() {
        Call<ResultDO<List<LimitTime>>> call = ApiClient.getApiAdapter().getLimitTimeList();
        call.enqueue(new Callback<ResultDO<List<LimitTime>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<LimitTime>>> call, Response<ResultDO<List<LimitTime>>> response) {
                if (isFinish) {
                    return;
                }
                ResultDO<List<LimitTime>> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    timeList.addAll(resultDO.getResult());
                    timeAdapter.notifyDataSetChanged();
                    recyclerTablayout.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<LimitTime>>> call, Throwable t) {

            }
        });
    }

    class Tabadapter extends RecyclerTabLayout.Adapter {

        private LimitTimeAdapter mAdapter;
        private List<LimitTime> dataSet = new ArrayList<>();

        public Tabadapter(List<LimitTime> datas) {
            super(datas);
            dataSet = datas;
        }
        public Tabadapter(ViewPager viewPager) {
            super(viewPager);
            mAdapter = (LimitTimeAdapter) mViewPager.getAdapter();
        }



        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.time_tab_item, parent,false);
            return new TabHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            TabHolder tabHolder = (TabHolder) holder;
            LimitTime item = mAdapter.getLimitTimeItem(position);
            tabHolder.timeTitle.setText(item.getStartTime() + "");
            tabHolder.timeExtra.setText(item.getTime());
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }

        class TabHolder extends RecyclerView.ViewHolder {

            @Bind(R.id.time_title)
            TextView timeTitle;
            @Bind(R.id.time_extra)
            TextView timeExtra;

            public TabHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = getAdapterPosition();

                    }
                });
            }
        }

    }

    class LimitTimeAdapter extends PagerAdapter{

        private List<LimitTime> dataSet=new ArrayList<>();

        public LimitTimeAdapter(List<LimitTime> datas){
            dataSet=datas;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view=LayoutInflater.from(mContext).inflate(R.layout.test_layout,container,false);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return dataSet.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        public LimitTime getLimitTimeItem(int position){
            return dataSet.get(position);
        }

        public void setDataSet(List<LimitTime> datas){
            dataSet=new ArrayList<>(datas);
        }

    }




}
