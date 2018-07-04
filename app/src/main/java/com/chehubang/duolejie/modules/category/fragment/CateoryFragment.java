package com.chehubang.duolejie.modules.category.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseFragment;
import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.model.GoodsTypeChildBean;
import com.chehubang.duolejie.modules.category.adapter.SubTypeChildAdapter;
import com.chehubang.duolejie.modules.gooddetails.activity.GoodsDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 2018/2/7.
 *
 * @date: 2018/2/7
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @description:
 */
public class CateoryFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    @BindView(R.id.gv_item_content)
    public GridView categoryList;
    public SubTypeChildAdapter typeChildAdapter;
    private List<GoodsTypeChildBean> childList = new ArrayList<>();
    private String mTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_category, container, false);
        ButterKnife.bind(this, view);
        typeChildAdapter = new SubTypeChildAdapter(getActivity(), childList);
        categoryList.setAdapter(typeChildAdapter);
        categoryList.setOnItemClickListener(this);
        return view;
    }

    public void addAll(List<GoodsTypeChildBean> childList) {
        this.childList.clear();
        this.childList.addAll(childList);
        typeChildAdapter.notifyDataSetChanged();
    }

    @Override
    protected MvpPresenter createPresenter() {
        return null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this.getActivity(), GoodsDetailsActivity.class);
        intent.putExtra("id", childList.get(position).getId());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }
}
