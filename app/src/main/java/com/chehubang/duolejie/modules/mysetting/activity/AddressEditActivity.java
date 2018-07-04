package com.chehubang.duolejie.modules.mysetting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.modules.mysetting.presenter.AddressEditPresenter;

import common.mvp.activity.MainView;

/**
 * Created by Thinkpad on 2017/12/25.
 */

public class AddressEditActivity extends BaseActivity<AddressEditPresenter> implements MainView, View.OnClickListener {

    private EditText content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        ImageView back = (ImageView) findViewById(R.id.iv_edit_address_back);
        TextView updata = (TextView) findViewById(R.id.tv_edit_address_update);
        content = (EditText) findViewById(R.id.et_edit_address_content);
        back.setOnClickListener(this);
        updata.setOnClickListener(this);
    }

    @Override
    public void getDataSuccess(Object model, int action) {

    }

    @Override
    public void getDataFail(String msg, int action) {

    }

    @Override
    protected AddressEditPresenter createPresenter() {
        return new AddressEditPresenter(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_edit_address_back:
                finish();
                break;
            case R.id.tv_edit_address_update:
                String s = content.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("address",s);
                setResult(2002,intent);
                finish();
                break;
            default:
                break;
        }
    }
}
