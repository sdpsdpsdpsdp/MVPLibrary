package com.laisontech.mvp.net.okconnect.callback;

import java.io.IOException;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by SDP
 * on 2019/3/29
 * Des：
 */
public class ResultWithResponse {
    private Response response;
    private Object tag;

     ResultWithResponse(Response response, Object tag) {
        this.response = response;
        this.tag = tag;
    }

    public Response getResponse() {
        return response;
    }

    public Object getTag() {
        return tag;
    }

    public String getResponseBody() {
        try {
            if (response == null) return null;
            ResponseBody body = response.body();
            if (body == null) return null;
            return body.string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
