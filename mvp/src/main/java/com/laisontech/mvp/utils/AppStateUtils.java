package com.laisontech.mvp.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * Created by SDP
 * on 2019/7/10
 * Des：
 */
public class AppStateUtils {
    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
}
