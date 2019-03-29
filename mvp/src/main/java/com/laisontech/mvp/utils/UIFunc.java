package com.laisontech.mvp.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by SDP on 2018/4/13.
 */

public class UIFunc {
    public static void showMessage(Activity activity, String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示提示信息
     *
     * @param context 提示信息的界面
     * @param msg     提示信息
     */
    public static void showMessage(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示提示信息
     *
     * @param activity 提示信息的界面
     * @param msgid    R.string.msgid
     */
    public static void showMessage(Activity activity, int msgid) {
        Toast.makeText(activity, msgid, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示提示信息
     *
     * @param context 提示信息的界面
     * @param msgid   R.string.msgid
     */
    public static void showMessage(Context context, int msgid) {
        Toast.makeText(context, msgid, Toast.LENGTH_SHORT).show();
    }

    /**
     * 根据ID获取字符串
     *
     * @param activity
     * @param msgid
     * @return
     */
    public static String getMessage(Context activity, int msgid) {
        String ret = activity.getResources().getString(msgid);
        return ret;
    }
}
