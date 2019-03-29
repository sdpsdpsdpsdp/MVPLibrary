package com.laisontech.mvp.net.okconnect.builder;

import com.laisontech.mvp.net.okconnect.OkHttpUtils;
import com.laisontech.mvp.net.okconnect.request.OtherRequest;
import com.laisontech.mvp.net.okconnect.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
