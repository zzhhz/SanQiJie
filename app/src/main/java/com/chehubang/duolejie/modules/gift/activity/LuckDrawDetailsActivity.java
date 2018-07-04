package com.chehubang.duolejie.modules.gift.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.model.CouponListBean;
import com.chehubang.duolejie.model.GoodsBrandInfoMapBean;
import com.chehubang.duolejie.model.UserListBean;
import com.chehubang.duolejie.modules.gift.adapter.PersonListAdapter;
import com.chehubang.duolejie.modules.gift.dialog.ConfirmDialog;
import com.chehubang.duolejie.modules.gift.presenter.LuckDrawDetailsPresenter;
import com.chehubang.duolejie.utils.DialogUtils;
import com.chehubang.duolejie.utils.log;
import com.chehubang.duolejie.widget.QuantityView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import common.Utils.JSONUtils;
import common.Utils.ToastUtils;
import common.http.RequestResult;
import common.mvp.activity.MainView;
import common.picture.PictureUtils;

/**
 * Created by fuyi on 2017/12/21.
 */

public class LuckDrawDetailsActivity extends BaseActivity<LuckDrawDetailsPresenter> implements MainView, View.OnClickListener {

    private ArrayList<GoodsBrandInfoMapBean> detailList = new ArrayList<>();
    private ArrayList<CouponListBean> couponList = new ArrayList<>();
    private ArrayList<UserListBean> userList = new ArrayList<>();
    private ImageView header;
    private TextView shopContent, shopAddress, shopPhone, conponNo, needPeople, pregress;
    private ProgressBar conponProgress;
    private Button confirm;
    private ListView personList;
    private PersonListAdapter personListAdapter;
    private String coupon_id;
    int mPage = 1;
    private QuantityView quantityView;
    private String mNum;
    private String user_id;
    private int i;

    private TextView tv_show_activity;
    private Dialog mDialog;

    private TextView tv_canyu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luckdrawdetails);
        ImageView iv_back = (ImageView) findViewById(R.id.iv_luck_detail_back);
        header = (ImageView) findViewById(R.id.iv_gift_detail_shop_pic);
        shopContent = (TextView) findViewById(R.id.tv_ld_jianjie_content);
        shopAddress = (TextView) findViewById(R.id.tv_ld_jianjie_address);
        shopPhone = (TextView) findViewById(R.id.tv_ld_jianjie_phone);
        needPeople = (TextView) findViewById(R.id.tv_ld_need_people);
        pregress = (TextView) findViewById(R.id.tv_pregress);
        conponNo = (TextView) findViewById(R.id.tv_ld_qihao);
        conponProgress = (ProgressBar) findViewById(R.id.progressbar_ld);
        quantityView = (QuantityView) findViewById(R.id.amount_view_ld);
        confirm = (Button) findViewById(R.id.btn_ld_confirm);
        personList = (ListView) findViewById(R.id.lv_ld_list);
        tv_canyu = (TextView) findViewById(R.id.tv_canyu);
        tv_show_activity = (TextView) findViewById(R.id.tv_show_activity);
        personListAdapter = new PersonListAdapter(this, userList);
        personList.setAdapter(personListAdapter);
        iv_back.setOnClickListener(this);
        confirm.setOnClickListener(this);
        Intent intent = getIntent();
        log.d("----------LuckDrawDetailsActivity-------------");
        if (intent != null) {
            user_id = intent.getStringExtra("user_id");
            String brand_id = intent.getStringExtra("brand_id");
            coupon_id = intent.getStringExtra("coupon_id");
            mvpPresenter.getLuckDrawDetailsData(ACTION_DEFAULT + 1, user_id, brand_id, coupon_id);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_luck_detail_back:
                finish();
                break;
            case R.id.btn_ld_confirm:
                if (TextUtils.isEmpty(UserInfo.getInstance().getId())) {
                    ToastUtils.centerToastWhite(this, "请登录");
                    return;
                }
                int quantity = quantityView.getQuantity();
                initDialog(quantity);
                break;
        }
    }

    private void initDialog(final int quantity) {
        final ConfirmDialog pd = new ConfirmDialog(this);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        View dialog = View.inflate(this, R.layout.dialog_gift_confirm, null);
        TextView totalNum = (TextView) dialog.findViewById(R.id.tv_dialog_num);
        TextView canyu = (TextView) dialog.findViewById(R.id.tv_dialog_canyu);
        String user_lettory = UserInfo.getString("user_lettory");
        if (TextUtils.isEmpty(user_lettory)) {
            totalNum.setText("0");
            if (TextUtils.isEmpty(UserInfo.getInstance().getId())) {
                ToastUtils.showToast(this, "未获取到个人信息");
            } else {
                ToastUtils.showToast(this, "对不起，您无奖券可用");
            }
        } else {
            totalNum.setText(UserInfo.getString("user_lettory"));
        }
        i = quantity * Integer.parseInt(mNum);
        canyu.setText(i + "");
        TextView cancel = (TextView) dialog.findViewById(R.id.btn_add_qx);
        TextView confirm = (TextView) dialog.findViewById(R.id.btn_add_qr);
        pd.setContentView(dialog);
        pd.show();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mvpPresenter.getConfirm(ACTION_DEFAULT + 2, UserInfo.getInstance().getId(), coupon_id, i, String.valueOf(quantity));
                pd.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.dismiss();
            }
        });
    }

    @Override
    public void getDataSuccess(Object model, int action) {
        if (action == ACTION_DEFAULT + 1) {
            if (model != null) {
                try {
                    detailList.clear();
                    couponList.clear();
                    JSONObject jsonObject = new JSONObject((String) model);
                    boolean flag = jsonObject.getBoolean("is_flag");
                    try {

                        String is_activity = jsonObject.getString("is_activity");
                        if (TextUtils.equals("y", is_activity)){
                            quantityView.setVisibility(View.GONE);
                            tv_canyu.setVisibility(View.GONE);
                        } else {
                            quantityView.setVisibility(View.VISIBLE);
                            tv_canyu.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception ex)
                    {
                        quantityView.setVisibility(View.GONE);
                        tv_canyu.setVisibility(View.GONE);
                    }

                    if (flag) {
                        tv_show_activity.setText("您已经参与了本次活动");

                    } else {
                        tv_show_activity.setText("您尚未参与了本次活动");
                    }

                    JSONObject map = jsonObject.getJSONObject("goodsBrandInfoMap");
                    GoodsBrandInfoMapBean GoodsBrandInfoMapBean = JSONUtils.GsonToBean(map.toString(), GoodsBrandInfoMapBean.class);
                    initShopping(GoodsBrandInfoMapBean);
                    JSONArray couponListar = jsonObject.getJSONArray("couponList");
                    ArrayList<CouponListBean> couponListBeans = JSONUtils.GsonjsonToArrayList(couponListar.toString(), CouponListBean.class);
                    couponList.addAll(couponListBeans);
                    initConpon();
                    JSONArray userListar = jsonObject.getJSONArray("userList");
                    ArrayList<UserListBean> userListBeans = JSONUtils.GsonjsonToArrayList(userListar.toString(), UserListBean.class);
                    userList.addAll(userListBeans);
                    personListAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (action == ACTION_DEFAULT + 2) {
            if (model != null) {
                RequestResult data = (RequestResult) model;
                if (TextUtils.equals(Constant.request_success, data.getStatus())) {
                    //ToastUtils.centerToastWhite(this, "成功");
                    if (mDialog == null) {
                        mDialog = DialogUtils.getShowMsg(this, "抽奖参与成功", getResources().getString(R.string.msg_gift));
                    }
                    mDialog.show();
                } else {
                    ToastUtils.centerToastWhite(this, data.getDesc());
                }
            }
        }
    }

    private void initConpon() {
        CouponListBean couponListBean = couponList.get(0);
        mNum = couponListBean.getNeed_lettory_num();
        conponNo.setText("期号：" + couponListBean.getPeriod());
        conponProgress.setProgress(couponListBean.getProgress());
        needPeople.setText("需多乐券：" + couponListBean.getNeed_lettory_num() + "劵/人");
        pregress.setText(couponListBean.getProgress() + "%");
        if ("倒计时".equals(couponListBean.getStatus()) || "已揭晓".equals(couponListBean.getStatus())){
            confirm.setVisibility(View.GONE);
        } else {
            confirm.setVisibility(View.VISIBLE);
        }
    }

    private void initShopping(GoodsBrandInfoMapBean bean) {
        PictureUtils.loadPicture(this, bean.getBrand_header(), header, R.drawable.pic_cycjjl);
        shopContent.setText(bean.getBrand_introduction());
        shopAddress.setText(bean.getBrand_address());
        shopPhone.setText(bean.getBrand_tel());
    }

    @Override
    public void getDataFail(String msg, int action) {

    }

    @Override
    protected LuckDrawDetailsPresenter createPresenter() {
        return new LuckDrawDetailsPresenter(this);
    }
}
