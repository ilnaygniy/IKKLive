package com.jqh.jmedia;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.jqh.jmedia.utils.FileUtils;

import java.io.IOException;
import java.util.Map;

/**
 * Created by jiangqianghua on 2018/1/18.
 * media playe controller class
 */
public class JMediaPlayer {

    static {
        System.loadLibrary("ffmpegjni");
    }

    private static final String TAG = "JMediaPlayer";

    // 定义一些私有属性
    private Context mContext ;

    private Surface mSurface ;

    private SurfaceHolder mSurfaceHolder ;

    private EventHandler mEventHandler ;

    private SurfaceDraw mSurfaceDraw ;

    private JAudioPlayer mAudioPlayer ;

    // 定义一些静态常量
    private static final int MEDIA_PREPARED = 1 ;
    private static final int MEDIA_PLAYBACK_COMPLETE = 2 ;
    private static final int MEDIA_BUFFERING_UPDATE = 3 ;
    private static final int MEDIA_SEEK_COMPLETE = 4 ;
    private static final int MEDIA_ERROR = 100 ;
    private static final int MEDIA_INFO = 200 ;

    // 接口对象
    private OnPreparedListener mOnPreparedListener ;
    private OnCompletionListener mOnCompletionListener ;
    private OnBufferingUpdateListener mOnBufferingUpdateListener ;
    private OnSeekCompleteListener mOnSeekCompleteListener;
    private OnErrorListener mOnErrorListener;
    private OnInfoListener mOnInfoListener;

    public void setOnPreparedListener(OnPreparedListener mOnPreparedListener) {
        this.mOnPreparedListener = mOnPreparedListener;
    }

    public void setOnInfoListener(OnInfoListener mOnInfoListener) {
        this.mOnInfoListener = mOnInfoListener;
    }

    public void setOnErrorListener(OnErrorListener mOnErrorListener) {
        this.mOnErrorListener = mOnErrorListener;
    }

    public void setOnSeekCompleteListener(OnSeekCompleteListener mOnSeekCompleteListener) {
        this.mOnSeekCompleteListener = mOnSeekCompleteListener;
    }

    public void setOnBufferingUpdateListener(OnBufferingUpdateListener mOnBufferingUpdateListener) {
        this.mOnBufferingUpdateListener = mOnBufferingUpdateListener;
    }

    public void setOnCompletionListener(OnCompletionListener mOnCompletionListener) {
        this.mOnCompletionListener = mOnCompletionListener;
    }


    // jni方法
    /**
     * 初始化底层Native
     */
    private native void _initNative();

    /**
     *
     * @param path play url
     * @param keys   http header key
     * @param values http header value
     */
    private native void _setDataSource(String path,String[] keys,String[] values);

    /**
     *  准备播放，同步操作
     */
    public native void prepare();

    /**
     * 准备播放，异步操作
     */
    public native void prepareAsync();

    /**
     * 开始播放
     */
    private native void _start();

    /**
     * 停止播放
     */
    private native void _stop();

    /**
     * 暂停播放
     */
    private native void _pause();

    /**
     * 是否正在播放
     * @return
     */
    public native boolean isPlaying();


    /**
     * 毫秒单位，指定到某一时间播放
     * @param msec
     */
    public native void seekTo(long msec);

    /**
     * 倍速
     * @param speed
     */
    public native void setPlaybackSpeed(float speed);

    /**
     * 从jni层获取当前视屏播放的一些参数
     * @return
     */
    public native long getDuration();

    public native int getVideoWidth();

    public native int getVideoHeight();

    public native int getSampleRate();

    public native boolean is16Bit();

    public native boolean isStereo();

    public native int getDesiredFrames();


    // jni回调接口

    /**
     * jni 回调资源加载准备完成工作,返回加载完的一些参数
     */
    public void jni_perpare(){
        Message m = mEventHandler.obtainMessage(MEDIA_PREPARED);
        mEventHandler.sendMessage(m);
    }

    public void jni_flush_video_data(byte[] data){
        if(mSurfaceDraw != null){
            mSurfaceDraw.flushVideoData(data);
        }
    }

    public void jni_flush_audio_short_data(short[] data){
        mAudioPlayer.audioWriteShortBuffer(data);
    }

    public void jni_flush_audio_byte_data(byte[] data){
        mAudioPlayer.audioWriteByteBuffer(data);
    }

    public void jni_play_completion(){
        Message m = mEventHandler.obtainMessage(MEDIA_PLAYBACK_COMPLETE);
        mEventHandler.sendMessage(m);
    }

    public void jni_seek_complete(){
        Message m = mEventHandler.obtainMessage(MEDIA_SEEK_COMPLETE);
        mEventHandler.sendMessage(m);
    }

    public void jni_error(int what,int extra){
        Message m = mEventHandler.obtainMessage(MEDIA_ERROR);
        mEventHandler.sendMessage(m);
    }

    public void jni_info(int what, int extra){
        Message m = mEventHandler.obtainMessage(MEDIA_INFO);
        mEventHandler.sendMessage(m);
    }

    // 接口回调
    public interface OnPreparedListener{
        /**
         * 当媒体文件准备完成回调
         * @param mp
         */
        void onPrepared(JMediaPlayer mp);
    }


    public  interface  OnCompletionListener{
        /**
         * 当文件播放完成回调
         */
        void onCompletion(JMediaPlayer mp);
    }

    public interface OnBufferingUpdateListener{
        /**
         * 缓冲进度回调
         * @param mp  0 到 100
         * @param percent
         */
        void onBufferingUpdate(JMediaPlayer mp,int percent);
    }

    public  interface  OnSeekCompleteListener{

        /**
         * 用户seek后，seek成功回调事件
         * @param mp
         */
        void onSeekComplete(JMediaPlayer mp);
    }


    public interface  onVideoSizeChangedListener{
        /**
         * 视频大小发现改变调用
         * @param mp
         * @param width
         * @param height
         */
        void onVideoSizeChanged(JMediaPlayer mp , int width , int height);
    }

    public interface OnErrorListener{
        /**
         * 播放发生错误回调
         * @param mp
         * @param what
         * @param extra
         * @return
         */
        boolean onError(JMediaPlayer mp, int what,int extra);
    }


    public interface OnInfoListener{

        /**
         *
         * @param mp
         * @param what
         * @param extra
         * @return
         */
        boolean onInfo(JMediaPlayer mp,int what, int extra);
    }

    //  java层函数

    public JMediaPlayer(Context ctx){
        mContext = ctx ;
        Looper looper ;
        if((looper = Looper.myLooper()) != null)
            mEventHandler = new EventHandler(this,looper);
        else if((looper = Looper.getMainLooper()) != null)
            mEventHandler = new EventHandler(this,looper);
        else
            mEventHandler = null ;

        _initNative();

    }
    /**
     * 直接设置播放路径
     * @param path
     */
    public void setDataSource(String path){
        _setDataSource(path,null,null);
    }

    /**
     * 设置播放路径
     * @param context
     * @param uri
     */
    public void setDataSource(Context context, Uri uri)throws IOException ,IllegalArgumentException {
        setDataSource(context,uri,null);
    }

    public void setDataSource(String path , Map<String,String> headers){
        String[] keys = null ;
        String[] values = null ;

        keys = new String[headers.size()];
        values = new String[headers.size()];
        if(headers != null){
            int i = 0 ;
            for(Map.Entry<String,String> entry: headers.entrySet()){
                keys[i] = entry.getKey();
                values[i] = entry.getValue();
                ++i;
            }
        }
        _setDataSource(path,keys,values);
    }
    /**
     * 设置播放路径，带http请求头部信息
     * @param context
     * @param uri
     * @param headers
     */
    public void setDataSource(Context context, Uri uri, Map<String,String> headers)throws IOException ,IllegalArgumentException{
        if(context == null || uri == null)
            throw new IllegalArgumentException();
        String scheme = uri.getScheme();
        // 判断是否是本地文件
        if(scheme == null || scheme.equals("file")){
            setDataSource(FileUtils.getPath(uri.toString()));
            return ;
        }
        setDataSource(uri.toString(),headers);
    }


    public void setDisplay(SurfaceHolder sh){
        if(sh == null){
            releaseDisplay();
        }else{
            mSurfaceHolder = sh ;
            mSurface = sh.getSurface();
            if(mSurfaceDraw == null)
                mSurfaceDraw = new SurfaceDraw(mSurfaceHolder);
            else
                mSurfaceDraw.setSurfaceHolder(mSurfaceHolder);
            updateSurfaceScreenOn();
        }
    }

    public void setSurface(Surface surface){
        if(surface == null){
            releaseDisplay();
        }else{
            mSurfaceHolder = null ;
            mSurface = surface ;
            updateSurfaceScreenOn();
        }
    }

    /**
     * 更新是否保持屏幕亮
     */
    private void updateSurfaceScreenOn(){
        if(mSurfaceHolder != null)
            mSurfaceHolder.setKeepScreenOn(true); // TODO
    }

    /**
     * 释放display
     */
    public void releaseDisplay(){
        mSurfaceHolder = null ;
        mSurface = null ;
    }

    public void start(){
        _start();
    }

    public void stop(){
        _stop();
    }

    public void pause(){
        _pause();
    }


    private class EventHandler extends Handler{

        private JMediaPlayer mMediaPlayer ;
        private Bundle mData ;

        public EventHandler(JMediaPlayer mp, Looper looper){
            super(looper);
            mMediaPlayer = mp ;
        }
        public void release(){
            mMediaPlayer = null ;
        }

        @Override
        public void handleMessage(Message msg) {
            if(mMediaPlayer == null){
                Log.e(TAG,"mediaPlayer 没有初始化");
                return ;
            }
            switch (msg.what){
                case MEDIA_PREPARED:
                    if(mOnPreparedListener != null){

                        if(mSurfaceDraw == null)
                            mSurfaceDraw = new SurfaceDraw();
                        mSurfaceDraw.width = getVideoWidth();
                        mSurfaceDraw.height = getVideoHeight();

                        mAudioPlayer.audioInit(getSampleRate(),is16Bit(),isStereo(),getDesiredFrames());
                        mOnPreparedListener.onPrepared(mMediaPlayer);
                    }
                    break;
                case MEDIA_PLAYBACK_COMPLETE:
                    if(mOnCompletionListener != null){
                        mOnCompletionListener.onCompletion(mMediaPlayer);
                    }
                    break;
                case MEDIA_SEEK_COMPLETE:
                    if(mOnSeekCompleteListener != null){
                        mOnSeekCompleteListener.onSeekComplete(mMediaPlayer);
                    }
                    break;
                case MEDIA_BUFFERING_UPDATE:
                    if(mOnBufferingUpdateListener != null){
                        //mOnBufferingUpdateListener.onBufferingUpdate(mMediaPlayer);
                    }
            }
        }
    }



}
