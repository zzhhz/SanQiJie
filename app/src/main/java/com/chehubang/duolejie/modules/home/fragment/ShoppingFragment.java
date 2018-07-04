package com.chehubang.duolejie.modules.home.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseFragment;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.listener.OnButtonClick;
import com.chehubang.duolejie.listener.OnItemClickListener;
import com.chehubang.duolejie.model.ShoppingListBean;
import com.chehubang.duolejie.modules.chargecenter.activity.ChargeOrderActivity;
import com.chehubang.duolejie.modules.home.activity.MainActivity;
import com.chehubang.duolejie.modules.home.presenter.ShoppingPresenter;
import com.chehubang.duolejie.modules.home.viewholder.ShoppingViewHolder;
import com.chehubang.duolejie.widget.EmptyRecyclerView;
import com.chehubang.duolejie.widget.QuantityView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import common.Utils.JSONUtils;
import common.Utils.ToastUtils;
import common.http.RequestResult;
import common.mvp.activity.MainView;
import common.picture.PictureUtils;

/**
 * Created by Thinkpad on 2017/12/9.
 */

public class ShoppingFragment extends BaseFragment<ShoppingPresenter> implements MainView, View.OnClickListener, OnItemClickListener, OnButtonClick, CheckBox.OnCheckedChangeListener {

    private EmptyRecyclerView shoppinglistview;
    private SmartRefreshLayout refreshLayout;
    private List<ShoppingListBean> shoppinglist = new ArrayList();
    private ShoppingAdapter shoppingAdapter;
    private TextView totalAmount;
    private OnButtonClick mclick;
    private LinearLayout ln_shopping;
    private double mtotalAmount = 0;
    private String goods_id = "";
    private String goods_num = "";
    private String goods_color_id = "";
    private String goods_spec_id = "";
    private CheckBox selectAll;
    private String good_price = "";
    private String good_color = "";
    private String good_spec = "";
    private int deleteposition = 0;
    int mPage = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping, container, false);
        shoppinglistview = (EmptyRecyclerView) view.findViewById(R.id.rcy_shoppinglist);
        refreshLayout = (SmartRefreshLayout) view.findViewById(R.id.srf_shopping);
        totalAmount = (TextView) view.findViewById(R.id.tv_shopping_total_amount);
        TextView buy = (TextView) view.findViewById(R.id.tv_shopping_buy);
        selectAll = (CheckBox) view.findViewById(R.id.cb_shpping_select_all);
        selectAll.setOnCheckedChangeListener(this);
        Button button = (Button) view.findViewById(R.id.btn_shopping);
        ln_shopping = (LinearLayout) view.findViewById(R.id.ln_shopping);
        shoppingAdapter = new ShoppingAdapter(getActivity(), this);
        shoppinglistview.setAdapter(shoppingAdapter);
        if (shoppinglist.size() > 0) {
            ln_shopping.setVisibility(View.VISIBLE);
        } else {
            ln_shopping.setVisibility(View.GONE);
        }
        shoppinglistview.setLayoutManager(new LinearLayoutManager(getActivity()));
        LinearLayout emptyview = (LinearLayout) view.findViewById(R.id.ll_empty_view);
        shoppinglistview.setEmptyView(emptyview);

        buy.setOnClickListener(this);
        button.setOnClickListener(this);
       /* UserInfo.getInstance().getIdCache(getActivity());
        mvpPresenter.getShoppingList(ACTION_DEFAULT + 1, UserInfo.getInstance().getId());*/
        //设置 Footer 为 球脉冲

        UserInfo.getInstance().getIdCache(getActivity());
        if (!TextUtils.isEmpty(UserInfo.getInstance().getId())) {
            mvpPresenter.getShoppingList(ACTION_DEFAULT + 1, UserInfo.getInstance().getId(), mPage);
        } else {
            shoppinglist.clear();
            shoppingAdapter.notifyDataSetChanged();
        }

        refreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));
        ((MainActivity) getActivity()).addButtonClickListener(this);
        shoppinglistview.setOnItemClickListener(new com.chehubang.duolejie.widget.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }

            @Override
            public void onDeleteClick(int position) {
                deleteposition = position;
                mvpPresenter.deleteItem(ACTION_DEFAULT + 2, shoppinglist.get(position).getId());
            }
        });
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_shopping_buy:
                if (shoppinglist.size() > 0) {
                    //todo
                    goToChargeCenter();
                    if (TextUtils.isEmpty(goods_id) && TextUtils.isEmpty(goods_num)
                            && TextUtils.isEmpty(goods_color_id) && TextUtils.isEmpty(goods_spec_id)
                            && TextUtils.isEmpty(good_price) && TextUtils.isEmpty(good_color) && TextUtils.isEmpty(good_spec)) {
                        ToastUtils.centerToastWhite(getActivity(), "您还未选择宝贝哦！");
                    } else {
                        Intent intent = new Intent(getActivity(), ChargeOrderActivity.class);
                        intent.putExtra("id", goods_id);
                        intent.putExtra("colorid", goods_color_id);
                        intent.putExtra("specid", goods_spec_id);
                        intent.putExtra("num", goods_num);
                        intent.putExtra("is_shop_cart", "y");
                        intent.putExtra("price", good_price);
                        intent.putExtra("color", good_color);
                        intent.putExtra("spec", good_spec);
                        startActivity(intent);
                    }
                } else {
                    ToastUtils.centerToastWhite(getActivity(), "您还未选择宝贝哦！");
                }
                break;
            case R.id.btn_shopping:
                if (mclick != null) {
                    mclick.OnHomeButtonClick(0);
                }
                break;
            default:
                break;
        }
    }

    public void goToChargeCenter() {
        mtotalAmount = 0;
        goods_id = "";
        goods_num = "";
        goods_color_id = "";
        goods_spec_id = "";
        good_color = "";
        good_spec = "";
        for (int i = 0; i < shoppinglist.size(); i++) {
            if (shoppinglist.get(i).isIs_shop_car_pay()) {
                int goods_buy_num = shoppinglist.get(i).getGoods_buy_num();
                String goodsid = shoppinglist.get(i).getGoods_id();
                String goodscolor_id = shoppinglist.get(i).getGoods_color_id();
                String goodsspec_id = shoppinglist.get(i).getGoods_spec_id();
                String goodprice = shoppinglist.get(i).getGood_price();
                String goodsspec = shoppinglist.get(i).getGoods_spec();
                String goodscolor = shoppinglist.get(i).getGoods_color();
                goods_id = goods_id + goodsid + ",";
                goods_num = goods_num + goods_buy_num + ",";
                if (goods_color_id != "") {
                    goods_color_id = goods_color_id + goodscolor_id + ",";
                } else {
                    goods_color_id = goods_color_id + goodscolor_id;
                }
                goods_spec_id = goods_spec_id + goodsspec_id + ",";
                good_price = good_price + goodprice + ",";
                if (goodscolor != " ") {
                    good_color = good_color + goodscolor + ",";
                } else {
                    good_color = good_color + goodscolor;
                }
                good_spec = good_spec + goodsspec + ",";
            }
        }


    }

    public void showTotalAmount() {
        if (shoppinglist.size() > 0) {
            mtotalAmount = 0;
            for (int i = 0; i < shoppinglist.size(); i++) {
                if (shoppinglist.get(i).isIs_shop_car_pay()) {
                    int goods_buy_num = shoppinglist.get(i).getGoods_buy_num();
                    String good_price = shoppinglist.get(i).getGood_price();
                    mtotalAmount = mtotalAmount + goods_buy_num * Double.parseDouble(good_price);
                }
            }
            totalAmount.setText("合计：￥" + mtotalAmount);
        }
    }

    public void setClickListener(OnButtonClick clickListener) {
        this.mclick = clickListener;
    }

    @Override
    public void getDataSuccess(Object model, int action) {
        if (action == ACTION_DEFAULT + 1) {
            if (model != null) {
                try {
                    JSONObject jsonObject = new JSONObject((String) model);
                    Log.d("-----", "getDataSuccess: " + jsonObject);
                    JSONArray shopCartList = jsonObject.getJSONArray("shopCartList");
                    String all_price = jsonObject.getString("all_price");
                    shoppinglist.clear();
                    for (int i = 0; i < shopCartList.length(); i++) {
                        JSONObject o1 = (JSONObject) shopCartList.get(i);
                        ShoppingListBean goodsHotListBean = JSONUtils.GsonToBean(o1.toString(), ShoppingListBean.class);
                        shoppinglist.add(goodsHotListBean);
                    }
                    shoppingAdapter.notifyDataSetChanged();
                    //   totalAmount.setText("合计：￥" + all_price);
                    totalAmount.setText("合计：￥" + mtotalAmount);
                    if (shoppinglist.size() > 0) {
                        ln_shopping.setVisibility(View.VISIBLE);
                    } else {
                        ln_shopping.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (action == ACTION_DEFAULT + 2) {
            if (model != null) {
                RequestResult data = (RequestResult) model;
                String status = data.getStatus();
                if (TextUtils.equals(Constant.request_success, status)) {
                    shoppingAdapter.removeItem(deleteposition);
                    ToastUtils.centerToastWhite(getActivity(), "修改成功");
                    mvpPresenter.getShoppingList(ACTION_DEFAULT + 1, UserInfo.getInstance().getId(), mPage);
                } else {
                    ToastUtils.centerToastWhite(getActivity(), "修改失败");
                }
            }
        }
    }

    @Override
    public void getDataFail(String msg, int action) {
        Log.d("-------", "getDataFail: " + msg + ",  " + action);
    }

    @Override
    protected ShoppingPresenter createPresenter() {
        return new ShoppingPresenter(this);
    }

    @Override
    public void onItemClick(View v, int position) {
        //TODO
    }

    @Override
    public void OnHomeButtonClick(int position) {
        if (position == 3) {
            UserInfo.getInstance().getIdCache(getActivity());
            if (!TextUtils.isEmpty(UserInfo.getInstance().getId())) {
                mvpPresenter.getShoppingList(ACTION_DEFAULT + 1, UserInfo.getInstance().getId(), mPage);
            } else {
                shoppinglist.clear();
                shoppingAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (shoppinglist.size() > 0) {
            for (int i = 0; i < shoppinglist.size(); i++) {
                ShoppingListBean shoppingListBean = shoppinglist.get(i);
                shoppingListBean.setIs_shop_car_pay(b);
            }
            shoppingAdapter.notifyDataSetChanged();
            showTotalAmount();
        }
    }


    public class ShoppingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context mcontext;
        private OnItemClickListener clickListener;

        public ShoppingAdapter(Context context, OnItemClickListener clickListener) {
            this.mcontext = context;
            this.clickListener = clickListener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(mcontext);
            View view = mInflater.inflate(R.layout.item_home_shoppinglist, parent, false);
            ShoppingViewHolder holder = new ShoppingViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
           /* ((ShoppingViewHolder) holder).item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(v, position);
                }
            });*/
            if (shoppinglist.size() > 0) {
                PictureUtils.loadPicture(mcontext, shoppinglist.get(position).getGood_header(), ((ShoppingViewHolder) holder).pic, R.drawable.pic_cycjjl);
                ((ShoppingViewHolder) holder).name1.setText(shoppinglist.get(position).getGood_name());
                ((ShoppingViewHolder) holder).price.setText(shoppinglist.get(position).getGood_price());
                ((ShoppingViewHolder) holder).amount.setQuantity(shoppinglist.get(position).getGoods_buy_num());
                ((ShoppingViewHolder) holder).amount.setMinQuantity(1);
                ((ShoppingViewHolder) holder).type.setText(shoppinglist.get(position).getGoods_color() + ":" + shoppinglist.get(position).getGoods_spec());
                ((ShoppingViewHolder) holder).select.setChecked(false);
                ((ShoppingViewHolder) holder).select.setChecked(shoppinglist.get(position).isIs_shop_car_pay());
                ((ShoppingViewHolder) holder).select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        ShoppingListBean shoppingListBean = shoppinglist.get(position);
                        shoppingListBean.setIs_shop_car_pay(b);
                        showTotalAmount();
                    }
                });

                ((ShoppingViewHolder) holder).amount.setOnQuantityChangeListener(new QuantityView.OnQuantityChangeListener() {
                    @Override
                    public void onQuantityChanged(int oldQuantity, int newQuantity, boolean programmatically) {
                        ShoppingListBean shoppingListBean = shoppinglist.get(position);
                        shoppingListBean.setGoods_buy_num(newQuantity);
                        showTotalAmount();
                    }

                    @Override
                    public void onLimitReached() {

                    }
                });
            }
        }

        public void removeItem(int position) {
            shoppinglist.remove(position);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return shoppinglist.size();
        }
    }
}