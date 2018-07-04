package com.chehubang.duolejie.modules.carlife.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.modules.carlife.presenter.BindCardPresenter;
import com.chehubang.duolejie.modules.home.activity.WebviewActivity;

import common.Utils.ToastUtils;
import common.http.RequestResult;
import common.mvp.activity.MainView;

/**
 * Created by Administrator on 2017/12/17 0017.
 */

public class BindCardActivity extends BaseActivity<BindCardPresenter> implements MainView, View.OnClickListener {

    private EditText cardNumber;
    private TextView commit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.binding_refueling_card);
        ImageView back = findAtyViewById(R.id.tv_bind_card_back);
        cardNumber = findAtyViewById(R.id.et_oil_card_number);
        commit = findAtyViewById(R.id.tv_oil_card_commit);
        back.setOnClickListener(this);
        commit.setOnClickListener(this);
        UserInfo.getInstance().getIdCache(this);
        findViewById(R.id.tv_bind).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_bind_card_back:
                finish();
                break;
            case R.id.tv_oil_card_commit:
                if (TextUtils.isEmpty(cardNumber.getText().toString())) {
                    ToastUtils.centerToastWhite(this, "请输入加油卡卡号");
                } else {
                    mvpPresenter.addCardNumber(ACTION_DEFAULT + 1, UserInfo.getInstance().getId(), cardNumber.getText().toString());
                }
                break;
            case R.id.tv_bind:
                Intent intent2 = new Intent(this, WebviewActivity.class);
                intent2.putExtra("url", "http://39.107.14.118/appInterface/aboutUsContentInfo.jhtml?id=13");
                intent2.putExtra("title", "加油卡介绍");
                startActivity(intent2);
                break;
            default:
                break;
        }
    }

    @Override
    public void getDataSuccess(Object model, int action) {
        if (action == ACTION_DEFAULT + 1) {
            RequestResult date = (RequestResult) model;
            if (TextUtils.equals(Constant.request_success, date.getStatus())) {   //后台只要返回o,就认为请求成功，可以跳转
                ToastUtils.centerToastWhite(this, "加油卡绑定成功");
                setResult(RESULT_OK);
                finish();
            } else {
                ToastUtils.centerToastWhite(this, date.getDesc());
            }
        }
    }

    @Override
    public void getDataFail(String msg, int action) {

    }

    @Override
    protected BindCardPresenter createPresenter() {
        return new BindCardPresenter(this);
    }
}
