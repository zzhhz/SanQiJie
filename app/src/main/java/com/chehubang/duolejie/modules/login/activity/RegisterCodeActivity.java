package com.chehubang.duolejie.modules.login.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.modules.login.presenter.RegisterCodePresenter;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import common.Utils.ToastUtils;
import common.http.RequestResult;
import common.mvp.activity.MainView;
import common.view.widget.CaptchaTimeCount;
import io.reactivex.functions.Consumer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ZZH on 2018/1/29.
 *
 * @Date: 2018/1/29
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description: 注册时验证验证码
 */
public class RegisterCodeActivity extends BaseActivity<RegisterCodePresenter> implements MainView {
    public static final String KEY_USER_ACCOUNT = "KEY_USER_ACCOUNT";
    public static final String KEY_USER_PASSWORD = "KEY_USER_PASSWORD";


    @BindView(R.id.tv_phone)
    public TextView mTvPhone;
    @BindView(R.id.et_code)
    public PinEntryEditText et_code;

    @BindView(R.id.tv_get_code)
    public TextView tv_get_code;
    private CaptchaTimeCount mTimeCount;
    private String account;
    private String password;
    RxPermissions mRxPermissions;

    public static void open(Context ctx, String account, String pwd) {
        Intent intent = new Intent(ctx, RegisterCodeActivity.class);
        intent.putExtra(KEY_USER_ACCOUNT, account);
        intent.putExtra(KEY_USER_PASSWORD, pwd);
        ctx.startActivity(intent);
    }

    private String jingdu;
    private String weidu;
    MyLocationListener mMyLocationListener = new MyLocationListener();
    public static String city = "";


    @Override
    protected RegisterCodePresenter createPresenter() {
        return new RegisterCodePresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_code);
        ButterKnife.bind(this);
        account = getIntent().getStringExtra(KEY_USER_ACCOUNT);
        password = getIntent().getStringExtra(KEY_USER_PASSWORD);
        mTimeCount = new CaptchaTimeCount(60000, 1000, tv_get_code, this);
        mvpPresenter.getVerificationCode(ACTION_DEFAULT + 1, account, "1");
        mTimeCount.start();
        mRxPermissions = new RxPermissions(this);
        if (mRxPermissions.isGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {

            initLocation();
        } else {
            mRxPermissions.requestEach(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION).subscribe(new Consumer<Permission>() {
                @Override
                public void accept(Permission permission) throws Exception {
                    if (permission.granted) {
                        initLocation();
                    } else {
                        jingdu = "0.0";
                        weidu = "0.0";
                    }
                }
            });
        }
    }

    @OnClick({R.id.tv_get_code, R.id.tv_register_enter})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.tv_get_code:
                mvpPresenter.getVerificationCode(ACTION_DEFAULT + 1, account, "1");
                break;
            case R.id.tv_register_enter:
                if (TextUtils.isEmpty(et_code.getText().toString())) {
                    ToastUtils.centerToastWhite(this, "请输入验证码");
                    return;
                }
                mvpPresenter.register(3, account, password, et_code.getText().toString());
                break;
        }

    }

    @Override
    public void getDataSuccess(Object model, int action) {

        if (action == 3) {
            RequestResult date = (RequestResult) model;
            //后台只要返回o,就认为请求成功，可以跳转
            if (TextUtils.equals(Constant.request_success, date.getStatus())) {
                ToastUtils.centerToastWhite(this, "注册成功");
                Intent intent = new Intent();
                intent.putExtra("account", account);
                intent.putExtra("pwd", password);
                setResult(ACTION_DEFAULT + 2002, intent);
                finish();
            }
        }
    }

    @Override
    public void getDataFail(String msg, int action) {

    }

    LocationClient mLocationClient;

    private void initLocation() {
        mLocationClient = new LocationClient(this);
        LocationClientOption option = new LocationClientOption();
        //就是这个方法设置为true，才能获取当前的位置信息
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("gcj02");//可选，默认gcj02，设置返回的定位结果坐标系
        //int span = 1000;
        //option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(mMyLocationListener);
        mLocationClient.start();
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            //经纬度
            double lati = location.getLatitude();
            double longa = location.getLongitude();
            //打印出当前位置
            city = location.getCity();
            Log.i("TAG", "location.getAddrStr()=" + location.getAddrStr());
            //打印出当前城市
            Log.i("TAG", "location.getCity()=" + location.getCity());
        }
    }


}
