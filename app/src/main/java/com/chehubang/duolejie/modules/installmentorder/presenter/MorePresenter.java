package com.chehubang.duolejie.modules.installmentorder.presenter;

import android.text.TextUtils;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.modules.installmentorder.activity.MoreInstallDetailsActivity;
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
 * Created by ZZH on 2018/3/6.
 *
 * @Date: 2018/3/6
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description:
 */
public class MorePresenter extends MvpPresenter<MoreInstallDetailsActivity> {
    public MorePresenter(MoreInstallDetailsActivity activity) {
        attachView(activity);
    }

    public void getOrderListInstall(int action, String orderId) {

        String time = String.valueOf(System.currentTimeMillis());
        Map<String, String> map = new HashMap<>();
        map.put("order_id", orderId);
        try {
            String s = orderId + "|$|" + time;
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
        Observable<ResponseBody> observable = service.getRechargeDetailStagesList(map);

        addAction(action, observable);

    }

    private void addAction(final int action, Observable<ResponseBody> observable) {
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
                    mvpView.getDataFail(result.getDesc(), action);
                }
            }
        });
    }
}
