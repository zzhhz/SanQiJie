package com.chehubang.duolejie.modules.home.presenter;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.modules.home.activity.ScanActivity;

/**
 * Created by Thinkpad on 2017/12/13.
 */

public class ScanPresenter extends MvpPresenter<ScanActivity> {

    public ScanPresenter(ScanActivity activity) {
        attachView(activity);
    }
}
