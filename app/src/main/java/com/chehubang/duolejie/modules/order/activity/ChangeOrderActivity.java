package com.chehubang.duolejie.modules.order.activity;

import android.os.Bundle;
import android.view.View;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.listener.OnPicDeleteListener;
import com.chehubang.duolejie.listener.OnloadDataListener;
import com.chehubang.duolejie.modules.order.adapter.PicAdapter;
import com.chehubang.duolejie.modules.order.presenter.ChangeOrderPresenter;
import com.chehubang.duolejie.widget.IconGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;
import common.Utils.BitmapUtils;
import common.mvp.activity.MainView;

/**
 * Created by ZZH on 2018/1/31
 *
 * @date: 2018/1/31 上午11:53
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @Description: 换货
 */
public class ChangeOrderActivity extends BaseActivity<ChangeOrderPresenter> implements MainView, View.OnClickListener, OnloadDataListener, OnPicDeleteListener {

    private IconGridView piclist;
    private List<MediaBean> mlist = new ArrayList<>();
    private PicAdapter picAdapter;
    private int picNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_order);
        ButterKnife.bind(this);
        piclist = (IconGridView) findViewById(R.id.gv_order_piclist);
        picAdapter = new PicAdapter(this, mlist);
        piclist.setAdapter(picAdapter);
        picAdapter.addOnloadDataListener(this);
        picAdapter.addOnPicDeleteListener(this);

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void getDataSuccess(Object model, int action) {

    }

    @Override
    public void getDataFail(String msg, int action) {

    }

    @Override
    protected ChangeOrderPresenter createPresenter() {
        return new ChangeOrderPresenter(this);
    }

    @Override
    public void OnPicDelete(int position) {
        picAdapter.notifyDataSetChanged();
        if (position > 0) {
            picNum--;
        }
    }

    @Override
    public void OnloadData(ImageMultipleResultEvent baseResultEvent) {
        List<MediaBean> result = baseResultEvent.getResult();
        for (int i = 0; i < result.size(); i++) {
            BitmapUtils.compressImage(baseResultEvent.getResult().get(i).getOriginalPath());
        }
        mlist.addAll(result);
        picNum = result.size();
        picAdapter.notifyDataSetChanged();
    }
}
