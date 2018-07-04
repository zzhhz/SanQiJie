package com.chehubang.duolejie.modules.gooddetails.presenter;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.modules.gooddetails.activity.GoodsDetailsActivity;

/**
 * Created by Thinkpad on 2017/12/15.
 */

public class GoodsDetailsPresenter extends MvpPresenter<GoodsDetailsActivity> {

    public GoodsDetailsPresenter(GoodsDetailsActivity activity) {
        attachView(activity);
    }


}
