package com.jqh.kklive.net;

import com.jqh.kklive.AppManager;
import com.jqh.kklive.config.HttpConfig;
import com.jqh.kklive.model.ErrorInfo;
import com.jqh.kklive.model.RequestResult;
import com.jqh.kklive.model.RoomListResult;
import com.jqh.kklive.model.RoomResult;
import com.jqh.kklive.task.ErrorMessage;
import com.jqh.kklive.task.Poster;
import com.jqh.kklive.task.SuccessMessage;
import com.jqh.kklive.utils.OkHttpUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by jiangqianghua on 18/1/29.
 */

public class IKKLiveListManager {

    private static IKKLiveListManager instance ;

    private IKKLiveListManager(){

    }
    public static IKKLiveListManager getInstance(){
        if(instance == null){
            synchronized (IKKLiveListManager.class){
                if(instance == null)
                    instance = new IKKLiveListManager();
            }
        }
        return instance ;
    }

    /**
     * 创建直播
     * @param roomTitle
     * @param coverImg
     * @param listener
     */
    public void createLive(String userId , String roomTitle, String coverImg,final IKKLiveCallBack listener){

        Map<String,String> params = new HashMap<>();
        params.put("userId",userId);
        params.put("liveCover",coverImg);
        params.put("liveTitle",roomTitle);

        OkHttpUtils.excute(HttpConfig.getCreateRoomUrl(), HttpConfig.REQUEST_POST, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ErrorInfo errorInfo = new ErrorInfo();
                errorInfo.setErrCode(-104);
                errorInfo.setErrMsg(e.getMessage());
                errorInfo.setModule("createLive");
                ErrorMessage message = new ErrorMessage(errorInfo, listener);
                Poster.getInstance().post(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                RoomResult requestResult = AppManager.getGson().fromJson(response.body().string(),RoomResult.class);

                if(response.isSuccessful() && requestResult.getCode() == RequestResult.OK_RESULT){
                    SuccessMessage message = new SuccessMessage(requestResult.getData(),listener);
                    Poster.getInstance().post(message);
                }
                else
                {
                    ErrorInfo errorInfo = new ErrorInfo();
                    errorInfo.setErrCode(requestResult.getCode());
                    errorInfo.setErrMsg(requestResult.getMsg());
                    errorInfo.setModule("createLive");
                    ErrorMessage message = new ErrorMessage(errorInfo, listener);
                    Poster.getInstance().post(message);
                }
            }
        });
    }

    /**
     * 请求直播列表
     * @param page
     * @param size
     * @param listener
     */
    public void requestLiveList(int page,int size,final IKKLiveCallBack listener){
        Map<String,String> params = new HashMap<>();
        params.put("size",size+"");
        params.put("page",page+"");

        OkHttpUtils.excute(HttpConfig.getRoomListUrl(), HttpConfig.REQUEST_GET, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ErrorInfo errorInfo = new ErrorInfo();
                errorInfo.setErrCode(-104);
                errorInfo.setErrMsg(e.getMessage());
                errorInfo.setModule("requestLiveList");
                ErrorMessage message = new ErrorMessage(errorInfo, listener);
                Poster.getInstance().post(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                RoomListResult roomListResult = AppManager.getGson().fromJson(response.body().string(),RoomListResult.class);

                if(response.isSuccessful() && roomListResult.getCode() == RequestResult.OK_RESULT){
                    SuccessMessage message = new SuccessMessage(roomListResult.getData(),listener);
                    Poster.getInstance().post(message);
                }
                else
                {
                    ErrorInfo errorInfo = new ErrorInfo();
                    errorInfo.setErrCode(roomListResult.getCode());
                    errorInfo.setErrMsg(roomListResult.getMsg());
                    errorInfo.setModule("requestLiveList");
                    ErrorMessage message = new ErrorMessage(errorInfo, listener);
                    Poster.getInstance().post(message);
                }
            }
        });


    }
}
