package com.jqh.kklive.widget;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.jqh.kklive.AppManager;
import com.jqh.kklive.R;
import com.jqh.kklive.im.IKKIMManager;
import com.jqh.kklive.im.IMMsgPacket;
import com.jqh.kklive.im.IMUtils;
import com.jqh.kklive.model.ChatMsgInfo;
import com.jqh.kklive.model.UserProfile;
import com.jqh.kklive.utils.KeybordS;
import com.jqh.kklive.view.BottomControllView;
import com.jqh.kklive.view.ChatMsgListView;
import com.jqh.kklive.view.ChatView;
import com.jqh.kklive.view.DanmuView;
import com.jqh.kklive.view.SizeChangeRelativeLayout;
import com.jqh.kklive.widget.base.BaseActivity;

public class HostLiveActivity extends BaseActivity {

    private BottomControllView mBottomControllView ;
    private ChatView mChatView ;
    private SizeChangeRelativeLayout mSizeChangeRelativeLayout ;
    private ChatMsgListView mChatMsgListView ;
    private String roomId;
    private String title ;
    private DanmuView mDanmuView ;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_host_live;
    }

    @Override
    protected void initView() {
        mBottomControllView = bindViewId(R.id.control_view);
        mChatView = bindViewId(R.id.chat_view);
        mSizeChangeRelativeLayout = bindViewId(R.id.activity_host_live);
        mChatMsgListView = bindViewId(R.id.chat_list);
        mDanmuView = bindViewId(R.id.danmu_view);
        setDefault();
    }

    @Override
    protected void initData() {

        roomId = this.getIntent().getStringExtra("roomId");
        title = this.getIntent().getStringExtra("title");

    }

    @Override
    protected void initEvent() {
        mBottomControllView.setOnControlClickListener(new BottomControllView.OnControlClickListener() {
            @Override
            public void onCloseClick() {
                finish();
            }

            @Override
            public void onChatClick() {
                mBottomControllView.setVisibility(View.INVISIBLE);
                mChatView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onGiftClick() {

            }
        });

        mSizeChangeRelativeLayout.setOnKeyBoardStatusListener(new SizeChangeRelativeLayout.OnKeyBoardStatusListener() {
            @Override
            public void onShow() {

            }

            @Override
            public void onHide() {
                mBottomControllView.setVisibility(View.VISIBLE);
                mChatView.setVisibility(View.INVISIBLE);
            }
        });

        IKKIMManager.getInstance().setOnIKKLiveMsgListener(new IKKIMManager.OnIKKLiveMsgListener() {
            @Override
            public void onUserIn(final IMMsgPacket packet) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ChatMsgInfo chatMsgInfo = ChatMsgInfo.createListInfo("进入房间", packet.getAccount(),packet.getHeader());
                        mChatMsgListView.addMsgInfo(chatMsgInfo);
                    }
                });
            }

            @Override
            public void onUserOut(String id) {

            }

            @Override
            public void onNewMsg( final IMMsgPacket packet) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ChatMsgInfo chatMsgInfo = ChatMsgInfo.createListInfo(packet.getContent(), packet.getAccount(),packet.getHeader());
                        if(packet.getMsgType() == IMUtils.CMD_CHAT_MSG_DANMU)
                        {
                            mDanmuView.addMsgInfo(chatMsgInfo);
                        }
                        else{

                            mChatMsgListView.addMsgInfo(chatMsgInfo);
                        }
                        mChatMsgListView.addMsgInfo(chatMsgInfo);
                    }
                });
            }

            @Override
            public void onGiftMsg(IMMsgPacket packet) {

            }

            @Override
            public void onHeartMsg(IMMsgPacket packet) {

            }

            @Override
            public void onError(int code, String msg) {

            }
        });

        mChatView.setOnSendClickListener(new ChatView.OnSendClickListener() {
            @Override
            public void onSendClick(String content) {
                if(TextUtils.isEmpty(content))
                {
                    Toast("发送不能为空");
                    return ;
                }
                // 发送消息
                IKKIMManager.getInstance().sendChatMsgForList(content);
                // close input
                mBottomControllView.setVisibility(View.VISIBLE);
                mChatView.setVisibility(View.INVISIBLE);

                KeybordS.closeKeybord(mChatView.chatContent,HostLiveActivity.this);

            }
        });

        initIMChat();

    }

    private void initIMChat(){
        UserProfile userProfile = AppManager.getUserProfile();
        IMMsgPacket packet = new IMMsgPacket();
        packet.setAccount(userProfile.getAccount());
        packet.setHeader(userProfile.getHeader());
        packet.setNickName(userProfile.getNickName());
        packet.setLevel(userProfile.getLevel());
        IKKIMManager.getInstance().initChat(roomId,packet);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        quitRoom();
    }

    private void quitRoom(){
        IKKIMManager.getInstance().destryChat();
    }

    private void setDefault(){
        mBottomControllView.setVisibility(View.VISIBLE);
        mChatView.setVisibility(View.INVISIBLE);
    }

    private void Toast(String tip){
        Toast.makeText(HostLiveActivity.this,tip,Toast.LENGTH_SHORT).show();
    }
}
