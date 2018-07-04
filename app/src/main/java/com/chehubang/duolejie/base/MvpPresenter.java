package com.chehubang.duolejie.base;



import com.chehubang.duolejie.http.LoadDataService;

import common.http.HttpApi;
import common.http.RxDataHelper;
import common.http.RxIOTransformer;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Beidouht on 2017/10/13.
 */

public class MvpPresenter<V> {

    public V mvpView;
    private CompositeSubscription mCompositeSubscription;
    protected LoadDataService service;
    public static final int ACTION_DEFAULT = 1;
    public static String BASEURL = "39.107.14.118/";   //外网地址
    public static String BASEURLS = "192.168.3.25:8080/GetTreasureAppJinDong/";  //内网地址
    /**
     * 服务器地址
     */
    public static String BASE_URL = "http://"+BASEURL;//请求接口根地址   正式
    public static final String URL_UPLOAD = BASE_URL+"appInterface/uploadimgnew.jhtml";
    public static final String URL_UPLOAD_CASE = BASE_URL+"appInterface/uploadImgNew.jhtml";

    public void attachView(V mvpView) {
        this.mvpView = mvpView;
        service = HttpApi.getHttpApi(BASE_URL).getService(LoadDataService.class);
    }

    public void detachView() {
        this.mvpView = null;
        onUnsubscribe();
    }

    //RXjava取消注册，以避免内存泄露
    public void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    public void addSubscription(Observable observable, Subscriber subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }

        mCompositeSubscription.add(observable
                .compose(RxIOTransformer.<ResponseBody>defaultTransformer())
                .compose(RxDataHelper.<String>handleResult())
                .subscribe(subscriber));
    }

    public void addScription(Observable observable, Subscriber subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }

        mCompositeSubscription.add(observable
                .compose(RxIOTransformer.<ResponseBody>defaultTransformer())
                .subscribe(subscriber));
    }
}
