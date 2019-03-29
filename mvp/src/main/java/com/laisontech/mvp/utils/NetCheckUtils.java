package com.laisontech.mvp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.util.Patterns;

/**
 * Created by SDP on 2017/5/22.
 */

public class NetCheckUtils {
    /**
     * 获取当前网路连接状态
     */
    public static boolean isNetwork(Context activity) {
        if (activity == null) return false;
        ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }

        @SuppressLint("MissingPermission") NetworkInfo[] info = manager.getAllNetworkInfo();
        if (info == null) {
            return false;
        }

        NetworkInfo.State state;
        boolean connected = false;
        try {
            for (NetworkInfo networkInfo : info) {
                if (networkInfo == null) continue;
                state = networkInfo.getState();
                if (state != NetworkInfo.State.CONNECTED) {
                    continue;
                }

                connected = true;
                break;
            }
        } catch (NullPointerException e) {
            Log.e("LoginFunc", "GetNetworkState: ", e);
        }
        return connected;
    }

    //判断一个web网址是否合法
    public static boolean isValidUrl(String url) {
        return Patterns.WEB_URL.matcher(url).matches();
    }
}
