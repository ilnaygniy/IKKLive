package com.jqh.ikkavlivemodule.utils;

import android.hardware.Camera;
import android.util.Log;

import java.util.List;

/**
 * Created by jiangqianghua on 18/1/30.
 */

public class CameraUtils {

    /**
     * 通过对比得到与宽高比最接近的尺寸（如果有相同尺寸，优先选择）
     *
     * @param surfaceWidth
     *            需要被进行对比的原宽
     * @param surfaceHeight
     *            需要被进行对比的原高
     * @param camera
     *            相机实例
     * @return 得到与原宽高比例最接近的尺寸
     */
    protected Camera.Size getCloselyPreSize(int surfaceWidth, int surfaceHeight,
                                            Camera camera) {


        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> preSizeList = parameters.getSupportedPreviewSizes();
        int ReqTmpWidth;
        int ReqTmpHeight;
        // 当屏幕为垂直的时候需要把宽高值进行调换，保证宽大于高
        //mIsPortrait
        if (true) {
            ReqTmpWidth = surfaceHeight;
            ReqTmpHeight = surfaceWidth;
        } else {
            ReqTmpWidth = surfaceWidth;
            ReqTmpHeight = surfaceHeight;
        }
        //先查找preview中是否存在与surfaceview相同宽高的尺寸
        for(Camera.Size size : preSizeList){
            if((size.width == ReqTmpWidth) && (size.height == ReqTmpHeight)){
                return size;
            }
        }

        // 得到与传入的宽高比最接近的size
        float reqRatio = ((float) ReqTmpWidth) / ReqTmpHeight;
        float curRatio, deltaRatio;
        float deltaRatioMin = Float.MAX_VALUE;
        Camera.Size retSize = null;
        for (Camera.Size size : preSizeList) {
            curRatio = ((float) size.width) / size.height;
            deltaRatio = Math.abs(reqRatio - curRatio);
            if (deltaRatio < deltaRatioMin) {
                deltaRatioMin = deltaRatio;
                retSize = size;
            }
        }

        return retSize;
    }


    /**
     * 修改相机的预览尺寸，调用此方法就行
     *
     * @param camera     相机实例
     * @param viewWidth  预览的surfaceView的宽
     * @param viewHeight 预览的surfaceView的高
     */
    protected void changePreviewSize(Camera camera, int viewWidth, int viewHeight) {
        if (camera == null) {
            return;
        }
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();
        Camera.Size closelySize = null;//储存最合适的尺寸
        for (Camera.Size size : sizeList) { //先查找preview中是否存在与surfaceview相同宽高的尺寸
            if ((size.width == viewWidth) && (size.height == viewHeight)) {
                closelySize = size;
            }
        }
        if (closelySize == null) {
            // 得到与传入的宽高比最接近的size
            float reqRatio = ((float) viewWidth) / viewHeight;
            float curRatio, deltaRatio;
            float deltaRatioMin = Float.MAX_VALUE;
            for (Camera.Size size : sizeList) {
                if (size.width < 1024) continue;//1024表示可接受的最小尺寸，否则图像会很模糊，可以随意修改
                curRatio = ((float) size.width) / size.height;
                deltaRatio = Math.abs(reqRatio - curRatio);
                if (deltaRatio < deltaRatioMin) {
                    deltaRatioMin = deltaRatio;
                    closelySize = size;
                }
            }
        }
        if (closelySize != null) {
            Log.i("changePreviewSize", "预览尺寸修改为：" + closelySize.width + "*" + closelySize.height);
            parameters.setPreviewSize(closelySize.width, closelySize.height);
            camera.setParameters(parameters);
        }
    }
}
