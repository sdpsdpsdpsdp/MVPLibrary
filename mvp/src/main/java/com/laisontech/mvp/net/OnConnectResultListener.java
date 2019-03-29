package com.laisontech.mvp.net;

/**
 * Created by SDP on 2018/4/13.
 */

public interface OnConnectResultListener {
    void onResponse(String response, Object tag);

    void onError(String errorMsg, Object tag);
}
