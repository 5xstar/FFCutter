package com._5xstar.ffcutter.file;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 *文件有关操作
 *庞海文 2020-2-17
**/
public final class MyFileService
{

  //本类方法同步对象
  final private static Object syncObj=new Object();

 /*
  * copy
  */
 public static void copy(final File inF, final File outF) throws IOException
 {
    synchronized(syncObj){
        if(inF!=null && inF.isDirectory()){  //zip文件
            ZipOutputStream out=null;
            try {
                out=new ZipOutputStream(new FileOutputStream(outF));
                copy2Zip("", inF, out);
            }finally {
                if(out!=null)try{out.close();}catch(Exception e){}
            }
            return;
        }
	   FileInputStream in=null;
	   FileOutputStream out=null;	   
	   try{		   
	       in=new FileInputStream(inF);
	       out=new FileOutputStream(outF);
		   final int length=(int)inF.length();
		   final byte[] data=new byte[2048];
           int num;
           int mx=length/2048;
           if(length%2048>0)mx++;
	       for(int i=0;i<mx;i++){
	    	 num=in.read(data);
	    	 out.write(data,0,num);	
	       }	    	 
	     }finally{
		   if(in!=null)try{in.close();}catch(Exception e){}	
		   if(out!=null)try{out.close();}catch(Exception e){}
	     }
    } 
 }


  public static void writeToDefaultFile(final File f, final InputStream in)throws IOException{
      FileOutputStream out =null;
        try{
            out = new FileOutputStream(f);
            final byte[] data=new byte[2048];
            int num;
            while((num=in.read(data))>0){
                out.write(data,0,num);
                if(num<2048)break;
            }
            out.flush();
        }finally{
            if(out!=null)try{out.close();}catch(Exception e){}
        }
 }



  /**
    *把字符串写入utf-8文本文件
    *@f 文本文件
    *@str 要写的字符串（默认编码）
    *@throws 写入错误
  **/
  public static void writeStringToUTF8File(final File f, final String str)throws IOException{
     OutputStreamWriter osw =null;
     try{
        osw = new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8);
        osw.write(str);
        osw.flush();   
     }finally{
        if(osw!=null)try{osw.close();}catch(Exception e){}
     }   
  }

////////////////////////////////

  private static void delTreeFiles1(final File dir, final long lastModified)
  {

     if(dir==null || !dir.exists())return;
     if(dir.isFile()){
       if(dir.lastModified()<lastModified)try{dir.delete();}catch(Exception e1){}
       return;
     }
     final String[] fnames=dir.list(); 
     int length=0;
     if(fnames==null || (length=fnames.length)==0){
       return;
     }
     for(int i=0;i<length;i++){ 
       delTreeFiles1(new File(dir,fnames[i]),lastModified);   //递归调用     
       try{
             Thread.sleep(1);
        }catch(Exception e1){}
     }
  }

  //删除目录的全部文件及本身
  public static void delTreeFiles(final File dir)
  {
      synchronized(syncObj){
        delTreeFiles1(dir);
      }
  }
  private static void delTreeFiles1(final File dir)
  {

     if(dir==null || !dir.exists())return;
     if(dir.isFile()){
       try{dir.delete();}catch(Exception e1){}
       return;
     }
     final String[] fnames=dir.list(); 
     int length=0;
     if(fnames==null || (length=fnames.length)==0){
       try{dir.delete();}catch(Exception e1){} 
       return;
     }
     for(int i=0;i<length;i++){ 
       delTreeFiles1(new File(dir,fnames[i]));   //递归调用    
     }
     try{dir.delete();}catch(Exception e1){}    //递归返回时删除目录
  }

 //递归写入目录
 private static void copy2Zip(final String base, final File dir, final ZipOutputStream out ) throws IOException
 {
          String[] fls=dir.list();
          if(fls==null || fls.length==0)return;
          File f;
          ArrayList<File> inFs=new ArrayList<File>(fls.length);
          ArrayList<String> zipEntryNames=new ArrayList<String>(fls.length);         
          for(int i=0;i<fls.length;i++){
              f=new File(dir,fls[i]);
              if(f.isDirectory()){
                   copy2Zip(base+f.getName()+File.separator, f, out );
              }else{
                   inFs.add(f);
                   zipEntryNames.add(base+f.getName());
              }             
          }
          if(!inFs.isEmpty()){
             copy2Zip(  inFs, out,  zipEntryNames);
          }
 }
 private static void copy2Zip(final List<File> inFs, final ZipOutputStream out, final List<String> zipEntryNames) throws IOException
 {

	   FileInputStream in=null;

	   try{
             for(int j=0;j<inFs.size();j++){	
   
	       in=new FileInputStream(inFs.get(j));
               out.putNextEntry(new ZipEntry(zipEntryNames.get(j)));
		   final int length=(int)inFs.get(j).length();
		   final byte[] data=new byte[2048];
           int num;
           int mx=length/2048;
           if(length%2048>0)mx++;
	       for(int i=0;i<mx;i++){
	    	 num=in.read(data);
	    	 out.write(data,0,num);	
	       }
               out.closeEntry();
               if(in!=null)try{in.close();in=null;}catch(Exception e){}

               }	    	 
	     }finally{
		   if(in!=null)try{in.close();}catch(Exception e){}
	     }
 }

 
}
