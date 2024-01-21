package com._5xstar.ffcutter.util;

import com._5xstar.ffcutter.constant.PubConst;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具 
 * 庞海文 2019-9-7
 **/
public class StringUtil 
{


	// 判断一个字符串是否为空
	public static boolean isEmptyString( String str )
	{
		return str==null || str.length()==0;
	}

	// 去掉一个字符串两端的空格
	public static String trim( String str )
	{
		if ( str == null ) return null;
		return str.trim();
	}

    public static String[] strTokenizer( String str, String delim )
    {
		if( isEmptyString( trim( str ) ) ) return null;

		StringTokenizer st = new StringTokenizer( str, delim );
		String[] result = new String[st.countTokens()];
		int i=0;
		while ( st.hasMoreTokens() ) 
		{
			result[i++] = st.nextToken(); 
		}
		return result;
    }



  //构建dos命令List
  final private static Pattern dosPattern = Pattern.compile("[\"']([^\"']+)[\"']"); 
  public static List<String> getDosComLine(String str){
      if(str==null)return null;
      Matcher mt=dosPattern.matcher(str);
      final ArrayList<String> rst=new ArrayList<String>(50);
      int start=0;
      String ac;
      while(mt.find(start)){
           dosAppend(str.substring(start,mt.start()), rst);
           ac=mt.group(1);
           if(ac!=null && !"".equals(ac=ac.trim()))rst.add(ac);
           start=mt.end();
      }
      if(start<str.length())dosAppend(str.substring(start), rst);
      return rst;
  }
 private static void  dosAppend(String str, final ArrayList<String> rst){
      if(str==null || "".equals(str=str.trim()))return;
      String[] sts=str.split("\\s+");
      if(sts!=null && sts.length>0) {
          Collections.addAll(rst, sts);
      }
 }


  //用于配置
  public static String parse(final Properties prop, final String key, final String defaultValue){
           String st=prop.getProperty(key);
           if(st==null)return defaultValue;
           else return st;
  }
  public static int parse(final Properties prop, final String key, final int defaultValue){
           String st=prop.getProperty(key);
           if(st!=null)try{
             return Integer.parseInt(st);
           }catch(Exception e){
              e.printStackTrace();
           }
           return defaultValue;
  }
  public static long parse(final Properties prop, final String key, final long defaultValue){
           String st=prop.getProperty(key);
           if(st!=null)try{
             return Long.parseLong(st);
           }catch(Exception e){
              e.printStackTrace();
           }
           return defaultValue;
  }
  public static float parse(final Properties prop, final String key, final float defaultValue){
           String st=prop.getProperty(key);
           if(st!=null)try{
             return Float.parseFloat(st);
           }catch(Exception e){
              e.printStackTrace();
           }
           return defaultValue;
  } 
  public static double parse(final Properties prop, final String key, final double defaultValue){
           String st=prop.getProperty(key);
           if(st!=null)try{
             return Double.parseDouble(st);
           }catch(Exception e){
              e.printStackTrace();
           }
           return defaultValue;
  }
  public static boolean parse(final Properties prop, final String key, final boolean defaultValue){
           String st=prop.getProperty(key);
           if(st==null || "".equals(st=st.trim()))return defaultValue;
           st=st.toLowerCase();
      return st.startsWith("yes")
              || st.startsWith("true")
              || st.startsWith("ok")
              || st.startsWith("1")
              || st.startsWith("2")
              || st.startsWith("3")
              || st.startsWith("4")
              || st.startsWith("5")
              || st.startsWith("6")
              || st.startsWith("7")
              || st.startsWith("8")
              || st.startsWith("9");
  }
    public static int[] parse(final Properties prop, final String key, final int[] defaultValue){
        String str=prop.getProperty(key);
        if(str==null)return defaultValue;
        String[] sts=strTokenizer(str, "," );
        if(sts==null || sts.length==0)return null;
        final int[] rst = new int[sts.length];
        try{
            for(int i=0;i<sts.length;i++)rst[i]=Integer.parseInt(sts[i]);
            return rst;
        }catch(Exception e){
            e.printStackTrace();
            return defaultValue;
        }
    }
  public static Color parse(final Properties prop, final String key, final Color defaultValue){
       String str=prop.getProperty(key);
       if(str==null)return defaultValue;
       String[] sts=strTokenizer(str, "," );
       try{
           if(sts.length==1){
              if(str.startsWith("#"))return createColor16(str.substring(1)); 
              else if(str.startsWith("0x"))return createColor16(str.substring(2)); 
              else return createColor16(str); 
          }else if(sts.length==3){
             return new Color(Integer.parseInt(sts[0]), Integer.parseInt(sts[1]), Integer.parseInt(sts[2]));
         }else{
            return defaultValue;
         }
      }catch(Exception e){
         e.printStackTrace();
         return defaultValue;
      }
  }
  //转换16进制数
  private static Color createColor16(String str)throws Exception{
      if(str.length()!=6)throw new Exception();
      return new Color(toInt16(str.substring(0,2)), toInt16(str.substring(2,4)), toInt16(str.substring(4)));
 }
 private static int toInt16(String str)throws Exception{
     return 16*toInt16(str.charAt(0))+toInt16(str.charAt(1));
 }
 private static int toInt16(char c)throws Exception{
    switch(c){
        case '0':
                return 0;
        case '1':
                return 1;
        case '2':
                return 2;
        case '3':
                return 3;
        case '4':
                return 4;
        case '5':
                return 5;
        case '6':
                return 6;
        case '7':
                return 7;
        case '8':
                return 8;
        case '9':
                return 9;
        case 'a':
        case 'A':
                return 10;
        case 'b':
        case 'B':
                return 11;
        case 'c':
        case 'C':
                return 12;
        case 'd':
        case 'D':
                return 13;
        case 'e':
        case 'E':
                return 14;
        case 'f':
        case 'F':
                return 15;
       default:
       throw new Exception();
    }
 }

  public static Font parse(final Properties prop, final String key, final Font defaultValue){
       String str=prop.getProperty(key);
       if(str==null)return defaultValue;
       String[] sts=strTokenizer(str, "," );
       if(sts.length!=3)return defaultValue;
       try{
          return new Font(sts[0], parseFontStyleInt(sts[1]),  Integer.parseInt(sts[2]));  
      }catch(Exception e){
         e.printStackTrace();
         return defaultValue;
      }
  }

  private static int parseFontStyleInt(String style)throws Exception{
      if(Pattern.matches("^[0-9]+$", style))return Integer.parseInt(style);
      if(style.equalsIgnoreCase("PLAIN"))return Font.PLAIN;
      String[] sts=strTokenizer(style, "|" );
      int s=0;
      for(int i=0;i<sts.length;i++)s+=getFontStyle(sts[i]);
      return s;
  }
  private static int getFontStyle(String str)throws Exception{
      if(str.equalsIgnoreCase("BOLD"))return Font.BOLD;
      if(str.equalsIgnoreCase("ITALIC"))return Font.BOLD;
      throw new Exception();
  }

  public static String[] parse(final Properties prop, final String key, final String[] defaultValue){
       String str=prop.getProperty(key);
       if(str==null)return defaultValue;
       return strTokenizer(str, "," );
  }

  public static File parse(final Properties prop, final String key, final File defaultValue){
       String str=prop.getProperty(key);
       if(str==null)return defaultValue;
       return new File(str );
  }

    //低频使用的unicode to String
    public static String unicode2String(String str){
            final Matcher mtr = Pattern.compile("\\\\u(\\w{4})").matcher(str);
            final StringBuilder sb = new StringBuilder();
            int start=0, end;
            while(mtr.find()){
                end = mtr.start();
                sb.append(str.substring(start,end));
                sb.append((char)Integer.parseInt(mtr.group(1), 16));
                start = end+6;
            }
            return sb.append(str.substring(start)).toString();
    }

    //取得前面的行，使长度符合要求<=limLen
    public static String subLine(String st, int limLen){
        int len=0;
        if(st==null || (len=st.length())<=limLen)return st;
        String[] sts=strTokenizer( st, PubConst.newLine );
        StringBuilder sb=new StringBuilder(limLen);
        int mx=len-limLen;
        boolean toAdd=false;
        int sum=0;
        for(int i=0;i<sts.length;i++){
            if(toAdd){
                sb.append(sts[i]);
                sb.append(PubConst.newLine);
            }else{
                toAdd=(sum+=sts[i].length())>=mx;
            }
        }
        return sb.toString();
    }

}

