package com.laisontech.mvp.net.okserver.okhttp.model;

/**
 * 描    述：优先级的枚举类
 */
public interface Priority {
    int UI_TOP = Integer.MAX_VALUE;
    int UI_NORMAL = 1000;
    int UI_LOW = 100;
    int DEFAULT = 0;
    int BG_TOP = -100;
    int BG_NORMAL = -1000;
    int BG_LOW = Integer.MIN_VALUE;
}
