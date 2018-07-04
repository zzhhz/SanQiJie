package com.chehubang.duolejie.modules.chargecenter.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.model.ChargeOrderGoodsBean;
import com.chehubang.duolejie.model.MoneyTypeBean;

import java.util.ArrayList;
import java.util.List;

import common.picture.PictureUtils;

/**
 * Created by Thinkpad on 2018/1/9.
 */

public class GoodsListAdapter extends BaseAdapter {
    private Context mcontext;
    private List<ChargeOrderGoodsBean> mlist = new ArrayList<>();
    private int selectedPosition = -1;// 选中的位置

    public GoodsListAdapter(Context context, List<ChargeOrderGoodsBean> list) {
        this.mcontext = context;
        this.mlist = list;
    }

    @Override
    public int getCount() {
        return mlist.size();
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
            convertView = View.inflate(mcontext, R.layout.item_charge_center_goods, null);
             viewHolder.goodsPic = (ImageView) convertView.findViewById(R.id.iv_charge_order_goods_pic);
             viewHolder.goodsName = (TextView) convertView.findViewById(R.id.tv_charge_order_goods_name);
              viewHolder.nickName = (TextView) convertView.findViewById(R.id.tv_charge_order_goods_nickname);
              viewHolder.goodsPrice = (TextView) convertView.findViewById(R.id.tv_charge_order_goods_price);
             viewHolder.goodsPriceOri = (TextView) convertView.findViewById(R.id.tv_charge_order_goods_price_ori);
            viewHolder.goodsPiece = (TextView) convertView.findViewById(R.id.tv_charge_order_goods_piece);
             convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PictureUtils.loadPicture(mcontext, mlist.get(position).getGood_header(), viewHolder.goodsPic, R.drawable.loading);
        viewHolder.goodsName.setText(mlist.get(position).getGood_name());
        viewHolder.nickName.setText(mlist.get(position).getBrand_name());
        viewHolder.goodsPrice.setText("￥"+mlist.get(position).getGood_price());
        viewHolder.goodsPiece.setText("X"+mlist.get(position).getGoods_buy_num());
        viewHolder.goodsPriceOri.setText("￥"+mlist.get(position).getGood_single_price());
        return convertView;
    }

    class ViewHolder {
        ImageView goodsPic;
        TextView goodsName,nickName,goodsPrice,goodsPriceOri,goodsPiece;
    }
}
