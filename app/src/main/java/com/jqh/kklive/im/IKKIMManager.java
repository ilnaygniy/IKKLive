package com.jqh.kklive.im;

import com.jqh.kklive.AppManager;
import com.jqh.kklive.model.UserProfile;
import com.jqh.kklive.task.Poster;
import com.jqh.kklive.task.SuccessMessage;

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

            IMMsgPacket packet = AppManager.getGson().fromJson(msg,IMMsgPacket.class);
            if(mOnIKKLiveMsgListener != null) {
                switch (packet.getMsgType()){
                    case IMUtils.ILVLIVE_CMD_ENTER:
                        mOnIKKLiveMsgListener.onUserIn(packet);
                        break;
                    case IMUtils.ILVLIVE_CMD_LEAVE:
                        mOnIKKLiveMsgListener.onUserOut(packet.getAccount());
                        break;
                    case IMUtils.CMD_CHAT_MSG_LIST:
                    case IMUtils.CMD_CHAT_MSG_DANMU:
                    case IMUtils.CMD_CHAT_GIFT:
                        mOnIKKLiveMsgListener.onNewMsg(packet);
                        break;
                }

            }
        }
        @Override
        public void onOpen() {
            // 发送改用户进入房间
            sendChatMsgForEnter();

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
        String wsUrl = IMUtils.WS_URL+"/"+IMUtils.WS_NAME+"/";
        wsUrl += roomId+"/"+packet.getAccount();
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
        IMMsgPacket packet = newPack();
        packet.setMsgType(IMUtils.CMD_CHAT_MSG_LIST);
        sendChatMsg(packet,content);
    }

    /**
     * 发送聊天
     * @param content
     */
    public void sendChatMsgForDanMu(String content){
        IMMsgPacket packet = newPack();
        packet.setMsgType(IMUtils.CMD_CHAT_MSG_DANMU);
        sendChatMsg(packet,content);
    }


    /**
     * 发送聊天
     * @param
     */
    public void sendChatMsgForEnter(){
        IMMsgPacket packet = newPack();
        packet.setMsgType(IMUtils.ILVLIVE_CMD_ENTER);
        sendChatMsg(packet,"");
    }

    /**
     * 发送聊天
     * @param
     */
    public void sendChatMsgForQuit(){
        IMMsgPacket packet = newPack();
        packet.setMsgType(IMUtils.ILVLIVE_CMD_LEAVE);
        sendChatMsg(packet,"");
    }

    public void sendChatMsg(IMMsgPacket packet,String content){
        packet.setContent(content);
        String json = IMUtils.Obj2Json(packet);
        mIMClient.send(json);
    }

    public void setOnIKKLiveMsgListener(OnIKKLiveMsgListener ikkLiveMsgListener){
        this.mOnIKKLiveMsgListener = ikkLiveMsgListener ;
    }

    public interface OnIKKLiveMsgListener {

        void onUserIn(IMMsgPacket packet);

        void onUserOut(String id);

        void onNewMsg(IMMsgPacket packet);

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