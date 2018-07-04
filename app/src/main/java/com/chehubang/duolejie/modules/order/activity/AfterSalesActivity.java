package com.chehubang.duolejie.modules.order.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.model.OrderBean;
import com.chehubang.duolejie.modules.order.adapter.AfterSalesAdapter;
import com.chehubang.duolejie.modules.order.presenter.AfterSalesPresenter;

import java.util.ArrayList;
import java.util.List;

import common.mvp.activity.MainView;
/**
 * Created by ZZH on 2018/1/31
 *
 * @date: 2018/1/31 上午11:52
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @Description: 售后/退换货
 */
public class AfterSalesActivity extends BaseActivity<AfterSalesPresenter> implements MainView, View.OnClickListener {

    private ListView order_list;
    private AfterSalesAdapter orderAdapter;
    private ArrayList<OrderBean> mlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_sales);
        order_list = (ListView) findViewById(R.id.lv_after_sales_list);
        ImageView back = (ImageView) findViewById(R.id.iv_after_sales_back);
        mvpPresenter.getOderList(ACTION_DEFAULT + 1, UserInfo.getInstance().getId(), "退款退货", "1", "50");
        orderAdapter = new AfterSalesAdapter(this, mlist);
        order_list.setAdapter(orderAdapter);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_after_sales_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void getDataSuccess(Object model, int action) {
        if (model != null) {
            if (action == ACTION_DEFAULT + 1) {
                mlist.clear();
                List<OrderBean> data = (List<OrderBean>) model;
                mlist.addAll(data);
                orderAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void getDataFail(String msg, int action) {

    }

    @Override
    protected AfterSalesPresenter createPresenter() {
        return new AfterSalesPresenter(this);
    }
}
