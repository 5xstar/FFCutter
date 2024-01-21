package com._5xstar.ffcutter;


import com._5xstar.ffcutter.util.StringUtil;

import java.util.Properties;

/**
*播放器实现接口
*庞海文 2024-1-17
**/
public class PlayerProps implements Configure {
        public static String unspportMedia = "不支持媒体";
        public static String loadMedia = "装载媒体";  //测试
        public static String loadMediaSuccess = "装载媒体成功";
        public static String loadMediaFail = "装载媒体失败";
        public static String unsupportOpenFileTitle = "不支持";
        public static String unsupportOpenFile = "不支持打开文件";
        /**
         *@prop 配置值
         **/
        public void conf(final Properties prop) {
            unspportMedia = StringUtil.parse(prop, "unspportMedia", unspportMedia);
            loadMedia = StringUtil.parse(prop, "loadMedia", loadMedia);
            loadMediaSuccess = StringUtil.parse(prop, "loadMediaSuccess", loadMediaSuccess);
            loadMediaFail = StringUtil.parse(prop, "loadMediaFail", loadMediaFail);
            unsupportOpenFileTitle = StringUtil.parse(prop, "unsupportOpenFileTitle", unsupportOpenFileTitle);
            unsupportOpenFile = StringUtil.parse(prop, "unsupportOpenFile", unsupportOpenFile);
        }

    
}
