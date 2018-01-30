package com.jqh.kklive;

import android.content.Intent;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;

import com.jqh.kklive.fragment.EditProfileFragment;
import com.jqh.kklive.fragment.LiveListFragment;
import com.jqh.kklive.widget.CreateLiveActivity;
import com.jqh.kklive.widget.EditProfileActivity;
import com.jqh.kklive.widget.ProfileTextView;
import com.jqh.kklive.widget.base.BaseActivity;

public class MainActivity extends BaseActivity {


    private FrameLayout mContainer ;

    private FragmentTabHost mTabHost ;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mContainer = bindViewId(R.id.fragment_container);
        mTabHost = bindViewId(R.id.fragment_tabhost);
        setUpTab();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    private void setUpTab(){
        mTabHost.setup(this,getSupportFragmentManager(),R.id.fragment_container);
        TabHost.TabSpec liveListTabSpec = mTabHost.newTabSpec("livelist").setIndicator(getIndicatorView(R.drawable.tab_livelist));
        mTabHost.addTab(liveListTabSpec, LiveListFragment.class,null);


        TabHost.TabSpec createLiveTabSpec = mTabHost.newTabSpec("createlive").setIndicator(getIndicatorView(R.mipmap.tab_publish_live));
        mTabHost.addTab(createLiveTabSpec, null,null);

        TabHost.TabSpec profileTabSpec = mTabHost.newTabSpec("profile").setIndicator(getIndicatorView(R.drawable.tab_profile));
        mTabHost.addTab(profileTabSpec, EditProfileFragment.class,null);

        mTabHost.getTabWidget().getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 进入创建直播页面
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,CreateLiveActivity.class);
                startActivity(intent);
            }
        });
    }

    private View getIndicatorView(int resId){

        View view = LayoutInflater.from(this).inflate(R.layout.view_indicator,null);
        ImageView image = (ImageView) view.findViewById(R.id.tab_icon);
        image.setImageResource(resId);
        return view ;
    }

}
