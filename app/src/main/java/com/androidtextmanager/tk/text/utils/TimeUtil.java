package com.androidtextmanager.tk.text.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by TK on 2015/6/5.
 */
public class TimeUtil {


    public static String getNowTimeString(){
        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss yyyy年MM月dd日");
        Date now = new Date();
       return fmt.format(now);
    }
}
