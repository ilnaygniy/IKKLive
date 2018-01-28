package com.jqh.kklive.widget;

import android.content.Context;
import android.media.Image;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jqh.kklive.R;

/**
 * Created by jiangqianghua on 18/1/24.
 */

public class ProfileEdit extends LinearLayout {

    private TextView mProfileKeyView;
    private TextView mProfileValueView;
    private ImageView mRightArrrow ;

    public ProfileEdit(Context context) {
        super(context);
        initView();
    }

    public ProfileEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ProfileEdit(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        LayoutInflater.from(getContext()).inflate(R.layout.view_profile_edit,this,true);
        findAlViews();
    }

    private void findAlViews(){
        mProfileKeyView =  (TextView) findViewById(R.id.profile_key) ;
        mProfileValueView = (TextView)findViewById(R.id.profile_value);
        mRightArrrow = (ImageView)findViewById(R.id.right_arrow);

    }

    public void setKey(int resId,String name){
        mProfileKeyView.setText(name);
        mProfileKeyView.setCompoundDrawablesWithIntrinsicBounds(resId,0,0,0);
    }

    public void setValue(int resId, String name){
        mProfileValueView.setText(name);
        mProfileValueView.setCompoundDrawablesWithIntrinsicBounds(resId,0,0,0);
    }

    public void setValue(String name){
        mProfileValueView.setText(name);
    }

    public String getVaue(){
        return mProfileValueView.getText().toString();
    }

    public void disableEdit(){
        mRightArrrow.setVisibility(GONE);
    }

}
