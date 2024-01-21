package com._5xstar.ffcutter.cmd;

import com._5xstar.ffcutter.StringHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * CmdExecuter
 * 命令执行器
 * 庞海文 2014-11-25
 */
public class CmdExecuter {
	
  /**
   * 执行指令
   * @param cmd 执行指令
   * @param handler 指令返回处理接口，若为null则不处理输出
  **/
  final public static void exec(final List<String> cmd, final StringHandler handler )throws IOException
  {
         exec(cmd,  handler, false );
  }
    final public static void exec(final List<String> cmd, final StringHandler handler, final boolean waitForOn)throws IOException
  {
    System.out.println("cmd="+cmd);  //测试
    BufferedReader stdout =null;
    try {
      final ProcessBuilder builder = new ProcessBuilder();	
      builder.command(cmd);
      builder.redirectErrorStream(true);
      final Process proc = builder.start();
      stdout = new BufferedReader(new InputStreamReader(proc.getInputStream()));
      String line;
      if(handler==null){
        int i=0;
        while ((line = stdout.readLine()) != null){
           if(++i<50)System.out.println(line);  //测试
        }
      }else{
        while ((line = stdout.readLine()) != null) {
	   handler.handle(line);
        }
      }
      if(waitForOn)proc.waitFor();
    }catch (Exception e) {
      throw new IOException(e);
    }finally{
      if(stdout!=null)try{ stdout.close();}catch(Exception e2){}
    }
  }

}


