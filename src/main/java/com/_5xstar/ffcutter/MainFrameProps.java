package com._5xstar.ffcutter;

import com._5xstar.ffcutter.util.StringUtil;

import java.util.Properties;

/**
 * 主窗口props
 * 庞海文 2024-1-20
 */
public class MainFrameProps implements Configure{
    //确定询问窗默认标题
    public static String confirmDlgTitle="确定";
    public static String confirmYesCaption="确定";
    public static String confirmNoCaption="否定";
    public static String confirmCancelCaption="取消";
    public static String listDisplayDlgTitle="列表选择";
    public static String atFilterSendYesCaption="    确定    ";
    public static String[] msgTitle = {"Debug消息","用户消息"};
    public static String fileChooserSelectDlgTitle="文件选择";
    public static String fileChooserSaveDlgTitle="文件保存";
    public static String fileTyeName_text="文本文件";
    public static String fileTyeName_media="媒体文件";
    public static String fileTyeName_image="图片文件";
    public static String fileTyeName_music="音乐文件";
    public static String[] fileUploadLimit={"文件上传", "文件大小已超过", "，请重新选择！"};
    public static String undefinedText = "未定义";
    //timout msg
    public static String[] timeOutMsg = {"关闭",  "不能关闭，请选择功能按钮！"};
    public static String withoutBinFileFilterDescription="排除可执行文档";
    public  static String noFileFilterDescription="全部文件(*)";
    public static String fileChooserCancelCaption="    取消    ";
    public static String fileChooserSelectYesCaption="    确定    ";
    public static String fileChooserSaveYesCaption="    保存    ";
    public static String[] fileSelectMsg = {"文件上传", "文件大小已超过", "，请重新选择！"};

        /**
         *@prop 配置值
         **/
        public void conf( final Properties prop){
            //确定询问窗默认标题
            confirmDlgTitle= StringUtil.parse(prop, "confirmDlgTitle", confirmDlgTitle);
            confirmYesCaption=StringUtil.parse(prop, "confirmYesCaption", confirmYesCaption);
            confirmNoCaption=StringUtil.parse(prop, "confirmNoCaption", confirmNoCaption);
            confirmCancelCaption=StringUtil.parse(prop, "confirmCancelCaption", confirmCancelCaption);
            listDisplayDlgTitle=StringUtil.parse(prop, "listDisplayDlgTitle", listDisplayDlgTitle);
            atFilterSendYesCaption=StringUtil.parse(prop, "atFilterSendYesCaption", atFilterSendYesCaption);
            msgTitle=StringUtil.parse(prop, "msgTitle", msgTitle);

            fileChooserSelectDlgTitle=StringUtil.parse(prop, "fileChooserSelectDlgTitle", fileChooserSelectDlgTitle);
            fileChooserSaveDlgTitle=StringUtil.parse(prop, "fileChooserSaveDlgTitle", fileChooserSaveDlgTitle);
            fileTyeName_text=StringUtil.parse(prop, "fileTyeName_text", fileTyeName_text);
            fileTyeName_media=StringUtil.parse(prop, "fileTyeName_media", fileTyeName_media);
            fileTyeName_image=StringUtil.parse(prop, "fileTyeName_image", fileTyeName_image);
            fileTyeName_music=StringUtil.parse(prop, "fileTyeName_music", fileTyeName_music);
            fileUploadLimit=StringUtil.parse(prop, "fileUploadLimit", fileUploadLimit);
            undefinedText=StringUtil.parse(prop, "undefinedText", undefinedText);
            timeOutMsg = StringUtil.parse(prop, "timeOutMsg", timeOutMsg);
            withoutBinFileFilterDescription=StringUtil.parse(prop, "withoutBinFileFilterDescription", withoutBinFileFilterDescription);
            noFileFilterDescription = StringUtil.parse(prop, "noFileFilterDescription", noFileFilterDescription);
            fileChooserCancelCaption = StringUtil.parse(prop, "fileChooserCancelCaption", fileChooserCancelCaption);
            fileChooserSelectYesCaption=StringUtil.parse(prop, "fileChooserSelectYesCaption", fileChooserSelectYesCaption);
            fileChooserSaveYesCaption = StringUtil.parse(prop, "fileChooserSaveYesCaption", fileChooserSaveYesCaption);


        }

}
