package com.chehubang.duolejie.modules.category.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.model.BannerBean;
import com.chehubang.duolejie.model.GoodsChildTypeBean;
import com.chehubang.duolejie.model.GoodsTypeBean;
import com.chehubang.duolejie.modules.category.adapter.TypeAdapter;
import com.chehubang.duolejie.modules.category.adapter.TypeChildAdapter;
import com.chehubang.duolejie.modules.category.presenter.CategoryLifePresenter;
import com.chehubang.duolejie.widget.IconGridView;
import com.chehubang.duolejie.widget.RecyclerViewBanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import common.Utils.JSONUtils;
import common.http.RequestResult;
import common.mvp.activity.MainView;
import common.picture.PictureUtils;

/**
 * Created by Thinkpad on 2017/12/13.
 */

public class CategoryLifeActivity extends BaseActivity<CategoryLifePresenter> implements MainView, View.OnClickListener, ListView.OnItemClickListener {

    private ImageView search;
    private ListView typeList;
    private IconGridView contentList;
    private List<GoodsTypeBean> mtypeList = new ArrayList<>();
    private List<GoodsChildTypeBean> childList = new ArrayList<>();
    private List<BannerBean> bannerList = new ArrayList<>();

    private TypeAdapter typeAdapter;
    private TypeChildAdapter typeChildAdapter;
    private int mposition = 0;
    private RecyclerViewBanner contentPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_life);
        ImageView back = (ImageView) findViewById(R.id.iv_back);
        typeList = findAtyViewById(R.id.lv_item_type);
        contentPic = findAtyViewById(R.id.iv_category_life_pic);
        contentList = findAtyViewById(R.id.lv_item_content);
        typeAdapter = new TypeAdapter(this, mtypeList);
        typeList.setAdapter(typeAdapter);
        typeChildAdapter = new TypeChildAdapter(this, childList);
        contentList.setAdapter(typeChildAdapter);
        back.setOnClickListener(this);
        typeList.setOnItemClickListener(this);
        contentList.setOnItemClickListener(this);
        mvpPresenter.getTypeList(ACTION_DEFAULT + 1, "0");   //请求第一列的列
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void getDataSuccess(Object model, int action) {
        if (action == ACTION_DEFAULT + 1) {
            mtypeList.clear();
            ArrayList<GoodsTypeBean> model1 = (ArrayList<GoodsTypeBean>) model;
            if (model1.size() > 0) {
                mtypeList.addAll(model1);
                typeAdapter.setSelectedPosition(0);
                typeAdapter.notifyDataSetChanged();
                mvpPresenter.getTypeChildList(ACTION_DEFAULT + 2, mtypeList.get(0).getId());
            }
        } else if (action == ACTION_DEFAULT + 2) {
            if (model != null) {
                childList.clear();
                RequestResult result = (RequestResult) model;
                try {
                    JSONObject jsonObject = new JSONObject(result.getData());
                    JSONArray goodsList = jsonObject.getJSONArray("goodsTypeChilderList");
                    JSONArray advertisementList = jsonObject.getJSONArray("advertisementList");
                    ArrayList<GoodsChildTypeBean> list = JSONUtils.GsonjsonToArrayList(goodsList.toString(), GoodsChildTypeBean.class);
                    final ArrayList<BannerBean> bannerBeans = JSONUtils.GsonjsonToArrayList(advertisementList.toString(), BannerBean.class);
                    if (list.size() > 0) {
                        childList.addAll(list);
                        typeChildAdapter.notifyDataSetChanged();
                    }
                    if (bannerBeans.size() > 0) {
                        contentPic.setVisibility(View.VISIBLE);
                        contentPic.isShowIndicator(true);
                        contentPic.setRvBannerData(bannerBeans);
                        contentPic.setOnSwitchRvBannerListener(new RecyclerViewBanner.OnSwitchRvBannerListener() {
                            @Override
                            public void switchBanner(int position, AppCompatImageView bannerView) {
                                PictureUtils.loadPicture(CategoryLifeActivity.this, bannerBeans.get(position % bannerBeans.size()).getHeader(), bannerView, R.drawable.pic_cycjjl);
                            }
                        });
                    } else {
                        contentPic.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void getDataFail(String msg, int action) {

    }

    @Override
    protected CategoryLifePresenter createPresenter() {
        return new CategoryLifePresenter(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Adapter adapter = adapterView.getAdapter();
        if (adapter instanceof TypeAdapter) {
            typeAdapter.setSelectedPosition(position);
            typeAdapter.notifyDataSetChanged();
            mposition = position;
            if (mtypeList.size() > 0) {
                String id = mtypeList.get(position).getId();
                mvpPresenter.getTypeChildList(ACTION_DEFAULT + 2, id);//第一列点击一次用它的id
            }
        } else if (adapter instanceof TypeChildAdapter) {
            Intent intent = new Intent(this, CategoryActivity.class);   //点击第二条的条目进入第三条
            // intent.putExtra("list",(Serializable) childList);
            intent.putExtra("position", position);
            intent.putExtra("childid", mtypeList.get(mposition).getId());
            intent.putExtra("id", childList.get(position).getId());
            intent.putExtra("title", mtypeList.get(mposition).getGoods_type_name());
            for (int i = 0; i < childList.size(); i++) {
                intent.putExtra("name", childList.get(i).getGoods_type_name());
            }
            startActivity(intent);
        }
    }
}
