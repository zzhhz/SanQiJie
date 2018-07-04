package com.chehubang.duolejie.modules.installmentorder.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.model.InstallOrderBean;
import com.chehubang.duolejie.modules.installmentorder.activity.InstallDetailsActivity;

import java.util.List;

/**
 * Created by Thinkpad on 2017/12/25.
 */

public class GascardAdapter extends BaseAdapter{

    private Context mcontext;
    private List<InstallOrderBean> mlist;

    public GascardAdapter(Context context, List<InstallOrderBean> list) {
        mcontext = context;
        mlist = list;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public InstallOrderBean getItem(int i) {
        return mlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mcontext, R.layout.item_installmentorder, null);
             viewHolder.gasInsatall = (TextView) convertView.findViewById(R.id.tv_gascard_install);
             viewHolder.tv_view_details = (TextView) convertView.findViewById(R.id.tv_view_detail);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_view_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext,InstallDetailsActivity.class);
                intent.putExtra("order_id",mlist.get(position).getOrder_id());
                intent.putExtra("type", 2);
                mcontext.startActivity(intent);
            }
        });
        viewHolder.gasInsatall.setText(mlist.get(position).getAll_price() + "元加油卡分期12个月加油卡");
        return convertView;
    }

    class ViewHolder {
        TextView gasInsatall;
        TextView tv_view_details;
    }
}
