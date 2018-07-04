package com.chehubang.duolejie.modules.installmentorder.presenter;

import android.text.TextUtils;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.model.InstallDetailsBean;
import com.chehubang.duolejie.model.InstallOrderBean;
import com.chehubang.duolejie.modules.installmentorder.activity.InstallDetailsActivity;
import com.chehubang.duolejie.utils.RsaTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import common.Log.LogUtils;
import common.Utils.JSONUtils;
import common.http.LoadDataSubscriber;
import common.http.RequestResult;
import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by Administrator on 2017/12/26 0026.
 */

public class InstallDetailsPresenter extends MvpPresenter<InstallDetailsActivity> {
    public InstallDetailsPresenter(InstallDetailsActivity activity) {
        attachView(activity);
    }

    public void getRechargeDetailList(int action,String id) {
        Map<String, String> map = new HashMap<>();
       map.put("order_id",id);
        String time = System.currentTimeMillis() + "";
        ;
        try {
            String s = id + "|$|" +time;
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
        Observable<ResponseBody> verificationCode = service.getRechargeDetailList(map);
        loginAction(action, verificationCode);
    }
    private void loginAction(final int action, Observable<ResponseBody> observable) {
        addSubscription(observable, new LoadDataSubscriber() {
            private ArrayList<InstallDetailsBean> InstallDetailsBeans = new ArrayList<>();
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
                       mvpView.getDataSuccess(data,action);
                    }
                }
            }
        });
    }
}
