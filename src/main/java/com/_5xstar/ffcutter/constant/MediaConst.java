package com._5xstar.ffcutter.constant;

import com._5xstar.ffcutter.Configure;
import com._5xstar.ffcutter.MainFrameProps;
import com._5xstar.ffcutter.util.StringUtil;

import java.io.File;
import java.util.Properties;

/**
 * 媒体常数  静态引用会拷贝，配置无效
 * 庞海文 2020-2-16
 **/
public class MediaConst implements Configure 
{

     //ffmpeg 加工命令
     public static String ffmpeg="ffmpeg";

     //ffmpeg 显示命令
     public static String ffplay="ffplay";

     //外播放器，完成后用外播放器"C:\\Program Files (x86)\\IQIYI Video\\GeePlayer\\5.2.61.5220\\GeePlayer.exe";
     public static String extPlay=ffplay;

     //图片
     public static String[] imageTypes=new String[]{"jpg", "jpeg", "gif", "png", "bmp", "tif", "pcx", "tga", "exif", "fpx", "svg", "psd", "cdr", "pcd", "dxf", "ufo", "eps", "ai", "raw", "WMF", "webp", "avif", "apng"};
     //音乐
     public static String[] musicTypes=new String[]{"au", "mp2", "mp3", "wav", "cda", "aiff", "mid", "wma", "ra", "rm", "rmx", "vqf", "ogg", "amr", "ape", "flac", "aac"};
     //媒体，在配置文件中更新
     public static String[] mediaTypes=new String[]{"au", "avi", "dv", "flv", "gif", "gxf", "kux", "lvf", "lxf", "m4v", "mjpeg", "mjpeg_2000", "mlp", "mlv", "mov", "mp4", "m4a", "3gp", "3g2", "mj2", "mp2", "mp3", "mpeg", "mpegvideo", "mpjpeg", "mv", "mvi", "mxg", "nc", "nsv", "nuv", "ogg", "oma", "pvf", "rawvideo", "rm", "rpl", "smjpeg", "svag", "swf", "tmv", "tta", "vag", "vc1", "vc1test", "vcd", "vivo", "vmd", "vob", "voc", "vpk", "vqf", "w64", "wav", "wc3movie", "webm", "wma", "wmv", "wsaud", "wsd", "wsvqa", "wtv", "wv", "wve", "xa", "xbin", "xmv", "xvag", "xwma", "yop"};
      //媒体目录
     public static File extMediaDir=new File("extmedia");
     static{
         if(extMediaDir.exists() && extMediaDir.isFile())extMediaDir.delete();
         if(!extMediaDir.exists())extMediaDir.mkdirs();
     }

     //最大可以采用拷贝策略的数 单位 M
     public static long mediaMaxCopyLength=500L;


    /**
     *@prop 配置值
    **/
    public void conf(final Properties prop){
         ffmpeg=StringUtil.parse(prop,"ffmpeg",ffmpeg);
         ffplay=StringUtil.parse(prop,"ffplay",ffplay);
         extPlay=StringUtil.parse(prop,"extPlay",extPlay);
        imageTypes=StringUtil.parse(prop,"imageTypes",imageTypes);
         musicTypes=StringUtil.parse(prop,"musicTypes",musicTypes);
         mediaTypes=StringUtil.parse(prop,"mediaTypes",mediaTypes);
         extMediaDir=StringUtil.parse(prop,"extMediaDir",extMediaDir);
         if(extMediaDir.exists() && extMediaDir.isFile())extMediaDir.delete();
         if(!extMediaDir.exists())extMediaDir.mkdirs();
         mediaMaxCopyLength=StringUtil.parse(prop,"mediaMaxCopyLength",mediaMaxCopyLength);
    }

}

