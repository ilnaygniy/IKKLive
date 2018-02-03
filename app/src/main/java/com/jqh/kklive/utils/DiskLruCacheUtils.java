package com.jqh.kklive.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 磁盘缓存的使用
 * Created by user on 2017/12/15.
 */
public class DiskLruCacheUtils {

    public final static String TAG = "DiskLruCacheUtils";
    private Context mContext ;
    private String cacheName = "jcache";

    private DiskLruCache diskLruCache ;
    private long MAXSIZE = 10*1024*1024 ;
    private static DiskLruCacheUtils instance ;
    private OkHttpClient client ;
    public static Bitmap.Config IMAGE_FORMAT = Bitmap.Config.ARGB_8888 ;

    public synchronized  static DiskLruCacheUtils getInstance(){
        if(instance == null){
            instance = new DiskLruCacheUtils();
        }
        return instance ;
    }

    /**
     * 初始化
     * @param context
     */
    public void init(Context context){
        mContext = context ;
        File cacheFile = getDiskCacheDir(context,cacheName);
        try {
            diskLruCache = DiskLruCache.open(cacheFile, 1, 1, MAXSIZE);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 获取默认的缓存路径
     * @param context
     * @param cacheName
     * @return
     */
    private File getDiskCacheDir(Context context,String cacheName){
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }

        File dir = new File(cachePath + File.separator + cacheName);
        if (!dir.exists()) {
            Log.d(TAG, "缓存目录不存在，创建之...");
            dir.mkdirs();
        } else {
            Log.d(TAG, "缓存目录已存在，不需创建.");
        }

        Log.d(TAG,"缓存目录:"+dir.getAbsolutePath());
        return dir;
    }

    /**
     * 缓存图片
     * @param imageUrl
     */
    public void showImage(final ImageView imageview , final String imageUrl){
        //先从本地获取
        Bitmap bmp = loadFromDisk(imageUrl);
        if(bmp == null) {
            //本地获取失败，再从网络获取
            loadFromNetWork(imageview, imageUrl);
        }
        else
        {
            imageview.setImageBitmap(bmp);
        }
    }

    private void loadFromNetWork(final ImageView imageview , final String imageUrl){
        client = new OkHttpClient();
        final Request request = new Request.Builder().get().url(imageUrl).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 保存
                byte[] bytes = response.body().bytes();
                try {
                    writeBytesToDiskLruCache(imageUrl, bytes);
                    //final Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    final Bitmap bmp = loadFromDisk(imageUrl);
                    if(bmp == null){
                        Log.d(TAG,"加载图片失败");
                        return ;
                    }
                    //回调是运行在非ui主线程，
                    //数据请求成功后，在主线程中更新
//                    mContext.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            //网络图片请求成功，更新到主线程的ImageView
//                            imageview.setImageBitmap(bmp);
//                        }
//                    });
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
    private void writeBytesToDiskLruCache(String imageUrl,byte[] bytes) throws Exception{
        String key = MD5Utils.getStringByMD5(imageUrl);
        DiskLruCache.Editor editor = diskLruCache.edit(key);
        OutputStream os = editor.newOutputStream(0);
        os.write(bytes);
        os.flush();
        editor.commit();
    }

    /**
     * 从磁盘读取bitmap
     * @param url
     */
    private Bitmap loadFromDisk(String url){
        DiskLruCache.Snapshot snapshot = null ;
        String key = MD5Utils.getStringByMD5(url);
        try {
            snapshot = diskLruCache.get(key);
        }catch (Exception e){
            e.printStackTrace();
            return null ;
        }

        if(snapshot != null){
            InputStream is = snapshot.getInputStream(0);
            Bitmap bm = loadBitMapFromInputStream(is);
            return bm ;
        }
        return null ;
    }

    /**
     * 转RGB_565模式bitmap
     * @param is
     * @return
     */
    private Bitmap loadBitMapFromInputStream(InputStream is){
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = IMAGE_FORMAT;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        return BitmapFactory.decodeStream(is,null,opt);
    }


    public void set(String key,String value) throws Exception{
        DiskLruCache.Editor editor = diskLruCache.edit(key);
        OutputStream os = editor.newOutputStream(0);
        os.write(value.getBytes());
        os.flush();
        editor.commit();
    }

    public void del(String key) throws Exception{
        set(key,"");
    }

    public String get(String key) throws Exception{
        DiskLruCache.Snapshot snapshot = null ;
        try {
            snapshot = diskLruCache.get(key);
        }catch (Exception e){
            e.printStackTrace();
            return null ;
        }

        if(snapshot != null){
            InputStream is = snapshot.getInputStream(0);
            byte[] byt = new byte[is.available()];
            is.read(byt);
            return new String(byt);
        }
        return null ;
    }

}
