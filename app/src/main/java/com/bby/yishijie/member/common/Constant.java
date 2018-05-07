package com.bby.yishijie.member.common;

import com.bby.yishijie.member.http.ApiClient;

/**
 * Created by 刘涛 on 2017/4/26.
 */

public class Constant {

    public final static int TYPE_SELECT = 1;
    public final static int TYPE_ADDCART = 2;
    public final static int TYPE_BUY_NOW = 3;

    //微信回调区分 订单类型
    public final static int TYPE_NORMAL = 11;//普通订单
    public final static int TYPE_GROUP = 22;//团购订单
    public final static int TYPE_OPEN_SHOP = 33;//开店订单

    //支付宝
    public static String payNotifyUrl1 = ApiClient.API_URL+"/mobi/member/AiAlipayNotify";//申请店主支付回调
    public static String payNotifyUrl2 = ApiClient.API_URL+"/mobi/cart/AkAlipayNotify";
    public static String ALI_APP_ID = "2018042802602827";
    public static String PARTNER = "2088921577248740";// 签约合作者身份ID
    public static String SELLER = "3439305185@qq.com";// 签约卖家支付宝账号
    public static String RSA_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCaJHFfW3nyI4/LaXVOm9+zob/z/phiSGEIKEnOVKyN9ZXw+sKnT+n60gIZBCdxcrRvEiVl9cw99J7+dJaNCES8FUmhGQffqzx6Be+F8xzo8b7jG5Xv4avrk5xPBrAdFIJda+d6dRMUdImItIDVoHrjMFEO2BrtTXKShWBmGgQPv2pQBG3hOwXjt2wjCqs4mt6Guq2ZAY1oYuZ2/bMV1AGlc1GLIcqfZ6aY4Agk3U6m4ibgb+PTDxU/otEoZy/DCA8nvzCZcO8KstWTSRZUtevdZNLP60TEZWAGNl9SrSpGtAqw34JFRPUR/8aKmrS0ZrSDoCP+K4QS7vemtZK5gL+7AgMBAAECggEAKAFHT1ldd+OU4VUYdfNshqF3QSsVf4SLcmOKbDt7oUhYxvc959CMyGUS2E1Es2PywlTyUdkOThIb5ax6mlXICQx5VI87hOhbRMwfkyM6oJaFVUoGiwCF7xkbW0NAF/wHrZlgtV6HOirYuxslyMviuO4ES5b4vhf4oWBuWXFtFmzOZLk8lHd0hmNYDDyDw++24qj19YdXB/tEkxMlcVsNQCUIKCffrXaf1n9DPdpgWDv2lk9sCxN4/RKUtnCpHFj0hXK7AdcqOo/vBQbg/wOsZciwovgab5qYQJTsXBv4CZUm8CRtfoYuFGo45rlV8FWYHKeRJ9yZyMripWLkPlGAaQKBgQDj05w/IXDvz/5c+ZGDSzoNaPPndo/1O6SF2VwqMcSOx9WcKfuaI5UziI0RICQEICz3FahKyB5FosHymdle3dOI36OzMAH+7dKcZNf/j5KqA4MBPLP3x9HgYq4mmqDyQVeNsLOmzgx9hg3PWc/5Ng5/aqOLto6cbn+ERYQvPAgBnQKBgQCtNC74QSamLSF14BeFYuNXLwbtK0oEbkVFFSOU1WxooM5scLI9d620rwTPTCkw9e3Piatm+eFaUDNlfMnQwn81d7HEDU3yB0KpiWFrsEg+/AvVlW6yY35S/sxAlw0ifWtKXXsqbFf9S27DNKvl9PuAolShUaAZ2JdvKU0OxATTNwKBgQDKZ4W40Iz1gWQKVe7hJkQ9BbErQjpDNOnFwIYRNoXaE8/DHclZ96LmVbp+LH/S6oMIQUVyqre6Rg7o6aYkkg8M2yMkXtJnGgKyzyTW90blpJkO0E9TI0NihkkSIu46S7LtPr9cwlJ2clJylOggD9ZxfNwvk9C/SawgZ2I4VK0IQQKBgDdDBRF0SA6RK7HeU+LdEEgK2xzkyPLs8/D8s6OTDMm9NN9ofZYbDzMs/E3fsOzMKpojBKsQYxRbrmpdDE8tH0VB8VfqHspwApbxHEIttn9SD7yPtwf3sqBsZ2LcTvaIKalKdjes5c4NOUPTFF2EcpIyOOpXjstwTbDL7qgdZqS1AoGARo736B9+SsOmaNbkT6z9uzrJGuS1xyyisESJoB7AuAzVLvgk6DDduC1yASehy9kB9/rx1B5IoBLwsfk9rjSjIHsJb40j/c0V50UP9LJbKxwBh5Lhbemj2ORIdQ0PpuBNzOaCO2hgmZWTeRHwTl53khrfiIWZ3Xo8DMPOOFxgnb0=";

    //微信
    public static String APP_ID = "wxf575fa0e9bc46293";
    public static String APPSecret = "b4ec0841d90cf65f88b739ab026b60b1";

    public static String PARTER_ID = "1488877922";


}
