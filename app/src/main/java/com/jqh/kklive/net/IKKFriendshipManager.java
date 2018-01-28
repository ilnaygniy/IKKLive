package com.jqh.kklive.net;

import com.jqh.kklive.AppManager;
import com.jqh.kklive.config.HttpConfig;
import com.jqh.kklive.model.ErrorInfo;
import com.jqh.kklive.model.GetUserResult;
import com.jqh.kklive.model.RequestResult;
import com.jqh.kklive.model.UserProfile;
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
 * Created by jiangqianghua on 18/1/27.
 */

public class IKKFriendshipManager {

    private static IKKFriendshipManager instance ;

    private IKKFriendshipManager(){

    }
    public static IKKFriendshipManager getInstance(){
        if(instance == null){
            synchronized (IKKFriendshipManager.class){
                if(instance == null)
                    instance = new IKKFriendshipManager();
            }
        }
        return instance ;
    }

    /**
     * 更新用户信息
     * @param userProfile
     * @param listener
     */
    public void updateUserProfile(UserProfile userProfile,final IKKLiveCallBack listener){

        Map<String,String> params = new HashMap<>();
        params.put("userAccount",userProfile.getAccount());
        params.put("userNick",userProfile.getNickName());
        params.put("userGender",userProfile.getGender()+"");
        params.put("userSign",userProfile.getSign());
        params.put("userRenzheng",userProfile.getRenzheng());
        params.put("userLocation",userProfile.getLocation());
        params.put("userHeader",userProfile.getHeader());
//        params.put("userLevel",userProfile.getLevel()+"");
//        params.put("userGetnum",userProfile.getGetNum()+"");
//        params.put("userSendnum",userProfile.getSendNum()+"");

        OkHttpUtils.excute(HttpConfig.getUpdateUserUrl(), HttpConfig.REQUEST_POST, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ErrorInfo errorInfo = new ErrorInfo();
                errorInfo.setErrCode(-104);
                errorInfo.setErrMsg(e.getMessage());
                errorInfo.setModule("updateUserProfile");
                ErrorMessage message = new ErrorMessage(errorInfo, listener);
                Poster.getInstance().post(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                RequestResult requestResult = AppManager.getGson().fromJson(response.body().string(),RequestResult.class);

                if(response.isSuccessful() && requestResult.getCode() == RequestResult.OK_RESULT){
                    SuccessMessage message = new SuccessMessage(requestResult,listener);
                    Poster.getInstance().post(message);
                }
                else
                {
                    ErrorInfo errorInfo = new ErrorInfo();
                    errorInfo.setErrCode(requestResult.getCode());
                    errorInfo.setErrMsg(requestResult.getMsg());
                    errorInfo.setModule("updateUserProfile");
                    ErrorMessage message = new ErrorMessage(errorInfo, listener);
                    Poster.getInstance().post(message);
                }
            }
        });
    }

    /**
     * 获取用户信息
     * @param account
     */
    public void selfprofile(String account,final IKKLiveCallBack listener){

        Map<String,String> params = new HashMap<>();
        params.put("account",account);
        OkHttpUtils.excute(HttpConfig.getUserUrl(), HttpConfig.REQUEST_POST,params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ErrorInfo errorInfo = new ErrorInfo();
                errorInfo.setErrCode(-104);
                errorInfo.setErrMsg(e.getMessage());
                errorInfo.setModule("updateUserProfile");
                ErrorMessage message = new ErrorMessage(errorInfo, listener);
                Poster.getInstance().post(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                GetUserResult getUserResult = AppManager.getGson().fromJson(response.body().string(),GetUserResult.class);
                if(getUserResult.getCode() == GetUserResult.OK_RESULT
                        && response.isSuccessful()){

                    SuccessMessage message = new SuccessMessage(getUserResult.getData(),listener);
                    Poster.getInstance().post(message);
                }
                else
                {
                    ErrorInfo errorInfo = new ErrorInfo();
                    errorInfo.setErrCode(getUserResult.getCode());
                    errorInfo.setErrMsg(getUserResult.getMsg());
                    errorInfo.setModule("getUserProfile");
                    ErrorMessage message = new ErrorMessage(errorInfo, listener);
                    Poster.getInstance().post(message);
                }
            }
        });

    }


}
