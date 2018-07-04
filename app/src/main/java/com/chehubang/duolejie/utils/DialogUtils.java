package com.chehubang.duolejie.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chehubang.duolejie.R;

/**
 * Created by Beidouht on 2017/8/14.
 */

public class DialogUtils {

    public static void setDialogStyle(Dialog dialog) {
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = WindowManager.LayoutParams.FILL_PARENT;
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(attributes);
        window.setGravity(Gravity.BOTTOM);
    }

    public static PopupWindow shareUi(Context ctx, View showAbove, final OnShareClickListener clickListener) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.share_ui, null);
        final PopupWindow window = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setContentView(view);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        window.setBackgroundDrawable(dw);
        view.findViewById(R.id.tv_weixin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                if (clickListener != null) {
                    clickListener.onShareWeiXin();
                }
            }
        });
        view.findViewById(R.id.tv_weixin_circle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                if (clickListener != null) {
                    clickListener.onShareCircle();
                }
            }
        });
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        return window;
    }

    public interface OnShareClickListener {
        void onShareWeiXin();

        void onShareCircle();
    }

    public static Dialog getShowMsg(Context ctx, String title, String msg) {
        final Dialog dialog = new Dialog(ctx, R.style.AlertDialogStyle);
        View view = LayoutInflater.from(ctx).inflate(
                R.layout.view_alertdialog, null);

        // 获取自定义Dialog布局中的控件
        LinearLayout lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        TextView txt_title = (TextView) view.findViewById(R.id.txt_title);
        TextView txt_msg = (TextView) view.findViewById(R.id.txt_msg);
        TextView btn_pos = (TextView) view.findViewById(R.id.btn_pos);

        txt_title.setText(title);
        txt_msg.setText(msg);
        btn_pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // 定义Dialog布局和参数

        dialog.setContentView(view);

        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new LinearLayout.LayoutParams((int) (((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth() * 0.65), LinearLayout.LayoutParams.WRAP_CONTENT));

        return dialog;
    }

    public static Dialog inputCode(Context ctx, final InputCodeListener codeListener) {
        final Dialog dialog = new Dialog(ctx, R.style.AlertDialogStyle);
        View view = LayoutInflater.from(ctx).inflate(
                R.layout.view_alertdialog2, null);

        // 获取自定义Dialog布局中的控件
        LinearLayout lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        final EditText txt_msg = (EditText) view.findViewById(R.id.txt_msg);
        TextView btn_pos = (TextView) view.findViewById(R.id.btn_pos);
        TextView btn_cancel = (TextView) view.findViewById(R.id.btn_cancel);
        btn_pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (codeListener != null) {
                    codeListener.inputCode(txt_msg.getText().toString());
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // 定义Dialog布局和参数
        dialog.setContentView(view);

        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new LinearLayout.LayoutParams((int) (((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth() * 0.75), LinearLayout.LayoutParams.WRAP_CONTENT));
        return dialog;
    }

    public interface InputCodeListener {

        void inputCode(String code);

    }
}
