package com.chehubang.duolejie.modules.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.model.BannerBean;
import com.chehubang.duolejie.model.MyCouponListBean;
import com.chehubang.duolejie.modules.home.activity.WebviewActivity;
import com.chehubang.duolejie.widget.QuantityView;
import com.chehubang.duolejie.widget.RecyclerViewBanner;

import java.util.ArrayList;
import java.util.List;

import common.Utils.ToastUtils;
import common.picture.PictureUtils;

/**
 * Created by Thinkpad on 2018/1/15.
 */

public class GiftTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MyCouponListBean> mlist = new ArrayList<>();
    private Context mcontext;
    private final int TYPE_NEW = 10000;
    private final int TYPE_HEADER = 10002;
    private final int TYPE_NORMAL = 10001;
    private int headerSize = 0;
    private List<BannerBean> mBannerBeans;

    public GiftTypeAdapter(Context context, List<MyCouponListBean> list) {
        this.mcontext = context;
        this.mlist = list;
    }

    public int getHeaderSize() {
        return headerSize;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItemCount() != 0) {

            if (headerSize != 0) {
                if (position == 0) {
                    return TYPE_HEADER;
                } else if (position == headerSize) {
                    return TYPE_NEW;
                } else {
                    return TYPE_NORMAL;
                }
            } else {
                if (position == 0) {
                    return TYPE_NEW;
                } else {
                    return TYPE_NORMAL;
                }
            }
        }
        return TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(mcontext);
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case TYPE_NEW:
                View header = mInflater.inflate(R.layout.item_gift_header, parent, false);
                holder = new HeaderHolder(header);
                break;
            case TYPE_NORMAL:
                View icon = mInflater.inflate(R.layout.item_gift, parent, false);
                holder = new GiftViewHolder(icon);
                break;
            case TYPE_HEADER:
                View rule = mInflater.inflate(R.layout.item_recycler_banner, parent, false);
                holder = new RuleHolder(rule);
            default:
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderHolder) {
            final MyCouponListBean bean = mlist.get(0);
            HeaderHolder headerHolder = (HeaderHolder) holder;
            headerHolder.giftNo.setText("期号：" + bean.getPeriod());
            headerHolder.title.setText(bean.getCoupon_info());
            headerHolder.totalPassager.setText("总需：" + bean.getNeed_people() + "人/" + bean.getNeed_lettory_num() + "券");
            headerHolder.tvprogress.setText(bean.getProgress() + "%");
            int needpeople = Integer.parseInt(bean.getNeed_people());
            int Now_people = Integer.parseInt(bean.getNow_people());
            int i = needpeople - Now_people;
            headerHolder.needPeople.setText("还需" + i + "人次");
            String brand_header = bean.getBrand_header();
            Log.d("-----------------",brand_header+", "+headerSize);
            PictureUtils.loadPicture(mcontext, brand_header, headerHolder.myHeader, R.drawable.pic_cycjjl);
            headerHolder.progress.setProgress(bean.getProgress());
            headerHolder.confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickLitener != null) {
                        mOnItemClickLitener.onConfirmClick(0, "1");
                    }
                }
            });

        } else if (holder instanceof GiftViewHolder) {
            final MyCouponListBean bean = mlist.get(position - headerSize);
            final GiftViewHolder giftViewHolder = (GiftViewHolder) holder;
            giftViewHolder.id_num.setText(bean.getNeed_lettory_num() + "卷区");
            giftViewHolder.tv_item_gift_passengers.setText("总需：" + bean.getNeed_people() + "人/" + bean.getNeed_lettory_num() + "券");
            giftViewHolder.tv_gift_title.setText(bean.getCoupon_info());
            giftViewHolder.tv_gift_no.setText("期号：" + bean.getPeriod());
            int needpeople = Integer.parseInt(bean.getNeed_people());
            int Now_people = Integer.parseInt(bean.getNow_people());
            int i = needpeople - Now_people;
            giftViewHolder.needpeople.setText("还需" + i + "人次");
            String brand_header = bean.getBrand_header();
            Log.d("-------",brand_header+", "+(position - headerSize));
            PictureUtils.loadPicture(mcontext, brand_header
                    , giftViewHolder.iv_item_gift, R.drawable.pic_cycjjl);
            giftViewHolder.quantityView.setMinQuantity(1);
            giftViewHolder.progress.setProgress(bean.getProgress());
            giftViewHolder.progresstext.setText(bean.getProgress() + "%");
            giftViewHolder.btn_item_gift_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickLitener != null) {
                        int quantity = giftViewHolder.quantityView.getQuantity();
                        String need_people = bean.getNeed_people();
                        if (quantity > Integer.parseInt(need_people)) {
                            ToastUtils.centerToastWhite(mcontext, "超过抽奖人数，请重新选择参与人数");
                        } else {
                            mOnItemClickLitener.onConfirmClick(position - headerSize, String.valueOf(quantity));
                        }
                    }
                }
            });
            giftViewHolder.iv_item_gift.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickLitener != null) {
                        mOnItemClickLitener.onItemClick(position);
                    }
                }
            });
        } else if (holder instanceof RuleHolder) {
            RuleHolder ruleHolder = (RuleHolder) holder;
            ruleHolder.mViewBanner.setRvAutoPlaying(false);
            ruleHolder.mViewBanner.setRvBannerData(mBannerBeans);
            ruleHolder.mViewBanner.isShowIndicator(false);
            ruleHolder.mViewBanner.setOnRvBannerClickListener(new RecyclerViewBanner.OnRvBannerClickListener() {
                @Override
                public void onClick(int position) {
                    BannerBean bannerBean = mBannerBeans.get(position);
                    bannerBean.getIs_jump();
                    if ("y".equals(bannerBean.getIs_jump())) {
                        Intent intent7 = new Intent(mcontext, WebviewActivity.class);
                        intent7.putExtra("url", "http://39.107.14.118/appInterface/aboutUsContentInfo.jhtml?id=15");  //外网版本
                        // intent7.putExtra("url","http://192.168.3.25:8090/GetTreasureAppJinDong/appInterface/aboutUsContentInfo.jhtml?id=1&is_show_message=y");
                        intent7.putExtra("title", "抽奖规则");
                        mcontext.startActivity(intent7);
                    }
                }
            });
            ruleHolder.mViewBanner.setOnSwitchRvBannerListener(new RecyclerViewBanner.OnSwitchRvBannerListener() {
                @Override
                public void switchBanner(int position, AppCompatImageView bannerView) {
                    bannerView.setScaleType(ImageView.ScaleType.FIT_XY);
                    PictureUtils.loadPicture(mcontext, mBannerBeans.get(position).getHeader(), bannerView, R.drawable.icon_logo);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mlist.size() + headerSize;
    }

    public void addHeaderRule(List<BannerBean> advertisementList) {
        this.mBannerBeans = advertisementList;
        headerSize = 1;
    }

    private class GiftViewHolder extends RecyclerView.ViewHolder {
        private TextView id_num, tv_gift_no, tv_gift_title;
        private TextView tv_item_gift_passengers, progresstext, needpeople;
        private ImageView iv_item_gift;
        private Button btn_item_gift_confirm;
        private QuantityView quantityView;
        private ProgressBar progress;

        public GiftViewHolder(View view) {
            super(view);
            id_num = (TextView) view.findViewById(R.id.id_num);
            quantityView = (QuantityView) view.findViewById(R.id.amount_view);
            tv_item_gift_passengers = (TextView) view.findViewById(R.id.tv_item_gift_passengers);
            tv_gift_no = (TextView) view.findViewById(R.id.item_tv_gift_no);
            tv_gift_title = (TextView) view.findViewById(R.id.tv_item_gift_title);
            iv_item_gift = (ImageView) view.findViewById(R.id.iv_item_gift);
            btn_item_gift_confirm = (Button) view.findViewById(R.id.btn_item_gift_confirm);
            progress = (ProgressBar) view.findViewById(R.id.pb_gift_progressbar);
            progresstext = (TextView) view.findViewById(R.id.item_tv_pregress);
            needpeople = (TextView) view.findViewById(R.id.tv_need_people);
        }
    }


    private class HeaderHolder extends RecyclerView.ViewHolder {

        private TextView giftNo, title, totalPassager, tvprogress, needPeople;
        private ImageView myHeader;
        private ProgressBar progress;
        private Button confirm;

        public HeaderHolder(View itemView) {
            super(itemView);
            giftNo = (TextView) itemView.findViewById(R.id.item_tv_my_gift_no);
            myHeader = (ImageView) itemView.findViewById(R.id.iv_item_my_gift);
            title = (TextView) itemView.findViewById(R.id.tv_item_my_gift_title);
            totalPassager = (TextView) itemView.findViewById(R.id.tv_item_my_gift_passengers);
            progress = (ProgressBar) itemView.findViewById(R.id.pb_my_gift_progressbar);
            tvprogress = (TextView) itemView.findViewById(R.id.item_my_tv_pregress);
            needPeople = (TextView) itemView.findViewById(R.id.tv_my_need_people);
            confirm = (Button) itemView.findViewById(R.id.btn_item_my_gift_confirm);
        }
    }

    private class RuleHolder extends RecyclerView.ViewHolder {

        private RecyclerViewBanner mViewBanner;

        public RuleHolder(View itemView) {
            super(itemView);
            mViewBanner = (RecyclerViewBanner) itemView.findViewById(R.id.rvb_banner_image_view_id);
        }
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener onItemClickLitener) {
        mOnItemClickLitener = onItemClickLitener;
    }

    public interface OnItemClickLitener {
        void onItemClick(int position);

        void onConfirmClick(int position, String num);
    }
}
