package com.base.ad.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import com.base.ad.AppContext;
import com.base.ad.R;
import com.base.ad.SimplexToast;
import com.base.ad.utils.Rom;

public class NotificationUtils extends ContextWrapper {

    private NotificationManager manager;

    //通知渠道
    public static final String id = "channel_order";
    public static final String name = "订单通知";


    public NotificationUtils(Context base) {
        super(base);
    }

    private synchronized NotificationManager getManager() {
        synchronized (NotificationUtils.class){
            if (manager == null) {
                manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            }
        }
        return manager;
    }

    @TargetApi(Build.VERSION_CODES.O)
    Notification.Builder getChannelNotification(String title, String content) {
        return new Notification.Builder(getApplicationContext(), id)
                .setContentTitle(title)
                .setContentText(content)
                .setNumber(2)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setAutoCancel(true);
    }

    NotificationCompat.Builder getNotification_25(String title, String content) {
        return new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true);
    }

    public void sendNotification(String title, String content) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
            channel.setShowBadge(true);
            getManager().createNotificationChannel(channel);

            if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.getId());
                startActivity(intent);
                SimplexToast.show(getApplicationContext(), "请手动将通知打开");
            }

            Notification notification = getChannelNotification
                    (title, content).build();
            getManager().notify(1, notification);

        } else {
            Notification notification = getNotification_25(title, content).build();
            getManager().notify(1, notification);
        }

        int count = AppContext.get("puch_count",0);
        count++;
        AppContext.set("puch_count",count);


        if(Rom.isEmui()){
            Bundle bundle = new Bundle();
            bundle.putString("package", getBaseContext().getPackageName());
            String launchClassName = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName()).getComponent().getClassName();
            bundle.putString("class", launchClassName);
            bundle.putInt("badgenumber", count);
            getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, bundle);
        }
    }

}
