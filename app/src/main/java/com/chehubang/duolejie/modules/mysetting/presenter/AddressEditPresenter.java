package com.chehubang.duolejie.modules.mysetting.presenter;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.modules.mysetting.activity.AddressEditActivity;

/**
 * Created by Thinkpad on 2017/12/25.
 */

public class AddressEditPresenter extends MvpPresenter<AddressEditActivity> {

    public AddressEditPresenter(AddressEditActivity addressEditActivity) {
        attachView(addressEditActivity);
    }
}
