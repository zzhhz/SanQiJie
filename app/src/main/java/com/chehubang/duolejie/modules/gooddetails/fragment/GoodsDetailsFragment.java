package com.chehubang.duolejie.modules.gooddetails.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseFragment;
import com.chehubang.duolejie.modules.gooddetails.presenter.GoodsFragmentPresenter;

import common.mvp.activity.MainView;

/**
 * Created by Thinkpad on 2017/12/15.
 */

public class GoodsDetailsFragment extends BaseFragment<GoodsFragmentPresenter> implements MainView {

    private WebView webView;
    private String url;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_webview, container, false);
        view.findViewById(R.id.title).setVisibility(View.GONE);
        Intent intent = getActivity().getIntent();
        String id = intent.getStringExtra("id");
        http:
//39.107.14.118/appInterface/goodsDetail.jhtml?goods_id=522860622603

//        url = "http://192.168.3.25:8090/GetTreasureAppJinDong/appInterface/goodsDetail.jhtml?goods_id="+id;  //内网用
        url = "http://39.107.14.118/appInterface/goodsDetail.jhtml?goods_id=" + id;   //外网用
        //  url = getIntent().getStringExtra("url");
        webView = (WebView) view.findViewById(R.id.webview);
        /* 设置支持Js,必须设置的,不然网页基本上不能看 */
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBlockNetworkImage(false);
        /* 设置缓存模式,我这里使用的默认*/
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        /* 设置为true表示支持使用js打开新的窗口 */
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        /* 大部分网页需要自己保存一些数据,这个时候就的设置下面这个属性 */
        webView.getSettings().setDomStorageEnabled(true);
        /* 设置为使用webview推荐的窗口 */
        webView.getSettings().setUseWideViewPort(true);
        /* 设置网页自适应屏幕大小 ---这个属性应该是跟上面一个属性一起用 */
        //webView.getSettings().setLoadWithOverviewMode(true);
        /* HTML5的地理位置服务,设置为true,启用地理定位 */
        webView.getSettings().setGeolocationEnabled(true);
        /* 设置是否允许webview使用缩放的功能,我这里设为false,不允许 */
        webView.getSettings().setBuiltInZoomControls(false);
        /* 提高网页渲染的优先级 */
        //webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        /* 设置显示水平滚动条,就是网页右边的滚动条.我这里设置的不显示 */
        webView.setHorizontalScrollBarEnabled(false);
        /* 重写WebViewClient可以监听网页的跳转和资源加载等等... */
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.loadUrl(url);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.reload();
    }

    @Override
    public void getDataSuccess(Object model, int action) {

    }

    @Override
    public void getDataFail(String msg, int action) {

    }

    @Override
    protected GoodsFragmentPresenter createPresenter() {
        return null;
    }
}
