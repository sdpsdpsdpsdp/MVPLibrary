package com.laisontech.mvp.net.okconnect.callback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by SDP
 * on 2019/3/29
 * Des：携带Tag的Callback
 */
public abstract class ResultWithTagCallback extends Callback<ResultWithTag> {
    @Override
    public ResultWithTag parseNetworkResponse(Response response, int id) throws IOException {
        if (response == null) return null;
        ResponseBody body = response.body();
        Request request = response.request();
        return new ResultWithTag(body == null ? "" : body.string(),
                request == null ? null : request.tag());
    }
}
