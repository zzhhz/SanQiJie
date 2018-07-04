package com.chehubang.duolejie.modules.order.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.listener.OnPicDeleteListener;
import com.chehubang.duolejie.listener.OnloadDataListener;
import com.chehubang.duolejie.model.Logistic;
import com.chehubang.duolejie.model.OrderBean;
import com.chehubang.duolejie.modules.logistic.activity.LogisticCompanyActivity;
import com.chehubang.duolejie.modules.order.adapter.PicAdapter;
import com.chehubang.duolejie.modules.order.presenter.ReturnOrderPresenter;
import com.chehubang.duolejie.utils.UploadUtil;
import com.chehubang.duolejie.widget.IconGridView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;
import common.Utils.BitmapUtils;
import common.Utils.ToastUtils;
import common.mvp.activity.MainView;
import common.picture.PictureUtils;

/**
 * Created by ZZH on 2018/1/31
 *
 * @date: 2018/1/31 下午2:30
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @Description: 退货退款
 */
public class ReturnOrderActivity extends BaseActivity<ReturnOrderPresenter> implements MainView, View.OnClickListener, OnloadDataListener, OnPicDeleteListener {

    private IconGridView piclist;
    private List<MediaBean> mlist = new ArrayList<>();
    private PicAdapter picAdapter;

    @BindView(R.id.tv_logistic)
    TextView tv_logistic;
    @BindView(R.id.tv_order_item_goods_title)
    TextView tv_order_item_goods_title;
    @BindView(R.id.tv_order_item_goods_type)
    TextView tv_order_item_goods_type;
    @BindView(R.id.tv_order_item_goods_price)
    TextView tv_order_item_goods_price;

    Logistic logistic;
    @BindView(R.id.et_order_num)
    EditText et_order_num;
    @BindView(R.id.tv_phone)
    EditText tv_phone;

    @BindView(R.id.et_rule)
    EditText et_rule;

    @BindView(R.id.et_money)
    TextView et_money;
    @BindView(R.id.iv_order_item_goods_pic)
    ImageView iv_order_item_goods_pic;
    private int picNum = 0;

    private String id;
    private String money;
    private OrderBean mOrderBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_order);
        ButterKnife.bind(this);
        piclist = (IconGridView) findViewById(R.id.gv_order_piclist);
        picAdapter = new PicAdapter(this, mlist);
        piclist.setAdapter(picAdapter);
        picAdapter.addOnloadDataListener(this);
        picAdapter.addOnPicDeleteListener(this);
        id = getIntent().getStringExtra("id");
        mOrderBean = getIntent().getParcelableExtra("order");
        mvpPresenter.getChildReturnOrder(1, id);

        bindOrder();
    }

    private void bindOrder() {
        PictureUtils.loadPicture(this, mOrderBean.getGood_header(), iv_order_item_goods_pic, R.mipmap.ic_share_logo);
        tv_order_item_goods_title.setText(mOrderBean.getGood_name());
        tv_order_item_goods_type.setText(mOrderBean.getGoods_color() + " " + mOrderBean.getGoods_spec());
        tv_order_item_goods_price.setText("￥ " + mOrderBean.getGood_price());

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void getDataSuccess(Object model, int action) {
        dismissDialog();
        switch (action) {
            case 1:
                bindMoney(model.toString());
                break;
            case 2:
                ToastUtils.centerToastWhite(this, "提交成功");
                finish();
                break;
        }
    }

    /**
     * {"returnLetory":"12","returnMoney":"62.00"}
     *
     * @param str
     */
    private void bindMoney(String str) {
        try {
            JSONObject obj = new JSONObject(str);
            money = obj.getString("returnMoney");
            et_money.setText("￥ " + money);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void getDataFail(String msg, int action) {
        ToastUtils.centerToastWhite(this, msg);
    }

    @Override
    protected ReturnOrderPresenter createPresenter() {
        return new ReturnOrderPresenter(this);
    }

    @Override
    public void OnPicDelete(int position) {
        picAdapter.notifyDataSetChanged();
        if (position > 0) {
            picNum--;
        }
    }

    @OnClick({R.id.fl_logistic, R.id.iv_edit_address_back, R.id.tv_submit})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.fl_logistic:
                Intent intent = new Intent(this, LogisticCompanyActivity.class);
                startActivityForResult(intent, 1000);
                break;
            case R.id.iv_edit_address_back:
                finish();
                break;
            case R.id.tv_submit:
                if (logistic == null) {
                    ToastUtils.centerToastWhite(this, "请选择物流公司");
                    return;
                } else if (TextUtils.isEmpty(et_order_num.getText().toString())) {
                    ToastUtils.centerToastWhite(this, "请输入物流单号");
                    return;
                } else if (TextUtils.isEmpty(tv_phone.getText().toString())) {
                    ToastUtils.centerToastWhite(this, "请输入手机号");
                    return;
                } else {
                    showDialog();
                    new Thread() {
                        @Override
                        public void run() {
                            if (mlist != null && !mlist.isEmpty()) {
                                List<String> list = new ArrayList<>();
                                for (MediaBean bean : mlist) {
                                    String url = UploadUtil.getInstance().toAsyncUploadFile(bean.getOriginalPath());
                                    list.add(UploadUtil.picPath(url));
                                }
                                Message msg = Message.obtain();
                                msg.obj = list;
                                msg.what = 1;
                                mHandler.sendMessage(msg);

                            }
                        }
                    }.start();
                }

                break;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mvpPresenter.getOrderReturn(2, id, logistic.getId() + "", et_order_num.getText().toString(), tv_phone.getText().toString()
                    , et_rule.getText().toString(), (ArrayList<String>) msg.obj, money);
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            logistic = data.getParcelableExtra("data");
            tv_logistic.setText(logistic.getLogistics());
        }
    }

    @Override
    public void OnloadData(ImageMultipleResultEvent baseResultEvent) {
        List<MediaBean> result = baseResultEvent.getResult();
        for (int i = 0; i < result.size(); i++) {
            BitmapUtils.compressImage(baseResultEvent.getResult().get(i).getOriginalPath());
        }
        mlist.addAll(result);
        picNum = mlist.size();
        picAdapter.notifyDataSetChanged();
    }
}
