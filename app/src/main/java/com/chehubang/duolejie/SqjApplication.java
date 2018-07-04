package com.chehubang.duolejie;

import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.baidu.mapapi.SDKInitializer;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.config.UserInfo;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.jpush.android.api.JPushInterface;
import common.Common;

/**
 * Created by ZZH on 2018/1/29
 *
 * @date: 2018/1/29 下午4:50
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @Description:
 */
public class SqjApplication extends Common {

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        JPushInterface.init(this);
        SDKInitializer.initialize(this);
        //开始进行初始化
        registToWX();
        ///
        UserInfo info = new UserInfo();
        info.getCache(this);
        UserInfo instance = UserInfo.getInstance();
        if (TextUtils.isEmpty(instance.getId())) {
            instance.setId(info.getId());
            instance.setUser_tel(info.getUser_tel());
            instance.setUser_money(info.getUser_money());
            instance.setUser_lettory(info.getUser_lettory());
            instance.setUser_header(info.getUser_header());
            instance.setNick_name(info.getNick_name());
        }
    }

    private void registToWX() {
        //AppConst.WEIXIN.APP_ID是指你应用在微信开放平台上的AppID，记得替换。
        IWXAPI mWxApi = WXAPIFactory.createWXAPI(this, Constant.APP_ID, false);
        // 将该app注册到微信
        mWxApi.registerApp(Constant.APP_ID);
    }
}
