package com.chehubang.duolejie.modules.setting.presenter;

import android.text.TextUtils;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.model.NoticeListBean;
import com.chehubang.duolejie.modules.setting.activitiy.NoticeActivity;
import com.chehubang.duolejie.utils.RsaTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.Utils.JSONUtils;
import common.http.LoadDataSubscriber;
import common.http.RequestResult;
import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by Thinkpad on 2017/12/25.
 */

public class NoticePresenter extends MvpPresenter<NoticeActivity> {

    public NoticePresenter(NoticeActivity activity) {
        attachView(activity);
    }

    public void getNoticeList(int action, String... data) {
        Map<String, String> map = new HashMap<>();
        map.put("pageNum", data[0]);
        map.put("limitNum", data[1]);
        String time = System.currentTimeMillis() + "";
        ;
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
        Observable<ResponseBody> verificationCode = service.getNoticeList(map);
        loginAction(action, verificationCode);
    }

    private void loginAction(final int action, Observable<ResponseBody> observable) {
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
                List<NoticeListBean> noticeListBeans = new ArrayList<>();
                if (TextUtils.equals(Constant.request_success, status)) {
                    if (action == ACTION_DEFAULT + 1) {
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            JSONArray messageList = jsonObject.getJSONArray("messageList");
                            for (int i = 0; i < messageList.length(); i++) {
                                JSONObject jsonObject1 = messageList.getJSONObject(i);
                                NoticeListBean noticeListBean = JSONUtils.GsonToBean(jsonObject1.toString(), NoticeListBean.class);
                                noticeListBeans.add(noticeListBean);
                            }
                            mvpView.getDataSuccess(noticeListBeans,action);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }
}
