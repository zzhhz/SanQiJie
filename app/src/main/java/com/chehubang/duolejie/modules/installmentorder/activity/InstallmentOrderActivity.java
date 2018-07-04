package com.chehubang.duolejie.modules.installmentorder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.model.InstallOrderBean;
import com.chehubang.duolejie.modules.installmentorder.fragment.GascardFragment;
import com.chehubang.duolejie.modules.installmentorder.fragment.PhoneChargeFragment;
import com.chehubang.duolejie.modules.installmentorder.presenter.InstallmentOrderPresenter;

import java.util.ArrayList;
import java.util.List;

import common.mvp.activity.MainView;

/**
 * Created by ZZH on 2018/1/31
 *
 * @date: 2018/1/31 下午5:18
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @Description: 分期订单
 */
public class InstallmentOrderActivity extends BaseActivity<InstallmentOrderPresenter> implements MainView, View.OnClickListener {


    private TabLayout mTabLayout;
    private ViewPager viewPager;
    private List<InstallOrderBean> mlist = new ArrayList<>();
    private String order_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ImageView back = (ImageView) findViewById(R.id.iv_order_back);
        mTabLayout = (TabLayout) findViewById(R.id.tl_order_tabs);
        viewPager = (ViewPager) findViewById(R.id.vp_order_viewpager);
        TextView details = (TextView) findViewById(R.id.tv_view_details);
        back.setOnClickListener(this);
        for (int i = 0; i < mlist.size(); i++) {
            order_id = mlist.get(i).getOrder_id();
        }
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            private String[] mTitles = new String[]{"加油卡", "手机充值"};

            @Override
            public Fragment getItem(int position) {
                if (position == 1) {
                    return new PhoneChargeFragment();
                } else {
                    return new GascardFragment();
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
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_order_back:
                finish();
                break;
            case R.id.tv_view_details:
                Intent intent = new Intent(this, InstallDetailsActivity.class);
                intent.putExtra("order_id", order_id);
                startActivity(intent);
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
    protected InstallmentOrderPresenter createPresenter() {
        return new InstallmentOrderPresenter(this);
    }
}
