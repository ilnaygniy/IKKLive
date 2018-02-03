package com.jqh.kklive.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.jqh.kklive.R;

/**
 * Created by jiangqianghua on 18/2/1.
 */

public class BottomControllView extends RelativeLayout {

    public interface OnControlClickListener{
        void onCloseClick();
        void onChatClick();
        void onGiftClick();
    }

    private OnControlClickListener mOnControlClickListener;

    public void setOnControlClickListener(OnControlClickListener listener){
        mOnControlClickListener = listener ;
    }

    public BottomControllView(Context context) {
        super(context);
        init();
    }

    public BottomControllView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BottomControllView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.view_bottom_control,this,true);
        bindView();
    }
    private OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.close){
                if(mOnControlClickListener != null)
                    mOnControlClickListener.onCloseClick();
            }
            else if(v.getId() == R.id.chat){
                if(mOnControlClickListener != null)
                    mOnControlClickListener.onChatClick();
            }
            else if(v.getId() == R.id.option){
                if(mOnControlClickListener != null)
                    mOnControlClickListener.onGiftClick();
            }
        }
    };
    private void bindView(){

        findViewById(R.id.close).setOnClickListener(clickListener);
        findViewById(R.id.chat).setOnClickListener(clickListener);
        findViewById(R.id.option).setOnClickListener(clickListener);
    }
}
