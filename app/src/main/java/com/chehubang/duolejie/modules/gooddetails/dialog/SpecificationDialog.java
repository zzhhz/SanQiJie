package com.chehubang.duolejie.modules.gooddetails.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.model.ColorBean;
import com.chehubang.duolejie.model.SpecBean;
import com.chehubang.duolejie.modules.gooddetails.adapter.ColorAdapter;
import com.chehubang.duolejie.modules.gooddetails.adapter.SpecAdapter;
import com.chehubang.duolejie.utils.CodeUtils;
import com.chehubang.duolejie.widget.IconGridView;
import com.chehubang.duolejie.widget.QuantityView;

import java.util.ArrayList;

import common.Utils.ToastUtils;
import common.picture.PictureUtils;

/**
 * Created by Thinkpad on 2017/12/27.
 */

public class SpecificationDialog extends Dialog implements View.OnClickListener, GridView.OnItemClickListener {

    private ArrayList<ColorBean> mcolorList = new ArrayList<>();
    private ArrayList<SpecBean> mspecList = new ArrayList<>();
    private QuantityView quantityView;
    private String colorid = null;
    private String specId = null;
    private ColorAdapter colorAdapter;
    private SpecAdapter specAdapter;
    private DataSelectedListener mSListener = null;
    private String mpic = null;
    private String mprice = null;
    private String mbalance = null;
    private String colorString = null;
    private String specString = null;
    TextView goodsBalance;

    public SpecificationDialog(@NonNull Context context, ArrayList<ColorBean> list1, ArrayList<SpecBean> list2, String pic, String price, String num) {
        super(context, R.style.DialogStyleBottom);

        this.mcolorList = list1;

        if (list1 != null && !list1.isEmpty()) {
            this.mcolorList = new ArrayList<>();
            for (ColorBean bean : list1) {
                if (!TextUtils.isEmpty(bean.getGoods_color()) && !TextUtils.isEmpty(bean.getGoods_color())) {
                    this.mcolorList.add(bean);
                }
            }
        }


        this.mspecList = list2;
        this.mpic = pic;
        this.mprice = price;
        this.mbalance = num;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sepcification);
        TextView confirm = (TextView) findViewById(R.id.tv_spcification_confirm);
        IconGridView colorlist = (IconGridView) findViewById(R.id.igv_color);
        IconGridView speclist = (IconGridView) findViewById(R.id.igv_spec);
        ImageView header = (ImageView) findViewById(R.id.iv_spcification_pic);
        TextView goodsPrice = (TextView) findViewById(R.id.tv_spcification_price);
        goodsBalance = (TextView) findViewById(R.id.tv_spcification_balance);
        TextView back = (TextView) findViewById(R.id.tv_back);
        TextView tv_color = (TextView) findViewById(R.id.tv_color);
        PictureUtils.loadPicture(getContext(), mpic, header, R.drawable.pic_cycjjl);
        goodsPrice.setText("￥" + mprice);
        goodsBalance.setText("库存" + mbalance + "件");

        colorAdapter = new ColorAdapter(getContext(), mcolorList);
        colorlist.setAdapter(colorAdapter);
        colorlist.setOnItemClickListener(this);
        specAdapter = new SpecAdapter(getContext(), mspecList);
        speclist.setAdapter(specAdapter);
        speclist.setOnItemClickListener(this);

        quantityView = (QuantityView) findViewById(R.id.quantity_view);
        quantityView.setMinQuantity(1);
        confirm.setOnClickListener(this);
        back.setOnClickListener(this);
        if (mcolorList == null || mcolorList.isEmpty()) {
            colorlist.setVisibility(View.GONE);
            tv_color.setVisibility(View.GONE);
        }
    }

    public void setBalance(String mbalance) {
        this.mbalance = mbalance;
        if (goodsBalance != null) {
            /*if (goodsBalance != null) {
                double v = Double.parseDouble(mbalance);
                if (v>10000) {

                }
            }*/
            goodsBalance.setText("库存" + mbalance + "件");

        }
    }

    public void setOnDataSelectedListener(DataSelectedListener listener) {
        mSListener = listener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_spcification_confirm:
                int quantity = quantityView.getQuantity();
                /*if (TextUtils.isEmpty(colorid) && TextUtils.isEmpty(specId)) {
                    ToastUtils.centerToastWhite(getContext(), "请选择商品规格");
                } else if (!TextUtils.isEmpty(colorid) && TextUtils.isEmpty(specId)) {
                    ToastUtils.centerToastWhite(getContext(), "请选择商品规格");
                } else if (TextUtils.isEmpty(colorid) && !TextUtils.isEmpty(specId)) {
                    ToastUtils.centerToastWhite(getContext(), "请选择商品规格");
                } else {
                    if (mSListener != null) {
                        mSListener.dataSelected(colorid, specId, colorString, specString, quantity);
                        dismiss();
                    }
                }*/

                if (TextUtils.isEmpty(specId)) {
                    ToastUtils.centerToastWhite(getContext(), "请选择商品规格");
                    break;
                }


                if (mSListener != null) {
                    mSListener.dataSelected(colorid, specId, colorString, specString, quantity);
                    dismiss();
                }
                break;
            case R.id.tv_back:
                dismiss();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Adapter adapter = adapterView.getAdapter();
        if (adapter instanceof ColorAdapter) {
            colorAdapter.setSelectedPosition(i);
            colorid = mcolorList.get(i).getId();
            colorString = mcolorList.get(i).getGoods_color();
            colorAdapter.notifyDataSetChanged();
        } else if (adapter instanceof SpecAdapter) {
            specAdapter.setSelectedPosition(i);
            specId = mspecList.get(i).getId();
            specString = mspecList.get(i).getSpec();
            specAdapter.notifyDataSetChanged();
        }
        if (mSoftClickListener != null) {
            mSoftClickListener.getNumByColorAndSpec(colorid, specId);
        }
    }

    private OnSoftClickListener mSoftClickListener;

    public void setSoftClickListener(OnSoftClickListener softClickListener) {
        mSoftClickListener = softClickListener;
    }

    public interface DataSelectedListener {
        void dataSelected(String colorId, String specId, String color, String spec, int num);
    }

    public interface OnSoftClickListener {
        void getNumByColorAndSpec(String colorId, String specId);
    }
}
