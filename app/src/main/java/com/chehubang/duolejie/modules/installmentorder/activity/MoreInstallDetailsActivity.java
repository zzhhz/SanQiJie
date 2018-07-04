package com.chehubang.duolejie.modules.installmentorder.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.model.MoreInstall;
import com.chehubang.duolejie.model.WrapperMoreInstall;
import com.chehubang.duolejie.modules.installmentorder.presenter.MorePresenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import common.Utils.JSONUtils;
import common.mvp.activity.MainView;

/**
 * Created by ZZH on 2018/3/6.
 *
 * @Date: 2018/3/6
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description:
 */
public class MoreInstallDetailsActivity extends BaseActivity<MorePresenter> implements MainView {

    @BindView(R.id.lv_list)
    ListView mListView;
    private MoreAdapter mAdapter;

    public static void open(Context ctx, String orderId, int type) {
        Intent intent = new Intent(ctx, MoreInstallDetailsActivity.class);
        intent.putExtra("orderId", orderId);
        intent.putExtra("type", type);
        ctx.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_install);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String orderId = intent.getStringExtra("orderId");
        int type = intent.getIntExtra("type", -1);
        mAdapter = new MoreAdapter(this, type);
        mListView.setAdapter(mAdapter);
        mvpPresenter.getOrderListInstall(ACTION_DEFAULT + 1, orderId);
    }

    @Override
    protected MorePresenter createPresenter() {
        return new MorePresenter(this);
    }

    @OnClick({R.id.iv_order_back})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.iv_order_back:
                finish();
                break;
        }
    }

    @Override
    public void getDataSuccess(Object model, int action) {

        String json = model.toString();
        if (!TextUtils.isEmpty(json)) {

            WrapperMoreInstall install = JSONUtils.GsonToBean(json, WrapperMoreInstall.class);
            List<MoreInstall> list = install.getRechargeStagesList();
            mAdapter.addAll(list);
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void getDataFail(String msg, int action) {

    }

    class MoreAdapter extends BaseAdapter {
        private List<MoreInstall> dataList;
        private Context ctx;
        private SimpleDateFormat mFormat;
        private int type;

        public MoreAdapter(Context ctx) {
            this.ctx = ctx;
            dataList = new ArrayList<>();
            mFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        }

        public MoreAdapter(MoreInstallDetailsActivity ctx, int type) {
            this.ctx = ctx;
            this.type = type;
            dataList = new ArrayList<>();
            mFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        }

        public void addAll(List<MoreInstall> list) {
            if (list != null && !list.isEmpty()) {
                dataList.addAll(list);
            }
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(ctx).inflate(R.layout.item_more_install, null);
                holder = new ViewHolder();
                holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
                holder.tvMoney = (TextView) convertView.findViewById(R.id.tv_money);
                holder.tvStatus = (TextView) convertView.findViewById(R.id.tv_status);
                holder.tvMonth = (TextView) convertView.findViewById(R.id.tv_month);
                holder.tv_id = (TextView) convertView.findViewById(R.id.tv_id);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (type == 1) {
                holder.tv_id.setText("充值卡分期");
            } else {
                holder.tv_id.setText("加油卡分期");
            }

            MoreInstall moreInstall = dataList.get(position);
            holder.tvDate.setText(mFormat.format(new Date(moreInstall.getCreate_time())));
            holder.tvMoney.setText("+" + moreInstall.getStages_price());
            holder.tvStatus.setText(moreInstall.getStages_status());
            holder.tvMonth.setText(moreInstall.getMonth() + " 月");
            return convertView;
        }

        class ViewHolder {
            TextView tvDate;
            TextView tvMoney;
            TextView tvStatus;
            TextView tvMonth;
            TextView tv_id;
        }
    }
}
