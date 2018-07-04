package com.chehubang.duolejie.modules.address.presenter;

import android.content.Intent;
import android.text.TextUtils;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.modules.address.activity.AddAddressActivity;
import com.chehubang.duolejie.modules.login.activity.LoginActivity;
import com.chehubang.duolejie.utils.RsaTool;

import java.util.HashMap;
import java.util.Map;

import common.Log.LogUtils;
import common.Utils.JSONUtils;
import common.http.LoadDataSubscriber;
import common.http.RequestResult;
import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by Thinkpad on 2017/12/28.
 */

public class AddAddressPresenter extends MvpPresenter<AddAddressActivity> {

    public AddAddressPresenter(AddAddressActivity activity) {
        attachView(activity);
    }

    public void getAddressList(int action, String id) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", id);
        map.put("token", UserInfo.getToken());
        String time = System.currentTimeMillis() + "";
        try {
            String s = id + "|$|" + UserInfo.getToken() + "|$|" + time;
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
        Observable<ResponseBody> typelist = service.getUserAddressList(map);
        loadData(action, typelist);
    }

    public void getLocationData(int action) {
        Map<String, String> map = new HashMap<>();
        String time = System.currentTimeMillis() + "";
        try {
            String s = time;
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
        Observable<ResponseBody> typelist = service.getUserAddressListData(map);
        loadData(action, typelist);
    }

    public void saveLocationInfo(int action, String... param) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", param[0]);
        map.put("id", param[1]);
        map.put("user_tel", param[2]);
        map.put("user_name", param[3]);
        map.put("province_id", param[4]);
        map.put("city_id", param[5]);
        map.put("detail_address", param[6]);
        map.put("is_default", param[7]);
        map.put("token", UserInfo.getToken());
        String time = System.currentTimeMillis() + "";
        try {
            String s = param[0] + "|$|" + param[1] + "|$|" + param[2] + "|$|" + param[3] + "|$|" + param[4] + "|$|" + param[5] + "|$|" + param[6] + "|$|" + param[7] + "|$|" + UserInfo.getToken() + "|$|" + time;
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
        Observable<ResponseBody> typelist = service.saveLocationData(map);
        loadData(action, typelist);
    }

    public void getUserAddressListData(int action) {
        Map<String, String> map = new HashMap<>();
        String time = System.currentTimeMillis() + "";
        try {
            String s = time;
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
        Observable<ResponseBody> typelist = service.getUserAddressListData(map);
        loadData(action, typelist);
    }


    private void loadData(final int action, Observable<ResponseBody> observable) {
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
                    if (action == ACTION_DEFAULT + 1) {
                        mvpView.getDataSuccess(data, action);
                    } else if (action == ACTION_DEFAULT + 2) {
                        mvpView.getDataSuccess(result, action);
                    }
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
