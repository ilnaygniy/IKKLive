package com.jqh.ikkavlivemodule;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.view.SurfaceHolder;

import com.jqh.jmedia.JMediaPushStream;
import com.jqh.jmedia.SurfaceDraw;

/**
 * 直播推流管理
 * Created by jiangqianghua on 18/2/10.
 */
public class IKKLiveManager {

    private static IKKLiveManager instance ;

    private IKKLiveManager(){
        mJMediaPushStream = new JMediaPushStream();
        mPcmRecordThread = new PcmRecordThread();
    }

    public static IKKLiveManager getInstance(){
        if(instance == null){
            synchronized (IKKLiveManager.class){
                if(instance == null){
                    instance = new IKKLiveManager();
                }
            }
        }
        return instance ;
    }

    PcmRecordThread mPcmRecordThread;

    private int viewH , viewW ;

    private JMediaPushStream mJMediaPushStream ;

    public void setVideoSize(int w , int h){
        viewH = h ;
        viewW = w ;
    }

    public void startLive(){
        String filePath = "rtmp://livemediabspb.xdfkoo.com/mediaserver/live334422113";
        mJMediaPushStream.publicStreamInit(filePath,viewW,viewH);
        mPcmRecordThread.start();
    }

    public void stopLive(){
        mPcmRecordThread.stopRecord();
        mPcmRecordThread.stop();
        mJMediaPushStream.stopStream();
    }

    public void flushVideoData(byte[] data)
    {
        mJMediaPushStream.flushStreamDataToJni(data,1);
    }


    private class PcmRecordThread extends Thread {
        private int sampleRate = 22050;
        private AudioRecord audioRecord;
        private int minBufferSize = 0;
        private boolean isRecording = false;

        public PcmRecordThread() {
            minBufferSize = AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT);
            minBufferSize = 4096;
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT, minBufferSize);

        }

        @Override
        public synchronized void start() {
            audioRecord.startRecording();
            isRecording = true;
            super.start();
        }

        @Override
        public void run() {
            while (isRecording == true) {
                byte[] bytes = new byte[minBufferSize];
                if (audioRecord == null)
                    return;
                int res = audioRecord.read(bytes, 0, minBufferSize);
                if (res > 0 && isRecording == true) {
                    mJMediaPushStream.flushStreamDataToJni(bytes,0);
                }
            }
        }

        public void stopRecord() {
            isRecording = false;
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
    }



}
