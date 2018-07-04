package com.chehubang.duolejie.modules.chargecenter.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.model.PayResult;
import com.chehubang.duolejie.modules.chargecenter.presenter.RechargePresenter;
import com.chehubang.duolejie.utils.CodeUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import common.Utils.ToastUtils;
import common.mvp.activity.MainView;

/**
 * Created by ZZH on 2018/1/30.
 *
 * @Date: 2018/1/30
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description: 充值
 */
public class RechargeActivity extends BaseActivity<RechargePresenter> implements MainView{

    @BindView(R.id.tv_money)
    EditText tv_money;
    @BindView(R.id.tv_m)
    TextView tv_m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        ButterKnife.bind(this);
        String user_money = UserInfo.getInstance().getUser_money();
        tv_m.setText("您的余额：" + CodeUtils.scaleValue(Double.parseDouble(user_money)) + " 元");

    }

    @Override
    protected RechargePresenter createPresenter() {
        return new RechargePresenter(this);
    }

    private String mon;

    @OnClick({R.id.tv_chong, R.id.iv_order_back})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.tv_chong:
                mon = tv_money.getText().toString();
                if (!TextUtils.isEmpty(mon)) {
                    mvpPresenter.recharge(2, mon);
                } else {
                    ToastUtils.showToast(this, "请输入充值金额");
                }
                break;
            case R.id.iv_order_back:
                finish();
                break;
        }
    }

    @Override
    public void getDataSuccess(Object model, int action) {
        if (action == 2){
            mvpPresenter.getAppClient(3, model.toString(), mon);
        } else if (action == 3){
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(model.toString());
                String responseBody = jsonObject.getString("responseBody");
                pay(responseBody);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
    private static final int SDK_PAY_FLAG = 1;
    public void pay(final String orderInfo) {

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask payTask = new PayTask(RechargeActivity.this);
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
                        //表示支付成功
                        EventBus.getDefault().post(Constant.EVENT_REFRESH_USER);
                        ToastUtils.centerToastWhite(RechargeActivity.this, "支付成功");
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            ToastUtils.centerToastWhite(RechargeActivity.this, "支付结果确认中");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            ToastUtils.centerToastWhite(RechargeActivity.this, "支付失败");
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
    public void getDataFail(String msg, int action) {

    }
}
