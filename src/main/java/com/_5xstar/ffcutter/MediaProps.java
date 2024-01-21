package com._5xstar.ffcutter;

import com._5xstar.ffcutter.util.StringUtil;

import java.util.Properties;

/**
 *媒体的Props
 *庞海文 2024-1-19
**/
public class MediaProps implements Configure {
        /////////////  ExtMediaAbstractImpl  abstract implements Media ///////////////////////
        public static String[] execMsg = {"客户端没设置，不可以执行。", "外置媒体执行失败！"};
        public static String colon = "：";

        //////////////////////    FilesFFMpeg        ///////////////////
        public static String[] setIOFileMsg = {"输入文件不存在或是文件夹", "输出文件已存。", "系统可能不认识的输入的媒体格式或时长为0！"};
        public static String mediaTitle = "媒体文件加工";
        public static String[] finishedMsg = {"成功转换到", "媒体文件格式转换失败！", "成功转换到目录："};
        public static String handleFallText = "媒体加工不成功！";
        public static String coverFileText = "文件覆盖";

        /////////////////////   UnorderedFilesFFMpeg      //////////////////
        public static String[] filesPrepareMsg = {"生成的媒体覆盖文件：", "生成的媒体可能覆盖文件夹：“", "”中的文件。",
                "媒体格式不相同，不支持合并！", "保存媒体文件", "选取或输入保存文件，注意后缀名是你需要的格式。",
                "媒体文件", "文件不存在或系统不认识的媒体或时长为0", "选择要加工的文件", "媒体文件"};
        /**
         *@prop 配置值
         **/
        public void conf(final Properties prop) {
            execMsg = StringUtil.parse(prop, "execMsg", execMsg);
            colon = StringUtil.parse(prop, "colon", colon);
            setIOFileMsg = StringUtil.parse(prop, "setIOFileMsg", setIOFileMsg);
            mediaTitle = StringUtil.parse(prop, "mediaTitle", mediaTitle);
            finishedMsg = StringUtil.parse(prop, "finishedMsg", finishedMsg);
            handleFallText = StringUtil.parse(prop, "handleFallText", handleFallText);
            coverFileText = StringUtil.parse(prop, "coverFileText", coverFileText);
        }

}
