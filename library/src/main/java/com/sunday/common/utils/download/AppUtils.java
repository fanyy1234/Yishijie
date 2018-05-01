package com.sunday.common.utils.download;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Author by Damon,  on 2018/2/5.
 */

public class AppUtils {
    private final static String TAG="appUtils";
    public static boolean isAppaLive(Context context , String str) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        boolean isAppRunning = false;
        //String MY_PKG_NAME = "你的包名";
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(str)
                    || info.baseActivity.getPackageName().equals(str)) {
                isAppRunning = true;
                Log.i(TAG, info.topActivity.getPackageName()
                        + " info.baseActivity.getPackageName()="
                        + info.baseActivity.getPackageName());
                break;
            }
        }
        return isAppRunning;
    }
}
