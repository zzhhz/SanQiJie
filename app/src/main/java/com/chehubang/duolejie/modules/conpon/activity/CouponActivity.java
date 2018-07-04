package com.chehubang.duolejie.modules.conpon.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.modules.conpon.presenter.CouponPresenter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.mvp.activity.MainView;

/**
 * Created by ZZH on 2018/1/29.
 *
 * @Date: 2018/1/29
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description:
 */
public class CouponActivity extends BaseActivity<CouponPresenter> implements MainView {

    @BindView(R.id.lv_home_content)
    public RecyclerView lv_home_content;
    @BindView(R.id.srf_home)
    public SmartRefreshLayout mRefreshLayout;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        ButterKnife.bind(this);
    }

    @Override
    public void getDataSuccess(Object model, int action) {

    }

    @Override
    public void getDataFail(String msg, int action) {

    }

    @Override
    protected CouponPresenter createPresenter() {
        return new CouponPresenter(this);
    }
}
