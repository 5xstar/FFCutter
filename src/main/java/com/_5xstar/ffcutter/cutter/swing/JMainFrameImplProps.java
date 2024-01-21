package com._5xstar.ffcutter.cutter.swing;


import com._5xstar.ffcutter.Configure;
import com._5xstar.ffcutter.util.StringUtil;

import java.util.Properties;

public class JMainFrameImplProps implements Configure {
    //窗口
    public static String title="FFCutter图片剪辑器";
    public static String contact="联系我们";
    public static String[] contactItems = {"主页：https://5xstar.com",
            "微信订阅号：学思营  ID：xuesying",
            "微信电话：13729135043","qq邮箱：543296087@qq.com",
            "深圳五行星软件有限公司制作"};
    public static String openText="打开FFCutter";
    public static String popupMenuText = "快捷菜单";
    public static String btnChangeBGCaption_day="日间模式";
    public static String btnChangeBGCaption_night="夜间模式";
    public static String loaoutText = "主菜单";
    public static String[] Nimbus = {"冲突", "Nimbus界面不支持夜间模式！"};
    public static String centerLabelText = "欢迎使用FFCutter!";
    public static String displayPopMenuTitle = "显示栏快捷菜单";


    /**
     *@prop 配置值
     **/
    public void conf(final Properties prop) {
        title = StringUtil.parse(prop, "title", title);
        contact = StringUtil.parse(prop, "contact", contact);
        contactItems = StringUtil.parse(prop, "contactItems", contactItems);
        openText = StringUtil.parse(prop, "openText", openText);
        popupMenuText = StringUtil.parse(prop, "popupMenuText", popupMenuText);
        btnChangeBGCaption_day = StringUtil.parse(prop, "btnChangeBGCaption_day", btnChangeBGCaption_day);
        btnChangeBGCaption_night = StringUtil.parse(prop, "btnChangeBGCaption_night", btnChangeBGCaption_night);
        loaoutText = StringUtil.parse(prop, "loaoutText", loaoutText);
        Nimbus = StringUtil.parse(prop, "Nimbus", Nimbus);
        centerLabelText = StringUtil.parse(prop, "centerLabelText", centerLabelText);
        displayPopMenuTitle = StringUtil.parse(prop, "displayPopMenuTitle", displayPopMenuTitle);
    }
}
