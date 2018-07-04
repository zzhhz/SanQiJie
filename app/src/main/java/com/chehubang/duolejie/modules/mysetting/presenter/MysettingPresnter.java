package com.chehubang.duolejie.modules.mysetting.presenter;

import android.content.Intent;
import android.text.TextUtils;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.modules.login.activity.LoginActivity;
import com.chehubang.duolejie.modules.mysetting.activity.MySettingActivity;
import com.chehubang.duolejie.utils.RsaTool;

import java.util.HashMap;
import java.util.Map;

import common.Utils.JSONUtils;
import common.http.LoadDataSubscriber;
import common.http.RequestResult;
import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by Beidouht on 2017/12/21.
 */

public class MysettingPresnter extends MvpPresenter<MySettingActivity> {

    public MysettingPresnter(MySettingActivity activity) {
        attachView(activity);
    }

    public void updataInfo(int action, String... data) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", data[0]);
        map.put("fieldName", data[1]);
        map.put("fieldNameValue", data[2]);
        map.put("updateFieldNameNum", data[3]);
        map.put("token", UserInfo.getToken());
        String time = System.currentTimeMillis() + "";
        try {
            String s = data[0] + "|$|" + data[1] + "|$|" + data[2] + "|$|" + data[3] + "|$|" + UserInfo.getToken() + "|$|" + time;
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
        Observable<ResponseBody> verificationCode = service.updataUserInfo(map);
        loginAction(action, verificationCode);
    }

    public void loadheader(int action, String bytes) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", UserInfo.getInstance().getId());
        map.put("fieldName", "user_header");
        map.put("fieldNameValue", bytes);
        map.put("updateFieldNameNum", String.valueOf(1));
        String time = System.currentTimeMillis() + "";
        try {
            String s = UserInfo.getInstance().getId() + "|$|" + "user_header" + "|$|" + bytes + "|$|" + 1 + "|$|" + time;
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
        Observable<ResponseBody> verificationCode = service.updataUserInfo(map);
        loginAction(action, verificationCode);
    }

    private void loginAction(final int action, Observable<ResponseBody> observable) {
        addSubscription(observable, new LoadDataSubscriber() {
            @Override
            protected void _error(String message) {
                mvpView.getDataFail(message, action);
            }

            @Override
            protected void _success(Object o) {
                RequestResult result = (RequestResult) o;
                String data = result.getData();
                String status = result.getStatus();
                if (TextUtils.equals(Constant.request_success, status)) {
                    mvpView.getDataSuccess(status, action);
                }
            }

            @Override
            public void jumpLogin() {
                Intent intent = new Intent(mvpView, LoginActivity.class);
                mvpView.startActivity(intent);
            }
        });
    }
}
