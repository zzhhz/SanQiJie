package com.chehubang.duolejie.modules.order.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.modules.order.adapter.PhotoPreviewAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.ui.widget.FixViewPager;


public class PhotoPreviewActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    static final String PHOTO_LIST = "photo_list";

    private RelativeLayout mTitleBar;
    private ImageView mIvBack;
    private TextView mTvTitle;
    private TextView mTvIndicator;

    private ViewPager mVpPager;
    private List<MediaBean> mPhotoList;
    private PhotoPreviewAdapter mPhotoPreviewAdapter;
    int currentImageIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_preview);
        findViews();
        setListener();
        setTheme();

        mPhotoList = (List< MediaBean>) getIntent().getSerializableExtra(PHOTO_LIST);
        if (mPhotoList==null) {
            mPhotoList = new ArrayList<>();
            List<String> banners = getIntent().getExtras().getStringArrayList("fileList");
            for (int i = 0; i < banners.size(); i++) {
                MediaBean photoInfo = new MediaBean();
                photoInfo.setOriginalPath(banners.get(i));
                photoInfo.setId(i);
                mPhotoList.add(photoInfo);
            }
        }
            currentImageIndex = getIntent().getIntExtra("currentImageIndex",0);
            mPhotoPreviewAdapter = new PhotoPreviewAdapter(this, mPhotoList);
            mVpPager.setAdapter(mPhotoPreviewAdapter);
            mVpPager.setCurrentItem(currentImageIndex);
//            boolean isShowTitle = getIntent().getBooleanExtra("isShow",true);
//            if (isShowTitle) {
//                findViewById(R.id.titlebar).setVisibility(View.VISIBLE);
//            }else {
//                findViewById(R.id.titlebar).setVisibility(View.GONE);
//            }

    }

    private void findViews() {
        mTitleBar = (RelativeLayout) findViewById(R.id.titlebar);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvIndicator = (TextView) findViewById(R.id.tv_indicator);

        mVpPager = (FixViewPager) findViewById(R.id.vp_pager);
    }

    private void setListener() {
        mVpPager.addOnPageChangeListener(this);
        mIvBack.setOnClickListener(mBackListener);
    }

    private void setTheme() {
        mTitleBar.setBackgroundColor(Color.TRANSPARENT);
        mTvTitle.setTextColor(Color.WHITE);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mTvIndicator.setText((position + 1) + "/" + mPhotoList.size());
        mTvIndicator.setTextColor(Color.WHITE);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private View.OnClickListener mBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

}
