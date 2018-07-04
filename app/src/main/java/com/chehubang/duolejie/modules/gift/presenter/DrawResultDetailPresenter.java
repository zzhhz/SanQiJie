package com.chehubang.duolejie.modules.gift.presenter;

import android.text.TextUtils;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.modules.gift.activity.DrawResultDetailActivity;
import com.chehubang.duolejie.utils.RsaTool;
import com.chehubang.duolejie.utils.log;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import common.Utils.JSONUtils;
import common.http.LoadDataSubscriber;
import common.http.RequestResult;
import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by ZZH on 2018/2/22.
 *
 * @Date: 2018/2/22
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description: 抽奖记录详情
 */
public class DrawResultDetailPresenter extends MvpPresenter<DrawResultDetailActivity> {
    public DrawResultDetailPresenter(DrawResultDetailActivity activity) {
        attachView(activity);
    }

    public void getDrawResultDetail(int action, String coupon_id) {
        Map<String, String> map = new HashMap<>(8);
        String time = System.currentTimeMillis() + "";
        map.put("coupon_id", coupon_id);
        map.put("pageNum", String.valueOf(1));
        map.put("limitNum", "120");
        try {
            String s = coupon_id + "|$|" + 1 + "|$|" + "120" + "|$|" + time;
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
        Observable<ResponseBody> verificationCode = service.getDrawResultUserList(map);
        loadData(action, verificationCode);
    }

    public void loadData(final int action, Observable<ResponseBody> observable) {
        addSubscription(observable, new LoadDataSubscriber() {

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
                    mvpView.getDataSuccess(data, action);
                } else {
                    mvpView.getDataFail(result.getDesc(), action);
                }
            }
        });
    }
}
