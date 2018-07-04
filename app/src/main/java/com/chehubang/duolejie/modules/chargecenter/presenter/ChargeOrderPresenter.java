package com.chehubang.duolejie.modules.chargecenter.presenter;

import android.content.Intent;
import android.text.TextUtils;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.modules.chargecenter.activity.ChargeOrderActivity;
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
 * @Date: 2018/1/29 21:40
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description: 填写订单
 */
public class ChargeOrderPresenter extends MvpPresenter<ChargeOrderActivity> {

    public ChargeOrderPresenter(ChargeOrderActivity activity) {
        attachView(activity);
    }

    public void payRechargeOrder(int action, String id, String goods_ids, String goods_buy_nums, String is_shop_cart) {
        Map<String, String> map = new HashMap<>();
        map.put("goods_ids", goods_ids);
        map.put("goods_buy_nums", goods_buy_nums);
        map.put("is_shop_cart", is_shop_cart);
        map.put("user_id", id);
        String time = System.currentTimeMillis() + "";
        try {
            String s = id + "|$|" + goods_ids + "|$|" + goods_buy_nums + "|$|" + is_shop_cart + "|$|" + time;
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
        Observable<ResponseBody> typelist = service.getChargeDetails(map);
        loadData(action, typelist);
    }

    public void getOrderNo(int action, String... data) {
        Map<String, String> map = new HashMap<>();
        map.put("total_fees", data[0]);
        map.put("goods_ids", data[1]);
        map.put("goods_buy_nums", data[2]);
        map.put("goods_spec_ids", data[3]);
        String data4 = "";
        if (TextUtils.isEmpty(data[4])) {
            map.put("goods_color_ids", data4);
        } else {
            data4 = data[4];
            map.put("goods_color_ids", data4);
        }

        map.put("all_price", data[5]);
        map.put("user_address_id", data[6]);
        map.put("is_shop_cart", data[7]);
        map.put("user_id", data[8]);
        map.put("ticket_id", "");
        map.put("freight", data[9]);
        map.put("goods_spec", data[10]);
        map.put("goods_color", data[11]);
        map.put("token", UserInfo.getToken());
        String time = System.currentTimeMillis() + "";
        try {
            String s = data[0] + "|$|" + data[1] + "|$|" + data[2] + "|$|" + data[3] + "|$|" + data4 + "|$|" + data[10] + "|$|" + data[11] + "|$|" + data[5] + "|$|" + data[6] + "|$|" + "" + "|$|" + data[7] + "|$|" + data[8] + "|$|" + data[9] + "|$|" + UserInfo.getToken() + "|$|" + time;
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
        Observable<ResponseBody> typelist = service.getGoodsOrderNo(map);
        loadData(action, typelist);
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
                String data = result.getData();
                String status = result.getStatus();

                if (TextUtils.equals(Constant.request_success, status)) {
                    if (action == ACTION_DEFAULT + 1) {
                        mvpView.getDataSuccess(data, action);
                    } else if (action == ACTION_DEFAULT + 2) {
                        mvpView.getDataSuccess(data, action);
                    }
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
