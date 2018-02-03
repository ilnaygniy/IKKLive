package com.jqh.kklive.im;

import com.jqh.kklive.AppManager;
import com.jqh.kklive.model.UserProfile;

/**
 * Created by jiangqianghua on 18/2/1.
 */

public class IMUtils {

    public static final int ILVLIVE_CMD_ENTER = 0 ;
    public static final int ILVLIVE_CMD_LEAVE = 1 ;
    public static final int CMD_CHAT_GIFT = 2 ;
    public static final int CMD_CHAT_MSG_DANMU = 3 ;
    public static final int CMD_CHAT_MSG_LIST = 4 ;
    public static final int CMD_CHAT_HEART = 5;
    public static String WS_URL = "http://192.168.1.103:8086/live";

    public static String WS_NAME= "imService";

        public static String Obj2Json(Object obj){
        String jsonStr = AppManager.getGson().toJson(obj);
        return jsonStr ;
    }

}
