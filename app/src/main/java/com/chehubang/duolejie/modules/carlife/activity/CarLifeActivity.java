package com.chehubang.duolejie.modules.carlife.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.model.CarLifePicBean;
import com.chehubang.duolejie.modules.carlife.presenter.CarLifePresenter;
import com.chehubang.duolejie.widget.RecyclerViewBanner;

import java.util.ArrayList;
import java.util.List;

import common.mvp.activity.MainView;
import common.picture.PictureUtils;

/**
 * Created by Administrator on 2017/12/14 0014.
 */

public class CarLifeActivity extends BaseActivity<CarLifePresenter> implements MainView, View.OnClickListener {

    private TextView gas_filling_card, car_insurance;
    private List<CarLifePicBean> pagerList = new ArrayList<>();
    private RecyclerViewBanner headerPic;
    private List<String> banners = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_life);
        ImageView back = (ImageView) findViewById(R.id.iv_back);
        gas_filling_card = (TextView) findViewById(R.id.gas_filling_card);
        car_insurance = (TextView) findViewById(R.id.car_insurance);
        headerPic = (RecyclerViewBanner) findViewById(R.id.iv_carlife_pic);
        back.setOnClickListener(this);
        gas_filling_card.setOnClickListener(this);
        car_insurance.setOnClickListener(this);
        mvpPresenter.getCarLifeBanner(ACTION_DEFAULT + 1, "4");
    }

    @Override
    protected CarLifePresenter createPresenter() {
        return new CarLifePresenter(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_back) {
            finish();
        } else if (id == R.id.gas_filling_card) {
            Intent intent = new Intent(this, GasCardRecordActivity.class);
            startActivity(intent);
        } else if (id == R.id.car_insurance) {
            Intent intent = new Intent(this, CarInsuranceActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public void getDataSuccess(Object model, int action) {
        if (action == ACTION_DEFAULT + 1) {
            ArrayList<CarLifePicBean> data = (ArrayList<CarLifePicBean>) model;
            if (data.size() > 0) {
                pagerList.clear();
                pagerList.addAll(data);
                headerPic.isShowIndicator(true);
                headerPic.setRvBannerData(pagerList);
                headerPic.setOnSwitchRvBannerListener(new RecyclerViewBanner.OnSwitchRvBannerListener() {
                    @Override
                    public void switchBanner(int position, AppCompatImageView bannerView) {
                        bannerView.setScaleType(ImageView.ScaleType.FIT_XY);
                        PictureUtils.loadPicture(CarLifeActivity.this, pagerList.get(position % pagerList.size()).getHeader(), bannerView, R.drawable.pic_cycjjl);
                    }
                });
            }
        }
    }

    @Override
    public void getDataFail(String msg, int action) {

    }
}
