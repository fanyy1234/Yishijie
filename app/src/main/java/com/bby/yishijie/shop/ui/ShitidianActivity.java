package com.bby.yishijie.shop.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.MainActivity;
import com.bby.yishijie.member.utils.EntityUtil;
import com.bby.yishijie.shop.adapter.CommonAdapter;
import com.bby.yishijie.shop.adapter.ViewHolder;
import com.bby.yishijie.shop.entity.IntegralDetail;
import com.bby.yishijie.shop.entity.Member;
import com.bby.yishijie.shop.entity.Shitidian;
import com.bumptech.glide.Glide;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.SharePerferenceUtils;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 刘涛 on 2017/9/29.
 */

public class ShitidianActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.rightTxt)
    TextView rightTxt;
    @Bind(R.id.right_btn)
    ImageView rightBtn;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private int score;
    private List<Shitidian> dataSet = new ArrayList<>();
    private long memberId;
    private CommonAdapter<Shitidian> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shitidian);
        ButterKnife.bind(this);
        titleView.setText("实体店");
        if (MainActivity.isShop){
            memberId = BaseApp.getInstance().getShopMember().getId();
        }
        else {
            memberId = BaseApp.getInstance().getMember().getId();
        }

        initRecyclerView();
        getData();
    }

    private LinearLayoutManager layoutManager;

    private void initRecyclerView() {
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CommonAdapter<Shitidian>(mContext, R.layout.item_shitidian, dataSet) {
            @Override
            public void convert(ViewHolder holder, Shitidian balanceDetail) {
                bind(holder, balanceDetail);
            }
        };
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .drawable(R.drawable.shape_divider_width)
                .build());

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE
//                        && lastVisibleItem + 1 == adapter.getItemCount() && isCanloadMore) {
////                    getData();
//                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
    }


    private void bind(ViewHolder holder, Shitidian balanceDetail) {

        ((TextView) holder.getView(R.id.name)).setText(balanceDetail.getName());
        ((TextView) holder.getView(R.id.mobile)).setText(balanceDetail.getMobile());
        ((TextView) holder.getView(R.id.address)).setText(balanceDetail.getAddress());

        Glide.with(mContext)
                .load(balanceDetail.getLogo())
                .error(R.mipmap.ic_default)
                .into(((ImageView) holder.getView(R.id.shitidian_img)));
    }

    private int lastVisibleItem;

    private void getData() {
        showLoadingDialog(0);
        Call<ResultDO<List<Shitidian>>> call = ApiClient.getApiAdapter().shitidian();
        call.enqueue(new Callback<ResultDO<List<Shitidian>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<Shitidian>>> call, Response<ResultDO<List<Shitidian>>> response) {
                if (isFinish) {
                    return;
                }
                dissMissDialog();
                ResultDO<List<Shitidian>> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    if (resultDO.getResult() == null) {
                        return;
                    }
                    dataSet.addAll(resultDO.getResult());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<Shitidian>>> call, Throwable t) {
                dissMissDialog();
            }
        });
    }
}
