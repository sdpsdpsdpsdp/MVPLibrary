package com.laisontech.mvplibrary.presenter;

/**
 * Created by SDP on 2018/2/5.
 * 任命者工厂
 */

public interface PresenterFactory<P extends Presenter> {
    P createPresenter();
}
