package com.jqh.kklive.widget;

import android.graphics.Color;
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
import com.jqh.kklive.model.ErrorInfo;
import com.jqh.kklive.model.GiftInfo;
import com.jqh.kklive.model.UserProfile;
import com.jqh.kklive.net.IKKFriendshipManager;
import com.jqh.kklive.net.IKKLiveCallBack;
import com.jqh.kklive.utils.ColorUtils;
import com.jqh.kklive.utils.KeybordS;
import com.jqh.kklive.view.BottomControllView;
import com.jqh.kklive.view.ChatMsgListView;
import com.jqh.kklive.view.ChatView;
import com.jqh.kklive.view.DanmuView;
import com.jqh.kklive.view.GiftFullView;
import com.jqh.kklive.view.GiftRepeatView;
import com.jqh.kklive.view.SizeChangeRelativeLayout;
import com.jqh.kklive.view.TitleView;
import com.jqh.kklive.widget.base.BaseActivity;

import tyrantgit.widget.HeartLayout;

public class WatcherActivity extends BaseActivity {

    private BottomControllView mBottomControllView ;
    private ChatView mChatView ;
    private SizeChangeRelativeLayout mSizeChangeRelativeLayout ;
    private ChatMsgListView mChatMsgListView ;
    private String roomId;
    private String title ;
    private String hostId ;
    private DanmuView mDanmuView ;
    private GiftSelectDialog mGiftSelectDialog ;
    private GiftRepeatView giftRepeatView ;
    private GiftFullView giftFullView ;
    private HeartLayout heartLayout ;
    private TitleView mTitleView ;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_watcher;
    }

    @Override
    protected void initView() {
        mBottomControllView = bindViewId(R.id.control_view);
        mChatView = bindViewId(R.id.chat_view);
        mSizeChangeRelativeLayout = bindViewId(R.id.activity_host_live);
        mChatMsgListView = bindViewId(R.id.chat_list);
        mDanmuView = bindViewId(R.id.danmu_view);
        giftRepeatView = bindViewId(R.id.gift_repeat_view);
        giftFullView = bindViewId(R.id.gift_full_view);
        heartLayout = bindViewId(R.id.heartLayout);
        mTitleView = bindViewId(R.id.title_view);
        setDefault();
        mBottomControllView.setWatcher();
    }

    @Override
    protected void initData() {

        roomId = this.getIntent().getStringExtra("roomId");
        title = this.getIntent().getStringExtra("title");
        hostId = this.getIntent().getStringExtra("hostId");

        // 获取主播信息
        IKKFriendshipManager.getInstance().selfprofile(hostId, new IKKLiveCallBack() {
            @Override
            public void onSuccess(Object obj) {
                UserProfile userProfile = (UserProfile)obj;
                mTitleView.setHost(userProfile);
            }

            @Override
            public void onError(ErrorInfo errorInfo) {
                Toast("获取主播信息失败:"+errorInfo.getErrMsg());
            }
        });

    }

    @Override
    protected void initEvent() {
        mBottomControllView.setOnControlClickListener(new BottomControllView.OnControlClickListener() {
            @Override
            public void onCloseClick() {
                // 退出教室
                finish();
            }

            @Override
            public void onChatClick() {
                mBottomControllView.setVisibility(View.INVISIBLE);
                mChatView.setVisibility(View.VISIBLE);
                KeybordS.openKeybord(mChatView.chatContent,WatcherActivity.this);
            }

            @Override
            public void onGiftClick() {
                if(mGiftSelectDialog == null) {
                    mGiftSelectDialog = new GiftSelectDialog(WatcherActivity.this);
                    mGiftSelectDialog.setGiftSendListener(giftSendListener);
                }
                mGiftSelectDialog.show();
            }

            @Override
            public void onOptionClick(View view) {

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

                        //用户进入直播
                        UserProfile userProfile = new UserProfile();
                        userProfile.setHeader(packet.getHeader());
                        userProfile.setNickName(packet.getNickName());
                        userProfile.setAccount(packet.getAccount());
                        userProfile.setLevel(packet.getLevel());

                        mTitleView.addWatcher(userProfile);
                        // mVipEnterView.showVipEnter(userProfile);
                    }
                });
            }

            @Override
            public void onUserOut(final String id) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UserProfile userProfile = new UserProfile();
                        userProfile.setAccount(id);
                        mTitleView.removeWatcher(userProfile);
                    }
                });
            }

            @Override
            public void onNewMsg(final IMMsgPacket packet) {
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

                    }
                });

            }

            @Override
            public void onGiftMsg(final  IMMsgPacket packet) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // format   giftid|repeatid
                        String content = packet.getContent();
                        String[] arr = content.split("&");

                        int giftId = Integer.parseInt(arr[0]);
                        String repeatId = arr[0];

                        GiftInfo giftInfo = GiftInfo.getGiftById(giftId);
                        if (giftInfo == null) {
                            return;
                        }
                        if (giftInfo.type == GiftInfo.Type.ContinueGift) {
                            giftRepeatView.showGift(giftInfo, repeatId, AppManager.getUserProfile());
                        } else if (giftInfo.type == GiftInfo.Type.FullScreenGift) {
                            //全屏礼物
                            giftFullView.showGift(giftInfo, AppManager.getUserProfile());
                        }
                    }
                });
            }

            @Override
            public void onHeartMsg(final IMMsgPacket packet) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String content = packet.getContent();
                        int color = Integer.parseInt(content);
                        heartLayout.addHeart(color);
                    }
                });

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
                if(mChatView.isDanMuModel())
                    IKKIMManager.getInstance().sendChatMsgForDanMu(content);
                else
                    IKKIMManager.getInstance().sendChatMsgForList(content);

                // close input
                mBottomControllView.setVisibility(View.VISIBLE);
                mChatView.setVisibility(View.INVISIBLE);

                KeybordS.closeKeybord(mChatView.chatContent,WatcherActivity.this);

            }
        });

        heartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send heart
                int color = ColorUtils.getRandColor();
                IKKIMManager.getInstance().sendChatMsgForHeart(color+"");
            }
        });

        initIMChat();

    }


    private GiftSelectDialog.OnGiftSendListener giftSendListener = new GiftSelectDialog.OnGiftSendListener() {

        @Override
        public void onGiftSendClick(final int giftId, final String repeatId) {

            // 先请求服务器
            IKKFriendshipManager.getInstance().sendGift(Integer.parseInt(roomId), AppManager.getUserProfile().getAccount(), giftId, 1, new IKKLiveCallBack() {
                @Override
                public void onSuccess(Object obj) {
                    // 请求成功，再发送数据给所有人
                    String content = giftId+"&"+repeatId;
                    IKKIMManager.getInstance().sendChatMsgForGift(content);
                }

                @Override
                public void onError(ErrorInfo errorInfo) {
                    Toast(errorInfo.getErrMsg());
                }
            });
        }
    };

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
        Toast.makeText(WatcherActivity.this,tip,Toast.LENGTH_SHORT).show();
    }
}
