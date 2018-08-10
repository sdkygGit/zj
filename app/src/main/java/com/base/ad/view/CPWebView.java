package com.base.ad.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.base.ad.R;
import com.base.ad.SimplexToast;
import com.base.ad.bean.LoginRet;
import com.base.ad.utils.Utils;
import com.google.gson.JsonObject;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.common.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class CPWebView extends WebView {

    public CPWebView(Context context) {
        super(context);
        initWebViewSettings(context, null);
    }

    public CPWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initWebViewSettings(context, attributeSet);
    }

    public CPWebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initWebViewSettings(context, attributeSet);
    }

    @SuppressLint("JavascriptInterface")
    private void initWebViewSettings(Context context, AttributeSet attributeSet) {


        WebSettings webSetting = this.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setUseWideViewPort(true);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        addJavascriptInterface(getHtmlObject(), "jsObj");

        webSetting.setSupportMultipleWindows(false);
        // webSetting.setLoadWithOverviewMode(true);
        // webSetting.setDatabaseEnabled(true);
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);
//		if (mIntentUrl == null) {
//		} else {
//			mWebView.loadUrl(mIntentUrl.toString());
//		}
//        CookieSyncManager.createInstance(getContext());
//        CookieSyncManager.getInstance().sync();

        // this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
        // settings 的设计


    }


    private Object getHtmlObject() {
        Object insertObj = new Object() {
//            @JavascriptInterface
//            public void seth(final int h) {//方法名必须是js使用的方法名
//
//                ((Activity) getContext()).runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        ViewGroup.LayoutParams params = getLayoutParams();
//                        params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, h, getResources().getDisplayMetrics());
//                        setLayoutParams(params);
//                    }
//                });
//            }

            @JavascriptInterface
            public void loginCallback(final String object) {//方法名必须是js使用的方法名

              Log.d("H5Activity",object);

                XGPushManager.registerPush(getContext(), object,
                        new XGIOperateCallback() {
                            @Override
                            public void onSuccess(Object data, int flag) {
                                Log.w(Constants.LogTag, "+++ register push sucess. token:" + data + "flag" + flag);
                            }

                            @Override
                            public void onFail(Object data, int errCode, String msg) {
                                Log.w(Constants.LogTag,
                                        "+++ register push fail. token:" + data
                                                + ", errCode:" + errCode + ",msg:"
                                                + msg);
                            }
                        });
            }

            @JavascriptInterface
            public String getDeviceId() {//方法名必须是js使用的方法名
                return Utils.getDeviceId(getContext());
            }
        };

        return insertObj;
    }
}
