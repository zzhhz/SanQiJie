package common.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import common.Common;
import common.Log.LogUtils;

/**
 * Created by apolunor on 16/8/16.
 */
public class BroadcastUtils {

    private static final String KEY_REPORT = "broadcast_utils";
    private static final boolean DEBUG = Common.sAppDebug;
    private static final boolean sCatchError = false;

    private static Context sContext;

    public static void init(Context context) {
        sContext = context;
    }

    public static void sendGlobalBroadcast(Intent intent){
        if (!DEBUG && Common.sGlobalCatchError && sCatchError) {
            try {
                sContext.sendBroadcast(intent);
            } catch (Throwable t) {
                LogUtils.e(KEY_REPORT+"error"+t.toString());
            }
        } else {
            sContext.sendBroadcast(intent);
        }
    }

    public static void sendGlobalOrderedBroadcast(Intent intent){
        if (!DEBUG && Common.sGlobalCatchError && sCatchError) {
            try {
                sContext.sendOrderedBroadcast(intent, null);
            } catch (Throwable t) {
                LogUtils.e(KEY_REPORT+"error"+t.toString());
            }
        } else {
            sContext.sendOrderedBroadcast(intent, null);
        }
    }

    public static void registerGlobalReceiver(BroadcastReceiver receiver, IntentFilter filter){
        if (!DEBUG && Common.sGlobalCatchError && sCatchError) {
            try {
                sContext.registerReceiver(receiver, filter);
            } catch (Throwable t) {
                LogUtils.e(KEY_REPORT+"error"+t.toString());
            }
        } else {
            sContext.registerReceiver(receiver, filter);
        }
    }

    public static void unRegisterGlobalReceiver(BroadcastReceiver receiver){
        if (!DEBUG && Common.sGlobalCatchError && sCatchError) {
            try {
                sContext.unregisterReceiver(receiver);
            } catch (Throwable t) {
                LogUtils.e(KEY_REPORT+"error"+t.toString());
            }
        } else {
            sContext.unregisterReceiver(receiver);
        }
    }

    public static void sendLocalBroadcast(Intent intent){
        if (!DEBUG && Common.sGlobalCatchError && sCatchError) {
            try {
                LocalBroadcastManager.getInstance(sContext).sendBroadcast(intent);
            } catch (Throwable t) {
                LogUtils.e(KEY_REPORT+"error"+t.toString());
            }
        } else {
            LocalBroadcastManager.getInstance(sContext).sendBroadcast(intent);
        }
    }

    public static void registerLocalReceiver(BroadcastReceiver receiver, IntentFilter filter){
        if (!DEBUG && Common.sGlobalCatchError && sCatchError) {
            try {
                LocalBroadcastManager.getInstance(sContext).registerReceiver(receiver, filter);
            } catch (Throwable t) {
                LogUtils.e(KEY_REPORT+"error"+t.toString());
            }
        } else {
            LocalBroadcastManager.getInstance(sContext).registerReceiver(receiver, filter);
        }
    }

    public static void unRegisterLocalReceiver(BroadcastReceiver receiver){
        if (!DEBUG && Common.sGlobalCatchError && sCatchError) {
            try {
                LocalBroadcastManager.getInstance(sContext).unregisterReceiver(receiver);
            } catch (Throwable t) {
                LogUtils.e(KEY_REPORT+"error"+t.toString());
            }
        } else {
            LocalBroadcastManager.getInstance(sContext).unregisterReceiver(receiver);
        }
    }

}
