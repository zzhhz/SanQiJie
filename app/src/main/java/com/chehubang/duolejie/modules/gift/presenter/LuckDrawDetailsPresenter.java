package com.chehubang.duolejie.modules.gift.presenter;

import android.content.Intent;
import android.text.TextUtils;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.model.BannerBean;
import com.chehubang.duolejie.modules.gift.activity.LuckDrawDetailsActivity;
import com.chehubang.duolejie.modules.home.activity.ScanActivity;
import com.chehubang.duolejie.modules.login.activity.LoginActivity;
import com.chehubang.duolejie.utils.RsaTool;
import com.chehubang.duolejie.utils.log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

import common.Log.LogUtils;
import common.Utils.JSONUtils;
import common.http.LoadDataSubscriber;
import common.http.RequestResult;
import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by fuyi on 2017/12/21.
 */

public class LuckDrawDetailsPresenter extends MvpPresenter<LuckDrawDetailsActivity> {
    public LuckDrawDetailsPresenter(LuckDrawDetailsActivity activity) {
        attachView(activity);
    }

    public void getLuckDrawDetailsData(int action, String user_id, String brand_id, String coupon_id) {
        Map<String, String> map = new HashMap<>(8);
        map.put("user_id", user_id);
        map.put("brand_id", brand_id);
        map.put("coupon_id", coupon_id);
        map.put("pageNum", "1");
        map.put("limitNum", "20");
        String time = System.currentTimeMillis() + "";
        try {
            String s = user_id + "|$|" + coupon_id + "|$|" + brand_id + "|$|" + 1 + "|$|" + 20 + "|$|" + time;
            String sign = RsaTool.encrypt(Constant.OUR_RSA_PUBLIC, s);
            map.put("sign", sign);
        } catch (Exception e) {
            map.put("sign", "");
            e.printStackTrace();
        }
        map.put("request_time", time);
        String params = JSONUtils.mapToJson(map);
        log.d(params);
        map.clear();
        map.put("param", params);
        Observable<ResponseBody> verificationCode = service.getLuckDrawDetailsData(map);
        loadData(action, verificationCode);
    }

    //抽奖页面确认接口
    public void getConfirm(int action, String user_ids, String coupon_id, int spent, String num) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", user_ids);
        map.put("coupon_id", coupon_id);
        map.put("spend", String.valueOf(spent));
        map.put("goods_buy_num", num);
        map.put("token", UserInfo.getToken());
        String time = System.currentTimeMillis() + "";
        try {
            String s = user_ids + "|$|" + coupon_id + "|$|" + spent + "|$|" + num + "|$|" + UserInfo.getToken() + "|$|" + time;
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
        Observable<ResponseBody> verificationCode = service.getConfirm(map);
        loadData(action, verificationCode);
    }

    public void loadData(final int action, Observable<ResponseBody> observable) {
        addSubscription(observable, new LoadDataSubscriber() {

            private ArrayList<BannerBean> categoryLifeBeans;

            @Override
            protected void _error(String message) {
                mvpView.getDataFail(message, action);
            }

            @Override
            protected void _success(Object o) throws JSONException {
                RequestResult result = (RequestResult) o;
                log.d(result.toString());
                String data = result.getData();
                String status = result.getStatus();
                if (TextUtils.equals(Constant.request_success, status)) {
                    if (action == ACTION_DEFAULT + 1) {

                        mvpView.getDataSuccess(data, action);
                    }
                } else if (action == ACTION_DEFAULT + 2) {
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
