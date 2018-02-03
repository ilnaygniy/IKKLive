package com.jqh.ikkavlivemodule;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.jqh.ikkavlivemodule.utils.DataUtils;
import com.tencent.livesdk.ILVLiveManager;

/**
 * Created by jiangqianghua on 18/1/30.
 */

public class AVRootView extends SurfaceView implements SurfaceHolder.Callback,Camera.AutoFocusCallback,Camera.PictureCallback {

    private OrientationEventListener orientationEventListener;
    private int screenWidth;
    private int screenHeight;
    private String screenRate;
    private Context mContext ;


    public AVRootView(Context context) {
        super(context);
        initData(context);
        //ILVLiveManager.getInstance().setAvVideoView();

    }

    public AVRootView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context);
    }

    public AVRootView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context);
    }

    private void initData(Context context){
        this.mContext = context ;
        getHolder().addCallback(this);
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        //DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        System.out.println("width-display :" + metrics.widthPixels);
        System.out.println("heigth-display :" + metrics.heightPixels);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        screenRate = getSurfaceViewSize(screenWidth, screenHeight);
        setSurfaceViewSize(screenRate);

        orientationEventListener = new OrientationEventListener(mContext) {
            @Override
            public void onOrientationChanged(int orientation) {
                if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
                    return;  //手机平放时，检测不到有效的角度
                }
                //只检测是否有四个角度的改变
                if (orientation > 350 || orientation < 10) { //0度
                    orientation = 90;
                } else if (orientation > 80 && orientation < 100) { //90度
                    orientation = 0;
                } else if (orientation > 170 && orientation < 190) { //180度
                    orientation = 270;
                } else if (orientation > 260 && orientation < 280) { //270度
                    orientation = 180;
                } else {
                    orientation = 0;
                }
                DataUtils.degree = orientation;
            }
        };
    }

    /**
     * 根据分辨率设置预览SurfaceView的大小以防止变形
     *
     * @param surfaceSize
     */
    private void setSurfaceViewSize(String surfaceSize) {
        ViewGroup.LayoutParams params = this.getLayoutParams();

        if(params == null )
            params = new ViewGroup.LayoutParams(160, 160);

        if (surfaceSize.equals("16:9")) {
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        } else if (surfaceSize.equals("4:3")) {
            params.height = 4 * screenWidth / 3;
        }
        this.setLayoutParams(params);
    }

    public String getSurfaceViewSize(int width, int height) {
        if (equalRate(width, height, 1.33f)) {
            return "4:3";
        } else {
            return "16:9";
        }
    }

    public boolean equalRate(int width, int height, float rate) {
        float r = (float) width / (float) height;
        if (Math.abs(r - rate) <= 0.2) {
            return true;
        } else {
            return false;
        }
    }


    // 提供一个静态方法，用于根据手机方向获得相机预览画面旋转的角度
    private int getPreviewDegree(Context context) {
        // 获得手机的方向
        int rotation = ((Activity)context).getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        // 根据手机的方向计算相机预览画面应该选择的角度
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 90;
                break;
            case Surface.ROTATION_90:
                degree = 0;
                break;
            case Surface.ROTATION_180:
                degree = 270;
                break;
            case Surface.ROTATION_270:
                degree = 180;
                break;
        }
        return degree;
    }

    private void destroyCamera() {
        CameraManager.getInstance().destroyCamera();
        getHolder().getSurface().release();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        CameraManager.getInstance().openCamera(holder,this,getPreviewDegree(mContext));
        CameraManager.getInstance().startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        CameraManager.getInstance().setCameraParameters(screenWidth,screenHeight);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        destroyCamera();
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {

    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

    }
}
