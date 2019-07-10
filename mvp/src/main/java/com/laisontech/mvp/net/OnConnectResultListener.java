package com.laisontech.mvp.net;

import okhttp3.Call;

/**
 * Created by SDP on 2018/4/13.
 */

public abstract class OnConnectResultListener {
    public abstract void onResponse(String response, Object tag);

    public void onError(String errorMsg, Object tag) {
    }

    public void onError(Call call, Exception e, Object tag) {
    }
}
