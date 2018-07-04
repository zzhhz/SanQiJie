package com.chehubang.duolejie.modules.gooddetails.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.modules.gooddetails.fragment.GoodsDetailsFragment;
import com.chehubang.duolejie.modules.gooddetails.fragment.GoodsFragment;
import com.chehubang.duolejie.modules.gooddetails.presenter.GoodsDetailsPresenter;

import common.mvp.activity.MainView;

/**
 * Created by Thinkpad on 2017/12/15.
 */

public class GoodsDetailsActivity extends BaseActivity<GoodsDetailsPresenter> implements MainView, View.OnClickListener {

    private GoodsFragment goodsFragment;
    private GoodsDetailsFragment goodsDetailsFragment;
    private Fragment mFragment;//当前显示的Fragment
    private RadioButton rbGoods, rbDetails;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);
        initView();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fl_goods_details_content, goodsFragment).commit();
        mFragment = goodsFragment;
    }

    private void initView() {
        rbGoods = findAtyViewById(R.id.rb_goods_details_goods);
        rbDetails = findAtyViewById(R.id.rb_goods_details);
        TextView back = findAtyViewById(R.id.tv_goodsdetails_back);
        back.setOnClickListener(this);
        rbGoods.setOnClickListener(this);
        rbDetails.setOnClickListener(this);
        goodsFragment = new GoodsFragment();
        goodsDetailsFragment = new GoodsDetailsFragment();
        ;
    }

    @Override
    public void getDataSuccess(Object model, int action) {

    }

    @Override
    public void getDataFail(String msg, int action) {

    }

    @Override
    protected GoodsDetailsPresenter createPresenter() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb_goods_details_goods:
                rbGoods.setTextColor(Color.WHITE);
                rbGoods.setBackgroundColor(Color.parseColor("#999999"));
                rbDetails.setTextColor(Color.parseColor("#333333"));
                rbDetails.setBackgroundColor(Color.WHITE);
                switchFragment(goodsFragment);
                break;
            case R.id.rb_goods_details:
                rbGoods.setTextColor(Color.parseColor("#333333"));
                rbGoods.setBackgroundColor(Color.WHITE);
                rbDetails.setTextColor(Color.WHITE);
                rbDetails.setBackgroundColor(Color.parseColor("#999999"));
                switchFragment(goodsDetailsFragment);
                break;
            case R.id.tv_goodsdetails_back:
                finish();
                break;
            default:
                break;
        }
    }

    private void switchFragment(Fragment fragment) {
        if (mFragment != fragment) {
            if (!fragment.isAdded()) {
                getSupportFragmentManager().beginTransaction().hide(mFragment).add(R.id.fl_goods_details_content, fragment).commit();
            } else {
                getSupportFragmentManager().beginTransaction().hide(mFragment).show(fragment).commit();
            }
            mFragment = fragment;
        }
    }

}
