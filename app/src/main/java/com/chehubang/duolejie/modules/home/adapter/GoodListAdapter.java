package com.chehubang.duolejie.modules.home.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.model.GoodsListBean;

import java.util.List;

import common.picture.PictureUtils;

/**
 * Created by Thinkpad on 2017/12/11.
 */

public class GoodListAdapter extends BaseAdapter {

    private List<GoodsListBean> mList;
    private Context mcontext;

    public GoodListAdapter(Context context, List<GoodsListBean> list) {
        mcontext = context;
        mList = list;
    }
    @Override
    public int getCount() {
        if (mList.size()>4){
            return 4;
        }else {
            return mList.size();
        }
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mcontext, R.layout.item_goos_pinpai, null);
            viewHolder.icon_pic = (ImageView) convertView.findViewById(R.id.iv_home_goods_pic);
            viewHolder.icon_title = (TextView) convertView.findViewById(R.id.tv_home_goods_text);
            viewHolder.tv_good_price_text = (TextView) convertView.findViewById(R.id.tv_good_price_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        Picasso.with(mcontext).load(mList.get(position).getGood_header()).config(Bitmap.Config.RGB_565).into(viewHolder.icon_pic);
        PictureUtils.loadPicture(mcontext,mList.get(position).getGood_header(),viewHolder.icon_pic,R.drawable.pic_cycjjl);
       // Picasso.with(mcontext).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1513102405146&di=e35096fba3d3b90e5c9b6a5fad5c2956&imgtype=0&src=http%3A%2F%2Fimage.tianjimedia.com%2FuploadImages%2F2017%2F319%2F18%2F450HM9U7G69F_600.jpg").config(Bitmap.Config.RGB_565).into(viewHolder.icon_pic);
        viewHolder.icon_title.setText(mList.get(position).getBrand_name());
        //viewHolder.tv_content_text.setText(mList.get(position).getBrand_name());
        viewHolder.tv_good_price_text.setText(mList.get(position).getGood_price());
        return convertView;
    }

    class ViewHolder {
        ImageView icon_pic;
        TextView icon_title;
        TextView tv_good_price_text;
    }
}
