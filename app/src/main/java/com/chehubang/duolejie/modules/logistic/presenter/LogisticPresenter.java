package com.chehubang.duolejie.modules.logistic.presenter;

import android.text.TextUtils;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.modules.logistic.activity.LogisticCompanyActivity;
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
 * Created by user on 2018/1/31.
 *
 * @date: 2018/1/31
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @description:
 */
public class LogisticPresenter extends MvpPresenter<LogisticCompanyActivity> {

    public LogisticPresenter(LogisticCompanyActivity activity) {
        attachView(activity);
    }

    public void getLogisticList() {
        Map<String, String> map = new HashMap<>();
        String value = System.currentTimeMillis() + "";
        map.put("sign", RsaTool.encrypt(Constant.OUR_RSA_PUBLIC, value));
        map.put("request_time", value);
        String params = JSONUtils.mapToJson(map);
        map.clear();
        map.put("param", params);
        Observable<ResponseBody> logisticList = service.getLogisticList(map);
        loadData(1, logisticList);
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
                log.d(result.toString());
                String data = result.getData();
                String status = result.getStatus();
                if (TextUtils.equals(Constant.request_success, status)) {
                    mvpView.getDataSuccess(data, action);
                } else {
                    mvpView.getDataFail(data, action);
                }
            }
        });
    }
}
