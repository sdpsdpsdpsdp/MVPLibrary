package com.laisontech.mvp.net.okconnect.callback;

import android.util.Log;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by zhy on 15/12/14.
 */
public abstract class StringCallback extends Callback<String>
{
    @Override
    public String parseNetworkResponse(Response response, int id) throws IOException
    {
        Log.e("queryNet", "parseNetworkResponse: "+response );
        return response.body().string();
    }
}
