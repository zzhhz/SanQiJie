package com.chehubang.duolejie.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.config.UserInfo;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by ZZH on 2018/2/7.
 *
 * @Date: 2018/2/7
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description:
 */
public class ShareUtils {
    /**
     * @param activity
     * @param user
     * @param qrCode
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static Bitmap createBitmap(final Activity activity, Bitmap user, Bitmap qrCode) {
        View view = LayoutInflater.from(activity).inflate(R.layout.share_img, null);
        view.setDrawingCacheEnabled(true);
        UserInfo.getInstance().getCache(activity);
        ImageView qr = (ImageView) view.findViewById(R.id.iv_qr);
        ImageView userHeaer = (ImageView) view.findViewById(R.id.iv_header);
        TextView userName = (TextView) view.findViewById(R.id.tv_user_name);
        TextView tv_id = (TextView) view.findViewById(R.id.tv_id);
        TextView tv_code = (TextView) view.findViewById(R.id.tv_code);
        if (TextUtils.isEmpty(UserInfo.getInstance().getInvite_code())) {
            tv_code.setVisibility(View.GONE);
        } else {
            tv_code.setVisibility(View.VISIBLE);
            tv_code.setText("邀请码：" + UserInfo.getInstance().getInvite_code());
        }
        //userHeaer.setBackground(new BitmapDrawable(user));
        userHeaer.setImageBitmap(user);
        //qr.setBackground(new BitmapDrawable(qrCode));
        qr.setImageBitmap(qrCode);
        tv_id.setText(UserInfo.getInstance().getId());
        userName.setText(UserInfo.getInstance().getNick_name());
//图片的大小 固定的语句
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        //将位置传给view
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        //转化为bitmap文件
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    /**
     * @param activity
     * @param handler
     * @param qrCodeUrl
     */
    public static void getBitmaps(final Activity activity, final Handler handler, final String qrCodeUrl) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Bitmap avater = Picasso.with(activity).load(UserInfo.getInstance().getUser_header()).get();
                    Bitmap qrImage = Picasso.with(activity).load(qrCodeUrl).get();
                    Bitmap bits[] = new Bitmap[]{avater, qrImage};
                    Message msg = Message.obtain();
                    msg.obj = bits;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        return bos.toByteArray();
    }
}
