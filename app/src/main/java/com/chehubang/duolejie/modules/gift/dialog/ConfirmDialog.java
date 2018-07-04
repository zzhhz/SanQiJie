package com.chehubang.duolejie.modules.gift.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.chehubang.duolejie.R;

/**
 * Created by fuyi on 2017/12/27.
 */

public class ConfirmDialog extends Dialog {
    private Context context;
    private Window window;


    public ConfirmDialog(Context context) {
        super(context, R.style.AddressStyleDialogTheme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_HORIZONTAL;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager m = window.getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.height = (int) (d.getHeight() * 0.38);
        p.width = (int) (d.getWidth() * 0.65);
        getWindow().setAttributes(p);
    }

}
