package com.chehubang.duolejie.modules.login.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.http.OkHttpManager;
import com.chehubang.duolejie.modules.login.presenter.LoginPresenter;
import com.chehubang.duolejie.utils.log;
import com.chehubang.duolejie.wxapi.WXEntryActivity;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import common.Utils.AppConfig;
import common.Utils.BroadcastUtils;
import common.Utils.StringUtils;
import common.Utils.ToastUtils;
import common.http.RequestResult;
import common.mvp.activity.MainView;

/**
 * Created by Thinkpad on 2017/12/10.
 */

public class LoginActivity extends BaseActivity<LoginPresenter> implements MainView, View.OnClickListener {

    private EditText etAccount, etPwd;
    private TextView tvConfirm, tvRegister, tvForgetPwd;
    private ImageView ivWechat;
    public static IWXAPI api;
    private WechatBroadcastReceiver wechatBroadcastReceiver;
    public final String ACTION_LOGIN_EVENT = "login_event";
    private final String ACTION_SWITCH = "com.chehubang.duolejie.ACTION_SWITCH";
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID, true);
        api.registerApp(Constant.APP_ID);

        etAccount = findAtyViewById(R.id.et_login_account);
        etPwd = findAtyViewById(R.id.et_login_pwd);
        tvConfirm = findAtyViewById(R.id.tv_login_confirm);
        tvRegister = findAtyViewById(R.id.tv_login_register);
        tvForgetPwd = findAtyViewById(R.id.tv_login_forget_pwd);
        ivWechat = findAtyViewById(R.id.iv_login_wechat);

        tvConfirm.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        tvForgetPwd.setOnClickListener(this);
        ivWechat.setOnClickListener(this);

        wechatBroadcastReceiver = new WechatBroadcastReceiver();
        BroadcastUtils.registerLocalReceiver(wechatBroadcastReceiver, new IntentFilter(ACTION_LOGIN_EVENT));
        Intent intent = getIntent();
        if (intent.hasExtra("type")) {
            type = intent.getIntExtra("type", -1);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login_confirm:
                if (TextUtils.isEmpty(etAccount.getText().toString())) {
                    ToastUtils.centerToastWhite(this, "请输入账号");
                } else if (TextUtils.isEmpty(etPwd.getText().toString())) {
                    ToastUtils.centerToastWhite(this, "请输入密码");
                } else if (!StringUtils.isPhone(etAccount.getText().toString())) {
                    ToastUtils.centerToastWhite(this, "手机号码错误");
                } else {
                    log.d(JPushInterface.getRegistrationID(this));
                    mvpPresenter.login(ACTION_DEFAULT + 1, etAccount.getText().toString(), etPwd.getText().toString(), JPushInterface.getRegistrationID(this));
                }
                break;
            case R.id.tv_login_register:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivityForResult(intent, ACTION_DEFAULT + 2001);
                break;
            case R.id.tv_login_forget_pwd:
                Intent intent1 = new Intent(this, GetPasswordActivity.class);
                startActivity(intent1);
                break;
            case R.id.iv_login_wechat:
                if (api.isWXAppInstalled() && api.isWXAppSupportAPI()) {
                    loginByWechat();
                } else {
                    ToastUtils.centerToastWhite(this, "未安装微信客户端");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTION_DEFAULT + 2001) {
            if (resultCode == ACTION_DEFAULT + 2002) {
                Intent date = data;
                if (date != null) {
                    etAccount.setText(date.getStringExtra("account"));
                    etPwd.setText(data.getStringExtra("pwd"));
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void getDataSuccess(Object model, int action) {
        if (action == ACTION_DEFAULT + 1) {
            RequestResult data = (RequestResult) model;

            if (TextUtils.equals(Constant.request_success, data.getStatus())) {
                try {
                    JSONObject jsonObject = new JSONObject(data.getData());
                    String app_login_id = jsonObject.getString("id");
                    String nick_name = jsonObject.getString("nick_name");
                    String user_money = jsonObject.getString("user_money");
                    String user_lettory = jsonObject.getString("user_lettory");
                    String user_heard = jsonObject.getString("user_header");
                    String invite_code = jsonObject.getString("invite_code");
                    String user_tel = jsonObject.getString("user_tel");
                    UserInfo.getInstance().setId(app_login_id);
                    UserInfo.getInstance().setNick_name(nick_name);
                    UserInfo.getInstance().setUser_money(user_money);
                    UserInfo.getInstance().setUser_lettory(user_lettory);
                    UserInfo.getInstance().setUser_header(user_heard);
                    UserInfo.getInstance().setUser_tel(user_tel);
                    UserInfo.getInstance().setInvite_code(invite_code);
                    UserInfo.getInstance().writeToCache(this);
                    UserInfo.putToken(data.getToken());
                    AppConfig.putBoolean("islogin", true);   //
                    if (type == 2) {
                        finish();
                    } else {
                        Intent intent1 = new Intent();
                        intent1.setAction(ACTION_SWITCH);
                        intent1.putExtra("action", "fragment4");
                        LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                ToastUtils.centerToastWhite(this, data.getDesc());
            }
        } else if (action == ACTION_DEFAULT + 2) {
            ToastUtils.centerToastWhite(this, "微信登录成功");
        }
    }

    @Override
    public void getDataFail(String msg, int action) {

    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }


    private void loginByWechat() {
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "duolejie_wechat";
        req.transaction = "get_user";
        api.sendReq(req);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BroadcastUtils.unRegisterLocalReceiver(wechatBroadcastReceiver);
    }

    class WechatBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.equals(WXEntryActivity.ACTION_LOGIN_EVENT, action)) {
                int type = intent.getIntExtra(WXEntryActivity.LOGIN_EVENT_TYPE, WXEntryActivity.LOGIN_EVENT_TYPE_NONE);
                switch (type) {
                    case WXEntryActivity.LOGIN_EVENT_TYPE_LOGIN:
                        String code = intent.getStringExtra(WXEntryActivity.LOGIN_EVENT_TYPE_CODE);
                        mvpPresenter.getAccessToken(code);
                        break;
                }
            }
        }
    }
}
