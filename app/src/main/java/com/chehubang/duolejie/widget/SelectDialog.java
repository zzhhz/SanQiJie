package com.chehubang.duolejie.widget;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.chehubang.duolejie.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZZH on 2018/2/12.
 *
 * @Date: 2018/2/12
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description:
 */
public class SelectDialog extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select);

        ButterKnife.bind(this);
    }

    @OnClick({R.id.ll_empty_view, R.id.tv_store, R.id.tv_other, R.id.tv_product, R.id.tv_wuliu})
    public void onClickView(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.ll_empty_view:
                break;
            case R.id.tv_store:
                intent.putExtra("data", "商品相关");
                setResult(RESULT_OK, intent);
                break;
            case R.id.tv_other:
                intent.putExtra("data", "其他");
                setResult(RESULT_OK, intent);
                break;
            case R.id.tv_product:
                intent.putExtra("data", "产品建议");
                setResult(RESULT_OK, intent);
                break;
            case R.id.tv_wuliu:
                intent.putExtra("data", "物流状况");
                setResult(RESULT_OK, intent);
                break;
        }
        finish();
    }
}
