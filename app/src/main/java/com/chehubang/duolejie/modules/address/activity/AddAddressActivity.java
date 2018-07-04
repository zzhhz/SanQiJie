package com.chehubang.duolejie.modules.address.activity;

import android.content.Intent;
import android.location.LocationListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.model.AddressListBean;
import com.chehubang.duolejie.model.AreaBean;
import com.chehubang.duolejie.modules.address.dialog.LocationDialog;
import com.chehubang.duolejie.modules.address.presenter.AddAddressPresenter;
import com.chehubang.duolejie.utils.DialogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import common.Utils.JSONUtils;
import common.Utils.ToastUtils;
import common.http.RequestResult;
import common.mvp.activity.MainView;

/**
 * Created by Thinkpad on 2017/12/28.
 */

public class AddAddressActivity extends BaseActivity<AddAddressPresenter> implements MainView, View.OnClickListener, LocationDialog.DataSelectedListener {

    private List<AreaBean> areaList = new ArrayList();
    private LocationDialog locationDialog;
    private EditText mtel;
    private EditText mlocationDetails;
    private TextView mlocation;
    private CheckBox isDefault;
    private EditText mname;
    private String privinceid;
    private String cityid;
    private String is_default;
    private String id = null;
    TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_address);
        ImageView back = (ImageView) findViewById(R.id.iv_new_address_back);
        TextView save = (TextView) findViewById(R.id.tv_new_address_save);
        LinearLayout areaSelect = (LinearLayout) findViewById(R.id.ll_new_address_area);
        mname = (EditText) findViewById(R.id.et_add_address_name);
        mtel = (EditText) findViewById(R.id.et_add_address_tel);
        mlocationDetails = (EditText) findViewById(R.id.tv_add_address_location_details);
        mlocation = (TextView) findViewById(R.id.tv_add_address_location);
        isDefault = (CheckBox) findViewById(R.id.cb_add_address_default);
        tv_title = (TextView) findViewById(R.id.tv_title);
        back.setOnClickListener(this);
        save.setOnClickListener(this);
        areaSelect.setOnClickListener(this);
        UserInfo.getInstance().getIdCache(this);
        Intent intent = getIntent();
        AddressListBean item = (AddressListBean) intent.getSerializableExtra("item");
        if (item != null) {
            tv_title.setText("编辑地址");
            mname.setText(item.getUser_name());
            mtel.setText(item.getUser_tel());
            id = item.getId();
            mlocation.setText(item.getProvince_addr() + "--" + item.getCity_addr());
            mlocationDetails.setText(item.getDetail_address());
            String is_defaulta = item.getIs_default();
            if (TextUtils.equals(is_defaulta, "n")) {
                isDefault.setChecked(false);
            } else if (TextUtils.equals(is_defaulta, "y")) {
                isDefault.setChecked(true);
            }
            privinceid = item.getProvince_id();
            cityid = item.getCity_id();
        }
        mvpPresenter.getLocationData(ACTION_DEFAULT + 1);
    }

    @Override
    public void dataSelected(String location1, String location2, String id1, String id2) {
        privinceid = id1;
        cityid = id2;
        mlocation.setText(location1 + "--" + location2);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_new_address_back:
                finish();
                break;
            case R.id.tv_new_address_save:
                String name = mname.getText().toString();
                String tel = mtel.getText().toString();
                String locationdetails = mlocationDetails.getText().toString();
                String location = mlocation.getText().toString();
                boolean checked = isDefault.isChecked();
                if (TextUtils.isEmpty(name)) {
                    ToastUtils.centerToastWhite(this, "请输入收货人");
                } else if (TextUtils.isEmpty(tel)) {
                    ToastUtils.centerToastWhite(this, "请输入联系电话");
                } else if (TextUtils.equals(location, "请选择")) {
                    ToastUtils.centerToastWhite(this, "请选择地址");
                } else if (TextUtils.isEmpty(locationdetails)) {
                    ToastUtils.centerToastWhite(this, "请填写详细地址");
                } else {
                    if (checked) {
                        is_default = "y";
                    } else {
                        is_default = "n";
                    }
                    if (TextUtils.isEmpty(id)) {
                        mvpPresenter.saveLocationInfo(ACTION_DEFAULT + 2, UserInfo.getInstance().getId(), "", tel, name, privinceid, cityid, locationdetails, is_default);
                    } else {
                        mvpPresenter.saveLocationInfo(ACTION_DEFAULT + 2, UserInfo.getInstance().getId(), id, tel, name, privinceid, cityid, locationdetails, is_default);
                    }
                }
                break;
            case R.id.ll_new_address_area:
                if (areaList.size() > 0) {
                    locationDialog = new LocationDialog(this, areaList);
                    DialogUtils.setDialogStyle(locationDialog);
                    locationDialog.setOnDataSelectedListener(this);
                    locationDialog.show();
                }
                break;
        }
    }

    @Override
    public void getDataSuccess(Object model, int action) {
        if (action == ACTION_DEFAULT + 1) {
            if (model != null) {
                try {
                    JSONObject jsonObject = new JSONObject((String) model);
                    JSONArray provinceList = jsonObject.getJSONArray("provinceList");
                    ArrayList<AreaBean> areaBeans = JSONUtils.GsonjsonToArrayList(provinceList.toString(), AreaBean.class);
                    areaList.addAll(areaBeans);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        if (action == ACTION_DEFAULT + 2) {
            if (model != null) {
                RequestResult data = (RequestResult) model;
                String status = data.getStatus();
                if (TextUtils.equals(Constant.request_success, status)) {
                    if (TextUtils.isEmpty(id)) {
                        ToastUtils.centerToastWhite(this, "地址添加成功");
                        setResult(5002);
                        finish();
                    } else {
                        ToastUtils.centerToastWhite(this, "地址修改成功");
                        setResult(5002);
                        finish();
                    }
                }
            }
        }
    }

    @Override
    public void getDataFail(String msg, int action) {

    }

    @Override
    protected AddAddressPresenter createPresenter() {
        return new AddAddressPresenter(this);
    }

}
