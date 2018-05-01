package com.bby.yishijie.shop.ui;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.shop.adapter.CommonAdapter;
import com.bby.yishijie.shop.adapter.ViewHolder;
import com.bby.yishijie.shop.entity.Customer;
import com.bby.yishijie.shop.entity.Member;
import com.bumptech.glide.Glide;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.CircleImageView;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 刘涛 on 2017/5/10.
 */

public class CustomerManageActivity extends BaseActivity {

    @Bind(R.id.left_btn)
    ImageView leftBtn;
    @Bind(R.id.left_txt)
    TextView leftTxt;
    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.rightTxt)
    TextView rightTxt;
    @Bind(R.id.right_btn)
    ImageView rightBtn;
    @Bind(R.id.common_header)
    RelativeLayout commonHeader;
    @Bind(R.id.right_arrow)
    ImageView rightArrow;
    @Bind(R.id.text_code)
    TextView textCode;
    @Bind(R.id.btn_copy)
    TextView btnCopy;
    @Bind(R.id.layout_code)
    RelativeLayout layoutCode;
    @Bind(R.id.fans_num)
    TextView fansNum;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private ClipboardManager myClipboard;

    private CommonAdapter<Member> adapter;
    private List<Member> dataSet = new ArrayList<>();
    private Long memberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_mange);
        ButterKnife.bind(this);
        initView();
        getData();
    }

    private void initView() {
        Member member = BaseApp.getInstance().getShopMember();
        memberId=member.getId();
        rightTxt.setVisibility(View.VISIBLE);
        rightTxt.setText("邀请客户");
        titleView.setText("客户管理");
        textCode.setText(member.getInitCode());
        myClipboard = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CommonAdapter<Member>(mContext, R.layout.list_customer_item, dataSet) {
            @Override
            public void convert(ViewHolder holder, Member member) {
                bindData(holder, member);
            }
        };
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .drawable(R.drawable.shape_divider)
                .build());
    }

    private void getData() {
        showLoadingDialog(0);
        Call<ResultDO<Customer>> call = ApiClient.getApiAdapter().getMemberList(memberId);
        call.enqueue(new Callback<ResultDO<Customer>>() {
            @Override
            public void onResponse(Call<ResultDO<Customer>> call, Response<ResultDO<Customer>> response) {
                if (isFinish||response.body()==null) {
                    return;
                }
                dissMissDialog();
                ResultDO<Customer> resultDO = response.body();
                if (resultDO.getCode() == 0) {
                    if (resultDO.getResult() == null) {
                        return;
                    }
                    dataSet.clear();
                    dataSet.addAll(resultDO.getResult().getList());
                    if (dataSet.size()>0){
                        fansNum.setText(String.format("共有%d位",dataSet.size()));
                        fansNum.setVisibility(View.VISIBLE);
                    }else {
                        fansNum.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Customer>> call, Throwable t) {
                dissMissDialog();
            }
        });
    }
    private void bindData(ViewHolder viewHolder, Member member) {
        CircleImageView circleImageView = viewHolder.getView(R.id.user_logo);
        if (!TextUtils.isEmpty(member.getLogo())) {
            Glide.with(mContext)
                    .load(member.getLogo())
                    .error(R.mipmap.ic_default)
                    .into(circleImageView);
        }
        ((TextView) viewHolder.getView(R.id.nick_name)).setText(member.getNickName());
        ((TextView) viewHolder.getView(R.id.reg_time)).setText("注册时间"+member.getRegTime());
    }


    @OnClick({R.id.btn_copy, R.id.rightTxt})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_copy:
                ClipData myClip = ClipData.newPlainText("text", textCode.getText());
                myClipboard.setPrimaryClip(myClip);

                ToastUtils.showToast(mContext, "已复制到粘贴板");
                break;
            case R.id.rightTxt:
                UMShareAPI umShareAPI = UMShareAPI.get(mContext);
                UMImage umImage = new UMImage(mContext, R.mipmap.logo);
                String shareUrl = String.format("http://weixin.haowukongtou.com/authorizationPage.html?param=5-%d-0-0", BaseApp.getInstance().getMember().getId());
                UMWeb web = new UMWeb(shareUrl);
                web.setTitle(getString(R.string.invite_title));
                web.setThumb(umImage);
                web.setDescription(getString(R.string.invite_content));
                new ShareAction((Activity) mContext).setPlatform(SHARE_MEDIA.WEIXIN)
                        .withMedia(web)
                        .share();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
