package com.bby.yishijie.member.http;

import com.bby.yishijie.member.http.gson.DoubleJsonDeserializer;
import com.bby.yishijie.member.http.gson.FloatJsonDeserializer;
import com.bby.yishijie.member.http.gson.IntegerJsonDeserializer;
import com.bby.yishijie.member.http.gson.StringJsonDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 刘涛 on 2017/6/5.
 */

public class AppOperator {
    private static ExecutorService EXECUTORS_INSTANCE;
    private static Gson GSON_INSTANCE;

    public static Executor getExecutor() {
        if (EXECUTORS_INSTANCE == null) {
            synchronized (AppOperator.class) {
                if (EXECUTORS_INSTANCE == null) {
                    EXECUTORS_INSTANCE = Executors.newFixedThreadPool(6);
                }
            }
        }
        return EXECUTORS_INSTANCE;
    }

    public static void runOnThread(Runnable runnable) {
        getExecutor().execute(runnable);
    }



    public static Gson createGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        //gsonBuilder.setExclusionStrategies(new SpecificClassExclusionStrategy(null, Model.class));
        gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");

        JsonDeserializer deserializer = new IntegerJsonDeserializer();
        gsonBuilder.registerTypeAdapter(int.class, deserializer);
        gsonBuilder.registerTypeAdapter(Integer.class, deserializer);

        deserializer = new FloatJsonDeserializer();
        gsonBuilder.registerTypeAdapter(float.class, deserializer);
        gsonBuilder.registerTypeAdapter(Float.class, deserializer);

        deserializer = new DoubleJsonDeserializer();
        gsonBuilder.registerTypeAdapter(double.class, deserializer);
        gsonBuilder.registerTypeAdapter(Double.class, deserializer);

        deserializer = new StringJsonDeserializer();
        gsonBuilder.registerTypeAdapter(String.class, deserializer);



        return gsonBuilder.create();
    }

    public synchronized static Gson getGson() {
        if (GSON_INSTANCE == null)
            GSON_INSTANCE = createGson();
        return GSON_INSTANCE;
    }
}
