package com.chehubang.duolejie.modules.chargecenter.fragment;

import com.chehubang.duolejie.base.BaseFragment;
import com.chehubang.duolejie.modules.chargecenter.activity.RechargeActivity;
import com.chehubang.duolejie.modules.chargecenter.presenter.RechargePresenter;

import common.mvp.activity.MainView;

/**
 * Created by ZZH on 2018/1/30.
 *
 * @Date: 2018/1/30
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description: 提现记录
 */
public class CashFragment extends BaseFragment<RechargePresenter> implements MainView{
    @Override
    protected RechargePresenter createPresenter() {
        return new RechargePresenter((RechargeActivity) getActivity());
    }

    @Override
    public void getDataSuccess(Object model, int action) {

    }

    @Override
    public void getDataFail(String msg, int action) {

    }
}
