package com.laisontech.mvp.net.okconnect;


import android.os.Build;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.laisontech.mvp.net.OnConnectResultListener;
import com.laisontech.mvp.net.okconnect.callback.ResultWithTag;
import com.laisontech.mvp.net.okconnect.callback.ResultWithTagCallback;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;

/**
 * Created by SDP on 2018/4/13.
 * 连接使用
 */

class OkHttpBuilder {
    private OnConnectResultListener mConnectListener;
    private int connectDuration;//链接时间
    private int readDuration;//读取时间
    private int writeDuration;//写入时间
    private List<Interceptor> interceptors;//拦截器
    private int maxConnectTimes;//重连次数
    //当前连接次数
    private int mCurrentRetryTime = 1;

    //连接器；
    public static class Builder {
        private int connectDuration;
        private int readDuration;
        private int writeDuration;
        private int maxRetryTimes;
        private List<Interceptor> interceptors;

        public Builder connectDuration(int connectDuration) {
            this.connectDuration = connectDuration;
            return this;
        }

        public Builder readDuration(int readDuration) {
            this.readDuration = readDuration;
            return this;
        }

        public Builder writeDuration(int writeDuration) {
            this.writeDuration = writeDuration;
            return this;
        }

        public Builder maxRetryTimes(int maxRetryTimes) {
            this.maxRetryTimes = maxRetryTimes;
            return this;
        }

        public Builder addInterceptor(List<Interceptor> interceptors) {
            this.interceptors = interceptors;
            return this;
        }

        public OkHttpBuilder build() {
            return new OkHttpBuilder(this);
        }

    }

    public OkHttpBuilder(Builder builder) {
        this.connectDuration = builder.connectDuration;
        this.readDuration = builder.readDuration;
        this.writeDuration = builder.writeDuration;
        this.interceptors = builder.interceptors;
        this.maxConnectTimes = builder.maxRetryTimes;
    }

    /**
     * get请求 String(三次重发)
     */
    public void buildGetString(final String url, final Object tag, final OnConnectResultListener listener) {
        mConnectListener = listener;
        getString(url, tag, new OnRetryListener() {
            @Override
            public void onRetry() {
                buildGetString(url, tag, listener);
            }
        });
    }

    private void getString(String url, Object tag, OnRetryListener retryListener) {
        OkHttpUtils.get().url(url).tag(tag).build()
                .readTimeOut(readDuration)
                .writeTimeOut(writeDuration)
                .connTimeOut(connectDuration)
                .interceptors(interceptors)
                .execute(new UserWithTagCallback(retryListener));
    }

    /**
     * Post 请求 String
     */
    public void buildPostString(final String url, final Object tag, final LinkedHashMap<String, String> map, final OnConnectResultListener listener) {
        mConnectListener = listener;
        postString(url, tag, map, new OnRetryListener() {
            @Override
            public void onRetry() {
                buildPostString(url, tag, map, listener);
            }
        });
    }

    private void postString(String url, Object tag, LinkedHashMap<String, String> map, final OnRetryListener listener) {
        OkHttpUtils.post().url(url).tag(tag).params(map).build()
                .readTimeOut(readDuration)
                .writeTimeOut(writeDuration)
                .connTimeOut(connectDuration)
                .interceptors(interceptors)
                .execute(new UserWithTagCallback(listener));
    }

    /**
     * post 请求Json
     */
    public void buildPostJson(final String url, final Object tag, final Object objJson, final OnConnectResultListener listener) {
        mConnectListener = listener;
        postJson(url, tag, objJson, new OnRetryListener() {
            @Override
            public void onRetry() {
                buildPostJson(url, tag, objJson, listener);
            }
        });
    }

    private void postJson(String url, Object tag, Object objJson, OnRetryListener listener) {
        OkHttpUtils.postString().url(url).tag(tag).mediaType(MediaType.parse("application/json; charset=utf-8")).content(new Gson().toJson(objJson))
                .build()
                .readTimeOut(readDuration)
                .writeTimeOut(writeDuration)
                .connTimeOut(connectDuration)
                .interceptors(interceptors)
                .execute(new UserWithTagCallback(listener));
    }

    /**
     * Post File
     */
    public void buildPostFile(final String url, final Object tag, final File file, final OnConnectResultListener listener) {
        mConnectListener = listener;
        postFile(url, tag, file, new OnRetryListener() {
            @Override
            public void onRetry() {
                buildPostFile(url, tag, file, listener);
            }
        });
    }

    private void postFile(String url, Object tag, File file, final OnRetryListener listener) {
        if (!file.exists()) return;
        OkHttpUtils.postFile().url(url).tag(tag).file(file).build()
                .readTimeOut(readDuration)
                .writeTimeOut(writeDuration)
                .connTimeOut(connectDuration)
                .interceptors(interceptors)
                .execute(new UserWithTagCallback(listener));
    }

    //连接请求包含tag
    private class UserWithTagCallback extends ResultWithTagCallback {
        private OnRetryListener retryListener;

        UserWithTagCallback(OnRetryListener retryListener) {
            this.retryListener = retryListener;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            executeError(call, e, retryListener);
        }

        @Override
        public void onResponse(ResultWithTag response, int id) {
            executeResponse(response);
        }
    }


    private void executeError(Call call, Exception e, OnRetryListener retryListener) {
        if (mConnectListener == null) return;
        Object tag = null;
        if (call != null) {
            Request request = call.request();
            if (request != null) {
                tag = request.tag();
            }
        }
        if (mCurrentRetryTime >= maxConnectTimes) {
            setDefault();
            mConnectListener.onError(getNetErrorMsg(e), tag);
            mConnectListener.onError(call, e, tag);//失败了三次才会发送失败的原因
            return;
        }
        if (retryListener != null) {
            retryListener.onRetry();            //重发的事件
        }
        mCurrentRetryTime++;
    }

    private void executeResponse(ResultWithTag response) {
        if (mConnectListener == null) return;
        setDefault();
        String result = "";
        Object obj = null;
        if (response != null) {
            result = response.getResult();
            obj = response.getTag();
        }
        mConnectListener.onResponse(result, obj);
    }

    //网络请求错误提示
    private String getNetErrorMsg(Exception e) {
        if (e == null) return "unknown error!";
        String message = e.getMessage();
        if (message == null || TextUtils.isEmpty(message)) {
            return "Time out";
        }
        return message;
    }

    private void setDefault() {
        mCurrentRetryTime = 1;
    }

    //重连事件的接口
    public interface OnRetryListener {
        void onRetry();
    }
}
