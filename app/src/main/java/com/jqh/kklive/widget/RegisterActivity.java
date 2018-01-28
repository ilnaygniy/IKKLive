package com.jqh.kklive.widget;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jqh.kklive.R;
import com.jqh.kklive.model.ErrorInfo;
import com.jqh.kklive.net.IKKLiveCallBack;
import com.jqh.kklive.net.IKKLiveLoginManager;
import com.jqh.kklive.widget.base.BaseActivity;

import org.w3c.dom.Text;

public class RegisterActivity extends BaseActivity{

    private EditText mAccountEdit;
    private EditText mPasswordEdit ;
    private EditText mConfirmPasswordEdit ;
    private Button mRegisterBtn ;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        mAccountEdit = bindViewId(R.id.account);
        mPasswordEdit = bindViewId(R.id.password);
        mConfirmPasswordEdit = bindViewId(R.id.confirm_password);
        mRegisterBtn = bindViewId(R.id.register);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }
    private void register(){
        String accountStr = mAccountEdit.getText().toString();
        String passwrodStr = mPasswordEdit.getText().toString();
        String confirmPasswordStr = mConfirmPasswordEdit.getText().toString();
        if(TextUtils.isEmpty(accountStr) ||
                TextUtils.isEmpty(passwrodStr)||
                TextUtils.isEmpty(confirmPasswordStr)){
            Toast("输入不能为空");
            return ;
        }
        if(!passwrodStr.equals(confirmPasswordStr)){
            Toast("密码两次输入不一致");
            return ;
        }

        IKKLiveLoginManager.getInstance().liveRegister(accountStr, passwrodStr, new IKKLiveCallBack() {
            @Override
            public void onSuccess(Object object) {
                Toast("创建用户成功");
                finish();
            }

            @Override
            public void onError(ErrorInfo errorInfo) {
                Toast("创建用户失败 "+errorInfo.getErrMsg());
            }
        });

    }


    private void Toast(String tip){
        Toast.makeText(RegisterActivity.this,tip,Toast.LENGTH_SHORT).show();
    }
}
