package com.chehubang.duolejie.modules.address.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.listener.OnItemCheckedListener;
import com.chehubang.duolejie.listener.OnItemClickListener;
import com.chehubang.duolejie.model.AddressListBean;
import com.chehubang.duolejie.model.AddressListMangerBean;
import com.chehubang.duolejie.model.AreaBean;
import com.chehubang.duolejie.modules.address.adapter.AddressManageAdapter;
import com.chehubang.duolejie.modules.address.presenter.AddressManagePresenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import common.Log.LogUtils;
import common.Utils.JSONUtils;
import common.Utils.ToastUtils;
import common.http.RequestResult;
import common.mvp.activity.MainView;

/**
 * Created by Thinkpad on 2017/12/28.
 */

public class AddressManageActivity extends BaseActivity<AddressManagePresenter> implements MainView, View.OnClickListener, OnItemClickListener, OnItemCheckedListener, ListView.OnItemClickListener {

    private ListView addressList;
    private List<AddressListMangerBean> arealist = new ArrayList();
    private AddressManageAdapter addressManageAdapter;
    private boolean isUpdata = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_manage);
        ImageView back = (ImageView) findViewById(R.id.iv_address_manage_back);
        addressList = (ListView) findViewById(R.id.lv_address_manage_list);
        TextView addAddress = (TextView) findViewById(R.id.tv_address_manage_add);
        back.setOnClickListener(this);
        addAddress.setOnClickListener(this);
        UserInfo.getInstance().getIdCache(this);
        mvpPresenter.getAddressList(ACTION_DEFAULT + 1, UserInfo.getInstance().getId());
        addressManageAdapter = new AddressManageAdapter(this, arealist, this, this);
        addressList.setAdapter(addressManageAdapter);
        addressList.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_address_manage_back:
                finish();
                break;
            case R.id.tv_address_manage_add:
                Intent intent = new Intent(this, AddAddressActivity.class);
                startActivityForResult(intent, 5001);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 5001) {
            if (resultCode == 5002) {
                mvpPresenter.getAddressList(ACTION_DEFAULT + 1, UserInfo.getInstance().getId());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void getDataSuccess(Object model, int action) {
        if (action == ACTION_DEFAULT + 1) {
            if (model != null) {
                try {
                    arealist.clear();
                    JSONObject data = new JSONObject((String) model);
                    JSONArray addressList = data.getJSONArray("addressList");
                    ArrayList<AddressListMangerBean> areaBeans = JSONUtils.GsonjsonToArrayList(addressList.toString(), AddressListMangerBean.class);
                    arealist.addAll(areaBeans);
                    addressManageAdapter.notifyDataSetChanged();
                    if (isUpdata) {
                        ToastUtils.centerToastWhite(this, "删除地址成功");
                        isUpdata = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (action == ACTION_DEFAULT + 2) {
            if (model != null) {
                RequestResult data = (RequestResult) model;
                String status = data.getStatus();
                if (TextUtils.equals(Constant.request_success, status)) {
                    mvpPresenter.getAddressList(ACTION_DEFAULT + 1, UserInfo.getInstance().getId());
                    isUpdata = true;
                }
            }
        }else if (action ==ACTION_DEFAULT+3){
            if (model != null) {
                RequestResult data = (RequestResult) model;
                String status = data.getStatus();
                if (TextUtils.equals(Constant.request_success, status)) {
                    ToastUtils.centerToastWhite(this,"设置默认地址成功");
                }
            }
        }
    }

    @Override
    public void getDataFail(String msg, int action) {

    }

    @Override
    protected AddressManagePresenter createPresenter() {
        return new AddressManagePresenter(this);
    }

    @Override
    public void onItemClick(View v, int position) {
        mvpPresenter.deleteAddressItem(ACTION_DEFAULT + 2, arealist.get(position).getId());
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public void onItemChecked(int position, boolean checked) {
        if (arealist.size() > 0) {
            if (checked) {
                String id = arealist.get(position).getId();
                mvpPresenter.setDefaultItem(ACTION_DEFAULT + 3, UserInfo.getInstance().getId(), id);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (arealist.size() > 0) {
            Intent intent = new Intent();
            intent.putExtra("data",arealist.get(position));
            setResult(7002,intent);
            finish();
        }
    }
}
