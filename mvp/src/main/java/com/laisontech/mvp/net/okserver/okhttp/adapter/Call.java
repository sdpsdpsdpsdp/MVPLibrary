package com.laisontech.mvp.net.okserver.okhttp.adapter;

import com.laisontech.mvp.net.okserver.okhttp.callback.Callback;
import com.laisontech.mvp.net.okserver.okhttp.model.Response;
import com.laisontech.mvp.net.okserver.okhttp.request.base.Request;

/**
 * 描    述：请求的包装类
 */
public interface Call<T> {
    /** 同步执行 */
    Response<T> execute() throws Exception;

    /** 异步回调执行 */
    void execute(Callback<T> callback);

    /** 是否已经执行 */
    boolean isExecuted();

    /** 取消 */
    void cancel();

    /** 是否取消 */
    boolean isCanceled();

    Call<T> clone();

    Request getRequest();
}
