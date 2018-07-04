package com.chehubang.duolejie.modules.gift.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.model.DrawResult;
import com.chehubang.duolejie.modules.gift.activity.DrawResultDetailActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.picture.PictureUtils;

/**
 * Created by user on 2018/1/31.
 *
 * @date: 2018/1/31
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @description:
 * @update 修改中奖状态（进行中，倒计时，已揭晓）
 */
public class DrawResultAdapter extends BaseAdapter {

    public List<DrawResult> dataList;
    private Context mContext;
    private SimpleDateFormat mDateFormat;

    public DrawResultAdapter(Context context) {
        mContext = context;
        dataList = new ArrayList<>();
        mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public void addAll(List<DrawResult> data) {
        dataList.addAll(data);
    }

    public void clear() {
        dataList.clear();
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public DrawResult getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_draw_result, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final DrawResult item = getItem(position);
        PictureUtils.loadPicture(mContext, item.getBrand_header(), holder.head, R.drawable.pic_cycjjl);
        holder.title.setText(item.getBrand_name());
        holder.count.setText("第" + item.getPeriod() + "期   参与券" + item.getNeed_lettory_num() + "张");
        try {
            holder.time.setText(mDateFormat.format(new Date(Long.parseLong(item.getCreate_time()))));
        } catch (Exception ex) {
            holder.time.setText("");
        }

        if (TextUtils.isEmpty(item.getCoupon_status())) {
            if ("y".equalsIgnoreCase(item.getStatus())) {
                holder.result.setText("中奖：" + item.getCoupon_num() + " 元代金券");
            } else {
                holder.result.setText("未中奖");
            }
        } else {
            if ("进行中".equals(item.getCoupon_status())) {
                holder.result.setText("未开奖");
            } else if ("倒计时".equals(item.getCoupon_status())) {
                holder.result.setText("开奖中");
            } else {
                if ("y".equalsIgnoreCase(item.getStatus())) {
                    holder.result.setText("中奖：" + item.getCoupon_num() + " 元代金券");
                } else {
                    holder.result.setText("未中奖");
                }
            }
        }

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DrawResultDetailActivity.class);
                intent.putExtra("coupon_id", item.getId());
                intent.putExtra("period", item.getPeriod());

                mContext.startActivity(intent);
            }
        });


        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.head)
        ImageView head;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.count)
        TextView count;
        @BindView(R.id.result)
        TextView result;
        @BindView(R.id.time)
        TextView time;
        View item;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
            item = itemView;
        }
    }
}
