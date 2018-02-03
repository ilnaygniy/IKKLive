package com.jqh.kklive.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.jqh.kklive.AppManager;
import com.jqh.kklive.MainActivity;
import com.jqh.kklive.R;
import com.jqh.kklive.dialog.EditGenderDialog;
import com.jqh.kklive.dialog.EditStrProfileDialog;
import com.jqh.kklive.model.ErrorInfo;
import com.jqh.kklive.model.UserProfile;
import com.jqh.kklive.net.IKKFriendshipManager;
import com.jqh.kklive.net.IKKLiveCallBack;
import com.jqh.kklive.net.IKKLiveLoginManager;
import com.jqh.kklive.utils.DiskLruCacheUtils;
import com.jqh.kklive.utils.ImgUtils;
import com.jqh.kklive.utils.PicChooserHelper;
import com.jqh.kklive.widget.EditProfileActivity;
import com.jqh.kklive.widget.LoginActivity;
import com.jqh.kklive.widget.ProfileEdit;
import com.jqh.kklive.widget.ProfileTextView;

/**
 * Created by jiangqianghua on 18/1/28.
 */

public class EditProfileFragment extends BaseFragment {

    private View mAvatarView;
    private ImageView mAvatarImg;
    private ProfileEdit mNickNameEdt;
    private ProfileEdit mGenderEdt;
    private ProfileEdit mSignEdt;
    private ProfileEdit mRenzhengEdt;
    private ProfileEdit mLocationEdt;

    private ProfileTextView mIdView;
    private ProfileTextView mLevelView;
    private ProfileTextView mGetNumsView;
    private ProfileTextView mSendNumsView;

    private Button mCompleteBtn;
    private Button logoutBtn ;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_profile;
    }

    @Override
    protected void initView() {
       // setSupportActionBar();
       // mToolBar.setTitle("编辑个人信息");
       // mToolBar.setTitleTextColor(Color.WHITE);
        mAvatarView = bindViewId(R.id.avatar);
        mAvatarImg = bindViewId(R.id.avatar_img);
        mNickNameEdt = bindViewId(R.id.nick_name);
        mGenderEdt = bindViewId(R.id.gender);
        mSignEdt = bindViewId(R.id.sign);
        mRenzhengEdt = bindViewId(R.id.renzheng);
        mLocationEdt = bindViewId(R.id.location);

        mIdView = bindViewId(R.id.id);
        mLevelView = bindViewId(R.id.level);
        mGetNumsView = bindViewId(R.id.get_nums);
        mSendNumsView = bindViewId(R.id.send_nums);

        mCompleteBtn = bindViewId(R.id.complete);
        logoutBtn = bindViewId(R.id.logout);
    }

    @Override
    protected void initData() {
        mNickNameEdt.setKey(R.mipmap.ic_info_nickname, "昵称");
        mGenderEdt.setKey(R.mipmap.ic_info_gender, "性别");
        mSignEdt.setKey(R.mipmap.ic_info_sign, "签名");
        mRenzhengEdt.setKey(R.mipmap.ic_info_renzhen, "认证");
        mLocationEdt.setKey(R.mipmap.ic_info_location, "地区");
        mIdView.setKey(R.mipmap.ic_info_id, "ID");
        mLevelView.setKey(R.mipmap.ic_info_level, "等级");
        mGetNumsView.setKey(R.mipmap.ic_info_get, "获得票数");
        mSendNumsView.setKey(R.mipmap.ic_info_send, "送出票数");

        getSelfUserProfile();
    }

    @Override
    protected void initEvent() {
        mAvatarView.setOnClickListener(clickListener);
        mNickNameEdt.setOnClickListener(clickListener);
        mGenderEdt.setOnClickListener(clickListener);
        mSignEdt.setOnClickListener(clickListener);
        mRenzhengEdt.setOnClickListener(clickListener);
        mLocationEdt.setOnClickListener(clickListener);
        mCompleteBtn.setOnClickListener(clickListener);
        logoutBtn.setOnClickListener(clickListener);
    }

    private void updateView(){

        UserProfile userProfile = AppManager.getUserProfile();

        String faceUrl = userProfile.getHeader();
        if (TextUtils.isEmpty(faceUrl)) {
            ImgUtils.loadRound(R.mipmap.default_avatar, mAvatarImg);
        } else {
            ImgUtils.loadRound(faceUrl, mAvatarImg);
        }

        if(userProfile == null)
            return ;
        mNickNameEdt.setValue(userProfile.getNickName());
        mGenderEdt.setValue(userProfile.getGender());
        mSignEdt.setValue(userProfile.getSign());
        mRenzhengEdt.setValue(userProfile.getRenzheng());
        mLocationEdt.setValue(userProfile.getLocation());
        mLevelView.setValue(userProfile.getLevel()+"");
        mGetNumsView.setValue(userProfile.getGetNum()+"");
        mSendNumsView.setValue(userProfile.getSendNum()+"");
        mIdView.setValue(userProfile.getAccount());
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.avatar) {
                //修改头像
                choosePic();
            } else if (id == R.id.nick_name) {
                //修改昵称
                showEditNickNameDialog();
            } else if (id == R.id.gender) {
                //修改性别
                showEditGenderDialog();
            } else if (id == R.id.sign) {
                //修改签名
                showEditSignDialog();
            } else if (id == R.id.renzheng) {
                //修改认证
                showEditRenzhengDialog();
            } else if (id == R.id.location) {
                //修改位置
                showEditLocationDialog();
            } else if (id == R.id.complete) {
                //完成，点击跳转到主界面
                Intent intent = new Intent();
                intent.setClass(getContext(), MainActivity.class);
                startActivity(intent);
            }else if(id == R.id.logout){
                // 退出账户
                String token = null;
                try {
                    token = DiskLruCacheUtils.getInstance().get("autotoken");
                }catch (Exception e){

                }
                if(token == null)
                    return ;
                IKKLiveLoginManager.getInstance().sigout(token, new IKKLiveCallBack() {
                    @Override
                    public void onSuccess(Object obj) {
                        //  退出
                        //  删除本地token
                        try{

                            DiskLruCacheUtils.getInstance().del("autotoken");

                            // 回到登录
                            Intent intent = new Intent();
                            intent.setClass(getContext(),LoginActivity.class);
                            startActivity(intent);

                            getActivity().finish();
                        }catch (Exception e){
                            Toast(e.getMessage());
                        }

                    }

                    @Override
                    public void onError(ErrorInfo errorInfo) {
                        Toast("退出失败:"+errorInfo.getErrMsg());
                    }
                });
            }
        }
    };

    /**
     * 显示昵称修改
     */
    private void showEditNickNameDialog(){
        EditStrProfileDialog dialog = new EditStrProfileDialog(getActivity());
        dialog.setOnOKListener(new EditStrProfileDialog.OnOKListener() {
            @Override
            public void onOk(String title, String content) {
                mNickNameEdt.setValue(content);
                AppManager.getUserProfile().setNickName(content);
                updateUserProfile();
            }
        });
        dialog.show("昵称",R.mipmap.ic_info_nickname,mNickNameEdt.getVaue());
    }

    /**
     * 显示昵称修签名
     */
    private void showEditSignDialog(){
        EditStrProfileDialog dialog = new EditStrProfileDialog(getActivity());
        dialog.setOnOKListener(new EditStrProfileDialog.OnOKListener() {
            @Override
            public void onOk(String title, String content) {
                mSignEdt.setValue(content);
                AppManager.getUserProfile().setSign(content);
                updateUserProfile();
            }
        });
        dialog.show("签名",R.mipmap.ic_info_nickname,mSignEdt.getVaue());
    }
    /**
     * 显示昵称修改框
     */
    private void showEditRenzhengDialog(){
        EditStrProfileDialog dialog = new EditStrProfileDialog(getActivity());
        dialog.setOnOKListener(new EditStrProfileDialog.OnOKListener() {
            @Override
            public void onOk(String title, String content) {
                mRenzhengEdt.setValue(content);
                AppManager.getUserProfile().setRenzheng(content);
                updateUserProfile();
            }
        });
        dialog.show("认证",R.mipmap.ic_info_nickname,mRenzhengEdt.getVaue());
    }

    /**
     * 显示昵称修改框
     */
    private void showEditLocationDialog(){
        EditStrProfileDialog dialog = new EditStrProfileDialog(getActivity());
        dialog.setOnOKListener(new EditStrProfileDialog.OnOKListener() {
            @Override
            public void onOk(String title, String content) {
                mLocationEdt.setValue(content);
                AppManager.getUserProfile().setLocation(content);
                updateUserProfile();
            }
        });
        dialog.show("位置",R.mipmap.ic_info_nickname,mLocationEdt.getVaue());
    }


    private void showEditGenderDialog(){
        EditGenderDialog dialog = new EditGenderDialog(getActivity());
        dialog.setOnChangeGenderListener(new EditGenderDialog.OnChangeGenderListener() {
            @Override
            public void onChangeGender(boolean isMale) {
                String gender = "男";
                if(!isMale)
                    gender = "女";
                AppManager.getUserProfile().setGender(gender);
                updateUserProfile();
            }
        });
        dialog.show();
    }

    private void updateUserProfile(){
        IKKFriendshipManager.getInstance().updateUserProfile(AppManager.getUserProfile(), new IKKLiveCallBack() {
            @Override
            public void onSuccess(Object obj) {
                // 更新成功，重新获取一遍用户信息
                getSelfUserProfile();
            }

            @Override
            public void onError(ErrorInfo errorInfo) {
                Toast("更新用户信息:"+errorInfo.getErrMsg());
            }
        });
    }

    private void getSelfUserProfile(){
        if(AppManager.getUserProfile() == null)
            return ;
        String account = AppManager.getUserProfile().getAccount();
        IKKFriendshipManager.getInstance().selfprofile(account, new IKKLiveCallBack() {
            @Override
            public void onSuccess(Object obj) {
                UserProfile userProfile = (UserProfile)obj;
                AppManager.setUserProfile(userProfile);
                updateView();
            }

            @Override
            public void onError(ErrorInfo errorInfo) {
                Toast("获取用户信息失败:"+errorInfo.getErrMsg());
            }
        });
    }

    private PicChooserHelper mPicChooserHelper ;
    private void choosePic(){
        if (mPicChooserHelper == null) {
            mPicChooserHelper = new PicChooserHelper(this, PicChooserHelper.PicType.Avatar);
            mPicChooserHelper.setOnChooseResultListener(new PicChooserHelper.OnChooseResultListener() {
                @Override
                public void onSuccess(String url) {
                    AppManager.getUserProfile().setHeader(url);
                    updateUserProfile();
                }

                @Override
                public void onFail(String msg) {
                    Toast("选择失败：:"+msg);
                }
            });
        }

        mPicChooserHelper.showPicChooserDialog();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(mPicChooserHelper != null){
            mPicChooserHelper.onActivityResult(requestCode,resultCode,data);
        }else{
            super.onActivityResult(requestCode,resultCode,data);;
        }
    }

    private void Toast(String tip){
        Toast.makeText(getActivity(),tip,Toast.LENGTH_SHORT).show();
    }
}
