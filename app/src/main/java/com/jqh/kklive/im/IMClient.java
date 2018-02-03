package com.jqh.kklive.im;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Map;

/**
 * 客户端
 */
public class IMClient extends WebSocketClient {

    private IMCallBack listener ;

    public IMClient(URI serverUri) {
        super(serverUri);
    }

    public IMClient(URI serverUri, Draft protocolDraft) {
        super(serverUri, protocolDraft);
    }

    public IMClient(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders, int connectTimeout) {
        super(serverUri, protocolDraft, httpHeaders, connectTimeout);
    }

    public void setListener(IMCallBack listener) {
        this.listener = listener;
    }

    /**
     * 打开连接
     * @param handshakedata The handshake of the websocket instance
     */
    public void onOpen(ServerHandshake handshakedata) {
        this.listener.onOpen();
    }

    /**
     * 接受到消息
     * @param message The UTF-8 decoded message that was received.
     */
    public void onMessage(String message) {
        this.listener.onMessage(message);
    }

    /**
     * 断开连接
     * @param code
     *            The codes can be looked up here: {@link }
     * @param reason
     *            Additional information string
     * @param remote
     */
    public void onClose(int code, String reason, boolean remote) {
        this.listener.onClose(code,reason,remote);
    }

    /**
     * 错误
     * @param ex The exception causing this error
     */
    public void onError(Exception ex) {
        this.listener.onError(ex);
    }
}
