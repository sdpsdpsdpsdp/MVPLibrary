package com.laisontech.mvp.net;

import com.laisontech.mvp.net.okconnect.callback.ResultWithResponse;

/**
 * Created by SDP on 2018/4/13.
 */

public interface OnConnectResultListener {
    void onResponse(ResultWithResponse response);

    void onError(Exception e, Object tag);
}
