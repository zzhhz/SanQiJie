package common.Utils;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import common.Common;

/**
 * Created by apolunor on 16/8/23.
 */
public class DeviceUtils {
    @NonNull
    private final static Context sContext = Common.sApplicationContext;

    private static String sDeviceId = "";
    public static String getDeviceId() {
        if (TextUtils.isEmpty(sDeviceId)) {
            TelephonyManager tm = (TelephonyManager) sContext.getSystemService(
                    Context.TELEPHONY_SERVICE);
            if (tm.getDeviceId() != null) {
                sDeviceId = tm.getDeviceId();
            } else {
                sDeviceId = "UNKNOWN";
            }
        }
        return sDeviceId;
    }

    private static int sScreenWidth = -1;
    private static int sScreenHeight = -1;
    private static float sScreenDensity = -1;
    private static int sScreenDensityDpi = -1;
    public static int getScreenWidth() {
        if (sScreenWidth < 0) {
            obtainScreenProperty();
        }
        return sScreenWidth;
    }

    public static int getScreenHeight() {
        if (sScreenHeight < 0) {
            obtainScreenProperty();
        }
        return sScreenHeight;
    }

    public static float getScreenDensity() {
        if (sScreenDensity < 0) {
            obtainScreenProperty();
        }
        return sScreenDensity;
    }

    public static int getScreenDensityDpi() {
        if (sScreenDensityDpi < 0) {
            obtainScreenProperty();
        }
        return sScreenDensityDpi;
    }

    private static synchronized void obtainScreenProperty() {
        if (sScreenWidth != -1) return;
        WindowManager wm = (WindowManager) sContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        sScreenWidth = outMetrics.widthPixels;
        sScreenHeight = outMetrics.heightPixels;
        sScreenDensity = outMetrics.density;
        sScreenDensityDpi = outMetrics.densityDpi;
    }

    public static String getMobileModle(){
        return TextUtils.isEmpty(Build.MODEL) ? "UNKNOWN" : Build.MODEL;
    }

    public static String getAndroidVersion(){
        return TextUtils.isEmpty(Build.VERSION.RELEASE) ? "UNKNOWN" : Build.VERSION.RELEASE;
    }

    public static boolean isMiui() {
        return "Xiaomi".equalsIgnoreCase(Build.MANUFACTURER);
    }
}
