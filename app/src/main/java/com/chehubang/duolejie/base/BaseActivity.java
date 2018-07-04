package com.chehubang.duolejie.base;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chehubang.duolejie.modules.home.activity.MainActivity;
import com.chehubang.duolejie.utils.AppManager;
import com.chehubang.duolejie.widget.LoadingDialog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import common.Utils.ToastUtils;
import common.mvp.activity.CommBaseActivity;

/**
 * Created by Beidouht on 2017/10/13.
 */
public abstract class BaseActivity<P extends MvpPresenter> extends CommBaseActivity {

    protected P mvpPresenter;
    public static boolean isForeground = false;//Jpush、设备是否在前端，是true，后台运行false
    public BaseActivity activity;
    private FrameLayout mContentLayout;
    private FrameLayout titleBar;
    private TextView baseback;
    public TextView basetitle;
    private TextView baseRight;
    // private SlidingPaneLayout.PanelSlideListener mPanelSlideListener;
    protected LoadingDialog mLoadingDialog;

    public void showDialog(String msg) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        if (!TextUtils.isEmpty(msg)) {
            mLoadingDialog.setShowText(msg);
        }
        mLoadingDialog.show();
    }

    public void showDialog() {
        showDialog(null);
    }

    public void dismissDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mvpPresenter = createPresenter();
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        AppManager.getAppManager().addActivity(this);
        activity = this;
        setMiuiStatusBarDarkMode(this, true);
        setMeizuStatusBarDarkIcon(this, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isForeground = true;
       /* if (StringUtils.getAPNType(getApplicationContext()) == 0) {
           DialogHelper.showRemindDialog(activity, "网络无连接，请检查网络", "确定", new DialogHelper.DialogListener() {
                @Override
                public void handleMessage() {
                }
           });
        }*/
    }

    private long mExitTime = 0;

    @Override
    public void onBackPressed() {
        if (this instanceof MainActivity) {
            if ((System.currentTimeMillis() - mExitTime) < 2000) {
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "再按一次退出多乐街", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isForeground = false;
        // MobclickAgent.onPageEnd("SplashScreen"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().removeActivity(this);
        if (mvpPresenter != null) {
            mvpPresenter.detachView();
        }
    }


    public <T extends View> T findAtyViewById(int id) {
        return (T) findViewById(id);
    }

    public void showMsg(String message) {
        ToastUtils.centerToastWhite(getApplicationContext(), message);
    }

    protected abstract P createPresenter();

    public static boolean setMiuiStatusBarDarkMode(Activity activity, boolean darkmode) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return false;
    }

    public static boolean setMeizuStatusBarDarkIcon(Activity activity, boolean dark) {
        boolean result = false;
        if (activity != null) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                result = true;
            } catch (Exception e) {
            }
        }
        return result;
    }

}
