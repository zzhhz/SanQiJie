package com.chehubang.duolejie.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.chehubang.duolejie.R;

/**
 * Created by user on 2017/11/21.
 *
 * @date: 2017/11/21
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @description: 等待框
 */
public class LoadingDialog extends Dialog {
    TextView msg;

    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.progress_dialog);
        initDialog();
    }

    private void initDialog() {
        setContentView(R.layout.dialog_loading);
        msg = (TextView) findViewById(R.id.id_tv_loading);
        msg.setText("加载中...");
    }

    public void setShowText(String text) {
        msg.setText(text);
    }
}
