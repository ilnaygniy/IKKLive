package com.jqh.ikkavlivemodule;

import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Build;
import android.view.SurfaceHolder;

import com.jqh.ikkavlivemodule.utils.DataUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by jiangqianghua on 18/1/30.
 */

public class CameraManager {

    private static CameraManager instance;
    private Camera camera;

    public static final int TYPE_PREVIEW = 0;
    public static final int TYPE_PICTURE = 1;
    public static final int ALLOW_PIC_LEN = 2000;       //最大允许的照片尺寸的长度   宽或者高

    private int cameraPosition;

        private CameraManager() {

        }

        public static CameraManager getInstance() {
            if (instance == null) {
                instance = new CameraManager();
            }
            return instance;
        }

        /**
         * 打开摄像头
         *
         * @param holder
         * @param autoFocusCallback
         * @param degree
         */
        public void openCamera(SurfaceHolder holder, Camera.AutoFocusCallback autoFocusCallback, int degree) {
            try {
                //初始化摄像头
                cameraPosition = Camera.CameraInfo.CAMERA_FACING_BACK;
                // 打开摄像头
                camera = Camera.open(cameraPosition);
                // 设置用于显示拍照影像的SurfaceHolder对象
                camera.setPreviewDisplay(holder);
                camera.setDisplayOrientation(degree);
                camera.autoFocus(autoFocusCallback);

            } catch (Exception e) {
    //                e.printStackTrace();
                camera.release();
                camera = null;
            }
        }

        /**
         * 设置参数
         */
        public void setCameraParameters(int screenWidth, int screenHeight) {
            try {
                if (camera != null) {
                    Camera.Parameters parameters = camera.getParameters();//获取各项参数
                    Camera.Size previewSize = findFitPreResolution(parameters);
                    parameters.setPreviewSize(previewSize.width, previewSize.height);// 设置预览大小
                    IKKLiveManager.getInstance().setVideoSize(previewSize.width,previewSize.height);
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                    parameters.setPictureFormat(PixelFormat.JPEG);//设置图片格式
                    //不能与setPreviewSize一起使用，否则setParamters会报错
    //                    parameters.setPreviewFrameRate(5);//设置每秒显示4帧
                    parameters.setJpegQuality(80);// 设置照片质量
                    Camera.Size pictureSize = null;
                    if (equalRate(screenWidth, screenHeight, 1.33f)) {
                        pictureSize = findFitPicResolution(parameters, (float) 4 / 3);
                    } else {
                        pictureSize = findFitPicResolution(parameters, (float) 16 / 9);
                    }

                    parameters.setPictureSize(pictureSize.width, pictureSize.height);// 设置保存的图片尺寸
                    camera.setParameters(parameters);
                    camera.startPreview();

                    // 实现自动对焦
                    camera.autoFocus(new Camera.AutoFocusCallback() {
                        @Override
                        public void onAutoFocus(boolean success, Camera camera) {
                            if (success) {
                                camera.cancelAutoFocus();// 只有加上了这一句，才会自动对焦
                                doAutoFocus();
                            }
                        }
                    });

                    camera.setPreviewCallback(new Camera.PreviewCallback() {
                        @Override
                        public void onPreviewFrame(byte[] data, Camera camera) {
                            IKKLiveManager.getInstance().flushVideoData(data);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 自动对焦
        // handle button auto focus
        private void doAutoFocus() {
            if(cameraPosition != Camera.CameraInfo.CAMERA_FACING_BACK)
                return ;
            final Camera.Parameters parameters1 = camera.getParameters();
            parameters1.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            camera.setParameters(parameters1);
            camera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    if (success) {
                        camera.cancelAutoFocus();// 只有加上了这一句，才会自动对焦。
                        if (!Build.MODEL.equals("KORIDY H30")) {
                            Camera.Parameters parameters = camera.getParameters();
                            parameters = camera.getParameters();
                            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 1连续对焦
                            camera.setParameters(parameters);
                        }else{
                            Camera.Parameters parameters = camera.getParameters();
                            parameters = camera.getParameters();
                            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                            camera.setParameters(parameters);
                        }
                    }
                }
            });
        }

        /**
         * 摄像头切换
         *
         * @param holder
         * @param autoFocusCallback
         * @param degree
         */
        public void turnCamera(SurfaceHolder holder, Camera.AutoFocusCallback autoFocusCallback, int degree, int screenWidth, int screenHeight) {
            //切换前后摄像头
            //现在是后置，变更为前置
            if (camera != null && cameraPosition == Camera.CameraInfo.CAMERA_FACING_BACK) {
                camera.stopPreview();//停掉原来摄像头的预览
                camera.release();//释放资源
                camera = null;//取消原来摄像头
                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);//打开当前选中的摄像头
                try {
                    camera.setPreviewDisplay(holder);//通过surfaceview显示取景画面
                    camera.setDisplayOrientation(degree);
                    camera.autoFocus(autoFocusCallback);
                    setCameraParameters(screenWidth, screenHeight);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                camera.startPreview();//开始预览
                cameraPosition = Camera.CameraInfo.CAMERA_FACING_FRONT;
                DataUtils.isBackCamera = false;
            } else if (cameraPosition == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                //代表摄像头的方位，CAMERA_FACING_FRONT前置
                // CAMERA_FACING_BACK后置
                //现在是前置， 变更为后置
                camera.stopPreview();//停掉原来摄像头的预览
                camera.release();//释放资源
                camera = null;//取消原来摄像头
                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);//打开当前选中的摄像头
                try {
                    camera.setPreviewDisplay(holder);//通过surfaceview显示取景画面
                    camera.setDisplayOrientation(degree);
                    camera.autoFocus(autoFocusCallback);
                    setCameraParameters(screenWidth, screenHeight);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                camera.startPreview();//开始预览
                cameraPosition = Camera.CameraInfo.CAMERA_FACING_BACK;
                DataUtils.isBackCamera = true;
            }
        }


        /**
         * 销毁摄像头
         */
        public void destroyCamera() {
            if (camera != null) {
                //当surfaceview关闭时，关闭预览并释放资源
                camera.stopPreview();
                camera.release();//释放相机
                camera = null;
            }

            IKKLiveManager.getInstance().stopLive();
        }

    /**
     * 重拍
     */
    public void startPreview() {
        if (camera != null) {
            camera.startPreview();
        }
    }

    /**
     * 停止拍摄
     */
    public void stopPreview() {
        camera.stopPreview();
    }

        private boolean equalRate(int width, int height, float rate) {
            float r = (float) width / (float) height;
            if (Math.abs(r - rate) <= 0.2) {
                return true;
            } else {
                return false;
            }
        }

        /**
         * 返回最小的预览尺寸
         *
         * @param cameraInst
         * @param type
         * @return
         */
        private Camera.Size findMinResolution(Camera cameraInst, int type) throws Exception {
            Camera.Parameters cameraParameters = cameraInst.getParameters();
            List<Camera.Size> supportedPicResolutions = type == TYPE_PREVIEW ? cameraParameters.getSupportedPreviewSizes() : cameraParameters.getSupportedPictureSizes(); // 至少会返回一个值

            if (supportedPicResolutions == null) {
                return null;
            }

            Camera.Size resultSize = supportedPicResolutions.get(0);
            for (Camera.Size size : supportedPicResolutions) {
                if (size.width < resultSize.width) {
                    resultSize = size;
                }
            }
            return resultSize;
        }

        /**
         * 找到合适的尺寸
         *
         * @param cameraParameters
         * @param maxDistortion    最大允许的宽高比
         * @return
         * @type 尺寸类型 0：preview  1：picture
         */
        public Camera.Size findBestResolution(Camera.Parameters cameraParameters, double maxDistortion, int type) throws Exception {
            List<Camera.Size> supportedPicResolutions = type == TYPE_PREVIEW ? cameraParameters.getSupportedPreviewSizes() : cameraParameters.getSupportedPictureSizes(); // 至少会返回一个值

//        StringBuilder picResolutionSb = new StringBuilder();
//        for (Camera.Size supportedPicResolution : supportedPicResolutions) {
//            picResolutionSb.append(supportedPicResolution.width).append('x')
//                    .append(supportedPicResolution.height).append(" ");
//        }
////        Log.d(TAG, "Supported picture resolutions: " + picResolutionSb);
//
//        Camera.Size defaultPictureResolution = cameraParameters.getPictureSize();
//        Log.d(TAG, "default picture resolution " + defaultPictureResolution.width + "x"
//                + defaultPictureResolution.height);

            // 排序
            List<Camera.Size> sortedSupportedPicResolutions = new ArrayList<>(
                    supportedPicResolutions);
            Collections.sort(sortedSupportedPicResolutions, new Comparator<Camera.Size>() {
                @Override
                public int compare(Camera.Size a, Camera.Size b) {
                    int aRatio = a.width / a.height;
                    int bRatio = b.width / a.height;

                    if (Math.abs(aRatio - 1) <= Math.abs(bRatio - 1)) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });

            //返回最合适的
            return sortedSupportedPicResolutions.get(sortedSupportedPicResolutions.size() - 1);
        }

        /**
         * 返回合适的照片尺寸参数
         *
         * @param cameraParameters
         * @param bl
         * @return
         */
        private Camera.Size findFitPicResolution(Camera.Parameters cameraParameters, float bl) throws Exception {
            List<Camera.Size> supportedPicResolutions = cameraParameters.getSupportedPictureSizes();

            Camera.Size resultSize = null;
            for (Camera.Size size : supportedPicResolutions) {
                if ((float) size.width / size.height == bl && size.width <= ALLOW_PIC_LEN && size.height <= ALLOW_PIC_LEN) {
                    if (resultSize == null) {
                        resultSize = size;
                    } else if (size.width > resultSize.width) {
                        resultSize = size;
                    }
                }
            }
            if (resultSize == null) {
                return supportedPicResolutions.get(0);
            }
            return resultSize;
        }

        /**
         * 返回合适的预览尺寸参数
         *
         * @param cameraParameters
         * @return
         */
        private Camera.Size findFitPreResolution(Camera.Parameters cameraParameters) throws Exception {
            List<Camera.Size> supportedPicResolutions = cameraParameters.getSupportedPreviewSizes();

            Camera.Size resultSize = null;
            for (Camera.Size size : supportedPicResolutions) {
                if (size.width <= ALLOW_PIC_LEN) {
                    if (resultSize == null) {
                        resultSize = size;
                    } else if (size.width > resultSize.width) {
                        resultSize = size;
                    }
                }
            }
            if (resultSize == null) {
                return supportedPicResolutions.get(0);
            }
            return resultSize;
        }
    }


