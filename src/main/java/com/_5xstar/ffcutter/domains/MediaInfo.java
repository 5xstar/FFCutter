package com._5xstar.ffcutter.domains;

import com._5xstar.ffcutter.Configure;
import com._5xstar.ffcutter.constant.PubConst;
import com._5xstar.ffcutter.util.StringUtil;

import java.util.Properties;

/**
  *媒体信息
  *庞海文 2020-4-12
**/
public class MediaInfo  implements Configure {
    private static String[] toStrHds={"时间信息包：" ,
            "        时长(时:分:秒)：" ,
            "        开始(秒)：" ,
            "        码率(kb/s)：" ,
            "视频信息包：" ,
            "        编码格式：",
            "        视频格式：" ,
            "        分辨率：" ,
            "音频信息包：" ,
            "        音频编码：" ,
            "        采样频率(Hz)："};

    public void conf(Properties prop){
        toStrHds = StringUtil.parse(prop,"toStrHds", toStrHds);
    }


    /**********
ffmpeg -i 123.mp4 返回
Metadata:
    major_brand     : isom
    minor_version   : 512
    compatible_brands: isomiso2avc1mp41
    encoder         : Lavf58.32.104
  Duration: 00:00:54.16, start: 0.000000, bitrate: 958 kb/s
    Stream #0:0(und): Video: h264 (High) (avc1 / 0x31637661), yuv420p, 640x360, 823 kb/s, 25 fps, 25 tbr, 12800 tbn, 50 tbc (default)
    Metadata:
      handler_name    : VideoHandler
    Stream #0:1(und): Audio: aac (LC) (mp4a / 0x6134706D), 44100 Hz, stereo, fltp, 128 kb/s (default)
    Metadata:
      handler_name    : SoundHandler
At least one output file must be specified

********/
     //时间信息包
     public static class Duration{
           //媒体播放持续时间 00:00:00.00
          public String duration=null;
          public long longDuration=-1L;
          //开始时间0.000000
          public String start=null;
          //码率 单位 kb
          public String bitrate=null;
     }
     final public Duration duration=new Duration();

     //视频信息包
     public static class Video{
         //编码格式
         public String encodeFormat=null;
         //视频格式
         public String videoFormat=null;
         //分辨率 
         public String size=null;
     }
     final public Video video=new Video();

     //音频信息包
     public static class Audio{
         //音频编码
         public String audioFormat=null;
         //音频采样频率 Hz
         public String sampling=null;
     }
     final public Audio audio=new Audio();

   //显示列表项目
   public String toString(){
       return new StringBuilder().append(toStrHds[0] ).append( PubConst.newLine )
               .append(toStrHds[1] ).append( duration.duration).append(PubConst.newLine )
               .append(toStrHds[2]).append( duration.start).append( PubConst.newLine )
               .append(toStrHds[3] ).append( duration.bitrate  ).append( PubConst.newLine )
               .append(toStrHds[4]  ).append( PubConst.newLine )
               .append( toStrHds[5]  ).append( video.encodeFormat  ).append( PubConst.newLine )
               .append( toStrHds[6] ).append( video.videoFormat  ).append( PubConst.newLine )
               .append(toStrHds[7] ).append( video.size  ).append( PubConst.newLine )
               .append( toStrHds[8]  ).append( PubConst.newLine )
               .append(toStrHds[9]  ).append( audio.audioFormat  ).append( PubConst.newLine )
               .append( toStrHds[10] ).append( audio.sampling  ).append(PubConst.newLine).toString();
   }

}
