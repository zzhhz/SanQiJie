package com.chehubang.duolejie.wxapi;
/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.chehubang.duolejie.config.Constant;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

import common.Utils.BroadcastUtils;
import common.Utils.ToastUtils;


/**
 * 微信客户端回调activity示例
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
    public static final String ACTION_LOGIN_EVENT = "login_event";
    public static final String LOGIN_EVENT_TYPE = "login_event_type";
    public static final int LOGIN_EVENT_TYPE_NONE = -1;
    public static final int LOGIN_EVENT_TYPE_LOGIN = 0;
    public static final int LOGIN_EVENT_TYPE_FAIL = 1;
    public static final int LOGIN_EVENT_TYPE_CANCEL = 2;
    public static final String LOGIN_EVENT_TYPE_CODE = "2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID, false);
        api.registerApp(Constant.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.transaction.equals("get_user")) {
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    String codeSuc = ((SendAuth.Resp) resp).code;
                    Intent intentSuc = new Intent(WXEntryActivity.ACTION_LOGIN_EVENT);
                    intentSuc.putExtra(WXEntryActivity.LOGIN_EVENT_TYPE, WXEntryActivity.LOGIN_EVENT_TYPE_LOGIN);
                    intentSuc.putExtra(WXEntryActivity.LOGIN_EVENT_TYPE_CODE, codeSuc);
                    BroadcastUtils.sendLocalBroadcast(intentSuc);
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    Intent intentCan = new Intent(WXEntryActivity.ACTION_LOGIN_EVENT);
                    intentCan.putExtra(WXEntryActivity.LOGIN_EVENT_TYPE, WXEntryActivity.LOGIN_EVENT_TYPE_CANCEL);
                    BroadcastUtils.sendLocalBroadcast(intentCan);
                    Toast.makeText(getApplicationContext(), "取消微信登录", Toast.LENGTH_LONG).show();
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    Intent intentDen = new Intent(WXEntryActivity.ACTION_LOGIN_EVENT);
                    intentDen.putExtra(WXEntryActivity.LOGIN_EVENT_TYPE, WXEntryActivity.LOGIN_EVENT_TYPE_FAIL);
                    BroadcastUtils.sendLocalBroadcast(intentDen);
                    Toast.makeText(getApplicationContext(), "微信授权失败，请重新尝试", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
            this.finish();
        } else if (resp.transaction.startsWith("img")) {
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    ToastUtils.centerToastWhite(WXEntryActivity.this, "分享成功");
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    ToastUtils.centerToastWhite(WXEntryActivity.this, "取消分享");
                    break;
                default:
                    break;
            }
            EventBus.getDefault().post(Constant.EVENT_SHARE_SUCCESS);
            this.finish();
        }
    }
}
