package com.laisontech.mvp.net.okconnect.callback;

/**
 * Created by SDP
 * on 2019/3/29
 * Des：
 */
public class ResultWithTag {
    private String result;
    private Object tag;

    public ResultWithTag(String result, Object tag) {
        this.result = result;
        this.tag = tag;
    }

    public String getResult() {
        return result;
    }

    public Object getTag() {
        return tag;
    }
}
