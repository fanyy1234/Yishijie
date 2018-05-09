package com.bby.yishijie.member.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.entity.Member;
import com.bby.yishijie.member.event.UpdateBaseInfo;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.MainActivity;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.event.EventBus;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.SharePerferenceUtils;
import com.sunday.common.utils.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 刘涛 on 2017/5/15.
 */

public class SetRecCodeActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.reccode_edittext)
    EditText reccodeEdittext;
    @Bind(R.id.set_reccode_btn)
    LinearLayout setReccodeBtn;

    private long memberId;
    private String recCodeStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_reccode);
        ButterKnife.bind(this);
        titleView.setText("设置邀请码");
        if (MainActivity.isShop){
            com.bby.yishijie.shop.entity.Member member = BaseApp.getInstance().getShopMember();
            memberId = member.getId();
            if (member.getRecId()!=0){
                reccodeEdittext.setEnabled(false);
                setReccodeBtn.setVisibility(View.GONE);
                getShopMemberInfo2(member.getRecId());
            }
        }
        else {
            Member member = BaseApp.getInstance().getMember();
            memberId = member.getId();
            if (member.getRecId()!=0){
                reccodeEdittext.setEnabled(false);
                setReccodeBtn.setVisibility(View.GONE);
                getShopMemberInfo2(member.getRecId());
            }
        }

        initView();
    }

    private void initView() {
        setReccodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recCodeStr = reccodeEdittext.getText().toString().trim();
                if (recCodeStr.equals("")){
                    ToastUtils.showToast(mContext,"邀请码不能为空");
                }
                else {
                    bindCode();
                }
            }
        });
    }

    private void bindCode() {
        showLoadingDialog(0);
        Call<ResultDO> call = ApiClient.getApiAdapter().bindShop(memberId,recCodeStr);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                dissMissDialog();
                if (response.body() == null || isFinish) {
                    return;
                }
                if (response.body().getResult() != null && response.body().getCode() == 0) {
                    ToastUtils.showToast(mContext,"绑定成功");
                    if (MainActivity.isShop){
                        getShopMemberInfo();
                    }
                    else {
                        getMemberInfo();
                    }

                }
                else {
                    ToastUtils.showToast(mContext,response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext, "网络错误");
            }
        });
    }
    private void getMemberInfo() {
        showLoadingDialog(0);
        Call<ResultDO<Member>> call = ApiClient.getApiAdapter().getMemberInfo(memberId);
        call.enqueue(new Callback<ResultDO<Member>>() {
            @Override
            public void onResponse(Call<ResultDO<Member>> call, Response<ResultDO<Member>> response) {
                dissMissDialog();
                if (isFinish) {
                    return;
                }
                if (response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    if (response.body().getResult() == null) {
                        return;
                    }
                    SharePerferenceUtils.getIns(mContext).saveOAuth(response.body().getResult());
                    BaseApp.getInstance().setMember(response.body().getResult());
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Member>> call, Throwable t) {
                dissMissDialog();
            }
        });
    }
    private void getShopMemberInfo() {
        Call<ResultDO<com.bby.yishijie.shop.entity.Member>> call = ApiClient.getApiAdapter().getMemberInfo2(memberId);
        call.enqueue(new Callback<ResultDO<com.bby.yishijie.shop.entity.Member>>() {
            @Override
            public void onResponse(Call<ResultDO<com.bby.yishijie.shop.entity.Member>> call, Response<ResultDO<com.bby.yishijie.shop.entity.Member>> response) {
                if (isFinish || response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    if (response.body().getResult() != null) {
                        com.bby.yishijie.shop.entity.Member member = response.body().getResult();
                        SharePerferenceUtils.getIns(mContext).saveOAuth(member);
                        BaseApp.getInstance().setShopMember(member);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultDO<com.bby.yishijie.shop.entity.Member>> call, Throwable t) {

            }
        });
    }
    private void getShopMemberInfo2(long shopId) {
        Call<ResultDO<com.bby.yishijie.shop.entity.Member>> call = ApiClient.getApiAdapter().getMemberInfo2(shopId);
        call.enqueue(new Callback<ResultDO<com.bby.yishijie.shop.entity.Member>>() {
            @Override
            public void onResponse(Call<ResultDO<com.bby.yishijie.shop.entity.Member>> call, Response<ResultDO<com.bby.yishijie.shop.entity.Member>> response) {
                if (isFinish || response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    if (response.body().getResult() != null) {
                        reccodeEdittext.setText(response.body().getResult().getInitCode());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultDO<com.bby.yishijie.shop.entity.Member>> call, Throwable t) {

            }
        });
    }
}
