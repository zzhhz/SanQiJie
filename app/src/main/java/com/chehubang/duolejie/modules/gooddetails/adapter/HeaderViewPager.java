package com.chehubang.duolejie.modules.gooddetails.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chehubang.duolejie.R;

import java.util.ArrayList;
import java.util.List;

import common.picture.PictureUtils;

/**
 * Created by Thinkpad on 2017/12/11.
 */

public class HeaderViewPager extends PagerAdapter {

    private Context mcontext;
    private List<String> mlist = new ArrayList<>();

    public HeaderViewPager(Context context, List<String> list) {
        this.mcontext = context;
        this.mlist = list;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

            ImageView imageView = new ImageView(mcontext);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            PictureUtils.loadPicture(mcontext,mlist.get(position),imageView, R.drawable.pic_cycjjl);
            container.addView(imageView);
            return imageView;
    }

    // 销毁条目对象
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // super.destroyItem(container, position, object);
        container.removeView((View) object);
    }

}
