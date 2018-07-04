package com.chehubang.duolejie.modules.store.presenter;

import android.text.TextUtils;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.modules.store.activity.StoreActivity;
import com.chehubang.duolejie.utils.RsaTool;
import com.chehubang.duolejie.utils.log;

import java.util.HashMap;
import java.util.Map;

import common.Utils.JSONUtils;
import common.http.LoadDataSubscriber;
import common.http.RequestResult;
import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by ZZH on 2018/1/31.
 *
 * @Date: 2018/1/31
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description:
 */
public class StorePresenter extends MvpPresenter<StoreActivity> {
    public StorePresenter(StoreActivity activity) {
        attachView(activity);
    }

    /**
     * {
     * id:推荐用户主键id 可以传空字符串,
     * merchant_name:店家名称,
     * brand_name:商户名称,
     * brand_header:商家头像,
     * brand_introduction:商家简介,
     * brand_address:商家详细地址,
     * brand_tel:联系方式,
     * map_address:地图定位,
     * bus_license:营业执照,
     * brand_href:链接地址,
     * guide_img:导流图文,
     * zhifubao:支付宝
     * }
     **/
    public void submit(int action, String storeName, String storeUserName,
                       String address, String addressDetail, String info, String yingyezhizhao, String logo, String daoliu, String phone, String zhifubao,
                       String type) {
        Map<String, String> map = new HashMap<>(8);
        map.put("type", type);
        map.put("id", "");
        map.put("merchant_name", storeUserName);
        map.put("brand_name", storeName);
        map.put("brand_header", logo);
        map.put("brand_introduction", info);
        map.put("brand_address", addressDetail);
        map.put("brand_tel", phone);
        map.put("map_address", address);
        map.put("bus_license", yingyezhizhao);
        map.put("brand_href", "");
        map.put("guide_img", daoliu);
        map.put("brand_type", zhifubao);
        /**Map<String, RequestBody> data = new HashMap<>(8);

         RequestBody bus_license = RequestBody.create(MediaType.parse("multipart/form-data"), new File(yingyezhizhao));
         data.put("bus_license", bus_license);
         RequestBody brand_header = RequestBody.create(MediaType.parse("multipart/form-data"), new File(logo));
         data.put("brand_header", brand_header);
         RequestBody guide_img = RequestBody.create(MediaType.parse("multipart/form-data"), new File(daoliu));
         data.put("guide_img", guide_img);*/
        String time = System.currentTimeMillis() + "";
        String signStr = type + "|$|" + storeUserName + "|$|" + storeName + "|$|" + logo + "|$|" + info + "|$|" + addressDetail + "|$|" + phone + "|$|" + address + "|$|" + yingyezhizhao + "|$||$||$|" + daoliu + "|$|" + zhifubao + "|$|" + time;
        String sign = RsaTool.encrypt(Constant.OUR_RSA_PUBLIC, signStr);
        map.put("sign", sign);
        map.put("request_time", time);
        String params = JSONUtils.mapToJson(map);
        map.clear();
        map.put("param", params);
        Observable<ResponseBody> observable = service.addBrand(map);
        loadData(action, observable);
    }

    public void getBrandType() {
        HashMap<String, String> map = new HashMap<>();
        String time = System.currentTimeMillis() + "";
        String sign = RsaTool.encrypt(Constant.OUR_RSA_PUBLIC, time);
        map.put("sign", sign);
        map.put("request_time", time);
        String params = JSONUtils.mapToJson(map);
        map.clear();
        map.put("param", params);

        Observable<ResponseBody> observable = service.getBrandType(map);
        loadData(10, observable);
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
                }
            }
        });
    }
}
