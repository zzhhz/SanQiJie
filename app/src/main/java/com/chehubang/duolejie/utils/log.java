package com.chehubang.duolejie.utils;

import android.text.TextUtils;
import android.util.Log;

import com.chehubang.duolejie.BuildConfig;

/**
 * Created by ZZH on 2018/1/29.
 *
 * @Date: 2018/1/29
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description: 日志文件
 */
public class log {
    public static final int LENGTH = 5000;
    public static final int LENGTH_2 = 3000;
    private static final String TAG = "---log---";
    /**
     * 类名
     */
    static String className;
    /**
     * 方法名
     */
    static String methodName;
    /**
     * 行数
     */
    static int lineNumber;

    /**
     * @param msg 日志信息
     */
    public static void d(String msg) {
        if (BuildConfig.DEBUG) {
            if (TextUtils.isEmpty(msg)) {
                return;
            }
            getMethodNames(new Throwable().getStackTrace());
            if (msg.length() > LENGTH) {
                for (int i = 0; i < msg.length(); i += LENGTH_2) {
                    if (i + LENGTH_2 < msg.length()) {
                        Log.d(TAG, createLog(msg.substring(i, i + LENGTH_2)));
                    } else {
                        Log.d(TAG, createLog(msg.substring(i, msg.length())));
                    }
                }
            } else {
                Log.d("---", createLog(msg));
            }
        }
    }

    /**
     * @param msg 日志信息
     */
    public static void e(String msg) {
        if (BuildConfig.DEBUG) {
            if (TextUtils.isEmpty(msg)) {
                return;
            }
            getMethodNames(new Throwable().getStackTrace());
            Log.e("---", createLog(msg));
        }
    }

    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }

    private static String createLog(String log) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(className);
        buffer.append("(").append(methodName).append(":").append(lineNumber).append(")");
        buffer.append(log);
        return buffer.toString();
    }
}
