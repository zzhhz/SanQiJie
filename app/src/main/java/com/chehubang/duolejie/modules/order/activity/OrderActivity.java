package com.chehubang.duolejie.modules.order.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.modules.order.fragment.AllOrderFragment;
import com.chehubang.duolejie.modules.order.fragment.HangOrderFragment;
import com.chehubang.duolejie.modules.order.fragment.ShipOrderFragment;
import com.chehubang.duolejie.modules.order.presenter.OrderPresenter;

import java.util.ArrayList;
import java.util.List;

import common.mvp.activity.MainView;

/**
 * Created by Thinkpad on 2017/12/25.
 */

public class OrderActivity extends BaseActivity<OrderPresenter> implements MainView, View.OnClickListener {


    private TabLayout mTabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ImageView back = (ImageView) findViewById(R.id.iv_order_back);
        mTabLayout = (TabLayout) findViewById(R.id.tl_order_tabs);
        viewPager = (ViewPager) findViewById(R.id.vp_order_viewpager);

        back.setOnClickListener(this);


        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            private String[] mTitles = new String[]{"全部订单", "待发货", "已发货"};
            private Fragment fragment1, fragment2,
                    fragment0;

            @Override
            public Fragment getItem(int position) {
                if (position == 1) {    // 专场提醒
                    if (fragment1 == null) {
                        fragment1 = new HangOrderFragment();
                    }
                    return fragment1;
                } else if (position == 2) {
                    if (fragment2 == null) {
                        fragment2 = new ShipOrderFragment();
                    }
                    return fragment2;
                } else {
                    if (fragment0 == null) {
                        fragment0 = new AllOrderFragment();
                    }
                    return fragment0;
                }
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }

            @Override
            public int getCount() {
                return mTitles.length;
            }
        };
        viewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(0);//判断是从哪跳进来的

        Intent intent = getIntent();
        if (intent.hasExtra("position")) {
            int position = intent.getIntExtra("position", 0);
            viewPager.setCurrentItem(position);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_order_back:
                finish();
                break;
        }
    }

    @Override
    public void getDataSuccess(Object model, int action) {

    }

    @Override
    public void getDataFail(String msg, int action) {

    }

    @Override
    protected OrderPresenter createPresenter() {
        return null;
    }
}
