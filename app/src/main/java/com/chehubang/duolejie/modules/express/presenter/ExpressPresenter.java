package com.chehubang.duolejie.modules.express.presenter;

import android.content.Intent;
import android.text.TextUtils;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.modules.express.activity.ExpressActivity;
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
 * Created by ZZH on 2018/2/1.
 *
 * @Date: 2018/2/1
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description:
 */
public class ExpressPresenter extends MvpPresenter<ExpressActivity> {
    public ExpressPresenter(ExpressActivity activity) {
        attachView(activity);
    }

    public void getExpressMessage(int actionDefault, String id) {
        Map<String, String> map = new HashMap<>(8);
        String time = String.valueOf(System.currentTimeMillis());
        map.put("request_time", time);
        map.put("child_order_id", id);
        String signStr = id + "|$|" + time;
        String sign = RsaTool.encrypt(Constant.OUR_RSA_PUBLIC, signStr);
        map.put("sign", sign);
        String params = JSONUtils.mapToJson(map);
        map.put("param", params);
        Observable<ResponseBody> courier = service.getCourier(map);
        loadData(actionDefault, courier);

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
                if (TextUtils.equals(status, Constant.request_success)) {
                    mvpView.getDataSuccess(data, action);
                } else {
                    mvpView.getDataFail(data, action);
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
