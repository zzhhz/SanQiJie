package com.chehubang.duolejie.modules.login.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.modules.login.presenter.GetPasswordPresenter;

import common.Utils.StringUtils;
import common.Utils.ToastUtils;
import common.http.RequestResult;
import common.mvp.activity.MainView;
import common.view.widget.CaptchaTimeCount;

/**
 * Created by Thinkpad on 2017/12/10.
 */

public class GetPasswordActivity extends BaseActivity<GetPasswordPresenter> implements MainView, View.OnClickListener {

    private EditText account, password, verifyCode,passwordAgain;
    private CaptchaTimeCount mTimeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_password);
        ImageView back = findAtyViewById(R.id.iv_getpassword_back);
        account = findAtyViewById(R.id.et_getpassword_account);
        password = findAtyViewById(R.id.et_getpassword_pwd);
        passwordAgain = findAtyViewById(R.id.et_getpassword_pwd_two);
        verifyCode = findAtyViewById(R.id.et_getpassword_verify);
        TextView getVerify = findAtyViewById(R.id.tv_getpassword_get_verify);
        TextView save = findAtyViewById(R.id.tv_getpassword_save);
        back.setOnClickListener(this);
        getVerify.setOnClickListener(this);
        save.setOnClickListener(this);
        mTimeCount = new CaptchaTimeCount(60000, 1000, getVerify, this);
    }

    @Override
    public void getDataSuccess(Object model, int action) {
        if (action == ACTION_DEFAULT + 2) {
            if (model !=null){
                RequestResult result = (RequestResult) model;
                if (TextUtils.equals(Constant.request_success,result.getStatus())){
                    ToastUtils.centerToastWhite(this,"密码修改成功");
                    finish();
                }
            }
        }
    }

    @Override
    public void getDataFail(String msg, int action) {

    }

    @Override
    protected GetPasswordPresenter createPresenter() {
        return new GetPasswordPresenter(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_getpassword_back:
                finish();
                break;
            case R.id.tv_getpassword_get_verify:
                if (TextUtils.isEmpty(account.getText().toString())) {
                    ToastUtils.centerToastWhite(this, "请输入账号");
                } else if (!StringUtils.isPhone(account.getText().toString())) {
                    ToastUtils.centerToastWhite(this, "手机号码错误");
                } else {
                    mTimeCount.start();
                    mvpPresenter.getVerificationCode(ACTION_DEFAULT + 1, account.getText().toString(), "2");
                }
                break;
            case R.id.tv_getpassword_save:
                String acc = account.getText().toString();
                String pwd = password.getText().toString();
                String code = verifyCode.getText().toString();
                String pwdAgain = passwordAgain.getText().toString();
                if (TextUtils.isEmpty(acc)) {
                    ToastUtils.centerToastWhite(this, "请输入账号");
                    return;
                } else if (TextUtils.isEmpty(pwd)) {
                    ToastUtils.centerToastWhite(this, "请输入密码");
                    return;
                } else if (TextUtils.isEmpty(code)) {
                    ToastUtils.centerToastWhite(this, "请输入验证码");
                    return;
                } else if (TextUtils.isEmpty(pwdAgain)){
                    ToastUtils.centerToastWhite(this, "请再次输入密码");
                }else if (!TextUtils.equals(pwd,pwdAgain)){
                    ToastUtils.centerToastWhite(this, "对不起，两次密码输入不一致");
                }else {
                    mvpPresenter.updataPassword(ACTION_DEFAULT + 2, acc, pwd, code);
                }
                break;
            default:
                break;
        }
    }
}
