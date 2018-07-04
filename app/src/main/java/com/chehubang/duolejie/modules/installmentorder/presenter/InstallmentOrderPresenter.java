package com.chehubang.duolejie.modules.installmentorder.presenter;

import android.text.TextUtils;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.model.InstallOrderBean;
import com.chehubang.duolejie.utils.RsaTool;

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

public class InstallmentOrderPresenter extends MvpPresenter<MainView> {

    public InstallmentOrderPresenter(MainView activity) {
        attachView(activity);
    }

    public void getRechargeList(int action, String... data) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", data[0]);
        map.put("pageNum", data[1]);
        map.put("limitNum", data[2]);
        String time = System.currentTimeMillis() + "";
        ;
        try {
            String s = data[0] + "|$|" + data[1] + "|$|" + data[2] + "|$|" + time;
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
        Observable<ResponseBody> verificationCode = service.getRechargeList(map);
        loginAction(action, verificationCode);
    }

    private void loginAction(final int action, Observable<ResponseBody> observable) {
        addSubscription(observable, new LoadDataSubscriber() {
            private ArrayList<InstallOrderBean> installOrderBeans = new ArrayList<>();

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
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            JSONArray installOrderList = jsonObject.getJSONArray("rechargeOilCardList");
                            for (int i = 0; i < installOrderList.length(); i++) {
                                JSONObject jsonObject2 = installOrderList.getJSONObject(i);
                                InstallOrderBean installOrderBean = JSONUtils.GsonToBean(jsonObject2.toString(), InstallOrderBean.class);
                                installOrderBeans.add(installOrderBean);
                            }
                            mvpView.getDataSuccess(installOrderBeans, action);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (action == ACTION_DEFAULT + 2) {
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            JSONArray installOrderList = jsonObject.getJSONArray("rechargePhoneList");
                            for (int i = 0; i < installOrderList.length(); i++) {
                                JSONObject jsonObject2 = installOrderList.getJSONObject(i);
                                InstallOrderBean installOrderBean = JSONUtils.GsonToBean(jsonObject2.toString(), InstallOrderBean.class);
                                installOrderBeans.add(installOrderBean);
                            }
                            mvpView.getDataSuccess(installOrderBeans, action);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }
}
