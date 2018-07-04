package com.chehubang.duolejie.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.chehubang.duolejie.BuildConfig;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.utils.log;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by user on 2018/2/10.
 *
 * @date: 2018/2/10
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @description: <!-- User defined. 用户自定义的广播接收器-->
 * <receiver
 * android:name="您自己定义的Receiver"
 * android:enabled="true">
 * <intent-filter>
 * <!--Required 用户注册SDK的intent-->
 * <action android:name="cn.jpush.android.intent.REGISTRATION" />
 * <!--Required 用户接收SDK消息的intent-->
 * <action android:name="cn.jpush.android.intent.MESSAGE_RECEIVED" />
 * <!--Required 用户接收SDK通知栏信息的intent-->
 * <action android:name="cn.jpush.android.intent.NOTIFICATION_RECEIVED" />
 * <!--Required 用户打开自定义通知栏的intent-->
 * <action android:name="cn.jpush.android.intent.NOTIFICATION_OPENED" />
 * <!-- 接收网络变化 连接/断开 since 1.6.3 -->
 * <action android:name="cn.jpush.android.intent.CONNECTION" />
 * <category android:name="您应用的包名" />
 * </intent-filter>
 * </receiver>
 */
public class JPushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Constant.ACTION_LOGOUT.equals(intent.getAction())) {
            UserInfo.getInstance().clearCache(context);
        }
        Bundle extras = intent.getExtras();
        if (BuildConfig.DEBUG) {
            Set<String> strings = extras.keySet();
            for (String keyStr : strings) {
                log.d(keyStr + ",   value: " + extras.get(keyStr).toString());
            }
        }
        if (extras.containsKey(JPushInterface.EXTRA_EXTRA)) {
            String string = extras.getString(JPushInterface.EXTRA_EXTRA);
            try {
                JSONObject object = new JSONObject(string);
                String extra_key = object.getString("extra_key");
                if ("111111".equals(extra_key)){
                    UserInfo.getInstance().clearCache(context);
                    EventBus.getDefault().post(Constant.EVENT_REFRESH_USER);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
