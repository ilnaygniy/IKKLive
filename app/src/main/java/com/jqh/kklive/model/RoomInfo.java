package com.jqh.kklive.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jiangqianghua on 18/1/28.
 */

public class RoomInfo {


    @Expose
    private int roomId;

    @Expose
    private String userId;

    @Expose
    private String userName;

    @Expose
    @SerializedName("userAvater")
    private String userAvatar;

    @Expose
    private String liveCover;

    @Expose
    private String liveTitle;

    @Expose
    private int watcherNum;

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getLiveCover() {
        return liveCover;
    }

    public void setLiveCover(String liveCover) {
        this.liveCover = liveCover;
    }

    public String getLiveTitle() {
        return liveTitle;
    }

    public void setLiveTitle(String liveTitle) {
        this.liveTitle = liveTitle;
    }

    public int getWatcherNums() {
        return watcherNum;
    }

    public void setWatcherNums(int watcherNums) {
        this.watcherNum = watcherNums;
    }
}
