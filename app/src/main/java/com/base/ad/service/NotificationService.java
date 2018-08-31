package com.base.ad.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.base.ad.db.DBOpenHelper;
import com.base.ad.notification.XGNotification;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class NotificationService {
    private DBOpenHelper dbOpenHelper;
    private static NotificationService instance = null;

    public NotificationService(Context context) {
        this.dbOpenHelper = new DBOpenHelper(context);
    }

    public synchronized static NotificationService getInstance(Context ctx) {
        if (null == instance) {
            instance = new NotificationService(ctx);
        }
        return instance;
    }

    public void save(XGNotification notification) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("msg_id", notification.getMsg_id());
        values.put("title", notification.getTitle());
        values.put("content", notification.getContent());
        values.put("activity", notification.getActivity());
        values.put("notificationActionType", notification.getNotificationActionType());
        values.put("update_time", notification.getUpdate_time());
        db.insert("notification", null, values);
    }

    public void delete(Integer id) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.delete("notification", "id=?", new String[]{id.toString()});
    }

    public void deleteAll() {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.delete("notification", "", null);
    }

    public void update(XGNotification notification) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("msg_id", notification.getMsg_id());
        values.put("title", notification.getTitle());
        values.put("content", notification.getContent());
        values.put("activity", notification.getActivity());
        values.put("notificationActionType", notification.getNotificationActionType());
        values.put("update_time", notification.getUpdate_time());
        db.update("notification", values, "id=?", new String[]{notification
                .getId().toString()});
    }

    public XGNotification find(Integer id) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db
                .query("notification",
                        new String[]{"id,msg_id,title,content,activity,notificationActionType,update_time"},
                        "id=?", new String[]{id.toString()}, null, null,
                        null, "1");
        try {
            if (cursor.moveToFirst()) {
                return new XGNotification(cursor.getInt(cursor
                        .getColumnIndex("id")), cursor.getLong(cursor
                        .getColumnIndex("msg_id")), cursor.getString(cursor
                        .getColumnIndex("title")), cursor.getString(cursor
                        .getColumnIndex("content")), cursor.getString(cursor
                        .getColumnIndex("activity")), cursor.getInt(cursor
                        .getColumnIndex("notificationActionType")), cursor.getString(cursor
                        .getColumnIndex("update_time")));
            }
            return null;
        } finally {
            cursor.close();
        }
    }

    public List<XGNotification> getScrollData(int currentPage, int lineSize,
                                              String msg_id) {
        String firstResult = String.valueOf((currentPage - 1) * lineSize);
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            if (msg_id == null || "".equals(msg_id)) {
                cursor = db
                        .query("notification",
                                new String[]{"id,msg_id,title,content,activity,notificationActionType,update_time"},
                                null, null, null, null, "update_time DESC",
                                firstResult + "," + lineSize);


            } else {
                cursor = db
                        .query("notification",
                                new String[]{"id,msg_id,title,content,activity,notificationActionType,update_time"},
                                "msg_id like ?", new String[]{msg_id + "%"},
                                null, null, "update_time DESC", firstResult
                                        + "," + lineSize);
            }
            List<XGNotification> notifications = new ArrayList<XGNotification>();
            while (cursor.moveToNext()) {
                notifications.add(new XGNotification(cursor.getInt(cursor
                        .getColumnIndex("id")), cursor.getLong(cursor
                        .getColumnIndex("msg_id")), cursor.getString(cursor
                        .getColumnIndex("title")), cursor.getString(cursor
                        .getColumnIndex("content")), cursor.getString(cursor
                        .getColumnIndex("activity")), cursor.getInt(cursor
                        .getColumnIndex("notificationActionType")), cursor.getString(cursor
                        .getColumnIndex("update_time"))));
            }
            return notifications;
        } finally {
            cursor.close();
        }
    }


    public List<XGNotification> getScrollData2(int currentPage, int lineSize,
                                               String msg_id) {
        int firstResult = (currentPage - 1) * lineSize;
        if (msg_id == null || "".equals(msg_id)) {

            return LitePal.select(new String[]{"id,msg_id,title,content,activity,notificationActionType,update_time"})
                    .order("update_time DESC")
                    .limit(10).offset(firstResult)
                    .find(XGNotification.class);
        } else {

            return LitePal.select(new String[]{"id,msg_id,title,content,activity,notificationActionType,update_time"})
                    .where("msg_id like ?", msg_id + "%")
                    .order("update_time DESC")
                    .limit(10).offset(firstResult)
                    .find(XGNotification.class);
        }
    }

    public int getCount() {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from notification", null);
        try {
            cursor.moveToFirst();
            return cursor.getInt(0);
        } finally {
            cursor.close();
        }
    }
}
