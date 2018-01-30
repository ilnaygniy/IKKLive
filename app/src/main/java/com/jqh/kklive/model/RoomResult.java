package com.jqh.kklive.model;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by jiangqianghua on 18/1/29.
 */

public class RoomResult {

    public static final int OK_RESULT = 0 ;
    @Expose
    private int code ;

    @Expose
    private String msg ;

    private RoomInfo data;

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

    public RoomInfo getData() {
        return data;
    }

    public void setData(RoomInfo data) {
        this.data = data;
    }
}
