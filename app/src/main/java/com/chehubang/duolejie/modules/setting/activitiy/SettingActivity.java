package com.chehubang.duolejie.modules.setting.activitiy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.modules.setting.dialog.ClearCacheDialog;
import com.chehubang.duolejie.modules.setting.presenter.SettingPresenter;
import com.chehubang.duolejie.utils.DataCleanManager;

import common.Utils.ToastUtils;
import common.mvp.activity.MainView;

/**
 * Created by Thinkpad on 2017/12/25.
 */

public class SettingActivity extends BaseActivity<SettingPresenter> implements MainView, View.OnClickListener {

    private TextView tvclear;
    private ClearCacheDialog clearCacheDialog;
    private final String ACTION_SWITCH = "com.chehubang.duolejie.ACTION_SWITCH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ImageView back = (ImageView) findViewById(R.id.iv_setting_back);
        TextView push = (TextView) findViewById(R.id.tv_setting_push);
        TextView share = (TextView) findViewById(R.id.tv_setting_share);
        TextView exit = (TextView) findViewById(R.id.tv_setting_exit);
        FrameLayout clear = (FrameLayout) findViewById(R.id.fl_setting_clear);
        tvclear = (TextView) findViewById(R.id.tv_setting_clear);
        back.setOnClickListener(this);
        push.setOnClickListener(this);
        clear.setOnClickListener(this);
        share.setOnClickListener(this);
        exit.setOnClickListener(this);
        findViewById(R.id.fl_setting_sugestion).setOnClickListener(this);
        String totalCacheSize = DataCleanManager.getTotalCacheSize(this);
        tvclear.setText(totalCacheSize);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_setting_back:
                finish();
                break;
            case R.id.tv_setting_push:
                Intent intent = new Intent(this, NoticeActivity.class);
                startActivity(intent);
                break;
            case R.id.fl_setting_clear:
                clearCacheDialog = new ClearCacheDialog(this);
                clearCacheDialog.show();
                clearCacheDialog.setOnButtonClickListener(this);
                break;
            case R.id.tv_setting_share:
                ToastUtils.centerToastWhite(this, "点击了分享");
                break;
            case R.id.tv_setting_exit:
                UserInfo.getInstance().clearCache(this);
                Intent intent1 = new Intent();
                intent1.setAction(ACTION_SWITCH);
                intent1.putExtra("action", "fragment4");
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
                finish();
                break;
            case R.id.clear_cache_negative_Button:
                clearCacheDialog.dismiss();
                break;
            case R.id.clear_cache_positive_Button:
                clearCacheDialog.dismiss();
                Toast.makeText(this, "清除完成", Toast.LENGTH_SHORT).show();
                tvclear.setText(0 + "M");
                DataCleanManager.clearAllCache(this);
                break;
            case R.id.fl_setting_sugestion:
                Intent intent2 = new Intent(this, SuggestActivity.class);
                startActivity(intent2);
                break;

            default:
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
    protected SettingPresenter createPresenter() {
        return null;
    }
}
