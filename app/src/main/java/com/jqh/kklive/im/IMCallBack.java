package com.jqh.kklive.im;

import org.java_websocket.handshake.ServerHandshake;

public interface IMCallBack {

    void onMessage(String msg);
    void onOpen();
    void onClose(int code, String reason, boolean remote);
    void onError(Exception ex);
}
