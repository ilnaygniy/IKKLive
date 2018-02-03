package com.jqh.kklive.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by jiangqianghua on 18/2/1.
 */

public class SizeChangeRelativeLayout   extends RelativeLayout {
    public SizeChangeRelativeLayout(Context context) {
        super(context);
    }

    public SizeChangeRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SizeChangeRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(h > oldh){
            // hide input
            if(mOnKeyBoardStatusListener !=null)
                mOnKeyBoardStatusListener.onHide();
        }
        else{
            // show input
            if(mOnKeyBoardStatusListener !=null)
                mOnKeyBoardStatusListener.onShow();
        }
    }

    private OnKeyBoardStatusListener mOnKeyBoardStatusListener ;

    public void setOnKeyBoardStatusListener(OnKeyBoardStatusListener listener) {
        this.mOnKeyBoardStatusListener = listener;
    }

    public interface OnKeyBoardStatusListener{
        void onShow();
        void onHide();
    }
}
