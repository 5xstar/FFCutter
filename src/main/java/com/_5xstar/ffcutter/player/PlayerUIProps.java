package com._5xstar.ffcutter.player;

import com._5xstar.ffcutter.Configure;
import com._5xstar.ffcutter.util.StringUtil;

import java.util.Properties;

/**
 * 播放器自身用props
 * 庞海文 2024-1-19
 */
public class PlayerUIProps implements Configure {
        public static String title = "FFCutter播放器";
        //控制按钮
        public static String btnCutBeginCaption = "媒体开始";
        public static String btnCutEndCaption = "媒体结束";
        public static String btnStopCaption = "停止";
        public static String btnPlayCaption = "播放";
        public static String btnPauseCaption = "暂停";
        public static String btnFullScreenCaption = "全屏";
        public static String btnOpenFileCaption = "打开";
        public static String btnCloseCaption = "离开";
        public static String btnMuteCaption = "静音";
        public static String btnSendCaption = "发送";
        public static String btnKillCaption = "终止";
        public static String btnRepeatCaption = "重复";
        public static String btnReloadCaption = "重载";
        public static String btnEqualizerCaption = "均衡";
        public static String btnVideoAdjustCaption = "视觉";
        public static String lblVolumeText = "音量：";   //音量标签
        public static String lblTrackCountText = "声道：";  //声道标签
        public static String lblVolumeDefaultText = "默认";
        public static String sliderTipText = "音量调节";
        public static String msgDialogTitle = "网络串流";
        public static String loadSuccessMsg = "加载成功！本消息等待";
        public static String closeLateText = "秒后关闭。";
        public static String loadFalseText = "加载失败！本消息等待";
        public static String waitLoadMsg = "等待串流";
        public static String delayText = "秒。";
        public static String[] btnMuteCaptionExt = {"（开）", "（关）"};
        public static String circlePlayText = "（循环播放）";
        public static String fileSelectTitle = "选择媒体";
        public static String fileSelectDemo = "选择要播放的媒体文件";
        public static String mediaFilterName = "媒体文件";
        public static String openMediaTitle = "打开媒体";
        public static String unsupportMediaText = "不支持媒体：";
        public static String mediaPlayText = "媒体播放";
        public static String unsupportMediaFormatText = "不支持的媒体格式";
        public static String loadMediaText = "加载媒体:";
        public static String loadMediaSuccessText = "媒体加载成功:";
        public static String loadMediaFalseText = "媒体加载失败:";
        public static String awtCanvasDemo = " AWTCanvas 播放器启动\n";
        public static String awtPanelDemo = " AWTPanel 播放器启动\n";
        public static String unknowState = "未知";
        /**
         *@prop 配置值
         **/
        public void conf(final Properties prop){
            title= StringUtil.parse(prop,"title",title);
            btnCutBeginCaption= StringUtil.parse(prop,"btnCutBeginCaption",btnCutBeginCaption);
            btnCutEndCaption=StringUtil.parse(prop,"btnCutEndCaption",btnCutEndCaption);
            btnStopCaption=StringUtil.parse(prop,"btnStopCaption",btnStopCaption);
            btnPlayCaption=StringUtil.parse(prop,"btnPlayCaption",btnPlayCaption);
            btnPauseCaption=StringUtil.parse(prop,"btnPauseCaption",btnPauseCaption);
            btnFullScreenCaption=StringUtil.parse(prop,"btnFullScreenCaption",btnFullScreenCaption);
            btnOpenFileCaption=StringUtil.parse(prop,"btnOpenFileCaption",btnOpenFileCaption);
            btnCloseCaption=StringUtil.parse(prop,"btnCloseCaption",btnCloseCaption);
            btnMuteCaption=StringUtil.parse(prop,"btnMuteCaption",btnMuteCaption);
            btnSendCaption=StringUtil.parse(prop,"btnSendCaption",btnSendCaption);
            btnKillCaption=StringUtil.parse(prop,"btnKillCaption",btnKillCaption);
            btnRepeatCaption=StringUtil.parse(prop,"btnRepeatCaption",btnRepeatCaption);
            btnReloadCaption=StringUtil.parse(prop,"btnReloadCaption", btnReloadCaption);
            btnEqualizerCaption=StringUtil.parse(prop,"btnEqualizerCaption",btnEqualizerCaption);
            btnVideoAdjustCaption=StringUtil.parse(prop,"btnVideoAdjustCaption",btnVideoAdjustCaption);
            lblVolumeText=StringUtil.parse(prop,"lblVolumeText",lblVolumeText);
            lblTrackCountText=StringUtil.parse(prop,"lblTrackCountText",lblTrackCountText);
            lblVolumeDefaultText=StringUtil.parse(prop,"lblVolumeDefaultText", lblVolumeDefaultText);
            sliderTipText=StringUtil.parse(prop,"sliderTipText",sliderTipText);
            msgDialogTitle=StringUtil.parse(prop,"msgDialogTitle",msgDialogTitle);
            loadSuccessMsg=StringUtil.parse(prop,"loadSuccessMsg",loadSuccessMsg);
            closeLateText=StringUtil.parse(prop,"closeLateText",closeLateText);
            loadFalseText=StringUtil.parse(prop,"loadFalseText", loadFalseText);
            waitLoadMsg=StringUtil.parse(prop,"waitLoadMsg", waitLoadMsg);
            delayText=StringUtil.parse(prop,"delayText",delayText);
            btnMuteCaptionExt=StringUtil.parse(prop,"btnMuteCaptionExt",btnMuteCaptionExt);
            circlePlayText=StringUtil.parse(prop,"circlePlayText",circlePlayText);
            fileSelectTitle=StringUtil.parse(prop,"fileSelectTitle",fileSelectTitle);
            fileSelectDemo=StringUtil.parse(prop,"fileSelectDemo", fileSelectDemo);
            mediaFilterName=StringUtil.parse(prop,"mediaFilterName", mediaFilterName);
            openMediaTitle=StringUtil.parse(prop,"openMediaTitle", openMediaTitle);
            unsupportMediaText=StringUtil.parse(prop,"unsupportMediaText",unsupportMediaText);
            mediaPlayText=StringUtil.parse(prop,"mediaPlayText",mediaPlayText);
            unsupportMediaFormatText=StringUtil.parse(prop,"unsupportMediaFormatText",unsupportMediaFormatText);
            loadMediaText=StringUtil.parse(prop,"loadMediaText",loadMediaText);
            loadMediaSuccessText=StringUtil.parse(prop,"loadMediaSuccessText", loadMediaSuccessText);
            loadMediaFalseText=StringUtil.parse(prop,"loadMediaFalseText", loadMediaFalseText);
            awtCanvasDemo=StringUtil.parse(prop,"awtCanvasDemo", awtCanvasDemo);
            awtPanelDemo=StringUtil.parse(prop,"awtPanelDemo",awtPanelDemo);
            unknowState=StringUtil.parse(prop,"unknowState",unknowState);
        }

}
