package com._5xstar.ffcutter.constant;

import com._5xstar.ffcutter.Configure;
import com._5xstar.ffcutter.util.StringUtil;

import java.util.Properties;

/**
 * 媒体常数  静态引用会拷贝，配置无效
 * 庞海文 2020-2-16
 **/
public class MediaConstProps implements Configure
{

     public static String mediaFilterName="媒体文件（.avi,.gif,.flv,.mp3,.mp4等）";


    /**
     *@prop 配置值
    **/
    public void conf(final Properties prop){

         mediaFilterName=StringUtil.parse(prop,"mediaFilterName",mediaFilterName);
         System.out.println("mediaFilterName="+mediaFilterName);  //测试

    }



 
}

