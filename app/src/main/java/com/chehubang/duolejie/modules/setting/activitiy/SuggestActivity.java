package com.chehubang.duolejie.modules.setting.activitiy;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.modules.setting.adapter.PhotoAdapter;
import com.chehubang.duolejie.modules.setting.presenter.RecyclerItemClickListener;
import com.chehubang.duolejie.modules.setting.presenter.SuggestPresenter;
import com.chehubang.duolejie.utils.UploadUtil;
import com.chehubang.duolejie.widget.SelectDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.Utils.ToastUtils;
import common.mvp.activity.MainView;
import io.reactivex.functions.Consumer;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

/**
 * Created by ZZH on 2018/2/12.
 *
 * @Date: 2018/2/12
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description:
 */
public class SuggestActivity extends BaseActivity<SuggestPresenter> implements MainView, View.OnClickListener {
    @BindView(R.id.et_suggest)
    public EditText etSuggest;
    @BindView(R.id.et_phone)
    EditText etPhone;

    @BindView(R.id.tv_type)
    TextView tv_type;
    private PhotoAdapter photoAdapter;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private ArrayList<String> selectedPhotos = new ArrayList<>();
    RxPermissions mRxPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);
        ButterKnife.bind(this);
        mRxPermissions = new RxPermissions(this);
        tv_type.setOnClickListener(this);
        photoAdapter = new PhotoAdapter(this, selectedPhotos);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(photoAdapter);

        findViewById(R.id.tv_submit).setOnClickListener(this);
        findViewById(R.id.iv_setting_back).setOnClickListener(this);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        if (mRxPermissions.isGranted(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            if (photoAdapter.getItemViewType(position) == PhotoAdapter.TYPE_ADD) {
                                PhotoPicker.builder()
                                        .setPhotoCount(PhotoAdapter.MAX)
                                        .setShowCamera(false)
                                        .setPreviewEnabled(false)
                                        .setSelected(selectedPhotos)
                                        .start(SuggestActivity.this);
                            } else {
                                PhotoPreview.builder()
                                        .setPhotos(selectedPhotos)
                                        .setCurrentItem(position)
                                        .start(SuggestActivity.this);
                            }
                        } else {
                            mRxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
                                @Override
                                public void accept(Boolean flag) throws Exception {
                                    if (flag) {
                                        if (photoAdapter.getItemViewType(position) == PhotoAdapter.TYPE_ADD) {
                                            PhotoPicker.builder()
                                                    .setPhotoCount(PhotoAdapter.MAX)
                                                    .setShowCamera(false)
                                                    .setPreviewEnabled(false)
                                                    .setSelected(selectedPhotos)
                                                    .start(SuggestActivity.this);
                                        } else {
                                            PhotoPreview.builder()
                                                    .setPhotos(selectedPhotos)
                                                    .setCurrentItem(position)
                                                    .start(SuggestActivity.this);
                                        }
                                    } else {
                                        ToastUtils.centerToastWhite(SuggestActivity.this, "请授予应用读写SD的权限");
                                    }

                                }
                            });
                        }
                    }
                }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && RESULT_OK == resultCode) {
            tv_type.setText(data.getStringExtra("data"));
        }
        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {
            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            }
            selectedPhotos.clear();
            if (photos != null) {
                selectedPhotos.addAll(photos);
            }
            photoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getDataSuccess(Object model, int action) {
        if (action == 2) {
            ToastUtils.centerToastWhite(this, "反馈意见提交成功");
            dismissDialog();
            finish();

        }

    }


    @Override
    public void getDataFail(String msg, int action) {
        if (action == 2) {
            dismissDialog();
            ToastUtils.centerToastWhite(this, msg);
        }
    }

    @Override
    protected SuggestPresenter createPresenter() {
        return new SuggestPresenter(this);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mvpPresenter.submitSuggest(UserInfo.getInstance().getId(), etSuggest.getText().toString(), etPhone.getText().toString(), tv_type.getText().toString(), (ArrayList<String>) msg.obj);
                    break;
            }
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_type:
                Intent intent = new Intent(this, SelectDialog.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.tv_submit:
                if (checkData()) {
                    if (selectedPhotos.size() > 0) {
                        showDialog();
                        new Thread() {
                            @Override
                            public void run() {
                                List<String> list = new ArrayList<>();
                                for (String path : selectedPhotos) {
                                    String photo = UploadUtil.getInstance().toAsyncUploadFile(new File(path), "file", MvpPresenter.URL_UPLOAD, null);
                                    list.add(UploadUtil.picPath(photo));
                                }
                                Message msg = Message.obtain();
                                msg.obj = list;
                                msg.what = 1;
                                mHandler.sendMessage(msg);
                            }
                        }.start();
                    } else {

                    }
                }
                break;
            case R.id.iv_setting_back:
                finish();
                break;
        }
    }

    private boolean checkData() {
        if (TextUtils.isEmpty(etPhone.getText().toString())) {
            ToastUtils.centerToastWhite(this, "请输入手机号");
            return false;
        }
        if (TextUtils.isEmpty(tv_type.getText().toString())) {
            ToastUtils.centerToastWhite(this, "请选择反馈类型");
            return false;
        }
        if (TextUtils.isEmpty(etSuggest.getText().toString())) {
            ToastUtils.centerToastWhite(this, "请输入反馈意见");
            return false;
        }
        return true;
    }
}
