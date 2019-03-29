package com.laisontech.mvplibrary.presenter;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;


import com.laisontech.mvplibrary.presenter.delivery.DeliverFirst;
import com.laisontech.mvplibrary.presenter.delivery.DeliverLatestCache;
import com.laisontech.mvplibrary.presenter.delivery.DeliverReplay;
import com.laisontech.mvplibrary.presenter.delivery.Delivery;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func4;
import rx.internal.util.SubscriptionList;
import rx.subjects.BehaviorSubject;

/**
 * Created by SDP on 2018/2/5.
 * 这是一个提供RxJava功能的演示程序的扩展。
 */

public class RxPresenter<View> extends Presenter<View> {

    private static final String REQUESTED_KEY = RxPresenter.class.getName() + "#requested";

    private final BehaviorSubject<View> views = BehaviorSubject.create();

    // 希望在destroy的时候能够终结的subscription list
    private SubscriptionList subscriptions = new SubscriptionList();

    // 可重复使用的函数
    private final HashMap<Integer, Func0<Subscription>> restartables = new HashMap<>();

    private final HashMap<Integer, Func4<?, ?, ?, ?, Subscription>> restartables4 =
            new HashMap<>();

    // 工作中的订阅者们
    private final HashMap<Integer, Subscription> workingSubscribers = new HashMap<>();


    public Observable<View> view() {
        return views;
    }

    /**
     * 注册订阅，在销毁期间自动取消订阅。
     */
    public void add(Subscription subscription) {
        subscriptions.add(subscription);
    }

    /**
     * 删除和取消订阅已注册为 {@link #add} previously.
     */
    public void remove(Subscription subscription) {
        subscriptions.remove(subscription);
    }

    /**
     * 这是一个可重复使用的,已绑定订阅者的可观察对象
     */
    public void restartable(int restartableId, Func0<Subscription> factory) {
        if (workingSubscribers.containsKey(restartableId))
            stop(restartableId);
        restartables.put(restartableId, factory);
    }

    public <T1, T2, T3, T4> void restartable(int restartableId, Func4<T1, T2, T3, T4, Subscription> factory) {
        if (workingSubscribers.containsKey(restartableId)) {
            stop(restartableId);
        }
        restartables4.put(restartableId, factory);
    }

    public void start(int restartableId) {
        stop(restartableId);
        Func0<Subscription> func = restartables.get(restartableId);
        if (func != null)
            workingSubscribers.put(restartableId, func.call());
    }


    public <T1, T2, T3, T4> void start(int restartableId, T1 arg1, T2 arg2, T3 arg3, T4 arg4) {
        stop(restartableId);
        Func4<T1, T2, T3, T4, Subscription> func4 =
                (Func4<T1, T2, T3, T4, Subscription>) restartables4.get(restartableId);
        if (func4 != null)
            workingSubscribers.put(restartableId, func4.call(arg1, arg2, arg3, arg4));
    }


    public void stop(int restartableId) {
        Subscription subscription = workingSubscribers.get(restartableId);
        if (subscription != null) {
            if (!subscription.isUnsubscribed())
                subscription.unsubscribe();
            workingSubscribers.remove(restartableId);
        }
    }

    /**
     * 这是一种快捷方式, 结合了
     * {@link #restartable(int, Func0)}
     * {@link #deliverFirst()},
     * {@link #split(Action2, Action2)}
     * 这三个方法
     */
    public <T> void restartableFirst(int restartableId, final Func0<Observable<T>> observableFactory,
                                     final Action2<View, T> onNext,
                                     @Nullable final Action2<View, Throwable> onError) {

        restartable(restartableId, new Func0<Subscription>() {
            @Override
            public Subscription call() {
                return observableFactory.call()
                        .compose(RxPresenter.this.<T>deliverFirst())
                        .subscribe(split(onNext, onError));
            }
        });
    }

    public <T, T1, T2, T3, T4> void restartableFirst(int restartableId,
                                                     final Func4<T1, T2, T3, T4, Observable<T>> observableFactory,
                                                     final Action2<View, T> onNext,
                                                     @Nullable final Action2<View, Throwable> onError) {

        restartable(restartableId, new Func4<T1, T2, T3, T4, Subscription>() {
            @Override
            public Subscription call(T1 arg1, T2 arg2, T3 arg3, T4 arg4) {
                return observableFactory.call(arg1, arg2, arg3, arg4)
                        .compose(RxPresenter.this.<T>deliverFirst())
                        .subscribe(split(onNext, onError));
            }
        });
    }

    public <T> void restartableFirst(int restartableId, final Func0<Observable<T>> observableFactory, final Action2<View, T> onNext) {
        restartableFirst(restartableId, observableFactory, onNext, null);
    }

    public <T> void restartableLatestCache(int restartableId, final Func0<Observable<T>> observableFactory,
                                           final Action2<View, T> onNext, @Nullable final Action2<View, Throwable> onError) {

        restartable(restartableId, new Func0<Subscription>() {
            @Override
            public Subscription call() {
                return observableFactory.call()
                        .compose(RxPresenter.this.<T>deliverLatestCache())
                        .subscribe(split(onNext, onError));
            }
        });
    }

    public <T> void restartableLatestCache(int restartableId, final Func0<Observable<T>> observableFactory, final Action2<View, T> onNext) {
        restartableLatestCache(restartableId, observableFactory, onNext, null);
    }

    /**
     * 这是一种快捷方式,结合了
     * {@link #restartable(int, Func0)},
     * {@link #deliverReplay()},
     * {@link #split(Action2, Action2)}.
     */
    public <T> void restartableReplay(int restartableId, final Func0<Observable<T>> observableFactory,
                                      final Action2<View, T> onNext, @Nullable final Action2<View, Throwable> onError) {

        restartable(restartableId, new Func0<Subscription>() {
            @Override
            public Subscription call() {
                return observableFactory.call()
                        .compose(RxPresenter.this.<T>deliverReplay())
                        .subscribe(split(onNext, onError));
            }
        });
    }

    /**
     * 这是调用@link restartablereplay的快捷方式(int，函数，Action2，Action2)
     * 最后一个参数=null。
     */
    public <T> void restartableReplay(int restartableId, final Func0<Observable<T>> observableFactory, final Action2<View, T> onNext) {
        restartableReplay(restartableId, observableFactory, onNext, null);
    }

    /**
     * 联合{@link BehaviorSubject<View>}和{@link Observable<T>}
     */
    public <T> DeliverLatestCache<View, T> deliverLatestCache() {
        return new DeliverLatestCache<>(views);
    }

    /**
     * 联合{@link BehaviorSubject<View>}和{@link Observable<T>}
     */
    public <T> DeliverFirst<View, T> deliverFirst() {
        return new DeliverFirst<>(views);
    }

    /**
     * 联合{@link BehaviorSubject<View>}和{@link Observable<T>}
     */
    public <T> DeliverReplay<View, T> deliverReplay() {
        return new DeliverReplay<>(views);
    }

    public <T> Action1<Delivery<View, T>> split(final Action2<View, T> onNext, @Nullable final Action2<View, Throwable> onError) {
        return new Action1<Delivery<View, T>>() {
            @Override
            public void call(Delivery<View, T> delivery) {
                delivery.split(onNext, onError);
            }
        };
    }

    /**
     * This is a shortcut for calling {@link #split(Action2, Action2)} when the second parameter is null.
     */
    public <T> Action1<Delivery<View, T>> split(Action2<View, T> onNext) {
        return split(onNext, null);
    }

    public <T> Observable<Delivery<View, T>> afterTakeViewDeliverLastestCache() {
        return Observable.create((new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                subscriber.onNext(null);
                subscriber.onCompleted();
            }
        })).compose(this.<T>deliverLatestCache());
    }

    /**
     * 仅在第一次TakeView的时候起作用
     *
     * @return
     */
    public Observable<View> afterTakeView() {
        return views.first();
    }

    @CallSuper
    @Override
    protected void onCreate(Bundle savedState) {

    }

    @Override
    public void restore() {
//        subscriptions = new SubscriptionList();
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        views.onCompleted();
        subscriptions.unsubscribe();
        for (Map.Entry<Integer, Subscription> entry : workingSubscribers.entrySet()) {
            if (entry.getValue() != null)
                entry.getValue().unsubscribe();
        }
        workingSubscribers.clear();
    }

    @CallSuper
    @Override
    protected void onSave(Bundle state) {
    }

    @CallSuper
    @Override
    protected void onBoundView(View view) {
        views.onNext(view);
    }

    @CallSuper
    @Override
    protected void onDropView() {
        views.onNext(null);
    }

    /**
     * 请使用restartableXX和deliverXX方法将数据从RxPresenter推入视图。
     */
    @Deprecated
    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }
}
