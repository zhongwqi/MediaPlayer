package com.example.irving.myproject1.activity.utils;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by Irving on 2017/10/27.
 */

public class Utils {
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;

    public Utils(){
        //转换成字符串的时间
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
    }

    /**
     * 把毫秒转换成，1：20：30这样的形式
     */

    public String stringForTime(int timeMs){
        int totalSeconds = timeMs / 1000;
        int second = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds /3600;

        mFormatBuilder.setLength(0);
        if(hours > 0){
            return mFormatter.format("%02d:%02d:%02d",hours,minutes,second).toString();
        }else{
            return mFormatter.format("%02d:%02d",minutes,second).toString();
        }
    }
}
