package com.laisontech.mvp.net.okserver.okhttp.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.laisontech.mvp.net.okserver.okhttp.model.Progress;

import java.util.List;

public class UploadManager extends BaseDao<Progress> {

    private UploadManager() {
        super(new DBHelper());
    }

    public static UploadManager getInstance() {
        return UploadManagerHolder.instance;
    }

    private static class UploadManagerHolder {
        private static final UploadManager instance = new UploadManager();
    }

    @Override
    public Progress parseCursorToBean(Cursor cursor) {
        return Progress.parseCursorToBean(cursor);
    }

    @Override
    public ContentValues getContentValues(Progress progress) {
        return Progress.buildContentValues(progress);
    }

    @Override
    public String getTableName() {
        return DBHelper.TABLE_UPLOAD;
    }

    @Override
    public void unInit() {
    }

    /** 获取上传任务 */
    public Progress get(String tag) {
        return queryOne(Progress.TAG + "=?", new String[]{tag});
    }

    /** 移除上传任务 */
    public void delete(String taskKey) {
        delete(Progress.TAG + "=?", new String[]{taskKey});
    }

    /** 更新上传任务 */
    public boolean update(Progress progress) {
        return update(progress, Progress.TAG + "=?", new String[]{progress.tag});
    }

    /** 更新上传任务 */
    public boolean update(ContentValues contentValues, String tag) {
        return update(contentValues, Progress.TAG + "=?", new String[]{tag});
    }

    /** 获取所有上传信息 */
    public List<Progress> getAll() {
        return query(null, null, null, null, null, Progress.DATE + " ASC", null);
    }

    /** 获取所有上传信息 */
    public List<Progress> getFinished() {
        return query(null, "status=?", new String[]{Progress.FINISH + ""}, null, null, Progress.DATE + " ASC", null);
    }

    /** 获取所有上传信息 */
    public List<Progress> getUploading() {
        return query(null, "status not in(?)", new String[]{Progress.FINISH + ""}, null, null, Progress.DATE + " ASC", null);
    }

    /** 清空上传任务 */
    public boolean clear() {
        return deleteAll();
    }
}
