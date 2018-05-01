package com.sunday.common.update;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.sunday.common.R;
import com.sunday.common.logger.Logger;
import com.sunday.common.utils.MD5Utils;


import java.io.File;

import static android.support.v4.content.FileProvider.getUriForFile;


/**
 * app更新下载后台服务
 * Created by admin
 */
public class UpdateService extends Service {

    private static int NOTIFICATION_ID_LIVE = 1;//点击取消通知栏
    private static String NOTIFICATION_DELETED_ACTION = "com.sunday.notify_cancel_action";

    private String apkURL;
    private String filePath;
    private NotificationManager notificationManager;

    private static final String TAG = "TAG";
    private String md5;//文件MD5   校验  同时也是下载文件名
    private Context mContext;
    private NotifyCancelBroadcastRecever notifyCancelBroadcastRecever;
    private String applicationId;
    private static final long timestamp = 1231243;

    private boolean isCancel ;//通知栏是否取消

    @Override
    public void onCreate() {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mContext = this;
        notifyCancelBroadcastRecever = new NotifyCancelBroadcastRecever();
        IntentFilter filter = new IntentFilter();
        filter.addAction(NOTIFICATION_DELETED_ACTION);
        registerReceiver(notifyCancelBroadcastRecever, filter);
        applicationId = getPackageName();
        Log.i("TAG",applicationId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            notifyUser(getString(R.string.update_download_failed), getString(R.string.update_download_failed_msg), 0);
            stopSelf();
        }
        apkURL = intent.getStringExtra("apkUrl");
        md5 = intent.getStringExtra("md5");
        filePath = Environment.getExternalStorageDirectory() + "/sunday/"+md5+".apk";
        Log.i(TAG, "下载地址: " + apkURL);
        notifyUser(getString(R.string.update_download_start), getString(R.string.update_download_start), 0);
        startDownload();

        return super.onStartCommand(intent, flags, startId);
    }

    private void startDownload() {
        UpdateManager.getInstance().startDownloads(apkURL, filePath, new UpdateDownloadListener() {

            @Override
            public void onStarted() {
            }

            @Override
            public void onProgressChanged(int progress, String downloadUrl) {
                notifyUser(getString(R.string.update_download_processing), String.format(getString(R.string.update_download_processing),progress), progress);
            }

            @Override
            public void onFinished(int completeSize, String downloadUrl) {
                UpdateManager.getInstance().finishDownload();
                notifyUser(getString(R.string.update_download_finish), getString(R.string.update_download_finish), 100);
                stopSelf();
                installApp(filePath);
                isCancel = true;
                notificationManager.cancel(0);
            }

            @Override
            public void onFailure() {
                notifyUser(getString(R.string.update_download_failed), getString(R.string.update_download_failed_msg), 0);
                UpdateManager.getInstance().finishDownload();
                stopSelf();
            }
        });
    }

    private class NotifyCancelBroadcastRecever extends  BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("@@","收到取消通知栏广播");
            if(!TextUtils.isEmpty(intent.getAction())&&intent.getAction().equals(NOTIFICATION_DELETED_ACTION)){
                isCancel = true;
                UpdateManager.getInstance().finishDownload();
                stopSelf();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(notifyCancelBroadcastRecever);
    }

    /**
     * 更新notification来告知用户下载进度
     *
     * @param result
     * @param reason
     * @param progress
     */
    private void notifyUser(String result, String reason, int progress) {
        if(isCancel){
            return ;
        }
        Logger.d("TAG","更新通知栏");
        Notification mNotification;
        NotificationCompat.Builder build = null;
        if(build==null){
            build = new NotificationCompat.Builder(this);
            build.setSmallIcon(R.drawable.notify_icon_download)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.notify_icon_download))
                    .setContentTitle(getString(R.string.app_name))
                    //  .setContentInfo(config.getContentInfo())
                    .setContentText(reason)
                    .setContentIntent(getContentIntent())
                    //  .setDeleteIntent(config.getDeleteIntent())
                    .setContentTitle(getString(R.string.app_name))
                    .setDeleteIntent(PendingIntent.getBroadcast(this, NOTIFICATION_ID_LIVE, new Intent(NOTIFICATION_DELETED_ACTION), 0));
        }
        if (progress > 0 && progress < 100) {
            build.setProgress(100, progress, false);
        } else {
            build.setProgress(0, 0, false);
        }

        build.setAutoCancel(false);
      //  build.setWhen(timestamp);
        build.setTicker(result);
        build.setContentIntent(progress >= 100 ? getContentIntent() : PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT));
        mNotification = build.build();
        notificationManager.notify(111, mNotification);
    }

    public PendingIntent getContentIntent() {
        File apkFile = new File(filePath);

        if(!MD5Utils.verifyInstallPackage(apkFile,md5)){
            notificationManager.cancel(0);
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       //
        if(Build.VERSION.SDK_INT< Build.VERSION_CODES.N){
            intent.setDataAndType(Uri.parse("file://" + apkFile.getAbsolutePath()), "application/vnd.android.package-archive");
        }else{
            intent.setDataAndType(getUriForFile(mContext,
                    applicationId+".fileprovider", apkFile),"application/vnd.android.package-archive");
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    private void installApp(String filePath) {
        // TODO Auto-generated method stub
        File apkFile = new File(filePath);
        //校验文件md5 值是否合法
        if(!MD5Utils.verifyInstallPackage(apkFile,md5)){
            notificationManager.cancel(0);
            return ;
        }
        Log.e("OpenFile", apkFile.getName());
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        if(Build.VERSION.SDK_INT< Build.VERSION_CODES.N){
            intent.setDataAndType(Uri.parse("file://" + apkFile.getAbsolutePath()), "application/vnd.android.package-archive");
        }else{
            intent.setDataAndType(getUriForFile(mContext,
                    applicationId+".fileprovider", apkFile),"application/vnd.android.package-archive");
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
