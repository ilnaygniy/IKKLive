package com.jqh.kklive.utils;

import android.graphics.Color;

import java.util.Random;

/**
 * Created by jiangqianghua on 18/2/3.
 */

public class ColorUtils {

    public static int getRandColor(){
        return Color.rgb(new Random().nextInt(255),new Random().nextInt(255),new Random().nextInt(255));
    }
}
