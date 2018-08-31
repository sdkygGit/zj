package com.base.ad;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.SharedPreferencesCompat;

//import com.base.ad.api.ApiClient;

//import com.tencent.android.otherPush.StubAppUtils;
import com.base.ad.api.ApiClient;
import com.tencent.android.tpush.XGPushConfig;

import org.litepal.LitePalApplication;

public class AppContext extends LitePalApplication{
    private static AppContext instance;
    private static final String PREF_NAME = "creativelocker.pref";
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();

        XGPushConfig.enableOtherPush(getApplicationContext(), true);
        XGPushConfig.setHuaweiDebug(false);

        XGPushConfig.enableDebug(this, false);
        XGPushConfig.setMiPushAppId(getApplicationContext(), "2882303761517849259");
        XGPushConfig.setMiPushAppKey(getApplicationContext(), "5951784972259");
        XGPushConfig.setMzPushAppId(this, "1001447");
        XGPushConfig.setMzPushAppKey(this, "0a999617d04840a6841b56d6095a0851");
    }


//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        StubAppUtils.attachBaseContext(base);
//    }

    public static AppContext getInstance() {
        return instance;
    }


    void init(){
        AppCrashHandler.getInstance().init(this);
//        ApiClient.init(this);
    }


    public static void set(String key, int value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putInt(key, value);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }

    public static void set(String key, boolean value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(key, value);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }

    public static void set(String key, String value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(key, value);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }

    public static boolean get(String key, boolean defValue) {
        return getPreferences().getBoolean(key, defValue);
    }

    public static String get(String key, String defValue) {
        return getPreferences().getString(key, defValue);
    }

    public static int get(String key, int defValue) {
        return getPreferences().getInt(key, defValue);
    }

    public static long get(String key, long defValue) {
        return getPreferences().getLong(key, defValue);
    }

    public static float get(String key, float defValue) {
        return getPreferences().getFloat(key, defValue);
    }

    public static SharedPreferences getPreferences() {
        return context().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized Context context() {
        return instance;
    }

}
