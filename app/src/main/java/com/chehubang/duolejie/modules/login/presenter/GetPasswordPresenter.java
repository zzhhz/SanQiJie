package com.chehubang.duolejie.modules.login.presenter;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.modules.login.activity.GetPasswordActivity;
import com.chehubang.duolejie.utils.RsaTool;

import java.util.HashMap;
import java.util.Map;

import common.Utils.JSONUtils;
import common.http.LoadDataSubscriber;
import common.http.RequestResult;
import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by Thinkpad on 2017/12/18.
 */

public class GetPasswordPresenter extends MvpPresenter<GetPasswordActivity> {

    public GetPasswordPresenter(GetPasswordActivity activity) {
        attachView(activity);
    }

    public void getVerificationCode(int action, String... data) {
        Map<String, String> map = new HashMap<>();
        map.put("app_login_id", data[0]);
        map.put("type", data[1]);
        String time=System.currentTimeMillis()+"";
        try {
            String s=data[0]+"|$|"+data[1]+"|$|"+time;
            String sign= RsaTool.encrypt(Constant.OUR_RSA_PUBLIC, s);
            map.put("sign", sign);
        } catch (Exception e) {
            map.put("sign", "");
            e.printStackTrace();
        }
        map.put("request_time", time);
        String params = JSONUtils.mapToJson(map);
        map.clear();
        map.put("param",params);

        Observable<ResponseBody> verificationCode = service.getVerificationCode(map);
        loadData(action, verificationCode);
    }

    public void updataPassword(int action,String...data){
        Map<String, String> map = new HashMap<>();
        map.put("app_login_id", data[0]);
        map.put("password", data[1]);
        map.put("auth_code",data[2]);
        String time=System.currentTimeMillis()+"";
        try {
            String s=data[0]+"|$|"+data[1]+"|$|"+data[2]+"|$|"+time;
            String sign= RsaTool.encrypt(Constant.OUR_RSA_PUBLIC, s);
            map.put("sign", sign);
        } catch (Exception e) {
            map.put("sign", "");
            e.printStackTrace();
        }
        map.put("request_time", time);
        String params = JSONUtils.mapToJson(map);
        map.clear();
        map.put("param",params);
        Observable<ResponseBody> verificationCode = service.updataPassword(map);
        loadData(action, verificationCode);
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
                if (action== ACTION_DEFAULT+2){
                    mvpView.getDataSuccess(result,action);
                }
            }
        });
    }
}
