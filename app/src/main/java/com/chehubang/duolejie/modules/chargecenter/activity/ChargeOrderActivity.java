package com.chehubang.duolejie.modules.chargecenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.model.AddressListBean;
import com.chehubang.duolejie.model.AddressListMangerBean;
import com.chehubang.duolejie.model.ChargeOrderGoodsBean;
import com.chehubang.duolejie.modules.address.activity.AddressManageActivity;
import com.chehubang.duolejie.modules.chargecenter.adapter.GoodsListAdapter;
import com.chehubang.duolejie.modules.chargecenter.presenter.ChargeOrderPresenter;
import com.chehubang.duolejie.modules.home.activity.WebviewActivity;
import com.chehubang.duolejie.utils.log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import common.Utils.JSONUtils;
import common.Utils.ToastUtils;
import common.mvp.activity.MainView;

/**
 * @Date: 2018/1/29 21:23
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description: 填写订单
 */
public class ChargeOrderActivity extends BaseActivity<ChargeOrderPresenter> implements MainView, View.OnClickListener {

    private TextView name, tel, address, conponInfo,
            conponumber, amount, deliveryFee, buy, buyAmount;
    private String colorid;
    private String specid;
    private String num;
    private String id;
    private double amount1;
    private String addressId;
    private String freight;
    private ListView goodList;
    private List<ChargeOrderGoodsBean> goodsList = new ArrayList<>();
    private GoodsListAdapter goodsListAdapter;
    private String is_shop_cart;
    private AddressListBean listBean;
    private String price;
    private String color;
    private String spec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_order);
        ImageView back = (ImageView) findViewById(R.id.iv_charge_order_back);
        LinearLayout addressSelect = (LinearLayout) findViewById(R.id.ll_charge_order_address);
        FrameLayout conponSelect = (FrameLayout) findViewById(R.id.fl_charge_order_conpon);
        name = (TextView) findViewById(R.id.tv_charge_order_name);
        tel = (TextView) findViewById(R.id.tv_charge_order_tel);
        address = (TextView) findViewById(R.id.tv_charge_order_address);
        conponInfo = (TextView) findViewById(R.id.tv_charge_order_conpon);
        conponumber = (TextView) findViewById(R.id.tv_charge_order_conpon_number);
        amount = (TextView) findViewById(R.id.tv_charge_order_amount);
        deliveryFee = (TextView) findViewById(R.id.tv_charge_order_delivery);
        goodList = (ListView) findViewById(R.id.lv_charge_order_goods_list);
        buy = (TextView) findViewById(R.id.tv_charge_order_buy);
        buyAmount = (TextView) findViewById(R.id.tv_charge_order_buy_amount);
        LinearLayout contract = (LinearLayout) findViewById(R.id.ll_charge_order_contract);
        conponumber.setText(UserInfo.getString("user_lettory") + "张");
        goodsListAdapter = new GoodsListAdapter(this, goodsList);
        goodList.setAdapter(goodsListAdapter);
        back.setOnClickListener(this);
        addressSelect.setOnClickListener(this);
        conponSelect.setOnClickListener(this);
        buy.setOnClickListener(this);
        contract.setOnClickListener(this);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        colorid = intent.getStringExtra("colorid");
        specid = intent.getStringExtra("specid");
        num = intent.getStringExtra("num");
        price = intent.getStringExtra("price");
        is_shop_cart = intent.getStringExtra("is_shop_cart");
        color = intent.getStringExtra("color");
        spec = intent.getStringExtra("spec");
        UserInfo.getInstance().getIdCache(this);
        mvpPresenter.payRechargeOrder(ACTION_DEFAULT + 1, UserInfo.getInstance().getId(), id, num, is_shop_cart);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_charge_order_back:
                finish();
                break;
            case R.id.ll_charge_order_address:
                Intent intent = new Intent(this, AddressManageActivity.class);
                startActivityForResult(intent, 7001);
                break;
            case R.id.fl_charge_order_conpon:
                //          ToastUtils.centerToastWhite(this, "点击了优惠劵");
                break;
            case R.id.tv_charge_order_buy:
                if (listBean != null) {
                    mvpPresenter.getOrderNo(ACTION_DEFAULT + 2, price, id, num, specid, colorid, String.valueOf(amount1), addressId, is_shop_cart, UserInfo.getInstance().getId(), freight, spec, color);
                } else {
                    ToastUtils.centerToastWhite(this, "对不起，您还没有设置地址");
                }
                break;
            case R.id.ll_charge_order_contract:
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 7001) {
            if (resultCode == 7002) {
                AddressListMangerBean data1 = (AddressListMangerBean) data.getSerializableExtra("data");
                updataDefaultAddress(data1);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void getDataSuccess(Object model, int action) {
        if (action == ACTION_DEFAULT + 1) {
            if (model != null) {
                try {
                    JSONObject jsonObject = new JSONObject((String) model);
                    JSONArray goods_buy_list = jsonObject.getJSONArray("goods_buy_List");
                    String all_price = jsonObject.getString("all_price");
                    freight = jsonObject.getString("freight");
                    amount.setText("￥" + all_price);
                    deliveryFee.setText("￥" + freight);
                    amount1 = Double.parseDouble(all_price) + Double.parseDouble(freight);
                    buyAmount.setText("应付： ￥" + amount1);
                    if (goods_buy_list.length() > 0) {
                        JSONObject o = (JSONObject) goods_buy_list.get(0);
                        ArrayList<ChargeOrderGoodsBean> list = JSONUtils.GsonjsonToArrayList(goods_buy_list.toString(), ChargeOrderGoodsBean.class);
                        goodsList.addAll(list);
                        goodsListAdapter.notifyDataSetChanged();
                    }
                    JSONArray userAddressList = jsonObject.getJSONArray("userAddressList");
                    if (userAddressList.length() > 0) {
                        JSONObject item = (JSONObject) userAddressList.get(0);
                        listBean = JSONUtils.GsonToBean(item.toString(), AddressListBean.class);
                        updataAddress(listBean);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (action == ACTION_DEFAULT + 2) {
            if (model != null) {
                String orderid = (String) model;
                log.d(orderid);
                Intent intent = new Intent(this, CashierCenterActivity.class);
                // intent.putExtra("data", select);
                intent.putExtra("id", orderid);
                log.d(orderid);
                startActivity(intent);
            }
        }
    }

    private void updataDefaultAddress(AddressListMangerBean item) {
        addressId = item.getId();
        name.setText(item.getUser_name());
        tel.setText(item.getUser_tel());
        address.setText(item.getProvince_name() + item.getCity_name() + item.getDetail_address());
    }

    private void updataAddress(AddressListBean item) {
        addressId = item.getId();
        name.setText(item.getUser_name());
        tel.setText(item.getUser_tel());
        address.setText(item.getProvince_addr() + item.getCity_addr() + item.getDetail_address());
    }

    @Override
    public void getDataFail(String msg, int action) {
        log.d(msg);
    }

    @Override
    protected ChargeOrderPresenter createPresenter() {
        return new ChargeOrderPresenter(this);
    }
}
