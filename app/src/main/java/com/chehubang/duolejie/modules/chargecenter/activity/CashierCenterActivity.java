package com.chehubang.duolejie.modules.chargecenter.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.model.PayResult;
import com.chehubang.duolejie.modules.chargecenter.presenter.CashierCenterPresenter;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import common.Utils.ToastUtils;
import common.http.RequestResult;
import common.mvp.activity.MainView;


/**
 * Created by Beidouht on 2017/12/20.
 */

public class CashierCenterActivity extends BaseActivity<CashierCenterPresenter> implements MainView, View.OnClickListener, CheckBox.OnCheckedChangeListener {

    private TextView totalPay, balance, needPay, commit;
    private static final int SDK_PAY_FLAG = 1;
    private String id;
    private CheckBox useBalance;
    private CheckBox useZfb;
    private boolean isZfb = false;
    private boolean isBalance = false;
    private String all_perice;
    private String need_pay_money;
    private String user_money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_desk);
        ImageView back = (ImageView) findViewById(R.id.iv_cashiercenter_back);
        totalPay = (TextView) findViewById(R.id.tv_cashiercenter_total);
        balance = (TextView) findViewById(R.id.tv_cashiercenter_balance);
        needPay = (TextView) findViewById(R.id.tv_cashiercenter_need_pay);
        commit = (TextView) findViewById(R.id.tv_cashiercenter_commit);
        useBalance = (CheckBox) findViewById(R.id.cb_cashier_use_balance);
        useZfb = (CheckBox) findViewById(R.id.cb_cashier_use_zfb);
        back.setOnClickListener(this);
        commit.setOnClickListener(this);
        Intent intent = getIntent();
        if (intent != null) {
            //MoneyTypeBean data = (MoneyTypeBean) intent.getSerializableExtra("data");
            id = intent.getStringExtra("id");    //获取订单号
            //String value = data.getValue();
            //totalPay.setText("￥" + value);
            UserInfo.getInstance().getIdCache(this);
            mvpPresenter.getBalance(ACTION_DEFAULT + 1, UserInfo.getInstance().getId(), id);
        }
        useBalance.setOnCheckedChangeListener(this);
        useZfb.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cashiercenter_back:
                finish();
                break;
            case R.id.tv_cashiercenter_commit:
                String s = needPay.getText().toString();
                String s1 = balance.getText().toString();
                String substring = s.substring(1, s.length());
                String substring1 = s1.substring(1, s1.length());
                if (TextUtils.isEmpty(s)) {
                    ToastUtils.centerToastWhite(this, "暂无数据");
                } else if (TextUtils.equals("0.00", substring)) {
                    mvpPresenter.getappclient(ACTION_DEFAULT + 3, id, need_pay_money);
                } else if (Double.valueOf(substring) > Double.valueOf(substring1)) {
                    if (isBalance && isZfb) {
                        ToastUtils.centerToastWhite(this, "余额不足，不足部分将使用支付宝支付");
                        mvpPresenter.payRechargeOrder(ACTION_DEFAULT + 2, UserInfo.getInstance().getId(), id);
                        mvpPresenter.getappclient(ACTION_DEFAULT + 3, id, need_pay_money);
                    } else if (isZfb && !isBalance) {
                        mvpPresenter.getappclient(ACTION_DEFAULT + 3, id, all_perice);
                    } else if (isBalance && !isZfb) {
                        ToastUtils.centerToastWhite(this, "余额不足，请选择其他支付方式");
                    }
                } else {
                    if (isBalance && isZfb) {
                        mvpPresenter.payRechargeOrder(ACTION_DEFAULT + 2, UserInfo.getInstance().getId(), id);
                    } else if (isBalance && !isZfb) {
                        mvpPresenter.payRechargeOrder(ACTION_DEFAULT + 2, UserInfo.getInstance().getId(), id);
                    } else if (isZfb && !isBalance) {
                        mvpPresenter.getappclient(ACTION_DEFAULT + 3, id, need_pay_money);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void getDataSuccess(Object model, int action) {
        if (model != null) {
            if (action == ACTION_DEFAULT + 1) {
                try {
                    JSONObject jsonObject = new JSONObject((String) model);  //{"user_money":"10000.00","all_perice":"350.00"}
                    all_perice = jsonObject.getString("all_perice");
                    user_money = jsonObject.getString("user_money");
                    need_pay_money = jsonObject.getString("need_pay_money");
                    totalPay.setText("￥" + all_perice);
                    balance.setText("￥" + user_money);
                    needPay.setText("￥" + need_pay_money);
                    if (Double.parseDouble(user_money) == 0.00) {
                        useZfb.setChecked(true);
                        useBalance.setEnabled(false);
                    } else {
                        useBalance.setChecked(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (action == ACTION_DEFAULT + 2) {
                RequestResult data = (RequestResult) model;
                String status = data.getStatus();
                if (TextUtils.equals(Constant.request_success, status)) {
                    ToastUtils.centerToastWhite(this, "支付成功");
                    finish();
                } else {
                    ToastUtils.centerToastWhite(this, "余额不足，请充值");
                }
                if (isZfb) {
                    ToastUtils.centerToastWhite(this, "开始支付宝支付");
                    double price = Double.parseDouble(all_perice) - Double.parseDouble(user_money);
                    mvpPresenter.getappclient(ACTION_DEFAULT + 3, id, String.valueOf(price));
                }
            } else if (action == ACTION_DEFAULT + 3) {
                try {
                    JSONObject jsonObject = new JSONObject((String) model);
                    String responseBody = jsonObject.getString("responseBody");
                    pay(responseBody);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void getDataFail(String msg, int action) {

    }

    @Override
    protected CashierCenterPresenter createPresenter() {
        return new CashierCenterPresenter(this);
    }

    public void pay(final String orderInfo) {

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask payTask = new PayTask(CashierCenterActivity.this);
                // 调用支付接口，获取支付结果
                String result = payTask.pay(orderInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {    //同步支付请求
                    PayResult payResult = new PayResult((String) msg.obj);
//                       PayResult payResult = new PayResult((String) msg.obj);
                    /** 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知*/
                    String resultStatus = payResult.getResultStatus();
                    //     payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        EventBus.getDefault().post(Constant.EVENT_REFRESH_ORDER_TOTAL);
                        //表示支付成功
                        ToastUtils.centerToastWhite(CashierCenterActivity.this, "支付成功");
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            ToastUtils.centerToastWhite(CashierCenterActivity.this, "支付结果确认中");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            ToastUtils.centerToastWhite(CashierCenterActivity.this, "支付失败");
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.cb_cashier_use_balance:
                isBalance = b;
                break;
            case R.id.cb_cashier_use_zfb:
                isZfb = b;
                break;
        }
    }
}
