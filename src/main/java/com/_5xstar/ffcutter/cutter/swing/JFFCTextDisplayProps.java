package com._5xstar.ffcutter.cutter.swing;

import com._5xstar.ffcutter.Configure;
import com._5xstar.ffcutter.constant.PubConst;
import com._5xstar.ffcutter.util.StringUtil;

import java.awt.*;
import java.util.Properties;

/**
 *JTextArea代码编辑文本框 ，实现修饰功能     
 *庞海文 2021-2-14
**/
public class JFFCTextDisplayProps implements  Configure
{


    public static Font txtDisplayFont=new Font("宋体", Font.PLAIN, 20);  //new Font("宋体", Font.ITALIC+Font.BOLD,20);
    public static String txtDisplayDefaultText="欢迎使用五行星FFCutter！"+PubConst.newLine;

    //配置
        //配置
        public void conf( final Properties prop){

            txtDisplayFont=StringUtil.parse(prop, "txtDisplayFont", txtDisplayFont);
            txtDisplayDefaultText=StringUtil.parse(prop, "txtDisplayDefaultText", txtDisplayDefaultText);

        }


}


