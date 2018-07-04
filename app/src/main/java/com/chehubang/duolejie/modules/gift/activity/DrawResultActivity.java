package com.chehubang.duolejie.modules.gift.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.model.DrawResult;
import com.chehubang.duolejie.modules.gift.adapter.DrawResultAdapter;
import com.chehubang.duolejie.modules.gift.presenter.DrawResultPresenter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import common.mvp.activity.MainView;

/**
 * Created by user on 2018/1/31.
 *
 * @date: 2018/1/31
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @description: 抽奖记录
 */
public class DrawResultActivity extends BaseActivity<DrawResultPresenter> implements MainView {

    @BindView(R.id.lv_list)
    ListView mListView;
    DrawResultAdapter mResultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_result);
        ButterKnife.bind(this);
        mResultAdapter = new DrawResultAdapter(this);
        mListView.setAdapter(mResultAdapter);
        mvpPresenter.getDrawResultList(1, 1);
    }

    @Override
    public void getDataSuccess(Object model, int action) {
        if (action == 1) {
            try {
                JSONObject obj = new JSONObject(model.toString());
                String list = obj.getString("drawResultList");
                Gson gson = new Gson();
                List<DrawResult> o = gson.fromJson(list, new TypeToken<List<DrawResult>>() {
                }.getType());
                mResultAdapter.addAll(o);
                mResultAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void getDataFail(String msg, int action) {

    }


    @Override
    protected DrawResultPresenter createPresenter() {
        return new DrawResultPresenter(this);
    }

    @OnClick({R.id.tv_category_back})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.tv_category_back:
                finish();
                break;
        }
    }
}
