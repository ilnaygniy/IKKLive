package com.jqh.kklive.model;

import com.google.gson.annotations.Expose;

/**
 * Created by jiangqianghua on 18/1/25.
 */

public class RequestResult {


    public static final int OK_RESULT = 0 ;
    @Expose
    private int code ;

    @Expose
    private String msg ;

    @Expose
    private Object data ;
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
