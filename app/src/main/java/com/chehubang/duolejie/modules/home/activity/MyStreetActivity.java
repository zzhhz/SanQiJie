package com.chehubang.duolejie.modules.home.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.model.BannerBean;
import com.chehubang.duolejie.model.MyCouponListBean;
import com.chehubang.duolejie.model.UserAllListBean;
import com.chehubang.duolejie.modules.gift.dialog.ConfirmDialog;
import com.chehubang.duolejie.modules.home.adapter.GiftTypeAdapter;
import com.chehubang.duolejie.modules.home.presenter.MyStreetPresenter;
import com.chehubang.duolejie.modules.login.activity.LoginActivity;
import com.chehubang.duolejie.utils.DialogUtils;
import com.chehubang.duolejie.utils.ShareUtils;
import com.chehubang.duolejie.utils.log;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import common.Utils.JSONUtils;
import common.Utils.ToastUtils;
import common.http.RequestResult;
import common.mvp.activity.MainView;

/**
 * @Date: 2018/2/3 14:44
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description: 我的乐街
 */
public class MyStreetActivity extends BaseActivity<MyStreetPresenter> implements MainView, View.OnClickListener {

    private List<MyCouponListBean> mlist = new ArrayList<>();
    private List<UserAllListBean> mUserList = new ArrayList<>();
    private GiftTypeAdapter mAdapter;
    int mPage = 1;
    private View v_location;
    public PopupWindow mPopupWindow;
    private String qrImage = null;
    private SmartRefreshLayout refreshLayout;
    private Dialog mDialog;
    ImageView iv_empty;
    /**
     * 分享平台， 微信好友true, 朋友圈 false
     */
    private boolean flagSharePlatform = false;
    private Handler mHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            Bitmap[] bitmaps = (Bitmap[]) msg.obj;
            Bitmap bitmap = ShareUtils.createBitmap(MyStreetActivity.this, bitmaps[0], bitmaps[1]);
            WXImageObject shareImg = new WXImageObject(bitmap);
            WXMediaMessage shareMsg = new WXMediaMessage();
            shareMsg.mediaObject = shareImg;
            Bitmap thumbImg = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
            shareMsg.thumbData = ShareUtils.getBytes(thumbImg);
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("img");
            req.message = shareMsg;
            req.scene = flagSharePlatform ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
            IWXAPI wxapi = WXAPIFactory.createWXAPI(MyStreetActivity.this, Constant.APP_ID);
            wxapi.sendReq(req);
        }
    };

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_street);
        EventBus.getDefault().register(this);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        ImageView back = (ImageView) findViewById(R.id.iv_my_street_back);
        TextView share = (TextView) findViewById(R.id.iv_my_street_share);
        iv_empty = (ImageView) findViewById(R.id.iv_empty);
        RecyclerView content = (RecyclerView) findViewById(R.id.lv_gift_content);
        content.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new GiftTypeAdapter(this, mlist);
        content.setAdapter(mAdapter);
        UserInfo.getInstance().getIdCache(this);
        mvpPresenter.getLuckDrawData(ACTION_DEFAULT + 1, UserInfo.getInstance().getId(), mPage);
        mvpPresenter.getBanner(ACTION_DEFAULT + 2, 11);
        back.setOnClickListener(this);
        share.setOnClickListener(this);
        v_location = findAtyViewById(R.id.v_location);
        mAdapter.setOnItemClickLitener(new GiftTypeAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(int position) {
            }

            @Override
            public void onConfirmClick(int position, String num) {
                if (TextUtils.isEmpty(UserInfo.getInstance().getId())) {
                    ToastUtils.centerToastWhite(MyStreetActivity.this, "请登录");
                    return;
                }
                initDialog(position, num);
            }
        });
    }

    private int i2;

    private void initDialog(final int position, final String num) {
        final ConfirmDialog pd = new ConfirmDialog(MyStreetActivity.this);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        View dialog = View.inflate(MyStreetActivity.this, R.layout.dialog_gift_confirm, null);
        TextView totalNum = (TextView) dialog.findViewById(R.id.tv_dialog_num);
        TextView canyu = (TextView) dialog.findViewById(R.id.tv_dialog_canyu);
        String user_lettory = UserInfo.getString("user_lettory");
        String need_lettory_num = mlist.get(position).getNeed_lettory_num();
        int i = Integer.parseInt(need_lettory_num);
        int i1 = Integer.parseInt(num);
        if (TextUtils.isEmpty(user_lettory)) {
            totalNum.setText("0");
            if (TextUtils.isEmpty(UserInfo.getInstance().getId())) {
                ToastUtils.showToast(MyStreetActivity.this, "未获取到个人信息");
            } else {
                //ToastUtils.showToast(MyStreetActivity.this, "对不起，您无奖券可用");
                Dialog dialog1 = DialogUtils.getShowMsg(this, "多乐券余额不足", this.getResources().getString(R.string.msg_shao_gift));
                dialog1.show();
            }
        } else {
            totalNum.setText(UserInfo.getString("user_lettory"));
        }
        i2 = i1 * i;
        canyu.setText(i2 + "");
        TextView cancel = (TextView) dialog.findViewById(R.id.btn_add_qx);
        TextView confirm = (TextView) dialog.findViewById(R.id.btn_add_qr);
        pd.setContentView(dialog);
        pd.show();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshLayout.autoRefresh(500);
                mvpPresenter.getConfirm(ACTION_DEFAULT + 4, UserInfo.getInstance().getId(), mlist.get(position).getId(), i2, num);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_my_street_back:
                finish();
                break;
            case R.id.iv_my_street_share:
                if (!TextUtils.isEmpty(UserInfo.getInstance().getId())) {
                    if (!TextUtils.isEmpty(qrImage)) {
                        if (mPopupWindow == null) {
                            mPopupWindow = DialogUtils.shareUi(this, null, new DialogUtils.OnShareClickListener() {
                                @Override
                                public void onShareWeiXin() {
                                    showDialog();
                                    flagSharePlatform = true;
                                    ShareUtils.getBitmaps(MyStreetActivity.this, mHandler, qrImage);
                                }

                                @Override
                                public void onShareCircle() {
                                    showDialog();
                                    flagSharePlatform = false;
                                    ShareUtils.getBitmaps(MyStreetActivity.this, mHandler, qrImage);
                                }
                            });
                        }
                        mPopupWindow.showAtLocation(v_location, Gravity.NO_GRAVITY, 0, 0);
                    } else {
                        mvpPresenter.getShareData(ACTION_DEFAULT + 3);
                    }
                } else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void getDataSuccess(Object model, int action) {
        EventBus.getDefault().post(Constant.EVENT_REFRESH_USER);
        if (action == ACTION_DEFAULT + 1) {
            log.d("------" + model.toString());
            if (model != null) {
                mlist.clear();
                try {
                    JSONObject jsonObject = new JSONObject((String) model);
                    JSONArray couponList = jsonObject.getJSONArray("couponList");
                    log.d(model.toString());
                    ArrayList<MyCouponListBean> myCouponListBeans = JSONUtils.GsonjsonToArrayList(couponList.toString(), MyCouponListBean.class);
                    mlist.addAll(myCouponListBeans);
                    mAdapter.notifyDataSetChanged();
                    if (mlist.size() > 0) {
                        iv_empty.setVisibility(View.GONE);
                    } else {
                        iv_empty.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (action == (ACTION_DEFAULT + 2)) {
            if (!TextUtils.isEmpty(model.toString())) {
                Wrapper wrapper = JSONUtils.GsonToBean(model.toString(), Wrapper.class);
                if (wrapper != null && wrapper.getAdvertisementList() != null && !wrapper.getAdvertisementList().isEmpty()) {
                    mAdapter.addHeaderRule(wrapper.getAdvertisementList());
                    mAdapter.notifyDataSetChanged();
                }
                if (mlist.size() > 0) {
                    iv_empty.setVisibility(View.GONE);
                } else {
                    iv_empty.setVisibility(View.VISIBLE);
                }
            }
        } else if (action == (ACTION_DEFAULT + 3)) {
            qrImage = model.toString();
            if (mPopupWindow == null) {
                mPopupWindow = DialogUtils.shareUi(this, null, new DialogUtils.OnShareClickListener() {
                    @Override
                    public void onShareWeiXin() {

                        showDialog();
                        flagSharePlatform = true;
                        ShareUtils.getBitmaps(MyStreetActivity.this, mHandler, qrImage);

                    }

                    @Override
                    public void onShareCircle() {
                        showDialog();
                        flagSharePlatform = false;
                        ShareUtils.getBitmaps(MyStreetActivity.this, mHandler, qrImage);
                    }
                });
            }
            mPopupWindow.showAtLocation(v_location, Gravity.NO_GRAVITY, 0, 0);
        } else if (action == ACTION_DEFAULT + 4) {
            if (model != null) {
                mvpPresenter.getLuckDrawData(ACTION_DEFAULT + 1, UserInfo.getInstance().getId(), mPage);
                RequestResult data = (RequestResult) model;
                if (TextUtils.equals(Constant.request_success, data.getStatus())) {
                    //ToastUtils.centerToastWhite(this, "成功");
                    if (mDialog == null) {
                        mDialog = DialogUtils.getShowMsg(this, "抽奖参与成功", getResources().getString(R.string.msg_gift));
                    }
                    mDialog.show();
                } else {
                    ToastUtils.centerToastWhite(this, data.getDesc());
                }
            }
        }
        log.d(mAdapter.getItemCount() + "-------------");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void getDataFail(String msg, int action) {

        switch (action) {
            case (ACTION_DEFAULT + 4):
                ToastUtils.centerToastWhite(this, msg);
                break;
        }

    }

    @Override
    protected MyStreetPresenter createPresenter() {
        return new MyStreetPresenter(this);
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

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventShare(String action) {
        switch (action) {
            case Constant.EVENT_SHARE_SUCCESS:
                dismissDialog();
                break;
        }
    }

}
