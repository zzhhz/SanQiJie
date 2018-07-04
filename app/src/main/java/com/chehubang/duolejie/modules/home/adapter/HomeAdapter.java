package com.chehubang.duolejie.modules.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.listener.OnHomeIconClickListener;
import com.chehubang.duolejie.listener.OnItemClickListener;
import com.chehubang.duolejie.model.GoodsHotListBean;
import com.chehubang.duolejie.model.GoodsListBean;
import com.chehubang.duolejie.model.RadioListBean;
import com.chehubang.duolejie.model.ViewPagerPicBean;
import com.chehubang.duolejie.modules.gooddetails.activity.GoodsDetailsActivity;
import com.chehubang.duolejie.modules.gooddetails.adapter.HeaderViewPager;
import com.chehubang.duolejie.widget.IconGridView;
import com.chehubang.duolejie.widget.MarqueeTextView;
import com.chehubang.duolejie.widget.RecyclerViewBanner;

import java.util.ArrayList;
import java.util.List;

import common.Log.LogUtils;
import common.Utils.ToastUtils;
import common.picture.PictureUtils;

/**
 * Created by Thinkpad on 2017/12/10.
 */

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mcontext;
    private OnHomeIconClickListener onHomeIconClick;
    private OnItemClickListener clickListener;

    private final int TYPE_HEADER = 1000;
    private final int TYPE_ICON = 1001;
    private final int TYPE_GOODSLIST = 1002;
    private final int TYPE_CONTENT = 1005;
    private List<ViewPagerPicBean> mlist = new ArrayList<>();
    private List<GoodsHotListBean> mgoodhotlist = new ArrayList<>();
    private List<GoodsListBean> mgoodlist = new ArrayList<>();
    private ArrayList<RadioListBean> marqueelist = new ArrayList<>();
    private List<String> banners = new ArrayList<>();
    //显示那种类型的item


    public HomeAdapter(Context mcontext, List<ViewPagerPicBean> list, List<GoodsHotListBean> goodhotlist, List<GoodsListBean> goodlist
            ,ArrayList<RadioListBean> radioListBeans,OnItemClickListener clickListener) {
        this.mcontext = mcontext;
        this.mlist = list;
        this.mgoodlist = goodlist;
        this.mgoodhotlist = goodhotlist;
        this.marqueelist = radioListBeans;
        this.clickListener = clickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItemCount() != 0) {
            if (position == 0) {
                return TYPE_HEADER;
            } else if (position == 1) {
                return TYPE_ICON;
            }else if (position == 2){
                return TYPE_CONTENT;
            }else if (position == 3){
                return TYPE_GOODSLIST;
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(mcontext);
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case TYPE_HEADER:
                View header = mInflater.inflate(R.layout.item_home_header_viewpager, parent, false);
                holder = new HeaderViewHolder(header);
                break;
            case TYPE_ICON:
                View icon = mInflater.inflate(R.layout.item_home_icon, parent, false);
                holder = new IconViewHolder(icon);
                break;
            case TYPE_GOODSLIST:
                View goods = mInflater.inflate(R.layout.item_home_goodlist, parent,false);
                holder = new GoodsHotViewHolder(goods);
                break;
            case TYPE_CONTENT:
                View content = mInflater.inflate(R.layout.item_home_goodlist_pinpai, parent,false);
                holder = new GoodsViewHolder(content);
                break;
            default:
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            banners.clear();
            for (int i = 0;i<mlist.size();i++) {
                banners.add(mlist.get(i).getHeader());
            }
            ((HeaderViewHolder) holder).headerViewpager.isShowIndicator(true);
            ((HeaderViewHolder) holder).headerViewpager.setRvBannerData(banners);
            ((HeaderViewHolder) holder).headerViewpager.setOnSwitchRvBannerListener(new RecyclerViewBanner.OnSwitchRvBannerListener() {
                @Override
                public void switchBanner(int position, AppCompatImageView bannerView) {
                    PictureUtils.loadPicture(mcontext, banners.get(position % banners.size()),bannerView, R.drawable.pic_cycjjl);
                }
            });
            ((HeaderViewHolder) holder).headerViewpager.setOnRvBannerClickListener(new RecyclerViewBanner.OnRvBannerClickListener() {
                @Override
                public void onClick(int position) {
                    //点击事件
                    if (TextUtils.equals("y",mlist.get(position).getIs_jump())){
                        Intent intent = new Intent(mcontext, GoodsDetailsActivity.class);
                        intent.putExtra("id", mlist.get(position).getUrl());
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        mcontext.startActivity(intent);
                    }
                }
            });


        } else if (holder instanceof IconViewHolder) {
            ((IconViewHolder) holder).icon_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onHomeIconClick.OnHomeIconClick(0);
                }
            });
            ((IconViewHolder) holder).icon_car.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onHomeIconClick.OnHomeIconClick(1);
                }
            });
            ((IconViewHolder) holder).icon_charge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onHomeIconClick.OnHomeIconClick(2);
                }
            });
            ((IconViewHolder) holder).icon_service.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onHomeIconClick.OnHomeIconClick(3);
                    ToastUtils.showToast(mcontext,"该地区未开放本服务");
                }
            });

            if (marqueelist.size()>0){
                for (int i =0;i<marqueelist.size();i++){
                    View view = LayoutInflater.from(mcontext).inflate(R.layout.item_flipper_view, null);
                    TextView tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
                    TextView tv_num = (TextView) view.findViewById(R.id.tv_num);
                    TextView tv_price = (TextView) view.findViewById(R.id.tv_price);
                    tv_nickname.setText(marqueelist.get(i).getNick_name());
                    tv_num.setText("第" + marqueelist.get(i).getPeriod() +"期");
                    tv_price.setText(marqueelist.get(i).getCoupon_num()+"元代金券");
                    ((IconViewHolder) holder).tv_marquee.addView(view);
                }
            }
            // 设置文字in/out的动画效果
            ((IconViewHolder) holder).tv_marquee.startFlipping();
        }else if (holder instanceof GoodsHotViewHolder){
            GoodHotListAdapter goodHotListAdapter = new GoodHotListAdapter(mcontext, mgoodhotlist);
            ((GoodsHotViewHolder)holder).goodshotItem.setAdapter(goodHotListAdapter);
            ((GoodsHotViewHolder)holder).goodshotItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(mcontext, GoodsDetailsActivity.class);
                    intent.putExtra("id", mgoodhotlist.get(i).getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    mcontext.startActivity(intent);

                }
            });
        }else if (holder instanceof GoodsViewHolder){
            GoodListAdapter goodHotListAdapter = new GoodListAdapter(mcontext, mgoodlist);
            ((GoodsViewHolder)holder).goodsItem.setAdapter(goodHotListAdapter);
            ((GoodsViewHolder) holder).goodsItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(mcontext, GoodsDetailsActivity.class);
                    intent.putExtra("id", mgoodlist.get(i).getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    mcontext.startActivity(intent);
                }
            });
        }
    }

    public void addOnHomeIconClickListener(OnHomeIconClickListener clickListener) {
        this.onHomeIconClick = clickListener;
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        RecyclerViewBanner headerViewpager;

        HeaderViewHolder(View itemView) {
            super(itemView);
            headerViewpager = (RecyclerViewBanner) itemView.findViewById(R.id.vp_home_header);
        }
    }

    public class IconViewHolder extends RecyclerView.ViewHolder {
        TextView icon_all, icon_car, icon_charge, icon_service;
        ViewFlipper tv_marquee;

        IconViewHolder(View itemView) {
            super(itemView);
            icon_all = (TextView) itemView.findViewById(R.id.tv_home_icon_all);
            icon_car = (TextView) itemView.findViewById(R.id.tv_home_icon_car);
            icon_charge = (TextView) itemView.findViewById(R.id.tv_home_icon_charge);
            icon_service = (TextView) itemView.findViewById(R.id.tv_home_icon_service);
            tv_marquee = (ViewFlipper) itemView.findViewById(R.id.tv_marquee);
        }
    }

    public class GoodsHotViewHolder extends RecyclerView.ViewHolder {
        IconGridView goodshotItem;

        GoodsHotViewHolder(View itemView) {
            super(itemView);
            goodshotItem = (IconGridView) itemView.findViewById(R.id.gv_home_goods);

        }
    }

    public class GoodsViewHolder extends RecyclerView.ViewHolder {
        IconGridView goodsItem;

        GoodsViewHolder(View itemView) {
            super(itemView);
            goodsItem = (IconGridView) itemView.findViewById(R.id.gv_home_goods);

        }
    }
}
