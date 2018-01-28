package com.jqh.kklive.task;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by jiangqianghua on 18/1/6.
 */

public class Poster extends Handler {

    private static Poster instance;

    public static Poster getInstance(){
        if(instance == null){
            synchronized (Poster.class){
                if(instance == null)
                    instance = new Poster();
            }
        }
        return instance;
    }

    private Poster(){
        super(Looper.getMainLooper());
    }
}
