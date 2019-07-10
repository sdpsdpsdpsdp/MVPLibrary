package com.laisontech.mvp.net.okconnect;

import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.laisontech.mvp.BuildConfig;
import com.laisontech.mvp.net.OnConnectResultListener;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by SDP on 2018/4/13.
 * 使用该框架简单方便的执行数据请求操作
 * <p>
 * 2019/07/10，暴露拦截器，用于增加头文件
 */

public class OkHttpConnect {
    private int mDefaultDuration = 15 * 1000;//时长10s
    private int mDefaultMaxConnectTimes = 3;//最大的重连次数
    private static OkHttpConnect mInstance;
    private Random mRandom;
    private List<Interceptor> mInterceptors = null;

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
     * log拦截器获取请求过程
     */
    public class LogInterceptor implements Interceptor {
        private static final String LOG = "OkHttpLogger";
        private boolean debug;

        public LogInterceptor(boolean debug) {
            this.debug = debug;
        }

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request();
            if (debug) {
                Log.i(LOG, String.format("Log Request : %s on %s%n%s%n%s",
                        request.url(), chain.connection(), request.headers(), request.body()));
            }
            return chain.proceed(request);
        }
    }

    /**
     * 添加请求头增加header添加样式
     */
    private class AddHeader implements Interceptor {

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request()
                    .newBuilder()
                    .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                    .addHeader("Accept-Encoding", "gzip, deflate")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("Accept", "*/*")
                    .addHeader("Cookie", "add cookies here")
                    .build();
            return chain.proceed(request);
        }
    }

    /**
     * Token过期处理 emp
     */
    private class TokenInterceptorEmp implements Interceptor {

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request();//获取请求例
            Response response = chain.proceed(request);//响应实例
            ResponseBody responseBody = response.peekBody((1024 * 1024));
            String responseStr = responseBody.string();
            //进行判断是否过去，过去进行重新请求

            return response;
        }
    }

    /**
     * 取消Tag
     */
    public void cancelTag(Object tag) {
        OkHttpUtils.getInstance().cancelTag(tag);
    }

    public OkHttpBuilder.Builder buildConnectBuilder() {
        return buildConnectBuilder(mDefaultDuration, mDefaultMaxConnectTimes, mInterceptors);
    }

    public OkHttpBuilder.Builder buildConnectBuilder(Interceptor... interceptors) {
        if (interceptors != null && interceptors.length > 0) {
            mInterceptors = Arrays.asList(interceptors);
        }
        return buildConnectBuilder(mDefaultDuration, mDefaultMaxConnectTimes, mInterceptors);
    }

    public OkHttpBuilder.Builder buildConnectBuilder(int connectDuration, int maxRetryTimes) {
        return buildConnectBuilder(connectDuration, maxRetryTimes, mInterceptors);
    }

    /**
     * 设置基本参数
     *
     * @param connectDuration 连接时长
     * @param maxRetryTimes   最大重连次数
     * @param interceptors    拦截器
     */
    public OkHttpBuilder.Builder buildConnectBuilder(int connectDuration, int maxRetryTimes, Interceptor... interceptors) {
        if (interceptors != null && interceptors.length > 0) {
            mInterceptors = Arrays.asList(interceptors);
        }
        return buildConnectBuilder(connectDuration, maxRetryTimes, mInterceptors);
    }

    /**
     * 设置基本参数
     *
     * @param connectDuration 连接时长
     * @param maxRetryTimes   最大重连次数
     * @param interceptors    拦截器
     */
    public OkHttpBuilder.Builder buildConnectBuilder(int connectDuration, int maxRetryTimes, List<Interceptor> interceptors) {
        mDefaultDuration = connectDuration;
        mDefaultMaxConnectTimes = maxRetryTimes;
        mInterceptors = interceptors;
        return new OkHttpBuilder.Builder()
                .readDuration(mDefaultDuration)
                .writeDuration(mDefaultDuration)
                .connectDuration(mDefaultDuration)
                .addInterceptor(interceptors)
                .maxRetryTimes(mDefaultMaxConnectTimes);
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
