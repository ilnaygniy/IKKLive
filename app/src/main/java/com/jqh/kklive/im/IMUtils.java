package com.jqh.kklive.im;

import com.jqh.kklive.AppManager;
import com.jqh.kklive.model.UserProfile;

import org.java_websocket.util.Base64;

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
    public static final int ILVLIVE_CMD_USER_LIST = 6;
    public static String WS_URL = "http://192.168.1.100:8086/live";

    public static String WS_NAME= "imService";

        public static String Obj2Json(Object obj){
        String jsonStr = AppManager.getGson().toJson(obj);
        return jsonStr ;
    }

    /**
     * 转base64
     * @param json
     * @return
     */
    public static String toBase64FromJson(String json){
        String base64 =  new String(Base64.encodeBytes(json.getBytes()));
        return base64 ;
    }

    public static Object toObjFromJson(String json,Class clazz){
        return AppManager.getGson().fromJson(json,clazz);
    }

    public static String toJsonFromBase64(String base64){
        String json = new String(Base64.encodeBytes(base64.getBytes()));
        return json;
    }

    /**
     * 编码一报数据
     * @param obj
     * @return
     */
    public static String encodePackget(Object obj){
        String json = Obj2Json(obj);
        String base64Str = toBase64FromJson(json);
        return base64Str ;
    }

    /**
     * 解码一报数据
     * @param encodeStr
     * @return
     */
    public static Object decodePackget(String encodeStr,Class clazz){
        String json = toJsonFromBase64(encodeStr);
        return toObjFromJson(json,clazz);
    }

}
