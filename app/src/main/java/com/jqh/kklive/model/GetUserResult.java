package com.jqh.kklive.model;

import com.google.gson.annotations.Expose;

/**
 * Created by jiangqianghua on 18/1/27.
 */

public class GetUserResult {

    public static final int OK_RESULT = 0 ;
    @Expose
    private int code ;

    @Expose
    private String msg ;

    private UserProfile data;

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

    public UserProfile getData() {
        return data;
    }

    public void setData(UserProfile data) {
        this.data = data;
    }
}
