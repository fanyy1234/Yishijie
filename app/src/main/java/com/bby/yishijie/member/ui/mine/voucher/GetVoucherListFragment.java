package com.bby.yishijie.member.ui.mine.voucher;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bby.yishijie.member.ui.MainActivity;
import com.bumptech.glide.Glide;
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
import com.bby.yishijie.member.adapter.CommonAdapter;
import com.bby.yishijie.member.adapter.ProductListAdapter;
import com.bby.yishijie.member.adapter.ViewHolder;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.entity.Product;
import com.bby.yishijie.member.entity.Voucher;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.base.BaseLazyFragment;
import com.bby.yishijie.member.ui.index.ProductListFragment;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Author by Damon,  on 2017/12/27.
 */

public class GetVoucherListFragment extends BaseLazyFragment {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.ptr_frame)
    PtrClassicFrameLayout ptrFrame;
    @Bind(R.id.empty_layout)
    EmptyLayout emptyLayout;


    private int pageNo = 1, lastVisibleItem;

    private List<Voucher> dataSet = new ArrayList<>();
    private CommonAdapter<Voucher> adapter;
    private boolean isCanloadMore;
    private LinearLayoutManager layoutManager;
    private long memberId;

    public static GetVoucherListFragment newInstance() {
        GetVoucherListFragment fragment = new GetVoucherListFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isPrepared = true;
        lazyLoad();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order_list, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        if (!isPrepared || !isVisible) {
            return;
        }
        initRefreshLayout();
        isPrepared = false;
        if (MainActivity.isShop){
            memberId = BaseApp.getInstance().getShopMember().getId();
        }
        else {
            memberId = BaseApp.getInstance().getMember().getId();
        }

        ptrFrame.autoRefresh();
    }

    private void initRefreshLayout() {
        adapter = new CommonAdapter<Voucher>(mContext, R.layout.list_new_voucher_item, dataSet) {
            @Override
            public void convert(ViewHolder holder, final Voucher voucher) {
                RelativeLayout rl = holder.getView(R.id.rl_voucher);
                ImageView voucherImg = holder.getView(R.id.voucher_img);
                TextView voucherMoney = holder.getView(R.id.voucher_money);
                TextView voucherTitle = holder.getView(R.id.voucher_title);
                TextView voucherMoneyExtra = holder.getView(R.id.voucher_money_extra);
                TextView voucherTime = holder.getView(R.id.voucher_time);
                TextView voucherBtn = holder.getView(R.id.voucher_btn);
                ImageView voucherImgFlag = holder.getView(R.id.img_got_flag);
                Glide.with(mContext)
                        .load(voucher.getImage())
                        .error(R.mipmap.ic_default)
                        .into(voucherImg);
                voucherMoney.setText("¥" + voucher.getMoney().setScale(2, RoundingMode.HALF_UP));
                voucherTitle.setText(voucher.getName());
                voucherMoneyExtra.setText(String.format("满%s元使用", voucher.getAmount().setScale(0, RoundingMode.HALF_UP)));
                if (voucher.getType() == 1) {
                    voucherTime.setText("有效期：" + voucher.getStartDate() + "至" + voucher.getEndDate());
                } else {
                    voucherTime.setText("有效期：" + voucher.getDayCount() + "天");
                }

                rl.setBackgroundResource(voucher.getCanReceive() == 1 ? R.mipmap.voucher_item : R.mipmap.voucher_item1);
                voucherImgFlag.setVisibility(voucher.getCanReceive() == 1 ? View.GONE : View.VISIBLE);
                holder.setOnClickListener(R.id.voucher_btn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (voucher.getCanReceive() == 1) {
                            getVoucher(voucher);
                        }
                    }
                });
            }
        };
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setBackgroundColor(Color.parseColor("#f5f4f9"));
        recyclerView.setPadding(10, 10, 10, 10);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .drawable(R.drawable.shape_divider_width)
                .build());
        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pageNo = 1;
                getData();
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
                        && lastVisibleItem + 1 == adapter.getItemCount() && isCanloadMore) {
                    getData();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
        emptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNo = 1;
                getData();
            }
        });
    }

    private void getVoucher(Voucher voucher) {
        showLoadingDialog(0);
        Call<ResultDO> call = ApiClient.getApiAdapter().receiveVoucher(memberId, voucher.getId());
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                if (isFinish) {
                    return;
                }
                dissMissDialog();
                ResultDO resultDO = response.body();
                if (resultDO != null) {
                    if (resultDO.getCode() == 0) {
                        ToastUtils.showToast(mContext, "领取成功");
                        pageNo = 1;
                        getData();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }


    private void getData() {
        Call<ResultDO<List<Voucher>>> call = ApiClient.getApiAdapter().getAllVoucher(memberId, pageNo, Constants.PAGE_SIZE);
        call.enqueue(new Callback<ResultDO<List<Voucher>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<Voucher>>> call, Response<ResultDO<List<Voucher>>> response) {
                if (isFinish) {
                    return;
                }
                ptrFrame.refreshComplete();
                ResultDO<List<Voucher>> resultDO = response.body();
                if (resultDO != null) {
                    if (resultDO.getCode() == 0) {
                        if (resultDO.getResult() == null) {
                            return;
                        }
                        if (pageNo == 1) {
                            dataSet.clear();
                        }
                        if (resultDO.getResult().size() == Constants.PAGE_SIZE) {
                            isCanloadMore = true;
                            pageNo++;
                        } else {
                            isCanloadMore = false;
                        }
                        dataSet.addAll(resultDO.getResult());
                        if (dataSet.size() == 0) {
                            emptyLayout.setErrorType(EmptyLayout.NODATA);
                            emptyLayout.setNoDataContent("暂无数据");
                        } else {
                            emptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                    }
                } else {
                    emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<Voucher>>> call, Throwable t) {
                ptrFrame.refreshComplete();
                emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
            }
        });
    }


}
