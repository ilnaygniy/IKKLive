package com.jqh.kklive.net;

import com.jqh.kklive.AppManager;
import com.jqh.kklive.config.HttpConfig;
import com.jqh.kklive.model.ErrorInfo;
import com.jqh.kklive.model.RequestResult;
import com.jqh.kklive.task.ErrorMessage;
import com.jqh.kklive.task.Poster;
import com.jqh.kklive.task.SuccessMessage;
import com.jqh.kklive.utils.CrptoUtils;
import com.jqh.kklive.utils.OkHttpUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by jiangqianghua on 18/1/24.
 */

public class IKKLiveLoginManager {

    private static IKKLiveLoginManager instance ;

    private IKKLiveLoginManager(){

    }
    public static IKKLiveLoginManager getInstance(){
        if(instance == null){
            synchronized (IKKLiveLoginManager.class){
                if(instance == null)
                    instance = new IKKLiveLoginManager();
            }
        }
        return instance ;
    }

    /**
     * 登陆
     * @param account
     * @param password
     * @param isRunMain
     * @param listener
     */
    public void liveLogin(String account, String password, final boolean isRunMain, final IKKLiveCallBack listener){
         Map<String,String> params = new HashMap<>();
//        params.put("account",account);
//        params.put("password",password);
        try {
            params.put("logininfo", CrptoUtils.getEncrpty(account, password));
        }catch (Exception e){
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.setErrCode(-1);
            errorInfo.setErrMsg(e.getMessage());
            errorInfo.setModule("liveLogin");
            listener.onError(errorInfo);
            return ;
        }

        OkHttpUtils.excute(HttpConfig.getLiveLoginUrl(), HttpConfig.REQUEST_POST, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ErrorInfo errorInfo = new ErrorInfo();
                errorInfo.setErrCode(-1);
                errorInfo.setErrMsg(e.getMessage());
                errorInfo.setModule("liveLogin");
                if(isRunMain) {
                    ErrorMessage message = new ErrorMessage(errorInfo, listener);
                    Poster.getInstance().post(message);
                }else{
                    if(listener != null)
                        listener.onError(errorInfo);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //response.
                String result = response.body().string();
                RequestResult loginResult = AppManager.getGson().fromJson(result,RequestResult.class);
                if(response.isSuccessful() && loginResult.getCode() == 0){
                    if(isRunMain){
                        SuccessMessage message = new SuccessMessage(loginResult.getData(),listener);
                        Poster.getInstance().post(message);
                    }
                    else
                    {
                        listener.onSuccess(loginResult.getData());
                    }
                } else {
                    ErrorInfo errorInfo = new ErrorInfo();
                    errorInfo.setErrCode(loginResult.getCode());
                    errorInfo.setErrMsg(loginResult.getMsg());
                    errorInfo.setModule("liveLogin");
                    if(isRunMain) {
                        ErrorMessage message = new ErrorMessage(errorInfo, listener);
                        Poster.getInstance().post(message);
                    }else{
                        if(listener != null)
                            listener.onError(errorInfo);
                    }
                }
            }
        });
    }
    public void liveLogin(String account,String password,IKKLiveCallBack listener){
        liveLogin(account,password,true,listener);
    }

    public void liveAutoLogin(String token,IKKLiveCallBack listener){
        liveAutoLogin(token,true,listener);
    }

    /**
     * 自动登陆
     * @param token
     * @param isRunMain
     * @param listener
     */
    public void liveAutoLogin(String token,final  boolean isRunMain,final  IKKLiveCallBack listener) {
        Map<String,String> params = new HashMap<>();
        try {
            params.put("token", token);
        }catch (Exception e){
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.setErrCode(-1);
            errorInfo.setErrMsg(e.getMessage());
            errorInfo.setModule("liveLogin");
            listener.onError(errorInfo);
        }

        OkHttpUtils.excute(HttpConfig.getLiveAutoLoginUrl(), HttpConfig.REQUEST_POST, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ErrorInfo errorInfo = new ErrorInfo();
                errorInfo.setErrCode(-1);
                errorInfo.setErrMsg(e.getMessage());
                errorInfo.setModule("liveLogin");
                if(isRunMain) {
                    ErrorMessage message = new ErrorMessage(errorInfo, listener);
                    Poster.getInstance().post(message);
                }else{
                    if(listener != null)
                        listener.onError(errorInfo);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //response.
                String result = response.body().string();
                RequestResult loginResult = AppManager.getGson().fromJson(result,RequestResult.class);
                if(response.isSuccessful() && loginResult.getCode() == 0){
                    if(isRunMain){
                        SuccessMessage message = new SuccessMessage(loginResult.getData(),listener);
                        Poster.getInstance().post(message);
                    }
                    else
                    {
                        listener.onSuccess(loginResult);
                    }
                } else {
                    ErrorInfo errorInfo = new ErrorInfo();
                    errorInfo.setErrCode(loginResult.getCode());
                    errorInfo.setErrMsg(loginResult.getMsg());
                    errorInfo.setModule("liveLogin");
                    if(isRunMain) {
                        ErrorMessage message = new ErrorMessage(errorInfo, listener);
                        Poster.getInstance().post(message);
                    }else{
                        if(listener != null)
                            listener.onError(errorInfo);
                    }
                }
            }
        });
    }
    /**
     * 注册
     * @param accountStr
     * @param passwordStr
     * @param isRunMain
     * @param listener
     */
    public void liveRegister(String accountStr, String passwordStr,final boolean isRunMain,final IKKLiveCallBack listener){
        Map<String,String> params = new HashMap<>();
        params.put("account",accountStr);
        params.put("password",passwordStr);
        OkHttpUtils.excute(HttpConfig.getLiveRegisterUrl(), HttpConfig.REQUEST_POST, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ErrorInfo errorInfo = new ErrorInfo();
                errorInfo.setErrCode(001);
                errorInfo.setErrMsg(e.getMessage());
                errorInfo.setModule("liveLogin");
                if(isRunMain) {
                    ErrorMessage message = new ErrorMessage(errorInfo, listener);
                    Poster.getInstance().post(message);
                }else{
                    if(listener != null)
                        listener.onError(errorInfo);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                RequestResult loginResult = AppManager.getGson().fromJson(result,RequestResult.class);
                if(response.isSuccessful() && loginResult.getCode() == 0){
                    if(isRunMain){
                        SuccessMessage message = new SuccessMessage(loginResult,listener);
                        Poster.getInstance().post(message);
                    }
                    else
                    {
                        listener.onSuccess(loginResult);
                    }
                } else {
                    ErrorInfo errorInfo = new ErrorInfo();
                    errorInfo.setErrCode(loginResult.getCode());
                    errorInfo.setErrMsg(loginResult.getMsg());
                    errorInfo.setModule("liveRegister");
                    if(isRunMain) {
                        ErrorMessage message = new ErrorMessage(errorInfo, listener);
                        Poster.getInstance().post(message);
                    }else{
                        if(listener != null)
                            listener.onError(errorInfo);
                    }
                }
            }
        });
    }

    public void liveRegister(String accountStr, String passwordStr,IKKLiveCallBack listener){
        liveRegister(accountStr,passwordStr,true,listener);
    }

    /**
     * 登出操作
     * @param token
     * @param listener
     */
    public void sigout(String token,final IKKLiveCallBack listener){
        Map<String,String> params = new HashMap<>();
        params.put("token",token);

        OkHttpUtils.excute(HttpConfig.getSigoutUrl(), HttpConfig.REQUEST_POST, params,new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ErrorInfo errorInfo = new ErrorInfo();
                errorInfo.setErrCode(001);
                errorInfo.setErrMsg(e.getMessage());
                errorInfo.setModule("sigout");
                ErrorMessage message = new ErrorMessage(errorInfo, listener);
                Poster.getInstance().post(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                RequestResult loginResult = AppManager.getGson().fromJson(result,RequestResult.class);
                if(response.isSuccessful() && loginResult.getCode() == 0){
                    SuccessMessage message = new SuccessMessage(loginResult,listener);
                    Poster.getInstance().post(message);
                }
                else
                {
                    ErrorInfo errorInfo = new ErrorInfo();
                    errorInfo.setErrCode(loginResult.getCode());
                    errorInfo.setErrMsg(loginResult.getMsg());
                    errorInfo.setModule("sigout");
                    ErrorMessage message = new ErrorMessage(errorInfo, listener);
                    Poster.getInstance().post(message);
                }
            }
        });

    }


}
