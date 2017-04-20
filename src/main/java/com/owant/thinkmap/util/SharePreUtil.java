package com.owant.thinkmap.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

/**
 * Created by owant on 2016/9/1.
 */
public class SharePreUtil {
    private static final String PREFRENCE_NAME = "base_pre";
    private SharedPreferences mSharedPrefs;

    private SharePreUtil() {
    }

    private static class InstanceHolder {
        public static SharePreUtil sInstance = new SharePreUtil();
    }

    public static SharePreUtil getInstance() {
        return InstanceHolder.sInstance;
    }

    public void init(Context context) {
        mSharedPrefs = context.getSharedPreferences(PREFRENCE_NAME, Context.MODE_PRIVATE);
    }

    public String getString(String key) {
        if (mSharedPrefs == null) return null;
        return mSharedPrefs.getString(key, "");
    }

    public void putString(String key, String value) {
        if (mSharedPrefs == null) return;
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putString(key, value);
        if (Build.VERSION.SDK_INT >= 9)
            editor.apply();
        else
            editor.commit();
    }

    public void remove(String key) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.remove(key);

        if (Build.VERSION.SDK_INT >= 9) {
            editor.apply();
        } else {
            editor.commit();
        }
    }
}
