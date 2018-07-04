package com.chehubang.duolejie.modules.express.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.model.Express;
import com.chehubang.duolejie.modules.express.adapter.ExpressAdapter;
import com.chehubang.duolejie.modules.express.presenter.ExpressPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import common.Utils.JSONUtils;
import common.mvp.activity.MainView;

/**
 * @Date: 2018/2/1 20:23
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description: 物流信息
 */
public class ExpressActivity extends BaseActivity<ExpressPresenter> implements MainView {

    public static final String KEY_EXPRESS_ID = "KEY_EXPRESS_ID";

    @BindView(R.id.tv_no)
    TextView no;
    @BindView(R.id.tv_status)
    TextView status;
    @BindView(R.id.list_item)
    ListView mListView;

    ExpressAdapter mAdapter;

    /**
     *
     * @param ctx
     * @param expressId 查询自雷
     */
    public static void open(Context ctx, String expressId) {
        Intent intent = new Intent(ctx, ExpressActivity.class);
        intent.putExtra(KEY_EXPRESS_ID, expressId);
        ctx.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express);
        ButterKnife.bind(this);
        mAdapter = new ExpressAdapter(this);
        mListView.setAdapter(mAdapter);
        Intent intent = getIntent();
        String id = intent.getStringExtra(KEY_EXPRESS_ID);
        showDialog();
        mvpPresenter.getExpressMessage(ACTION_DEFAULT, id);
    }

    @Override
    protected ExpressPresenter createPresenter() {
        return new ExpressPresenter(this);
    }

    @Override
    public void getDataSuccess(Object model, int action) {

        Wrapper wrapper = JSONUtils.GsonToBean(model.toString(), Wrapper.class);
        mAdapter.addAll(wrapper.getTraces());
        mAdapter.notifyDataSetChanged();
        no.setText(wrapper.getShipperCode() + "  " + wrapper.getLogisticCode());
        if ("3".equals(wrapper.getState())) {
            status.setText("已签收");
        } else {
            status.setText("宝贝正在运送中");
        }
        dismissDialog();
    }

    @Override
    public void getDataFail(String msg, int action) {
        dismissDialog();
    }

    @OnClick({R.id.iv_back})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }

    }

    class Wrapper {
        private String LogisticCode;
        private String ShipperCode;
        private List<Express> Traces;
        private String State;

        public String getState() {
            return State;
        }

        public void setState(String state) {
            State = state;
        }

        public String getLogisticCode() {
            return LogisticCode;
        }

        public void setLogisticCode(String logisticCode) {
            LogisticCode = logisticCode;
        }

        public String getShipperCode() {
            return ShipperCode;
        }

        public void setShipperCode(String shipperCode) {
            ShipperCode = shipperCode;
        }

        public List<Express> getTraces() {
            return Traces;
        }

        public void setTraces(List<Express> traces) {
            Traces = traces;
        }
    }
}
