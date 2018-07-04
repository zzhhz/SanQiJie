package com.chehubang.duolejie.modules.home.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseFragment;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.listener.OnHomeIconClickListener;
import com.chehubang.duolejie.listener.OnItemClickListener;
import com.chehubang.duolejie.model.GoodsHotListBean;
import com.chehubang.duolejie.model.GoodsListBean;
import com.chehubang.duolejie.model.RadioListBean;
import com.chehubang.duolejie.model.ViewPagerPicBean;
import com.chehubang.duolejie.modules.carlife.activity.CarLifeActivity;
import com.chehubang.duolejie.modules.category.activity.CategoryLifeActivity;
import com.chehubang.duolejie.modules.chargecenter.activity.ChargeCenterActivity;
import com.chehubang.duolejie.modules.gooddetails.activity.GoodsDetailsActivity;
import com.chehubang.duolejie.modules.home.activity.HomeSearchActivity;
import com.chehubang.duolejie.modules.home.activity.ScanActivity;
import com.chehubang.duolejie.modules.home.adapter.HomeAdapter;
import com.chehubang.duolejie.modules.home.presenter.HomePresenter;
import com.chehubang.duolejie.modules.login.activity.LoginActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import common.Utils.JSONUtils;
import common.mvp.activity.MainView;

/**
 * Created by Thinkpad on 2017/12/9.
 */

public class HomeFragment extends BaseFragment<HomePresenter> implements MainView, View.OnClickListener, OnHomeIconClickListener, OnItemClickListener, OnRefreshListener {

    private SmartRefreshLayout refreshLayout;
    private TextView icon_scan;
    private TextView icon_search;
    private RecyclerView contentlist;
    private List<String> mDatas;
    private HomeAdapter mAdapter;
    private int sum = 0;
    private LinearLayout flHomeTitlebar;
    private List<ViewPagerPicBean> mlist = new ArrayList<>();
    private List<GoodsHotListBean> mgoodHotlist = new ArrayList<>();
    private List<GoodsListBean> mgoodlist = new ArrayList<>();
    private ArrayList<RadioListBean> radioListBeans = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        refreshLayout = (SmartRefreshLayout) view.findViewById(R.id.srf_home);
        icon_scan = (TextView) view.findViewById(R.id.iv_home_scan);
        icon_search = (TextView) view.findViewById(R.id.tv_home_search);
        contentlist = (RecyclerView) view.findViewById(R.id.lv_home_content);
        flHomeTitlebar = (LinearLayout) view.findViewById(R.id.fl_home_titlebar);
        icon_scan.setOnClickListener(this);
        icon_search.setOnClickListener(this);
        contentlist.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new HomeAdapter(getActivity(), mlist, mgoodHotlist, mgoodlist, radioListBeans, this);
        contentlist.setAdapter(mAdapter);
        mvpPresenter.getViewPagerData(ACTION_DEFAULT + 1);
        mvpPresenter.getGoodsList(ACTION_DEFAULT + 2);
        contentlist.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                sum = sum + dy;
                if (sum > getResources().getDimension(R.dimen.dp_165)) {
                    flHomeTitlebar.setBackgroundColor(Color.WHITE);
                } else {
                    flHomeTitlebar.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });
        mAdapter.addOnHomeIconClickListener(this);
        refreshLayout.setOnRefreshListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_home_scan:
                Intent intent = new Intent(getActivity(), ScanActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_home_search:
                Intent intent1 = new Intent(getActivity(), HomeSearchActivity.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(this);
    }

    @Override
    public void getDataSuccess(Object model, int action) {
        refreshLayout.finishRefresh(true);
        if (action == ACTION_DEFAULT + 1) {
            if (model != null) {
                mlist.clear();
                radioListBeans.clear();
                try {
                    JSONObject jsonObject = new JSONObject((String) model);
                    JSONArray goodsList = jsonObject.getJSONArray("advertisementList");
                    JSONArray goodsListBrand = jsonObject.getJSONArray("radiolist");
                    for (int i = 0; i < goodsList.length(); i++) {
                        JSONObject o1 = (JSONObject) goodsList.get(i);
                        ViewPagerPicBean goodsHotListBean = JSONUtils.GsonToBean(o1.toString(), ViewPagerPicBean.class);
                        mlist.add(goodsHotListBean);
                    }
                    for (int i = 0; i < goodsListBrand.length(); i++) {
                        JSONObject o1 = (JSONObject) goodsListBrand.get(i);
                        RadioListBean goodsListBean = JSONUtils.GsonToBean(o1.toString(), RadioListBean.class);
                        radioListBeans.add(goodsListBean);
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (action == ACTION_DEFAULT + 2) {
            if (model != null) {
                mgoodHotlist.clear();
                mgoodlist.clear();
                try {
                    JSONObject jsonObject = new JSONObject((String) model);
                    JSONArray goodsList = jsonObject.getJSONArray("goodsList");
                    JSONArray goodsListBrand = jsonObject.getJSONArray("goodsListBrand");
                    for (int i = 0; i < goodsList.length(); i++) {
                        JSONObject o1 = (JSONObject) goodsList.get(i);
                        GoodsHotListBean goodsHotListBean = JSONUtils.GsonToBean(o1.toString(), GoodsHotListBean.class);
                        mgoodHotlist.add(goodsHotListBean);
                        JSONObject o11 = (JSONObject) goodsList.get(i);
                    }
                    for (int i = 0; i < goodsListBrand.length(); i++) {
                        JSONObject o1 = (JSONObject) goodsListBrand.get(i);
                        GoodsListBean goodsListBean = JSONUtils.GsonToBean(o1.toString(), GoodsListBean.class);
                        mgoodlist.add(goodsListBean);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void getDataFail(String msg, int action) {

    }


    @Override
    public void OnHomeIconClick(int position) {
        switch (position) {
            case 0:
                Intent intent = new Intent(getActivity(), CategoryLifeActivity.class);
                startActivity(intent);
                break;
            case 1:
                if (TextUtils.isEmpty(UserInfo.getInstance().getId())) {
                    Intent intent4 = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent4);
                    break;
                }
                Intent intent1 = new Intent(getActivity(), CarLifeActivity.class);
                startActivity(intent1);
                break;
            case 2:
                if (TextUtils.isEmpty(UserInfo.getInstance().getId())) {
                    Intent intent3 = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent3);
                } else {
                    Intent intent2 = new Intent(getActivity(), ChargeCenterActivity.class);
                    startActivity(intent2);
                }
                break;
            case 3:
                /*Intent intent3 = new Intent(getActivity(), GoodsDetailsActivity.class);
                startActivity(intent3);*/
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(View v, int position) {
        Intent intent = new Intent(getActivity(), GoodsDetailsActivity.class);
        intent.putExtra("id", mgoodHotlist.get(position).getId());
        intent.putExtra("id1", mgoodlist.get(position).getId());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mvpPresenter.getViewPagerData(ACTION_DEFAULT + 1);
        mvpPresenter.getGoodsList(ACTION_DEFAULT + 2);
        refreshLayout.autoRefresh(1200);
    }
}



