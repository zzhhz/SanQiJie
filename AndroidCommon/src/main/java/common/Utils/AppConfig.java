package common.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import common.Common;


public class AppConfig {
    //默认用包名作为,sp名字
    private static SharedPreferences mSharedPreferences= PreferenceManager.getDefaultSharedPreferences(Common.getApplication());
    private static TelephonyManager tm;
    private static PackageManager manager;
    private static String versionName;
    private static String androidId ;//设备ID
    public static final boolean LOG_DEBUG = false;
    /**
     * 获取TelephonyManager单列
     *
     * @param context
     * @return
     */
    private static TelephonyManager getTelephonyManager(Context context){
        if(tm==null){
            tm=(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        }
        return tm;
    }

    public static String getString(String key, String defValue){
        return mSharedPreferences.getString(key, defValue);
    }
    public static boolean getBoolean(String key, boolean defValue){
        return mSharedPreferences.getBoolean(key, defValue);
    }
    public static String getString(String key){
        return mSharedPreferences.getString(key, null);
    }
    public static boolean getBoolean(String key){
        return mSharedPreferences.getBoolean(key, false);
    }
    public static void putString(String key, String value){
        SharedPreferences.Editor edit=mSharedPreferences.edit();
        edit.putString(key, value);
        edit.commit();
    }
    public static void putBoolean(String key, boolean value){
        SharedPreferences.Editor edit=mSharedPreferences.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }
    public static void putInt(String key, int value) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public static int getInt(String key) {
        return mSharedPreferences.getInt(key, 0);
    }
    public static void clear(){
        SharedPreferences.Editor edit=mSharedPreferences.edit();
        edit.clear();
        edit.commit();
    }


    /**
     * 获取软件版本
     */
    public static String getVersion(Context context){
        if(versionName==null){
            if(manager==null){
                manager=context.getApplicationContext().getPackageManager();
            }
            try {
                PackageInfo info=manager.getPackageInfo(context.getApplicationContext().getPackageName(), 0);
                versionName=info.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return versionName;
    }
    /**
     * 判断系统语言是否是中文 , 一个布尔类型的方法,  返回true就是 中文
     * @return
     */
    public static boolean isZh(){
        Locale locale = Common.getApplication().getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh"))
            return true;
        else
            return false;
    }

    public static boolean saveStringArray(List<String> sKey) {
        SharedPreferences.Editor edit=mSharedPreferences.edit();
        edit.putInt("Status_size",sKey.size()); /*sKey is an array*/
        for(int i=0;i<sKey.size();i++) {
            edit.remove("Status_" + i);
            edit.putString("Status_" + i, sKey.get(i));
        }
        return edit.commit();
    }

    public static List<String> loadArray(Context mContext) {
        List<String> sKey = new ArrayList<>();
        sKey.clear();
        int size = getInt("Status_size");
        for(int i=0;i<size;i++) {
            sKey.add(getString("Status_" + i, null));
        }
        return sKey;
    }
}
