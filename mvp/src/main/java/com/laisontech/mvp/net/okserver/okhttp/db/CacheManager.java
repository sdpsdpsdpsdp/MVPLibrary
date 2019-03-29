package com.laisontech.mvp.net.okserver.okhttp.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.laisontech.mvp.net.okserver.okhttp.cache.CacheEntity;

import java.util.List;

public class CacheManager extends BaseDao<CacheEntity<?>> {

    public static CacheManager getInstance() {
        return CacheManagerHolder.instance;
    }

    private static class CacheManagerHolder {
        private static final CacheManager instance = new CacheManager();
    }

    private CacheManager() {
        super(new DBHelper());
    }

    @Override
    public CacheEntity<?> parseCursorToBean(Cursor cursor) {
        return CacheEntity.parseCursorToBean(cursor);
    }

    @Override
    public ContentValues getContentValues(CacheEntity<?> cacheEntity) {
        return CacheEntity.getContentValues(cacheEntity);
    }

    @Override
    public String getTableName() {
        return DBHelper.TABLE_CACHE;
    }

    @Override
    public void unInit() {
    }

    /** 根据key获取缓存 */
    public CacheEntity<?> get(String key) {
        if (key == null) return null;
        List<CacheEntity<?>> cacheEntities = query(CacheEntity.KEY + "=?", new String[]{key});
        return cacheEntities.size() > 0 ? cacheEntities.get(0) : null;
    }

    /** 移除一个缓存 */
    public boolean remove(String key) {
        if (key == null) return false;
        return delete(CacheEntity.KEY + "=?", new String[]{key});
    }

    /** 返回带泛型的对象,注意必须确保泛型和对象对应才不会发生类型转换异常 */
    @SuppressWarnings("unchecked")
    public <T> CacheEntity<T> get(String key, Class<T> clazz) {
        return (CacheEntity<T>) get(key);
    }

    /** 获取所有缓存 */
    public List<CacheEntity<?>> getAll() {
        return queryAll();
    }

    /**
     * 更新缓存，没有就创建，有就替换
     *
     * @param key    缓存的key
     * @param entity 需要替换的的缓存
     * @return 被替换的缓存
     */
    public <T> CacheEntity<T> replace(String key, CacheEntity<T> entity) {
        entity.setKey(key);
        replace(entity);
        return entity;
    }

    /** 清空缓存 */
    public boolean clear() {
        return deleteAll();
    }
}
