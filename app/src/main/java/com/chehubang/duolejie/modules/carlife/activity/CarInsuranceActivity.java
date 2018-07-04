package com.chehubang.duolejie.modules.carlife.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.model.BannerBean;
import com.chehubang.duolejie.modules.carlife.presenter.CarInsurancePresenter;
import com.chehubang.duolejie.modules.home.activity.WebviewActivity;
import com.chehubang.duolejie.utils.SoftHideKeyBoardUtil;
import com.chehubang.duolejie.widget.KeyboardLayout;
import com.chehubang.duolejie.widget.RecyclerViewBanner;

import java.util.List;

import common.Utils.JSONUtils;
import common.Utils.StringUtils;
import common.Utils.ToastUtils;
import common.mvp.activity.MainView;
import common.picture.PictureUtils;

/**
 * Created by Administrator on 2017/12/17 0017.
 */

public class CarInsuranceActivity extends BaseActivity<CarInsurancePresenter> implements MainView, View.OnClickListener {

    private EditText mname, mphone, plateNumber, carType;
    private RecyclerViewBanner mViewBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.car_insurance);
        super.onCreate(savedInstanceState);
        SoftHideKeyBoardUtil.assistActivity(this);
        ImageView back = (ImageView) findViewById(R.id.iv_back);
        TextView problem = (TextView) findViewById(R.id.tv_insurance_problem);
        TextView order = (TextView) findViewById(R.id.tv_insurance_order);
        plateNumber = (EditText) findViewById(R.id.et_insurance_plate);
        carType = (EditText) findViewById(R.id.et_insurance_type);
        mname = (EditText) findViewById(R.id.et_insurance_name);
        mphone = (EditText) findViewById(R.id.et_insurance_phone);
        mViewBanner = (RecyclerViewBanner) findViewById(R.id.rvb_banner_image_view_id);
        TextView commit = (TextView) findViewById(R.id.tv_insurance_commit);
        back.setOnClickListener(this);
        problem.setOnClickListener(this);
        order.setOnClickListener(this);
        commit.setOnClickListener(this);
        UserInfo.getInstance().getCache(this);
        mvpPresenter.addBanner(ACTION_DEFAULT + 1, "5");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_insurance_problem:
                Intent intent2 = new Intent(CarInsuranceActivity.this, WebviewActivity.class);
                intent2.putExtra("url", "http://39.107.14.118/appInterface/commonProblemInfo.jhtml?problem_type=2");
                intent2.putExtra("title", "常见问题");
                startActivity(intent2);
                break;
            case R.id.tv_insurance_order:
                //ToastUtils.showToast(this, "暂无订单");
                Intent intent = new Intent(this, CarRecordActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_insurance_commit:
                String number = plateNumber.getText().toString();
                String name = mname.getText().toString();
                String phone = mphone.getText().toString();
                String type = carType.getText().toString();
                if (TextUtils.isEmpty(number)) {
                    ToastUtils.centerToastWhite(this, "请填写车牌号");
                } else if (TextUtils.isEmpty(name)) {
                    ToastUtils.centerToastWhite(this, "请填写姓名");
                } else if (TextUtils.isEmpty(phone)) {
                    ToastUtils.centerToastWhite(this, "请填写手机号码");
                } else if (TextUtils.isEmpty(type)) {
                    ToastUtils.centerToastWhite(this, "请填写车牌型号");
                } else if (!StringUtils.isPhone(phone)) {
                    ToastUtils.centerToastWhite(this, "请填写正确的手机号码");
                } else {
                    mvpPresenter.addCarinsurance(ACTION_DEFAULT + 2, phone, UserInfo.getInstance().getId(), number, type, name);
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void getDataSuccess(Object model, int action) {
        if (action == ACTION_DEFAULT + 1) {
            //ToastUtils.showToast(this, model.toString());
            final Wrapper wrapper = JSONUtils.GsonToBean(model.toString(), Wrapper.class);
            if (wrapper != null && wrapper.getAdvertisementList() != null && !wrapper.getAdvertisementList().isEmpty()) {
                mViewBanner.setRvAutoPlaying(false);
                mViewBanner.setRvBannerData(wrapper.getAdvertisementList());
                mViewBanner.isShowIndicator(false);
                mViewBanner.setOnRvBannerClickListener(new RecyclerViewBanner.OnRvBannerClickListener() {
                    @Override
                    public void onClick(int position) {
                        BannerBean bannerBean = wrapper.getAdvertisementList().get(position);
                        bannerBean.getIs_jump();
                        if ("y".equals(bannerBean.getIs_jump())) {
                            Intent intent = new Intent(CarInsuranceActivity.this, WebviewActivity.class);
                            intent.putExtra("url", bannerBean.getUrl());
                            intent.putExtra("title", "车险规则");
                            startActivity(intent);
                        }
                    }
                });
                mViewBanner.setOnSwitchRvBannerListener(new RecyclerViewBanner.OnSwitchRvBannerListener() {
                    @Override
                    public void switchBanner(int position, AppCompatImageView bannerView) {
                        bannerView.setScaleType(ImageView.ScaleType.FIT_XY);
                        PictureUtils.loadPicture(CarInsuranceActivity.this, wrapper.getAdvertisementList().get(position).getHeader(), bannerView, R.drawable.icon_logo);
                    }
                });
            }
        } else if (action == ACTION_DEFAULT + 2) {
            ToastUtils.centerToastWhite(this, model.toString());
        }
    }

    @Override
    public void getDataFail(String msg, int action) {
        ToastUtils.showToast(this, msg);
    }

    @Override
    protected CarInsurancePresenter createPresenter() {
        return new CarInsurancePresenter(this);
    }

    class Wrapper {
        private List<BannerBean> advertisementList;

        public List<BannerBean> getAdvertisementList() {
            return advertisementList;
        }

        public void setAdvertisementList(List<BannerBean> advertisementList) {
            this.advertisementList = advertisementList;
        }
    }
}
