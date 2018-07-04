package com.chehubang.duolejie.modules.carlife.presenter;

import android.text.TextUtils;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.modules.carlife.activity.CarRecordActivity;
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
 * Created by ZZH on 2018/2/6.
 *
 * @Date: 2018/2/6
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description:
 */
public class CarRecordPresenter extends MvpPresenter<CarRecordActivity> {

    public CarRecordPresenter(CarRecordActivity activity) {
        attachView(activity);
    }

    public void getCarRecord() {
        Map<String, String> map = new HashMap<>();
        String id = UserInfo.getInstance().getId();
        map.put("user_id", id);
        map.put("pageNum", String.valueOf(1));
        map.put("limitNum", String.valueOf(100));
        String time = String.valueOf(System.currentTimeMillis());
        map.put("request_time", time);
        String tmp = id + "|$|" + 1 + "|$|" + 100 + "|$|" + time;
        map.put("sign", RsaTool.encrypt(Constant.OUR_RSA_PUBLIC, tmp));
        String params = JSONUtils.mapToJson(map);
        map.clear();
        map.put("param", params);
        loadData(1, service.getCarOrder(map));

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
                log.d(result.toString());
                String data = result.getData();
                if (TextUtils.equals(Constant.request_success, result.getStatus())) {
                    mvpView.getDataSuccess(data, action);
                } else {
                    mvpView.getDataFail(result.getDesc(), action);
                }
            }
        });
    }
}
