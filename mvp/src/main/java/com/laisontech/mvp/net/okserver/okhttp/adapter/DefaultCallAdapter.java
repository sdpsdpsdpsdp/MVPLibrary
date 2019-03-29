package com.laisontech.mvp.net.okserver.okhttp.adapter;

/**
 * 描    述：默认的工厂处理,不对返回值做任何操作
 */
public class DefaultCallAdapter<T> implements CallAdapter<T, Call<T>> {

    @Override
    public Call<T> adapt(Call<T> call, AdapterParam param) {
        return call;
    }
}
