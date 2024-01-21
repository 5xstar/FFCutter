package com._5xstar.ffcutter.cutter.swing;


import com._5xstar.ffcutter.Configure;
import com._5xstar.ffcutter.util.StringUtil;
import java.awt.*;
import java.util.Properties;

public interface JFFCutterStatus {

    class Const implements Configure {
        public static int mediaPartDlgWidth = 920;
        public static int mediaPartDlgHeight = 500;
        public static Color mediaPartDlgBg = Color.gray;
        public static String lookAndFeelName = null;
        /**
         * @confFileAbsolutePath 配置文件全路径
         * @prop 配置值
         **/
        public void conf(final Properties prop) {
            mediaPartDlgWidth = StringUtil.parse(prop, "mediaPartDlgWidth", mediaPartDlgWidth);
            mediaPartDlgHeight = StringUtil.parse(prop, "mediaPartDlgHeight", mediaPartDlgHeight);
            mediaPartDlgBg = StringUtil.parse(prop, "mediaPartDlgBg", mediaPartDlgBg);
            lookAndFeelName = StringUtil.parse(prop, "lookAndFeelName", lookAndFeelName);
        }
    }

}
