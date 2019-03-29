package com.laisontech.mvp.net.okserver.okhttp.adapter;

/**
 * 描    述：返回值的适配器
 */
public interface CallAdapter<T, R> {

    /** call执行的代理方法 */
    R adapt(Call<T> call, AdapterParam param);
}
