package com.bby.yishijie.member.ui.product;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bby.yishijie.R;
import com.bby.yishijie.member.common.AppConfig;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.http.AppOperator;
import com.bby.yishijie.shop.adapter.ProductMaterialAdapter;
import com.bby.yishijie.shop.entity.ProductMaterial;
import com.bby.yishijie.shop.event.UpdateProMateria;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.Target;
import com.sunday.common.base.BaseFragment;
import com.sunday.common.event.EventBus;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.MD5Utils;
import com.sunday.common.utils.StreamUtil;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.utils.download.ImgUtils;
import com.sunday.common.widgets.UIAlertView;
import com.sunday.common.widgets.ptr.PtrClassicFrameLayout;
import com.sunday.common.widgets.ptr.PtrDefaultHandler;
import com.sunday.common.widgets.ptr.PtrFrameLayout;
import com.sunday.common.widgets.ptr.PtrHandler;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by 刘涛 on 2017/5/4.
 */

public class OfficialMaterialFragment extends BaseFragment {


    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.ptr_frame)
    PtrClassicFrameLayout ptrFrame;

    private long productId;
    private int type;
    private long memberId;
    private ProductMaterialAdapter adapter;
    private List<ProductMaterial> dataSet = new ArrayList<>();
    private ClipboardManager myClipboard;
    private ClipData myClip;

    public static OfficialMaterialFragment newInstance(long productId, int type) {
        OfficialMaterialFragment fragment = new OfficialMaterialFragment();
        Bundle args = new Bundle();
        args.putLong("productId", productId);
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_product_list, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
        initParams();
        initViews();
        getData();
    }

    private void initParams() {
        if (getArguments() != null) {
            productId = getArguments().getLong("productId");
            type = getArguments().getInt("type");
        }
        if (type==0||BaseApp.getInstance().getShopMember()==null){
            memberId=0;
        }
        else {
            memberId=BaseApp.getInstance().getShopMember().getId();
        }
        myClipboard = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
    }

    private void initViews() {
        adapter = new ProductMaterialAdapter(mContext, dataSet, type);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext).
                drawable(R.drawable.horizontal_space_divider).build());
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

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProductMaterial item = (ProductMaterial) v.getTag();
                switch (v.getId()) {
                    case R.id.btn_save:
                        if (item.getImages() != null && item.getImages().size() > 0) {
                            for (String img : item.getImages()) {
                                final Future<File> future = getImageLoader()
                                        .load(img)
                                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);

                                AppOperator.runOnThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            File sourceFile = future.get();
                                            if (sourceFile == null || !sourceFile.exists())
                                                return;
                                            String extension = ImgUtils.getExtension(sourceFile.getAbsolutePath());
                                            String extDir = AppConfig.DEFAULT_SAVE_IMAGE_PATH;
                                            File extDirFile = new File(extDir);
                                            if (!extDirFile.exists()) {
                                                if (!extDirFile.mkdirs()) {
                                                    // If mk dir error
                                                    callSaveStatus(false, null);
                                                    return;
                                                }
                                            }
                                            final File saveFile = new File(extDirFile, String.format("IMG_%1$s%2$s.%3$s", System.currentTimeMillis(), MD5Utils.random(), extension));
                                            final boolean isSuccess = StreamUtil.copyFile(sourceFile, saveFile);
                                            callSaveStatus(isSuccess, saveFile);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            callSaveStatus(false, null);
                                        }
                                    }
                                });
                            }
                        }
                        break;
                    case R.id.btn_share:
                        if (type == 0) {
                            ((ProductDetailsActivity) getActivity()).showWindow();
                        } else {
                            final UIAlertView uiAlertView = new UIAlertView(mContext, "温馨提示", "确认删除吗", "取消", "确定");
                            uiAlertView.show();
                            uiAlertView.setClicklistener(new UIAlertView.ClickListenerInterface() {
                                @Override
                                public void doLeft() {
                                    uiAlertView.dismiss();
                                }

                                @Override
                                public void doRight() {
                                    delMaterial(item.getId());
                                }
                            });
                        }
                        break;
                    case R.id.btn_copy:
                        myClip = ClipData.newPlainText("text", item.getText());
                        myClipboard.setPrimaryClip(myClip);
                        ToastUtils.showToast(mContext, "已复制到粘贴板");
                        break;
                }
            }
        });
    }

    private void callSaveStatus(final boolean success, final File savePath) {
        ((ProductDetailsActivity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (success) {
                    // notify
                    Uri uri = Uri.fromFile(savePath);
                    mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                    Log.i("img_name------", String.valueOf(savePath));
                    ToastUtils.showToast(mContext, "图片保存成功");
                } else {
                    ToastUtils.showToast(mContext, "图片保存失败");
                }
            }
        });
    }

    protected RequestManager mImageLoader;

    public synchronized RequestManager getImageLoader() {
        if (mImageLoader == null)
            mImageLoader = Glide.with(this);
        return mImageLoader;
    }


    private void delMaterial(long id) {
        Call<ResultDO> call = ApiClient.getApiAdapter().delMaterial(id);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                if (isFinish) {
                    return;
                }
                if (response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    ToastUtils.showToast(mContext, "删除成功");
                    getData();
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {

            }
        });
    }

    private void getData() {
        Call<ResultDO<List<ProductMaterial>>> call = ApiClient.getApiAdapter().getProductMaterial(memberId, productId);
        call.enqueue(new Callback<ResultDO<List<ProductMaterial>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<ProductMaterial>>> call, Response<ResultDO<List<ProductMaterial>>> response) {
                if (isFinish) {
                    return;
                }
                ptrFrame.refreshComplete();
                if (response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    dataSet.clear();
                    dataSet.addAll(response.body().getResult());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<ProductMaterial>>> call, Throwable t) {
                ptrFrame.refreshComplete();
            }
        });
    }

    public void onEvent(UpdateProMateria updateProMateria) {
        getData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
