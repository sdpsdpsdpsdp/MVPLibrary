package com.laisontech.mvp.net.okserver.okhttp.callback;

import com.laisontech.mvp.net.okserver.okhttp.model.Progress;
import com.laisontech.mvp.net.okserver.okhttp.model.Response;
import com.laisontech.mvp.net.okserver.okhttp.request.base.Request;
import com.laisontech.mvp.net.okserver.okhttp.utils.OkLogger;

/**
 * 描   述：抽象的回调接口
 */
public abstract class AbsCallback<T> implements Callback<T> {

    @Override
    public void onStart(Request<T, ? extends Request> request) {
    }

    @Override
    public void onCacheSuccess(Response<T> response) {
    }

    @Override
    public void onError(Response<T> response) {
        OkLogger.printStackTrace(response.getException());
    }

    @Override
    public void onFinish() {
    }

    @Override
    public void uploadProgress(Progress progress) {
    }

    @Override
    public void downloadProgress(Progress progress) {
    }
}
