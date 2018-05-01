package com.sunday.common.update;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sunday.common.R;
import com.sunday.common.base.BaseActivity;
import com.sunday.common.model.Version;
import com.sunday.common.utils.SharePerferenceUtils;

/**
 * Created by admin on 2017/3/8.
 */

public class UpdateDialogActivity extends BaseActivity implements View.OnClickListener{
    TextView content;
    private Version version;
    private static final int REQUEST_WRITE_STORAGE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.umeng_update_dialog);
        version = getIntent().getParcelableExtra("version");
        content = (TextView)findViewById(R.id.umeng_update_content);


        boolean ignore = SharePerferenceUtils.getIns(mContext).getBoolean("ignore",false);
        int versionCode = SharePerferenceUtils.getIns(mContext).getInt("versionCode",0);
        //如果当前版本已经被忽略则直接关掉界面
        if(ignore&&versionCode==version.getVersionCode()){
            finish();
            return ;
        }
        CheckBox checkBox = (CheckBox)findViewById(R.id.umeng_update_id_check);
        findViewById(R.id.umeng_update_id_ok).setOnClickListener(this);
        findViewById(R.id.umeng_update_id_cancel).setOnClickListener(this);
        if(!TextUtils.isEmpty(version.getFileLogs())){
            content.setText(version.getFileLogs());
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SharePerferenceUtils.getIns(mContext).putBoolean("ignore",true);
                    SharePerferenceUtils.getIns(mContext).putInt("versionCode",version.getVersionCode());
                }else{
                    SharePerferenceUtils.getIns(mContext).putBoolean("ignore",false);

                }
            }
        });



    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.umeng_update_id_ok){
            //请求存储权限
            boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
            if (!hasPermission) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
            } else {
                //下载
                startDownload();
                finish();
            }
        }else if(v.getId()== R.id.umeng_update_id_cancel){
            finish();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //获取到存储权限,进行下载
                    startDownload();
                } else {
                    Toast.makeText(this, "不授予存储权限将无法进行下载!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 启动下载
     */
    private void startDownload() {
        Intent it = new Intent(this, UpdateService.class);
        //下载地址
        it.putExtra("apkUrl", version.getDownLoadUrl());
        it.putExtra("md5",version.getFileMd5());
        startService(it);
    }
}
