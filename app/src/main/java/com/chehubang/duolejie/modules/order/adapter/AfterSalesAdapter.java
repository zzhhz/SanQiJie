package com.chehubang.duolejie.modules.order.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.model.OrderBean;
import com.chehubang.duolejie.modules.order.activity.ChangeOrderActivity;
import com.chehubang.duolejie.modules.order.activity.ReturnOrderActivity;

import java.util.List;

import common.picture.PictureUtils;

/**
 * Created by Thinkpad on 2017/12/25.
 */

public class AfterSalesAdapter extends BaseAdapter{

    private Context mcontext;
    private List<OrderBean> mlist;

    public AfterSalesAdapter(Context context, List<OrderBean> list) {
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
            convertView = View.inflate(mcontext, R.layout.item_after_sales, null);
            viewHolder.shopName = (TextView) convertView.findViewById(R.id.tv_order_item_shop_name);
            viewHolder.shippingStatus = (TextView) convertView.findViewById(R.id.tv_order_item_shipping_statue);
            viewHolder.goodsPic = (ImageView) convertView.findViewById(R.id.iv_order_item_goods_pic);
            viewHolder.goodsTitle = (TextView) convertView.findViewById(R.id.tv_order_item_goods_title);
            viewHolder.goodsPrice = (TextView) convertView.findViewById(R.id.tv_order_item_goods_price);
            viewHolder.goodsType = (TextView) convertView.findViewById(R.id.tv_order_item_goods_type);
            viewHolder.goodsPriceOri = (TextView) convertView.findViewById(R.id.tv_order_item_goods_price_ori);
            viewHolder.goodsNum = (TextView) convertView.findViewById(R.id.tv_order_item_goods_num);
            viewHolder.goodsTotal = (TextView) convertView.findViewById(R.id.tv_order_item_goods_total);
            viewHolder.goodsDeliveryFee = (TextView) convertView.findViewById(R.id.tv_order_item_goods_delivery_fee);
            viewHolder.checkStatus = (TextView) convertView.findViewById(R.id.tv_order_item_change);
            viewHolder.confirm = (TextView) convertView.findViewById(R.id.tv_order_item_return);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final OrderBean orderBean = mlist.get(position);
        viewHolder.shopName.setText(orderBean.getBrand_name());
        viewHolder.shippingStatus.setText(orderBean.getStatus());
        PictureUtils.loadPicture(mcontext, orderBean.getGood_header(),viewHolder.goodsPic,R.drawable.pic_cycjjl);
        viewHolder.goodsTitle.setText(orderBean.getGood_name());
        viewHolder.goodsPrice.setText("￥" + orderBean.getGood_price());
        viewHolder.goodsType.setText(orderBean.getGoods_color() + " " + orderBean.getGoods_spec()+"码");
        viewHolder.goodsPriceOri.setText("￥" + orderBean.getGood_single_price());
        viewHolder.goodsNum.setText("共" + orderBean.getBuy_nums()+"件商品");
        viewHolder.goodsTotal.setText("合计：￥" + orderBean.getOrder_price());
        viewHolder.goodsDeliveryFee.setText("（含运费：￥" + orderBean.getFreight()+")");

       /* if (mlist.get(position).getStatus().equals("已发货")) {
            viewHolder.checkStatus.setVisibility(View.VISIBLE);
            viewHolder.confirm.setVisibility(View.VISIBLE);
        }else {
            viewHolder.checkStatus.setVisibility(View.GONE);
            viewHolder.confirm.setVisibility(View.GONE);
        }*/
        viewHolder.checkStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext, ChangeOrderActivity.class);
                mcontext. startActivity(intent);
            }
        });
        viewHolder.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext, ReturnOrderActivity.class);
                intent.putExtra("id", orderBean.getChild_order_id());
                intent.putExtra("order", orderBean);
                mcontext.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView shopName;
        TextView shippingStatus;
        TextView goodsTitle;
        TextView goodsPrice;
        TextView goodsPriceOri;
        TextView goodsType;
        TextView goodsNum;
        TextView goodsDeliveryFee;
        TextView goodsTotal;
        TextView checkStatus;
        TextView confirm;
        ImageView goodsPic;
    }
}
