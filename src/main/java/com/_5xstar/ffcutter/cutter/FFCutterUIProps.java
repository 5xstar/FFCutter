package com._5xstar.ffcutter.cutter;


import com._5xstar.ffcutter.Configure;
import com._5xstar.ffcutter.util.StringUtil;

import java.util.Properties;

public class FFCutterUIProps implements Configure {
    
    public static String btnFinishedCaption="执行";
    public static String btnFinishedCaption2="剪开";
    public static String btnResetCaption="重置";
    public static String btnAddCaption="添加";
    public static String btnUpdateCaption="更新";
    public static String btnUpCaption="前移一位";
    public static String btnDownCaption="后移一位";
    public static String btnInsertCaption="该处插入";
    public static String btnDelCaption="删除";
    public static String mediaPartBeginLabel="开始：";
    public static String mediaPartHourLabel="时";
    public static String mediaPartMinuteLabel="分";
    public static String mediaPartSecondLabel="秒";
    public static String mediaPartPersentSecondLabel="百分秒";
    public static String mediaPartEndLabel="结束：";
    public static String btnMediaPartYesCaption="增加";
    public static String mediaPartDemoLabel="注：全空表示文件开头或结尾。";
    public static String btnPreViewCaption="预播放";
    public static String btnEditCaption="更新";
    public static String btnPreViewCurrentCaption="单项预览";
    public static String btnPreViewStopCaption="不再预览";
    public static String btnMediaPartRefreshCaption="重新填写";
    public static String btnLinkMediaPartFactoryCaption = "打开播放器";
    /**
     *@prop 配置值
     **/
    public void conf(final Properties prop) {
        btnFinishedCaption = StringUtil.parse(prop, "btnFinishedCaption", btnFinishedCaption);
        btnFinishedCaption2 = StringUtil.parse(prop, "btnFinishedCaption2", btnFinishedCaption2);
        btnResetCaption = StringUtil.parse(prop, "btnResetCaption", btnResetCaption);
        btnAddCaption = StringUtil.parse(prop, "btnAddCaption", btnAddCaption);
        btnUpdateCaption = StringUtil.parse(prop, "btnUpdateCaption", btnUpdateCaption);
        btnUpCaption = StringUtil.parse(prop, "btnUpCaption", btnUpCaption);
        btnDownCaption = StringUtil.parse(prop, "btnDownCaption", btnDownCaption);
        btnInsertCaption = StringUtil.parse(prop, "btnInsertCaption", btnInsertCaption);
        btnDelCaption = StringUtil.parse(prop, "btnDelCaption", btnDelCaption);
        mediaPartBeginLabel = StringUtil.parse(prop, "mediaPartBeginLabel", mediaPartBeginLabel);
        mediaPartHourLabel = StringUtil.parse(prop, "mediaPartHourLabel", mediaPartHourLabel);
        mediaPartMinuteLabel = StringUtil.parse(prop, "mediaPartMinuteLabel", mediaPartMinuteLabel);
        mediaPartSecondLabel = StringUtil.parse(prop, "mediaPartSecondLabel", mediaPartSecondLabel);
        mediaPartPersentSecondLabel = StringUtil.parse(prop, "mediaPartPersentSecondLabel", mediaPartPersentSecondLabel);
        mediaPartEndLabel = StringUtil.parse(prop, "mediaPartEndLabel", mediaPartEndLabel);
        btnMediaPartYesCaption = StringUtil.parse(prop, "btnMediaPartYesCaption", btnMediaPartYesCaption);
        mediaPartDemoLabel = StringUtil.parse(prop, "mediaPartDemoLabel", mediaPartDemoLabel);
        btnPreViewCaption = StringUtil.parse(prop, "btnPreViewCaption", btnPreViewCaption);
        btnEditCaption = StringUtil.parse(prop, "btnEditCaption", btnEditCaption);
        btnPreViewCurrentCaption = StringUtil.parse(prop, "btnPreViewCurrentCaption", btnPreViewCurrentCaption);
        btnPreViewStopCaption = StringUtil.parse(prop, "btnPreViewStopCaption", btnPreViewStopCaption);
        btnMediaPartRefreshCaption = StringUtil.parse(prop, "btnMediaPartRefreshCaption", btnMediaPartRefreshCaption);
        btnLinkMediaPartFactoryCaption = StringUtil.parse(prop, "btnLinkMediaPartFactoryCaption", btnLinkMediaPartFactoryCaption);
    }
}
