package common.Utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.PermissionChecker;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import common.Common;

/**
 * Created by apolunor on 16/8/22.
 */
public class PermissionUtils {
    private static final boolean DEBUG = Common.sAppDebug;

    @NonNull
    private static final Context sContext = Common.sApplicationContext;

    public static Set<String> lacksPermissions(String... permissions) {
        if (permissions == null || permissions.length == 0) {
            return Collections.EMPTY_SET;
        }
        final Set<String> lacks = new HashSet<>();
        boolean isLacks = false;
        for (String permission : permissions) {
            if (lacksPermission(permission)) {
                lacks.add(permission);
                isLacks = true;
            }
        }
        return isLacks ? lacks : Collections.EMPTY_SET;
    }

    public static boolean lacksPermission(String permission) {
        // For Android < Android M, self permissions are always granted.
        boolean result = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (VersionUtils.getAppTargetSdkVersion(sContext) >= Build.VERSION_CODES.M) {
                // targetSdkVersion >= Android M, we can
                // use Context#checkSelfPermission
                result = sContext.checkSelfPermission(permission)
                        != PackageManager.PERMISSION_GRANTED;
            } else {
                // targetSdkVersion < Android M, we have to use PermissionChecker
                result = PermissionChecker.checkSelfPermission(sContext, permission)
                        != PermissionChecker.PERMISSION_GRANTED;
            }
        }

        return result;
    }
}
