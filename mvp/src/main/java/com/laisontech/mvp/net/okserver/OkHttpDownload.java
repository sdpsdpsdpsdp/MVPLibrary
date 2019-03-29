package com.laisontech.mvp.net.okserver;

import android.os.Environment;

import com.laisontech.mvp.net.okserver.download.DownloadTask;
import com.laisontech.mvp.net.okserver.download.DownloadThreadPool;
import com.laisontech.mvp.net.okserver.okhttp.db.DownloadManager;
import com.laisontech.mvp.net.okserver.okhttp.model.Progress;
import com.laisontech.mvp.net.okserver.okhttp.request.base.Request;
import com.laisontech.mvp.net.okserver.okhttp.utils.IOUtils;
import com.laisontech.mvp.net.okserver.okhttp.utils.OkLogger;
import com.laisontech.mvp.net.okserver.task.XExecutor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 描    述：全局的下载管理类
 */
public class OkHttpDownload {

    private String folder;                                      //下载的默认文件夹
    private DownloadThreadPool threadPool;                      //下载的线程池
    private ConcurrentHashMap<String, DownloadTask> taskMap;    //所有任务

    public static OkHttpDownload getInstance() {
        return OkDownloadHolder.instance;
    }

    private static class OkDownloadHolder {
        private static final OkHttpDownload instance = new OkHttpDownload();
    }

    private OkHttpDownload() {
        folder = Environment.getExternalStorageDirectory() + File.separator + "download" + File.separator;
        IOUtils.createFolder(folder);
        threadPool = new DownloadThreadPool();
        taskMap = new ConcurrentHashMap<>();

        //校验数据的有效性，防止下载过程中退出，第二次进入的时候，由于状态没有更新导致的状态错误
        List<Progress> taskList = DownloadManager.getInstance().getDownloading();
        for (Progress info : taskList) {
            if (info.status == Progress.WAITING || info.status == Progress.LOADING || info.status == Progress.PAUSE) {
                info.status = Progress.NONE;
            }
        }
        DownloadManager.getInstance().replace(taskList);
    }

    public static DownloadTask request(String tag, Request<File, ? extends Request> request) {
        Map<String, DownloadTask> taskMap = OkHttpDownload.getInstance().getTaskMap();
        DownloadTask task = taskMap.get(tag);
        if (task == null) {
            task = new DownloadTask(tag, request);
            taskMap.put(tag, task);
        }
        return task;
    }

    /**
     * 从数据库中恢复任务
     */
    public static DownloadTask restore(Progress progress) {
        Map<String, DownloadTask> taskMap = OkHttpDownload.getInstance().getTaskMap();
        DownloadTask task = taskMap.get(progress.tag);
        if (task == null) {
            task = new DownloadTask(progress);
            taskMap.put(progress.tag, task);
        }
        return task;
    }

    /**
     * 从数据库中恢复任务
     */
    public static List<DownloadTask> restore(List<Progress> progressList) {
        Map<String, DownloadTask> taskMap = OkHttpDownload.getInstance().getTaskMap();
        List<DownloadTask> tasks = new ArrayList<>();
        for (Progress progress : progressList) {
            DownloadTask task = taskMap.get(progress.tag);
            if (task == null) {
                task = new DownloadTask(progress);
                taskMap.put(progress.tag, task);
            }
            tasks.add(task);
        }
        return tasks;
    }

    /**
     * 开始所有任务
     */
    public void startAll() {
        for (Map.Entry<String, DownloadTask> entry : taskMap.entrySet()) {
            DownloadTask task = entry.getValue();
            if (task == null) {
                OkLogger.w("can't find task with tag = " + entry.getKey());
                continue;
            }
            task.start();
        }
    }

    /**
     * 暂停全部任务
     */
    public void pauseAll() {
        //先停止未开始的任务
        for (Map.Entry<String, DownloadTask> entry : taskMap.entrySet()) {
            DownloadTask task = entry.getValue();
            if (task == null) {
                OkLogger.w("can't find task with tag = " + entry.getKey());
                continue;
            }
            if (task.progress.status != Progress.LOADING) {
                task.pause();
            }
        }
        //再停止进行中的任务
        for (Map.Entry<String, DownloadTask> entry : taskMap.entrySet()) {
            DownloadTask task = entry.getValue();
            if (task == null) {
                OkLogger.w("can't find task with tag = " + entry.getKey());
                continue;
            }
            if (task.progress.status == Progress.LOADING) {
                task.pause();
            }
        }
    }

    /**
     * 删除所有任务
     */
    public void removeAll() {
        removeAll(false);
    }

    /**
     * 删除所有任务
     *
     * @param isDeleteFile 删除任务是否删除文件
     */
    public void removeAll(boolean isDeleteFile) {
        Map<String, DownloadTask> map = new HashMap<>(taskMap);
        //先删除未开始的任务
        for (Map.Entry<String, DownloadTask> entry : map.entrySet()) {
            DownloadTask task = entry.getValue();
            if (task == null) {
                OkLogger.w("can't find task with tag = " + entry.getKey());
                continue;
            }
            if (task.progress.status != Progress.LOADING) {
                task.remove(isDeleteFile);
            }
        }
        //再删除进行中的任务
        for (Map.Entry<String, DownloadTask> entry : map.entrySet()) {
            DownloadTask task = entry.getValue();
            if (task == null) {
                OkLogger.w("can't find task with tag = " + entry.getKey());
                continue;
            }
            if (task.progress.status == Progress.LOADING) {
                task.remove(isDeleteFile);
            }
        }
    }

    /**
     * 设置下载目录
     */
    public String getFolder() {
        return folder;
    }

    public OkHttpDownload setFolder(String folder) {
        this.folder = folder;
        return this;
    }

    public DownloadThreadPool getThreadPool() {
        return threadPool;
    }

    public Map<String, DownloadTask> getTaskMap() {
        return taskMap;
    }

    public DownloadTask getTask(String tag) {
        return taskMap.get(tag);
    }

    //任务中是否有当前tag的任务 tag使用的下载apk的url
    public boolean hasTask(String tag) {
        return taskMap != null && tag != null && !tag.isEmpty() && taskMap.containsKey(tag);
    }

    public DownloadTask removeTask(String tag) {
        return taskMap.remove(tag);
    }

    public void addOnAllTaskEndListener(XExecutor.OnAllTaskEndListener listener) {
        threadPool.getExecutor().addOnAllTaskEndListener(listener);
    }

    public void removeOnAllTaskEndListener(XExecutor.OnAllTaskEndListener listener) {
        threadPool.getExecutor().removeOnAllTaskEndListener(listener);
    }
}
