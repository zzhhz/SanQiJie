package com.chehubang.duolejie.modules.home.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.modules.home.presenter.ScanPresenter;
import com.chehubang.duolejie.modules.payqr.activity.PayQrActivity;

import common.Utils.ToastUtils;
import common.mvp.activity.MainView;
import zxing.activity.CaptureFragment;
import zxing.activity.CodeUtils;


/**
 * Created by Thinkpad on 2017/12/11.
 */

public class ScanActivity extends BaseActivity<ScanPresenter> implements MainView, View.OnClickListener {


    private FrameLayout container;
    private CaptureFragment captureFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        container = findAtyViewById(R.id.fl_my_container);
        ImageView back = (ImageView) findViewById(R.id.iv_scan_back);
        back.setOnClickListener(this);
        captureFragment = new CaptureFragment();
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 3);
        }
        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 4);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_scan_back:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 二维码解析回调函数
     */

    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            PayQrActivity.open(ScanActivity.this, result);
            finish();
        }

        @Override
        public void onAnalyzeFailed() {
            finish();
            ToastUtils.centerToastWhite(ScanActivity.this, "获取SN号码失败");
        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


    @Override
    public void getDataSuccess(Object model, int action) {

    }

    @Override
    public void getDataFail(String msg, int action) {

    }

    @Override
    protected ScanPresenter createPresenter() {
        return new ScanPresenter(this);
    }
}
