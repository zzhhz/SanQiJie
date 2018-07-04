package com.chehubang.duolejie.modules.payqr.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.modules.gift.dialog.ConfirmDialog;
import com.chehubang.duolejie.modules.login.activity.LoginActivity;
import com.chehubang.duolejie.modules.payqr.presenter.PayQrPresenter;
import com.chehubang.duolejie.utils.log;

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
 * @Description: 扫描二维码付款界面。
 */
public class PayQrActivity extends BaseActivity<PayQrPresenter> implements MainView {

    public static final String KEY_ID = "store_id";

    public static void open(Context ctx, String storeId) {
        Intent intent = new Intent(ctx, PayQrActivity.class);
        intent.putExtra(KEY_ID, storeId);
        ctx.startActivity(intent);
    }

    @BindView(R.id.et_money)
    EditText et_money;
    @BindView(R.id.tv_store)
    TextView tv_store;
    private String storeId;
    @BindView(R.id.et_ps)
    public EditText et_ps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_pay);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        storeId = intent.getStringExtra(KEY_ID);
        mvpPresenter.getBrandMsg(1, storeId);
    }

    @Override
    public void getDataSuccess(Object model, int action) {
        switch (action) {
            case 1:
                bindBrandData(model.toString());
                break;
            case 2:
                showPayResultMsg(model.toString());
                break;
        }
        EventBus.getDefault().post(Constant.EVENT_REFRESH_USER);

    }

    ConfirmDialog mDialog = null;

    private void showPayResultMsg(String s) {
        mDialog = new ConfirmDialog(this);
        View content = LayoutInflater.from(this).inflate(R.layout.dialog_msg, null);
        TextView textView = (TextView) content.findViewById(R.id.tv_msg);
        TextView confirm = (TextView) content.findViewById(R.id.tv_confirm);
        if (s.equals("1")) {
            EventBus.getDefault().post(Constant.EVENT_REFRESH_MONEY);
            textView.setText("支付成功");
            mDialog.setCanceledOnTouchOutside(false);
            confirm.setText("确定");
        } else {
            textView.setText("余额不足");
            mDialog.setCanceledOnTouchOutside(true);
            confirm.setText("取消");
        }
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialog != null) {
                    mDialog.dismiss();
                    PayQrActivity.this.finish();
                }
            }
        });
        mDialog.setContentView(content);
        mDialog.show();
    }

    /**
     * {"goodsBrandMap":{"id":1000,"brand_name":"多乐街"}}
     *
     * @param s
     */
    private void bindBrandData(String s) {
        log.d(s);
        try {
            JSONObject obj = new JSONObject(s);
            JSONObject brandMap = obj.getJSONObject("goodsBrandMap");
            String brandName = brandMap.getString("brand_name");
            tv_store.setText(brandName);
        } catch (JSONException e) {
            ToastUtils.centerToastWhite(this, "商家信息获取失败");
            e.printStackTrace();
        }
    }

    @Override
    public void getDataFail(String msg, int action) {
        switch (action) {
            case 1:
                ToastUtils.centerToastWhite(this, "商家信息获取失败");
                findViewById(R.id.tv_pay).setClickable(false);
                break;
            default:
                ToastUtils.centerToastWhite(this, msg);
                break;
        }

    }

    @Override
    protected PayQrPresenter createPresenter() {
        return new PayQrPresenter(this);
    }

    @OnClick({R.id.iv_edit_address_back, R.id.tv_pay})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.iv_edit_address_back:
                finish();
                break;
            case R.id.tv_pay:
                if (TextUtils.isEmpty(et_money.getText().toString())) {
                    ToastUtils.centerToastWhite(this, "请输入支付金额");
                } else {
                    if (!TextUtils.isEmpty(UserInfo.getInstance().getId())) {
                        mvpPresenter.payBalance(2, et_money.getText().toString(), storeId, et_ps.getText().toString());
                    } else {
                        Intent intent = new Intent(this, LoginActivity.class);
                        intent.putExtra("type", 2);
                        startActivity(intent);
                    }
                }
                break;
        }
    }
}
