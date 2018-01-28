package com.jqh.kklive.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jiangqianghua on 18/1/24.
 */

public class UserProfile {


    @Expose
    @SerializedName("userAccount")
    private String account ;

    @Expose
    @SerializedName("userNick")
    private String nickName ="";

    @Expose
    @SerializedName("userLocation")
    private String location ="" ;

    @Expose
    @SerializedName("userRenzheng")
    private String renzheng = "";

    @Expose
    @SerializedName("userHeader")
    private String header = "";

    @Expose
    @SerializedName("userGender")
    private String gender ="未知";

    @Expose
    @SerializedName("userGetnum")
    private int getNum = 0;

    @Expose
    @SerializedName("userSendnum")
    private int sendNum = 0;

    @Expose
    @SerializedName("userSign")
    private String sign ="";

    @Expose
    @SerializedName("userLevel")
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRenzheng() {
        return renzheng;
    }

    public void setRenzheng(String renzheng) {
        this.renzheng = renzheng;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getGetNum() {
        return getNum;
    }

    public void setGetNum(int getNum) {
        this.getNum = getNum;
    }

    public int getSendNum() {
        return sendNum;
    }

    public void setSendNum(int sendNum) {
        this.sendNum = sendNum;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }


}
