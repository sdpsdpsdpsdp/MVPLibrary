package com.laisontech.mvp.net;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by SDP on 2018/4/13.
 */

public interface OnConnectResultListener {
    void onResponse(Response response);

    void onError(Call call, Exception e, Object tag);
}
