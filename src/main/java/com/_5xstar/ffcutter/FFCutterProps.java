package com._5xstar.ffcutter;

import com._5xstar.ffcutter.util.StringUtil;

import java.util.Properties;

/**
 * 剪切器Props
 * 庞海文 2024-1-19
 */
public class FFCutterProps implements Configure {
        public static String cutTitle = "图片剪切";

        ////////////////    FFCutterBase        ///////////////////
        public static String addCaption = "添加";
        public static String insertCaption = "插入";
        public static String updateCaption = "更新";
        public static String dialogTitle = "媒体剪切";
        public static String cutMsg = "确定媒体片段编辑完成，执行媒体剪切吗？";
        public static String onlyCutMsg = "确定数据编辑完成，执行媒体剪切成多个文件吗？";
        public static String[] actionMsg = {"未能生成临时文件，媒体加工失败！", "由于文件操作发生错误而失败！"};
        public static String resetActionMsg = "是否删除已有的所有媒体片段？（注：单个片段的删除或移位可点那个片段。）";
        public static String partTitle = "媒体片段";
        public static String[] yesMsg = {"媒体片段结束时间不能比开始时间小，或者已超出了媒体时长：", "媒体片段时间有误："};
        public static String[] updatePartMsg = {"片段对象已被删除，该次操作被忽略！", "从媒体文件生成媒体失败，该次操作被忽略！"};

        //////////////////////    FFCutter           //////////////////////////
        //默认标题
        public static String defaultTitle = "媒体剪切";
        //默认描述
        public static String defaultDesc = "用FFMpeg，从文件到文件的各种媒体文件的剪切、合并与转码。";
        public static String[] getTitleMsg = {"单个", "多个", "    文件："};
        /**
         *@prop 配置值
         **/
        public void conf(final Properties prop) {
            cutTitle = StringUtil.parse(prop, "cutTitle", cutTitle);
            addCaption = StringUtil.parse(prop, "addCaption", addCaption);
            insertCaption = StringUtil.parse(prop, "insertCaption", insertCaption);
            updateCaption = StringUtil.parse(prop, "updateCaption", updateCaption);
            dialogTitle = StringUtil.parse(prop, "dialogTitle", dialogTitle);
            cutMsg = StringUtil.parse(prop, "cutMsg", cutMsg);
            onlyCutMsg = StringUtil.parse(prop, "onlyCutMsg", onlyCutMsg);
            actionMsg = StringUtil.parse(prop, "actionMsg", actionMsg);
            resetActionMsg = StringUtil.parse(prop, "resetActionMsg", resetActionMsg);
            partTitle = StringUtil.parse(prop, "partTitle", partTitle);
            yesMsg = StringUtil.parse(prop, "yesMsg", yesMsg);
            updatePartMsg = StringUtil.parse(prop, "updatePartMsg", updatePartMsg);
            defaultTitle = StringUtil.parse(prop, "defaultTitle", defaultTitle);
            defaultDesc = StringUtil.parse(prop, "defaultDesc", defaultDesc);
            getTitleMsg = StringUtil.parse(prop, "getTitleMsg", getTitleMsg);
        }

}
