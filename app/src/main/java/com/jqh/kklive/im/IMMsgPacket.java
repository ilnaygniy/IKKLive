package com.jqh.kklive.im;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 存放用户信息
 * Created by jiangqianghua on 18/2/2.
 */

public class IMMsgPacket {

    @Expose
    @SerializedName("account")
    private String account ;

    @Expose
    @SerializedName("nickName")
    private String nickName ="";

    @Expose
    @SerializedName("header")
    private String header = "";

    @Expose
    @SerializedName("level")
    private int level = 0;



    public String getAccount() {
        return account;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }


    public void setAccount(String account) {
        this.account = account;
    }


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

}
