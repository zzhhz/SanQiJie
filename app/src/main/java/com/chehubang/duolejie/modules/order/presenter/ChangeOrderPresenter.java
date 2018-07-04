package com.chehubang.duolejie.modules.order.presenter;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.modules.order.activity.ChangeOrderActivity;

/**
 * Created by Thinkpad on 2017/12/26.
 */

public class ChangeOrderPresenter extends MvpPresenter<ChangeOrderActivity> {

    public ChangeOrderPresenter(ChangeOrderActivity activity) {
        attachView(activity);
    }
}
