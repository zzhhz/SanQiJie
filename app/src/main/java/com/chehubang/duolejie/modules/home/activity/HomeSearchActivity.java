package com.chehubang.duolejie.modules.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.model.GoodsTypeChildBean;
import com.chehubang.duolejie.model.HotKey;
import com.chehubang.duolejie.modules.category.adapter.SubTypeChildAdapter;
import com.chehubang.duolejie.modules.gooddetails.activity.GoodsDetailsActivity;
import com.chehubang.duolejie.modules.home.presenter.HomeSearchPresenter;
import com.chehubang.duolejie.widget.FluidLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import common.Utils.JSONUtils;
import common.mvp.activity.MainView;

/**
 * @Date: 2018/1/29 19:41
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description: 搜索页面
 */
public class HomeSearchActivity extends BaseActivity<HomeSearchPresenter> implements MainView, View.OnClickListener {
    private List<GoodsTypeChildBean> childList = new ArrayList<>();
    private SubTypeChildAdapter typeChildAdapter;
    private String searchStr;
    private EditText ed_home_search;
    LinearLayout ll_hot;
    FluidLayout flow_layout;
    List<String> hotList = new ArrayList<>();
    LinearLayout ll_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_search);
        ImageView back = (ImageView) findViewById(R.id.iv_back);
        ll_hot = (LinearLayout) findViewById(R.id.ll_hot);
        flow_layout = (FluidLayout) findViewById(R.id.flow_layout);
        ll_history = (LinearLayout) findViewById(R.id.ll_history);
        TextView search = (TextView) findViewById(R.id.tv_search_go);
        ed_home_search = (EditText) findViewById(R.id.et_home_search);
        GridView searchList = (GridView) findViewById(R.id.gv_item_content);
        typeChildAdapter = new SubTypeChildAdapter(this, childList);
        searchList.setAdapter(typeChildAdapter);
        search.setOnClickListener(this);
        back.setOnClickListener(this);
        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GoodsTypeChildBean bean = childList.get(position);
                Intent intent = new Intent(HomeSearchActivity.this, GoodsDetailsActivity.class);
                intent.putExtra("id", bean.getId());
                startActivity(intent);
            }
        });
        mvpPresenter.getHotKeyWord(ACTION_DEFAULT + 2);
        ed_home_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    ll_hot.setVisibility(View.VISIBLE);
                    bindHistory(hotList);
                }
            }
        });
        String hot_key = UserInfo.getString("hot_key", "");
        List<String> listKey = new Gson().fromJson(hot_key, new TypeToken<List<String>>() {
        }.getType());
        if (listKey != null) {
            hotList.addAll(listKey);
        }
        bindHistory(hotList);
    }

    private void bindHistory(List<String> listKey) {
        if (listKey != null && !listKey.isEmpty()) {
            ll_history.removeAllViews();
            LayoutInflater from = LayoutInflater.from(this);
            for (final String str : listKey) {
                final View inflate = from.inflate(R.layout.item_history_key, null);
                TextView msg = (TextView) inflate.findViewById(R.id.tv);
                msg.setText(str);
                View v = inflate.findViewById(R.id.iv_delete);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ll_history.removeView(inflate);
                        hotList.remove(str);
                    }
                });
                msg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView view = (TextView) v;
                        searchStr = view.getText().toString().trim();
                        ed_home_search.setText(searchStr);
                        mvpPresenter.getGoodsSearchList(ACTION_DEFAULT + 1, searchStr);
                    }
                });
                ll_history.addView(inflate);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveHistoryList(hotList);
    }

    private void saveHistoryList(List<String> listKey) {
        if (listKey != null && !listKey.isEmpty()) {
            Gson gson = new Gson();
            UserInfo.putString("hot_key", gson.toJson(listKey));
        } else {
            UserInfo.putString("hot_key", "");
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_search_go) {
            ll_hot.setVisibility(View.GONE);
            searchStr = ed_home_search.getText().toString().trim();
            mvpPresenter.getGoodsSearchList(ACTION_DEFAULT + 1, searchStr);
            if (!hotList.contains(searchStr)) {
                hotList.add(searchStr);
            }
        } else if (id == R.id.iv_back) {
            finish();
        }
    }

    @Override
    public void getDataSuccess(Object model, int action) {
        if (model != null) {
            if (action == ACTION_DEFAULT + 1) {
                ll_hot.setVisibility(View.GONE);
                childList.clear();
                List<GoodsTypeChildBean> model1 = (List<GoodsTypeChildBean>) model;
                if (model1.size() > 0) {
                    childList.addAll(model1);
                    typeChildAdapter.notifyDataSetChanged();
                }
            } else if (action == ACTION_DEFAULT + 2) {
                if (model != null) {
                    HotKey hotKey = JSONUtils.GsonToBean(model.toString(), HotKey.class);
                    if (hotKey != null && hotKey.getHotSearchList() != null && !hotKey.getHotSearchList().isEmpty()) {
                        ll_hot.setVisibility(View.VISIBLE);
                        List<HotKey.HotSearchListBean> list = hotKey.getHotSearchList();
                        LayoutInflater from = LayoutInflater.from(this);
                        for (HotKey.HotSearchListBean bean : list) {
                            FrameLayout view = (FrameLayout) from.inflate(R.layout.item_hot_key, null);
                            final TextView textView = (TextView) view.findViewById(R.id.tv);
                            textView.setText(bean.getSearch_key());

                            textView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    searchStr = textView.getText().toString();
                                    ed_home_search.setText(searchStr);
                                    mvpPresenter.getGoodsSearchList(ACTION_DEFAULT + 1, searchStr);
                                }
                            });
                            flow_layout.addView(view);
                        }

                    } else {
                        ll_hot.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    @Override
    public void getDataFail(String msg, int action) {

    }

    @Override
    protected HomeSearchPresenter createPresenter() {
        return new HomeSearchPresenter(this);
    }
}
