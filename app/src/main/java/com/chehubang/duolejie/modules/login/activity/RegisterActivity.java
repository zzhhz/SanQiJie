package com.chehubang.duolejie.modules.login.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.modules.home.activity.WebviewActivity;
import com.chehubang.duolejie.modules.login.presenter.RegisterPresenter;
import com.chehubang.duolejie.utils.CodeUtils;

import common.Utils.StringUtils;
import common.Utils.ToastUtils;
import common.http.RequestResult;
import common.mvp.activity.MainView;
import common.view.widget.CaptchaTimeCount;

/**
 * Created by Thinkpad on 2017/12/10.
 */

public class RegisterActivity extends BaseActivity<RegisterPresenter> implements MainView, View.OnClickListener {

    private TextView title;
    private EditText account, password, verifyCode;
    private CaptchaTimeCount mTimeCount;
    ImageView iv_code;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ImageView back = findAtyViewById(R.id.iv_register_back);
        title = findAtyViewById(R.id.tv_normal_title);
        account = findAtyViewById(R.id.et_register_account);
        password = findAtyViewById(R.id.et_register_pwd);
        verifyCode = findAtyViewById(R.id.et_register_verify);
        iv_code = findAtyViewById(R.id.tv_register_get_verify);
        TextView register = findAtyViewById(R.id.tv_register_enter);
        LinearLayout contract = (LinearLayout) findViewById(R.id.ll_register_contract);
        back.setOnClickListener(this);
        iv_code.setOnClickListener(this);
        register.setOnClickListener(this);
        contract.setOnClickListener(this);
        //mTimeCount = new CaptchaTimeCount(60000, 1000, getVerify, this);
        randomCode();
    }

    private void randomCode() {
        Bitmap bitmap = CodeUtils.getInstance().createBitmap();
        code = CodeUtils.getInstance().getCode();
        iv_code.setImageBitmap(bitmap);
    }

    @Override
    public void getDataSuccess(Object model, int action) {
        if (action == ACTION_DEFAULT + 2) {
            RequestResult date = (RequestResult) model;
            if (TextUtils.equals(Constant.request_success, date.getStatus())) {   //后台只要返回o,就认为请求成功，可以跳转
                Intent intent = new Intent();
                intent.putExtra("account", account.getText().toString());
                intent.putExtra("pwd", password.getText().toString());
                setResult(ACTION_DEFAULT + 2002, intent);
                finish();
            }
            //底下部分应该为登录部分的添加缓存的代码
           /* JSONObject jsonObject = new JSONObject(date);
            String sign = jsonObject.getString("sign");
            String app_login_id = jsonObject.getString("app_login_id");
            UserInfo.getInstance().setApp_login_id(app_login_id);
            UserInfo.getInstance().setSign(sign);
            UserInfo.getInstance().writeToCache(this);*/
        }
    }

    @Override
    public void getDataFail(String msg, int action) {

    }

    @Override
    protected RegisterPresenter createPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_register_back:
                finish();
                break;
            case R.id.tv_register_get_verify:
                /*if (TextUtils.isEmpty(account.getText().toString())) {
                    ToastUtils.centerToastWhite(this, "请输入账号");
                } else if (!StringUtils.isPhone(account.getText().toString())) {
                    ToastUtils.centerToastWhite(this, "手机号码错误");
                } else {
                    mTimeCount.start();
                    mvpPresenter.getVerificationCode(ACTION_DEFAULT + 1, account.getText().toString(), "1");
                }*/
                randomCode();
                break;
            case R.id.tv_register_enter:
                String acc = account.getText().toString();
                String pwd = password.getText().toString();
                String code = verifyCode.getText().toString();
                if (TextUtils.isEmpty(acc)) {
                    ToastUtils.centerToastWhite(this, "请输入账号");
                    return;
                } else if (TextUtils.isEmpty(pwd)) {
                    ToastUtils.centerToastWhite(this, "请输入密码");
                    return;
                } else if (!TextUtils.equals(this.code, code)) {
                    ToastUtils.centerToastWhite(this, "请输入图形验证码");
                    return;
                } else {
                    //mvpPresenter.register(ACTION_DEFAULT + 2, acc, pwd, code);
                    if (!StringUtils.isPhone(acc)) {
                        ToastUtils.centerToastWhite(this, "请输入正确的手机号");
                        return;
                    }
                    RegisterCodeActivity.open(this, acc, pwd);
                    finish();

                }
                break;
            case R.id.ll_register_contract:
                Intent intent7 = new Intent(this, WebviewActivity.class);
                intent7.putExtra("url", "http://39.107.14.118/appInterface/aboutUsContentInfo.jhtml?id=6");  //外网版本
                // intent7.putExtra("url","http://192.168.3.25:8090/GetTreasureAppJinDong/appInterface/aboutUsContentInfo.jhtml?id=1&is_show_message=y");
                intent7.putExtra("title", "多乐街服务协议");
                startActivity(intent7);
                break;
            default:
                break;
        }
    }
}
