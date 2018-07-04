package com.chehubang.duolejie.modules.setting.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.chehubang.duolejie.R;
import com.chehubang.duolejie.model.NoticeListBean;
import java.util.List;

/**
 * Created by Thinkpad on 2017/12/25.
 */

public class NoticeAdapter extends BaseAdapter {

    private Context mcontext;
    private List<NoticeListBean> mlist;

    public NoticeAdapter(Context context, List<NoticeListBean> list) {
        mcontext = context;
        mlist = list;
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
            convertView = View.inflate(mcontext, R.layout.item_notice, null);
            viewHolder.time = (TextView) convertView.findViewById(R.id.tv_notice_item_time);
            viewHolder.title = (TextView) convertView.findViewById(R.id.tv_notice_item_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.time.setText(mlist.get(position).getCreate_time());
        viewHolder.title.setText(mlist.get(position).getTitle());
        return convertView;
    }

    class ViewHolder {
        TextView time;
        TextView title;
    }
}
