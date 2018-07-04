package com.chehubang.duolejie.modules.home.presenter;

import android.text.TextUtils;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.model.GoodsTypeChildBean;
import com.chehubang.duolejie.modules.home.activity.HomeSearchActivity;
import com.chehubang.duolejie.utils.RsaTool;

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
 * Created by Administrator on 2018/1/4 0004.
 */

public class HomeSearchPresenter extends MvpPresenter<HomeSearchActivity> {
    public HomeSearchPresenter(HomeSearchActivity activity) {
        attachView(activity);
    }

    public void getGoodsSearchList(int action, String searchKey) {
        Map<String, String> map = new HashMap<>();
        String time = System.currentTimeMillis() + "";
        map.put("searchKey", searchKey);
        try {

            String s = searchKey + "|$|" + time;
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
        Observable<ResponseBody> verificationCode = service.getGoodsSearchList(map);
        loadHomeData(action, verificationCode);
    }

    public void getHotKeyWord(int action) {
        Map<String, String> map = new HashMap<>();
        String time = System.currentTimeMillis() + "";
        try {

            String s = time;
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
        Observable<ResponseBody> verificationCode = service.getHotSearchData(map);
        loadHomeData(action, verificationCode);
    }


    public void loadHomeData(final int action, Observable<ResponseBody> observable) {
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
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            JSONArray goodsList = jsonObject.getJSONArray("goodsSearchList");
                            ArrayList<GoodsTypeChildBean> beans = JSONUtils.GsonjsonToArrayList(goodsList.toString(), GoodsTypeChildBean.class);
                            mvpView.getDataSuccess(beans, action);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (action == ACTION_DEFAULT + 2) {
                        mvpView.getDataSuccess(data, action);
                    }
                }
            }
        });
    }
}
