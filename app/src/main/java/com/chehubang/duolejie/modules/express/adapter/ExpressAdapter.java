package com.chehubang.duolejie.modules.express.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.model.Express;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZZH on 2018/2/1.
 *
 * @Date: 2018/2/1
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description:
 */
public class ExpressAdapter extends BaseAdapter {
    private List<Express> dataList;
    private Context mContext;
    private LayoutInflater layoutInflater;

    public ExpressAdapter(Context context) {
        mContext = context;
        this.dataList = new ArrayList<>();
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.item_time_line, null);
            holder.viewTopLine = convertView.findViewById(R.id.view_top_line);
            holder.ivExpresSpot = (ImageView) convertView.findViewById(R.id.iv_expres_spot);
            holder.tvExpressText = (TextView) convertView.findViewById(R.id.tv_express_text);
            holder.tvExpressTime = (TextView) convertView.findViewById(R.id.tv_express_time);
            holder.tvExpressTime2 = (TextView) convertView.findViewById(R.id.tv_express_time_2);

            //将ViewHolder与convertView进行绑定
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Express content = dataList.get(position);

        //设置数据颜色，防止view 复用，必须每个设置
        if (position == 0) {  //上顶部背景透明，点是灰色,字体是绿色
            holder.viewTopLine.setBackgroundColor(Color.TRANSPARENT);
            holder.ivExpresSpot.setBackgroundResource(R.drawable.express_point_new);
            holder.tvExpressText.setTextColor(mContext.getResources().getColor(R.color.color_express));
            holder.tvExpressTime.setTextColor(mContext.getResources().getColor(R.color.color_express));
            holder.tvExpressTime2.setTextColor(mContext.getResources().getColor(R.color.color_express));
        } else {
            holder.viewTopLine.setBackgroundColor(mContext.getResources().getColor(R.color.deep_gray));
            holder.ivExpresSpot.setBackgroundResource(R.drawable.express_point_old);
            holder.tvExpressText.setTextColor(Color.BLACK);
            holder.tvExpressTime.setTextColor(Color.BLACK);
            holder.tvExpressTime2.setTextColor(Color.BLACK);
        }
        holder.tvExpressText.setText(content.getAcceptStation());
        if (!TextUtils.isEmpty(content.getAcceptTime()) && content.getAcceptTime().contains(" ")) {
            String[] split = content.getAcceptTime().split(" ", 2);
            holder.tvExpressTime.setText(split[1]);
            holder.tvExpressTime2.setText(split[0]);
        } else {
            holder.tvExpressTime.setText(content.getAcceptTime());
        }
        return convertView;
    }

    public void addAll(List<Express> expresses) {
        dataList.addAll(expresses);
    }

    class ViewHolder {
        View viewTopLine;
        ImageView ivExpresSpot;
        TextView tvExpressText;
        TextView tvExpressTime;
        TextView tvExpressTime2;
    }
}
