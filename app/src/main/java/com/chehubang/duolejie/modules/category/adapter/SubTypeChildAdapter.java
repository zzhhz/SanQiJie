package com.chehubang.duolejie.modules.category.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.model.GoodsChildTypeBean;
import com.chehubang.duolejie.model.GoodsTypeChildBean;

import java.util.List;

import common.Log.LogUtils;
import common.picture.PictureUtils;

/**
 * Created by Thinkpad on 2017/12/11.
 */

public class SubTypeChildAdapter extends BaseAdapter {

    private List<GoodsTypeChildBean> mList;
    private Context mcontext;

    public SubTypeChildAdapter(Context context, List<GoodsTypeChildBean> list) {
        mcontext = context;
        mList = list;
    }
    @Override
    public int getCount() {
       return mList.size();
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
            convertView = View.inflate(mcontext, R.layout.item_cate_good, null);
            viewHolder.icon_pic = (ImageView) convertView.findViewById(R.id.iv_home_goods_pic);
            viewHolder.icon_title = (TextView) convertView.findViewById(R.id.tv_home_goods_text);
            viewHolder.icon_price = (TextView) convertView.findViewById(R.id.tv_good_price_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
    //    Picasso.with(mcontext).load(mList.get(position).getGoods_type_header()).config(Bitmap.Config.RGB_565).into(viewHolder.icon_pic);
      //  Picasso.with(mcontext).load(mList.get(position).getGoods_type_header()).config(Bitmap.Config.RGB_565).into(viewHolder.icon_pic);
        PictureUtils.loadPicture(mcontext,mList.get(position).getGoods_type_header(),viewHolder.icon_pic, R.drawable.pic_cycjjl);
        viewHolder.icon_title.setText(mList.get(position).getGoods_type_name());
        viewHolder.icon_price.setText("￥" + mList.get(position).getGood_price());
        return convertView;
    }

    class ViewHolder {
        ImageView icon_pic;
        TextView icon_title;
        TextView icon_price;
    }
}
