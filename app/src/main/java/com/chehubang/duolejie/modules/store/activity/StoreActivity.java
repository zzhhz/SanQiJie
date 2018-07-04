package com.chehubang.duolejie.modules.store.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.model.BrandType;
import com.chehubang.duolejie.modules.home.activity.WebviewActivity;
import com.chehubang.duolejie.modules.store.presenter.StorePresenter;
import com.chehubang.duolejie.utils.UploadUtil;
import com.chehubang.duolejie.utils.log;
import com.chehubang.duolejie.widget.NestRadioGroup;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import common.Utils.JSONUtils;
import common.Utils.ToastUtils;
import common.mvp.activity.MainView;
import common.picture.PictureUtils;
import io.reactivex.functions.Consumer;

/**
 * @Date: 2018/1/31 19:21
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description: 商户入驻
 */
public class StoreActivity extends BaseActivity<StorePresenter> implements MainView, NestRadioGroup.OnCheckedChangeListener {

    @BindView(R.id.storeName)
    EditText storeName;
    @BindView(R.id.storeUserName)
    EditText storeUserName;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.addressDetail)
    EditText addressDetail;
    @BindView(R.id.storeDetail)
    EditText storeDetail;
    @BindView(R.id.phone)
    EditText phone;
    /*@BindView(R.id.payZhifubao)
    EditText payZhifubao;*/
    /*@BindView(R.id.recommendId)
    EditText recommendId;*/
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    @BindView(R.id.iv_yingyezhizhao)
    ImageView iv_yingyezhizhao;
    @BindView(R.id.iv_logo)
    ImageView iv_logo;
    @BindView(R.id.iv_daoliu)
    ImageView iv_daoliu;
    private String yingyezhizhao, logo, daoliu;
    private RxPermissions mRxPermissions;
    @BindView(R.id.rg_store_type)
    RadioGroup rg_store_type;

    private int brand_type;
    private int type = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        ButterKnife.bind(this);
        mRxPermissions = new RxPermissions(this);
        mvpPresenter.getBrandType();
        rg_store_single_type.setOnCheckedChangeListener(this);
    }

    private void reloadData() {

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mvpPresenter.submit(1, storeName.getText().toString(), storeUserName.getText().toString()
                    , address.getText().toString(), addressDetail.getText().toString(), storeDetail.getText().toString(),
                    yingyezhizhao, logo, daoliu, phone.getText().toString(), String.valueOf(brand_type), String.valueOf(type));

        }
    };


    @OnClick({R.id.iv_back, R.id.btn_submit, R.id.ll_yingyezhizhao, R.id.ll_logo, R.id.ll_daoliu, R.id.address, R.id.tv_agree})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_submit:
                if (checkData()) {
                    showDialog();
                    new Thread() {
                        @Override
                        public void run() {
                            String json1 = UploadUtil.getInstance().toAsyncUploadFile(new File(yingyezhizhao), "file", MvpPresenter.URL_UPLOAD, null);
                            yingyezhizhao = UploadUtil.picPath(json1);
                            log.d(yingyezhizhao);
                            String json2 = UploadUtil.getInstance().toAsyncUploadFile(new File(logo), "file", MvpPresenter.URL_UPLOAD, null);
                            logo = UploadUtil.picPath(json2);
                            log.d(logo);
                            String json3 = UploadUtil.getInstance().toAsyncUploadFile(new File(daoliu), "file", MvpPresenter.URL_UPLOAD, null);
                            daoliu = UploadUtil.picPath(json3);
                            log.d(daoliu);
                            mHandler.sendEmptyMessage(0);

                        }
                    }.start();
                }
                break;
            case R.id.ll_yingyezhizhao:
                if (mRxPermissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 100);
                } else {
                    mRxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe(new Consumer<Boolean>() {
                                @Override
                                public void accept(Boolean grant) throws Exception {
                                    if (grant) {
                                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        startActivityForResult(intent, 100);
                                    } else {
                                        ToastUtils.centerToastWhite(StoreActivity.this, "请授予读写SD卡的权限");
                                    }

                                }
                            });
                }

                break;
            case R.id.ll_logo:
                if (mRxPermissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 200);
                } else {
                    mRxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe(new Consumer<Boolean>() {
                                @Override
                                public void accept(Boolean grant) throws Exception {
                                    if (grant) {
                                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        startActivityForResult(intent, 200);
                                    } else {
                                        ToastUtils.centerToastWhite(StoreActivity.this, "请授予读写SD卡的权限");
                                    }

                                }
                            });
                }
                break;
            case R.id.ll_daoliu:
                if (mRxPermissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 300);
                } else {
                    mRxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe(new Consumer<Boolean>() {
                                @Override
                                public void accept(Boolean grant) throws Exception {
                                    if (grant) {
                                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        startActivityForResult(intent, 300);
                                    } else {
                                        ToastUtils.centerToastWhite(StoreActivity.this, "请授予读写SD卡的权限");
                                    }

                                }
                            });
                }
                break;
            case R.id.address:
                if (mRxPermissions.isGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Intent intent = new Intent(this, BaiduMapActivity.class);
                    startActivityForResult(intent, 400);
                } else {
                    mRxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                            .subscribe(new Consumer<Boolean>() {
                                @Override
                                public void accept(Boolean flag) throws Exception {
                                    if (flag) {
                                        Intent intent = new Intent(StoreActivity.this, BaiduMapActivity.class);
                                        startActivityForResult(intent, 400);
                                    } else {
                                        ToastUtils.centerToastWhite(StoreActivity.this, "请在设置里授予应用定位权限");
                                    }

                                }
                            });
                }
                break;
            case R.id.tv_agree:
                Intent intent1 = new Intent(this, WebviewActivity.class);
                intent1.putExtra("url", "http://39.107.14.118/appInterface/aboutUsContentInfo.jhtml?id=9");
                intent1.putExtra("title", "商家入驻须知");
                startActivity(intent1);
                break;
        }
    }

    private boolean checkData() {
        if (TextUtils.isEmpty(storeName.getText().toString())) {
            ToastUtils.centerToastWhite(this, "请输入店铺名称");
            return false;
        }
        if (TextUtils.isEmpty(storeUserName.getText().toString())) {
            ToastUtils.centerToastWhite(this, "请输入店主名称");
            return false;
        }
        if (TextUtils.isEmpty(address.getText().toString())) {
            ToastUtils.centerToastWhite(this, "请输入地址名称");
            return false;
        }
        if (TextUtils.isEmpty(addressDetail.getText().toString())) {
            ToastUtils.centerToastWhite(this, "请输入详细地址名称");
            return false;
        }
        if (TextUtils.isEmpty(storeDetail.getText().toString())) {
            ToastUtils.centerToastWhite(this, "请输入经营项目简介");
            return false;
        }
        if (TextUtils.isEmpty(yingyezhizhao)) {
            ToastUtils.centerToastWhite(this, "请选择营业执照");
            return false;
        }
        if (TextUtils.isEmpty(logo)) {
            ToastUtils.centerToastWhite(this, "请选择门头LOGO");
            return false;
        }
        if (TextUtils.isEmpty(daoliu)) {
            ToastUtils.centerToastWhite(this, "请选择导流图");
            return false;
        }
        if (TextUtils.isEmpty(phone.getText())) {
            ToastUtils.centerToastWhite(this, "请输入联系方式");
            return false;
        }
        /*if (TextUtils.isEmpty(payZhifubao.getText())) {
            ToastUtils.centerToastWhite(this, "请输入支付宝账号");
            return false;
        }*/
        if (!checkbox.isChecked()) {
            ToastUtils.centerToastWhite(this, "请同意《商家入驻协议》");
            return false;
        }
        if (type == 2) {
            if (brand_type == -1) {
                ToastUtils.centerToastWhite(this, "请选择商户类型");
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != 400) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                Uri selectedImage = data.getData();
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                String imagePath = c.getString(columnIndex);
                showImage(imagePath, requestCode);
                c.close();
            }
        } else {
            if (resultCode == RESULT_OK && data != null) {
                address.setText(data.getStringExtra("data"));
            }
        }
    }

    //加载图片
    private void showImage(String imaePath, int requestCode) {
        log.d(imaePath);
        if (requestCode == 100) {
            yingyezhizhao = imaePath;
            PictureUtils.loadPictureLoc(this, imaePath, iv_yingyezhizhao, R.drawable.icon_logo);
        } else if (requestCode == 200) {
            logo = imaePath;
            PictureUtils.loadPictureLoc(this, imaePath, iv_logo, R.drawable.icon_logo);
        } else {
            daoliu = imaePath;
            PictureUtils.loadPictureLoc(this, imaePath, iv_daoliu, R.drawable.icon_logo);
        }
    }

    @Override
    protected StorePresenter createPresenter() {
        return new StorePresenter(this);
    }

    @BindView(R.id.rg_store_single_type)
    NestRadioGroup rg_store_single_type;

    @Override
    public void getDataSuccess(Object model, int action) {
        if (action == 1) {
            dismissDialog();
            ToastUtils.centerToastWhite(this, "提交成功, 请等待审核");
            finish();
        } else {
            log.d(model.toString());
            BrandType brandType = JSONUtils.GsonToBean(model.toString(), BrandType.class);
            if (brandType != null && brandType.getBrandTypeList() != null && !brandType.getBrandTypeList().isEmpty()) {
                List<BrandType.BrandTypeListBean> brandTypeList = brandType.getBrandTypeList();
                LayoutInflater from = LayoutInflater.from(this);
                for (BrandType.BrandTypeListBean bean : brandTypeList) {
                    if ("y".equals(bean.getIs_open())) {
                        View view =  from.inflate(R.layout.single_radio_button, null);
                        RadioButton btn = (RadioButton) view.findViewById(R.id.rb_offline_store_type_1);
                        btn.setId(bean.getId());
                        btn.setText(bean.getBrand_type());
                        rg_store_single_type.addView(view);
                    }
                }

            }
        }
    }

    @Override
    public void getDataFail(String msg, int action) {
        dismissDialog();
        ToastUtils.centerToastWhite(this, "提交审核失败，请稍后再试");
    }

    @BindView(R.id.rb_offline_store)
    RadioButton rb_offline_store;
    @BindView(R.id.rb_online_store)
    RadioButton rb_online_store;
    @BindView(R.id.rg_offline_type)
    View rg_offline_type;

    @OnClick({R.id.rb_online_store, R.id.rb_offline_store})
    public void onClickButton(View view) {
        switch (view.getId()) {
            case R.id.rb_online_store:
                rg_offline_type.setVisibility(View.GONE);
                type = 1;
                break;
            case R.id.rb_offline_store:
                type = 2;
                rg_offline_type.setVisibility(View.VISIBLE);
                break;
        }

    }

    @Override
    public void onCheckedChanged(NestRadioGroup group, int checkedId) {
        brand_type = checkedId;
    }
}
