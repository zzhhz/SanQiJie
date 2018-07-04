package com.chehubang.duolejie.modules.carlife.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.model.OilCardBean;
import com.chehubang.duolejie.model.OilCardMoneyTypeBean;
import com.chehubang.duolejie.modules.carlife.adapter.MoneyTypeAdapter;
import com.chehubang.duolejie.modules.carlife.presenter.GasCardRecordPresenter;
import com.chehubang.duolejie.modules.chargecenter.activity.CashierCenterActivity;
import com.chehubang.duolejie.modules.home.activity.WebviewActivity;
import com.chehubang.duolejie.widget.IconGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import common.Utils.JSONUtils;
import common.Utils.ToastUtils;
import common.mvp.activity.MainView;

/**
 * Created by Administrator on 2017/12/17 0017.
 */

public class GasCardRecordActivity extends BaseActivity<GasCardRecordPresenter> implements MainView, View.OnClickListener, GridView.OnItemClickListener {

    ImageView back;
    private IconGridView moneyType;
    private List<OilCardMoneyTypeBean> mlist = new ArrayList<>();
    private List<OilCardBean> oilCardBeanList = new ArrayList<>();
    private MoneyTypeAdapter moneyTypeAdapter;
    private TextView money, selectMoney, person, gas_filling_card, tv_buy_commit, tv_lettory;
    private OilCardMoneyTypeBean select = null;
    private OilCardBean oilSelect = null;
    private int selectPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gas_filling_card);
        moneyType = (IconGridView) findViewById(R.id.igv_gas_record_type);
        back = (ImageView) findViewById(R.id.iv_back);
        person = (TextView) findViewById(R.id.tv_gas_record_person);
        money = (TextView) findViewById(R.id.tv_gas_record_money);
        gas_filling_card = (TextView) findViewById(R.id.gas_filling_card);
        tv_lettory = (TextView) findViewById(R.id.tv_lettory);
        tv_buy_commit = (TextView) findViewById(R.id.tv_buy_commit);
        selectMoney = (TextView) findViewById(R.id.tv_gas_record_select_money);
        TextView introduction = (TextView) findViewById(R.id.tv_gas_record_introduction);
        LinearLayout contract = (LinearLayout) findViewById(R.id.ll_gas_filling_contract);
        moneyTypeAdapter = new MoneyTypeAdapter(this, mlist);
        moneyType.setAdapter(moneyTypeAdapter);
        moneyType.setOnItemClickListener(this);
        gas_filling_card.setOnClickListener(this);
        tv_buy_commit.setOnClickListener(this);
        contract.setOnClickListener(this);
        back.setOnClickListener(this);
        introduction.setOnClickListener(this);
        UserInfo.getInstance().getIdCache(this);
        String app_login_id = UserInfo.getInstance().getId();
        mvpPresenter.getCardStatus(ACTION_DEFAULT + 1, app_login_id);
        if (app_login_id == null) {
            selectMoney.setText(0 + "元");
        }
    }

    @Override
    public void getDataSuccess(Object model, int action) {
        if (action == ACTION_DEFAULT + 1) {
            String data = (String) model;
            if (data != null) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String amout_total = jsonObject.getString("amout_total");
                    String person_total = jsonObject.getString("person_total");
                    JSONArray oilCardList = jsonObject.getJSONArray("oilCardList");
                    JSONArray userOilCardList = jsonObject.getJSONArray("getUserOilCardList");
                    if (userOilCardList.length() > 0) {
                        for (int i = 0; i < userOilCardList.length(); i++) {
                            OilCardBean oilCardBean = JSONUtils.GsonToBean(userOilCardList.get(i).toString(), OilCardBean.class);
                            oilCardBeanList.add(oilCardBean);
                        }
                        gas_filling_card.setText("已绑定:" + oilCardBeanList.get(0).getCard_num());
                        gas_filling_card.setClickable(false);
                    }
                    mlist.clear();
                    for (int i = 0; i < oilCardList.length(); i++) {
                        OilCardMoneyTypeBean oilCardMoneyTypeBean = JSONUtils.GsonToBean(oilCardList.get(i).toString(), OilCardMoneyTypeBean.class);
                        mlist.add(oilCardMoneyTypeBean);
                    }
                    moneyTypeAdapter.notifyDataSetChanged();
                    person.setText(person_total);
                    money.setText(amout_total + "元");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (action == ACTION_DEFAULT + 2) {
            if (model != null) {
                String orderid = (String) model;
                Intent intent = new Intent(this, CashierCenterActivity.class);
                intent.putExtra("id", orderid);
                startActivityForResult(intent, 1000);
            }
        }
    }


    @Override
    public void getDataFail(String msg, int action) {
        ToastUtils.centerToastWhite(this, msg);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.gas_filling_card:
                Intent intent = new Intent(this, BindCardActivity.class);
                startActivityForResult(intent, 200);
                break;
            case R.id.tv_buy_commit:
                if (mlist.size() > 0 && oilCardBeanList.size() > 0 && selectPosition >= 0) {
                    mvpPresenter.addOilCardOrder(ACTION_DEFAULT + 2, UserInfo.getInstance().getId(), mlist.get(selectPosition).getValue(), oilCardBeanList.get(0).getCard_num());
                } else if (oilCardBeanList.size() == 0) {
                    ToastUtils.centerToastWhite(this, "请添加加油卡");
                } else if (selectPosition == -1) {
                    ToastUtils.centerToastWhite(this, "请选择充值金额");
                } else {
                    ToastUtils.centerToastWhite(this, "数据加载失败");
                }
                break;
            case R.id.ll_gas_filling_contract:
                Intent intent7 = new Intent(this, WebviewActivity.class);
                intent7.putExtra("url", "http://39.107.14.118/appInterface/aboutUsContentInfo.jhtml?id=11");  //外网版本
                // intent7.putExtra("url","http://192.168.3.25:8090/GetTreasureAppJinDong/appInterface/aboutUsContentInfo.jhtml?id=1&is_show_message=y");
                intent7.putExtra("title", "加油卡协议");
                startActivity(intent7);
                break;
            case R.id.tv_gas_record_introduction:
                Intent intent1 = new Intent(this, WebviewActivity.class);
                intent1.putExtra("url", "http://39.107.14.118/appInterface/aboutUsContentInfo.jhtml?id=13");  //外网版本
                // intent7.putExtra("url","http://192.168.3.25:8090/GetTreasureAppJinDong/appInterface/aboutUsContentInfo.jhtml?id=1&is_show_message=y");
                intent1.putExtra("title", "加油卡介绍");
                startActivity(intent1);
                break;
               /* else {
                ToastUtils.centerToastWhite(this,"请选择套餐");
            }*/
        }
    }

    @Override
    protected GasCardRecordPresenter createPresenter() {
        return new GasCardRecordPresenter(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (mlist.size() > 0) {
            moneyTypeAdapter.setSelectedPosition(position);
            moneyTypeAdapter.notifyDataSetChanged();
            String value = mlist.get(position).getValue();
            selectPosition = position;
            selectMoney.setText(value + "元");
            tv_lettory.setText("充值可获取" + mlist.get(position).getLettory() + "多乐券");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 200 && resultCode == RESULT_OK) {
            String app_login_id = UserInfo.getInstance().getId();
            mvpPresenter.getCardStatus(ACTION_DEFAULT + 1, app_login_id);
        } else if (requestCode == 1000 && resultCode == RESULT_OK) {

        }
    }
}
