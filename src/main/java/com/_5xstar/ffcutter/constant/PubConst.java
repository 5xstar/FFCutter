package com._5xstar.ffcutter.constant;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 公用常数 
 * 庞海文 2019-9-7 
 **/
public class PubConst
{



      //本应用用的执行池,在定时执行器中关闭
      public final static ExecutorService es=Executors.newCachedThreadPool();


    //换行符
    private static String  _newLine = System.getProperty("line.separator");
    public static String  newLine=(_newLine==null || "".equals(_newLine=_newLine.trim()))?"\n":_newLine;



  //reload问题解决，忽略网络传输时间，视频流大概有3-4秒的时差
  final public static int defaultFlowWaitTimeMillis = 7000;

    //最大消息长度 2k个字符，汉字算2，这个参数在连接服务器后需要与服务器值比较，如果大于服务器值，采用服务器值
    public static int maxMsgLength=2048;
    //每行参考字数
    public static int lineMsgLength=10;


 
}

