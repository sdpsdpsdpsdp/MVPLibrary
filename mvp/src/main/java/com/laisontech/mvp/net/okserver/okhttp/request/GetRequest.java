package com.laisontech.mvp.net.okserver.okhttp.request;

import com.laisontech.mvp.net.okserver.okhttp.model.HttpMethod;
import com.laisontech.mvp.net.okserver.okhttp.request.base.NoBodyRequest;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 描    述：Get请求的实现类，注意需要传入本类的泛型
 */
public class GetRequest<T> extends NoBodyRequest<T, GetRequest<T>> {

    public GetRequest(String url) {
        super(url);
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.GET;
    }

    @Override
    public Request generateRequest(RequestBody requestBody) {
        Request.Builder requestBuilder = generateRequestBuilder(requestBody);
        return requestBuilder.get().url(url).tag(tag).build();
    }
}
