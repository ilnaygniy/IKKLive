package com.jqh.kklive.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by jiangqianghua on 18/1/24.
 */

public class ProfileTextView extends  ProfileEdit {

    public ProfileTextView(Context context) {
        super(context);
        disableEdit();
    }

    public ProfileTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        disableEdit();
    }

    public ProfileTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        disableEdit();
    }
}
