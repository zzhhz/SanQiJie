package com.chehubang.duolejie.modules.carlife.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.model.MoneyTypeBean;
import com.chehubang.duolejie.model.OilCardMoneyTypeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thinkpad on 2017/12/19.
 */

public class MoneyTypeAdapter extends BaseAdapter {

    private Context mcontext;
    private List<OilCardMoneyTypeBean> mlist = new ArrayList<>();
    private int selectedPosition = -1;// 选中的位置

    public MoneyTypeAdapter(Context context, List<OilCardMoneyTypeBean> list) {
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
            convertView = View.inflate(mcontext, R.layout.item_money_type, null);
            viewHolder.type = (TextView) convertView.findViewById(R.id.tv_money_type_item);
            viewHolder.tv_yuanjia = (TextView) convertView.findViewById(R.id.tv_yuanjia);
            viewHolder.rl_content = (RelativeLayout) convertView.findViewById(R.id.rl_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (selectedPosition == position) {
            viewHolder.rl_content.setBackgroundResource(R.drawable.shape_corner_red);
            viewHolder.type.setTextColor(Color.WHITE);
            viewHolder.tv_yuanjia.setTextColor(Color.WHITE);

        } else {
            viewHolder.rl_content.setBackgroundResource(R.drawable.shape_corner);
            viewHolder.type.setTextColor(Color.parseColor("#eca29c"));
            viewHolder.tv_yuanjia.setTextColor(Color.parseColor("#eca29c"));
        }
        String value = mlist.get(position).getValue();
        viewHolder.tv_yuanjia.setText((int) (Double.parseDouble(value) / 0.95) + "元");

        viewHolder.type.setText("折后价:" + value + "元");
        return convertView;
    }

    class ViewHolder {
        TextView type;
        TextView tv_yuanjia;
        RelativeLayout rl_content;
    }
}
