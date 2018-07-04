package common.http;

import android.text.TextUtils;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import common.Common;
import common.Utils.NetWorkUtils;
import retrofit2.HttpException;
import rx.Subscriber;

/**
 * Created by jxl on 2017/6/30.
 * 请求成功和失败，可在此处控制loading
 */
public abstract class LoadDataSubscriber<T> extends Subscriber<T> {

    public static final String ACTION_JUMP_LOGIN = "111111";


    @Override
    public void onCompleted() {
        if (!isUnsubscribed()) {
            unsubscribe();
        }
    }

    @Override
    public void onNext(T t) {
        try {
            if (t instanceof RequestResult) {
                RequestResult result = (RequestResult) t;
                if (TextUtils.equals(ACTION_JUMP_LOGIN, result.getStatus())) {
                    jumpLogin();
                } else {
                    _success(t);
                }


            } else {
                _success(t);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void jumpLogin() {
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
        if (NetWorkUtils.getAPNType(Common.getApplication()) == 0) {
            _error("网络无连接");
        } else if (throwable instanceof SocketTimeoutException || throwable instanceof ConnectException) {
            _error("请求超时，请稍后重试");
        } else if (throwable instanceof HttpException) {
            _error("服务器异常，请稍后再试");
        } else {
            if (throwable.getMessage().contains("*******")) {
                _error("网络请求失败，请重新再试");
            } else {
                _error(throwable.getMessage());
            }
        }
    }

    protected abstract void _error(String message);

    protected abstract void _success(T t) throws JSONException;
}
