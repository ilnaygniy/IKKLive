package com.jqh.kklive.widget;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jqh.kklive.AppManager;
import com.jqh.kklive.R;
import com.jqh.kklive.model.ErrorInfo;
import com.jqh.kklive.model.RoomInfo;
import com.jqh.kklive.net.IKKLiveCallBack;
import com.jqh.kklive.net.IKKLiveListManager;
import com.jqh.kklive.utils.ImgUtils;
import com.jqh.kklive.utils.PicChooserHelper;
import com.jqh.kklive.widget.base.BaseActivity;

public class CreateLiveActivity extends BaseActivity {


    private View mSetCoverView;
    private ImageView mCoverImg;
    private TextView mCoverTipTxt;
    private EditText mTitleEt;
    private TextView mCreateRoomBtn;
    private TextView mRoomNoText;

    private String mCoverUrl = "" ;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_live;
    }

    @Override
    protected void initView() {
        mSetCoverView = bindViewId(R.id.set_cover);
        mCoverImg = bindViewId(R.id.cover);
        mCoverTipTxt = bindViewId(R.id.tv_pic_tip);
        mTitleEt = bindViewId(R.id.title);
        mCreateRoomBtn = bindViewId(R.id.create);
        mRoomNoText = bindViewId(R.id.room_no);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        mSetCoverView.setOnClickListener(clickListener);
        mCreateRoomBtn.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int id = v.getId() ;
            if (id == R.id.create) {
                //创建直播
                requestCreateRoom();
            }
            else if(id == R.id.set_cover){
                //选择图片
                choosePic();
            }
        }
    };

    /**
     * 创建直播
     */
    private void requestCreateRoom(){
        if(TextUtils.isEmpty(mTitleEt.getText().toString()))
        {
            Toast("房间标题不能为空");
            return ;
        }
        String roomText = mTitleEt.getText().toString() ;
        String userId = AppManager.getUserProfile().getAccount();
        if(TextUtils.isEmpty(userId)){
            Toast("账户信息为空，不能创建房间，请退出重新登录后再尝试");
            return ;
        }
        IKKLiveListManager.getInstance().createLive(userId, roomText, mCoverUrl, new IKKLiveCallBack() {
            @Override
            public void onSuccess(Object obj) {
                RoomInfo roomInfo = (RoomInfo)obj;
                //
            }

            @Override
            public void onError(ErrorInfo errorInfo) {
                Toast("创建直播失败:"+errorInfo.getErrMsg());
            }
        });

    }


    private PicChooserHelper mPicChooserHelper ;
    private void choosePic(){
        if (mPicChooserHelper == null) {
            mPicChooserHelper = new PicChooserHelper(this, PicChooserHelper.PicType.Cover);
            mPicChooserHelper.setOnChooseResultListener(new PicChooserHelper.OnChooseResultListener() {
                @Override
                public void onSuccess(String url) {
                    ImgUtils.load(url,mCoverImg);
                    mCoverTipTxt.setVisibility(View.GONE);
                    mCoverUrl = url ;
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
        Toast.makeText(CreateLiveActivity.this,tip,Toast.LENGTH_SHORT).show();
    }
}
