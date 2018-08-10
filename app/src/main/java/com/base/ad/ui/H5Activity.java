package com.base.ad.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.base.ad.AppContext;
import com.base.ad.BaseActivity;
import com.base.ad.R;
import com.base.ad.SimplexToast;
import com.base.ad.databinding.ActivityH5Binding;
import com.base.ad.utils.Utils;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.common.Constants;
import com.tencent.mid.util.Util;

public class H5Activity extends BaseActivity implements View.OnClickListener {
    private String mCurrentUrl = "";
    ActivityH5Binding mBinding;
    //两次返回事件间隔
    private long mBackPressedTime;
    private View start;
    private View error;
    boolean isError
            = false;

    @Override
    protected int getContentView() {
        return R.layout.activity_h5;
    }

    @Override
    protected void setBinding(int layout) {
        mBinding = DataBindingUtil.setContentView(this, layout);
    }

    @Override
    protected void initWidget() {

        start = findViewById(R.id.wrap_start);
        error = findViewById(R.id.wrap_error);
        findViewById(R.id.webview_onclick).setOnClickListener(this);
        mBinding.webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mCurrentUrl = url;
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

//                if(!Utils.isNetworkAvailable(H5Activity.this)){
//
//                }else{
//                    if (view != null) {
//                        view.loadUrl("about:blank");//接收到错误时加载空页面，如果没有这个过程，在显示我们自己的错误页面之前默认错误页面会一闪而过，可自行实验验证
//                    }
//                    Toast.makeText(H5Activity.this, "加载错误！", Toast.LENGTH_SHORT).show();
//                }
                isError = true;
                //错误处理
                mBinding.webview.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mBinding.progress.setVisibility(View.GONE);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                isError= false;
                if (mBinding.webview.getVisibility() == View.GONE)
                    mBinding.webview.setVisibility(View.VISIBLE);
            }
        });

        mBinding.webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
//                onWebTitle(view, title);
                mBinding.setEmployee(title);
            }


            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
//                onWebIcon(view, icon);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) { // 进度
                super.onProgressChanged(view, newProgress);
                if (newProgress >= 99) {
                    mBinding.progress.setVisibility(View.GONE);
                    if (error.getVisibility() == View.VISIBLE&& !isError)
                        error.setVisibility(View.GONE);

                } else {

                    if (start.getVisibility() == View.VISIBLE )
                        start.setVisibility(View.GONE);

                    if (mBinding.progress.getVisibility() == View.GONE) {
                        mBinding.progress.setVisibility(View.VISIBLE);
                    }
                    mBinding.progress.setProgress(newProgress);
                }
            }
        });
    }

    @Override
    protected void initData() {
        mCurrentUrl = getIntent().getStringExtra("url");

        mBinding.webview.loadUrl(mCurrentUrl);
//        mBinding.webview.loadUrl("file:///android_asset/a.html");
    }

    @Override
    protected void onDestroy() {
        mBinding.webview.destroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        if (mBinding.webview.canGoBack()) {
            mBinding.webview.goBack();
        } else {
            long curTime = SystemClock.uptimeMillis();
            if ((curTime - mBackPressedTime) < (3 * 1000)) {
                moveTaskToBack(true);
            } else {
                mBackPressedTime = curTime;
                Toast.makeText(this, "再次点击退出应用", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onClick(View view) {
        if (!Utils.isNetworkAvailable(H5Activity.this)) {//网络是否连接
            SimplexToast.show(H5Activity.this, "请检查网络连接");
        } else {
            mBinding.webview.loadUrl(mCurrentUrl);
        }
    }
}
