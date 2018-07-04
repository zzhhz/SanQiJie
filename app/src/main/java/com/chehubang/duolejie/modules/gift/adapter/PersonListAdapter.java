package com.chehubang.duolejie.modules.gift.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.model.GoodsChildTypeBean;
import com.chehubang.duolejie.model.UserListBean;
import com.chehubang.duolejie.utils.DateUtils;
import com.chehubang.duolejie.widget.CircleImageView;

import java.util.List;

import common.picture.PictureUtils;

/**
 * Created by Thinkpad on 2017/12/11.
 */

public class PersonListAdapter extends BaseAdapter {

    private List<UserListBean> mList;
    private Context mcontext;

    public PersonListAdapter(Context context, List<UserListBean> list) {
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
            convertView = View.inflate(mcontext, R.layout.item_person_list, null);
            viewHolder.header = (CircleImageView) convertView.findViewById(R.id.iv_ld_head);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_ld_name);
            viewHolder.time = (TextView) convertView.findViewById(R.id.tv_ld_time);
            viewHolder.address = (TextView) convertView.findViewById(R.id.tv_ld_address);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PictureUtils.loadPicture(mcontext,mList.get(position).getUser_header(),viewHolder.header, R.drawable.pic_cycjjl);
        viewHolder.name.setText(mList.get(position).getNick_name());
        viewHolder.time.setText(DateUtils.getData(mList.get(position).getCreate_time()));
        viewHolder.address.setText(mList.get(position).getUser_ip_address());
        return convertView;
    }

    class ViewHolder {
        CircleImageView header;
        TextView name;
        TextView time;
        TextView address;
    }
}
