package com.chehubang.duolejie.modules.setting.activitiy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.model.NoticeListBean;
import com.chehubang.duolejie.modules.home.activity.WebviewActivity;
import com.chehubang.duolejie.modules.setting.adapter.NoticeAdapter;
import com.chehubang.duolejie.modules.setting.presenter.NoticePresenter;

import java.util.ArrayList;
import java.util.List;

import common.mvp.activity.MainView;

/**
 * Created by Thinkpad on 2017/12/25.
 */

public class NoticeActivity extends BaseActivity<NoticePresenter> implements MainView, View.OnClickListener, ListView.OnItemClickListener {

    private List<NoticeListBean> mlist = new ArrayList<>();
    private NoticeAdapter noticeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        ListView noticeList = (ListView) findViewById(R.id.lv_notice_list);
        mvpPresenter.getNoticeList(ACTION_DEFAULT + 1, "1", "20");
        noticeAdapter = new NoticeAdapter(this, mlist);
        noticeList.setAdapter(noticeAdapter);
        noticeList.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void getDataSuccess(Object model, int action) {
        if (model != null) {
            if (action == ACTION_DEFAULT + 1) {
                mlist.clear();
                List<NoticeListBean> data = (List<NoticeListBean>) model;
                mlist.addAll(data);
                noticeAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void getDataFail(String msg, int action) {

    }

    @Override
    protected NoticePresenter createPresenter() {
        return new NoticePresenter(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (mlist.size() > 0) {
            NoticeListBean noticeListBean = mlist.get(i);
            Intent intent = new Intent(this, WebviewActivity.class);
            intent.putExtra("url",noticeListBean.getHref());
            intent.putExtra("title",noticeListBean.getTitle());
            startActivity(intent);
        }
    }
}
