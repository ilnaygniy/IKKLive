package com.jqh.kklive.net;

import com.jqh.kklive.model.ErrorInfo;

/**
 * Created by jiangqianghua on 18/1/24.
 */

public interface IKKLiveCallBack {

    void onSuccess(Object obj);

    void onError(ErrorInfo errorInfo);
}
