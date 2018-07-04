package com.chehubang.duolejie.modules.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseFragment;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.model.BannerBean;
import com.chehubang.duolejie.model.OrderBean;
import com.chehubang.duolejie.modules.order.adapter.OrderAdapter;
import com.chehubang.duolejie.modules.order.presenter.OrderPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import common.Utils.JSONUtils;
import common.mvp.activity.MainView;
import common.picture.PictureUtils;

/**
 * Created by Thinkpad on 2017/12/25.
 */

public class AllOrderFragment extends BaseFragment<OrderPresenter> implements MainView, View.OnClickListener, OrderAdapter.OnClickConfirmListener {

    private ListView order_list;
    private ArrayList<OrderBean> mlist = new ArrayList<>();
    private OrderAdapter orderAdapter;
    private ImageView banner;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        banner = (ImageView) view.findViewById(R.id.iv_banner);
        order_list = (ListView) view.findViewById(R.id.lv_order_list);
        mvpPresenter.getOderList(ACTION_DEFAULT + 1, UserInfo.getInstance().getId(), "", "1", "50");
        orderAdapter = new OrderAdapter(getActivity(), mlist);
        order_list.setAdapter(orderAdapter);
        orderAdapter.setConfirmListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter.getBanner(ACTION_DEFAULT + 3, "7");
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void getDataSuccess(Object model, int action) {
        if (model != null) {
            if (action == ACTION_DEFAULT + 1) {
                mlist.clear();
                List<OrderBean> data = (List<OrderBean>) model;
                mlist.addAll(data);
                orderAdapter.notifyDataSetChanged();
            } else if (action == ACTION_DEFAULT + 2) {
                //刷新
                mvpPresenter.getOderList(ACTION_DEFAULT + 1, UserInfo.getInstance().getId(), "", "1", "50");
            } else if (action == ACTION_DEFAULT + 3) {
                if (!TextUtils.isEmpty(model.toString())) {
                    Wrapper wrapper = JSONUtils.GsonToBean(model.toString(), Wrapper.class);
                    if (wrapper != null && wrapper.getAdvertisementList() != null && !wrapper.getAdvertisementList().isEmpty()) {
                        PictureUtils.loadPicture(getActivity(), wrapper.getAdvertisementList().get(0).getHeader(), banner, R.drawable.default_avatar);
                    } else {
                        banner.setVisibility(View.GONE);
                    }
                } else {
                    banner.setVisibility(View.GONE);
                }
            }
        }

    }

    @Override
    public void getDataFail(String msg, int action) {

    }

    @Override
    protected OrderPresenter createPresenter() {
        return new OrderPresenter(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventHandle(String action) {
        if (Constant.EVENT_REFRESH_ORDER_TOTAL.equals(action)) {
            mvpPresenter.getOderList(ACTION_DEFAULT + 1, UserInfo.getInstance().getId(), "", "1", "50");
        }
    }

    @Override
    public void onClickConfirm(String child_order_id) {
        if (!TextUtils.isEmpty(child_order_id)) {
            mvpPresenter.onConfirmGoods(ACTION_DEFAULT + 2, child_order_id);
        }

    }

    class Wrapper {
        private List<BannerBean> advertisementList;

        public List<BannerBean> getAdvertisementList() {
            return advertisementList;
        }

        public void setAdvertisementList(List<BannerBean> advertisementList) {
            this.advertisementList = advertisementList;
        }
    }
}
