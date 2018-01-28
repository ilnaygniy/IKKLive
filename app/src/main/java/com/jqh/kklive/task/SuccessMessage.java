package com.jqh.kklive.task;

import com.jqh.kklive.net.IKKLiveCallBack;

/**
 * Created by jiangqianghua on 18/1/6.
 */

public class SuccessMessage implements Runnable{

    private Object data ;
    private IKKLiveCallBack listener ;
    public SuccessMessage(Object data, IKKLiveCallBack listener){
        this.data = data;
        this.listener = listener ;
    }

    @Override
    public void run() {
        // 主线程执行
        if(listener != null)
            listener.onSuccess(data);
    }
}
