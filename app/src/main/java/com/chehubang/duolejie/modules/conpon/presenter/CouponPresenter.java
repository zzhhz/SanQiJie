package com.chehubang.duolejie.modules.conpon.presenter;

import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.modules.conpon.activity.CouponActivity;

/**
 * Created by ZZH on 2018/1/29.
 *
 * @Date: 2018/1/29
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description:
 */
public class CouponPresenter extends MvpPresenter<CouponActivity> {
    public CouponPresenter(CouponActivity activity) {
        attachView(activity);
    }
}
