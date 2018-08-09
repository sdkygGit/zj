package com.base.ad;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

public class AppCrashHandler implements Thread.UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private static AppCrashHandler INSTANCE = new AppCrashHandler();
    private Context mContext;

    private AppCrashHandler() {
    }

    public static AppCrashHandler getInstance() {
        return INSTANCE;
    }
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (mDefaultHandler != null && (BuildConfig.DEBUG || (!handleException(ex)))) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }
    public void init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }
    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return false;
        }
        ex.printStackTrace();

        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "OSC异常；正准备重启！！", Toast.LENGTH_LONG).show();
//                CacheManager.saveString(mContext, "AppCrashHandler.txt", ex.getMessage());
                Looper.loop();
            }
        }.start();

//        AppContext.getInstance().clearAppCache();

//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


        return true;
    }
}
