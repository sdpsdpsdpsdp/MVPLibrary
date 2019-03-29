package com.laisontech.mvp.net.okserver.okhttp.request;

import com.laisontech.mvp.net.okserver.okhttp.model.HttpMethod;
import com.laisontech.mvp.net.okserver.okhttp.request.base.BodyRequest;

import okhttp3.Request;
import okhttp3.RequestBody;

public class PatchRequest<T> extends BodyRequest<T, PatchRequest<T>> {

    public PatchRequest(String url) {
        super(url);
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.PATCH;
    }

    @Override
    public Request generateRequest(RequestBody requestBody) {
        Request.Builder requestBuilder = generateRequestBuilder(requestBody);
        return requestBuilder.patch(requestBody).url(url).tag(tag).build();
    }
}
