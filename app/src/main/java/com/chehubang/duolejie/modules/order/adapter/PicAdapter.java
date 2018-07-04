package com.chehubang.duolejie.modules.order.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.chehubang.duolejie.R;
import com.chehubang.duolejie.listener.OnPicDeleteListener;
import com.chehubang.duolejie.listener.OnloadDataListener;
import com.chehubang.duolejie.modules.order.activity.PhotoPreviewActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;
import common.picture.PictureUtils;


public class PicAdapter extends BaseAdapter {

    private Activity mcontext;
    private List<MediaBean> mlist;
    private OnloadDataListener mloadDatalistener;
    private int maxImages = 5;
    private OnPicDeleteListener mOnPicDeleteListener;


    public PicAdapter(Activity context, List list) {
        this.mcontext = context;
        this.mlist = list;
    }

    @Override
    public int getCount() {
        if (mlist ==null ||mlist.size()==0){
            return 1;
        }else if (mlist.size() == maxImages){
            return maxImages;
        }else {
            return mlist.size() + 1;
        }
    }

    public void addOnloadDataListener(OnloadDataListener loadDatalistener) {
        this.mloadDatalistener = loadDatalistener;
    }

    public void addOnPicDeleteListener(OnPicDeleteListener clickListener){
        this.mOnPicDeleteListener = clickListener;
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mcontext, R.layout.item_buscard_pic, null);
            holder.pic = (ImageView) convertView.findViewById(R.id.iv_buscard_picture);
            holder.picDel = (ImageView) convertView.findViewById(R.id.iv_buscard_pic_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        boolean isAdd = false;
        if (mlist != null && position < mlist.size()) {
            isAdd = false;
            holder.picDel.setVisibility(View.VISIBLE);
            PictureUtils.loadPictureLoc(mcontext, mlist.get(position).getOriginalPath(), holder.pic, R.drawable.loading);
            holder.picDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnPicDeleteListener.OnPicDelete(position);
                }
            });
        } else {   //+号的图片
            isAdd = true;
            holder.picDel.setVisibility(View.GONE);
            //加载资源文件
            Picasso.with(mcontext).load(R.drawable.add_icon_photo).into(holder.pic);
        }

        final boolean finalIsAdd = isAdd;
        holder.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalIsAdd){
                    if (ContextCompat.checkSelfPermission(mcontext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(mcontext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                    } else {
                        onClickPicture();
                    }
                }else {
                    Intent intent = new Intent(mcontext, PhotoPreviewActivity.class);
                    intent.putParcelableArrayListExtra("photo_list", (ArrayList<? extends Parcelable>) mlist);
                    intent.putExtra("currentImageIndex", position);
                    mcontext.startActivity(intent);
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView pic;
        ImageView picDel;
    }

    public void onClickPicture() {
        RxGalleryFinal.with(mcontext)
                .image()
                .multiple()
                .maxSize(maxImages - mlist.size())
                .imageLoader(ImageLoaderType.PICASSO)
                .subscribe(new RxBusResultDisposable<ImageMultipleResultEvent>() {
                    @Override
                    protected void onEvent(ImageMultipleResultEvent baseResultEvent) throws Exception {
                        mloadDatalistener.OnloadData(baseResultEvent);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }
                })
                .openGallery();
    }
}
