package com._5xstar.ffcutter.cutter.swing;


import com._5xstar.ffcutter.Configure;
import com._5xstar.ffcutter.util.StringUtil;

import java.awt.*;
import java.util.Properties;

public interface JMainFrameImplStatus {

    class Const implements Configure {
        public static String lookAndFeelName = null;
        //窗口
        public static String icon="logo/jtrIcon.png";
        public static Color background_day=Color.lightGray;
        public static Color background_night=Color.gray;
        public static int width=1366;
        public static int height=768;
        public static boolean defaultDayType=true;  //默认模式是白天，false是夜晚。


        /**
         * @confFileAbsolutePath 配置文件全路径
         * @prop 配置值
         **/
        public void conf(final Properties prop) {
            lookAndFeelName = StringUtil.parse(prop, "lookAndFeelName", lookAndFeelName);
            icon = StringUtil.parse(prop, "icon", icon);
            background_day = StringUtil.parse(prop, "background_day", background_day);
            background_night = StringUtil.parse(prop, "background_night", background_night);
            width = StringUtil.parse(prop, "width", width);
            height = StringUtil.parse(prop, "height", height);
            defaultDayType = StringUtil.parse(prop, "defaultDayType", defaultDayType);
        }
    }

}
