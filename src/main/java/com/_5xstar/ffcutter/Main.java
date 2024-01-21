package com._5xstar.ffcutter;


import java.io.*;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com._5xstar.ffcutter.cutter.swing.JMainFrameImpl;
import com._5xstar.ffcutter.file.GetFile;
import com._5xstar.ffcutter.file.MyFileService;
import org.apache.commons.io.Charsets;

/**
 * FFCutter入口
 * 庞海文  2024-1-21
 */
@Language
public class Main {
    /**
     * 本应用使用的道具
     */
    static{
        //Props
        String fn = Main.class.getAnnotation(Language.class).propsFile();
        String charset= Main.class.getAnnotation(Language.class).charset();
        System.out.println("props file name:"+fn);
        System.out.println("charset:"+charset);
        init( charset,   fn);
        //参数
        fn = Main.class.getAnnotation(Language.class)._parasFile();
        System.out.println("parameters file name:"+fn);
        init( charset,   fn);
        GetFile.conf();  //测试
    }
    private static void init(String charset, String fn){
            File f = new File(fn);  //运行根目录查找
            boolean getFileOn = false;
            if(f.exists() && f.isFile()){
                getFileOn = true;
            }else {
                URL url = Main.class.getClassLoader().getResource(fn);  //类装载根目录
                if(url==null)url = Main.class.getResource(fn);  //类装目录
                if (url != null) {
                    String urlFile = url.getFile();
                    System.out.println("conf File:" + urlFile);  //测试
                    if (urlFile.indexOf(".jar!") > 0) {
                        InputStream in = null;
                        try {
                            in = url.openStream();  //getClass().getClassLoader().getResourceAsStream(urlFile);
                            if (in != null) {
                                MyFileService.writeToDefaultFile(f, in);
                                getFileOn = true;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (in != null) try {
                                in.close();
                            } catch (Exception e55) {
                            }
                        }
                    }
                }
            }
            if(getFileOn) {
                Reader reader = null;
                try {
                    reader = new FileReader(f, Charsets.toCharset(charset, StandardCharsets.UTF_8));
                    final Properties paras = new Properties();
                    paras.load(reader);
                    final HashMap<String, Properties> classParases = new HashMap<>();
                    for (String pn : paras.stringPropertyNames()) {
                        parse(classParases, pn, paras.getProperty(pn));
                    }
                    for (Map.Entry<String, Properties> entry : classParases.entrySet()) {
                        conf(entry.getKey(), entry.getValue());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(reader!=null)try{reader.close();}catch (Exception e){}
                }
            }

    }
    private static void parse(final HashMap<String,Properties> classParases, String paraName, String paraValue){
        final int ldot = paraName.lastIndexOf(".");
        String className = paraName.substring(0, ldot);
        String classParaName = paraName.substring(ldot+1);
        Properties classParas = classParases.get(className);
        if(classParas==null){
            classParas=new Properties();
            classParases.put(className, classParas);
        }
        classParas.setProperty(classParaName, paraValue);
    }
    private static  void conf(final String className, final Properties paras){
        System.out.println("Configure for " + className + " paras=" + paras.stringPropertyNames());
        try {
            Constructor<?>[] t = Class.forName(className).getConstructors();
            for(Constructor<?> c : t) {
                if(c.getParameterCount()==0) {
                    Configure cf = (Configure) c.newInstance();
                    cf.conf(paras);
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("Start FFCutter.......");
        new JMainFrameImpl();
    }
}