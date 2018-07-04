package com.chehubang.duolejie.modules.mysetting.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chehubang.duolejie.R;


/**
 * Created by Thinkpad on 2016/6/24.
 */
public class HeadDialog extends Dialog implements View.OnClickListener{

    private TextView pic_album;
    private TextView pic_camera;
    private TextView pic_cancel;

    public HeadDialog(Context context) {
        super(context, R.style.DialogStyleBottom);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pip_source_select);
        // 设置dialog宽度填充屏幕，高度自适应
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置dialog显示位置
        getWindow().setGravity(Gravity.BOTTOM);
        TextView tvCancel = (TextView) findViewById(R.id.tv_getpic_cancel);
        tvCancel.setOnClickListener(this);
    }

    public void setOnButtonClickListener(View.OnClickListener listener) {
        findViewById(R.id.tv_pic_from_album).setOnClickListener(listener);
        findViewById(R.id.tv_pic_from_camera).setOnClickListener(listener);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_getpic_cancel) {
            dismiss();
        }
    }
}
