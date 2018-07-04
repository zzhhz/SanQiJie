package com.chehubang.duolejie.modules.setting.presenter;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.modules.setting.activitiy.SettingActivity;

/**
 * Created by Thinkpad on 2017/12/25.
 */

public class SettingPresenter extends MvpPresenter<SettingActivity> {

    public SettingPresenter(SettingActivity activity) {
        attachView(activity);
    }
}
