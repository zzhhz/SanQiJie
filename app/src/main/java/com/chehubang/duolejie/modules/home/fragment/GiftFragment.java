package com.chehubang.duolejie.modules.home.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseFragment;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.model.BannerBean;
import com.chehubang.duolejie.model.GiftListBean;
import com.chehubang.duolejie.model.GoodsBrandInfoBean;
import com.chehubang.duolejie.model.UserAllListBean;
import com.chehubang.duolejie.model.ViewPagerPicBean;
import com.chehubang.duolejie.modules.gift.activity.LuckDrawDetailsActivity;
import com.chehubang.duolejie.modules.gift.adapter.GiftAdapter;
import com.chehubang.duolejie.modules.gift.dialog.ConfirmDialog;
import com.chehubang.duolejie.modules.gift.presenter.GiftPresenter;
import com.chehubang.duolejie.modules.home.activity.MyStreetActivity;
import com.chehubang.duolejie.modules.login.activity.LoginActivity;
import com.chehubang.duolejie.utils.DialogUtils;
import com.chehubang.duolejie.utils.ShareUtils;
import com.chehubang.duolejie.widget.RecyclerViewBanner;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
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
 * @date: 2018/1/30 上午10:23
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @Description: 幸运1+1
 */
public class GiftFragment extends BaseFragment<GiftPresenter> implements MainView, View.OnClickListener, OnRefreshLoadmoreListener, DialogUtils.InputCodeListener {

    private SmartRefreshLayout refreshLayout;
    private RecyclerView content;
    private GiftAdapter mAdapter;
    private List<GiftListBean> mlist = new ArrayList<>();
    private List<UserAllListBean> mUserList = new ArrayList<>();
    private List<GoodsBrandInfoBean> mData = new ArrayList<>();
    int mPage = 1;
    private RecyclerViewBanner mRecyclerViewBanner;
    private ImageView contentPic;
    private String user_id;
    private int i2;
    private Dialog mDialog;
    private ImageView iv_empty;

    /**
     * 分享平台， 微信好友true, 朋友圈 false
     */
    private boolean flagSharePlatform = false;
    private Handler sHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            Bitmap[] bitmaps = (Bitmap[]) msg.obj;
            Bitmap bitmap = ShareUtils.createBitmap(getActivity(), bitmaps[0], bitmaps[1]);
            WXImageObject shareImg = new WXImageObject(bitmap);
            WXMediaMessage shareMsg = new WXMediaMessage();
            shareMsg.mediaObject = shareImg;
            Bitmap thumbImg = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
            shareMsg.thumbData = ShareUtils.getBytes(thumbImg);
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("img");
            req.message = shareMsg;
            req.scene = flagSharePlatform ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
            IWXAPI wxapi = WXAPIFactory.createWXAPI(getActivity(), Constant.APP_ID);
            wxapi.sendReq(req);
        }
    };

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gift, container, false);
        refreshLayout = (SmartRefreshLayout) view.findViewById(R.id.srf_gift);
        v_location = view.findViewById(R.id.v_location);
        content = (RecyclerView) view.findViewById(R.id.lv_gift_content);
        iv_empty = (ImageView) view.findViewById(R.id.iv_empty);
        content.setAdapter(mAdapter = new GiftAdapter(getActivity(), mlist));
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity()
                , LinearLayoutManager.VERTICAL, false);
        content.setLayoutManager(mLayoutManager);
        setHeader(content);
        refreshLayout.setOnRefreshLoadmoreListener(this);
        UserInfo.getInstance().getIdCache(getActivity());
        user_id = UserInfo.getInstance().getId();
        //设置 Header 为 Material风格
        refreshLayout.setRefreshHeader(new MaterialHeader(getActivity()).setColorSchemeColors(Color.BLUE));
        //设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));
        mvpPresenter.getGoodsBrandInfo(ACTION_DEFAULT + 1, "y");
        mvpPresenter.getBanner(10, 6);
        mAdapter.setOnItemClickLitener(new GiftAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), LuckDrawDetailsActivity.class);
                PictureUtils.loadPicture(getActivity(), mlist.get(position).getGuide_img(), contentPic, R.drawable.pic_cycjjl);
                intent.putExtra("user_id", user_id);
                intent.putExtra("brand_id", mlist.get(position).getBrand_id());
                intent.putExtra("coupon_id", mlist.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onConfirmClick(int position, String num) {
                if (TextUtils.isEmpty(UserInfo.getInstance().getId())) {
                    ToastUtils.centerToastWhite(getActivity(), "请登录");
                    return;
                }
                initDialog(position, num);
            }
        });

        mAdapter.setInputListener(new GiftAdapter.OnItemClickInputListener() {
            @Override
            public void onInputCode() {
                if (TextUtils.isEmpty(UserInfo.getInstance().getId())) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Dialog dialog = DialogUtils.inputCode(getActivity(), GiftFragment.this);
                    dialog.show();
                }
            }
        });
        TextView myStreet = (TextView) view.findViewById(R.id.tv_header_my_street);
        final EditText mSearch = (EditText) view.findViewById(R.id.et_gift_search);

        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    stopPlay();
                    mPage = 1;
                    String id = mSearch.getText().toString().trim();
                    mvpPresenter.getLuckDrawData(ACTION_DEFAULT + 2, id, mPage);
                    contentPic.setVisibility(View.GONE);
                } else {
                    startPlay();
                    contentPic.setVisibility(View.VISIBLE);
                }
            }
        });

        myStreet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(UserInfo.getToken())) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), MyStreetActivity.class));
                }
            }
        });
        return view;
    }

    private void initDialog(final int position, final String num) {
        final ConfirmDialog pd = new ConfirmDialog(getActivity());
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        View dialog = View.inflate(getActivity(), R.layout.dialog_gift_confirm, null);
        TextView totalNum = (TextView) dialog.findViewById(R.id.tv_dialog_num);
        TextView canyu = (TextView) dialog.findViewById(R.id.tv_dialog_canyu);
        String user_lettory = UserInfo.getString("user_lettory");
        String need_lettory_num = mlist.get(position).getNeed_lettory_num();
        int i = Integer.parseInt(need_lettory_num);
        int i1 = Integer.parseInt(num);
        if (TextUtils.isEmpty(user_lettory)) {
            totalNum.setText("0");
            if (TextUtils.isEmpty(UserInfo.getInstance().getId())) {
                ToastUtils.showToast(getActivity(), "未获取到个人信息");
            } else {
                Dialog dialog1 = DialogUtils.getShowMsg(getActivity(), "多乐券余额不足", getActivity().getResources().getString(R.string.msg_shao_gift));
                dialog1.show();
            }
        } else {
            totalNum.setText(UserInfo.getString("user_lettory"));
        }
        i2 = i1 * i;
        try {
            int le = Integer.parseInt(user_lettory);
            if (i2 > le) {
                Dialog dialog1 = DialogUtils.getShowMsg(getActivity(), "多乐券余额不足", getActivity().getResources().getString(R.string.msg_shao_gift));
                dialog1.show();
                return;
            }
        } catch (Exception ex) {

        }
        canyu.setText(i2 + "");
        TextView cancel = (TextView) dialog.findViewById(R.id.btn_add_qx);
        TextView confirm = (TextView) dialog.findViewById(R.id.btn_add_qr);
        pd.setContentView(dialog);
        pd.show();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshLayout.autoRefresh(500);
                mvpPresenter.getConfirm(ACTION_DEFAULT + 3, user_id, mlist.get(position).getId(), i2, num);
                pd.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.dismiss();
            }
        });
    }

    public PopupWindow mPopupWindow;
    private String qrImage = null;
    private View v_location;

    private void setHeader(RecyclerView view) {
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.giftheader, null);
        contentPic = (ImageView) header.findViewById(R.id.iv_gift_header);
        contentPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent7 = new Intent(getActivity(), WebviewActivity.class);
                intent7.putExtra("url", "http://39.107.14.118/appInterface/aboutUsContentInfo.jhtml?id=10");  //外网版本
                intent7.putExtra("title", "抽奖规则");
                startActivity(intent7);*/
                if (!TextUtils.isEmpty(UserInfo.getInstance().getId())) {
                    if (!TextUtils.isEmpty(qrImage)) {
                        if (mPopupWindow == null) {
                            mPopupWindow = DialogUtils.shareUi(getActivity(), null, new DialogUtils.OnShareClickListener() {
                                @Override
                                public void onShareWeiXin() {
                                    //((BaseActivity) getActivity()).showDialog();
                                    flagSharePlatform = true;
                                    ShareUtils.getBitmaps(getActivity(), sHandler, qrImage);
                                }

                                @Override
                                public void onShareCircle() {
                                    //((BaseActivity) getActivity()).showDialog();
                                    flagSharePlatform = false;
                                    ShareUtils.getBitmaps(getActivity(), sHandler, qrImage);
                                }
                            });
                        }
                        mPopupWindow.showAtLocation(v_location, Gravity.NO_GRAVITY, 0, 0);
                    } else {
                        mvpPresenter.getShareData(ACTION_DEFAULT + 6);
                    }
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        mAdapter.setHeaderView(header);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        mPage += 1;
        mvpPresenter.getLuckDrawData(ACTION_DEFAULT + 2, mData.get(0).getId(), mPage);
        refreshLayout.finishLoadmore(2000);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
//        ToastUtils.centerToastWhite(getActivity(), "准备下拉刷新");
        mPage = 1;
        if (!mData.isEmpty()) {
            mvpPresenter.getLuckDrawData(ACTION_DEFAULT + 2, mData.get(0).getId(), mPage);
        }
        refreshLayout.finishRefresh(2000);
    }

    @Override
    public void getDataSuccess(Object model, int action) {
        if (action == ACTION_DEFAULT + 1) {
            if (model != null) {
                try {
                    JSONObject jsonObject = new JSONObject((String) model);
                    JSONObject shopCartList = jsonObject.getJSONObject("goodsBrandMap");
                    mData.clear();
                    GoodsBrandInfoBean goodsHotListBean = JSONUtils.GsonToBean(shopCartList.toString(), GoodsBrandInfoBean.class);
                    mData.add(goodsHotListBean);
                    mvpPresenter.getLuckDrawData(ACTION_DEFAULT + 2, mData.get(0).getId(), mPage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (action == ACTION_DEFAULT + 2) {
            if (model != null) {
                if (mPage == 1) {
                    mlist.clear();
                    mUserList.clear();
                    try {
                        JSONObject jsonObject = new JSONObject((String) model);
                        JSONArray couponList = jsonObject.getJSONArray("couponList");
                        JSONArray userAllList = jsonObject.getJSONArray("userAllList");
                        String is_new_user = jsonObject.optString("is_user_new", "n");
                        mAdapter.setIsNewUser(is_new_user);
                        List<ViewPagerPicBean> imgs = new ArrayList<>();
                        for (int i = 0; i < couponList.length(); i++) {
                            JSONObject o1 = (JSONObject) couponList.get(i);
                            GiftListBean goodsHotListBean = JSONUtils.GsonToBean(o1.toString(), GiftListBean.class);
                            ViewPagerPicBean bean = new ViewPagerPicBean();
                            bean.setUrl(goodsHotListBean.getGuide_img());
                            bean.setHeader(goodsHotListBean.getGuide_img());
                            imgs.add(bean);
                            mlist.add(goodsHotListBean);
                        }
                        if (!imgs.isEmpty()) {
                            PictureUtils.loadPicture(getActivity(), imgs.get(0).getHeader(), contentPic, R.drawable.pic_cycjjl);
                        }
                        for (int i = 0; i < userAllList.length(); i++) {
                            JSONObject o1 = (JSONObject) userAllList.get(i);
                            UserAllListBean UserBean = JSONUtils.GsonToBean(o1.toString(), UserAllListBean.class);
                            mUserList.add(UserBean);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject((String) model);
                        JSONArray couponList = jsonObject.getJSONArray("couponList");
                        JSONArray userAllList = jsonObject.getJSONArray("userAllList");
                        String is_new_user = jsonObject.getString("is_user_new");
                        mAdapter.setIsNewUser(is_new_user);
                        List<ViewPagerPicBean> imgs = new ArrayList<>();
                        for (int i = 0; i < couponList.length(); i++) {
                            JSONObject o1 = (JSONObject) couponList.get(i);
                            GiftListBean goodsHotListBean = JSONUtils.GsonToBean(o1.toString(), GiftListBean.class);
                            ViewPagerPicBean bean = new ViewPagerPicBean();
                            bean.setUrl(goodsHotListBean.getGuide_img());
                            bean.setHeader(goodsHotListBean.getGuide_img());
                            imgs.add(bean);
                            mlist.add(goodsHotListBean);
                        }
                        if (!imgs.isEmpty()) {
                            PictureUtils.loadPicture(getActivity(), imgs.get(0).getHeader(), contentPic, R.drawable.pic_cycjjl);
                        }
                        for (int i = 0; i < userAllList.length(); i++) {
                            JSONObject o1 = (JSONObject) userAllList.get(i);
                            JSONArray userAllList2 = jsonObject.getJSONArray("userList");
                            for (int j = 0; j < userAllList2.length(); i++) {
                                JSONObject o2 = (JSONObject) userAllList2.get(j);
                                UserAllListBean UserBean = JSONUtils.GsonToBean(o2.toString(), UserAllListBean.class);
                                mUserList.add(UserBean);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mAdapter.notifyDataSetChanged();
                if (mlist.size() > 0) {
                    iv_empty.setVisibility(View.GONE);
                } else {
                    iv_empty.setVisibility(View.VISIBLE);
                }
            }
        } else if (action == ACTION_DEFAULT + 3) {
            EventBus.getDefault().post(Constant.EVENT_REFRESH_USER);
            if (model != null) {
                RequestResult data = (RequestResult) model;
                if (TextUtils.equals(Constant.request_success, data.getStatus())) {
                    if (mDialog == null) {
                        mDialog = DialogUtils.getShowMsg(getActivity(), "抽奖参与成功", getActivity().getResources().getString(R.string.msg_gift));
                    }
                    mDialog.show();
                } else {
                    ToastUtils.centerToastWhite(getActivity(), data.getDesc());
                }
            }
        } else if (action == 10) {
            Wrapper wrapper = JSONUtils.GsonToBean(model.toString(), Wrapper.class);
            if (wrapper != null && wrapper.getAdvertisementList() != null && !wrapper.getAdvertisementList().isEmpty()) {
                PictureUtils.loadPicture(getActivity(), wrapper.getAdvertisementList().get(0).getHeader(), contentPic, R.drawable.pic_cycjjl);
            }
        } else if (action == ACTION_DEFAULT + 5) {
            EventBus.getDefault().post(Constant.EVENT_REFRESH_USER);
            mvpPresenter.getLuckDrawData(ACTION_DEFAULT + 2, mData.get(0).getId(), mPage);
            ToastUtils.centerToastWhite(getActivity(), model.toString());
        } else if (action == ACTION_DEFAULT + 6) {
            qrImage = model.toString();
            if (mPopupWindow == null) {
                mPopupWindow = DialogUtils.shareUi(getActivity(), null, new DialogUtils.OnShareClickListener() {
                    @Override
                    public void onShareWeiXin() {
                        flagSharePlatform = true;
                        ShareUtils.getBitmaps(getActivity(), sHandler, qrImage);

                    }

                    @Override
                    public void onShareCircle() {
                        flagSharePlatform = false;
                        ShareUtils.getBitmaps(getActivity(), sHandler, qrImage);
                    }
                });
            }
            mPopupWindow.showAtLocation(v_location, Gravity.NO_GRAVITY, 0, 0);
        }
    }

    @Override
    public void getDataFail(String msg, int action) {
        if (action == ACTION_DEFAULT + 5) {
            ToastUtils.centerToastWhite(getActivity(), msg);
        } else if (action == ACTION_DEFAULT + 2) {
            if (mlist.size() > 0) {
                iv_empty.setVisibility(View.GONE);
            } else {
                iv_empty.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    protected GiftPresenter createPresenter() {
        return new GiftPresenter(this);
    }

    @Override
    public void inputCode(String code) {
        if (!TextUtils.isEmpty(code)) {
            mvpPresenter.inputCode(ACTION_DEFAULT + 5, code);
        } else {
            ToastUtils.centerToastWhite(getActivity(), "请输入邀请码");
        }

    }

    class Wrapper {
        private List<BannerBean> advertisementList;

        public List<BannerBean> getAdvertisementList() {
            return advertisementList;
        }

        public void setAdvertisementList(List<BannerBean> advertisementList) {
            this.advertisementList = advertisementList;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    break;
            }

        }
    };

    private class PlayThread implements Runnable {
        @Override
        public void run() {
            mPage = 1;
            if (mData.size() > 0) {
                mvpPresenter.getLuckDrawData(ACTION_DEFAULT + 2, mData.get(0).getId(), mPage);
            }
            handler.postDelayed(this, 10000);
        }
    }

    PlayThread thread;

    public void startPlay() {
        if (thread == null) {
            thread = new PlayThread();
        }
        handler.postDelayed(thread, 10000);
    }

    public void stopPlay() {
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        stopPlay();
        startPlay();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        stopPlay();
        startPlay();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopPlay();
    }
}
