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
import com.chehubang.duolejie.modules.chargecenter.activity.CashierCenterActivity;
import com.chehubang.duolejie.modules.express.activity.ExpressActivity;

import java.util.List;

import common.picture.PictureUtils;

/**
 * Created by ZZH on 2018/1/30
 *
 * @date: 2018/1/30 上午9:31
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @Description: 全部订单适配器
 */
public class OrderAdapter extends BaseAdapter {

    private Context mContext;
    private List<OrderBean> mlist;
    private OnClickConfirmListener mConfirmListener;

    public OrderAdapter(Context context, List<OrderBean> list) {
        mContext = context;
        mlist = list;
    }

    public void setConfirmListener(OnClickConfirmListener confirmListener) {
        mConfirmListener = confirmListener;
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
            convertView = View.inflate(mContext, R.layout.item_order, null);
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
            viewHolder.checkStatus = (TextView) convertView.findViewById(R.id.tv_order_item_check_delivery);
            viewHolder.confirm = (TextView) convertView.findViewById(R.id.tv_order_item_confirm_delivery);
            viewHolder.itemView = convertView.findViewById(R.id.itemView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final OrderBean bean = mlist.get(position);
        viewHolder.shopName.setText(bean.getBrand_name());
        viewHolder.shippingStatus.setText(bean.getStatus());
        PictureUtils.loadPicture(mContext, bean.getGood_header(), viewHolder.goodsPic, R.drawable.pic_cycjjl);
        viewHolder.goodsTitle.setText(bean.getGood_name());
        viewHolder.goodsPrice.setText("￥" + bean.getGood_price());
        viewHolder.goodsType.setText(bean.getGoods_color() + " " + bean.getGoods_spec() + "码");
        viewHolder.goodsPriceOri.setText("￥" + bean.getGood_single_price());
        viewHolder.goodsNum.setText("共" + bean.getBuy_nums() + "件商品");
        viewHolder.goodsTotal.setText("合计：￥" + bean.getChild_order_price());
        viewHolder.goodsDeliveryFee.setText("（含运费：￥" + bean.getChild_freight() + ")");

        if (bean.getStatus().equals("已发货")) {
            viewHolder.checkStatus.setVisibility(View.VISIBLE);
            viewHolder.confirm.setVisibility(View.VISIBLE);
            viewHolder.confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mConfirmListener != null) {
                        mConfirmListener.onClickConfirm(bean.getChild_order_id());
                    }
                }
            });
        } else {
            viewHolder.checkStatus.setVisibility(View.GONE);
            viewHolder.confirm.setVisibility(View.GONE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("待支付".equals(bean.getStatus())) {
                    Intent intent = new Intent(mContext, CashierCenterActivity.class);
                    intent.putExtra("id", bean.getParent_order_id());
                    mContext.startActivity(intent);
                }
            }
        });

        viewHolder.checkStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpressActivity.open(mContext, bean.getChild_order_id());
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
        View itemView;
    }

    public interface OnClickConfirmListener {
        /**
         * 子类order id
         *
         * @param child_order_id
         */
        void onClickConfirm(String child_order_id);
    }
}
