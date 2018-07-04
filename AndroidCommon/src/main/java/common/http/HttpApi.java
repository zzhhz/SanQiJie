package common.http;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import common.Log.LogUtils;
import common.Utils.AppConfig;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jxl on 2017/6/29.
 */
public class HttpApi {

    private static final int DEFAULT_TIMEOUT = 60;
    private Retrofit retrofit;
    private static HttpApi httpApi;
    public static Retrofit mRetrofit;


    private HttpApi(String Baseurl){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT,TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT,TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addConverterFactory(new FileRequestBodyConverterFactory())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Baseurl)
                .build();
    }

    public static HttpApi getHttpApi(String url){
        if (httpApi==null){
            synchronized (HttpApi.class){
                if (httpApi==null){
                    httpApi = new HttpApi(url);
                }
            }
        }
        return httpApi;
    }


    public <T> T getService(Class<T> tClass){
        return retrofit.create(tClass);
    }

}
