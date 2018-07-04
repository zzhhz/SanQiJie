package com.chehubang.duolejie.modules.home.presenter;


import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.modules.home.fragment.ShoppingFragment;
import com.chehubang.duolejie.modules.login.activity.LoginActivity;
import com.chehubang.duolejie.utils.RsaTool;

import java.util.HashMap;
import java.util.Map;

import common.Utils.JSONUtils;
import common.http.LoadDataSubscriber;
import common.http.RequestResult;
import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by Thinkpad on 2017/12/11.
 */

public class ShoppingPresenter extends MvpPresenter<ShoppingFragment> {

    public ShoppingPresenter(ShoppingFragment fragment) {
        attachView(fragment);
    }


    public void getShoppingList(int action, String id, int mPage) {
        Map<String, String> map = new HashMap<>();
        String time = System.currentTimeMillis() + "";
        map.put("user_id", id);
        map.put("pageNum", String.valueOf(mPage));
        map.put("limitNum", "50");
        map.put("token", UserInfo.getToken());
        try {
            String s = id + "|$|" + mPage + "|$|" + "50" + "|$|" + UserInfo.getToken() + "|$|" + time;
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
        Observable<ResponseBody> verificationCode = service.getShoppinglist(map);
        loadShoppingData(action, verificationCode);
    }

    public void deleteItem(int action, String id) {
        Map<String, String> map = new HashMap<>();
        map.put("ids", id);
        String time = System.currentTimeMillis() + "";
        ;
        try {
            String s = id + "|$|" + time;
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
        Observable<ResponseBody> verificationCode = service.deleteItem(map);
        loadShoppingData(action, verificationCode);
    }

    public void loadShoppingData(final int action, Observable<ResponseBody> observable) {
        addSubscription(observable, new LoadDataSubscriber() {

            @Override
            protected void _error(String message) {
                Log.d("-----", "_success: " + message);
                mvpView.getDataFail(message, action);
            }

            @Override
            protected void _success(Object o) {
                RequestResult result = (RequestResult) o;
                Log.d("-----", "_success: " + result);
                String data = result.getData();
                String status = result.getStatus();
                if (action == ACTION_DEFAULT + 1) {
                    if (TextUtils.equals(Constant.request_success, status)) {
                        mvpView.getDataSuccess(data, action);
                    }
                } else if (action == ACTION_DEFAULT + 2) {
                    mvpView.getDataSuccess(result, action);
                }

            }

            @Override
            public void jumpLogin() {
                Intent intent = new Intent(mvpView.getActivity(), LoginActivity.class);
                mvpView.startActivity(intent);
            }
        });
    }
}
