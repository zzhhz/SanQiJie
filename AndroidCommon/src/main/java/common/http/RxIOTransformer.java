package common.http;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jxl on 2017/6/30.
 * 线程转换，默认在IO线程执行，然后交给主线程回调
 */
public class RxIOTransformer {
    public static <T>Observable.Transformer<T,T> defaultTransformer(){
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
