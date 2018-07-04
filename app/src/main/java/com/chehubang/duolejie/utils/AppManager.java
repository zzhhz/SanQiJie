package com.chehubang.duolejie.utils;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by JIEXIA on 2016/9/3.
 * Activity管理类
 */

public class AppManager {

    public static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
//    public BaseActivity currentActivity() {
//        if (activityStack==null){
//            if (MainActivity.mainActivity!=null){
//                return MainActivity.mainActivity;
//            }else {
//                return null;
//            }
//        }
//        BaseActivity activity = activityStack.lastElement();
//        return activity;
//    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
//            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 移除最后一个Activity
     */
    public void removeActivity(Activity activity) {
        if (activityStack!=null&&activityStack.size()>0&&activityStack.lastElement() != null) {
            activityStack.remove(activity);
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (cls.equals(activity.getClass())) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    /*public void finishAllActivity() {
        int size = activityStack.size();
        for (int i = 0; i < size; i++) {
            if (null != activityStack.get(i)) {
                if (activityStack.get(i)!= MainActivity.mainActivity){
                    activityStack.get(i).finish();
                }
            }
        }
        activityStack.clear();
    }*/
    public void finishAllExceptActivity(Activity activity) {
        int position = activityStack.indexOf(activity);
        for (int i = 0; i < activityStack.size(); i++) {
            if (null != activityStack.get(i)) {
                if (i>position){
                    activityStack.get(i).finish();
                }
            }
        }
    }

    /**
     * 退出应用程序
     */
 /*   public void AppExit(Context context) {
        try {
            finishAllActivity();
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());

        } catch (Exception e) {
        }
    }*/

    /**
     * 结束指定的Activity
     */
    public void getActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 得到指定类名的Activity
     */
    public Activity getActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                return activity;
            }
        }
        return null;
    }
}
