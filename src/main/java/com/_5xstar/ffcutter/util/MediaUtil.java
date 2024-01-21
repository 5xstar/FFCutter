package com._5xstar.ffcutter.util;

import com._5xstar.ffcutter.Configure;
import com._5xstar.ffcutter.FFCTextField;
import com._5xstar.ffcutter.constant.MediaConstProps;
import com._5xstar.ffcutter.domains.MediaInfo;
import com._5xstar.ffcutter.domains.MediaPart;
import com._5xstar.ffcutter.cmd.CmdExecuter;
import com._5xstar.ffcutter.StringHandler;

import com._5xstar.ffcutter.constant.PubConst;
import com._5xstar.ffcutter.constant.MediaConst;
import com._5xstar.ffcutter.file.FFCFileFilter;
import com._5xstar.ffcutter.file.MyFileService;
import com._5xstar.ffcutter.Display;
import com._5xstar.ffcutter.MainFrame;
import com._5xstar.ffcutter.domains.FileMediaInfo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
  *媒体公用工具
  * 庞海文 2020-3-22
**/
final public class MediaUtil implements Configure
{

    private static String[] prePlayMsg={"正在播放", "媒体"};
    private static String[] getNumStrMsg={"【未知编号】", "【", "】"};
    private static String[] getMediaPartTimeMsg={"时数据输入有误，应该是0到23之间的整数。",
                                                                            "分数据输入有误，应该是0到59之间的整数。",
                                                                            "秒数据输入有误，应该是0到59之间的整数。",
                                                                            "百分秒数据输入有误，应该是0到99之间的整数。"};
    private static String[] buildStrFromMediaPartMsg={"【",  "】", "开始  ",  "  到  ", "  结束", "  媒体文件：（",  "）"};
    private static String[] getMediaRemoteMsg={"开始发送媒体……", "发送媒体出现异常！"};
    private static String[] buildFileMediaInfoMsg={"媒体时长(时:分:秒.百分秒)：", "    文件名："};
    private static String[] mediaPlayMsg = {"播放媒体：", "媒体播放失败！"};
    private static String srcFileNoExistsText = "原文件不存在或是文件夹。";

    //获取资源文件初始化
    public void conf(Properties prop) {
        prePlayMsg = StringUtil.parse(prop, "prePlayMsg", prePlayMsg);
        getNumStrMsg = StringUtil.parse(prop, "getNumStrMsg", getNumStrMsg);
        getMediaPartTimeMsg = StringUtil.parse(prop, "getMediaPartTimeMsg", getMediaPartTimeMsg);
        buildStrFromMediaPartMsg = StringUtil.parse(prop, "buildStrFromMediaPartMsg", buildStrFromMediaPartMsg);
        getMediaRemoteMsg = StringUtil.parse(prop, "getMediaRemoteMsg", getMediaRemoteMsg);
        buildFileMediaInfoMsg = StringUtil.parse(prop, "buildFileMediaInfoMsg", buildFileMediaInfoMsg);
        mediaPlayMsg = StringUtil.parse(prop, "mediaPlayMsg", mediaPlayMsg);
        srcFileNoExistsText = StringUtil.parse(prop, "srcFileNoExistsText", srcFileNoExistsText);
    }

    /*
     *获取媒体时长
     *@info 媒体信息包
     *@return 媒体时长
   **/
    public static long getDuration(final MediaInfo info){
       String d=null;
       if(info==null || (d=info.duration.duration)==null || "".equals(d=d.trim()))return -1L;
       String[] sts=StringUtil.strTokenizer(d, ":");
       if(sts==null || sts.length!=3)return -1L;
       String[] sps=StringUtil.strTokenizer(sts[2], ".");
       String h=sts[0],m=sts[1],s=sps[0];
       String ps=null;
       if(sps.length>1)ps=sps[1];
       try{
          return getMediaPartTime(h, m, s, ps);
       }catch(Exception e){
          e.printStackTrace();
          return -1L;
       }
    }

    final static private String regexDuration = "Duration\\s*: (.*?),\\s*start\\s*: (.*?),\\s*bitrate\\s*:\\s*(\\d*)\\s*kb\\/s";
    final private static Matcher matcherDuration=Pattern.compile(regexDuration).matcher("");
    final static private String regexVideo = "Video\\s*:(.*?),(.*?),(.*?)[,\\s]";
    final private static Matcher matcherVideo=Pattern.compile(regexVideo).matcher("");
    final static private String regexAudio = "Audio\\s*:\\s*(\\w*)\\s*,\\s*(\\d*)\\s*Hz";
    final private static Matcher matcherAudio=Pattern.compile(regexAudio).matcher("");
   /**
     *获取媒体信息 MainFrame原始使用，因为不涉及形参化类
     *@mediaFile 媒体文件
     *@return 媒体信息包
   **/
   public static synchronized   MediaInfo getMediaInfo(final MainFrame client, final File mediaFile){
       String strInfo=getMediaStrInfo(client, mediaFile);
       if(strInfo==null)return null;
       //client.getDisplayService().display(strInfo);  //测试
       MediaInfo info=new MediaInfo();
       boolean hsf=false;
       matcherDuration.reset(strInfo);
       if(matcherDuration.find()){
            hsf=true;
            info.duration.duration= matcherDuration.group(1); 
            info.duration.start= matcherDuration.group(2); 
            info.duration.bitrate= matcherDuration.group(3); 
       }
       matcherVideo.reset(strInfo);
       if(matcherVideo.find()){
            hsf=true;
            info.video.encodeFormat= matcherVideo.group(1); 
            info.video.videoFormat= matcherVideo.group(2); 
            info.video.size= matcherVideo.group(3); 
       }
       matcherAudio.reset(strInfo);
       if(matcherAudio.find()){
            hsf=true;
            info.audio.audioFormat= matcherAudio.group(1); 
            info.audio.sampling= matcherAudio.group(2); 
       }
       //client.getDisplayService().display(info.toString());  //测试
       if(hsf)return info;
       return null;
   }
   public static  String getMediaStrInfo(final MainFrame client, final File mediaFile){
      ExecStringData handler=new ExecStringData(client);
      ArrayList<String> com=new ArrayList<String>(3);
      com.add(MediaConst.ffmpeg);
      com.add("-i");
      com.add(mediaFile.getAbsolutePath());
      try{
         CmdExecuter.exec(com,handler);
         return handler.toString();
      }catch(Exception e){
          e.printStackTrace();
          return null;
      }
   }

    //命令行返回消息获取器
  private  static class ExecStringData implements StringHandler{
          final private MainFrame client;
          private ExecStringData(final MainFrame client){
             this.client=client;
          }
          final StringBuilder sb=new StringBuilder(1000);
          public void handle(final String msg){
              sb.append(msg);sb.append(PubConst.newLine);
              PubConst.es.submit(new Runnable(){
                 public void run(){
                   client.display(msg+PubConst.newLine, Display.DEBUG_TEXT);
                 }
              });
         }
         public String toString(){
            return sb.toString();
         }
   }

   /**
    *把结束拷贝进开始，结束清空。
    *@begins 时分秒百分秒顺序的field数组 开始
    *@ends 时分秒百分秒顺序的field数组  结束
   **/
   public static void endsToBegins(final FFCTextField[] begins, final FFCTextField[] ends){
        for(int i=0;i<begins.length;i++){
           begins[i].setText(ends[i].getText());ends[i].setText("");
        }
   }

   /**
    *装置媒体片段
    *@begins 时分秒百分秒顺序的field数组 开始
    *@ends 时分秒百分秒顺序的field数组  结束
    *@mp 媒体片段
   **/
   public static void load(final FFCTextField[] begins, final FFCTextField[] ends, final MediaPart mp){
        if(mp==null){
           load(begins[0], begins[1], begins[2], begins[3], -1L);
           load(ends[0], ends[1], ends[2], ends[3], -1L);
        }else{
           load(begins[0], begins[1], begins[2], begins[3], mp.begin);
           load(ends[0], ends[1], ends[2], ends[3], mp.end);
        }
   }
   private static void load(final FFCTextField fldH, final FFCTextField fldM, final FFCTextField fldS, final FFCTextField fldPS, long ps){
          if(ps<=0){
             fldH.setText("");   
             fldM.setText("");    
             fldS.setText("");    
             fldPS.setText("");  
          }else{
             long s=ps/100;
             ps=ps-100*s;
             if(ps==0L){
                fldPS.setText("");  
             }else{
               fldPS.setText(""+ps);  
             }
             if(s==0L){
                fldH.setText("");   
                fldM.setText("");    
                fldS.setText("");  
             }else{
                long m=s/60;
                s=s-60*m;
                if(s==0L){
                   fldS.setText(""); 
                }else{
                   fldS.setText(""+s); 
                }
                if(m==0L){
                   fldH.setText("");   
                   fldM.setText("");                   
                }else{
                   long h=m/60;
                   m=m-60*h;
                   if(m==0L){
                      fldM.setText(""); 
                   }else{
                      fldM.setText(""+m); 
                   }
                   if(h==0L){
                      fldH.setText("");   
                   }else{
                      fldH.setText(""+h);   
                   }
                }
             }
         }
   }



     /**
        *执行
        *@client 客户端
        *@com ffmpeg一行命令
     **/
    public static  void exec(final MainFrame client, final List<String> com)throws Exception
    {
            CmdExecuter.exec(com,new MyHandler(client));
    }

    //命令行返回消息处理器
    private  static class MyHandler implements StringHandler{
          final private MainFrame client;
          private MyHandler(final MainFrame client){
             this.client=client;
          }
          public void handle(final String msg){
            client.display(msg+PubConst.newLine);  
          }
   }


     /**
        *预播放  ffmpeg -i input.mkv -filter_complex "[0:v]setpts=0.25*PTS[v];[0:a]atempo=4.0[a]" -map "[v]" -map "[a]" output.mkv 四倍速度
        *@client 客户端  ffmpeg -i input.mkv -filter_complex "[0:v]setpts=0.5*PTS[v];[0:a]atempo=2.0[a]" -map "[v]" -map "[a]" output.mkv
        *@mp 媒体片段   ffplay 好像不支持
        *@num 片段编号
        *@srcFile 媒体文件
     **/
    public static  void prePlay(final MainFrame client, final MediaPart mp, final int num, final File srcFile)throws Exception
   {

          ArrayList<String> nwc=new ArrayList<String>(20);
          nwc.add(MediaConst.ffplay);
          nwc.add("-window_title");
          nwc.add(prePlayMsg[0]+getNumStr(num)+prePlayMsg[1]);
          nwc.add("-autoexit");                                              //播完自动关闭
          nwc.add("-x");                                                         //600x400
          nwc.add("600");
          nwc.add("-y"); 
          nwc.add("400"); 
          //String sp=getSpeed(speed);
         // if(sp!=null){
         //    nwc.add("-filter_complex");
         //    nwc.add(sp);
         //    nwc.add("-map");
        //     nwc.add("\"[v]\"");
        //     nwc.add("-map");
        //     nwc.add("\"[a]\"");
        //  }
          if(mp.begin>0L){
            nwc.add("-ss");
            nwc.add(buildStrFromPersentSeconds(mp.begin));
          }
          if(mp.end>0L){
            nwc.add("-t");
            nwc.add(buildStrFromPersentSeconds(mp.end-mp.begin));
          }
          nwc.add(srcFile.getAbsolutePath());
          exec(client, nwc);
   }
   private static String getSpeed(final int speed){  //2或4
         if(speed==4)return "\"[0:v]setpts=0.25*PTS[v];[0:a]atempo=4.0[a]\"";
         if(speed==2)return "\"[0:v]setpts=0.5*PTS[v];[0:a]atempo=2.0[a]\"";
         return null;
   }

     /**
        *预播放
        *@client 客户端
        *@com ffmpeg一行命令
     **/
    public static  void prePlay(final MainFrame client, final List<String> com)throws Exception
   {
         prePlay(  client,   com, null);
    }
    public static  void prePlay(final MainFrame client, final List<String> com, final Integer num)throws Exception
   {
          //clone 不改变源集
          Vector<String> nwc=new Vector<String>(com);
          //清除输出文件，最后
          nwc.removeElementAt(nwc.size()-1);
          //改为播放命令
          nwc.setElementAt(MediaConst.ffplay, 0);
          //清除copy参数
          int indexCopy=nwc.indexOf("copy");
          if(indexCopy>0){
             nwc.removeElementAt(indexCopy-1);
             nwc.remove("copy");
          }
          //清除覆盖参数
          nwc.remove("-y");
          //定义窗口标题
          nwc.add("-window_title");
          nwc.add(prePlayMsg[0]+getNumStr(num)+prePlayMsg[1]);
          nwc.add("-autoexit");                                              //播完自动关闭
          nwc.add("-x");                                                         //600x400
          nwc.add("600");
          nwc.add("-y"); 
          nwc.add("400");         
          exec(client, nwc);
    }
    private static String getNumStr(final Integer num){
         if(num==null)return getNumStrMsg[0];
         StringBuilder sb=new StringBuilder();
         int n=num;
         sb.append(getNumStrMsg[1]);
         if(n<100){
            sb.append('0');
            if(n<10)sb.append('0');
         }
         sb.append(n);
         sb.append(getNumStrMsg[2]);
         return sb.toString();
   }


      /**
        *是否拷贝策略：扩展名相同，大小小于MediaMaxCopyLength return true; 其它return false;
        *@srcFile 源文件
        *@tarFile 目标文件
        *@return 是否拷贝
     **/
    public static boolean checkCopy(final File srcFile,  final File tarFile){
         if(srcFile.length()>MediaConst.mediaMaxCopyLength*1024*1024)return false;
         String fn1=srcFile.getName();
         int idx1=fn1.lastIndexOf(".");
         String ext1=fn1.substring(idx1+1);
         String fn2=tarFile.getName();
         int idx2=fn2.lastIndexOf(".");
         String ext2=fn2.substring(idx2+1);
         return ext1.equalsIgnoreCase(ext2);
    }


     /**
       *片段命令行   ffmpeg -y -i 1.mp4 -ss 00:00:00.0  -t 00:00:10.0 201.mp4
       *@coms 多行命令
       *@parts 媒体片段
    **/
    public static void buildMediaPartComs(final List<List<String>> coms, final List<MediaPart> parts){
        for(int i=0; i<parts.size(); i++){
           coms.add(buildMediaPartParas(parts.get(i)));
        }
   }
     /**
       *
       *@mp 时段  
       *@return 一行的参数
    **/
    public static List<String> buildMediaPartParas(final MediaPart mp){
        List<String> extCom=null;
        if(mp.begin>0L || mp.end>0L){  //有时间参数-ss 00:00:00.0  -t 00:00:10.0  或 -ss 0.0 -t 0.0
           extCom=new ArrayList<String>(6);
           if(mp.begin>0L){
              extCom.add("-ss");
              extCom.add(buildStrFromPersentSeconds(mp.begin));
           }
           if(mp.end>0L){
              extCom.add("-t");
              extCom.add(buildStrFromPersentSeconds(mp.end-mp.begin));
           }
        }
        if(mp.isCopy){
           if(extCom==null)extCom=new ArrayList<String>(2);
           extCom.add("-c");
           extCom.add("copy");
        }
        return buildLineCom(mp.srcFile, mp.tarFile, extCom);
    }
     /**
       *构建一行代码
       *@input 源文件
       *@output 输出文件
       *@extCom 附件参数集
       *@return 一行命令
    **/
    public static List<String> buildLineCom(final File input, final File output, final List<String> extCom){
          List<String> com;
          if(extCom==null || extCom.isEmpty()){
              com=new ArrayList<String>(5);
              com.add(MediaConst.ffmpeg);
              com.add("-y");
              com.add("-i");
              com.add(input.getAbsolutePath());
          }else{
              com=new ArrayList<String>(5+extCom.size());
              com.add(MediaConst.ffmpeg);
              com.add("-y");
              com.add("-i");
              com.add(input.getAbsolutePath());
              com.addAll(extCom);
          }
          com.add(output.getAbsolutePath());
          return com;
    }

     /**
       *concat 命令行  ffmpeg -y -f concat -i  concat.txt  2.mp4
       *@f 一个文件  一行：file '201.mp4'
       *@return 一行concat
    **/
    public static List<String> createConcatCom(final File concatFile, final File tarFile){
         ArrayList<String> lst=new ArrayList<String>(9);
         lst.add(MediaConst.ffmpeg);
         lst.add("-y");
         lst.add("-f");
         lst.add("concat");
         lst.add("-safe");
         lst.add("0");
         lst.add("-i");
         lst.add(concatFile.getAbsolutePath());
         lst.add(tarFile.getAbsolutePath());
         return lst;
    }

     /**
       *
       *@f 一个文件  一行：file '201.mp4'
       *@return 一行concat
    **/
    public static String buildConcatLine(final File f){
        String sb = "file '" +
                f.getAbsolutePath() +
                '\'' +
                PubConst.newLine;
        return sb;
    }

    /**
      *构建媒体时间 全空表示文件头或文件尾，return -1
      *@hour 时
      *@minute 分
      *@second 秒
      *@persentSecond 百分秒
      *@return 百分秒数
      *@throws NumberFormatException 数据格式有误
    **/
   public static long getMediaPartTime(String hour, String minute, String second, String persentSecond)throws NumberFormatException
   {
        if( (hour==null || "".equals(hour=hour.trim())) 
            &&  (minute==null || "".equals(minute=minute.trim())) 
            &&  (second==null || "".equals(second=second.trim())) 
            &&  (persentSecond==null || "".equals(persentSecond=persentSecond.trim())) )return -1L;
       long rst=0;
       int n=0;
       if(hour!=null && !"".equals(hour)){
          boolean hsErr=false;
          try{
              n=Integer.parseInt(hour);
          }catch(NumberFormatException e){
             hsErr=true;
          }
          if(hsErr || n<0 || n>23)throw new NumberFormatException(getMediaPartTimeMsg[0]);
          rst+=n*60*60*100;
       }
       if(minute!=null && !"".equals(minute) ){
          boolean hsErr=false;
          try{
              n=Integer.parseInt(minute);
          }catch(NumberFormatException e){
             hsErr=true;
          }
          if(hsErr || n<0 || n>59)throw new NumberFormatException(getMediaPartTimeMsg[1]);
          rst+=n*60*100;
      }
       if(second!=null && !"".equals(second) ){
          boolean hsErr=false;
          try{
              n=Integer.parseInt(second);
          }catch(NumberFormatException e){
             hsErr=true;
          }
          if(hsErr || n<0 || n>59)throw new NumberFormatException(getMediaPartTimeMsg[2]);
          rst+=n*100;
      }
      if(persentSecond!=null && !"".equals(persentSecond) ){
          boolean hsErr=false;
          try{
              n=Integer.parseInt(persentSecond);
          }catch(NumberFormatException e){
             hsErr=true;
          }
          if(hsErr || n<0 || n>99)throw new NumberFormatException(getMediaPartTimeMsg[3]);
          rst+=n;
      }
      if(rst==0L)return -1;
      return rst;
   }

   /**
    *构建媒体片段描述
    *@part 视频片段
    *@return 媒体片段描述
   **/
   public static String buildStrFromMediaPart(final MediaPart part){  
       StringBuilder sb=new StringBuilder(100);
       sb.append(buildStrFromMediaPartMsg[0]);
       if(part.num<100){
           sb.append('0');
           if(part.num<10)sb.append('0');
       }
       sb.append(part.num);
       sb.append(buildStrFromMediaPartMsg[1]);
       if(part.begin<0L)sb.append(buildStrFromMediaPartMsg[2]);
       else sb.append(buildStrFromPersentSeconds(part.begin));
       sb.append(buildStrFromMediaPartMsg[3]);
       if(part.end<0L)sb.append(buildStrFromMediaPartMsg[4]);
       else sb.append(buildStrFromPersentSeconds(part.end));
       if(part.file!=null){
          sb.append(buildStrFromMediaPartMsg[5])
          .append(part.index+1).append(buildStrFromMediaPartMsg[6])
          .append(part.file.getName());
       }
       return sb.toString();
   }

   /**
    *从秒数构建字符串
    *@seconds 含百分秒的秒数
    *@return 时间字符串
   **/
   public static String buildStrFromPersentSeconds(long persentSecond){  
       long second=persentSecond/100;   //秒
       persentSecond-=100*second;  //百分秒
       long minute=0L;  //分
       long hour=0L;   //时
       if(second>59L){
          minute=second/60;
          second-=60*minute;
       }
       if(minute>59L){
          hour=minute/60;
          minute-=60*hour;
       }
       if(hour>23L){
          hour%=24;
       }
       StringBuilder sb=new StringBuilder(20);
       if(hour<10L)sb.append('0');
       sb.append(hour);
       sb.append(':');
       if(minute<10L)sb.append('0');
       sb.append(minute);
       sb.append(':');
       if(second<10L)sb.append('0');
       sb.append(second);
       sb.append('.');
       if(persentSecond<10L)sb.append('0');
       sb.append(persentSecond);
       return sb.toString();
   }

       /**
         *获取媒体过滤器
         *@return FFCFileFilter
       **/
       private static FFCFileFilter fileFilter=null;
       public static synchronized FFCFileFilter getMediaFileFilter(){
           if(fileFilter!=null)return fileFilter;
           if(MediaConst.mediaTypes==null || MediaConst.mediaTypes.length==0)return null;
           return new FFCFileFilter(MediaConstProps.mediaFilterName, MediaConst.mediaTypes);
           //(int i=0;i<MediaConst.mediaTypes.length;i++){
            //   MediaConst.mediaTypes[i]=MediaConst.mediaTypes[i].toLowerCase();
           //}
          // fileFilter=new MediaFileFilter();
          // return fileFilter;
      }
     //过滤器
    /*
     private static class MediaFileFilter extends FileFilter{
        
         * Whether the given file is accepted by this filter.
         
        public boolean accept(File f){
            if(f.isDirectory())return true;
           String fn=f.getName();
           int idx=fn.lastIndexOf(".");
           if(idx<0)return false;
           fn=fn.substring(idx+1).toLowerCase();
           for(int i=0;i<MediaConst.mediaTypes.length;i++){
               if(MediaConst.mediaTypes[i].equals(fn))return true;
           }
           return false;  
       }

       * The description of this filter. For example: "JPG and GIF Images"
       * @see FileView#getName
     
      public String getDescription(){return MediaConst.mediaFilterName;}

  } */

      /**
       *播放媒体 完成后播放，用外置播放器
       *@mediaFile 媒体文件
       *@mediaLink 媒体链接
     **/
      public static   void play(final MainFrame client, final File mediaFile){
          play(client, mediaFile.getAbsolutePath());
      }
      public static   void play(final MainFrame client, final String mediaLink){
          play( client, getPlayCom(),  mediaLink);
      }    
      public static  void play(final MainFrame client, final List<String> com, final String mediaLink){
          com.add(mediaLink);
          play(  client,   com);
      }       
      public static  void play(final MainFrame client, final List<String> com){
          play(  client,   com, true);
      }         
      private static  void play(final MainFrame client, final List<String> com, final boolean isDisplay){
          if(client!=null && isDisplay)client.display(mediaPlayMsg[0]+com+PubConst.newLine);  //测试
         try{
              CmdExecuter.exec(com,null);
         }catch(Exception e){
                           e.printStackTrace();
                           if(client!=null)client.display(mediaPlayMsg[1]+PubConst.newLine);
         }
      }
      //获取播放器
      public static  List<String> getPlayCom(){
          ArrayList<String> com=new ArrayList<String>(5);
          //final String fnStr=mediaFile.getAbsolutePath();
          com.addAll(StringUtil.getDosComLine(MediaConst.extPlay));
          //com.add("-window_title");                                        //标题
          //com.add("正在播放媒体："+fnStr);
         // com.add("-autoexit");                                              //播完自动关闭
         // com.add("-x");                                                         //600x400
         // com.add("600");
         // com.add("-y"); 
        //  com.add("400");
          return com;
      }
      /*
       public static Process play(final String  host, final int port){
           System.out.println("play=http://"+host+":"+port);

                   String[] com=new String[2];
                   com[0]="D:\\5xstar\\VLC\\vlc.exe";
                   com[1]="http://"+host+":"+port;
                   Process proc = null;
                   try{
                       Runtime run=Runtime.getRuntime();
                       if(run!=null){
                           proc = run.exec(com, null);
                           System.err.println("vlc have started"+com[1]);
                           // proc.waitFor();                        
                        }      
                   }catch(Exception e){
                       e.printStackTrace();
                      System.err.println("vlc start error!");
                   }
                   return proc;

       }*/

       /**
        *检查文件扩展名判断是否媒体
        *@fXst 文件扩展名
       **/
       public static boolean check(final File mediaFile){
           final String extStr= check1(mediaFile);
           if(extStr==null)return false;
           else return check(extStr);
       }
    private static String check1(final File mediaFile){
        if(mediaFile==null)return null;
        String name=mediaFile.getName();
        int idx=name.lastIndexOf(".");
        if(idx<0)return null;
        return name.substring(idx+1);
    }
       /**
        *检查文件扩展名判断是否媒体
        *@fXst 文件扩展名
       **/
       public static boolean check(String fXst){
           //System.out.println("扩展名“"+fXst+"“判断是否媒体:"+rst);  //测试
           if(fXst==null)return false;
           if("".equals(fXst=fXst.trim()))return false;
           for(int i=0;i<MediaConst.mediaTypes.length;i++){
               if(MediaConst.mediaTypes[i].equalsIgnoreCase(fXst))return true;
           }
           for(int i=0;i<MediaConst.imageTypes.length;i++){
               if(MediaConst.imageTypes[i].equalsIgnoreCase(fXst))return true;
           }
           return false; 
       }
       public static boolean checkMusic(String fXst){
           //System.out.println("扩展名“"+fXst+"“判断是否媒体:"+rst);  //测试
           if(fXst==null)return false;
           if("".equals(fXst=fXst.trim()))return false;
           for(int i=0;i<MediaConst.musicTypes.length;i++){
               if(MediaConst.musicTypes[i].equalsIgnoreCase(fXst))return true;
           }
           return false; 
       }

    /**
     * 检查一组媒体是否有同样的格式
     */
      public static boolean checkSame(final File[] files){
          assert (files!=null && files.length>0);
          final String extStr= check1(files[0]);
          if(extStr==null)return false;
          //if(!check(extStr))return false; 不做扩展名检查，否则不在列表中的媒体就无法输入
          int idx;
          String fn,sub;
          for(int i=1; i<files.length;i++){
              fn = files[i].getName();
              idx = fn.lastIndexOf('.');
              if(idx<0)return false;
              sub = fn.substring(idx+1);
              if(extStr.equalsIgnoreCase(sub))continue;
              else return false;
          }
          return true;
      }



    /**
     *创建媒体输入临时文件
       *@srcFile 需要拷贝的文件
       *@return 创建的临时文件
      **/
      public static File createSrcFile(final File srcFile)throws IOException
      {
          if(srcFile==null || !srcFile.exists() || srcFile.isDirectory() )throw new IOException(srcFileNoExistsText);
          String fXst=null;
          String name=srcFile.getName();
          int idx=name.lastIndexOf(".");
          if(idx>-1){
            fXst=name.substring(idx+1);
          }
          return createTempFile(srcFile, fXst, false);
      }
      /**
       *创建媒体输出临时文件
       *@srcFile 原需要加工的文件
       *@return 创建的临时文件
      **/
      public static File createTarFile(final File srcFile)throws IOException
      {
          String fXst=null;
          if(srcFile!=null){
             String name=srcFile.getName();
             int idx=name.lastIndexOf(".");
             if(idx>-1){
                fXst=name.substring(idx+1);
            }
          }
          return createTarFile(fXst);
      }
      /**
       *创建媒体输出临时文件
       *@fXst 扩展名
       *@return 创建的临时文件
      **/
      public static File createTarFile(final String fXst)throws IOException
      {
          return createTempFile(null, fXst, true);
      }    
      /**
       *创建媒体解包输出临时文件夹
       *@srcFile 原需要加工的文件
       *@return 创建的临时文件
      **/
      public static File createTarDir(final File tarFile){
         String temp=System.getProperty("java.io.tmpdir");
         if(temp==null)temp=System.getProperty("user.home");
         StringBuilder sb=new StringBuilder(255);
         sb.append('M');
         sb.append(++tempFileNum);
         sb.append('_');
         sb.append(System.currentTimeMillis());  //防覆盖
         File tempFile;
         if(temp==null){
             tempFile=new File(sb.toString());
         }else{
             tempFile=new File(temp,sb.toString());
         }
         tempFile.mkdirs();
         return tempFile;
      }

      /**
       *创建媒体临时文件
       *@srcFile 需要拷贝的文件  isTarget=false 时才起作用
       *@fXst 文件扩展名
       *@return 创建的临时文件
      **/
      private static long tempFileNum=0;     //防止临时文件重复
      private static synchronized File createTempFile(final File srcFile, String fXst, final boolean isTarget)throws IOException
      {
         String temp=System.getProperty("java.io.tmpdir");
         if(temp==null)temp=System.getProperty("user.home");
         StringBuilder sb=new StringBuilder(255);
         sb.append('M');
         sb.append(++tempFileNum);
         sb.append('_');
         sb.append(System.currentTimeMillis());  //防覆盖
         if(fXst!=null){
            sb.append('.');
            sb.append(fXst);
         }
         File tempFile;
         if(temp==null){
             tempFile=new File(sb.toString());
         }else{
             tempFile=new File(temp,sb.toString());
         }
         //System.out.println("tempFile="+tempFile.getAbsolutePath());  //测试
         if(!isTarget && srcFile!=null && srcFile.exists() && srcFile.isFile()){
             MyFileService.copy(srcFile, tempFile);
         }
         return tempFile;
      }

    public static String buildFileMediaInfo(final FileMediaInfo fmi){
        if(fmi.mediaInfo==null || fmi.mediaInfo.duration==null)return null;
        final StringBuilder sb= new StringBuilder();
        if(fmi.file!=null )sb.append('(').append(fmi.index+1).append(')');
        sb.append(buildFileMediaInfoMsg[0]).append(fmi.mediaInfo.duration.duration);
        if(fmi.file!=null) sb.append(buildFileMediaInfoMsg[1]).append(fmi.file.getName());
        return sb.toString();
    }


}
