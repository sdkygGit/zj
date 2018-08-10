package com.base.ad;

import android.app.Application;
import android.content.Context;

//import com.base.ad.api.ApiClient;

//import com.tencent.android.otherPush.StubAppUtils;
import com.base.ad.api.ApiClient;
import com.tencent.android.tpush.XGPushConfig;

public class AppContext extends Application{
    private static AppContext instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();

        XGPushConfig.enableOtherPush(getApplicationContext(), true);
        XGPushConfig.setHuaweiDebug(false);

        XGPushConfig.enableDebug(this, false);
//        XGPushConfig.getToken(this);
//        XGPushConfig.setMiPushAppId(getApplicationContext(), "APPID");
//        XGPushConfig.setMiPushAppKey(getApplicationContext(), "APPKEY");
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
        ApiClient.init(this);
    }


}
