package com.bby.yishijie.member.ui.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunday.common.base.BaseActivity;
import com.sunday.common.event.EventBus;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.SharePerferenceUtils;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.ClearEditText;
import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.entity.Member;
import com.bby.yishijie.member.event.UpdateBaseInfo;
import com.bby.yishijie.member.http.ApiClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2017/5/16.
 */

public class UpdateBaseInfoActivity extends BaseActivity{


    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.et_info)
    ClearEditText etInfo;
    @Bind(R.id.activity_update_base_info)
    LinearLayout activityUpdateBaseInfo;

    private int type;//0:昵称
    private String userName;
    private Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_base_info);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", 0);
        member = BaseApp.getInstance().getMember();
        switch (type) {
            case 0:
                titleView.setText("修改昵称");
                etInfo.setText(TextUtils.isEmpty(member.getNickName()) ? "" : member.getNickName());
                break;
        }
    }

    @OnClick(R.id.btn_submit)
    void submit() {
        String info = etInfo.getText().toString().trim();
        if (TextUtils.isEmpty(info)) {
            ToastUtils.showToast(mContext, "内容不能为空");
            return;
        }
        if (type==0){
            userName=info;
        }
        showLoadingDialog(0);
        Call<ResultDO<Member>> call = ApiClient.getApiAdapter().saveEditInfo(member.getId(),null,userName,null,
                null,null,null);
        call.enqueue(new Callback<ResultDO<Member>>() {
            @Override
            public void onResponse(Call<ResultDO<Member>> call, Response<ResultDO<Member>> response) {
                dissMissDialog();
                if (response.body() == null || isFinish) {
                    return;
                }
                if (response.body().getResult() != null && response.body().getCode() == 0) {
                    ToastUtils.showToast(mContext, "修改成功");
                    SharePerferenceUtils.getIns(mContext).saveOAuth(response.body().getResult());
                    BaseApp.getInstance().setMember(response.body().getResult());
                    EventBus.getDefault().post(new UpdateBaseInfo());
                    finish();
                }

            }

            @Override
            public void onFailure(Call<ResultDO<Member>> call, Throwable t) {
                dissMissDialog();
            }
        });
    }
}
