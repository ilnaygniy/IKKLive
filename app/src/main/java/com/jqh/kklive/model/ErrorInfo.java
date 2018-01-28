package com.jqh.kklive.model;

/**
 * Created by jiangqianghua on 18/1/24.
 */

public class ErrorInfo {

    private String module ;

    private int errCode ;

    private String errMsg ;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }
}
