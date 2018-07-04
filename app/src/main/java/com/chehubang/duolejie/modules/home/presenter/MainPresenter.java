package com.chehubang.duolejie.modules.home.presenter;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.modules.home.activity.MainActivity;

/**
 * Created by Thinkpad on 2017/12/9.
 */

public class MainPresenter extends MvpPresenter<MainActivity> {

    public MainPresenter(MainActivity view) {
        attachView(view);
    }

}
