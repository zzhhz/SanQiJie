package com.chehubang.duolejie.testa;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.MvpPresenter;
import com.chehubang.duolejie.utils.UploadUtil;
import com.chehubang.duolejie.utils.log;

import java.io.File;

/**
 * Created by user on 2018/2/1.
 *
 * @date: 2018/2/1
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @description: String json3 = UploadUtil.getInstance().toAsyncUploadFile(new File(daoliu), "filename", MvpPresenter.BASE_URL, null);
 */
public class TestActivity extends AppCompatActivity {
    ImageView iv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        new Thread() {
            @Override
            public void run() {
                String json3 = UploadUtil.getInstance().toAsyncUploadFile(new File("/storage/emulated/0/Pictures/Screenshots/Screenshot_20180102-113702.png"), "file", MvpPresenter.URL_UPLOAD, null);
                log.d(json3);
            }
        }.start();
        iv = (ImageView) findViewById(R.id.iv);
        Bitmap bitmap = BitmapFactory.decodeFile("/storage/emulated/0/Pictures/Screenshots/Screenshot_20180102-113702.png");
        iv.setImageBitmap(bitmap);

    }
}
