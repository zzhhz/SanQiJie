package com.chehubang.duolejie.modules.installmentorder.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseFragment;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.model.InstallOrderBean;
import com.chehubang.duolejie.modules.installmentorder.activity.InstallDetailsActivity;
import com.chehubang.duolejie.modules.installmentorder.adapter.GascardAdapter;
import com.chehubang.duolejie.modules.installmentorder.presenter.InstallmentOrderPresenter;

import java.util.ArrayList;
import java.util.List;

import common.mvp.activity.MainView;

/**
 * Created by Thinkpad on 2017/12/25.
 */

public class GascardFragment extends BaseFragment<InstallmentOrderPresenter> implements MainView, View.OnClickListener {

    private ListView order_list;
    private ArrayList<InstallOrderBean> mlist = new ArrayList<>();
    private GascardAdapter orderAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order3, container, false);
        order_list = (ListView) view.findViewById(R.id.lv_order_list);
        mvpPresenter.getRechargeList(ACTION_DEFAULT + 1, UserInfo.getInstance().getId(), "1", "50");
        orderAdapter = new GascardAdapter(getActivity(), mlist);
        order_list.setAdapter(orderAdapter);
        order_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), InstallDetailsActivity.class);
                intent.putExtra("order_id", orderAdapter.getItem(position).getOrder_id());
                intent.putExtra("type", 2);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void getDataSuccess(Object model, int action) {
        if (model != null) {
            if (action == ACTION_DEFAULT + 1) {
                mlist.clear();
                List<InstallOrderBean> data = (List<InstallOrderBean>) model;
                mlist.addAll(data);
                orderAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public void getDataFail(String msg, int action) {

    }

    @Override
    protected InstallmentOrderPresenter createPresenter() {
        return new InstallmentOrderPresenter(this);
    }
}
