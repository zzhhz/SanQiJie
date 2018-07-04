package com.chehubang.duolejie.http;

import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.model.WechatInfoBean;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import common.http.HttpApi;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Thinkpad on 2017/12/27.
 */

public class OkHttpManager {

    private static OkHttpManager instance = null;

    public static OkHttpManager getInstance() {
        if (instance == null) {
            instance = new OkHttpManager();
        }
        return instance;
    }


    //创建一个OkHttpClient对象
    private  OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build();

    private String get(String url) throws IOException {

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            return "";
        }
    }

    public Map<String, String> getAccessToken(String appid, String secret, String code, String grant_type) {
        try {
            String res = get(Constant.GET_ACCESS_TOKEN + "appid=" + appid + "&secret=" + secret + "&code=" + code + "&grant_type=" + grant_type);
            if (res != null) {
                JSONObject response = new JSONObject(res);
                String access_token = response.getString("access_token");
                String openid = response.getString("openid");
                Map<String, String> map = new HashMap<>();
                map.put("access_token", access_token);
                map.put("openid", openid);
                return map;
            }else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public WechatInfoBean getWechatMyInfo(String access_token, String openid) {
        try {
            String res = get(Constant.GET_WECHAT_INFO + "access_token=" + access_token + "&openid=" + openid);
            if (res != null) {
                WechatInfoBean wechatInfoBean = new Gson().fromJson(res, WechatInfoBean.class);
                return wechatInfoBean;
            }else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
