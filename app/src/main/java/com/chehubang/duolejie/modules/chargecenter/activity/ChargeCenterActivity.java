package com.chehubang.duolejie.modules.chargecenter.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.model.MoneyTypeBean;
import com.chehubang.duolejie.modules.chargecenter.adapter.FlowAdapter;
import com.chehubang.duolejie.modules.chargecenter.adapter.TelAdapter;
import com.chehubang.duolejie.modules.chargecenter.presenter.ChargeCenterPresenter;
import com.chehubang.duolejie.modules.home.activity.WebviewActivity;
import com.chehubang.duolejie.widget.IconGridView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import common.Utils.JSONUtils;
import common.Utils.StringUtils;
import common.Utils.ToastUtils;
import common.mvp.activity.MainView;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/12/14 0014.
 */

public class ChargeCenterActivity extends BaseActivity<ChargeCenterPresenter> implements MainView, View.OnClickListener, GridView.OnItemClickListener {
    ImageView back;
    LinearLayout liubai, yiqianer, liangqiansi, yizhao, liangzhao, sanzhao;
    private static final int SDK_PAY_FLAG = 1;
    private IconGridView tellist, flowlist;
    private List<MoneyTypeBean> mtellist = new ArrayList<>();
    private List<MoneyTypeBean> mflowlist = new ArrayList<>();
    private TelAdapter telAdapter;
    private FlowAdapter flowAdapter;
    private EditText tel;
    private String phoneNumber = null;
    private MoneyTypeBean select = null;
    private RxPermissions mRxPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voucher_center);
        mRxPermissions = new RxPermissions(this);
        back = (ImageView) findViewById(R.id.iv_chargecenter_back);
        TextView commit = (TextView) findViewById(R.id.tv_chargecenter_commit);
        LinearLayout contract = (LinearLayout) findViewById(R.id.ll_charge_center_contract);

        tel = (EditText) findViewById(R.id.tv_charge_tel);
        tellist = (IconGridView) findViewById(R.id.igv_charge_tel);
        flowlist = (IconGridView) findViewById(R.id.igv_charge_flow);
        back.setOnClickListener(this);
        commit.setOnClickListener(this);
        contract.setOnClickListener(this);
        findAtyViewById(R.id.iv_contact).setOnClickListener(this);

        telAdapter = new TelAdapter(this, mtellist);
        tellist.setAdapter(telAdapter);
        tellist.setOnItemClickListener(this);

        flowAdapter = new FlowAdapter(this, mflowlist);
        flowlist.setAdapter(flowAdapter);

        flowlist.setOnItemClickListener(this);

        UserInfo.getInstance().getIdCache(this);
        mvpPresenter.getChargeData(ACTION_DEFAULT + 1, UserInfo.getInstance().getId());
        findViewById(R.id.tv).setOnClickListener(this);
    }

    @Override
    protected ChargeCenterPresenter createPresenter() {
        return new ChargeCenterPresenter(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_chargecenter_back:
                finish();
                break;
            case R.id.tv_chargecenter_commit:
                if (select != null) {
                    phoneNumber = tel.getText().toString();
                    if (StringUtils.isPhone(phoneNumber)) {
                        mvpPresenter.getOrderNumber(ACTION_DEFAULT + 2, UserInfo.getInstance().getId(), select.getValue(), select.getChargeType(), phoneNumber);
                    } else {
                        ToastUtils.centerToastWhite(this, "请输入正确的手机号");
                    }
                } else {
                    ToastUtils.centerToastWhite(this, "请选择套餐");
                }
                break;
            case R.id.ll_charge_center_contract:
                Intent intent = new Intent(this, WebviewActivity.class);
                intent.putExtra("url", "http://39.107.14.118/appInterface/aboutUsContentInfo.jhtml?id=12");  //外网版本
                // intent7.putExtra("url","http://192.168.3.25:8090/GetTreasureAppJinDong/appInterface/aboutUsContentInfo.jhtml?id=1&is_show_message=y");
                intent.putExtra("title", "充值协议");
                startActivity(intent);
                break;
            case R.id.tv:
                Intent intent2 = new Intent(this, WebviewActivity.class);
                intent2.putExtra("url", "http://39.107.14.118/appInterface/commonProblemInfo.jhtml?problem_type=1");  //外网版本
                // intent7.putExtra("url","http://192.168.3.25:8090/GetTreasureAppJinDong/appInterface/aboutUsContentInfo.jhtml?id=1&is_show_message=y");
                intent2.putExtra("title", "常见问题");
                startActivity(intent2);
                break;
            case R.id.iv_contact:
                if (!mRxPermissions.isGranted(Manifest.permission.READ_CONTACTS)) {
                    mRxPermissions.request(Manifest.permission.READ_CONTACTS).subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean flag) throws Exception {
                            if (flag) {
                                jumpContact();
                            } else {
                                ToastUtils.centerToastWhite(ChargeCenterActivity.this, "请在权限设置里面授予本应用读写联系人权限");
                            }
                        }
                    });
                } else {
                    jumpContact();
                }
                break;
            default:
                break;
        }
    }

    private void jumpContact() {
        Intent contact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(contact, 0);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Adapter adapter = adapterView.getAdapter();
        if (adapter instanceof TelAdapter) {
            telAdapter.setSelectedPosition(position);
            telAdapter.notifyDataSetChanged();
            flowAdapter.setSelectedPosition(-1);
            flowAdapter.notifyDataSetChanged();
            select = mtellist.get(position);
        } else if (adapter instanceof FlowAdapter) {
            telAdapter.setSelectedPosition(-1);
            telAdapter.notifyDataSetChanged();
            flowAdapter.setSelectedPosition(position);
            flowAdapter.notifyDataSetChanged();
            select = mflowlist.get(position);
        }
    }

    @Override
    public void getDataSuccess(Object model, int action) {
        if (action == ACTION_DEFAULT + 1) {
            String data = (String) model;
            if (data != null) {
                mtellist.clear();
                mflowlist.clear();
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray getRechargeBillList = jsonObject.getJSONArray("getRechargeBillList");
                    JSONArray getUserList = jsonObject.getJSONArray("getUserList");
                    for (int i = 0; i < getRechargeBillList.length(); i++) {
                        JSONObject data1 = (JSONObject) getRechargeBillList.get(i);
                        MoneyTypeBean moneyTypeBean = JSONUtils.GsonToBean(data1.toString(), MoneyTypeBean.class);
                        String type = moneyTypeBean.getType();
                        if (type.startsWith("bill")) {
                            mtellist.add(moneyTypeBean);
                        } else if (type.startsWith("flow")) {
                            mflowlist.add(moneyTypeBean);
                        }
                    }
                    telAdapter.notifyDataSetChanged();
                    flowAdapter.notifyDataSetChanged();
                    JSONObject user = (JSONObject) getUserList.get(0);
                    phoneNumber = user.getString("user_tel");
                    tel.setText(phoneNumber);
                   /* if (mtellist.size()>0){
                        select = mtellist.get(0);
                    }*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (action == ACTION_DEFAULT + 2) {
            if (model != null) {
                String orderid = (String) model;
                Intent intent = new Intent(this, CashierCenterActivity.class);
                intent.putExtra("data", select);
                intent.putExtra("id", orderid);
                startActivity(intent);
            }
        }
    }

    @Override
    public void getDataFail(String msg, int action) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 0) {
                Uri uri = data.getData();
                String[] contacts = getPhoneContacts(uri);
                Log.d("--", "姓名:" + contacts[0] + " " + "手机号:" + contacts[1]);
                phoneNumber = contacts[1];
                tel.setText(phoneNumber);

            }
        }
    }

    private String[] getPhoneContacts(Uri uri) {
        String[] contact = new String[2];
        //得到ContentResolver对象
        ContentResolver cr = getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            //取得联系人姓名
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            contact[0] = cursor.getString(nameFieldColumnIndex);
            //取得电话号码
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
            if (phone != null) {
                phone.moveToFirst();
                contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            phone.close();
            cursor.close();
        } else {
            return null;
        }
        return contact;
    }
}
