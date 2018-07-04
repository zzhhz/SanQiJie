package com.chehubang.duolejie.modules.setting.presenter;

import android.text.TextUtils;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.modules.setting.activitiy.SuggestActivity;
import com.chehubang.duolejie.utils.RsaTool;
import com.chehubang.duolejie.utils.log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import common.Utils.JSONUtils;
import common.http.LoadDataSubscriber;
import common.http.RequestResult;
import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by ZZH on 2018/2/12.
 *
 * @Date: 2018/2/12
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description:
 */
public class SuggestPresenter extends MvpPresenter<SuggestActivity> {

    public SuggestPresenter(SuggestActivity activity) {
        attachView(activity);
    }

    public void submitSuggest(String id, String content, String tel, String type, ArrayList<String> suggest_imgs) {
        String time = String.valueOf(System.currentTimeMillis());
        Map<String, String> map = new HashMap<>(8);
        map.put("request_time", time);
        map.put("user_id", id);
        map.put("content", content);
        map.put("tel", tel);
        map.put("title", type);
        StringBuilder builder = new StringBuilder();
        if (suggest_imgs != null) {
            for (String img : suggest_imgs) {
                builder.append(img).append(",");
            }
        }
        if (builder.length() > 1) {
            builder.deleteCharAt(builder.length() - 1);
        }


        map.put("suggest_imgs", builder.toString());
        String signStr = id + "|$|" + content + "|$|" + tel + "|$|" + type + "|$|" + builder.toString() + "|$|" + time;

        String sign = RsaTool.encrypt(Constant.OUR_RSA_PUBLIC, signStr);
        map.put("sign", sign);
        String param = JSONUtils.mapToJson(map);
        map.clear();
        map.put("param", param);
        Observable<ResponseBody> observable = service.submitSuggest(map);
        loadData(2, observable);
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
