package com.laisontech.mvp.laisonjsonparse;

/**
 * Created by admin on 2017/6/12.
 */
public class JsonModel {
    //错误代码
    private int errorcode;
    //是否压缩
    private int gzipcompress;
    //数据对象
    private Object data;

    private String datetime;//日期
    private int startindex;//起始索引
    private int hasmore;//是否有后续

    public int getErrorcode() {
        return errorcode;
    }
    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }
    public int getGzipcompress() {
        return gzipcompress;
    }
    public void setGzipcompress(int gzipcompress) {
        this.gzipcompress = gzipcompress;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public int getStartindex() {
        return startindex;
    }

    public void setStartindex(int startindex) {
        this.startindex = startindex;
    }

    public int getHasmore() {
        return hasmore;
    }

    public void setHasmore(int hasmore) {
        this.hasmore = hasmore;
    }
}
