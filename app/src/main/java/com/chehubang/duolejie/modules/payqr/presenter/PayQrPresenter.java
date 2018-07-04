package com.chehubang.duolejie.modules.payqr.presenter;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.modules.login.activity.LoginActivity;
import com.chehubang.duolejie.modules.payqr.activity.PayQrActivity;
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
 * Created by ZZH on 2018/1/30.
 *
 * @Date: 2018/1/30
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description:
 */
public class PayQrPresenter extends MvpPresenter<PayQrActivity> {
    public PayQrPresenter(PayQrActivity activity) {
        attachView(activity);
    }

    public void getBrandMsg(int action, String storeId) {
        Map<String, String> map = new HashMap<>();
        String time = System.currentTimeMillis() + "";
        map.put("brand_id", storeId);
        try {
            String s = storeId + "|$|" + time;
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
        Observable<ResponseBody> brandMsg = service.getBrandMsg(map);
        loadData(action, brandMsg);
    }


    public void payBalance(int action, String money, String brand_id, String remark) {
        Map<String, String> map = new HashMap<>(16);
        String time = System.currentTimeMillis() + "";
        map.put("money", money);
        map.put("brand_id", brand_id);
        map.put("remark", remark);
        map.put("user_id", UserInfo.getInstance().getId());
        map.put("token", UserInfo.getToken());

        try {
            String s = brand_id + "|$|" + UserInfo.getInstance().getId() + "|$|" + money + "|$|" + remark + "|$|" + UserInfo.getToken() + "|$|" + time;
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
        Observable<ResponseBody> brandMsg = service.payMoneyGoodsBrandByQRCode(map);
        loadData(action, brandMsg);
    }

    public void loadData(final int action, Observable<ResponseBody> observable) {
        addSubscription(observable, new LoadDataSubscriber() {

            @Override
            protected void _error(String message) {
                log.d(message);
                mvpView.getDataFail(message, action);
            }

            @Override
            protected void _success(Object o) {
                RequestResult result = (RequestResult) o;
                log.d("_success: " + result);
                String data = result.getData();
                String status = result.getStatus();
                if (action == 1) {
                    if (TextUtils.equals(Constant.request_success, status)) {
                        mvpView.getDataSuccess(data, action);
                    }
                } else if (action == 2) {
                    if (TextUtils.equals(Constant.request_success, status)) {
                        mvpView.getDataSuccess("1", action);
                    } else {
                        mvpView.getDataSuccess("2", action);
                    }
                } else if (action == 3) {
                    mvpView.getDataSuccess(result, action);
                } else if (action == 4) {
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
