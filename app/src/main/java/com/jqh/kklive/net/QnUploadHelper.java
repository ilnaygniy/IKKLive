package com.jqh.kklive.net;

import com.jqh.kklive.AppManager;
import com.jqh.kklive.config.HttpConfig;
import com.jqh.kklive.model.ErrorInfo;
import com.jqh.kklive.model.RequestResult;
import com.jqh.kklive.task.ErrorMessage;
import com.jqh.kklive.task.Poster;
import com.jqh.kklive.task.SuccessMessage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by jiangqianghua on 18/1/28.
 */

public class QnUploadHelper {



    /**
     *
     * @param filepath
     * @param name
     * @param listener
     */
    public static void uploadPic(String filepath,String name,final IKKLiveCallBack listener){
        Map<String,String> map = new HashMap<>();
        map.put("name",name);
        String url = HttpConfig.getUploadImage();

        FormBody paramsBody=new FormBody.Builder()
                .add("name",name)
                .build();

        File file = new File(filepath);

//        MediaType type = MediaType.parse("application/octet-stream");//"text/xml;charset=utf-8"
//        File file = new File(filepath);
//        RequestBody fileBody = RequestBody.create(type,file);

//        RequestBody multipartBody = new MultipartBody.Builder()
//                .setType(MultipartBody.ALTERNATIVE)
//                .addPart(Headers.of(
//                "Content-Disposition",
//                "form-data; name=\"params\"")
//                ,paramsBody)
//                .addPart(Headers.of(
//                        "Content-Disposition",
//                        "form-data; name=\"file\"; filename=\"plans.xml\"")
//                        , fileBody)
//                .build();
//        final Request request=new Request.Builder().url(url)
//                .addHeader("User-Agent","android")
//                .header("Content-Type","text/html; charset=utf-8;")
//                .post(multipartBody)//传参数、文件或者混合，改一下就行请求体就行
//                .build();


        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
        String filename = file.getName();
        // 参数分别为， 请求key ，文件名称 ， RequestBody
        requestBody.addFormDataPart("file", filename, body);

        if (map != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : map.entrySet()) {
                requestBody.addFormDataPart(String.valueOf(entry.getKey()),String.valueOf(entry.getValue()));
            }
        }
        Request request = new Request.Builder().url(url).post(requestBody.build()).tag(AppManager.getContext()).build();

        AppManager.getHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ErrorInfo errorInfo = new ErrorInfo();
                errorInfo.setErrCode(-201);
                errorInfo.setErrMsg(e.getMessage());
                errorInfo.setModule("uploadPic");
                ErrorMessage message = new ErrorMessage(errorInfo, listener);
                Poster.getInstance().post(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                //Log.i("xxx","1、连接的消息"+response.message());
                String msg = response.body().string();
                RequestResult requestResult = AppManager.getGson().fromJson(msg,RequestResult.class);
                if(response.isSuccessful() && requestResult.getCode() == 0){
                    //Log.i("xxx","2、连接成功获取的内容"+response.body().string());
                    SuccessMessage message = new SuccessMessage(requestResult.getData(),listener);
                    Poster.getInstance().post(message);
                }
            }
        });

    }
}
