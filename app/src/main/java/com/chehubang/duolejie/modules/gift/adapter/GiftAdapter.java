package com.chehubang.duolejie.modules.gift.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.model.GiftListBean;
import com.chehubang.duolejie.widget.QuantityView;

import java.util.ArrayList;
import java.util.List;

import common.Utils.ToastUtils;
import common.picture.PictureUtils;

/**
 * Created by fuyi on 2017/12/20.
 */

public class GiftAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mcontext;

    private OnItemClickLitener mOnItemClickLitener;
    private final int TYPE_HEADER = 0;
    private final int TYPE_NORMAL = 1;
    private List<GiftListBean> mlist = new ArrayList<>();
    private View mHeaderView;
    private int num = 1;
    private String mIsNewUser;

    //显示那种类型的item
    public GiftAdapter(Context mcontext, List<GiftListBean> mList) {
        this.mcontext = mcontext;
        this.mlist = mList;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null) return TYPE_NORMAL;
        if (position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? mlist.size() : mlist.size() + 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) return new GiftViewHolder(mHeaderView);
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gift2, null);
        return new GiftViewHolder(layout);
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == TYPE_HEADER) return;
        final int pos = getRealPosition(holder);

        final GiftListBean bean = mlist.get(pos);
        final GiftViewHolder giftViewHolder = (GiftViewHolder) holder;
        if (pos == 0) {
            ViewGroup.LayoutParams params = giftViewHolder.v_s.getLayoutParams();
            params.height = 2;
            giftViewHolder.v_s.setLayoutParams(params);
        } else {
            ViewGroup.LayoutParams params = giftViewHolder.v_s.getLayoutParams();
            params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) 5, mcontext.getResources().getDisplayMetrics());
            giftViewHolder.v_s.setLayoutParams(params);
        }
        giftViewHolder.id_num.setText(bean.getNeed_lettory_num() + "卷区");
        giftViewHolder.tv_item_gift_passengers.setText("总需：" + bean.getNeed_people() + "人/" + bean.getNeed_lettory_num() + "券");
        giftViewHolder.tv_gift_title.setText(bean.getCoupon_info());
        // final String coupon_id = mUserList.get(pos).getCoupon_id();
        giftViewHolder.tv_gift_no.setText("期号：" + bean.getPeriod());
        int needpeople = Integer.parseInt(bean.getNeed_people());
        int Now_people = Integer.parseInt(bean.getNow_people());
        int i = needpeople - Now_people;
        giftViewHolder.needpeople.setText("还需" + i + "人次");
        PictureUtils.loadPicture(mcontext, bean.getBrand_header()
                , giftViewHolder.iv_item_gift, R.drawable.pic_cycjjl);
        // final String coupon_id = mUserList.get(pos).getCoupon_id();
        giftViewHolder.quantityView.setMinQuantity(1);
        giftViewHolder.progress.setProgress(bean.getProgress());
        giftViewHolder.progresstext.setText(bean.getProgress() + "%");

        final String id = bean.getId();
        if (holder instanceof GiftViewHolder) {
            if (mOnItemClickLitener == null) return;
            giftViewHolder.iv_item_gift.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(pos);
                }
            });

            giftViewHolder.btn_item_gift_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = giftViewHolder.quantityView.getQuantity();
                    String need_people = bean.getNeed_people();
                    if (quantity > Integer.parseInt(need_people)) {
                        ToastUtils.centerToastWhite(mcontext, "超过抽奖人数，请重新选择参与人数");
                    } else {
                        mOnItemClickLitener.onConfirmClick(pos, String.valueOf(quantity));
                    }
                }
            });
        }
        if (TextUtils.equals("y", bean.getIs_activity())) {
            giftViewHolder.quantityView.setVisibility(View.GONE);
            giftViewHolder.id_num.setVisibility(View.VISIBLE);
            giftViewHolder.id_num.setVisibility(View.GONE);

            if (TextUtils.equals(mIsNewUser, "y")) {
                giftViewHolder.tv_yaoqingma.setVisibility(View.VISIBLE);
            } else {
                giftViewHolder.tv_yaoqingma.setVisibility(View.GONE);
            }
        } else {
            giftViewHolder.quantityView.setVisibility(View.VISIBLE);
            giftViewHolder.id_num.setVisibility(View.GONE);
            giftViewHolder.tv_yaoqingma.setVisibility(View.GONE);
        }
        giftViewHolder.tv_yaoqingma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInputListener != null) {
                    mInputListener.onInputCode();
                }

            }
        });
    }

    public void setIsNewUser(String isNewUser) {
        mIsNewUser = isNewUser;
    }

    public class GiftViewHolder extends RecyclerView.ViewHolder {
        private TextView id_num, tv_gift_no, tv_gift_title;
        private TextView tv_item_gift_passengers, progresstext, needpeople;
        private LinearLayout ll_item;
        private ImageView iv_item_gift;
        private Button btn_item_gift_confirm;
        private QuantityView quantityView;
        private ProgressBar progress;
        private View v_s;
        private View tv_yaoqingma;


        public GiftViewHolder(View view) {
            super(view);
            v_s = view.findViewById(R.id.v_s);
            id_num = (TextView) view.findViewById(R.id.id_num);
            quantityView = (QuantityView) view.findViewById(R.id.amount_view);
            tv_item_gift_passengers = (TextView) view.findViewById(R.id.tv_item_gift_passengers);
            tv_gift_no = (TextView) view.findViewById(R.id.item_tv_gift_no);
            tv_gift_title = (TextView) view.findViewById(R.id.tv_item_gift_title);
            ll_item = (LinearLayout) view.findViewById(R.id.ll_item);
            iv_item_gift = (ImageView) view.findViewById(R.id.iv_item_gift);
            btn_item_gift_confirm = (Button) view.findViewById(R.id.btn_item_gift_confirm);
            progress = (ProgressBar) view.findViewById(R.id.pb_gift_progressbar);
            progresstext = (TextView) view.findViewById(R.id.item_tv_pregress);
            needpeople = (TextView) view.findViewById(R.id.tv_need_people);
            tv_yaoqingma = view.findViewById(R.id.tv_yaoqingma);
        }
    }


    public interface OnItemClickLitener {
        void onItemClick(int position);

        void onConfirmClick(int position, String num);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    OnItemClickInputListener mInputListener;

    public void setInputListener(OnItemClickInputListener inputListener) {
        mInputListener = inputListener;
    }

    public interface OnItemClickInputListener {
        void onInputCode();
    }

}
