package common.Utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

/**
 * Created by Bob on 2016/8/1.
 */

public class VersionUtils {


    private static int sTargetSdkVersion = -1;
    public static int getAppTargetSdkVersion(Context context) {
        if (sTargetSdkVersion < 0) {
            try {
                final PackageInfo info = context.getPackageManager().getPackageInfo(
                        context.getPackageName(), 0);
                sTargetSdkVersion = info.applicationInfo.targetSdkVersion;
            } catch (PackageManager.NameNotFoundException e) {
            }
        }
        return sTargetSdkVersion;
    }

    private static String sVersionName = "";
    public static String getVersionName(Context context) {
        if (TextUtils.isEmpty(sVersionName)) {
            final PackageManager packageManager = context.getPackageManager();
            try {
                PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
                sVersionName = packInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
            }
        }
        return sVersionName;
    }

    private static int sVersionCode = -1;
    public static int getVersionCode(Context context) {
        if (sVersionCode == -1) {
            final PackageManager packageManager = context.getPackageManager();
            try {
                PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
                sVersionCode = packInfo.versionCode;
            } catch (PackageManager.NameNotFoundException e) {
            }
        }
        return sVersionCode;
    }
}

