package com.bby.yishijie.member.ui.find;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.utils.PixUtils;
import com.sunday.common.utils.ToastUtils;
import com.bby.yishijie.R;
import com.bby.yishijie.member.entity.FindItem;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.product.ProductDetailActivity;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Author by Damon,  on 2017/12/15.
 */

public class FindItenDetailActivity extends BaseActivity {


    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.right_btn)
    ImageView rightBtn;

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.title_extra)
    TextView titleExtra;
    @Bind(R.id.img_grid_view)
    NineGridView imgGridView;
    @Bind(R.id.content)
    TextView content;
    @Bind(R.id.product_img)
    ImageView productImg;
    @Bind(R.id.product_title)
    TextView productTitle;
    @Bind(R.id.product_price)
    TextView productPrice;
    @Bind(R.id.product_layout)
    RelativeLayout productLayout;

    private FindItem findItem;
    private int appWidth;
    private long findId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_detail);
        ButterKnife.bind(this);
        titleView.setText("详情");
//        rightBtn.setVisibility(View.VISIBLE);
//        rightBtn.setImageResource(R.mipmap.find_share);
        findItem = (FindItem) getIntent().getSerializableExtra("findItem");
        findId=getIntent().getLongExtra("findId",0);
        appWidth = DeviceUtils.getDisplay(mContext).widthPixels;
        if (findId==0){
            updateView();
        }else {
            getDetail();
        }

    }

    private void updateView() {
        if (findItem == null) {
            return;
        }
        title.setText(findItem.getTitle());
        titleExtra.setText(findItem.getStatus() == 0 ? "好物平台发布  " + findItem.getCreateTime() : findItem.getName() + "  " + findItem.getCreateTime());
        ArrayList<ImageInfo> imageInfo = new ArrayList<>();
        List<String> imageDetails = findItem.getImages();
        if (imageDetails != null) {
            for (String imageDetail : imageDetails) {
                ImageInfo info = new ImageInfo();
                info.setThumbnailUrl(imageDetail);
                info.setBigImageUrl(imageDetail);
                imageInfo.add(info);
            }
        }
        imgGridView.setAdapter(new NineGridViewClickAdapter(mContext, imageInfo));
        imgGridView.setSingleImageSize(appWidth);
        imgGridView.setSingleImageRatio(2);
        content.setText(findItem.getDesc());

        productTitle.setText(findItem.getProductName());
        productPrice.setText(String.format("¥%s", findItem.getCurrentPrice().setScale(2, RoundingMode.HALF_UP)));
        if (!TextUtils.isEmpty(findItem.getProductImage())) {
            Glide.with(mContext)
                    .load(findItem.getProductImage())
                    .error(R.mipmap.ic_default)
                    .into(productImg);
        }

    }

    private void getDetail(){
        Call<ResultDO<FindItem>> call= ApiClient.getApiAdapter().getFindDetail(findId);
        call.enqueue(new Callback<ResultDO<FindItem>>() {
            @Override
            public void onResponse(Call<ResultDO<FindItem>> call, Response<ResultDO<FindItem>> response) {
                if (isFinish||response.body()==null){return;}
                if (response.body().getCode()==0){
                    if (response.body().getResult()==null){return;}
                    findItem=response.body().getResult();
                    updateView();
                }else {
                    ToastUtils.showToast(mContext,response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<FindItem>> call, Throwable t) {
                ToastUtils.showToast(mContext,R.string.network_error);
            }
        });
    }

    @OnClick({R.id.right_btn, R.id.product_layout})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_btn:

                break;
            case R.id.product_layout:
                Intent intent = new Intent(mContext, ProductDetailActivity.class);
                intent.putExtra("productId", findItem.getRefId());
                intent.putExtra("type", 1);//类型(1-普通商品 2-限时购)
                mContext.startActivity(intent);
                break;
        }
    }
}
