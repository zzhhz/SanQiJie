package com.chehubang.duolejie.modules.address.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.listener.OnItemCheckedListener;
import com.chehubang.duolejie.listener.OnItemClickListener;
import com.chehubang.duolejie.model.AddressListBean;
import com.chehubang.duolejie.model.AddressListMangerBean;
import com.chehubang.duolejie.modules.address.activity.AddAddressActivity;

import java.util.List;

/**
 * Created by Thinkpad on 2017/12/28.
 */

public class AddressManageAdapter extends BaseAdapter {

    private Context mcontext;
    private List<AddressListMangerBean> list;
    private OnItemClickListener clickListener;
    private OnItemCheckedListener mcheckedListener;
    private boolean isupdata = false;

    public AddressManageAdapter(Context context, List<AddressListMangerBean> list, OnItemClickListener clickListener, OnItemCheckedListener checkedListener) {
        mcontext = context;
        this.list = list;
        this.clickListener = clickListener;
        this.mcheckedListener = checkedListener;
    }

    @Override
    public int getCount() {
        return list.size();
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
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mcontext, R.layout.item_address_manage, null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_item_address_manage_name);
            viewHolder.tel = (TextView) convertView.findViewById(R.id.tv_item_address_manage_tel);
            viewHolder.address = (TextView) convertView.findViewById(R.id.tv_item_address_manage_address);
            viewHolder.edit = (TextView) convertView.findViewById(R.id.tv_item_address_manage_edit);
            viewHolder.delete = (TextView) convertView.findViewById(R.id.tv_item_address_manage_delete);
            viewHolder.setDefault = (CheckBox) convertView.findViewById(R.id.cb_item_address_manage_default);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(list.get(position).getUser_name());
        viewHolder.tel.setText(list.get(position).getUser_tel());
        viewHolder.address.setText(list.get(position).getProvince_name() + list.get(position).getCity_name() + list.get(position).getDetail_address());
        viewHolder.setDefault.setOnCheckedChangeListener(null);
        String is_default = list.get(position).getIs_default();
        if (TextUtils.equals(is_default, "n")) {
            viewHolder.setDefault.setChecked(false);
        } else if (TextUtils.equals(is_default, "y")) {
            viewHolder.setDefault.setChecked(true);
        }

        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext, AddAddressActivity.class);
                AddressListMangerBean value = list.get(position);
                AddressListBean listBean = new AddressListBean();
                listBean.setCity_id(value.getCity_id());
                listBean.setCreate_time(value.getCreate_time());
                listBean.setDetail_address(value.getDetail_address());
                listBean.setId(value.getId());
                listBean.setUser_id(value.getUser_id());
                listBean.setIs_default(value.getIs_default());
                listBean.setProvince_addr(value.getProvince_name());
                listBean.setProvince_id(value.getProvince_id());
                listBean.setCity_addr(value.getCity_name());
                listBean.setUser_tel(value.getUser_tel());
                listBean.setUser_name(value.getUser_name());
                intent.putExtra("item", listBean);
                ((Activity) mcontext).startActivityForResult(intent, 5001);
            }
        });
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(view, position);
            }
        });

        viewHolder.setDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mcheckedListener.onItemChecked(position, b);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView name;
        TextView tel;
        TextView address;
        TextView edit;
        TextView delete;
        CheckBox setDefault;
    }
}
