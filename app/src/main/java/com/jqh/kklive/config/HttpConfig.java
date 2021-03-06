package com.jqh.kklive.config;

/**
 * Created by jiangqianghua on 18/1/24.
 */

public class HttpConfig {

    public static final String webDomain = "http://192.168.1.100:8086/live";

    public static final String login = "/user/login";

    public static final String sigout = "/user/sigout";

    public static final String autoLogin = "/user/autologin";

    public static final String register = "/user/register";

    public static final String updateUser = "/user/update";

    public static final String getUserUrl = "/user/selfprofile";

    public static final String uploadImage = "/upload/img";

    public static final String createRoomUrl = "/room/createroom";

    public static final String roomListUrl = "/room/list";

    public static final String sendGiftUtl = "/user/sendgift";


    public static final int REQUEST_GET = 0 ;
    public static final int REQUEST_POST = 1;



    public static String getLiveLoginUrl(){
        return webDomain + login;
    }

    public static String getSigoutUrl(){
        return webDomain+sigout;
    }

    public static String getLiveRegisterUrl(){
        return webDomain+register;
    }


    public static String getLiveAutoLoginUrl(){
        return webDomain+autoLogin;
    }

    public static String getUpdateUserUrl(){
        return webDomain+updateUser ;
    }
    public static String getUserUrl(){
        return webDomain+getUserUrl;
    }
    public static String getUploadImage(){
        return webDomain + uploadImage ;
    }

    public static String getCreateRoomUrl(){
        return webDomain + createRoomUrl;
    }

    public static String getRoomListUrl(){
        return webDomain+roomListUrl ;
    }

    public static String getSendGiftUtl(){
        return webDomain+sendGiftUtl;
    }
}
