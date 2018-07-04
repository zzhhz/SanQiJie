package com.chehubang.duolejie.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

/**
 * Created by Beidouht on 2017/7/26.
 */

public class RerequestPermission {

    public static void request(final Context context){
        new AlertDialog.Builder(context)
                .setMessage("权限已被拒绝，前往设置打开？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + context.getPackageName())); // 根据包名打开对应的设置界面
                        context.startActivity(intent);
                    }
                }).show();
    }
}
