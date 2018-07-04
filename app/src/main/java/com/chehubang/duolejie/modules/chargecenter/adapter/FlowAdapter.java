package com.chehubang.duolejie.modules.chargecenter.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.model.MoneyTypeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thinkpad on 2017/12/19.
 */

public class FlowAdapter extends BaseAdapter {

    private Context mcontext;
    private List<MoneyTypeBean> mlist = new ArrayList<>();
    private int selectedPosition = -1;// 选中的位置

    public FlowAdapter(Context context, List<MoneyTypeBean> list) {
        this.mcontext = context;
        this.mlist = list;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
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
            convertView = View.inflate(mcontext, R.layout.item_money_type2, null);
            viewHolder.type = (TextView) convertView.findViewById(R.id.tv_money_type_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();  
        }

        if (selectedPosition == position) {
            viewHolder.type.setBackgroundResource(R.drawable.shape_corner_red);
            viewHolder.type.setTextColor(Color.WHITE);
        } else {
            viewHolder.type.setBackgroundResource(R.drawable.shape_corner);
            viewHolder.type.setTextColor(Color.parseColor("#eca29c"));
        }
        String type = mlist.get(position).getType();
        String data = type.substring(5, type.length());
        viewHolder.type.setText(data+"\t\t"+mlist.get(position).getValue()+"元");
        return convertView;
    }

    class ViewHolder {
        TextView type;
    }
}
