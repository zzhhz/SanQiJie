package com.chehubang.duolejie.modules.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.modules.home.presenter.MainPresenter;

import common.mvp.activity.MainView;

/**
 * Created by Thinkpad on 2017/12/25.
 */

public class WebviewActivity extends BaseActivity<MainPresenter> implements MainView, View.OnClickListener {
    private WebView webView;
    private String url;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        webView = (WebView) findViewById(R.id.webview);
        ImageView back = (ImageView) findViewById(R.id.iv_webview_back);
        TextView titlename = (TextView) findViewById(R.id.tv_webview_title);
        back.setOnClickListener(this);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
        titlename.setText(title);
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
        webView.getSettings().setLoadWithOverviewMode(true);
        /* HTML5的地理位置服务,设置为true,启用地理定位 */
        webView.getSettings().setGeolocationEnabled(true);
        /* 设置是否允许webview使用缩放的功能,我这里设为false,不允许 */
        webView.getSettings().setBuiltInZoomControls(false);
        /* 提高网页渲染的优先级 */
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
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
    protected MainPresenter createPresenter() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_webview_back:
                finish();
                break;
            default:
                break;
        }
    }
}
