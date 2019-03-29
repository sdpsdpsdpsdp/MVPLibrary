package com.laisontech.mvp.laisonjsonparse;

/**
 * Created by admin on 2017/6/12.
 */
public class PostJson {
    private String operatetype;
    private Object data;

    public PostJson(String operatetype, Object obj) {
        this.operatetype = operatetype;
        this.data = obj;
    }

    public String getOperatetype() {
        return operatetype;
    }

    public void setOperatetype(String operatetype) {
        this.operatetype = operatetype;
    }

    public Object getDataBean() {
        return data;
    }

    public void setDataBean(Object object) {
        this.data = object;
    }

}
