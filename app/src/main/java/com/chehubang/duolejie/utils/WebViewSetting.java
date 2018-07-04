package com.chehubang.duolejie.utils;

import android.os.Build;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Thinkpad on 2017/12/18.
 */

public class WebViewSetting {

    public static void setWebview(WebView view){
        view.getSettings().setJavaScriptEnabled(true);
        view.getSettings().setBlockNetworkImage(false);
        /* 设置缓存模式,我这里使用的默认*/
        view.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        /* 设置为true表示支持使用js打开新的窗口 */
        view.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        /* 大部分网页需要自己保存一些数据,这个时候就的设置下面这个属性 */
        view.getSettings().setDomStorageEnabled(true);
        /* 设置为使用webview推荐的窗口 */
        view.getSettings().setUseWideViewPort(true);
        /* 设置网页自适应屏幕大小 ---这个属性应该是跟上面一个属性一起用 */
        view.getSettings().setLoadWithOverviewMode(true);
        /* HTML5的地理位置服务,设置为true,启用地理定位 */
        view.getSettings().setGeolocationEnabled(true);
        /* 设置是否允许webview使用缩放的功能,我这里设为false,不允许 */
        view.getSettings().setBuiltInZoomControls(false);
        /* 提高网页渲染的优先级 */
        view.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        /* 设置显示水平滚动条,就是网页右边的滚动条.我这里设置的不显示 */
        view.setHorizontalScrollBarEnabled(false);
        /* 重写WebViewClient可以监听网页的跳转和资源加载等等... */
        view.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    /**
     * 富文本展示
     *
     * @param web     初始化的web view
     * @param urlDate 展示的数据
     */
    public static void openWebView(WebView web, String urlDate) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            web.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            web.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        web.loadDataWithBaseURL(null, urlDate, "text/html", "utf-8", null);
        web.getSettings().setJavaScriptEnabled(false);
    }

    private static String getHtmlData(String bodyHTML) {
        String head = "<head><style>img{max-width: 100%; width:auto; height: auto;}</style></head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }
}
