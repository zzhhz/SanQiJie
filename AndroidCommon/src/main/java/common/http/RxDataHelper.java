package common.http;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import common.Log.LogUtils;
import common.Utils.JSONUtils;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by jxl on 2017/6/30.
 * 用来处理返回数据，目前由于数据内容格式混乱，先做部分处理
 */
public class RxDataHelper {
    public static <T> Observable.Transformer<ResponseBody, T> handleResult() {
        return new Observable.Transformer<ResponseBody, T>() {
            @Override
            public Observable call(Observable<ResponseBody> stringObservable) {
                return stringObservable.flatMap(new Func1<ResponseBody, Observable<T>>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public Observable<T> call(ResponseBody s) {
                        String s1 = null;
                        try {
                            s1 = s.string();
                            RequestResult requestResult = JSONUtils.jsonObjectToBean(RequestResult.class,new JSONObject(s1));
                            return handleData(requestResult);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return null;
                    }
                });
            }
        };
    }


    public static <T> Observable handleData(final T t) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    subscriber.onNext(t);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(new Exception("*******"));
                }
            }
        });
    }


}
