package com.chehubang.duolejie.modules.gooddetails.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseFragment;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.listener.OnButtonClick;
import com.chehubang.duolejie.model.AdvertisementListBean;
import com.chehubang.duolejie.model.ColorBean;
import com.chehubang.duolejie.model.GoodsInfoAdvantageListBean;
import com.chehubang.duolejie.model.GoodsInfoMapBean;
import com.chehubang.duolejie.model.GoodsInfoServiceListBean;
import com.chehubang.duolejie.model.SpecBean;
import com.chehubang.duolejie.modules.chargecenter.activity.ChargeOrderActivity;
import com.chehubang.duolejie.modules.gooddetails.activity.GoodsDetailsActivity;
import com.chehubang.duolejie.modules.gooddetails.dialog.SpecificationDialog;
import com.chehubang.duolejie.modules.gooddetails.presenter.GoodsFragmentPresenter;
import com.chehubang.duolejie.modules.login.activity.LoginActivity;
import com.chehubang.duolejie.utils.CodeUtils;
import com.chehubang.duolejie.utils.DialogUtils;
import com.chehubang.duolejie.utils.WebViewSetting;
import com.chehubang.duolejie.utils.log;
import com.chehubang.duolejie.widget.FluidLayout;
import com.chehubang.duolejie.widget.RecyclerViewBanner;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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
 * Created by ZZH on 2018/1/30
 *
 * @date: 2018/1/30 上午11:46
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @Description: 商品详情
 */
public class GoodsFragment extends BaseFragment<GoodsFragmentPresenter> implements MainView, View.OnClickListener, SpecificationDialog.DataSelectedListener, OnRefreshListener, SpecificationDialog.OnSoftClickListener {

    private WebView webView;
    private RecyclerViewBanner viewPager;
    private ImageView goodPic1, goodPic2, goodPic3, goodPicBig;
    private TextView goodTitle1, goodTitle2, goodTitle3, goodName, goodPrice, goodPriceOrg, salenum, goodCommentCount,
            goodCommentShow, goodNorm, goodCoupon, /*goodService1, goodService2, goodService3, goodService4,*/
            goodCommentCountunder, goodCommentgood, goodsBuy, goodsAddShopping;
    private SpecificationDialog specificationDialog;
    private String id;
    private ArrayList<ColorBean> colorList = new ArrayList<>();
    private ArrayList<SpecBean> specList = new ArrayList<>();
    private String mpic = null;
    private String mprice = null;
    private String mbalance = null;
    private String colorId = null;
    private String specId = null;
    private String num = "1";
    private String colorString = null;
    private String specString = null;
    private OnButtonClick mclick;
    private List<String> banners = new ArrayList<>();
    private final String ACTION_SWITCH = "com.chehubang.duolejie.ACTION_SWITCH";
    private String mposition = "position";
    private FluidLayout mFluidLayout;
    private SmartRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods, container, false);
        initView(view);
        Intent intent = getActivity().getIntent();
        id = intent.getStringExtra("id");
        mvpPresenter.getGoodsInfoData(ACTION_DEFAULT + 1, id, UserInfo.getInstance().getId());
        mvpPresenter.getGoodsInfoMoreList(ACTION_DEFAULT + 3, id);
        return view;
    }

    private void initView(View view) {            //找控件
        viewPager = (RecyclerViewBanner) view.findViewById(R.id.rcyb_goodtails);
        mFluidLayout = (FluidLayout) view.findViewById(R.id.fl_container);
        goodPicBig = (ImageView) view.findViewById(R.id.iv_good_pic_big);
        goodName = (TextView) view.findViewById(R.id.tv_good_name);
        goodPrice = (TextView) view.findViewById(R.id.tv_good_price);
        goodPriceOrg = (TextView) view.findViewById(R.id.tv_good_price_org);
        salenum = (TextView) view.findViewById(R.id.tv_salenum);
        refreshLayout = (SmartRefreshLayout) view.findViewById(R.id.refreshLayout);
        ImageView iv_good_shooping = (ImageView) view.findViewById(R.id.iv_good_shooping);
        goodNorm = (TextView) view.findViewById(R.id.tv_good_norm);
        goodCoupon = (TextView) view.findViewById(R.id.tv_good_coupon);
        goodsBuy = (TextView) view.findViewById(R.id.tv_good_buy);
        goodsAddShopping = (TextView) view.findViewById(R.id.tv_good_add_shopping);
        webView = (WebView) view.findViewById(R.id.wv_good_webview);
        WebViewSetting.setWebview(webView);
        goodNorm.setOnClickListener(this);
        goodsBuy.setOnClickListener(this);
        goodsAddShopping.setOnClickListener(this);
        iv_good_shooping.setOnClickListener(this);
        //设置 Header 为 Material风格
        refreshLayout.setRefreshHeader(new MaterialHeader(getActivity()).setColorSchemeColors(Color.BLUE));
        //设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setEnableLoadmore(false);
    }


    @Override
    public void getDataSuccess(Object model, int action) {
        if (action == ACTION_DEFAULT + 1) {
            String data = (String) model;
            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONArray advertisementList = jsonObject.getJSONArray("advertisementList");
                JSONArray goodsInfoServiceList = jsonObject.getJSONArray("goodsInfoServiceList");
                String goodsInfoMap = jsonObject.getString("goodsInfoMap");
                JSONArray commentList = jsonObject.getJSONArray("commentList");
                JSONArray goodsInfoAdvantageList = jsonObject.getJSONArray("goodsInfoAdvantageList");

                GoodsInfoMapBean goodsInfoMapBean = JSONUtils.GsonToBean(goodsInfoMap, GoodsInfoMapBean.class);
                String good_imgs = goodsInfoMapBean.getGood_imgs();
                String[] split = good_imgs.split(",");
                banners.clear();
                for (int i = 0; i < split.length; i++) {
                    banners.add(split[i]);
                }

                WebViewSetting.openWebView(webView, goodsInfoMapBean.getGoods_detail());

                mpic = goodsInfoMapBean.getGood_header();
                mprice = goodsInfoMapBean.getGood_price();
                mbalance = goodsInfoMapBean.getGoods_num() + "";
                /*
                * 初始化bannner
                * */
                viewPager.isShowIndicator(true);
                viewPager.setRvBannerData(banners);
                viewPager.setOnSwitchRvBannerListener(new RecyclerViewBanner.OnSwitchRvBannerListener() {
                    @Override
                    public void switchBanner(int position, AppCompatImageView bannerView) {
                        PictureUtils.loadPicture(getActivity(), banners.get(position % banners.size()), bannerView, R.drawable.pic_cycjjl);
                    }
                });
                viewPager.setOnRvBannerClickListener(new RecyclerViewBanner.OnRvBannerClickListener() {
                    @Override
                    public void onClick(int position) {
                        //点击事件
                    }
                });

                goodName.setText(goodsInfoMapBean.getGood_name());
                //     goodContent.setText(goodsInfoMapBean.getContent());
                goodPrice.setText("￥" + goodsInfoMapBean.getGood_price());  //现价
                goodPriceOrg.setText("￥" + goodsInfoMapBean.getGood_single_price());  //原始价格
                String saleNum = goodsInfoMapBean.getSaleNum();
                int i1;
                try {
                    i1 = Integer.parseInt(saleNum);
                    if (i1 > 10000) {
                        float i = (float) i1 / (float) 10000;
                        salenum.setText("月销" + CodeUtils.scaleValue(i) + "万笔");
                    } else {
                        salenum.setText("月销" + saleNum + "笔");
                    }
                } catch (Exception ex) {
                    i1 = 0;
                    salenum.setText("月销" + saleNum + "笔");
                }

                goodCoupon.setText("领券：购买最高得" + goodsInfoMapBean.getLottery() + "奖券");

                AdvertisementListBean advertisementListBean = JSONUtils.GsonToBean(advertisementList.get(0).toString(), AdvertisementListBean.class);
                PictureUtils.loadPicture(getActivity(), advertisementListBean.getHeader(), goodPicBig, R.drawable.pic_cycjjl);

                LayoutInflater inflater = LayoutInflater.from(getActivity());
                for (int i = 0; i < goodsInfoServiceList.length(); i++) {
                    log.d("" + i);
                    View view = inflater.inflate(R.layout.item_goods_label, null);
                    TextView textView = (TextView) view.findViewById(R.id.tv_good_service);
                    textView.setText(setServiceContent(goodsInfoServiceList.get(i).toString()));
                    mFluidLayout.addView(view);
                }

                if (TextUtils.equals("y", advertisementListBean.getIs_jump())) {
                    goodPicBig.setOnClickListener(this);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (action == ACTION_DEFAULT + 2) {
            if (model != null) {
                RequestResult data = (RequestResult) model;
                String status = data.getStatus();
                if (TextUtils.equals(Constant.request_success, status)) {
                    ToastUtils.centerToastWhite(getActivity(), "添加购物车成功");
                } else if (TextUtils.equals("1", status)) {
                    ToastUtils.centerToastWhite(getActivity(), data.getDesc());
                }
            }
        } else if (action == ACTION_DEFAULT + 3) {
            if (model != null) {
                RequestResult data = (RequestResult) model;
                String status = data.getStatus();
                if (TextUtils.equals(Constant.request_success, status)) {
                    try {
                        colorList.clear();
                        specList.clear();
                        JSONObject jsonObject = new JSONObject(data.getData());
                        JSONArray goodsInfoColorList = jsonObject.getJSONArray("goodsInfoColorList");
                        JSONArray goodsInfoSpecList = jsonObject.getJSONArray("goodsInfoSpecList");
                        colorList = JSONUtils.GsonjsonToArrayList(goodsInfoColorList.toString(), ColorBean.class);
                        specList = JSONUtils.GsonjsonToArrayList(goodsInfoSpecList.toString(), SpecBean.class);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (action == ACTION_DEFAULT + 4) {
            ((GoodsDetailsActivity) getActivity()).dismissDialog();
            log.d(model.toString());
            try {
                JSONObject object = new JSONObject(model.toString());
                JSONArray goodsInfoNumList = object.getJSONArray("goodsInfoNumList");
                if (goodsInfoNumList != null && goodsInfoNumList.length() > 0) {
                    JSONObject jsonObject = goodsInfoNumList.getJSONObject(0);
                    int goods_num = jsonObject.getInt("goods_num");
                    if (specificationDialog != null) {
                        specificationDialog.setBalance(String.valueOf(goods_num));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void setContent(String content, ImageView imageView, TextView textView) {
        GoodsInfoAdvantageListBean listBean = JSONUtils.GsonToBean(content, GoodsInfoAdvantageListBean.class);
        PictureUtils.loadPicture(getActivity(), listBean.getGood_header(), imageView, R.drawable.forget_pwd_bg);
        textView.setText(listBean.getAdvantage());
    }

    public String setServiceContent(String content) {
        GoodsInfoServiceListBean goodsInfoServiceListBean = JSONUtils.GsonToBean(content, GoodsInfoServiceListBean.class);
        return goodsInfoServiceListBean.getService();
    }

    @Override
    public void getDataFail(String msg, int action) {

    }

    @Override
    protected GoodsFragmentPresenter createPresenter() {
        return new GoodsFragmentPresenter(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_good_norm:
                if (TextUtils.isEmpty(mpic) && TextUtils.isEmpty(mprice) && TextUtils.isEmpty(mbalance)) {
                    ToastUtils.centerToastWhite(getActivity(), "数据未请求成功");
                } else {
                    initDialog();
                }
                break;
            case R.id.tv_good_buy:
                if (TextUtils.isEmpty(UserInfo.getInstance().getId())) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    break;
                }
                if (colorList.size() == 1 && TextUtils.isEmpty(colorList.get(0).getGoods_color())) {
                    if (TextUtils.isEmpty(id) || TextUtils.isEmpty(specId) || TextUtils.isEmpty(num)) {
                        ToastUtils.centerToastWhite(getActivity(), "请选择商品的规格");
                        initDialog();
                        mposition = "position1";
                    } else {
                        Intent intent = new Intent(getActivity(), ChargeOrderActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("colorid", colorId);
                        intent.putExtra("specid", specId);
                        intent.putExtra("num", num);
                        intent.putExtra("price", mprice);
                        intent.putExtra("color", colorString);
                        intent.putExtra("spec", specString);
                        intent.putExtra("is_shop_cart", "n");
                        startActivity(intent);
                    }
                } else {
                    if (TextUtils.isEmpty(id) || TextUtils.isEmpty(colorId) || TextUtils.isEmpty(specId) || TextUtils.isEmpty(num)) {
                        ToastUtils.centerToastWhite(getActivity(), "请选择商品的规格");
                        initDialog();
                        mposition = "position1";
                    } else {
                        Intent intent = new Intent(getActivity(), ChargeOrderActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("colorid", colorId);
                        intent.putExtra("specid", specId);
                        intent.putExtra("num", num);
                        intent.putExtra("price", mprice);
                        intent.putExtra("color", colorString);
                        intent.putExtra("spec", specString);
                        intent.putExtra("is_shop_cart", "n");
                        startActivity(intent);
                    }
                }
                break;
            case R.id.tv_good_add_shopping:
                if (TextUtils.isEmpty(UserInfo.getInstance().getId())) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    break;
                }
                if (colorList.size() == 1 && TextUtils.isEmpty(colorList.get(0).getGoods_color())) {
                    if (TextUtils.isEmpty(id) || TextUtils.isEmpty(specId) || TextUtils.isEmpty(num)) {
                        ToastUtils.centerToastWhite(getActivity(), "请选择商品的规格");
                        initDialog();
                        mposition = "position2";
                    } else {
                        if (TextUtils.isEmpty(colorId) || "null".equals(colorId)) {
                            colorId = "";
                        }
                        if (TextUtils.isEmpty(colorString) || "null".equals(colorString)) {
                            colorString = "";
                        }
                        mvpPresenter.addToShoppingList(ACTION_DEFAULT + 2, UserInfo.getInstance().getId(), id, num, colorId, colorString, specId, specString);
                    }
                } else {
                    if (TextUtils.isEmpty(id) || TextUtils.isEmpty(colorId) || TextUtils.isEmpty(specId) || TextUtils.isEmpty(num)) {
                        ToastUtils.centerToastWhite(getActivity(), "请选择商品的规格");
                        initDialog();
                        mposition = "position2";
                    } else {
                        if (TextUtils.isEmpty(colorId) || "null".equals(colorId)) {
                            colorId = "";
                        }
                        if (TextUtils.isEmpty(colorString) || "null".equals(colorString)) {
                            colorString = "";
                        }
                        mvpPresenter.addToShoppingList(ACTION_DEFAULT + 2, UserInfo.getInstance().getId(), id, num, colorId, colorString, specId, specString);
                    }
                }
                break;
            case R.id.iv_good_pic_big:
                //ToastUtils.centerToastWhite(getActivity(), "图片现在让点了");
                break;
            case R.id.iv_good_shooping:
                Intent intent = new Intent();
                intent.setAction(ACTION_SWITCH);
                intent.putExtra("action", "fragment3");
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                getActivity().finish();
            default:
                break;
        }
    }

    public void initDialog() {
        specificationDialog = new SpecificationDialog(getActivity(), colorList, specList, mpic, mprice, mbalance);
        DialogUtils.setDialogStyle(specificationDialog);
        specificationDialog.show();
        specificationDialog.setOnDataSelectedListener(this);
        specificationDialog.setSoftClickListener(this);
    }

    @Override
    public void dataSelected(String colorId, String specId, String color, String spec, int num) {
        this.colorId = colorId;
        this.specId = specId;
        this.num = String.valueOf(num);
        this.specString = spec;
        this.colorString = color;
        if (TextUtils.equals(mposition, "position1")) {
            Intent intent = new Intent(getActivity(), ChargeOrderActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("colorid", this.colorId);
            intent.putExtra("specid", this.specId);
            intent.putExtra("num", this.num);
            intent.putExtra("price", mprice);
            intent.putExtra("color", colorString);
            intent.putExtra("spec", specString);
            intent.putExtra("is_shop_cart", "n");
            startActivity(intent);
        } else if (TextUtils.equals(mposition, "position2")) {
            if (TextUtils.isEmpty(colorId) || "null".equals(colorId)) {
                colorId = "";
            }
            if (TextUtils.isEmpty(colorString) || "null".equals(colorString)) {
                colorString = "";
            }
            mvpPresenter.addToShoppingList(ACTION_DEFAULT + 2, UserInfo.getInstance().getId(), id, this.num, colorId, colorString, specId, specString);
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mvpPresenter.getGoodsInfoData(ACTION_DEFAULT + 1, id, UserInfo.getInstance().getId());
        mvpPresenter.getGoodsInfoMoreList(ACTION_DEFAULT + 3, id);
        refreshLayout.finishRefresh(2000);
    }

    @Override
    public void getNumByColorAndSpec(String colorId, String specId) {
        ((GoodsDetailsActivity) getActivity()).showDialog();
        mvpPresenter.getGoodsInfoSpecByColorList(ACTION_DEFAULT + 4, id, colorId, specId);
    }
}
