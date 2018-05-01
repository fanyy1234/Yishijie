package com.sunday.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;


import com.sunday.common.common.MemberConst;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 *
 */
public class SharePerferenceUtils {
    private volatile  static SharePerferenceUtils instance;
    private SharedPreferences mSharedPreferences;


    private SharePerferenceUtils(Context context) {
        mSharedPreferences = context.getSharedPreferences("sunday",
                Activity.MODE_PRIVATE);
    }

    public synchronized static SharePerferenceUtils getIns(Context context) {
        if (null == instance) {
            instance = new SharePerferenceUtils(context);
        }

        return instance;
    }

    public void putBoolean(String key, boolean value) {
        mSharedPreferences.edit().putBoolean(key, value).commit();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return mSharedPreferences.getBoolean(key, defValue);
    }

    public void putInt(String key, int value) {
        mSharedPreferences.edit().putInt(key, value).commit();
    }

    public int getInt(String key, int defValue) {
        return mSharedPreferences.getInt(key, defValue);
    }

    public void putString(String key, String value) {
        mSharedPreferences.edit().putString(key, value).commit();
    }

    public String getString(String key, String defValue) {
        return mSharedPreferences.getString(key, defValue);
    }

    public void putLong(String key, long value) {
        mSharedPreferences.edit().putLong(key, value).commit();
    }

    public long getLong(String key, long defValue) {
        return mSharedPreferences.getLong(key, defValue);
    }

    public void removeKey(String key) {
        mSharedPreferences.edit().remove(key).apply();
    }



    public void saveOAuth(Object member){
        // 创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(member);
            // 将字节流编码成base64的字符窜
            String oAuth_Base64 = new String(Base64.encode(baos.toByteArray(),Base64.DEFAULT));
            putString("oAuth", oAuth_Base64);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object getOAuth(){
        String memberBase64 = mSharedPreferences.getString("oAuth", "");
        //读取字节
        byte[] base64 = Base64.decode(memberBase64,Base64.DEFAULT);
        Object member = null;
        //封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            //再次封装
            ObjectInputStream bis = new ObjectInputStream(bais);
            //读取对象
            member = bis.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            removeKey(MemberConst.IS_LOGIN);
            removeKey("oAuth");
            return null;
        }
        return member;
    }


}
