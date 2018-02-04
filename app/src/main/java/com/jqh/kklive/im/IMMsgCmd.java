package com.jqh.kklive.im;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jiangqianghua on 18/2/4.
 */

public class IMMsgCmd {

    @Expose
    @SerializedName("msgType")
    private int msgType ;

    @Expose
    @SerializedName("content")
    private String content;

    @Expose
    @SerializedName("packet")
    private IMMsgPacket packet ;

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public IMMsgPacket getPacket() {
        return packet;
    }

    public void setPacket(IMMsgPacket packet) {
        this.packet = packet;
    }
}
