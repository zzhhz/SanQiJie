package com.chehubang.duolejie.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import common.mvp.fragment.BasicFragment;

/**
 * Created by Beidouht on 2017/10/13.
 */

public abstract class BaseFragment<P extends MvpPresenter> extends BasicFragment {
    protected P mvpPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mvpPresenter = createPresenter();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter = createPresenter();
    }

    protected abstract P createPresenter();


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mvpPresenter != null) {
            mvpPresenter.detachView();
        }
    }
}
