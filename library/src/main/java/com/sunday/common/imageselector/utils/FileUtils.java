package com.sunday.common.imageselector.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 文件操作类
 * Created by Nereo on 2015/4/8.
 */
public class FileUtils {

    public static String getRandomFileName(){
        return java.util.UUID.randomUUID().toString();
    }


    public static String getVoicePath(Context mContext){
        String var4 = mContext.getExternalFilesDir(null).getAbsolutePath();
        String pathPrefix =  var4 + "/voice";
        File file = new File(pathPrefix);
        if(!file.exists()){
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    public static File createTmpFile(Context context){

        String state = Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED)){
            // 已挂载
            File pic = new File("/sdcard/sunday");
            if(!pic.exists()){
                pic.mkdirs();
            }
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = "multi_image_"+timeStamp+"";
            File tmpFile = new File(pic, fileName+".jpg");
            return tmpFile;
        }else{
            File cacheDir = context.getCacheDir();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = "multi_image_"+timeStamp+"";
            File tmpFile = new File(cacheDir, fileName+".jpg");
            return tmpFile;
        }

    }


}
