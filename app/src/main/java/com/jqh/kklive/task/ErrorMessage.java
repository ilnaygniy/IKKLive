package com.jqh.kklive.task;

import com.jqh.kklive.model.ErrorInfo;
import com.jqh.kklive.net.IKKLiveCallBack;

/**
 * Created by jiangqianghua on 18/1/24.
 */

public class ErrorMessage implements Runnable {

    private ErrorInfo errorInfo;
    private IKKLiveCallBack listener ;

    public ErrorMessage(ErrorInfo errorInfo, IKKLiveCallBack listener) {
        this.errorInfo = errorInfo;
        this.listener = listener;
    }

    @Override
    public void run() {
        if(listener != null){
            listener.onError(errorInfo);
        }
    }
}
