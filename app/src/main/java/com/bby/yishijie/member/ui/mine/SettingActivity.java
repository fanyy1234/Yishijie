package com.bby.yishijie.member.ui.mine;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bby.yishijie.R;
import com.bby.yishijie.member.common.BaseApp;
import com.bby.yishijie.member.entity.City;
import com.bby.yishijie.member.entity.Member;
import com.bby.yishijie.member.event.UpdateBaseInfo;
import com.bby.yishijie.member.http.ApiClient;
import com.bby.yishijie.member.ui.MainActivity;
import com.bumptech.glide.Glide;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.event.EventBus;
import com.sunday.common.event.ExitApp;
import com.sunday.common.imageselector.MultiImageSelectorActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.CacheUtils;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.utils.ImageUtils;
import com.sunday.common.utils.SharePerferenceUtils;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.utils.UploadUtils;
import com.sunday.common.widgets.UIAlertView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2017/5/15.
 */

public class SettingActivity extends BaseActivity {

    @Bind(R.id.left_btn)
    ImageView leftBtn;
    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.rightTxt)
    TextView rightTxt;
    @Bind(R.id.right_btn)
    ImageView rightBtn;
    @Bind(R.id.menu_person_info)
    TextView menuPersonInfo;
    @Bind(R.id.menu_clear_cash)
    TextView menuClearCash;
    @Bind(R.id.btn_logout)
    TextView btnLogout;


    @Bind(R.id.user_logo)
    ImageView userLogo;
    @Bind(R.id.menu_user_logo)
    LinearLayout menuUserLogo;
    @Bind(R.id.user_name)
    TextView userName;
    @Bind(R.id.menu_user_name)
    LinearLayout menuUserName;
    @Bind(R.id.user_sex)
    TextView userSex;
    @Bind(R.id.menu_user_sex)
    LinearLayout menuUserSex;
    @Bind(R.id.user_addr)
    TextView userAddr;
    @Bind(R.id.menu_user_addr)
    LinearLayout menuUserAddr;
    @Bind(R.id.menu_user_pwd)
    LinearLayout menuUserPwd;
    @Bind(R.id.user_version)
    TextView userVersion;
    @Bind(R.id.rec_code)
    TextView recCode;
    @Bind(R.id.rec_code_view)
    LinearLayout recCodeView;

    //    private Member member;
    private long memberId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        titleView.setText("设置");
        menuClearCash.setText("清理缓存  （" + CacheUtils.getTotalCacheSize(mContext) + "）");
        userVersion.setText(DeviceUtils.getVersion(mContext));
        if (MainActivity.isShop) {
            memberId = BaseApp.getInstance().getShopMember().getId();
        } else {
            memberId = BaseApp.getInstance().getMember().getId();
        }
        initView();
    }

    private void initView() {
        if (MainActivity.isShop) {
            com.bby.yishijie.shop.entity.Member member = BaseApp.getInstance().getShopMember();
            if (!TextUtils.isEmpty(member.getLogo())) {
                Glide.with(mContext)
                        .load(member.getLogo())
                        .error(R.mipmap.ic_default)
                        .into(userLogo);
            }
            if (member.getSex() == null) {
                userSex.setText("");
            } else {
                userSex.setText(member.getSex().equals(1) ? "男" : member.getSex().equals(2) ? "女" : "");
            }
            userName.setText(TextUtils.isEmpty(member.getNickName()) ? "" : member.getNickName());
            userAddr.setText(TextUtils.isEmpty(member.getCityName()) ? "" : member.getCityName());
            recCodeView.setVisibility(View.VISIBLE);
            recCode.setText(member.getInitCode());
        } else {
            Member member = BaseApp.getInstance().getMember();
            if (!TextUtils.isEmpty(member.getLogo())) {
                Glide.with(mContext)
                        .load(member.getLogo())
                        .error(R.mipmap.ic_default)
                        .into(userLogo);
            }
            if (member.getSex() == null) {
                userSex.setText("");
            } else {
                userSex.setText(member.getSex().equals(1) ? "男" : member.getSex().equals(2) ? "女" : "");
            }
            userName.setText(TextUtils.isEmpty(member.getNickName()) ? "" : member.getNickName());
            userAddr.setText(TextUtils.isEmpty(member.getCityName()) ? "" : member.getCityName());
            recCodeView.setVisibility(View.GONE);
        }

    }

    private final static int REQUEST_IMAGE = 0x1111;
    private final static int REQUEST_CITY = 0x1112;

    @OnClick({R.id.menu_clear_cash, R.id.btn_logout,
            R.id.menu_user_logo, R.id.menu_user_name, R.id.menu_user_sex, R.id.menu_user_addr, R.id.menu_user_pwd})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_clear_cash:
                CacheUtils.clearAllCache(mContext);
                menuClearCash.setText("清理缓存  （" + CacheUtils.getTotalCacheSize(mContext) + "）");
                break;
            case R.id.btn_logout:
                showLogoutDialog();
                break;
            case R.id.menu_user_logo:
                intent = new Intent(mContext, MultiImageSelectorActivity.class);
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
                startActivityForResult(intent, REQUEST_IMAGE);
                break;
            case R.id.menu_user_name:
                intent = new Intent(mContext, UpdateBaseInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_user_sex:
                setSex();
                break;
            case R.id.menu_user_addr:
                intent = new Intent(mContext, SelectProvinceActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("fromUserInfo", true);
                startActivityForResult(intent, REQUEST_CITY);
                break;
            case R.id.menu_user_pwd:
                intent = new Intent(mContext, SettingPwdActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void showLogoutDialog() {
        final UIAlertView delDialog = new UIAlertView(mContext, "温馨提示", "退出登录？",
                "取消", "确定");
        delDialog.setClicklistener(new UIAlertView.ClickListenerInterface() {
                                       @Override
                                       public void doLeft() {
                                           delDialog.dismiss();
                                       }

                                       @Override
                                       public void doRight() {
                                           SharePerferenceUtils.getIns(mContext).removeKey("oAuth");
                                           SharePerferenceUtils.getIns(mContext).removeKey(Constants.IS_LOGIN);
                                           SharePerferenceUtils.getIns(mContext).removeKey(Constants.MEMBERID);
                                           BaseApp.getInstance().setMember(null);
                                           BaseApp.getInstance().setShopMember(null);
                                           SharePerferenceUtils.getIns(mContext).removeKey(Constants.IS_SHOP);
                                           deleteClientId();
                                           delDialog.dismiss();
                                       }
                                   }
        );
        delDialog.show();
    }

    private void deleteClientId() {
        showLoadingDialog(0);
        Call<ResultDO> call = ApiClient.getApiAdapter().deleteClientId(memberId, 1);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                if (response.body() == null) {
                    return;
                }
                dissMissDialog();
                if (response.body().getCode() == 0) {
                    Log.i("upload_clientId", "success");
                    EventBus.getDefault().post(new ExitApp());
                    intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {

            }
        });
    }

    private int sexNum;

    private void setSex() {
        int checkedItem = 0;
        if (MainActivity.isShop) {
            com.bby.yishijie.shop.entity.Member member = BaseApp.getInstance().getShopMember();
            if (member.getSex() == null) {
                checkedItem = -1;
            } else if (member.getSex().equals(1)) {
                checkedItem = 0;
            } else if (member.getSex().equals(2)) {
                checkedItem = 1;
            }
        } else {
            Member member = BaseApp.getInstance().getMember();
            if (member.getSex() == null) {
                checkedItem = -1;
            } else if (member.getSex().equals(1)) {
                checkedItem = 0;
            } else if (member.getSex().equals(2)) {
                checkedItem = 1;
            }
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("性别");
        final String[] sex = {"男", "女"};
        //    设置一个单项选择下拉框
        /**
         * 第一个参数指定我们要显示的一组下拉单选框的数据集合
         * 第二个参数代表索引，指定默认哪一个单选框被勾选上，1表示默认'女' 会被勾选上
         * 第三个参数给每一个单选项绑定一个监听器
         */
        builder.setSingleChoiceItems(sex, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userSex.setText(sex[which]);
                sexNum = which + 1;
                update(null, sexNum);
                dialog.dismiss();
            }
        });
        builder.setCancelable(true);
        builder.show();
    }


    private ArrayList<String> mSelectPath = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_IMAGE:
                mSelectPath.clear();
                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                if (mSelectPath != null) {
                    String logo = ImageUtils.getCompressImagePath(mSelectPath.get(0));
                    Glide.with(mContext)
                            .load(new File(logo))
                            .placeholder(R.mipmap.ic_default)
                            .error(R.mipmap.ic_default)
                            .into(userLogo);
                    uploadImg(UploadUtils.getRequestBody(logo));
                }
                break;
            case REQUEST_CITY:
                City province = (City) data.getSerializableExtra("province");
                City city = (City) data.getSerializableExtra("city");
                updateUserInfo(province, city);
                break;
        }

    }


    private void updateUserInfo(final City province, final City city) {
        showLoadingDialog(0);
        if (MainActivity.isShop) {
            Call<ResultDO<com.bby.yishijie.shop.entity.Member>> call = ApiClient.getApiAdapter().saveEditInfo2(
                    BaseApp.getInstance().getMember().getId(), null, null, null, province.getId(), city.getId(), province.getValue() + city.getValue());
            call.enqueue(new Callback<ResultDO<com.bby.yishijie.shop.entity.Member>>() {
                @Override
                public void onResponse(Call<ResultDO<com.bby.yishijie.shop.entity.Member>> call, Response<ResultDO<com.bby.yishijie.shop.entity.Member>> response) {
                    dissMissDialog();
                    if (response.body() == null || isFinish) {
                        return;
                    }
                    if (response.body().getResult() != null && response.body().getCode() == 0) {
                        SharePerferenceUtils.getIns(mContext).removeKey("oAuth");
                        SharePerferenceUtils.getIns(mContext).saveOAuth(response.body().getResult());
                        com.bby.yishijie.shop.entity.Member member = response.body().getResult();
                        BaseApp.getInstance().setShopMember(member);
                        initView();
                    }
                }

                @Override
                public void onFailure(Call<ResultDO<com.bby.yishijie.shop.entity.Member>> call, Throwable t) {
                    dissMissDialog();
                    ToastUtils.showToast(mContext, "网络错误");
                }
            });
        } else {
            Call<ResultDO<Member>> call = ApiClient.getApiAdapter().saveEditInfo(
                    BaseApp.getInstance().getMember().getId(), null, null, null, province.getId(), city.getId(), province.getValue() + city.getValue());
            call.enqueue(new Callback<ResultDO<Member>>() {
                @Override
                public void onResponse(Call<ResultDO<Member>> call, Response<ResultDO<Member>> response) {
                    dissMissDialog();
                    if (response.body() == null || isFinish) {
                        return;
                    }
                    if (response.body().getResult() != null && response.body().getCode() == 0) {
                        SharePerferenceUtils.getIns(mContext).removeKey("oAuth");
                        SharePerferenceUtils.getIns(mContext).saveOAuth(response.body().getResult());
                        Member member = response.body().getResult();
                        BaseApp.getInstance().setMember(member);
                        initView();
                    }
                }

                @Override
                public void onFailure(Call<ResultDO<Member>> call, Throwable t) {
                    dissMissDialog();
                    ToastUtils.showToast(mContext, "网络错误");
                }
            });
        }

    }

    private void uploadImg(final RequestBody requestBody) {
        showLoadingDialog(0);
        Call<ResultDO<List<String>>> call
                = ApiClient.getApiAdapter().uploadImgs(requestBody);
        call.enqueue(new Callback<ResultDO<List<String>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<String>>> call, Response<ResultDO<List<String>>> response) {
                dissMissDialog();
                if (response.body() == null || isFinish) {
                    return;
                }
                if (response.body() != null && response.body().getCode() == 0) {
                    //ToastUtils.showToast(mContext,"上传成功");
                    List<String> list = new ArrayList<String>();
                    list.addAll(response.body().getResult());
                    update(list.get(0), null);
                } else {
                    ToastUtils.showToast(mContext, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<String>>> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext, "网络错误");
            }
        });
    }

    private void update(String logo, Integer sex) {
        showLoadingDialog(0);
        if (MainActivity.isShop) {
            Call<ResultDO<com.bby.yishijie.shop.entity.Member>> call = ApiClient.getApiAdapter().saveEditInfo2(
                    memberId, sex, null, logo, null, null, null);
            call.enqueue(new Callback<ResultDO<com.bby.yishijie.shop.entity.Member>>() {
                @Override
                public void onResponse(Call<ResultDO<com.bby.yishijie.shop.entity.Member>> call, Response<ResultDO<com.bby.yishijie.shop.entity.Member>> response) {
                    dissMissDialog();
                    if (response.body() == null || isFinish) {
                        return;
                    }
                    if (response.body().getResult() != null && response.body().getCode() == 0) {
                        SharePerferenceUtils.getIns(mContext).removeKey("oAuth");
                        SharePerferenceUtils.getIns(mContext).saveOAuth(response.body().getResult());
                        com.bby.yishijie.shop.entity.Member member = response.body().getResult();
                        BaseApp.getInstance().setShopMember(member);
                        EventBus.getDefault().post(new UpdateBaseInfo());
                    }
                }

                @Override
                public void onFailure(Call<ResultDO<com.bby.yishijie.shop.entity.Member>> call, Throwable t) {
                    dissMissDialog();
                    ToastUtils.showToast(mContext, "网络错误");
                }
            });
        } else {
            Call<ResultDO<Member>> call = ApiClient.getApiAdapter().saveEditInfo(
                    memberId, sex, null, logo, null, null, null);
            call.enqueue(new Callback<ResultDO<Member>>() {
                @Override
                public void onResponse(Call<ResultDO<Member>> call, Response<ResultDO<Member>> response) {
                    dissMissDialog();
                    if (response.body() == null || isFinish) {
                        return;
                    }
                    if (response.body().getResult() != null && response.body().getCode() == 0) {
                        SharePerferenceUtils.getIns(mContext).removeKey("oAuth");
                        SharePerferenceUtils.getIns(mContext).saveOAuth(response.body().getResult());
                        Member member = response.body().getResult();
                        BaseApp.getInstance().setMember(member);
                        EventBus.getDefault().post(new UpdateBaseInfo());
                    }
                }

                @Override
                public void onFailure(Call<ResultDO<Member>> call, Throwable t) {
                    dissMissDialog();
                    ToastUtils.showToast(mContext, "网络错误");
                }
            });
        }

    }


}
