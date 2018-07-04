package com.chehubang.duolejie.modules.order.presenter;

import android.text.TextUtils;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.model.OrderBean;
import com.chehubang.duolejie.utils.RsaTool;
import com.chehubang.duolejie.utils.log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import common.Utils.JSONUtils;
import common.http.LoadDataSubscriber;
import common.http.RequestResult;
import common.mvp.activity.MainView;
import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by Thinkpad on 2017/12/25.
 */

public class OrderPresenter extends MvpPresenter<MainView> {

    public OrderPresenter(MainView activity) {
        attachView(activity);
    }

    public void getOderList(int action, String... data) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", data[0]);
        map.put("status", data[1]);
        map.put("pageNum", data[2]);
        map.put("limitNum", data[3]);
        String time = System.currentTimeMillis() + "";
        try {
            String s = data[0] + "|$|" + data[1] + "|$|" + data[2] + "|$|" + data[3] + "|$|" + time;
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
        Observable<ResponseBody> verificationCode = service.getOrderData(map);
        loginAction(action, verificationCode);
    }

    public void onConfirmGoods(int action, String orderId) {
        Map<String, String> map = new HashMap<>();
        map.put("child_order_id", orderId);
        map.put("user_id", UserInfo.getInstance().getId());
        String time = System.currentTimeMillis() + "";
        try {
            String s = orderId + "|$|" + UserInfo.getInstance().getId() + "|$|" + time;
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
        Observable<ResponseBody> confirmGoods = service.confirmGoods(map);
        loginAction(action, confirmGoods);
    }
    public void getBanner(int action, String type) {
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        String time = System.currentTimeMillis() + "";
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
        Observable<ResponseBody> confirmGoods = service.getCarLifeBanner(map);
        loginAction(action, confirmGoods);
    }



    private void loginAction(final int action, Observable<ResponseBody> observable) {
        addSubscription(observable, new LoadDataSubscriber() {
            private ArrayList<OrderBean> orderBeans = new ArrayList<>();

            @Override
            protected void _error(String message) {
                mvpView.getDataFail(message, action);
            }

            @Override
            protected void _success(Object o) {
                RequestResult result = (RequestResult) o;
                log.d(result.toString());
                String data = result.getData();
                String status = result.getStatus();
                if (TextUtils.equals(Constant.request_success, status)) {
                    if (action == ACTION_DEFAULT + 1) {
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            log.d(jsonObject.toString());
                            JSONArray orderList = jsonObject.getJSONArray("orderList");
                            for (int i = 0; i < orderList.length(); i++) {
                                JSONObject jsonObject2 = orderList.getJSONObject(i);
                                OrderBean orderBean = JSONUtils.GsonToBean(jsonObject2.toString(), OrderBean.class);
                                orderBeans.add(orderBean);
                            }
                            mvpView.getDataSuccess(orderBeans, action);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        mvpView.getDataSuccess(data, action);
                    }
                }
            }
        });
    }
}
