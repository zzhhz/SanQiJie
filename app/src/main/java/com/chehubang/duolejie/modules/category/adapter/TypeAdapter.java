package com.chehubang.duolejie.modules.category.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.model.GoodsTypeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beidouht on 2017/11/10.
 */

public class TypeAdapter extends BaseAdapter {

    private List<GoodsTypeBean> itemList = new ArrayList<>();
    private Context context;
    private int selectedPosition = -1;// 选中的位置

    public TypeAdapter(Context context, List<GoodsTypeBean> list) {
        this.context = context;
        this.itemList = list;
        this.context =context;
        this.itemList = list;
    }


    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }


    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_category_type, null);
            viewHolder = new ViewHolder();
            viewHolder.item = (TextView) convertView.findViewById(R.id.tv_item_type);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (selectedPosition == position) {
            viewHolder.item.setBackgroundResource(R.drawable.select_button_bg);
            viewHolder.item.setTextColor(Color.RED);
        } else {
            viewHolder.item.setBackgroundColor(Color.parseColor("#f3f3f3"));
            viewHolder.item.setTextColor(Color.BLACK);
        }

        viewHolder.item.setText(itemList.get(position).getGoods_type_name());
        return convertView;
    }

    class ViewHolder {
        TextView item;
    }
}
