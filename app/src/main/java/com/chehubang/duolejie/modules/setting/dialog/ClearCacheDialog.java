package com.chehubang.duolejie.modules.setting.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.chehubang.duolejie.R;


/**
 * Created by liubo on 2016/9/2.
 */
public class ClearCacheDialog extends Dialog {
    public ClearCacheDialog(Context context) {
        super(context, R.style.Dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clear_cache);

    }

    public void setOnButtonClickListener(View.OnClickListener listener) {
        findViewById(R.id.clear_cache_negative_Button).setOnClickListener(listener);
        findViewById(R.id.clear_cache_positive_Button).setOnClickListener(listener);
    }
}
