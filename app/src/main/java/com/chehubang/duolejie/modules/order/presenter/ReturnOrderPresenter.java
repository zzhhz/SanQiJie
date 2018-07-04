package com.chehubang.duolejie.modules.order.presenter;

import android.content.Intent;
import android.text.TextUtils;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.modules.login.activity.LoginActivity;
import com.chehubang.duolejie.modules.order.activity.ReturnOrderActivity;
import com.chehubang.duolejie.utils.RsaTool;
import com.chehubang.duolejie.utils.log;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import common.Utils.JSONUtils;
import common.http.LoadDataSubscriber;
import common.http.RequestResult;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by ZZH on 2018/1/31
 *
 * @date: 2018/1/31 下午2:31
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @Description:
 */
public class ReturnOrderPresenter extends MvpPresenter<ReturnOrderActivity> {

    public ReturnOrderPresenter(ReturnOrderActivity activity) {
        attachView(activity);
    }

    public void getChildReturnOrder(int action, String childId) {
        Map<String, String> map = new HashMap<>(8);
        String time = System.currentTimeMillis() + "";
        map.put("child_order_id", childId);
        map.put("user_id", UserInfo.getInstance().getId());
        map.put("token", UserInfo.getToken());
        try {
            String s = UserInfo.getInstance().getId() + "|$|" + childId + "|$|" + UserInfo.getToken() + "|$|" + time;
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
        Observable<ResponseBody> returnList = service.getReturnList(map);
        loadData(action, returnList);
    }

    /**
     * {
     * user_id:用户id,
     * child_order_id:子订单主键id,
     * logistics_id:物流主键id,
     * logistics_num:物流单号,
     * phone:手机号,
     * return_details:退货说明,
     * return_imgs:上传凭证,
     * returnMoney:可退金额
     * }
     *
     * @param action
     * @param child_order_id
     * @param logistics_id
     * @param logistics_num
     * @param phone
     * @param return_details
     * @param return_imgs
     * @param returnMoney
     */
    public void getOrderReturn(int action, String child_order_id, String logistics_id, String logistics_num, String phone, String return_details, List<String> return_imgs, String returnMoney) {
        Map<String, String> map = new HashMap<>(8);
        String time = System.currentTimeMillis() + "";
        map.put("user_id", UserInfo.getInstance().getId());
        map.put("child_order_id", child_order_id);
        map.put("logistics_id", logistics_id);
        map.put("logistics_num", logistics_num);
        map.put("phone", phone);
        map.put("return_details", return_details);
        StringBuilder imgs = new StringBuilder();
        for (String path : return_imgs) {
            imgs.append(path).append(",");
        }
        map.put("return_imgs", imgs.toString());
        map.put("returnMoney", returnMoney);
        map.put("token", UserInfo.getToken());
        try {
            String s = UserInfo.getInstance().getId() + "|$|" + child_order_id + "|$|" + logistics_id + "|$|" + logistics_num + "|$|" + phone + "|$|" + return_details + "|$|" + imgs.toString() + "|$|" + returnMoney + "|$|" + UserInfo.getToken() + "|$|" + time;
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
        Observable<ResponseBody> orderReturn = service.getOrderReturn(map);
        loadData(action, orderReturn);
    }

    public void loadData(final int action, Observable<ResponseBody> observable) {
        addSubscription(observable, new LoadDataSubscriber() {

            @Override
            protected void _error(String message) {
                log.d(message);
                mvpView.getDataFail(message, action);
            }

            @Override
            protected void _success(Object o) {
                RequestResult result = (RequestResult) o;
                log.d("_success: " + result);
                String data = result.getData();
                String status = result.getStatus();
                if (TextUtils.equals(status, Constant.request_success)) {
                    mvpView.getDataSuccess(data, action);
                } else {
                    mvpView.getDataFail(data, action);
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
