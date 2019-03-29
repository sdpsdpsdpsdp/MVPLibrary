package com.laisontech.mvplibrary.presenter.delivery;

import rx.Notification;
import rx.functions.Action2;


public final class Delivery<View, T> {
    private final View view;
    private final Notification<T> notification;

    public Delivery(View view, Notification<T> notification) {
        this.view = view;
        this.notification = notification;
    }

    //分离
    public void split(Action2<View, T> onNext, Action2<View, Throwable> onError) {
        if (notification.getKind() == Notification.Kind.OnNext) {
            onNext.call(view, notification.getValue());
        } else if (onError != null && notification.getKind() == Notification.Kind.OnError) {
            onError.call(view, notification.getThrowable());
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Delivery<?, ?> delivery = (Delivery<?, ?>) obj;
        return (view != null ? view.equals(delivery.view) : delivery.view == null) && !(notification != null ? !notification.equals(delivery.notification) : delivery.notification != null);
    }

    @Override
    public int hashCode() {
        int result = view != null ? view.hashCode() : 0;
        result = 31 * result + (notification != null ? notification.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "view=" + view +
                ", notification=" + notification +
                '}';
    }
}
