package com.chehubang.duolejie.modules.category.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.model.GoodsChildTypeBean;
import com.chehubang.duolejie.model.GoodsTypeChildBean;
import com.chehubang.duolejie.modules.category.adapter.SubTypeChildAdapter;
import com.chehubang.duolejie.modules.category.fragment.CateoryFragment;
import com.chehubang.duolejie.modules.category.presenter.CategoryPresenter;
import com.chehubang.duolejie.modules.gooddetails.activity.GoodsDetailsActivity;
import com.chehubang.duolejie.utils.log;

import java.util.ArrayList;
import java.util.List;

import common.mvp.activity.MainView;

/**
 * Created by Thinkpad on 2017/12/13.
 */

public class CategoryActivity extends BaseActivity<CategoryPresenter> implements MainView, View.OnClickListener, GridView.OnItemClickListener {

    private TextView back;
    private TextView title;
    private ImageView search;
    private TabLayout tabLayout;
    private ViewPager categoryList;
    private List<GoodsTypeChildBean> childList = new ArrayList<>();
    private List<GoodsChildTypeBean> newchildList = new ArrayList<>();
    private CateoryFragment mCateoryFragment;
    private List<CateoryFragment> mFragments = new ArrayList<>();
    String name;
    ViewPagerAdapter pagerAdapter;
    private SubTypeChildAdapter typeChildAdapter;
    private TabLayout.OnTabSelectedListener listener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            String id = newchildList.get(tab.getPosition()).getId();
            mvpPresenter.getGoodsInfoData(ACTION_DEFAULT + 1, id);
            categoryList.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };
    private int mposition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String titles = intent.getStringExtra("title");
        name = intent.getStringExtra("name");
        String childid = intent.getStringExtra("childid");
        mposition = intent.getIntExtra("position", 0);
        back = findAtyViewById(R.id.tv_category_back);
        tabLayout = findAtyViewById(R.id.tl_tab_item_select);
        tabLayout.addOnTabSelectedListener(listener);
        categoryList = findAtyViewById(R.id.gv_item_content);
        title = (TextView) findViewById(R.id.tv_category_title);
        typeChildAdapter = new SubTypeChildAdapter(this, childList);
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mFragments);
        categoryList.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(categoryList);
        mvpPresenter.getTypeChildList(ACTION_DEFAULT + 3, childid);
        mvpPresenter.getGoodsInfoData(ACTION_DEFAULT + 1, id);
        back.setOnClickListener(this);
        title.setText(titles);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_category_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void getDataSuccess(Object model, int action) {
        if (action == ACTION_DEFAULT + 1) {
            childList.clear();
            List<GoodsTypeChildBean> model1 = (List<GoodsTypeChildBean>) model;
            if (model1.size() > 0) {
                childList.addAll(model1);
                typeChildAdapter.notifyDataSetChanged();
            }
            ((CateoryFragment) pagerAdapter.getItem(categoryList.getCurrentItem())).addAll(model1);
        } else if (action == ACTION_DEFAULT + 3) {
            newchildList.clear();
            List<GoodsChildTypeBean> data = (List<GoodsChildTypeBean>) model;
            if (data.size() > 0) {
                newchildList.addAll(data);
                for (int i = 0; i < data.size(); i++) {
                    String name = data.get(i).getGoods_type_name();
                    tabLayout.addTab(tabLayout.newTab().setText(name));
                    log.d(name);
                    //tabLayout.setScrollPosition(mposition, 0.0F, true);
                    CateoryFragment cateoryFragment = new CateoryFragment();
                    cateoryFragment.setTitle(name);
                    mFragments.add(cateoryFragment);
                    if (i == 0) {
                        mCateoryFragment = cateoryFragment;
                    }
                }
                pagerAdapter.notifyDataSetChanged();
                tabLayout.setupWithViewPager(categoryList);

                log.d(mposition + "---------");
                if (mposition >=0 && mposition<data.size()){
                    categoryList.setCurrentItem(mposition);
                }
            }
        }
    }

    @Override
    public void getDataFail(String msg, int action) {

    }

    @Override
    protected CategoryPresenter createPresenter() {
        return new CategoryPresenter(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (childList.size() > 0) {
            Intent intent = new Intent(this, GoodsDetailsActivity.class);
            intent.putExtra("id", childList.get(position).getId());
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<CateoryFragment> dataList;

        public ViewPagerAdapter(FragmentManager fm, List<CateoryFragment> dataList) {
            super(fm);
            this.dataList = dataList;
        }

        @Override
        public Fragment getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return dataList.get(position).getTitle();
        }
    }
}
