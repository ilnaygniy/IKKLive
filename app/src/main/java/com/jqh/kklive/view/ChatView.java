package com.jqh.kklive.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jqh.kklive.R;

/**
 * Created by jiangqianghua on 18/2/1.
 */

public class ChatView extends LinearLayout {
    private CheckBox checkMode ;
    public EditText chatContent ;
    private TextView sendChatBtn ;
    public ChatView(Context context) {
        super(context);
        init();
    }

    public ChatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.view_chat,this,true);
        bindView();
        initEvent();
    }

    private void bindView(){
        checkMode = (CheckBox) findViewById(R.id.switch_chat_type);
        chatContent = (EditText) findViewById(R.id.chat_content_edit);
        sendChatBtn = (TextView)findViewById(R.id.chat_send);
    }

    private void initEvent(){
        checkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    chatContent.setHint("发送弹幕聊天消息");
                }else{
                    chatContent.setHint("和大家聊天什么吧")  ;
                }
            }
        });


        sendChatBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 发送聊天消息
                if(mOnSendClickListener != null)
                    mOnSendClickListener.onSendClick(chatContent.getText().toString());
                chatContent.setText("");
            }
        });
    }

    private OnSendClickListener mOnSendClickListener ;
    public void setOnSendClickListener(OnSendClickListener l){
        mOnSendClickListener = l ;
    }
    public interface OnSendClickListener{
        void onSendClick(String content);
    }

}


