package com._5xstar.ffcutter.domains;

import com._5xstar.ffcutter.util.MediaUtil;

import java.io.File;
import java.io.Serializable;

/**
  *媒体剪切时使用的片段 
  *庞海文 2020-4-9
**/
public class MediaPart implements Serializable {

     //顺序号
    public int num=-1;

    //开始 单位百分秒
    public long begin=-1L;

   //结束 单位百分秒
   public long end=-1L;

   //媒体时长
    //public long length=1L;

   //源文件或目标文件，展示给用户使用
   public File file=null;

   //源文件，系统内部使用
   public File srcFile=null;

   //多个源文件使用
    public int index=-1;

  //目标文件，系统内部使用
  public File tarFile=null;

  //是否拷贝，系统内部使用
  public boolean isCopy=false;
 
   //显示列表项目
    @Override
   public String toString(){
       return MediaUtil.buildStrFromMediaPart(this);
   }

   //克隆
    @Override
    public MediaPart clone(){
        final MediaPart mp = new MediaPart();
        mp.num=this.num;
        mp.begin=this.begin;
        mp.end=this.end;
        //mp.length=this.length;
        mp.file=this.file;
        mp.srcFile=this.srcFile;
        mp.index=this.index;
        mp.tarFile=this.tarFile;
        mp.isCopy=this.isCopy;
        return mp;
    }

}
