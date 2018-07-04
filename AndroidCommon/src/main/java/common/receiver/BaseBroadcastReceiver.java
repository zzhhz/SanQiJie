package common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import common.Common;
import common.Log.LogUtils;

/**
 * Created by Beidouht on 2017/8/1.
 */
public abstract class BaseBroadcastReceiver extends BroadcastReceiver {
    private static final String KEY_REPORT = "base_broadcast";
    private static final boolean DEBUG = Common.sAppDebug;
    private static final boolean sCatchError = true;

    protected abstract void onReceiveImpl(Context context, Intent intent);

    @Override
    public final void onReceive(Context context, Intent intent) {
        if (!DEBUG && Common.sGlobalCatchError && sCatchError) {
            try {
                onReceiveImpl(context, intent);
            } catch (Throwable t) {
                LogUtils.e(getClass().getSimpleName()+"onViewCreated"+t.toString());
            }
        } else {
            onReceiveImpl(context, intent);
        }
    }
}
