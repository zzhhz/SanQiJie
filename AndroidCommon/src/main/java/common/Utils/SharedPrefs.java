package common.Utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;

import java.util.Map;
import java.util.Set;

/**
 * Created by Bob on 16/9/5.
 */
public abstract class SharedPrefs implements SharedPreferences {

    private static final LruCache<String, SharedPreferences> PREFS_CACHE = new LruCache(5);

    public static synchronized SharedPreferences get(Context context, String filename) {
        return context.getSharedPreferences(filename, Context.MODE_PRIVATE);//getImpl(context, filename, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPreferences get(Context context, String filename, int mode) {
        return getImpl(context, filename, mode);
    }

    private static synchronized SharedPreferences getImpl(Context context, String filename, int mode) {
        Context appContext = context instanceof Application ? context : context.getApplicationContext();
        SharedPreferences returnValue = PREFS_CACHE.get(filename);
        if(returnValue == null) {
            returnValue = appContext.getSharedPreferences(filename, mode);
            // TODO 暂时只提供同进程SharedPreferences，后面根据需求做跨进程SharedPreferences
            PREFS_CACHE.put(filename, returnValue);
        }
        return returnValue;
    }

    @Override
    public abstract HjEditor edit();

    public abstract static class HjEditor implements Editor {
        public HjEditor() {
        }

        public Editor putStringSet(String key, Set<String> values) {
            throw new UnsupportedOperationException("putStringSet");
        }

        public boolean commit() {
            return false;
        }

        public void apply() {
        }
    }

    @Override
    public Map<String, ?> getAll() {
        throw new UnsupportedOperationException("getAll");
    }

    @Nullable
    @Override
    public Set<String> getStringSet(String s, Set<String> set) {
        throw new UnsupportedOperationException("getStringSet");
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        throw new UnsupportedOperationException("registerOnSharedPreferenceChangeListener");
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        throw new UnsupportedOperationException("unregisterOnSharedPreferenceChangeListener");
    }
}
