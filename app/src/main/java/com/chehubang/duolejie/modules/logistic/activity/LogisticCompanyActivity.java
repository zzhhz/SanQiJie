package com.chehubang.duolejie.modules.logistic.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.model.Logistic;
import com.chehubang.duolejie.modules.logistic.presenter.LogisticPresenter;
import com.chehubang.duolejie.utils.log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
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
 * @description: 物流公司
 */
public class LogisticCompanyActivity extends BaseActivity<LogisticPresenter> implements MainView, AdapterView.OnItemClickListener {
    @BindView(R.id.lv_list)
    ListView lv_list;

    LogisticAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistic);
        ButterKnife.bind(this);
        mvpPresenter.getLogisticList();
    }

    @Override
    public void getDataSuccess(Object model, int action) {
        log.d(model.toString());
        try {
            JSONObject obj = new JSONObject(model.toString());
            String list = obj.getString("logisticsList");
            Gson gson = new Gson();
            List<Logistic> beans = gson.fromJson(list, new TypeToken<List<Logistic>>() {
            }.getType());
            mAdapter = new LogisticAdapter(this, beans);
            lv_list.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            lv_list.setOnItemClickListener(this);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getDataFail(String msg, int action) {

    }

    @OnClick({R.id.iv_edit_address_back})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.iv_edit_address_back:
                finish();
                break;
        }

    }

    @Override
    protected LogisticPresenter createPresenter() {
        return new LogisticPresenter(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Logistic item = mAdapter.getItem(position);
        Intent intent = new Intent();
        intent.putExtra("data", item);
        setResult(RESULT_OK, intent);
        finish();
    }


    class LogisticAdapter extends ArrayAdapter<Logistic> {

        public LogisticAdapter(@NonNull Context context, @NonNull List<Logistic> objects) {
            super(context, -1, -1, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(LogisticCompanyActivity.this).inflate(android.R.layout.simple_list_item_1, parent, false);
                holder = new ViewHolder();
                holder.text = (TextView) convertView.findViewById(android.R.id.text1);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.text.setText(getItem(position).toString());
            return convertView;
        }

        class ViewHolder {
            TextView text;
        }
    }
}
