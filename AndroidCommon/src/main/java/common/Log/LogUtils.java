package common.Log;

import android.util.Log;

/**
 * Created by Thinkpad on 2017/3/8.
 */
public class LogUtils {

    private static final String TAG = "三七街________";
    /**
     * 是否开启debug
     */
    public static boolean isDebug = true;


    /**
     * 错误
     * Write By LILIN
     * 2014-5-8
     *
     * @param msg
     */
    public static void e(String msg) {
        if (isDebug) {
            Log.e(TAG, msg + "");
        }
    }

    /**
     * 信息
     * Write By LILIN
     * 2014-5-8
     *
     * @param msg
     */
    public static void i(String msg) {
        if (isDebug) {
            Log.i(TAG, msg + "");
        }
    }

    /**
     * 警告
     * Write By LILIN
     * 2014-5-8
     *
     * @param msg
     */
    public static void w(String msg) {
        if (isDebug) {
            Log.w(TAG, msg + "");
        }
    }
}
