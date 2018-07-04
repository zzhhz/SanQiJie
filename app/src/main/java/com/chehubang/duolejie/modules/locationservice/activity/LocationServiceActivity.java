package com.chehubang.duolejie.modules.locationservice.activity;

import android.os.Bundle;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.modules.locationservice.presenter.LocationServicePresenter;

import common.mvp.activity.MainView;

/**
 * Created by Beidouht on 2017/12/21.
 */

public class LocationServiceActivity  extends BaseActivity<LocationServicePresenter> implements MainView{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locationservice);
    }

    @Override
    public void getDataSuccess(Object model, int action) {

    }

    @Override
    public void getDataFail(String msg, int action) {

    }

    @Override
    protected LocationServicePresenter createPresenter() {
        return new LocationServicePresenter(this);
    }
}
