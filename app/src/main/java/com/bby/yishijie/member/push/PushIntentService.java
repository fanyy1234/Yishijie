//package com.bby.yishijie.member.push;
//
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.support.v7.app.NotificationCompat;
//import android.text.TextUtils;
//import android.util.Log;
//
//import com.google.gson.Gson;
//import com.igexin.sdk.GTIntentService;
//import com.igexin.sdk.message.GTCmdMessage;
//import com.igexin.sdk.message.GTTransmitMessage;
//import com.sunday.common.model.ResultDO;
//import com.sunday.common.utils.download.AppUtils;
//import com.bby.yishijie.R;
//import com.bby.yishijie.member.common.BaseApp;
//import com.bby.yishijie.member.entity.PushMessage;
//import com.bby.yishijie.member.http.ApiClient;
//import com.bby.yishijie.member.ui.MainActivity;
//import com.bby.yishijie.member.ui.find.FindItenDetailActivity;
//import com.bby.yishijie.member.ui.mine.MessageListActivity;
//import com.bby.yishijie.member.ui.product.BrandProductListActivity;
//import com.bby.yishijie.member.ui.product.ProductDetailActivity;
//
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
///**
// * Created by 刘涛 on 2017/6/9.
// * <p>
// * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
// * onReceiveMessageData 处理透传消息<br>
// * onReceiveClientId 接收 cid <br>
// * onReceiveOnlineState cid 离线上线通知 <br>
// * onReceiveCommandResult 各种事件处理回执 <br>
// */
//
//
//public class PushIntentService extends GTIntentService {
//
//    public PushIntentService() {
//
//    }
//
//    @Override
//    public void onReceiveServicePid(Context context, int pid) {
//    }
//
//    @Override
//    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
//
//        String data = new String(msg.getPayload());
//        Log.d(TAG, "receiver payload = " + data);
//        showNotifyWindow(data, context);
//    }
//
//    @Override
//    public void onReceiveClientId(Context context, String clientid) {
//        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
//        if (TextUtils.isEmpty(clientid)) {
//            return;
//        }
//        if (BaseApp.getInstance().getMember() == null) {
//            return;
//        }
//        Call<ResultDO> call = ApiClient.getApiAdapter().upLoadClientId(BaseApp.getInstance().getMember().getId(), clientid,
//                1, 1);
//        call.enqueue(new Callback<ResultDO>() {
//            @Override
//            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
//                if (response.body() == null) {
//                    return;
//                }
//                if (response.body().getCode() == 0) {
//                    Log.i("upload_clientId", "success");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResultDO> call, Throwable t) {
//
//            }
//        });
//    }
//
//    @Override
//    public void onReceiveOnlineState(Context context, boolean online) {
//    }
//
//    @Override
//    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
//
//
//    }
//
//    private void showNotifyWindow(String data, Context context) {
//        PushMessage pushMessage = new Gson().fromJson(data, PushMessage.class);
//        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
//        boolean isAlive = AppUtils.isAppaLive(context, getPackageName());
//        Intent intent = null;
//        if (isAlive) {
//            if (pushMessage.getObjId() != null) {
//                switch (pushMessage.getType()) {
//                    case 1:
//                        intent = new Intent(context, ProductDetailActivity.class);
//                        intent.putExtra("productId", pushMessage.getObjId());
//                        break;
//                    case 2:
//                        intent = new Intent(context, BrandProductListActivity.class);
//                        intent.putExtra("brandId", pushMessage.getObjId());
//                        intent.putExtra("brandName", pushMessage.getBrandName());
//                        intent.putExtra("brandImg", pushMessage.getImage());
//                        break;
//                    case 3:
//                        intent = new Intent(context, FindItenDetailActivity.class);
//                        intent.putExtra("findId", pushMessage.getObjId());
//                        break;
//                }
//            } else {
//                intent = new Intent(context, MainActivity.class);
//            }
//        } else {
//            intent = new Intent(context, MainActivity.class);
//        }
//
////        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
////                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        mBuilder.setContentTitle(pushMessage.getTitle())//设置通知栏标题
//                .setContentText(pushMessage.getContent())
//                .setContentIntent(pendingIntent) //设置通知栏点击意图
////	.                   setNumber(number) //设置通知集合的数量
//                .setTicker(pushMessage.getContent()) //通知首次出现在通知栏，带上升动画效果的
//                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
//                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
//                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
//                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
//                .setDefaults(Notification.STREAM_DEFAULT)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
//                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND  DEFAULT_VIBRATE 添加声音 // requires VIBRATE permission
//                // .setSound(Uri.parse("file:///sdcard/order.mp3"))
//                // .setSound(Uri.parse(context.getResources().openRawResource(R.raw.order))
//                .setSmallIcon(R.mipmap.logo);//设置通知小ICON
//        mNotificationManager.notify(0, mBuilder.build());
//    }
//}
