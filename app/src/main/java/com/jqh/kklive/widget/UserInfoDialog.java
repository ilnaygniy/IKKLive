package com.jqh.kklive.widget;
import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.jqh.kklive.R;
import com.jqh.kklive.model.UserProfile;
import com.jqh.kklive.utils.ImgUtils;

import java.util.Map;

public class UserInfoDialog extends TransParentDialog {
    private UserProfile userInfo;

    private ImageView user_close;
    private ImageView user_avatar;
    private TextView user_name;
    private ImageView user_gender;
    private TextView user_level;
    private TextView user_id;
    private TextView user_renzhen;
    private TextView user_sign;
    private TextView user_songchu;
    private TextView user_bopiao;

    public UserInfoDialog(Activity activity, UserProfile userInfo) {
        super(activity);
        this.userInfo = userInfo;

        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_userinfo, null);
        setContentView(view);

        findAllViews(view);
        bindDataToViews();

        setWidthAndHeight(activity.getWindow().getDecorView().getWidth() * 80 / 100, WindowManager.LayoutParams.WRAP_CONTENT);
    }


    private void findAllViews(View view) {
        user_close = (ImageView) view.findViewById(R.id.user_close);
        user_avatar = (ImageView) view.findViewById(R.id.user_avatar);
        user_name = (TextView) view.findViewById(R.id.user_name);
        user_gender = (ImageView) view.findViewById(R.id.user_gender);
        user_level = (TextView) view.findViewById(R.id.user_level);
        user_id = (TextView) view.findViewById(R.id.user_id);
        user_renzhen = (TextView) view.findViewById(R.id.user_renzhen);
        user_sign = (TextView) view.findViewById(R.id.user_sign);
        user_songchu = (TextView) view.findViewById(R.id.user_songchu);
        user_bopiao = (TextView) view.findViewById(R.id.user_bopiao);

        user_close.setOnClickListener(clickListener);
        user_avatar.setOnClickListener(clickListener);
    }


    private void bindDataToViews() {

        String avatarUrl = userInfo.getHeader();
        if (TextUtils.isEmpty(avatarUrl)) {
            ImgUtils.loadRound(R.mipmap.default_avatar, user_avatar);
        } else {
            ImgUtils.loadRound(avatarUrl, user_avatar);
        }

        String nickName = userInfo.getNickName();
        if(TextUtils.isEmpty(nickName)){
            nickName = "用户";
        }
        user_name.setText(nickName);
        String genderValue = userInfo.getGender();
        if("男".equals(genderValue))
            user_gender.setImageResource( R.mipmap.ic_male);
        else
            user_gender.setImageResource(R.mipmap.ic_female);

        user_id.setText("ID：" + userInfo.getAccount());
        String sign = userInfo.getSign();
        user_sign.setText(TextUtils.isEmpty(sign) ? "Ta好像忘记写签名了..." : sign);

        user_renzhen.setText(userInfo.getRenzheng());
        user_songchu.setText("送出：" + userInfo.getSendNum());
        user_bopiao.setText("播票：" + userInfo.getGetNum());
        user_level.setText(userInfo.getLevel()+"");
    }

    private String getValue(Map<String, byte[]> customInfo, String key, String defaultValue) {
        if (customInfo != null) {
            byte[] valueBytes = customInfo.get(key);
            if (valueBytes != null) {
                return new String(valueBytes);
            }
        }
        return defaultValue;
    }

    private String formatLargNum(int num) {
        float wan = num * 1.0f / 10000;
        if (wan < 1) {
            return "" + num;
        } else {
            return new java.text.DecimalFormat("#.00").format(wan) + "万";
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == user_close) {
                hideDialog();
            }
        }
    };

    private void hideDialog() {
        hide();
    }

}
