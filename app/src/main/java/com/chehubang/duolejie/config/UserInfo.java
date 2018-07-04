package com.chehubang.duolejie.config;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.chehubang.duolejie.SqjApplication;

import common.Utils.SharedPrefs;

/**
 * Created by Beidouht on 2017/10/23.
 */

public class UserInfo {
    private String id;
    private String nick_name;
    private String user_money;
    private String user_lettory;
    private String user_header;
    private String user_tel;
    private String sex;
    private String invite_code;

    public String getInvite_code() {
        return invite_code;
    }

    public void setInvite_code(String invite_code) {
        this.invite_code = invite_code;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getUser_money() {
        return user_money;
    }

    public void setUser_money(String user_money) {
        this.user_money = user_money;
    }

    public String getUser_lettory() {
        return user_lettory;
    }

    public void setUser_lettory(String user_lettory) {
        this.user_lettory = user_lettory;
    }

    public String getUser_header() {
        return user_header;
    }

    public void setUser_header(String user_header) {
        this.user_header = user_header;
    }

    public String getUser_tel() {
        return user_tel;
    }

    public void setUser_tel(String user_tel) {
        this.user_tel = user_tel;
    }

    public String getId() {
        if (TextUtils.isEmpty(id)) {
            return "";
        } else {
            return id;
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    private static final UserInfo sInstance = new UserInfo();

    public static UserInfo getInstance() {
        return sInstance;
    }

    public static final String USER_INFO = "user_info";          //用户信息（缓存）
    public static final String ID = "app_login_id";
    public static final String NICK_NAME = "nick_name";
    public static final String USER_MONEY = "user_money";
    public static final String USER_LETTORY = "user_lettory";
    public static final String USER_HEADER = "user_header";
    public static final String USER_TEL = "user_tel";
    public static final String USER_SEX = "user_sex";
    public static final String USER_CODE = "user_code";

    public static final String PREFS_FILE_USER = USER_INFO;
    private static SharedPreferences sPrefsUser = null;

    public static SharedPreferences getUserSharedPrefs(Context context) {
        if (sPrefsUser == null) {
            sPrefsUser = SharedPrefs.get(context, PREFS_FILE_USER);
        }
        return sPrefsUser;
    }

    public void writeToCache(Context context) {
        SharedPreferences userSharedPrefs = getUserSharedPrefs(context);
        SharedPreferences.Editor editor = userSharedPrefs.edit();
        editor.putString(ID, id);
        editor.putString(NICK_NAME, nick_name);
        editor.putString(USER_MONEY, user_money);
        editor.putString(USER_LETTORY, user_lettory);
        editor.putString(USER_HEADER, user_header);
        editor.putString(USER_TEL, user_tel);
        editor.putString(USER_CODE, invite_code);
        editor.commit();
    }

    public void getCache(Context context) {
        SharedPreferences userSharedPrefs = getUserSharedPrefs(context);
        id = userSharedPrefs.getString(ID, null);
        nick_name = userSharedPrefs.getString(NICK_NAME, null);
        user_money = userSharedPrefs.getString(USER_MONEY, null);
        user_lettory = userSharedPrefs.getString(USER_LETTORY, null);
        user_header = userSharedPrefs.getString(USER_HEADER, null);
        user_tel = userSharedPrefs.getString(USER_TEL, null);
        invite_code = userSharedPrefs.getString(USER_CODE, null);
    }

    public void getIdCache(Context context) {
        SharedPreferences userSharedPrefs = getUserSharedPrefs(context);
        id = userSharedPrefs.getString(ID, null);
    }

    public void clearCache(Context context) {
        SharedPreferences userSharedPrefs = getUserSharedPrefs(context);
        SharedPreferences.Editor editor = userSharedPrefs.edit();
        editor.clear();
        editor.commit();
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.remove("token");
        edit.commit();
        sInstance.setId(null);
        sInstance.setNick_name(null);
        sInstance.setUser_header(null);
        sInstance.setUser_lettory(null);
        sInstance.setUser_money(null);
        sInstance.setUser_tel(null);
    }

    private static SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(SqjApplication.getApplication());

    public static void putString(String key, String value) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
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

    public static void clear() {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.clear();
        edit.commit();
    }

    public static String getString(String key, String defValue) {
        return mSharedPreferences.getString(key, defValue);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return mSharedPreferences.getBoolean(key, defValue);
    }

    public static String getString(String key) {
        return mSharedPreferences.getString(key, null);
    }

    public static boolean getBoolean(String key) {
        return mSharedPreferences.getBoolean(key, false);
    }

    public static void putToken(String token) {
        putString("token", token);
    }

    public static String getToken() {
        return getString("token");
    }
}
