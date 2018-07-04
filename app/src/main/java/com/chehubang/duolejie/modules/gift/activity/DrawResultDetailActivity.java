package com.chehubang.duolejie.modules.gift.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.model.DrawResult;
import com.chehubang.duolejie.modules.gift.presenter.DrawResultDetailPresenter;
import com.chehubang.duolejie.utils.log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import common.mvp.activity.MainView;

/**
 * Created by ZZH on 2018/2/22.
 *
 * @Date: 2018/2/22
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description: item_draw_result_detail
 */
public class DrawResultDetailActivity extends BaseActivity<DrawResultDetailPresenter> implements MainView, OnRefreshListener, OnLoadmoreListener {

    private String coupon_id;
    @BindView(R.id.tv_gift_num)
    TextView tv_gift_num;
    @BindView(R.id.lv_list)
    ListView lv_list;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    UserDrawAdapter mAdapter;

    private String period;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_result_detail);
        ButterKnife.bind(this);
        mAdapter = new UserDrawAdapter(this);
        lv_list.setAdapter(mAdapter);
        coupon_id = getIntent().getStringExtra("coupon_id");
        period = getIntent().getStringExtra("period");
        mvpPresenter.getDrawResultDetail(ACTION_DEFAULT + 1, coupon_id);
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setOnRefreshListener(this);
        tv_gift_num.setText(period + "抽奖参与用户");
        refreshLayout.setOnLoadmoreListener(this);
    }

    @Override
    public void getDataSuccess(Object model, int action) {
        log.d(model.toString());
        try {
            JSONObject obj = new JSONObject(model.toString());
            String list = obj.getString("drawResultList");
            Gson gson = new Gson();
            List<DrawResult> o = gson.fromJson(list, new TypeToken<List<DrawResult>>() {
            }.getType());
            mAdapter.clear();
            mAdapter.addAll(o);
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @OnClick({R.id.iv_back})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }

    }

    @Override
    public void getDataFail(String msg, int action) {
        log.d(msg + ", " + action);
    }

    @Override
    protected DrawResultDetailPresenter createPresenter() {
        return new DrawResultDetailPresenter(this);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mvpPresenter.getDrawResultDetail(ACTION_DEFAULT + 1, coupon_id);
        refreshLayout.autoRefresh(1500);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        refreshLayout.setLoadmoreFinished(true);
    }

    class UserDrawAdapter extends BaseAdapter {
        private List<DrawResult> dataList;
        private Context mContext;

        public UserDrawAdapter(Context context) {
            mContext = context;
            dataList = new ArrayList<>();
        }

        public void clear() {
            dataList.clear();
        }

        public void addAll(List<DrawResult> data) {
            dataList.addAll(data);
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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_draw_result_detail, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            DrawResult result = dataList.get(position);
            holder.address.setText("地址：" + result.getUser_ip_address());
            if ("进行中".equals(result.getCoupon_status())) {
                holder.status.setText("未开奖");
            } else if ("倒计时".equals(result.getCoupon_status())) {
                holder.status.setText("未开奖");
            } else if ("已揭晓".equals(result.getCoupon_status())) {
                if ("y".equals(result.getStatus())) {
                    holder.status.setText(result.getCoupon_num());
                } else {
                    holder.status.setText("未中奖");
                }
            }
            holder.username.setText(result.getNick_name());
            //PictureUtils.loadPictureCircle(mContext, result.getUser_header(), holder.user_header, R.mipmap.ic_share_logo);
            loadCircleImageView(mContext, result.getUser_header(), holder.user_header, R.mipmap.ic_share_logo);
            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.tv_user_name)
            TextView username;
            @BindView(R.id.tv_user_address)
            TextView address;
            @BindView(R.id.tv_status)
            TextView status;
            @BindView(R.id.iv_header)
            ImageView user_header;

            public ViewHolder(View itemView) {
                ButterKnife.bind(this, itemView);
            }
        }
    }
    public static void loadCircleImageView(Context ctx, String url, ImageView view, int errorId) {
        if (TextUtils.isEmpty(url)) {
            view.setImageResource(errorId);
            return;
        }
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(errorId)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new GlideCircleTransform(ctx))
                .error(errorId);
        Glide.with(ctx).load(url).apply(options).into(view);
    }
}
