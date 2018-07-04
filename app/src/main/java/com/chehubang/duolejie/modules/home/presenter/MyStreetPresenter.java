package com.chehubang.duolejie.modules.home.presenter;

import android.text.TextUtils;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.modules.home.activity.MyStreetActivity;
import com.chehubang.duolejie.utils.RsaTool;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import common.Utils.JSONUtils;
import common.http.LoadDataSubscriber;
import common.http.RequestResult;
import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by ZZH on 2018/1/30
 *
 * @date: 2018/1/30 上午11:40
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @Description:
 */
public class MyStreetPresenter extends MvpPresenter<MyStreetActivity> {

    public MyStreetPresenter(MyStreetActivity activity) {
        attachView(activity);
    }

    public void getLuckDrawData(int action, String ids, int mPage) {
        Map<String, String> map = new HashMap<>();
        String time = System.currentTimeMillis() + "";
        String id = UserInfo.getInstance().getId();
        map.put("search", ids);
        map.put("pageNum", String.valueOf(mPage));
        map.put("limitNum", "150");
        try {
            String s = id + "|$|" + mPage + "|$|" + "150" + "|$|" + time;
            String sign = RsaTool.encrypt(Constant.OUR_RSA_PUBLIC, s);
            map.put("sign", sign);
        } catch (Exception e) {
            map.put("sign", "");
        }
        map.put("request_time", time);
        String params = JSONUtils.mapToJson(map);
        map.clear();
        map.put("param", params);
        Observable<ResponseBody> verificationCode = service.getLuckDrawData(map);
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

            @Override
            protected void _error(String message) {
                mvpView.getDataFail(message, action);
            }

            @Override
            protected void _success(Object o) throws JSONException {
                RequestResult result = (RequestResult) o;
                String data = result.getData();
                String status = result.getStatus();
                if (TextUtils.equals(Constant.request_success, status)) {
                    if (action == ACTION_DEFAULT + 1) {
                        if (TextUtils.equals(Constant.request_success, status)) {
                            mvpView.getDataSuccess(data, action);
                        }
                    } else if (action == (ACTION_DEFAULT + 2)) {
                        if (TextUtils.equals(Constant.request_success, status)) {
                            mvpView.getDataSuccess(data, action);
                        }
                    } else if (action == (ACTION_DEFAULT + 3)) {
                        JSONObject obj = new JSONObject(data);
                        String qrcode_img = obj.getString("qrcode_img");
                        mvpView.getDataSuccess(qrcode_img, action);
                    } else if (action == ACTION_DEFAULT + 4) {
                        mvpView.getDataSuccess(result, action);
                    }
                } else {
                    mvpView.getDataFail(result.getDesc(), action);
                }
            }
        });
    }

    public void getBanner(int action, int id) {
        Map<String, String> map = new HashMap<>();
        String time = System.currentTimeMillis() + "";
        map.put("type", String.valueOf(id));
        try {
            String s = id + "|$|" + time;
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
        Observable<ResponseBody> verificationCode = service.getCarLifeBanner(map);
        loadData(action, verificationCode);
    }

    public void getShareData(int action) {
        Map<String, String> map = new HashMap<>();
        String time = System.currentTimeMillis() + "";
        map.put("user_id", UserInfo.getInstance().getId());
        try {
            String s = UserInfo.getInstance().getId() + "|$|" + time;
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
        Observable<ResponseBody> verificationCode = service.getQrCodeImg(map);
        loadData(action, verificationCode);
    }
}