package com._5xstar.ffcutter.domains;

import java.io.File;

/**
 *文件信息
 *庞海文 2020-2-29
**/
public class FileKeeper
{

           //保持文件
           public File file=null;
           //全路径文件名称
           public String fullName=null;
           //目录
           public String dir=null;
           //无扩展名文件名称，无目录
           public String shortName=null;
           //不含点的扩展名
           public String extName=null;

           //是否媒体
           public boolean isPlay=false;  
           //大小限制0不限制
           public long maxLength=0;  
           //是否为媒体
           public boolean isMedia = false;
           //文件长度
           public long fileSize=0L;
           //保持的多个文件，媒体加工用
           public File[] files=null;

}
