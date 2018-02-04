package com.jqh.kklive.im;

import com.jqh.kklive.AppManager;
import com.jqh.kklive.model.UserProfile;

import org.java_websocket.drafts.Draft_6455;

import java.net.URI;

/**
 * Created by jiangqianghua on 18/2/2.
 */

public class IKKIMManager {

    private IMClient mIMClient ;
    private IMMsgPacket mPacket ;
    private OnIKKLiveMsgListener mOnIKKLiveMsgListener ;
    private IMCallBack mIMCallBack = new IMCallBack() {
        @Override
        public void onMessage(String msg) {

            IMMsgCmd imMsgCmd = AppManager.getGson().fromJson(msg,IMMsgCmd.class);
            if(mOnIKKLiveMsgListener != null) {
                switch (imMsgCmd.getMsgType()){
                    case IMUtils.ILVLIVE_CMD_ENTER:
                        mOnIKKLiveMsgListener.onUserIn(imMsgCmd.getPacket());
                        break;
                    case IMUtils.ILVLIVE_CMD_LEAVE:
                        mOnIKKLiveMsgListener.onUserOut(imMsgCmd.getPacket());
                        break;
                    case IMUtils.CMD_CHAT_MSG_LIST:
                        mOnIKKLiveMsgListener.onNewMsgList(imMsgCmd.getPacket(),imMsgCmd.getContent());
                        break;
                    case IMUtils.CMD_CHAT_MSG_DANMU:
                        mOnIKKLiveMsgListener.onNewMsgDanMu(imMsgCmd.getPacket(),imMsgCmd.getContent());
                        break;
                    case IMUtils.CMD_CHAT_GIFT:
                        mOnIKKLiveMsgListener.onGiftMsg(imMsgCmd.getPacket(),imMsgCmd.getContent());
                        break;
                    case IMUtils.CMD_CHAT_HEART:
                        mOnIKKLiveMsgListener.onHeartMsg(imMsgCmd.getPacket(),imMsgCmd.getContent());
                        break;
                    case IMUtils.ILVLIVE_CMD_USER_LIST:
                        mOnIKKLiveMsgListener.onUserInList(imMsgCmd.getPacket());
                        break;
                }

            }
        }
        @Override
        public void onOpen() {


        }

        @Override
        public void onClose(int code, String reason, boolean remote) {

        }

        @Override
        public void onError(Exception ex) {
            if(mOnIKKLiveMsgListener != null)
                mOnIKKLiveMsgListener.onError(-1,ex.getMessage());
        }
    };


    private static IKKIMManager instance ;


    private IKKIMManager(){

    }
    public static IKKIMManager getInstance(){
        if(instance == null){
            synchronized (IKKIMManager.class){
                if(instance == null)
                    instance = new IKKIMManager();
            }
        }
        return instance ;
    }

    /**
     * 初始化聊天
     * @param roomId
     * @param packet
     */
    public void initChat(String roomId, IMMsgPacket packet){
        mPacket = packet ;
        String encodeStr = IMUtils.encodePackget(packet);
        String wsUrl = IMUtils.WS_URL+"/"+IMUtils.WS_NAME+"/";
        wsUrl += roomId+"/"+packet.getAccount()+"/"+encodeStr;
        try {
            mIMClient = new IMClient(new URI(wsUrl), new Draft_6455());
        }catch (Exception e){
            e.printStackTrace();
        }
        mIMClient.setListener(mIMCallBack);
        mIMClient.connect();
    }

    /**
     * 销毁聊天
     */
    public void destryChat(){
        mIMClient.close();
        mPacket = null ;
        mOnIKKLiveMsgListener = null ;
    }

    /**
     * 发送聊天
     * @param content
     */
    public void sendChatMsgForList(String content){
        IMMsgCmd cmd = new IMMsgCmd();
        cmd.setContent(content);
        cmd.setMsgType(IMUtils.CMD_CHAT_MSG_LIST);
        sendChatMsg(cmd);
    }

    /**
     * 发送礼物
     * @param content
     */
    public void sendChatMsgForGift(String content){
        IMMsgCmd cmd = new IMMsgCmd();
        cmd.setContent(content);
        cmd.setMsgType(IMUtils.CMD_CHAT_GIFT);
        sendChatMsg(cmd);
    }

    /**
     * 发送心心
     * @param content
     */
    public void sendChatMsgForHeart(String content){
        IMMsgCmd cmd = new IMMsgCmd();
        cmd.setContent(content);
        cmd.setMsgType(IMUtils.CMD_CHAT_HEART);
        sendChatMsg(cmd);

    }

    /**
     * 发送聊天
     * @param content
     */
    public void sendChatMsgForDanMu(String content){
        IMMsgCmd cmd = new IMMsgCmd();
        cmd.setContent(content);
        cmd.setMsgType(IMUtils.CMD_CHAT_MSG_DANMU);
        sendChatMsg(cmd);
    }


//    /**
//     * 发送聊天
//     * @param
//     */
//    public void sendChatMsgForEnter(){
//        IMMsgPacket packet = newPack();
//        packet.setMsgType(IMUtils.ILVLIVE_CMD_ENTER);
//        sendChatMsg(packet,"");
//    }


    public void sendChatMsg(IMMsgCmd cmd){
        IMMsgPacket packet = newPack();
        cmd.setPacket(packet);
        String json = IMUtils.Obj2Json(cmd);
        mIMClient.send(json);
    }

    public void setOnIKKLiveMsgListener(OnIKKLiveMsgListener ikkLiveMsgListener){
        this.mOnIKKLiveMsgListener = ikkLiveMsgListener ;
    }

    public interface OnIKKLiveMsgListener {

        void onUserIn(IMMsgPacket packet);

        void onUserOut(IMMsgPacket packet);

        void onUserInList(IMMsgPacket packet);

        void onNewMsgList(IMMsgPacket packet,String content);

        void onNewMsgDanMu(IMMsgPacket packet,String content);

        void onGiftMsg(IMMsgPacket packet,String content);

        void onHeartMsg(IMMsgPacket packet,String content);

        void onError(int code , String msg);

    }

    private IMMsgPacket newPack(){
        IMMsgPacket newPacket = new IMMsgPacket();
        newPacket.setAccount(mPacket.getAccount());
        newPacket.setLevel(mPacket.getLevel());
        newPacket.setHeader(mPacket.getHeader());
        newPacket.setNickName(mPacket.getNickName());
        return newPacket;
    }




}
