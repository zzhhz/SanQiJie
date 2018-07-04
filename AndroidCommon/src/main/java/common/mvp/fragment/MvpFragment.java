package common.mvp.fragment;

import android.os.Bundle;
import android.view.View;

import common.mvp.presenter.BasePresenter;

/**
 * Created by Beidouht on 2017/10/13.
 */

public abstract class MvpFragment<P extends BasePresenter> extends BasicFragment {
    protected P mvpPresenter;

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
