package com.laisontech.mvp.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.laisontech.mvp.base.BaseActivity;
import com.laisontech.mvp.base.BasePermissionsActivity;

import java.lang.reflect.ParameterizedType;


/**
 * 基类
 */

public abstract class MVPBasePermissionsActivity<V extends BaseView, T extends BasePresenterImpl<V>> extends BasePermissionsActivity implements BaseView {
    public T mPresenter;

    @Override
    protected void setOtherMethodBeforeLoadingContentView() {
        mPresenter = getInstance(this, 1);
        mPresenter.attachView((V) this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.detachView();
    }

    @Override
    public Context getContext() {
        return this;
    }

    public <T> T getInstance(Object o, int i) {
        try {
            return ((Class<T>) ((ParameterizedType) (o.getClass()
                    .getGenericSuperclass())).getActualTypeArguments()[i])
                    .newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }
}
