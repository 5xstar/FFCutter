package com._5xstar.ffcutter.file;

import com._5xstar.ffcutter.cmd.CmdExecuter;
import com._5xstar.ffcutter.StringHandler;
import com._5xstar.ffcutter.Configure;
import com._5xstar.ffcutter.constant.PubConst;
import com._5xstar.ffcutter.util.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 文件选定弹窗方法
 * 庞海文 2023-2-4
 */
public class GetFile implements Configure {
    private static String pythonCom="python";
    //是否存在python选文件
    public static boolean getFileOn = false;
    //获取文件对话框的Python脚本文件
    private static String getFileName = "getFile.py";
    private static String getFilePath=getFileName;
    private static boolean hasConf = false;
    //配置
    public static void conf(){
        new GetFile().conf(new Properties());
    }
    public void conf( final Properties prop) {
        pythonCom= StringUtil.parse(prop, "pythonCom", pythonCom);
        getFileName = StringUtil.parse(prop, "getFileName", getFileName);
        if (!getFileOn) {  //启动Python getFile

                final File f = new File(getFileName);  //检查运行目录
                getFilePath=f.getAbsolutePath();
                if (f.exists()) {
                    getFileOn = f.isFile();
                } else {
                    java.net.URL url = getClass().getClassLoader().getResource(getFileName);  //类装载根目录
                    if(url==null)url = getClass().getResource(getFileName);  //类目录
                    if (url != null) {
                        String urlFile = url.getFile();
                        System.out.println("getFile:" + urlFile);  //测试
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
                        } else {
                            final File srcFile = new File(urlFile);
                            if (srcFile.exists() && srcFile.isFile()) {
                                try {
                                    MyFileService.copy(srcFile, f);
                                    getFileOn = true;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
        }
        System.out.println(GetFileProps.confMsg[0] + getFileName+(getFileOn?GetFileProps.confMsg[1]:GetFileProps.confMsg[2]));
        hasConf = true;
    }


    //allType = 0无*.*，1在前（默认），2在后   ,  mode
    public static void openSystemFile(final Object window, final String title, final int mode, final String exportName, String openPath, final StringHandler handler, List<FFCFileFilter> filters, final int allType)throws Exception
    {
            if (!hasConf)conf();
            if (!getFileOn) {
                //if (!QtUtil.qtOn) QtUtil.init(null);
                //if (!QtUtil.qtOn) throw new Exception("Python 和 Qt都未初始化！");
                //QtUtil.openQtFile(window, title, mode, exportName, openPath, handler, filters, allType);
                //return;
                throw new Exception(GetFileProps.openSystemFileMsg);
            }
            if (filters == null) filters = new ArrayList<FFCFileFilter>();
            final ArrayList<String> coms = new ArrayList<String>(200 + 100 * filters.size());
            coms.add(pythonCom);
            coms.add(getFilePath);
            //"{'title':'选择文本文件','mode':0,'fileTypes':[['文本文件','*.txt'],['所有文件','*']]}"
            final StringBuilder sb = new StringBuilder();
            sb.append("\"{'title':'").append(getTitle(  title,   mode)).append("','mode':").append(mode).append(",'fileTypes':[");
            getFileFilter(sb, filters, allType);
            sb.append(']');
            if (exportName != null) {
                sb.append(",'exportName':'").append(exportName).append("'");
            }
            if (openPath != null) {
                openPath = openPath.replaceAll("\\\\", "/");
                sb.append(",'openPath':'").append(openPath).append("'");
            }
            sb.append("}\"");

            coms.add(sb.toString());
            PubConst.es.submit(new Runnable() {
                public void run() {
                    try {
                        CmdExecuter.exec(coms, handler);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
    }
    private static String getTitle(final String title, final int mode){
        if(title!=null)return title;
        switch(mode){
            case 0:
                return GetFileProps.getTitleMsg[0];
            case 1:
                return GetFileProps.getTitleMsg[1];
            case 2:
                return GetFileProps.getTitleMsg[2];
            default:
                return GetFileProps.getTitleMsg[3];
        }
    }
    private static void getFileFilter(final StringBuilder sb, final List<FFCFileFilter> filters, final int allType){
        switch(allType){
            case 1:
                sb.append("['"+GetFileProps.allFilesText+"','*']");
                if(!filters.isEmpty())sb.append(',');
            case 0:
                getFileFilter(  sb,    filters);
                break;
            default:
                if(!filters.isEmpty()){
                    getFileFilter(  sb,    filters);
                    sb.append(',');
                }
                sb.append("['"+GetFileProps.allFilesText+"','*']");
        }
    }
    private static void getFileFilter(final StringBuilder sb,  final List<FFCFileFilter> filters){
        boolean isFirst=true;
        for(FFCFileFilter ff : filters ){
            if(isFirst)isFirst=false;
            else sb.append(',');
            getFileFilter(sb, ff.name, ff.exts);
        }
    }
    //一个过滤器
    private static void getFileFilter(final StringBuilder sb,final String name, final String[] types) {
        sb.append("['").append(name).append("','");
        getFileFilter( sb, types);
        sb.append("']");
    }
    private static void getFileFilter(final StringBuilder sb,final String[] types) {
        sb.append("*.").append(types[0]);
        for(int i=1;i<types.length;i++)sb.append(" *.").append(types[i]);
    }
/*
     //测试
    public static void main(String[] args){
        try{
            Map<String,String[]> filters = new HashMap<String,String[]>();
            filters.put("文本文件", new String[]{"txt","bat","sh"});
            StringHandler handler = new StringHandler() {
                @Override
                public void handle(String obj) {
                    System.out.println(obj);
                }
            };
            //openSystemFile(null, null, 0,  "D:/5xstar/work", null,  handler,filters,1);
            //final JDialog dlg =new JDialog();
            //JFileChooser jc = createJFileChooser(new FileKeeperBase(),  dlg, null);
            //dlg.setVisible(true);
            //openSystemFile();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
*/


}
