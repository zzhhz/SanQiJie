package com.chehubang.duolejie.modules.gift.presenter;

import android.text.TextUtils;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.modules.gift.activity.DrawResultActivity;
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
 * Created by user on 2018/1/31.
 *
 * @date: 2018/1/31
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @description:
 */
public class DrawResultPresenter extends MvpPresenter<DrawResultActivity> {

    public DrawResultPresenter(DrawResultActivity activity) {
        attachView(activity);
    }

    public void getDrawResultList(int action, int page) {
        Map<String, String> map = new HashMap<>(8);
        String time = System.currentTimeMillis() + "";
        String id = UserInfo.getInstance().getId();
        map.put("user_id", id);
        map.put("pageNum", String.valueOf(page));
        map.put("limitNum", "120000");
        try {
            String s = id + "|$|" + page + "|$|" + "120000" + "|$|" + time;
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
        Observable<ResponseBody> verificationCode = service.getDrawResultList(map);
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
                if (action == 1) {
                    if (TextUtils.equals(Constant.request_success, status)) {
                        mvpView.getDataSuccess(data, action);
                    } else {
                        mvpView.getDataFail(data, action);
                    }
                }
            }
        });
    }
}
