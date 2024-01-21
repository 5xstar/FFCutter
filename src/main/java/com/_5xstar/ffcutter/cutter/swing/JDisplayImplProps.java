package com._5xstar.ffcutter.cutter.swing;

import com._5xstar.ffcutter.Configure;
import com._5xstar.ffcutter.util.StringUtil;
import java.util.Properties;

public class JDisplayImplProps implements  Configure {

    //清空消息栏
    public static String btnCleanDisplayCaption="清空消息";

    //配置
    public void conf( final Properties prop){
        //清空消息栏
        btnCleanDisplayCaption=StringUtil.parse(prop, "btnCleanDisplayCaption", btnCleanDisplayCaption);
    }

}
