package com.laisontech.mvp.net.okconnect;

import com.laisontech.mvp.net.OnConnectResultListener;

import java.io.File;
import java.util.LinkedHashMap;

/**
 * Created by SDP on 2018/4/13.
 * 使用该框架简单方便的执行数据请求操作
 */

public class OkHttpConnect {
    private int mDefaultDuration = 15 * 1000;//时长10s
    private int mDefaultMaxConnectTimes = 3;//最大的重连次数
    private static OkHttpConnect mInstance;

    private OkHttpConnect() {
    }

    public static OkHttpConnect getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpConnect.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpConnect();
                }
            }
        }
        return mInstance;
    }

    /**
     * 取消Tag
     */
    public void cancelTag(Object tag) {
        OkHttpUtils.getInstance().cancelTag(tag);
    }

    /**
     * 设置基本参数
     *
     * @param connectDuration 连接时长
     * @param maxRetryTimes   最大重连次数
     * @return 构建参数
     */
    public OkHttpBuilder.Builder buildConnectBuilder(int connectDuration, int maxRetryTimes) {
        mDefaultDuration = connectDuration;
        mDefaultMaxConnectTimes = maxRetryTimes;
        return new OkHttpBuilder.Builder()
                .readDuration(mDefaultDuration)
                .writeDuration(mDefaultDuration)
                .connectDuration(mDefaultDuration)
                .maxRetryTimes(mDefaultMaxConnectTimes);
    }

    private OkHttpBuilder.Builder buildConnectBuilder() {
        return buildConnectBuilder(mDefaultDuration, mDefaultMaxConnectTimes);
    }

    /**
     * get String请求
     */
    public void buildGetString(String url, String tag, OnConnectResultListener listener) {
        buildConnectBuilder().build().buildGetString(url, tag, listener);
    }

    /**
     * post String请求
     */
    public void buildPostString(String url, String tag, LinkedHashMap<String, String> map, OnConnectResultListener listener) {
        buildConnectBuilder().build().buildPostString(url, tag, map, listener);
    }

    /**
     * post json
     */
    public void buildPostJson(String url, String tag, Object jsonObj, OnConnectResultListener listener) {
        buildConnectBuilder().build().buildPostJson(url, tag, jsonObj, listener);
    }

    /**
     * post file
     */
    public void buildPostFile(String url, String tag, File file, OnConnectResultListener listener) {
        buildConnectBuilder().build().buildPostFile(url, tag, file, listener);
    }
}
