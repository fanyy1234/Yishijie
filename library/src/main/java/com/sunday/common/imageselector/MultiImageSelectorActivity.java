package com.sunday.common.imageselector;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.sunday.common.R;
import com.sunday.common.widgets.UIAlertView;

import java.io.File;
import java.util.ArrayList;

/**
 * 多图选择
 * Created by Nereo on 2015/4/7.
 */
public class MultiImageSelectorActivity extends FragmentActivity implements MultiImageSelectorFragment.Callback{

    /** 最大图片选择次数，int类型，默认9 */
    public static final String EXTRA_SELECT_COUNT = "max_select_count";
    /** 图片选择模式，默认多选 */
    public static final String EXTRA_SELECT_MODE = "select_count_mode";
    /** 是否显示相机，默认显示 */
    public static final String EXTRA_SHOW_CAMERA = "show_camera";
    /** 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合  */
    public static final String EXTRA_RESULT = "select_result";
    /** 默认选择集 */
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_list";

    /** 单选 */
    public static final int MODE_SINGLE = 0;
    /** 多选 */
    public static final int MODE_MULTI = 1;

    private ArrayList<String> resultList = new ArrayList<>();
    private Button mSubmitButton;
    private int mDefaultCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);

        if (Build.VERSION.SDK_INT >= 23){
            check();
        }else {
            Commit();
        }
        // 返回按钮
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        // 完成按钮
        mSubmitButton = (Button) findViewById(R.id.commit);
        if(resultList == null || resultList.size()<=0){
            mSubmitButton.setText("完成");
            mSubmitButton.setEnabled(false);
        }else{
            mSubmitButton.setText("完成("+resultList.size()+"/"+mDefaultCount+")");
            mSubmitButton.setEnabled(true);
        }
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resultList != null && resultList.size() >0){
                    // 返回已选择的图片数据
                    Intent data = new Intent();
                    data.putStringArrayListExtra(EXTRA_RESULT, resultList);
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });

    }

    private void check(){
        PackageManager pkgManager = getPackageManager();
        boolean sdCardReadPermission =
                pkgManager.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, getPackageName()) == PackageManager.PERMISSION_GRANTED;
        // read phone state用于获取 imei 设备信息
        boolean phoneSatePermission =
                pkgManager.checkPermission(Manifest.permission.READ_PHONE_STATE, getPackageName()) == PackageManager.PERMISSION_GRANTED;

        //相机权限
        boolean cameraPermission =
                pkgManager.checkPermission(Manifest.permission.CAMERA, getPackageName()) == PackageManager.PERMISSION_GRANTED;

        if (!phoneSatePermission ||!sdCardReadPermission || !cameraPermission) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA},
                    0);
        } else {
            // SDK初始化，第三方程序启动时，都要进行SDK初始化工作
            //PushManager.getInstance().initialize(this.getApplicationContext());
            Commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    // SDK初始化，第三方程序启动时，都要进行SDK初始化工作
                    //PushManager.getInstance().initialize(this.getApplicationContext());
                    Commit();
                } else {
                    //checkMMS();
                    show();
                }
                return;
        }
    }

    private void show(){
        final UIAlertView view = new UIAlertView(this , "温馨提示" , "权限已被关闭，是否需要重新设置" , "取消" , "确定");
        view.setClicklistener(new UIAlertView.ClickListenerInterface() {
            @Override
            public void doLeft() {
                view.dismiss();
                finish();
            }

            @Override
            public void doRight() {
                view.dismiss();
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_SETTINGS);
                startActivity(intent);
                finish();
            }
        });
        view.show();
    }

    private void Commit(){
        Intent intent = getIntent();
        mDefaultCount = intent.getIntExtra(EXTRA_SELECT_COUNT, 9);
        int mode = intent.getIntExtra(EXTRA_SELECT_MODE, MODE_MULTI);
        boolean isShow = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, true);
        if(mode == MODE_MULTI && intent.hasExtra(EXTRA_DEFAULT_SELECTED_LIST)) {
            resultList = intent.getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST);
        }

        Bundle bundle = new Bundle();
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_COUNT, mDefaultCount);
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_MODE, mode);
        bundle.putBoolean(MultiImageSelectorFragment.EXTRA_SHOW_CAMERA, isShow);
        bundle.putStringArrayList(MultiImageSelectorFragment.EXTRA_DEFAULT_SELECTED_LIST, resultList);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.image_grid, Fragment.instantiate(this, MultiImageSelectorFragment.class.getName(), bundle))
                .commitAllowingStateLoss();
    }

    @Override
    public void onSingleImageSelected(String path) {
        Intent data = new Intent();
        resultList.add(path);
        data.putStringArrayListExtra(EXTRA_RESULT, resultList);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onImageSelected(String path) {
        if(!resultList.contains(path)) {
            resultList.add(path);
        }
        // 有图片之后，改变按钮状态
        if(resultList.size() > 0){
            mSubmitButton.setText("完成("+resultList.size()+"/"+mDefaultCount+")");
            if(!mSubmitButton.isEnabled()){
                mSubmitButton.setEnabled(true);
            }
        }
    }

    @Override
    public void onImageUnselected(String path) {
        if(resultList.contains(path)){
            resultList.remove(path);
            mSubmitButton.setText("完成("+resultList.size()+"/"+mDefaultCount+")");
        }else{
            mSubmitButton.setText("完成("+resultList.size()+"/"+mDefaultCount+")");
        }
        // 当为选择图片时候的状态
        if(resultList.size() == 0){
            mSubmitButton.setText("完成");
            mSubmitButton.setEnabled(false);
        }
    }

    @Override
    public void onCameraShot(File imageFile) {
        if(imageFile != null) {
            Intent data = new Intent();
            resultList.add(imageFile.getAbsolutePath());
            data.putStringArrayListExtra(EXTRA_RESULT, resultList);
            setResult(RESULT_OK, data);
            finish();
        }
    }
}
