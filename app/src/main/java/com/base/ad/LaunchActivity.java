package com.base.ad;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.base.ad.ui.H5Activity;
import com.base.ad.ui.MainActivity;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class LaunchActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    final int RC_SMS = 0x88;
    Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    Intent intent = new Intent(LaunchActivity.this, H5Activity.class);
//                    Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                    intent.putExtra("url", Setting.ADMIN);
                    startActivity(intent);
                    finish();
                }
                break;
            }
        }
    };

    @Override
    protected void initWidget() {

    }

    @Override
    protected void initData() {
        requestExternalStorage();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    public void requestExternalStorage() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)) {//有權限

            mHandle.sendEmptyMessageDelayed(0, 500);
        } else {
            EasyPermissions.requestPermissions(this, "请开启读取SMS信息,SD卡,获取手机信息权限",
                    RC_SMS,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE
            );
        }
    }

    @Override
    protected int getContentView() {
        return 0;
    }

    @Override
    protected void setBinding(int layout) {

    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == RC_SMS) {
            if (perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE) && perms.contains(Manifest.permission.READ_PHONE_STATE)) {
                mHandle.sendEmptyMessageDelayed(0, 500);
            }
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == RC_SMS) {
            Toast.makeText(this, "请开启权限！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName())); // 根据包名打开对应的设置界面
            startActivity(intent);
            finish();
        }
    }
}
