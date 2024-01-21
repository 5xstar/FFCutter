package com._5xstar.ffcutter.player;

import com._5xstar.ffcutter.Configure;
import com._5xstar.ffcutter.util.StringUtil;

import java.util.Properties;

/**
 * 播放器UI状态
 * 庞海文  2024-1-20
 */
public interface PlayerUIStatus {
    class Const implements Configure
    {
        //String background = Color.BLACK;
        //String canvasBackground = Color.BLACK;
        public static int defaultWidth = 800;
        public static int defaultHeight = 600;

        //filename1[,delay1[,opacity1];filename2[,delay2[,opacity2];filename3[,delay3[,opacity3];可制作动图
        public static String logoFile = "logo/jtrIcon.png";
        public static String musicImgFile = "logo/jtrIcon2.png";
        public static String marqueeText = "https://5xstar.com";

        //VLCJPlayerType playerType = VLCJPlayerType.AWTCANVAS;

        public static int progressWaitTimeMillis = 1000;  //进度关联1秒
        public static int progressWaitTimes = 3;  //等待次数
        public static long defaultMediaLength = 15L*60*1000;  //默认媒体时长，15分钟
        public static int sentWaitTimeMillis = 3000;  //检查媒体是否播完时间间隔
        public static int waitSenderReadyTimeMillis = 500;  //等待发送方准备好
        public static int minPauseTime = 5;  //最短暂停时间，防频繁操作，本地播放不受限制

        public static int doubleClickedInterval = 1000;  //双击间隔
        public static int prepareTracksWaitTimeMillis = 3000; //轨道数据等待时间3秒
        public static int resizedNewIntervalMillis = 5000;   //新变动判断5秒
        public static int resizingIntervalMillis = 500;   //变动期间判断0.5秒
        public static int hideMouseCursorWaitTimeMillis=5000;  //全屏模式下隐藏鼠标时间
        public static int flushTimeMillis=3000; //无时间的等待3秒
        public static int flushIntervalMillis=50;  //时间采样
        public static int maxWaitStartTimes=7000/flushIntervalMillis;  //等待开始最大次数

        public static int flowWaitDelayTimeMillis = 5000;    //加载后渲染器需要大概2秒时间，提前加载
        public static int prePlayPrefixTimeMillis=5000;  //预览前置时间
        public static int jumpSafeTimeMillis=250;  //滑动检测安全时间
        /**
         *@prop 配置值
         **/
        public void conf(final Properties prop){
            //background=StringUtil.parse(prop,"background",background);
            // canvasBackground=StringUtil.parse(prop,"canvasBackground",canvasBackground);
            defaultWidth= StringUtil.parse(prop,"defaultWidth",defaultWidth);
            defaultHeight=StringUtil.parse(prop,"defaultHeight",defaultHeight);

            //String playerTypeStr=StringUtil.parse(prop,"playerType",(String)null);
            // if(playerTypeStr!=null)playerType=VLCJPlayerType.get(playerTypeStr);

            //logoFile=StringUtil.parse(prop,"logoFile",logoFile);
            musicImgFile=StringUtil.parse(prop,"musicImgFile",musicImgFile);
            //marqueeText=StringUtil.parse(prop,"marqueeText",marqueeText);

            progressWaitTimeMillis=StringUtil.parse(prop,"progressWaitTimeMillis",progressWaitTimeMillis);
            progressWaitTimes=StringUtil.parse(prop,"progressWaitTimes",progressWaitTimes);
            defaultMediaLength=StringUtil.parse(prop,"defaultMediaLength",defaultMediaLength);
            sentWaitTimeMillis=StringUtil.parse(prop,"sentWaitTimeMillis",sentWaitTimeMillis);
            waitSenderReadyTimeMillis=StringUtil.parse(prop,"waitSenderReadyTimeMillis",waitSenderReadyTimeMillis);

            doubleClickedInterval=StringUtil.parse(prop,"doubleClickedInterval",doubleClickedInterval);
            prepareTracksWaitTimeMillis=StringUtil.parse(prop,"prepareTracksWaitTimeMillis",prepareTracksWaitTimeMillis);

            resizedNewIntervalMillis=StringUtil.parse(prop,"resizedNewIntervalMillis",resizedNewIntervalMillis);
            resizingIntervalMillis=StringUtil.parse(prop,"resizingIntervalMillis",resizingIntervalMillis);
            prePlayPrefixTimeMillis=StringUtil.parse(prop,"prePlayPrefixTimeMillis", prePlayPrefixTimeMillis);

        }
    }



}
