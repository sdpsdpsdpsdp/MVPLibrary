package com.laisontech.mvplibrary.presenter;

import java.io.File;
import java.util.HashMap;

/**
 * Created by SDP on 2018/2/5.
 * 当系统回收或者横竖屏切换的时候,如果并不想presenter销毁,那么将它保存在这个单例之中
 */

public enum PresenterStorage {
    INSTANCE;
    private HashMap<String, Presenter> idToPresenter = new HashMap<>();//将id做key
    private HashMap<Presenter, String> presenterToId = new HashMap<>();//将presenter做key

    /**
     * 添加一个presenter添加到储存中
     */
    public void addPresenterToStorage(final Presenter presenter) {
        String id = presenter.getClass().getSimpleName() + File.separator
                + System.nanoTime() + File.separator
                + (int) (Math.random() * Integer.MAX_VALUE);
        idToPresenter.put(id, presenter);
        presenterToId.put(presenter, id);
        //删除，避免内存泄漏
        presenter.addOnDestroyListener(new Presenter.OnDestroyListener() {
            @Override
            public void onDestroy() {
                idToPresenter.remove(presenterToId.remove(presenter));
            }
        });
    }

    /**
     * 得到Presenter实例
     */
    public <P> P getPresenter(String id) {
        return (P) idToPresenter.get(id);
    }

    /**
     * 获取id
     */
    public String getId(Presenter presenter) {
        return presenterToId.get(presenter);
    }

    /**
     * 移除所有的presenter
     */
    public void clear() {
        idToPresenter.clear();
        presenterToId.clear();
    }
}
