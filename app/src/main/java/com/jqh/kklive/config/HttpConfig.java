package com.jqh.kklive.config;

/**
 * Created by jiangqianghua on 18/1/24.
 */

public class HttpConfig {

    public static final String webDomain = "http://192.168.1.102:8086/im";

    public static final String login = "/user/login";

    public static final String autoLogin = "/user/autologin";

    public static final String register = "/user/register";

    public static final String updateUser = "/user/update";

    public static final String getUserUrl = "/user/selfprofile";

    public static final String uploadImage = "/upload/header";

    public static final int REQUEST_GET = 0 ;
    public static final int REQUEST_POST = 1;



    public static String getLiveLoginUrl(){
        return webDomain + login;
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
        return webDomain+uploadImage ;
    }
}
