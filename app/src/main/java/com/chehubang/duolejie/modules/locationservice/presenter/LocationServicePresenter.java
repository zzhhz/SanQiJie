package com.chehubang.duolejie.modules.locationservice.presenter;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.modules.locationservice.activity.LocationServiceActivity;

/**
 * Created by Beidouht on 2017/12/21.
 */

public class LocationServicePresenter extends MvpPresenter<LocationServiceActivity> {

    public LocationServicePresenter(LocationServiceActivity activity) {
        attachView(activity);
    }
}
