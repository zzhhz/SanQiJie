package com.chehubang.duolejie.modules.carlife.presenter;

import android.content.Intent;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.modules.carlife.activity.BindCardActivity;
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
 * Created by Administrator on 2017/12/17 0017.
 */

public class BindCardPresenter extends MvpPresenter<BindCardActivity> {
    public BindCardPresenter(BindCardActivity activity) {
        attachView(activity);
    }

    public void addCardNumber(int action, String id, String cardid) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", id);
        map.put("card_num", cardid);
        map.put("token", UserInfo.getToken());
        String time = System.currentTimeMillis() + "";
        try {
            String s = id + "|$|" + cardid + "|$|" + UserInfo.getToken() + "|$|" + time;
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
        Observable<ResponseBody> responseBodyObservable = service.addOilCard(map);
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
                String data = result.getData();
                if (action == ACTION_DEFAULT + 1) {
                    mvpView.getDataSuccess(result, action);
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
