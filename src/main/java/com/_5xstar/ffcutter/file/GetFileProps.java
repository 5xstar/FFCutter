package com._5xstar.ffcutter.file;

import com._5xstar.ffcutter.Configure;
import com._5xstar.ffcutter.util.StringUtil;

import java.util.Properties;

/**
 * GetFile语言包
 * 庞海文 2024-1-20
 */
public class GetFileProps  implements Configure {
    public static String[] confMsg = {"系统文件获取对话框Python脚本：" ,"成功！","失败！"};
    public static String openSystemFileMsg = "Python getFile未初始化！";
    public static String[] getTitleMsg = {"打开一个文件", "打开多个文件", "打开一个文件夹", "打开一个文件（覆盖）或输入新文件保存"};
    public static String allFilesText = "所有文件";
    /**
     *@prop 配置值
     **/
    public void conf(final Properties prop) {
        confMsg = StringUtil.parse(prop, "confMsg", confMsg);
        openSystemFileMsg = StringUtil.parse(prop, "openSystemFileMsg", openSystemFileMsg);
        getTitleMsg = StringUtil.parse(prop, "getTitleMsg", getTitleMsg);
        allFilesText = StringUtil.parse(prop, "allFilesText", allFilesText);
    }

}
