package common.Utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.lang.reflect.Method;

/**
 * Created by Beidouht on 2017/6/2.
 */

public class AndroidWorkaround {

    public static void assistActivity(View content,boolean isRemove) {
        new AndroidWorkaround(content,isRemove);
    }

    private View mChildOfContent;
    private int usableHeightPrevious;
    private ViewGroup.LayoutParams frameLayoutParams;

    private AndroidWorkaround(final View content, boolean isRemove) {
        mChildOfContent = content;
        if (isRemove){
            mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(layoutListenerRemove);
        }else {
            mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);
        }
        frameLayoutParams = mChildOfContent.getLayoutParams();
    }
    private ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        public void onGlobalLayout() {
            possiblyResizeChildOfContent();
        }
    };
    private ViewTreeObserver.OnGlobalLayoutListener layoutListenerRemove = new ViewTreeObserver.OnGlobalLayoutListener() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public void onGlobalLayout() {
            possiblyResizeChildOfContentRemove();
        }
    };
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            frameLayoutParams.height = usableHeightNow;
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void possiblyResizeChildOfContentRemove() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            frameLayoutParams.height = usableHeightNow;
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
            mChildOfContent.getViewTreeObserver().removeOnGlobalLayoutListener(layoutListenerRemove);
        }
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.bottom);
    }

    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;

    }

}
