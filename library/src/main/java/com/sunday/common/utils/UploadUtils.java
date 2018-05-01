package com.sunday.common.utils;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 上传多图的对象拼接
 * Created by wang on 2016/3/28.
 */
public class UploadUtils {
    public static RequestBody getRequestBody(List<String> filePath){
        MultipartBody.Builder buildernew = new MultipartBody.Builder()
                .setType(MultipartBody.MIXED)
                /*.addFormDataPart("title", title)*/;   //Here you can add the fix number of data.
        for (int i = 0; i < filePath.size(); i++) {
            File file = new File(filePath.get(i));
            if (file.exists()) {
                buildernew.addFormDataPart("images",file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
            }
        }
        MultipartBody requestBody = buildernew.build();
        return requestBody;
    }
    public static RequestBody getRequestBody(String filePath){
        MultipartBody.Builder buildernew = new MultipartBody.Builder()
                .setType(MultipartBody.MIXED)
                /*.addFormDataPart("title", title)*/;   //Here you can add the fix number of data.

            File file = new File(filePath);
            if (file.exists()) {
                buildernew.addFormDataPart("images",file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
        }
        MultipartBody requestBody = buildernew.build();
        return requestBody;
    }
}
