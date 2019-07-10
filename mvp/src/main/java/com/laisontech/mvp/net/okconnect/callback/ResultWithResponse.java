package com.laisontech.mvp.net.okconnect.callback;

import okhttp3.Response;

/**
 * Created by SDP
 * on 2019/3/29
 * Des：
 */
public class ResultWithResponse {
    private Response response;
    private Object tag;

    public ResultWithResponse(Response response, Object tag) {
        this.response = response;
        this.tag = tag;
    }

    public Response getResponse() {
        return response;
    }

    public Object getTag() {
        return tag;
    }
}
