package com.laisontech.mvp.net.okconnect;

import com.laisontech.mvp.net.OnConnectResultListener;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Random;

/**
 * Created by SDP on 2018/4/13.
 * 使用该框架简单方便的执行数据请求操作
 */

public class OkHttpConnect {
    private int mDefaultDuration = 15 * 1000;//时长10s
    private int mDefaultMaxConnectTimes = 3;//最大的重连次数
    private static OkHttpConnect mInstance;
    private Random mRandom;

    private OkHttpConnect() {
        mRandom = new Random();
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

    private Object buildTag(String method) {
        return method + "_" + System.currentTimeMillis() + "_" + mRandom.nextInt(Integer.MAX_VALUE);
    }

    /**
     * get String请求
     */
    public void buildGetString(String url, OnConnectResultListener listener) {
        buildGetString(url, buildTag("GET"), listener);
    }

    public void buildGetString(String url, Object tag, OnConnectResultListener listener) {
        buildConnectBuilder().build().buildGetString(url, tag, listener);
    }

    /**
     * post String请求
     */
    public void buildPostString(String url, LinkedHashMap<String, String> map, OnConnectResultListener listener) {
        buildPostString(url, buildTag("POST"), map, listener);
    }

    public void buildPostString(String url, Object tag, LinkedHashMap<String, String> map, OnConnectResultListener listener) {
        buildConnectBuilder().build().buildPostString(url, tag, map, listener);
    }

    /**
     * post json
     */
    public void buildPostJson(String url, Object jsonObj, OnConnectResultListener listener) {
        buildPostJson(url, buildTag("JSON"), jsonObj, listener);
    }

    public void buildPostJson(String url, Object tag, Object jsonObj, OnConnectResultListener listener) {
        buildConnectBuilder().build().buildPostJson(url, tag, jsonObj, listener);
    }

    /**
     * post file
     */
    public void buildPostFile(String url, File file, OnConnectResultListener listener) {
        buildPostFile(url, buildTag("FILE"), file, listener);
    }

    public void buildPostFile(String url, Object tag, File file, OnConnectResultListener listener) {
        buildConnectBuilder().build().buildPostFile(url, tag, file, listener);
    }
}
