package com._5xstar.ffcutter.player.swing;

import com._5xstar.ffcutter.Configure;
import com._5xstar.ffcutter.util.StringUtil;

import java.util.Properties;

public class PlayerVideoAdjustPanelProps implements Configure {
    public static String dialogTitle="视频调节器";
    public static String cancelCaption="不操作";
    public static String dialogDemo="视频亮度等调节";
    public static String enableCaption = "应用调节";
    public static String contrastText =  "对比度";
    public static String contrastTipText ="对比度调节";
    public static String brightnessText = "亮度";
    public static String brightnessTipText ="亮度调节";
    public static String hueText = "色度";
    public static String hueTipText = "色度调节";
    public static String saturationText = "饱和度";
    public static String saturationTipText = "饱和度调节";
    public static String gammaText = "透明度";
    public static String gammaTipText = "透明度调节";
    /**
     *@prop 配置值
     **/
    public void conf(final Properties prop) {
        dialogTitle = StringUtil.parse(prop, "dialogTitle", dialogTitle);
        cancelCaption = StringUtil.parse(prop, "cancelCaption", cancelCaption);
        dialogDemo = StringUtil.parse(prop, "dialogDemo", dialogDemo);
        enableCaption = StringUtil.parse(prop, "enableCaption", enableCaption);
        contrastText = StringUtil.parse(prop, "contrastText", contrastText);
        contrastTipText = StringUtil.parse(prop, "contrastTipText", contrastTipText);
        brightnessText = StringUtil.parse(prop, "brightnessText", brightnessText);
        brightnessTipText = StringUtil.parse(prop, "brightnessTipText", brightnessTipText);
        hueText = StringUtil.parse(prop, "hueText", hueText);
        hueTipText = StringUtil.parse(prop, "hueTipText", hueTipText);
        saturationText = StringUtil.parse(prop, "saturationText", saturationText);
        saturationTipText = StringUtil.parse(prop, "saturationTipText", saturationTipText);
        gammaText = StringUtil.parse(prop, "gammaText", gammaText);
        gammaTipText = StringUtil.parse(prop, "gammaTipText", gammaTipText);
    }
}
