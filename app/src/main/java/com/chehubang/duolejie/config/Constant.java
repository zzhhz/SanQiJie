package com.chehubang.duolejie.config;

import common.http.LoadDataSubscriber;

/**
 * Created by Beidouht on 2017/10/26.
 */

public class Constant {


    /**
     * 请求的状态参数
     */
    public static final String request_success = "0";

    public static final String OUR_RSA_PUBLIC = "9mtTU3mgo0Mm2Aox";

    //支付宝
    public static final String PARTNER = "2088821969549531";// 商户PID
    public static final String SELLER = " zp@chehubang.com";// 商户收款账号
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCTsHBXJT6qMT0rz32KLgHLyHJlN5oFTIgXkMpIgp2SfFe5iF7ZOem9EiXKw6QXRz09mEmXQS1WrWnCV+JqiUc5XXN06fdTyLngyyXbmzy3uV/fJAOZO9B428crmyWK+t+zFGVbvPKZOt2Z0EPRu78psp31UbnSP4adrwgUMCmJDtkSssot2ZOJhgt7G1Ph0mEbYtrs7AyQ1TxLT5fsTblRzXed+XLNlTiEJqYLvRXM3/Hdh0geuC6byU67ORkG1U+bmCOw3S6qxGPC/uux7v1nQWcmtBnFYngl1V8FkKapsfLSLGLCOOfoRH1sPwplJmG8yFYDGaAVPKSZarw/hO/fAgMBAAECggEAFBuyv/q/NHbtyDhA9H5dGjzZbnSKkOwhn2vETukRwS8S1pz9pP/gljCRWrEWd2Dosqk4Y9Ko9KM5wg8rMawtOV9C6Aku9oZe4H9oUSUquC0SI5SItOVS9hGeZY6DJxAPQxek0T/BnCFKqLWVdknkti2umgcdkMW7c16wb+c60IEyufzeReUgE3P7OGukzxq82D0qD2DGa6EKM3ltqwSGbJMzGGKRcBP/A05uFOApOmw1qg9Y6ROdGzY5dH3d0WR9hHPbylVfLTDKSvG2IhAvJ57Zti9ei0aoaphpqrVniaz+kf4EHWtSVGo6MOtYumpLvoqHkMkumN7P3FKQwAboIQKBgQDaMm+ZG4fACvPXMzMAdR0DDaJmyYVzw/OuHFntJe/BoMRwF5bBB4zNVmGT0Q3RFp5Vlur9NJx34H3+gzyWeEMFZmijPVlxIoqFhDmRAvgMSAZfpEYqx2Kr1IhDVbWCDjecBFboT+WG1iWvN1Y7xAlgubwv6rIqbIO780SEDZEPLwKBgQCtRs/SdYSUVNNeTNFAqy3sf+/0n/LIEdahIflHJpZjTfN+JBMrzUdrSO0rJukMAQJ2I6MfJnTvJBbDVKrDeYhsUlo2zHozX8Y0jVLhGiQX8e3YDiz1bcxHpfD6M8yBCz0M5jQYMyqs599w2HSCJh52amjJEth1SNuLkzr9Kbp+UQKBgEB5KLqgBOO9GMIc8dQPxuvRCoy+vT0wyH9JB1VooUignYrVnYkaFwPRo3GPEzsWKOD3gCYjdpyBbBlZY/y+OmeNFBwwE463eyDqqivsKalPWvrFyqn3UnOdkEn9OG3ej5X9PkTU3uiRvH7ipgtw6Kh5GV87RTv95osR3cv7SkKnAoGAchNuerYXpLf7t9eG47ZOs6a+ySQMaB8mWErPnEIRPCtzpJYHcQziegU27vPc9aQshK6Nk9JzpYTy34H8MljusbG95tnTkcL58FsiN0RIguMpnNYoTyQMGxdqgK5dco99j+J2ZYA6Fj9JYTd/7Lc/0uHXPpmDnCNtNoSlj0pLlyECgYAHR2xhfTIJNn19SRdg7cwD1e1EDMHsMnh8Y9BsM2YfFwfUF+Brcs5t42lux3Aqe7dQSymP5Bn+/wYjOetfBJSYNQR80zCK9M+2V2vucvm/qH7Moyxieb1OuQaLQ2YCzWTEEoBVypArAtXr5xYFTWNlIYgtUkHlCypZFi82QHANxA==";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhvDDq0WgreqSXnz8SkbCEVpO6vQpByvo+CuSBOJ+tRmTLB1zqxaH+cZJF9UDqvWL1HPPZc9Tlh0Hp0CV9N4wLJpsFwKHt9DUzHx+GaUK/tUTQ0pLxcVyGCFuaxTAmdMuhY0x311prrqlXKuslpCxkJmBUtpntg1EETT6Vcq3yAxhClnqAbn6WQKTcv28IkOnoh6Bi2Wh0xs+jdh+bXi9sgbtrBqScfBubK48y/g3bd4cd6mfoPR4RQqrmD4vVydUrwrRHNbXTsjJszeUSbhbnhkDDPIwnF530awjaTLVa9DwXWlaao6fb89z9MKw6T9rf2eRz+tEAbAu1LLAz067cQIDAQAB";

    //微信
    public static final String APP_ID = "wx548c930041cfcb6d";
    public static final String APP_SECRET = "d8db4aee6abedff84f92fefb11b68669";

    //微信官方接口,获取accesstoken
    public static final String GET_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?";

    //微信官方接口,获取用户信息
    public static final String GET_WECHAT_INFO = "https://api.weixin.qq.com/sns/userinfo?";


    /**
     * 刷新全部
     */
    public static final String EVENT_REFRESH_ORDER_TOTAL = "com.zzh.refresh.order.total";
    public static final String EVENT_REFRESH_USER = "com.zzh.refresh.user";
    public static final String EVENT_REFRESH_HOME_WASH = "com.zzh.refresh.home.wash";
    public static final String EVENT_REFRESH_HOME_REPAIR = "com.zzh.refresh.home.repair";
    public static final String EVENT_SHARE_SUCCESS = "com.zzh.share.success";

    public static final String EVENT_REFRESH_MONEY = "com.zzh.refresh.money";
    public static final String ACTION_LOGOUT = "com.zzh.refresh.money";
    public static final String ACTION_JUMP_LOGIN = LoadDataSubscriber.ACTION_JUMP_LOGIN;
}
