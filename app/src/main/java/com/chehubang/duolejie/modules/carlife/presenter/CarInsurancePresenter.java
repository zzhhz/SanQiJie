package com.chehubang.duolejie.modules.carlife.presenter;

import android.content.Intent;
import android.text.TextUtils;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.modules.carlife.activity.CarInsuranceActivity;
import com.chehubang.duolejie.modules.login.activity.LoginActivity;
import com.chehubang.duolejie.utils.RsaTool;
import com.chehubang.duolejie.utils.log;

import java.util.HashMap;
import java.util.Map;

import common.Utils.JSONUtils;
import common.http.LoadDataSubscriber;
import common.http.RequestResult;
import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by Administrator on 2017/12/17 0017.
 */

public class CarInsurancePresenter extends MvpPresenter<CarInsuranceActivity> {

    public CarInsurancePresenter(CarInsuranceActivity activity) {
        attachView(activity);
    }

    public void addBanner(int action, String type) {
        Map<String, String> map = new HashMap<>();
        String time = System.currentTimeMillis() + "";
        map.put("type", type);
        try {
            String s = type + "|$|" + time;
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
        Observable<ResponseBody> responseBodyObservable = service.getCarLifeBanner(map);
        loadData(action, responseBodyObservable);
    }

    public void addCarinsurance(int action, String tel, String id, String plate, String type, String name) {
        Map<String, String> map = new HashMap<>();
        map.put("tel", tel);
        map.put("user_id", id);
        map.put("license_plate", plate);
        map.put("car_type", type);
        map.put("car_name", name);
        map.put("token", UserInfo.getToken());
        String time = System.currentTimeMillis() + "";
        try {
            String s = tel + "|$|" + id + "|$|" + plate + "|$|" + type + "|$|" + name + "|$|" + UserInfo.getToken() + "|$|" + time;
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
        Observable<ResponseBody> responseBodyObservable = service.addCarinsurance(map);
        loadData(action, responseBodyObservable);
    }

    public void loadData(final int action, Observable<ResponseBody> observable) {
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
                if (action == ACTION_DEFAULT + 2) {
                    String desc = result.getDesc();
                    String msg = null;
                    if (TextUtils.isEmpty(data)) {
                        msg = desc;
                    } else {
                        msg = data;
                    }
                    if (TextUtils.equals(Constant.request_success, result.getStatus())) {
                        mvpView.getDataSuccess(msg, action);
                    } else {
                        mvpView.getDataFail(msg, action);
                    }
                } else if (action == ACTION_DEFAULT + 1) {
                    mvpView.getDataSuccess(data, action);
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
