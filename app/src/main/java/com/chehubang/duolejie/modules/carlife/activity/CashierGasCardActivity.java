package com.chehubang.duolejie.modules.carlife.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.model.OilCardMoneyTypeBean;
import com.chehubang.duolejie.modules.carlife.presenter.CashierGasCardPresenter;
import com.chehubang.duolejie.utils.log;

import org.json.JSONException;
import org.json.JSONObject;

import common.Utils.ToastUtils;
import common.mvp.activity.MainView;

/**
 * Created by Beidouht on 2017/12/20.
 */

public class CashierGasCardActivity extends BaseActivity<CashierGasCardPresenter> implements MainView, View.OnClickListener {

    private TextView totalPay, balance, needPay, commit;
    private static final int SDK_PAY_FLAG = 1;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_desk);
        ImageView back = (ImageView) findViewById(R.id.iv_cashiercenter_back);
        totalPay = (TextView) findViewById(R.id.tv_cashiercenter_total);
        balance = (TextView) findViewById(R.id.tv_cashiercenter_balance);
        needPay = (TextView) findViewById(R.id.tv_cashiercenter_need_pay);
        commit = (TextView) findViewById(R.id.tv_cashiercenter_commit);
        back.setOnClickListener(this);
        commit.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent != null) {
            OilCardMoneyTypeBean data = (OilCardMoneyTypeBean) intent.getSerializableExtra("data");
            id = intent.getStringExtra("id");    //这里获取到订单号
            UserInfo.getInstance().getIdCache(this);
            mvpPresenter.getBalance(ACTION_DEFAULT + 1, UserInfo.getInstance().getId(), id);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cashiercenter_back:
                finish();
                break;
            case R.id.tv_cashiercenter_commit:
                String s = needPay.getText().toString();
                mvpPresenter.payRechargeOrder(ACTION_DEFAULT + 2, UserInfo.getInstance().getId(), id);

                if (TextUtils.isEmpty(s)) {
                    ToastUtils.centerToastWhite(this, "暂无数据");
                } else if (Integer.parseInt(s) == 0) {
                    //调用余额支付接口
                    mvpPresenter.payRechargeOrder(ACTION_DEFAULT + 2, UserInfo.getInstance().getId(), id);
                    //mvpPresenter.getAppclient(ACTION_DEFAULT + 3, id, s);
                } else {
                    //调用支付宝
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void getDataSuccess(Object model, int action) {
        if (action == ACTION_DEFAULT + 1) {
            if (model != null) {
                try {
                    JSONObject jsonObject = new JSONObject((String) model);  //{"user_money":"10000.00","all_perice":"350.00"}
                    String all_perice = jsonObject.getString("all_perice");
                    String user_money = jsonObject.getString("user_money");
                    String need_pay_money = jsonObject.getString("need_pay_money");
                    totalPay.setText("￥" + all_perice);
                    balance.setText("￥" + user_money);
                    needPay.setText("￥" + need_pay_money);
                    //int needpay = need_pay_money.
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } else if (action == ACTION_DEFAULT + 2) {
            //  pay(id);
            log.d(model.toString());
            if (model != null) {
                ToastUtils.showToast(this, "支付成功");
            } else {
                ToastUtils.showToast(this, "支付失败");
            }
        }
    }

    @Override
    public void getDataFail(String msg, int action) {

    }

    @Override
    protected CashierGasCardPresenter createPresenter() {
        return new CashierGasCardPresenter(this);
    }

    public void pay(final String orderInfo) {

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask payTask = new PayTask(CashierGasCardActivity.this);
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
                case SDK_PAY_FLAG: {
                    //   PayResult payResult = new PayResult((String) msg.obj);
                    /** 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知*/
                    // String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = null;
                    //     payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        //表示支付成功
                        // Toast.makeText(CashierGasCardActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(CashierGasCardActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(CashierGasCardActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

}
