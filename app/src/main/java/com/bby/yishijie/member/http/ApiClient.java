package com.bby.yishijie.member.http;

import android.text.TextUtils;

import com.sunday.common.converter.GsonConverterFactory;
import com.sunday.common.logger.Logger;
import com.sunday.common.utils.EncryptUtils;


import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.BufferedSink;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 2016/1/26.
 */
public class ApiClient {
    private static Retrofit imAdapter;
    //private static MemberService memberService;

//    public final static String API_URL = "http://192.168.0.104:8080";
   public final static String API_URL = "http://mobile.ysjkj.net";
   //public final static String API_URL = "http://test-mobile.haowukongtou.com";

    public final static String DETAIL_URL="http://admin.ysjkj.net/DataController/AfH5Info";
    public final static String GIFT_URL="http://admin.ysjkj.net/DataController/AfGetByType";
    public final static String SHARE_URL = "http://weixin.haowukongtou.com/authorizationPage.html?param=";
    public final static String SHARE_URL1= "http://weixin.haowukongtou.com/authorizationPage1.html?param=";
    public final static String CUSTOMER_URL="https://kefu.easemob.com/webim/im.html?tenantId=47178";
    public final static String PROTOCOL_URL="http://admin.ysjkj.net/SystemInfoController/getByType";
    public final static String APK_URL = "http://admin.ysjkj.net/share.png";
    public final static String OUR_PHONE = "0663-8513685";
    private static OkHttpClient client;
    static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

    public static ApiService getApiAdapter() {
        if (imAdapter == null) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(30, TimeUnit.SECONDS);
            builder.readTimeout(30, TimeUnit.SECONDS);
            builder.addInterceptor(loggingInterceptor);
            builder.build();
            client = builder.build();
            imAdapter = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        ApiService apiService = imAdapter.create(ApiService.class);
        return apiService;
    }

    /*public static MemberService getMemberAdapter(){
        if (imAdapter == null) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(30, TimeUnit.SECONDS);
            builder.readTimeout(30, TimeUnit.SECONDS);
            builder.addInterceptor(loggingInterceptor);
            builder.build();
            client = builder.build();
            imAdapter = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        memberService = imAdapter.create(MemberService.class);
        return memberService;
    }*/

    public static OkHttpClient getClient() {
        return client;
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }

    static class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {}

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {return new X509Certificate[0];}
    }


    private static String sign(String nonce, String timestamp) {
        StringBuilder sb = new StringBuilder();
        sb.append("wBH4YJUYIa6a").append(nonce).append(timestamp);
        return EncryptUtils.sha1(sb.toString());

    }


    static class AddPostParamRequestBody extends RequestBody {

        final RequestBody body;
        StringBuilder encodedParams = new StringBuilder();

        AddPostParamRequestBody(RequestBody body, HashMap<String, String> params) {
            this.body = body;
            try {
                encodedParams.append('&');
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    encodedParams.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                    encodedParams.append('=');
                    encodedParams.append(URLEncoder.encode(TextUtils.isEmpty(entry.getValue()) ? "" : entry.getValue(), "UTF-8"));
                    encodedParams.append('&');
                }
            } catch (Exception e) {
                e.printStackTrace();
                Logger.e("param", "参数编码异常");
            }
        }

        @Override
        public long contentLength() throws IOException {
            return body.contentLength() + encodedParams.length();
        }

        @Override
        public MediaType contentType() {
            return body.contentType();
        }

        @Override
        public void writeTo(BufferedSink bufferedSink) throws IOException {
            body.writeTo(bufferedSink);
            bufferedSink.writeString(encodedParams.toString(), Charset.forName("UTF-8"));
        }

    }


}
