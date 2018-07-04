package com.chehubang.duolejie.modules.store.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.model.Address;

import java.util.ArrayList;
import java.util.List;

/**
 * 百度地图搜索结果列表适配器
 */
public class AddressPoiListAdapter extends BaseAdapter {
    private Context context = null;
    private List<Address> addressList = null;

    public AddressPoiListAdapter(Context context) {
        this.context = context;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return addressList == null ? 0 : addressList.size();
    }

    @Override
    public Address getItem(int arg0) {
        return addressList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(final int positon, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.list_item_poi_addr_location, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.img);
            holder.addrName = (TextView) convertView
                    .findViewById(R.id.address_name);
            holder.addrDetail = (TextView) convertView.findViewById(R.id.address_detail);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Address address = addressList.get(positon);
        if (address.isCurrent()) {
            holder.imageView.setImageResource(R.drawable.addr_location_site_red);
            holder.addrName.setText("[当前]" + address.getAddress());
            holder.addrName.setTextColor(Color.rgb(255, 0, 0));
        } else {
            holder.imageView.setImageResource(R.drawable.addr_location_site);
            holder.addrName.setText("" + address.getAddress());
            holder.addrName.setTextColor(Color.rgb(0, 0, 0));
        }
        holder.addrDetail.setText(address.getDetailName());
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        TextView addrName;
        TextView addrDetail;
    }
}
