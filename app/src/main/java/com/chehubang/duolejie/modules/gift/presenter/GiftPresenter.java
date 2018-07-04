package com.chehubang.duolejie.modules.gift.presenter;

import android.content.Intent;
import android.text.TextUtils;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.model.BannerBean;
import com.chehubang.duolejie.modules.home.fragment.GiftFragment;
import com.chehubang.duolejie.modules.login.activity.LoginActivity;
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
import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by fuyi on 2017/12/20.
 */

public class GiftPresenter extends MvpPresenter<GiftFragment> {

    public GiftPresenter(GiftFragment fragment) {
        attachView(fragment);
    }

    //6.1.5 获取商户信息接口
    public void getGoodsBrandInfo(int action, String type) {
        Map<String, String> map = new HashMap<>();
        map.put("is_own", type);
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
        log.d(params);
        map.clear();
        map.put("param", params);
        Observable<ResponseBody> verificationCode = service.getGoodsBrandInfo(map);
        loadData(action, verificationCode);
    }

    public void getLuckDrawData(int action, String ids, int mPage) {
        Map<String, String> map = new HashMap<>();
        String time = System.currentTimeMillis() + "";
        String id = UserInfo.getInstance().getId();
        map.put("search", ids);
        map.put("pageNum", String.valueOf(mPage));
        map.put("limitNum", "150");
        map.put("user_id", id);
        try {
            String s = ids + "|$|" + mPage + "|$|" + 150 + "|$|" + id + "|$|" + time;
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

            private ArrayList<BannerBean> categoryLifeBeans;

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
                if (action == ACTION_DEFAULT + 1) {
                    if (TextUtils.equals(Constant.request_success, status)) {
                        mvpView.getDataSuccess(data, action);
                    }
                } else if (action == ACTION_DEFAULT + 2) {
                    if (TextUtils.equals(Constant.request_success, status)) {
                        mvpView.getDataSuccess(data, action);
                    }
                } else if (action == ACTION_DEFAULT + 3) {
                    mvpView.getDataSuccess(result, action);
                } else if (action == ACTION_DEFAULT + 4) {
                    if (TextUtils.equals(Constant.request_success, status)) {
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            JSONArray advertisList = jsonObject.getJSONArray("advertisementList");
                            categoryLifeBeans = new ArrayList<>();
                            for (int i = 0; i < advertisList.length(); i++) {
                                JSONObject o1 = (JSONObject) advertisList.get(i);
                                BannerBean advertisBean = JSONUtils.GsonToBean(o1.toString(), BannerBean.class);
                                categoryLifeBeans.add(advertisBean);
                            }
                            mvpView.getDataSuccess(categoryLifeBeans, action);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (action == ACTION_DEFAULT + 5) {
                    if (TextUtils.equals(Constant.request_success, status)) {
                        mvpView.getDataSuccess(result.getDesc(), action);
                    } else {
                        mvpView.getDataFail(result.getDesc(), action);
                    }
                } else if (action == ACTION_DEFAULT + 6) {
                    if (TextUtils.equals(Constant.request_success, status)) {
                        JSONObject obj = new JSONObject(data);
                        String qrcode_img = obj.getString("qrcode_img");
                        mvpView.getDataSuccess(qrcode_img, action);
                    } else {
                        mvpView.getDataFail(result.getDesc(), action);
                    }
                } else if (action == 10) {
                    if (TextUtils.equals(Constant.request_success, status)) {
                        mvpView.getDataSuccess(data, action);
                    }
                }
            }

            @Override
            public void jumpLogin() {
                Intent intent = new Intent(mvpView.getActivity(), LoginActivity.class);
                mvpView.startActivity(intent);
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

    public void inputCode(int action, String code) {
        Map<String, String> map = new HashMap<>();
        String time = System.currentTimeMillis() + "";
        map.put("user_id", UserInfo.getInstance().getId());
        map.put("invite_code", code);
        try {
            String s = UserInfo.getInstance().getId() + "|$|" + code + "|$|" + time;
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
        Observable<ResponseBody> observable = service.writeInviteCode(map);
        loadData(action, observable);

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
