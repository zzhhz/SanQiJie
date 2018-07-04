package com.chehubang.duolejie.modules.home.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.widget.QuantityView;

/**
 * Created by Thinkpad on 2018/1/11.
 */

public class ShoppingViewHolder extends RecyclerView.ViewHolder {

    public QuantityView amount;
    public LinearLayout item,layout;
    public CheckBox select;
    public ImageView pic;
    public TextView name1, type, price;

    public ShoppingViewHolder(View itemView) {
        super(itemView);
        item = (LinearLayout) itemView.findViewById(R.id.ll_shopping_item_main);
        select = (CheckBox) itemView.findViewById(R.id.cb_shopping_select);
        pic = (ImageView) itemView.findViewById(R.id.iv_shopping_pic);
        name1 = (TextView) itemView.findViewById(R.id.tv_commodity_name_1);
        type = (TextView) itemView.findViewById(R.id.tv_commodity_type);
        price = (TextView) itemView.findViewById(R.id.tv_commodity_price);
        amount = (QuantityView) itemView.findViewById(R.id.av_shopping);
        layout = (LinearLayout) itemView.findViewById(R.id.ll_shopping_item);
    }
}
