package com.laisontech.mvplibrary.presenter;

/**
 * Created by SDP on 2018/2/5.
 * 这个类代表一个@link PresenterFactory
 */

public class ReflectionPresenterFactory<P extends Presenter> implements PresenterFactory<P> {
    private Class<P> presenterClass;

    public ReflectionPresenterFactory(Class<P> presenterClass) {
        this.presenterClass = presenterClass;
    }

    public static <P extends Presenter> ReflectionPresenterFactory<P> fromViewClass(Class<?> viewClass) {
        RequiresPresenter annotation = viewClass.getAnnotation(RequiresPresenter.class);
        Class<P> presenterClass = annotation == null ? null : (Class<P>) annotation.value();
        return presenterClass == null ? null : new ReflectionPresenterFactory<P>(presenterClass);
    }

    @Override
    public P createPresenter() {
        try {
            return presenterClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
