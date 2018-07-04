package com.chehubang.duolejie.modules.carlife.presenter;

import android.content.Intent;
import android.text.TextUtils;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.modules.carlife.activity.CashierGasCardActivity;
import com.chehubang.duolejie.modules.chargecenter.activity.CashierCenterActivity;
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
 * Created by Beidouht on 2017/12/20.
 */

public class CashierGasCardPresenter extends MvpPresenter<CashierGasCardActivity> {
    public CashierGasCardPresenter(CashierGasCardActivity activity) {
        attachView(activity);
    }

    public void getBalance(int action, String id, String order_id) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", id);
        map.put("order_id", order_id);
        String time = System.currentTimeMillis() + "";
        try {
            String s = id + "|$|" + order_id + "|$|" + time;
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
        Observable<ResponseBody> typelist = service.getBalance(map);
        loadData(action, typelist);
    }

    public void payRechargeOrder(int action, String id, String order_id) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", id);
        map.put("order_id", order_id);
        map.put("token", UserInfo.getToken());
        String time = System.currentTimeMillis() + "";
        try {
            String s = id + "|$|" + order_id + "|$|" + UserInfo.getToken() + "|$|" + time;
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
        Observable<ResponseBody> typelist = service.payRechargeOrder(map);
        loadData(action, typelist);
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
                String data = result.getData();
                String status = result.getStatus();
                if (TextUtils.equals(Constant.request_success, status)) {
                    if (action == ACTION_DEFAULT + 1) {
                        mvpView.getDataSuccess(data, action);
                    } else if (action == ACTION_DEFAULT + 2) {
                        mvpView.getDataSuccess(data, action);
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
