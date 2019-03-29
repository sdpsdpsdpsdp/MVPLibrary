package com.laisontech.mvplibrary.presenter;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by SDP on 2018/2/5.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPresenter {
    Class<? extends Presenter> value();
}
