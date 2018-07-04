package com.chehubang.duolejie.modules.installmentorder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.model.InstallDetailsBean;
import com.chehubang.duolejie.modules.installmentorder.presenter.InstallDetailsPresenter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import common.Utils.JSONUtils;
import common.mvp.activity.MainView;

/**
 * Created by ZZH on 2018/1/31
 *
 * @date: 2018/1/31 下午5:26
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @Description: 分期订单详情
 */
public class InstallDetailsActivity extends BaseActivity<InstallDetailsPresenter> implements MainView, View.OnClickListener {
    TextView tv_order_id, tv_real_name, tv_goods_buy_nums, tv_all_price, tv_goods_ids, tv_create_time, phone;
    private ArrayList<InstallDetailsBean> mList = new ArrayList<>();

    String id;
    private View ll_card;
    private TextView tv_title;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_install_detail);

        tv_order_id = (TextView) findViewById(R.id.tv_order_id);
        tv_real_name = (TextView) findViewById(R.id.tv_real_name);
        tv_goods_buy_nums = (TextView) findViewById(R.id.tv_goods_buy_nums);
        tv_all_price = (TextView) findViewById(R.id.tv_all_price);
        tv_goods_ids = (TextView) findViewById(R.id.tv_goods_ids);
        tv_create_time = (TextView) findViewById(R.id.tv_create_time);
        ll_card = findViewById(R.id.ll_card);
        tv_title = (TextView) findViewById(R.id.tv_title);
        phone = (TextView) findViewById(R.id.phone);
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("order_id");
            type = intent.getIntExtra("type", -1);
            if (type == 1) {
                tv_title.setText("车互帮（北京）科技股份有限公司出售的分期充值卡");
            } else {
                tv_title.setText("车互帮（北京）科技股份有限公司出售的分期加油卡");
            }
            mvpPresenter.getRechargeDetailList(ACTION_DEFAULT + 1, id);
        }

        findViewById(R.id.iv_after_sales_back).setOnClickListener(this);
        findViewById(R.id.rl_more).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_after_sales_back:
                finish();
                break;
            case R.id.rl_more:
                MoreInstallDetailsActivity.open(this, id, type);
                break;
        }
    }

    @Override
    public void getDataSuccess(Object model, int action) {
        if (action == ACTION_DEFAULT + 1) {
            if (model != null) {
                try {
                    JSONObject jsonObject = new JSONObject((String) model);
                    JSONObject shopCartList = jsonObject.getJSONObject("rechargeMap");
                    mList.clear();
                    InstallDetailsBean installDetailsBean = JSONUtils.GsonToBean(shopCartList.toString(), InstallDetailsBean.class);
                    mList.add(installDetailsBean);
                    String real_name = mList.get(0).getReal_name();
                    if ("null".equals(real_name) || real_name == null || "".equals(real_name)) {
                        real_name = "";
                        ll_card.setVisibility(View.GONE);
                    }
                    if (type == 1) {
                        phone.setText("手机号");
                    } else {
                        phone.setText("绑定加油卡账号");
                    }
                    tv_goods_ids.setText(mList.get(0).getGoods_ids());
                    tv_real_name.setText(real_name + "");
                    tv_goods_buy_nums.setText(mList.get(0).getGoods_buy_nums() + "张");
                    tv_all_price.setText(mList.get(0).getAll_price() + "元");
                    tv_order_id.setText(mList.get(0).getOrder_id());
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    tv_create_time.setText(df.format(new Date(mList.get(0).getCreate_time())));
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
    protected InstallDetailsPresenter createPresenter() {
        return new InstallDetailsPresenter(this);
    }
}
