package com._5xstar.ffcutter.util;

import java.io.IOException;

/**
 * 网络工具 
 * 庞海文 2024-1-21改
 **/

public class NetUtil 
{

  /**
   * 通过浏览器打开超链接
  **/
  public static void browse(final String url){
      try{
          java.net.URI uri = java.net.URI.create(url);
          // 获取当前系统桌面扩展
          java.awt.Desktop dp = java.awt.Desktop.getDesktop();
          // 判断系统桌面是否支持要执行的功能
          if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
              dp.browse(uri);// 获取系统默认浏览器打开链接
          }
       } catch (NullPointerException e) {
          System.out.println("Empty uri!");
          e.printStackTrace();
       } catch (IOException e) {
          System.out.println("Can't find browser!");
          e.printStackTrace();
       }
   }

}

