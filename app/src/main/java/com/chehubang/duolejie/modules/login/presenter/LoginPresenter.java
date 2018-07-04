package com.chehubang.duolejie.modules.login.presenter;

import android.text.TextUtils;
import android.widget.Toast;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.http.OkHttpManager;
import com.chehubang.duolejie.model.WechatInfoBean;
import com.chehubang.duolejie.modules.login.activity.LoginActivity;
import com.chehubang.duolejie.utils.RsaTool;
import com.chehubang.duolejie.utils.log;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import common.Utils.JSONUtils;
import common.http.LoadDataSubscriber;
import common.http.RequestResult;
import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by Thinkpad on 2017/12/10.
 */

public class LoginPresenter extends MvpPresenter<LoginActivity> {
    public LoginPresenter(LoginActivity activity) {
        attachView(activity);
    }

    public void login(int action, String... data) {
        Map<String, String> map = new HashMap<>();
        map.put("app_login_id", data[0]);
        map.put("password", data[1]);
        map.put("jpush_id", data[2]);
        String time = System.currentTimeMillis() + "";
        ;
        try {
            String s = data[0] + "|$|" + data[1] + "|$|" + data[2] + "|$|" + time;
            String sign = RsaTool.encrypt(Constant.OUR_RSA_PUBLIC, s);
            map.put("sign", sign);
        } catch (Exception e) {
            map.put("sign", "");
            e.printStackTrace();
        }
        map.put("request_time", time);
        String params = JSONUtils.mapToJson(map);
        map.clear();
        map.put("param", params);
        Observable<ResponseBody> verificationCode = service.login(map);
        loginAction(action, verificationCode);
    }

    public void getAccessToken(String code) {
        Map<String, String> accessToken = OkHttpManager.getInstance().getAccessToken(Constant.APP_ID, Constant.APP_SECRET, code, "authorization_code");
        if (accessToken != null) {
            String access_token = accessToken.get("access_token");
            String openid = accessToken.get("openid");
            if (access_token != null && openid != null) {
                getWechatMyinfo(access_token, openid);
            }
        }
    }

    public void getWechatMyinfo(String... params) {
        WechatInfoBean wechatMyInfo = OkHttpManager.getInstance().getWechatMyInfo((String) params[0], (String) params[1]);
        if (wechatMyInfo != null) {
            String headimgurl = wechatMyInfo.getHeadimgurl();
            String nickname = wechatMyInfo.getNickname();
            loginByThirdPart(ACTION_DEFAULT + 2, (String) params[1], nickname, headimgurl);
        }
    }

    public void loginByThirdPart(int action, String... data) {
        Map<String, String> map = new HashMap<>();
        map.put("app_login_id", data[0]);
        map.put("nick_name", data[1]);
        map.put("user_header", data[2]);
        map.put("type", "weixin");
        map.put("jpush_id", JPushInterface.getRegistrationID(mvpView));
        String time = System.currentTimeMillis() + "";
        ;
        try {
            String s = data[0] + "|$|" + data[1] + "|$|" + data[2] + "|$|" + "weixin" + "|$|" + JPushInterface.getRegistrationID(mvpView) + "|$|" + time;
            String sign = RsaTool.encrypt(Constant.OUR_RSA_PUBLIC, s);
            map.put("sign", sign);
        } catch (Exception e) {
            map.put("sign", "");
            e.printStackTrace();
        }
        map.put("request_time", time);
        String params = JSONUtils.mapToJson(map);
        map.clear();
        map.put("param", params);
        Observable<ResponseBody> verificationCode = service.loginByThirdPart(map);
        loginAction(action, verificationCode);
    }

    public void loginAction(final int action, Observable<ResponseBody> observable) {
        addSubscription(observable, new LoadDataSubscriber() {
            @Override
            protected void _error(String message) {
                mvpView.getDataFail(message, action);
            }

            @Override
            protected void _success(Object o) {
                RequestResult result = (RequestResult) o;
                log.d(result.toString());
                String data = result.getData();
                String status = result.getStatus();
                if (action == ACTION_DEFAULT + 1) {
                    mvpView.getDataSuccess(result, action);
                } else if (action == ACTION_DEFAULT + 2) {
                    mvpView.getDataSuccess(data, action);
                }
                /*if (TextUtils.equals(Constant.request_success,status)){
                    if (action == ACTION_DEFAULT+1){
                        mvpView.getDataSuccess(data,action);
                    }else if (action ==ACTION_DEFAULT+2){
                        mvpView.getDataSuccess(data,action);
                    }
                }*/
            }
        });
    }
}
