package com._5xstar.ffcutter.util;


/**
*播放器工具类
*庞海文 2022-1-2
**/
final public class VLCJUtil {

    /**
     *构建分秒时间
    **/
    public static String buildTime(long time){
        int s = (int)(time/1000);
        int m = s/60;
        s = s-m*60;
        StringBuilder sb = new StringBuilder();
        sb.append(m);
        sb.append(':');
        if(s<10)sb.append('0');
        sb.append(s);
        return sb.toString();
    }

      
}
