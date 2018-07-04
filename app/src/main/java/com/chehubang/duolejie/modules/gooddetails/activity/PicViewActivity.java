package com.chehubang.duolejie.modules.gooddetails.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.modules.gooddetails.presenter.GoodsDetailsPresenter;

import common.mvp.activity.MainView;
import common.picture.PictureUtils;

/**
 * Created by Thinkpad on 2017/12/15.
 */

public class PicViewActivity extends BaseActivity<GoodsDetailsPresenter> implements MainView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picview);
        ImageView picview = findAtyViewById(R.id.pic_view);
        PictureUtils.loadPicture(this,"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1513336236328&di=c4fbbf69e3502e4131e8e2b181761bc5&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3D544ab2da8f35e5dd8421ad9c1eafcd9a%2Fe61190ef76c6a7efd593faaef7faaf51f2de6684.jpg",picview,R.drawable.forget_pwd_bg);
    }

    @Override
    public void getDataSuccess(Object model, int action) {

    }

    @Override
    public void getDataFail(String msg, int action) {

    }

    @Override
    protected GoodsDetailsPresenter createPresenter() {
        return null;
    }
}
