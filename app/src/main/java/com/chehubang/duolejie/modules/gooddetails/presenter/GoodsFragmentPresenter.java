package com.chehubang.duolejie.modules.gooddetails.presenter;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import common.Log.LogUtils;
import common.Utils.JSONUtils;
import common.http.LoadDataSubscriber;
import common.http.RequestResult;
import okhttp3.ResponseBody;
import rx.Observable;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.modules.gooddetails.fragment.GoodsFragment;
import com.chehubang.duolejie.modules.login.activity.LoginActivity;
import com.chehubang.duolejie.utils.RsaTool;
import com.chehubang.duolejie.utils.log;

/**
 * Created by ZZH on 2018/1/30
 *
 * @date: 2018/1/30 上午9:26
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @Description: 商品详情
 */
public class GoodsFragmentPresenter extends MvpPresenter<GoodsFragment> {

    public GoodsFragmentPresenter(GoodsFragment fragment) {
        attachView(fragment);
    }

    public void getGoodsInfoData(int action, String id, String user_id) {
        Map<String, String> map = new HashMap<>();
        String time = System.currentTimeMillis() + "";
        try {
            String s = id + "|$|" + user_id + "|$|" + time;
            String sign = RsaTool.encrypt(Constant.OUR_RSA_PUBLIC, s);
            map.put("sign", sign);
        } catch (Exception e) {
            map.put("sign", "");
            e.printStackTrace();
        }
        map.put("request_time", time);
        map.put("goods_id", id);
        map.put("user_id", user_id);
        String params = JSONUtils.mapToJson(map);
        map.clear();
        map.put("param", params);
        Observable<ResponseBody> typelist = service.getGoodsDetailsData(map);
        loadData(action, typelist);
    }

    public void getGoodsInfoMoreList(int action, String id) {
        Map<String, String> map = new HashMap<>();
        String time = System.currentTimeMillis() + "";
        try {
            String s = id + "|$|" + time;
            String sign = RsaTool.encrypt(Constant.OUR_RSA_PUBLIC, s);
            map.put("sign", sign);
        } catch (Exception e) {
            map.put("sign", "");
            e.printStackTrace();
        }
        map.put("request_time", time);
        map.put("goods_id", id);
        String params = JSONUtils.mapToJson(map);
        map.clear();
        map.put("param", params);
        Observable<ResponseBody> typelist = service.getGoodsInfoMoreList(map);
        loadData(action, typelist);
    }

    public void addToShoppingList(int action, String user_id, String goods_id, String num, String colorid, String color, String specid, String spec) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("goods_ids", goods_id);
        map.put("goods_buy_nums", num);
        map.put("spec_id", specid);
        if (TextUtils.isEmpty(colorid)) {
            colorid = "";
        }
        if (TextUtils.isEmpty(color)) {
            color = "";
        }
        map.put("color_id", colorid);
        map.put("goods_color", color);
        map.put("goods_spec", spec);
        map.put("token", UserInfo.getToken());
        String time = System.currentTimeMillis() + "";
        try {
            String s = user_id + "|$|" + goods_id + "|$|" + num + "|$|" + specid + "|$|" + colorid + "|$|" + color + "|$|" + spec + "|$|" + UserInfo.getToken() + "|$|" + time;
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
        Observable<ResponseBody> typelist = service.addToShoppingList(map);
        loadData(action, typelist);
    }

    public void getGoodsInfoSpecByColorList(int action, String goods_id, String colorId, String specId) {
        Map<String, String> map = new HashMap<>();
        map.put("goods_id", goods_id);
        map.put("spec_id", specId);
        if (TextUtils.isEmpty(colorId)) {
            colorId = "";
        }

        map.put("color_id", colorId);
        String time = System.currentTimeMillis() + "";
        try {
            String s = goods_id + "|$|" + specId + "|$|" + colorId + "|$|" + time;
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
        Observable<ResponseBody> typelist = service.getGoodsInfoSpecByColorList(map);
        loadData(action, typelist);
    }

    public void loadData(final int action, Observable<ResponseBody> observable) {
        addSubscription(observable, new LoadDataSubscriber() {

            @Override
            protected void _error(String message) {
                Log.d("-----", "_error: " + message);
                mvpView.getDataFail(message, action);
            }

            @Override
            protected void _success(Object o) {
                RequestResult result = (RequestResult) o;
                Log.d("-----", "_success: " + result);
                String data = result.getData();
                String status = result.getStatus();
                if (action == ACTION_DEFAULT + 1) {
                    if (TextUtils.equals(Constant.request_success, status)) {
                        mvpView.getDataSuccess(data, action);
                    }
                } else if (action == ACTION_DEFAULT + 2) {
                    mvpView.getDataSuccess(result, action);
                } else if (action == ACTION_DEFAULT + 3) {
                    mvpView.getDataSuccess(result, action);
                } else if (action == ACTION_DEFAULT + 4) {
                    mvpView.getDataSuccess(data, action);
                }
            }

            @Override
            public void jumpLogin() {
                Intent intent = new Intent(mvpView.getActivity(), LoginActivity.class);
                mvpView.startActivity(intent);
            }
        });
    }
}
