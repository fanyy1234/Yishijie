package com.bby.yishijie.member.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by fanyy on 2018/4/6.
 */

public class EntityUtil {
    /**
     * 把一个实体类对象转换为json,提取result中的json
     * @param o
     * @return
     */
    public static com.alibaba.fastjson.JSONObject ObjectToJson (Object o){
        String string = com.alibaba.fastjson.JSONObject.toJSON(o).toString();
        LogUtils.e(string);
        try {
            com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(string);
            return  jsonObject.getJSONObject("result");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  new JSONObject();
    }
    /**
     * 把一个实体类对象转换为json
     * @param o
     * @return
     */
    public static com.alibaba.fastjson.JSONObject ObjectToJson2 (Object o){
        String string = com.alibaba.fastjson.JSONObject.toJSON(o).toString();
        LogUtils.e(string);
        try {
            com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(string);
            return  jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  new JSONObject();
    }
}
