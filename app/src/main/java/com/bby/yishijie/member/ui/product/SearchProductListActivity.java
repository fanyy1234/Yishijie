package com.bby.yishijie.member.ui.product;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.widgets.ptr.PtrClassicFrameLayout;
import com.sunday.common.widgets.ptr.PtrDefaultHandler;
import com.sunday.common.widgets.ptr.PtrFrameLayout;
import com.sunday.common.widgets.ptr.PtrHandler;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;
import com.bby.yishijie.R;
import com.bby.yishijie.member.adapter.ProductListAdapter;
import com.bby.yishijie.member.entity.Product;
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
 * Created by 刘涛 on 2017/5/15.
 */

public class SearchProductListActivity extends BaseActivity {

    @Bind(R.id.btn_back)
    ImageView btnBack;
    @Bind(R.id.edit_query)
    EditText editQuery;


    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.ptr_frame)
    PtrClassicFrameLayout ptrFrame;

    private String name;
    private int pageNo = 1, lastVisibleItem;
    private boolean isCanloadMore;
    private LinearLayoutManager layoutManager;
    private ProductListAdapter adapter;
    private List<Product> dataSet = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initSearch();
        handlerRecyclerView();
        //getData();

    }

    private void handlerRecyclerView() {
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ProductListAdapter(mContext, dataSet);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .drawable(R.drawable.shape_divider)
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

    }


    private void getData() {
        Call<ResultDO<List<Product>>> call = ApiClient.getApiAdapter().getProductList(1,0, null,null, null, null, name, pageNo, Constants.PAGE_SIZE);
        call.enqueue(new Callback<ResultDO<List<Product>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<Product>>> call, Response<ResultDO<List<Product>>> response) {
                if (isFinish) {
                    return;
                }
                ptrFrame.refreshComplete();
                ResultDO<List<Product>> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }

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
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<Product>>> call, Throwable t) {
                ptrFrame.refreshComplete();
            }
        });
    }

    @OnClick(R.id.btn_back)
    void back() {
        onBackPressed();
    }

    @OnClick(R.id.btn_search)
    void search() {
        setKeyWord();
    }


    private void initSearch() {
        editQuery.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        editQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    setKeyWord();
                    return true;
                } else {
                    return false;
                }
            }

        });
    }

    private void setKeyWord() {
        name = editQuery.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(btnBack.getApplicationWindowToken(), 0);
        }
        pageNo = 1;
        getData();
    }

}
