package com.chehubang.duolejie.modules.carlife.presenter;

import android.content.Intent;
import android.text.TextUtils;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.modules.carlife.activity.GasCardRecordActivity;
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
 * Created by Thinkpad on 2017/12/19.
 */

public class GasCardRecordPresenter extends MvpPresenter<GasCardRecordActivity> {

    public GasCardRecordPresenter(GasCardRecordActivity activity) {
        attachView(activity);
    }

    public void getCardStatus(int action, String id) {
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

        Observable<ResponseBody> verificationCode = service.getOilCardRecord(map);
        loadData(action, verificationCode);
    }

    public void addOilCardOrder(int action, String id, String fee, String num) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", id);
        map.put("total_fee", fee);
        map.put("card_num", num);
        map.put("token", UserInfo.getToken());

        String time = System.currentTimeMillis() + "";
        try {
            String s = id + "|$|" + fee + "|$|" + num + "|$|" + UserInfo.getToken() + "|$|" + time;
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

        Observable<ResponseBody> verificationCode = service.addOilCardOrder(map);
        loadData(action, verificationCode);
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
                if (action == ACTION_DEFAULT + 1) {
                    mvpView.getDataSuccess(data, action);
                } else if (action == ACTION_DEFAULT + 2) {
                    if (TextUtils.equals(Constant.request_success, result.getStatus())) {
                        mvpView.getDataSuccess(data, action);
                    } else {
                        mvpView.getDataFail(result.getDesc(), action);
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
