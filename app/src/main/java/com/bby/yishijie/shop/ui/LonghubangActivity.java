package com.bby.yishijie.shop.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.shop.adapter.CommonAdapter;
import com.bby.yishijie.shop.adapter.ViewHolder;
import com.bby.yishijie.shop.entity.Longhubang;
import com.bumptech.glide.Glide;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.widgets.CircleImageView;
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

public class LonghubangActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.rightTxt)
    TextView rightTxt;
    @Bind(R.id.right_btn)
    ImageView rightBtn;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.name2)
    TextView name2;
    @Bind(R.id.name1)
    TextView name1;
    @Bind(R.id.name3)
    TextView name3;

    @Bind(R.id.img2)
    CircleImageView img2;
    @Bind(R.id.img1)
    CircleImageView img1;
    @Bind(R.id.img3)
    CircleImageView img3;

    @Bind({R.id.img1, R.id.img2, R.id.img3})
    CircleImageView[] imgArr;
    @Bind({R.id.name1, R.id.name2, R.id.name3})
    TextView[] nameArr ;
    private List<Longhubang> dataSet = new ArrayList<>();
    private CommonAdapter<Longhubang> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_longhubang);
        ButterKnife.bind(this);
        titleView.setText("龙虎榜");
        initRecyclerView();

        getData();
    }

    private LinearLayoutManager layoutManager;

    private void initRecyclerView() {
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CommonAdapter<Longhubang>(mContext, R.layout.item_longhubang, dataSet) {
            @Override
            public void convert(ViewHolder holder, Longhubang balanceDetail) {
                bind(holder, balanceDetail);
            }
        };
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .drawable(R.drawable.shape_divider_width1)
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


    private void bind(ViewHolder holder, Longhubang balanceDetail) {

        ((TextView) holder.getView(R.id.name)).setText(balanceDetail.getShopName());
        ((TextView) holder.getView(R.id.paiming)).setText(balanceDetail.getPaiming() + "");

        Glide.with(mContext)
                .load(balanceDetail.getLogo())
                .error(R.mipmap.ic_default)
                .into(((CircleImageView) holder.getView(R.id.logo)));
    }

    private int lastVisibleItem;

    private void getData() {
        showLoadingDialog(0);
        Call<ResultDO<List<Longhubang>>> call = ApiClient.getApiAdapter().longhubang();
        call.enqueue(new Callback<ResultDO<List<Longhubang>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<Longhubang>>> call, Response<ResultDO<List<Longhubang>>> response) {
                if (isFinish) {
                    return;
                }
                dissMissDialog();
                ResultDO<List<Longhubang>> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    if (resultDO.getResult() == null) {
                        return;
                    }
                    List<Longhubang> longhubangs = resultDO.getResult();
                    int size = longhubangs.size();

                    if (size > 3) {
                        for (int i = 0; i < 3; i++) {
                            nameArr[i].setText(longhubangs.get(i).getShopName());
                            Glide.with(mContext)
                                    .load(longhubangs.get(i).getLogo())
                                    .into(imgArr[i]);
                        }
                        for (int i = 3; i < size; i++) {
                            Longhubang longhubang = new Longhubang();
                            Longhubang theLonghubang = longhubangs.get(i);
                            longhubang.setPaiming(i + 1);
                            longhubang.setLogo(theLonghubang.getLogo());
                            longhubang.setShopName(theLonghubang.getShopName());
                            dataSet.add(longhubang);
                        }
                    } else {
                        for (int i = 0; i < size; i++) {
                            nameArr[i].setText(longhubangs.get(i).getShopName());
                            Glide.with(mContext)
                                    .load(longhubangs.get(i).getLogo())
                                    .into(imgArr[i]);
                        }
                        dataSet.clear();
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<Longhubang>>> call, Throwable t) {
                dissMissDialog();
            }
        });
    }
}
