package com.bby.yishijie.member.ui.product;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.shop.event.UpdateProMateria;
import com.squareup.picasso.Picasso;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.event.EventBus;
import com.sunday.common.imageselector.MultiImageSelectorActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.ImageUtils;
import com.sunday.common.utils.StringUtils;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.utils.UploadUtils;
import com.sunday.common.widgets.ClearEditText;
import com.sunday.common.widgets.NoScrollGridView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 刘涛 on 2017/5/4.
 */

public class EditProductMaterialActivity extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.left_btn)
    ImageView leftBtn;
//    @Bind(R.id.left_txt)
//    TextView leftTxt;
    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.rightTxt)
    TextView rightTxt;
    @Bind(R.id.right_btn)
    ImageView rightBtn;
    @Bind(R.id.common_header)
    RelativeLayout commonHeader;
    @Bind(R.id.edit_content)
    ClearEditText editContent;
    @Bind(R.id.grid_view)
    NoScrollGridView gridView;
    @Bind(R.id.product_name)
    TextView txtProductName;

    private long productId;
    private String productName;
    private ArrayList<String> imgList = new ArrayList<>();
    private GridAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_material);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        productId = getIntent().getLongExtra("productId", 0);
        productName = getIntent().getStringExtra("productName");
        txtProductName.setText(productName);
        leftBtn.setVisibility(View.VISIBLE);
//        leftTxt.setVisibility(View.VISIBLE);
//        leftTxt.setText("取消");
        titleView.setText("文案编辑");
        rightBtn.setVisibility(View.GONE);
        rightTxt.setVisibility(View.VISIBLE);
        rightTxt.setText("发布");
        adapter = new GridAdapter(imgList);
        gridView.setAdapter(adapter);
        adapter.setOnClickListener(this);
//        leftTxt.setOnClickListener(this);
        rightTxt.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img1:
                int position = (int) v.getTag();
                if (imgList.size() == position) {
                    selectImg();
                }
                break;
            case R.id.img11:
                int p = (int) v.getTag();
                imgList.remove(p);
                adapter.notifyDataSetChanged();
                break;
            case R.id.left_btn:
                finish();
                break;
            case R.id.rightTxt:
                String content = editContent.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    ToastUtils.showToast(mContext, "请输入文案内容");
                    return;
                }
                if (imgList != null && imgList.size() > 0) {
                    uploadImg();
                } else {
                    ToastUtils.showToast(mContext, "请添加文案图片");
                    return;
                }
                break;
        }
    }

    private void selectImg() {
        intent = new Intent(mContext, MultiImageSelectorActivity.class);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        if (imgList != null && imgList.size() > 0) {
            intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, imgList);
        }
        startActivityForResult(intent, 0x1111);
    }

    List<String> netImgs = new ArrayList<>();

    private void uploadImg() {
        showLoadingDialog(0);
        Call<ResultDO<List<String>>> call = ApiClient.getApiAdapter().uploadImgs(UploadUtils.getRequestBody(imgList));
        call.enqueue(new Callback<ResultDO<List<String>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<String>>> call, Response<ResultDO<List<String>>> response) {
                if (isFinish) {
                    return;
                }
                dissMissDialog();
                if (response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    netImgs.clear();
                    netImgs.addAll(response.body().getResult());
                    saveProductMaterial();
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<String>>> call, Throwable t) {
                dissMissDialog();
            }
        });
    }

    private void saveProductMaterial() {
        showLoadingDialog(0);
        long memberId = BaseApp.getInstance().getMember().getId();
        String content = editContent.getText().toString().trim();
        Call<ResultDO> call = ApiClient.getApiAdapter().saveMaterial(memberId, content, StringUtils.listToString(netImgs), productId);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                if (isFinish) {
                    return;
                }
                dissMissDialog();
                if (response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    ToastUtils.showToast(mContext, "保存成功");
                    EventBus.getDefault().post(new UpdateProMateria());
                    finish();
                } else {
                    ToastUtils.showToast(mContext, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        imgList.clear();
        imgList.addAll(data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT));
        adapter.notifyDataSetChanged();
    }

    private class GridAdapter extends BaseAdapter {

        private View.OnClickListener onClickListener;
        private final int maxImg = 9;
        private List<String> imgList = new ArrayList<>();

        public GridAdapter(List<String> datas) {
            this.imgList = datas;
        }

        @Override
        public int getCount() {
            int count = imgList.size() >= maxImg ? maxImg : (imgList.size() + 1);
            return count;
        }

        @Override
        public Object getItem(int position) {
            if (imgList.size() >= maxImg) {
                return imgList.get(position);
            } else {
                if (position == imgList.size()) {
                    return "";
                } else return imgList.get(position);
            }
            //return imgList.size()>=maxImg?imgList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHower viewHower;
            if (convertView == null) {
                viewHower = new ViewHower();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_img, null);
                // viewHower.imageView = (ImageView) convertView.findViewById(R.id.img1);
                //viewHower.image = (ImageView) convertView.findViewById(R.id.img11);
                convertView.setTag(viewHower);
            } else {
                viewHower = (ViewHower) convertView.getTag();
            }

            viewHower.imageView = (ImageView) convertView.findViewById(R.id.img1);
            viewHower.image = (ImageView) convertView.findViewById(R.id.img11);
            if (position == (imgList.size())) {
                Picasso.with(mContext)
                        .load(R.mipmap.add_pic)
                        .error(R.mipmap.ic_default)
                        .into(viewHower.imageView);
                viewHower.imageView.setImageResource(R.mipmap.add_pic);
                viewHower.image.setVisibility(View.GONE);
            } else {
                Picasso.with(mContext)
                        .load(new File(ImageUtils.getCompressImagePath(imgList.get(position))))
                        .error(R.mipmap.ic_default)
                        .into(viewHower.imageView);
                viewHower.image.setVisibility(View.VISIBLE);
            }
            viewHower.imageView.setOnClickListener(onClickListener);
            viewHower.image.setOnClickListener(onClickListener);
            viewHower.imageView.setTag(position);
            viewHower.image.setTag(position);
            return convertView;
        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        class ViewHower {
            ImageView imageView;
            ImageView image;
        }
    }

}
