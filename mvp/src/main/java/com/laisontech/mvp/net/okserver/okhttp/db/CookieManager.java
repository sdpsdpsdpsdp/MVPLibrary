package com.laisontech.mvp.net.okserver.okhttp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.laisontech.mvp.net.okserver.okhttp.cookie.SerializableCookie;

public class CookieManager extends BaseDao<SerializableCookie> {

    private static Context context;
    private volatile static CookieManager instance;

    public static CookieManager getInstance() {
        if (instance == null) {
            synchronized (CookieManager.class) {
                if (instance == null) {
                    instance = new CookieManager();
                }
            }
        }
        return instance;
    }

    private CookieManager() {
        super(new DBHelper(context));
    }

    public static void init(Context ctx) {
        context = ctx;
    }

    @Override
    public SerializableCookie parseCursorToBean(Cursor cursor) {
        return SerializableCookie.parseCursorToBean(cursor);
    }

    @Override
    public ContentValues getContentValues(SerializableCookie serializableCookie) {
        return SerializableCookie.getContentValues(serializableCookie);
    }

    @Override
    public String getTableName() {
        return DBHelper.TABLE_COOKIE;
    }

    @Override
    public void unInit() {
    }
}
