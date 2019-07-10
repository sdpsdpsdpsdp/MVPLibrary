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
public abstract class ResultWithResponseCallback extends Callback<ResultWithResponse> {
    @Override
    public ResultWithResponse parseNetworkResponse(Response response, int id) throws IOException {
        if (response == null) return null;
        return new ResultWithResponse(response, response.request().tag());
    }
}
