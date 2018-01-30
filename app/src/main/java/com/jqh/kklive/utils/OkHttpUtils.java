package com.jqh.kklive.utils;

import com.jqh.kklive.AppManager;
import com.jqh.kklive.config.HttpConfig;

import java.util.Map;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by jiangqianghua on 18/1/13.
 */

public class OkHttpUtils {

    private static final String REQUEST_TAG = "okhttp";
    public static Request buildRequest(String url){
        if(AppManager.isNetWorkAvailable()){
            Request request = new Request.Builder()
                    .tag(REQUEST_TAG)
                    .url(url)
                    .build();
            return request;
        }
        return null ;
    }

    public static Request buildRequest(String url, RequestBody body){
        if(AppManager.isNetWorkAvailable()){
            Request request = new Request.Builder()
                    .tag(REQUEST_TAG)
                    .url(url)
                    .post(body)
                    .build();
            return request;
        }
        return null ;
    }

    public static void excute(String url, Callback callback){
        Request request = buildRequest(url);
        excute(request,callback);
    }

    public static void excute(Request request , Callback callback) {
        AppManager.getHttpClient().newCall(request).enqueue(callback);
    }

    public static void excute(String url, int type, Map<String,String> params, Callback callback){
        Request request = null ;
        if(type == HttpConfig.REQUEST_POST) {
            request = buildRequest(url, buildRequestBody(params));
        }
        else {
            url = getURLByParams(url, params);
            request = buildRequest(url);
        }
        excute(request,callback);
    }

    private static RequestBody buildRequestBody(Map<String,String> params){
        FormBody.Builder builder = new FormBody.Builder();

        for(Map.Entry<String,String> entry:params.entrySet()){
            builder.add(entry.getKey(),entry.getValue());
        }

        RequestBody body = builder.build();
        return body ;
    }

    private static String getURLByParams(String url,Map<String,String> params){
        int i = 0 ;
        for(Map.Entry<String,String> entry:params.entrySet()){
            String param = entry.getKey()+"="+entry.getValue() ;
            if(i == 0){
                url += "?"+param;
            }else{
                url += "&"+param;
            }
            i++;
        }
        return url ;
    }
}
