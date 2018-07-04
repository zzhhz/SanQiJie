package com.chehubang.duolejie.modules.login.presenter;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.modules.login.activity.RegisterCodeActivity;
import com.chehubang.duolejie.utils.RsaTool;

import java.util.HashMap;
import java.util.Map;

import common.Utils.JSONUtils;
import common.http.LoadDataSubscriber;
import common.http.RequestResult;
import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by ZZH on 2018/1/29.
 *
 * @Date: 2018/1/29
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description:
 */
public class RegisterCodePresenter extends MvpPresenter<RegisterCodeActivity> {
    public RegisterCodePresenter(RegisterCodeActivity activity) {
        attachView(activity);
    }

    public void getVerificationCode(int action, String... data) {
        Map<String, String> map = new HashMap<>();
        map.put("app_login_id", data[0]);
        map.put("type", data[1]);
        String time = System.currentTimeMillis() + "";
        try {
            String s = data[0] + "|$|" + data[1] + "|$|" + time;
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
        Observable<ResponseBody> verificationCode = service.getVerificationCode(map);
        loadData(action, verificationCode);
    }

    public void register(int action, String account, String pwd, String code) {
        Map<String, String> map = new HashMap<>();
        map.put("app_login_id", account);
        map.put("password", pwd);
        map.put("auth_code", code);
        map.put("recommend_user_id", RegisterCodeActivity.city);
        String time = System.currentTimeMillis() + "";
        try {
            String s = account + "|$|" + pwd + "|$|" + RegisterCodeActivity.city + "|$|" + code + "|$|" + time;
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
        Observable<ResponseBody> register = service.register(map);
        loadData(action, register);
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
                if (action == ACTION_DEFAULT + 2) {
                    mvpView.getDataSuccess(result, action);
                }
            }
        });
    }
}
