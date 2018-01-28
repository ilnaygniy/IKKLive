package com.jqh.kklive.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jiangqianghua on 18/1/28.
 */

public class CommonUtils {

    public static String getIdentifier(){
        SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyy-MM-dd-HH-mm-ss");
        Date curDate =  new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }
}
