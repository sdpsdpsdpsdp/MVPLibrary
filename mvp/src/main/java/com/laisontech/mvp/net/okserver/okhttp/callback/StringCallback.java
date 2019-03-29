package com.laisontech.mvp.net.okserver.okhttp.callback;

import android.util.Log;

import com.laisontech.mvp.net.OnConnectResultListener;
import com.laisontech.mvp.net.okserver.okhttp.convert.StringConvert;
import com.laisontech.mvp.net.okserver.okhttp.request.base.Request;

import okhttp3.Response;

/**
 * 描    述：返回Json字符串数据
 */
public class StringCallback extends AbsCallback<String> {

    private StringConvert convert;
    private OnConnectResultListener listener;

    public StringCallback(OnConnectResultListener listener) {
        convert = new StringConvert();
        this.listener = listener;
    }

    @Override
    public String convertResponse(Response response) throws Throwable {
        String s = convert.convertResponse(response);
        response.close();
        return s;
    }

    @Override
    public void onStart(Request<String, ? extends Request> request) {
        Log.e("TAG", "onStart: " );
    }

    @Override
    public void onFinish() {
        Log.e("TAG", "onStart: " );
    }

    @Override
    public void onError(com.laisontech.mvp.net.okserver.okhttp.model.Response<String> response) {
        if (listener != null) {
        }
    }

    @Override
    public void onSuccess(com.laisontech.mvp.net.okserver.okhttp.model.Response<String> response) {
        if (listener != null) {
        }
    }

}
