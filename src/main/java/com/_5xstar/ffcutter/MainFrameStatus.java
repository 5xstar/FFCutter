package com._5xstar.ffcutter;

import com._5xstar.ffcutter.util.StringUtil;

import java.util.Properties;

/**
 * 主窗口props
 * 庞海文 2024-1-20
 */
public interface MainFrameStatus{
    class Const  implements Configure {
        public static boolean sysFileOn=true;  //是否用系统文件选择器
        public static int confirmDlgWidth = 300;
        public static int confirmDlgHeight = 200;
        public static int[] confirmDlgBg_rgb = {128, 128, 128};
        public static int listDisplayDlgWidth = 600;
        public static int listDisplayDlgHeight = 300;
        public static int[] listDisplayDlgBg_rgb = {128, 128, 128};

        public static int atFilterSendDlgWidth = 300;
        public static int atFilterSendDlgHeight = 400;
        public static int[] atFilterSendDlgBg_rgb = {128, 128, 128};
        //文本文件类型
        public static String[] textTypes = new String[]{"py", "java", "c", "h", "cpp", "properties", "txt", "bat", "sh", "cmd", "xml", "json", "htm*"};
        //过滤二进制文件
        public static String[] binFileFilters=new String[]{"exe","class","obj","bin"};
        public static int fileChooserDlgWidth=600;
        public static int fileChooserDlgHeight=400;
        public static int[] fileChooserDlgBg_rgb= {128,128,128};
        //保存时是否显示新文件
        public static boolean saveDemoNewFile=true;

        /**
         * @prop 配置值
         **/
        public void conf(final Properties prop) {
            sysFileOn = StringUtil.parse(prop, "sysFileOn", sysFileOn);
            confirmDlgWidth = StringUtil.parse(prop, "confirmDlgWidth", confirmDlgWidth);
            confirmDlgHeight = StringUtil.parse(prop, "confirmDlgHeight", confirmDlgHeight);
            confirmDlgBg_rgb = StringUtil.parse(prop, "confirmDlgBg_rgb", confirmDlgBg_rgb);
            listDisplayDlgWidth = StringUtil.parse(prop, "listDisplayDlgWidth", listDisplayDlgWidth);
            listDisplayDlgHeight = StringUtil.parse(prop, "listDisplayDlgHeight", listDisplayDlgHeight);
            listDisplayDlgBg_rgb = StringUtil.parse(prop, "listDisplayDlgBg_rgb", listDisplayDlgBg_rgb);
            atFilterSendDlgWidth = StringUtil.parse(prop, "atFilterSendDlgWidth", atFilterSendDlgWidth);
            atFilterSendDlgHeight = StringUtil.parse(prop, "atFilterSendDlgHeight", atFilterSendDlgHeight);
            atFilterSendDlgBg_rgb = StringUtil.parse(prop, "atFilterSendDlgBg_rgb", atFilterSendDlgBg_rgb);
            textTypes = StringUtil.parse(prop, "textTypes", textTypes);
            binFileFilters = StringUtil.parse(prop, "binFileFilters", binFileFilters);
            fileChooserDlgWidth = StringUtil.parse(prop, "fileChooserDlgWidth", fileChooserDlgWidth);
            fileChooserDlgHeight = StringUtil.parse(prop, "fileChooserDlgHeight", fileChooserDlgHeight);
            fileChooserDlgBg_rgb = StringUtil.parse(prop, "fileChooserDlgBg_rgb", fileChooserDlgBg_rgb);
            saveDemoNewFile = StringUtil.parse(prop, "saveDemoNewFile", saveDemoNewFile);
        }
    }
}
