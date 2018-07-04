package com.chehubang.duolejie.modules.category.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.model.GoodsTypeBean;
import com.chehubang.duolejie.modules.category.activity.CategoryLifeActivity;
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
 * Created by Thinkpad on 2017/12/13.
 */

public class CategoryLifePresenter extends MvpPresenter<CategoryLifeActivity> {

    public CategoryLifePresenter(CategoryLifeActivity activity) {
        attachView(activity);
    }

    public void getTypeList(int action,String id){
        Map<String, String> map = new HashMap<>();
        String time=System.currentTimeMillis()+"";
        map.put("goodsTypeId",id);
        try {
            String s=id+"|$|"+time;
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
        Observable<ResponseBody> typelist = service.getTypeChildlist(map);
        loadData(action,typelist);
    }


    public void getTypeChildList(int action,String id){
        Map<String, String> map = new HashMap<>();
        String time=System.currentTimeMillis()+"";
        try {
            String s=id+"|$|"+time;
            String sign= RsaTool.encrypt(Constant.OUR_RSA_PUBLIC, s);
            map.put("sign", sign);
        } catch (Exception e) {
            map.put("sign", "");
            e.printStackTrace();
        }
        map.put("request_time", time);
        map.put("goodsTypeId",id);
        String params = JSONUtils.mapToJson(map);
        map.clear();
        map.put("param",params);
        Observable<ResponseBody> typelist = service.getTypeChildlist(map);
        loadData(action,typelist);
    }

    public void loadData(final int action, Observable<ResponseBody> observable) {
        addSubscription(observable, new LoadDataSubscriber() {

            private ArrayList<GoodsTypeBean> goodsTypeBeans;
            @Override
            protected void _error(String message) {
                mvpView.getDataFail(message, action);
            }


            @Override
            protected void _success(Object o) {
                RequestResult result = (RequestResult) o;
                Log.d("------", "_success: "+result);
                String data = result.getData();
                String status = result.getStatus();
                if (TextUtils.equals(Constant.request_success,status)){
                    if (action == ACTION_DEFAULT+1){
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            JSONArray goodsList = jsonObject.getJSONArray("goodsTypeChilderList");
                            goodsTypeBeans = new ArrayList<>();
                            for (int i = 0; i<goodsList.length();i++){
                                JSONObject o1 = (JSONObject)goodsList.get(i);
                                GoodsTypeBean goodsTypeBean = JSONUtils.GsonToBean(o1.toString(), GoodsTypeBean.class);
                                goodsTypeBeans.add(goodsTypeBean);
                            }
                            mvpView.getDataSuccess(goodsTypeBeans, action);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if (action==ACTION_DEFAULT+2){
                        mvpView.getDataSuccess(result,action);
                    }
                }
            }
        });
    }
}
