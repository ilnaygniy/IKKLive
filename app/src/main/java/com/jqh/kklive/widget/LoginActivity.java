package com.jqh.kklive.widget;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jqh.kklive.AppManager;
import com.jqh.kklive.R;
import com.jqh.kklive.model.ErrorInfo;
import com.jqh.kklive.model.GetUserResult;
import com.jqh.kklive.model.UserProfile;
import com.jqh.kklive.net.IKKFriendshipManager;
import com.jqh.kklive.net.IKKLiveCallBack;
import com.jqh.kklive.net.IKKLiveLoginManager;
import com.jqh.kklive.utils.Base64Utils;
import com.jqh.kklive.utils.DiskLruCacheUtils;
import com.jqh.kklive.widget.base.BaseActivity;

public class LoginActivity extends BaseActivity {


    private EditText mAccountEdt;
    private EditText mPasswordEdt ;
    private Button mLoginBtn;
    private Button mReigsterBtn ;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        mAccountEdt = bindViewId(R.id.account);
        mPasswordEdt= bindViewId(R.id.password);
        mLoginBtn = bindViewId(R.id.login);
        mReigsterBtn = bindViewId(R.id.register);

    }

    @Override
    protected void initData() {
        autoLogin();
    }

    @Override
    protected void initEvent() {
        mLoginBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                final String accoutStr = mAccountEdt.getText().toString();
                String passwordStr = mPasswordEdt.getText().toString();
                if(TextUtils.isEmpty(accoutStr) || TextUtils.isEmpty(passwordStr)){
                    Toast("输入不能为空");
                    return ;
                }
                //登陆
                IKKLiveLoginManager.getInstance().liveLogin(mAccountEdt.getText().toString(), mPasswordEdt.getText().toString(), new IKKLiveCallBack() {
                    @Override
                    public void onSuccess(Object result) {
                        Toast("登陆成功");
                        String token = String.valueOf(result) ;
                        //保存本地token
                        try {
                            DiskLruCacheUtils.getInstance().set("autotoken", token);
                        }catch (Exception e){
                            Toast("登陆失败 "+ e.getMessage());
                            return ;
                        }

                        AppManager.getUserProfile().setAccount(accoutStr);
                        // 获取用户信息
                        GetUserProfile(accoutStr);
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this,EditProfileActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(ErrorInfo errorInfo) {
                        Toast("登陆失败 "+ errorInfo.getErrMsg());
                    }
                });
            }
        });

        mReigsterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注册
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 自动登陆
     */
    private void autoLogin(){

        String token = null;
        try {
            //byte[] bytes= Base64Utils.decode("123");
            token = DiskLruCacheUtils.getInstance().get("autotoken");
        }catch (Exception e){

        }
        if(token == null)
            return ;
        //登陆
        IKKLiveLoginManager.getInstance().liveAutoLogin(token, new IKKLiveCallBack() {
            @Override
            public void onSuccess(Object result) {
                Toast("自动登陆成功");
                String account = String.valueOf(result) ;
                AppManager.getUserProfile().setAccount(account);
                //保存本地token
                GetUserProfile(account);
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,EditProfileActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(ErrorInfo errorInfo) {
                Toast("自动登陆失败 "+ errorInfo.getErrMsg());
            }
        });
    }
    private void Toast(String tip){
        Toast.makeText(LoginActivity.this,tip,Toast.LENGTH_SHORT).show();
    }


    private void GetUserProfile(String account){
        IKKFriendshipManager.getInstance().selfprofile(account, new IKKLiveCallBack() {
            @Override
            public void onSuccess(Object obj) {
                UserProfile userProfile = (UserProfile)obj;
                AppManager.setUserProfile(userProfile);
            }

            @Override
            public void onError(ErrorInfo errorInfo) {
                Toast("获取用户信息失败:"+errorInfo.getErrMsg());
            }
        });

    }
}
