package com.laisontech.mvplibrary.presenter;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by SDP on 2018/2/5.
 * presenter 协作者实体类操作
 */

public class Presenter<View> {
    private View view;
    private CopyOnWriteArrayList<OnDestroyListener> onDestroyListeners
            = new CopyOnWriteArrayList<>();

    /**
     * 当presenter创建的时候调用
     * 注:它跟Activity, Fragment的onCreate没有关系
     *
     * @param saveBundleState 如果在进程重新启动之后，程序被重新实例化，那么这个包就会被重新实例化
     *                        包含在@link onsave中提供的数据。
     */
    protected void onCreate(Bundle saveBundleState) {
    }

    /**
     * presenter被销毁的时候将会被调用
     */
    protected void onDestroy() {
    }

    /**
     * 保存状态,保存的状态将会在{@link #onCreate(Bundle)} 中作为参数被传入
     */
    protected void onSave(Bundle state) {
    }

    /**
     * 绑定到Activity或者Fragment组件
     * 在哪里绑定最好呢? 最好选择在{@link Activity#onResume()}里绑定, 这样的话所有UI
     * 处理在此之前就处理好了
     */
    protected void onBoundView(View view) {
    }

    /**
     * 接触绑定View 对应{@link Activity#onSaveInstanceState(Bundle)}
     */
    protected void onDropView() {

    }

    /**
     * 恢复 ，非正常创建
     */
    public void restore() {

    }

    /**
     * 添加一个监听 {@link #onDestroy}。
     */
    public void addOnDestroyListener(OnDestroyListener listener) {
        onDestroyListeners.add(listener);
    }

    /**
     * 移除一个监听 {@link #onDestroy}
     */
    public void removeOnDestroyListener(OnDestroyListener listener) {
        onDestroyListeners.remove(listener);
    }

    /**
     * 返回当前绑定的View
     * View一般在这些地方有效:
     * {@link Activity#onResume()} and {@link Activity#onPause()},
     * {@link Fragment#onResume()} and {@link Fragment#onPause()},
     */
    public View getView() {
        return view;
    }

    /**
     * 初始化这个presenter
     */
    public void create(Bundle bundle) {
        onCreate(bundle);
    }

    /**
     * 销毁这个  presenter, calling all {@link Presenter.OnDestroyListener} callbacks.
     */
    public void destroy() {
        for (OnDestroyListener listener : onDestroyListeners) {
            listener.onDestroy();
        }
        onDestroy();
    }

    /**
     * 保存这个presenter
     */
    public void save(Bundle state) {
        onSave(state);
    }

    /**
     * 绑定View
     */
    public void boundView(View view) {
        this.view = view;
        onBoundView(view);
    }

    /**
     * 解绑View
     */
    public void dropView() {
        onDropView();
        this.view = null;
    }


    /**
     * 当一个Presenter即将被销毁时，将调用一个回调
     */
    public interface OnDestroyListener {
        void onDestroy();
    }
}
