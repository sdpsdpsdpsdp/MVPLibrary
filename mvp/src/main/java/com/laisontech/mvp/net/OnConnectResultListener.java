package com.laisontech.mvp.net;

import okhttp3.Call;

/**
 * Created by SDP on 2018/4/13.
 */

public interface OnConnectResultListener {
    void onResponse(String response, Object tag);

    void onError(Call call, Exception e, Object tag);
}
