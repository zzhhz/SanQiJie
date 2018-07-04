package com.chehubang.duolejie.modules.order.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;


import com.chehubang.duolejie.R;

import java.io.File;
import java.util.List;

import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.utils.DeviceUtils;
import common.picture.PictureUtils;
import uk.co.senab.photoview.PhotoView;



public class PhotoPreviewAdapter extends ViewHolderRecyclingPagerAdapter<PhotoPreviewAdapter.PreviewViewHolder, MediaBean> {

    private Activity mActivity;
    private DisplayMetrics mDisplayMetrics;

    public PhotoPreviewAdapter(Activity activity, List<MediaBean> list) {
        super(activity, list);
        this.mActivity = activity;
        this.mDisplayMetrics = DeviceUtils.getScreenSize(mActivity);
    }

    @Override
    public PreviewViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = getLayoutInflater().inflate(R.layout.gallery_media_image_preview_item, null);
        return new PreviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PreviewViewHolder holder, int position) {
        MediaBean photoInfo = getDatas().get(position);
        String path = "";
        if (photoInfo != null) {
            path = photoInfo.getOriginalPath();
        }
        holder.mImageView.setImageResource(R.drawable.gallery_default_image);
        Drawable defaultDrawable = mActivity.getResources().getDrawable(R.drawable.gallery_default_image);
        File file =new File(path);
        //本地图片
        if(file.exists()){
            PictureUtils.loadPictureLocSize(mActivity,path,holder.mImageView,R.drawable.gallery_default_image,mDisplayMetrics.widthPixels/2, mDisplayMetrics.heightPixels/2);
        }else{
            //网络图片
            PictureUtils.loadPicture(mActivity,path,holder.mImageView,R.drawable.gallery_default_image);
        }
    }

    static class PreviewViewHolder extends ViewHolderRecyclingPagerAdapter.ViewHolder{
        PhotoView mImageView;
        public PreviewViewHolder(View view) {
            super(view);
            mImageView = (PhotoView) view;
        }
    }
}
