package com._5xstar.ffcutter;

import com._5xstar.ffcutter.constant.MediaConst;
import com._5xstar.ffcutter.constant.PubConst;
import com._5xstar.ffcutter.domains.ButtonAction;
import com._5xstar.ffcutter.domains.FileKeeper;
import com._5xstar.ffcutter.file.FFCFileFilter;
import com._5xstar.ffcutter.file.GetFile;
import com._5xstar.ffcutter.util.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 主窗口
 * 庞海文 2024-1-17
 */
public interface MainFrame extends MainFrameStatus{

    /**
     * 获取框架
     * @return
     */
    Object getFrame();


    /**
     *消息窗
     *@timeoutSecond  超时秒数  0表示长期
     *@title 标题
     *@demo 显示语句
     **/
    default void openMsgDialog(final int timeoutSecond, final String title,  final String demo){

        openMsgDialog(getFrame(),   timeoutSecond,  title,    demo);
    }
    default void openMsgDialog(final Object frame, final int timeoutSecond, final String title,  final String demo){
        openConfirmDialog(frame,
                title,
                demo,
                timeoutSecond,
                null,
                null,
                null,
                null);
    }
    private void openConfirmDialog(final Object frame,final String title,   //有确定、否定、取消和超时，弹窗可以关闭
                                  final String demo,
                                  final int timeoutSecond,
                                  final  Runnable yesRun,
                                  final  Runnable noRun,
                                  final  Runnable cancelRun,
                                  final  Runnable timeoutRun){

        final List<ButtonAction> buttons = new ArrayList<ButtonAction>(3);
        buttons.add(new ButtonAction(MainFrameProps.confirmYesCaption, yesRun));
        if(noRun!=null)buttons.add(new ButtonAction(MainFrameProps.confirmNoCaption, noRun));
        if(cancelRun!=null)buttons.add(new ButtonAction(MainFrameProps.confirmCancelCaption, cancelRun));
        Runnable tout=timeoutRun;
        if(tout==null){
            if(cancelRun!=null)tout=cancelRun;
            else tout=noRun;
        }
        openConfirmDialog( frame, title,   demo, timeoutSecond, buttons, tout);
    }
     void openConfirmDialog(final Object frame,final String title,   //有确定、否定、取消和超时，弹窗可以关闭
                                              final String demo,
                                              final int timeoutSecond,
                                              final  List<ButtonAction> buttons,
                                              final  Runnable timeoutRun);

    /**
     * 显示消息
     * @param msg  消息
     * @param textType 显示类型
     */
    default void display(String msg, int textType) {
        Display<?> displayer = getDisplay();
        if(!msg.endsWith(PubConst.newLine))msg+=PubConst.newLine;  //加入换行
        displayer.display(msg, textType);
        //openMsgDialog(getFrame(), 3, textType== Display.DEBUG_TEXT?MainFrameProps.msgTitle[0]:MainFrameProps.msgTitle[1], msg);
    }
    default void display(String msg){
        display(msg,Display.CHAT_TEXT);
    }
    Display<?> getDisplay();

    /**
     *询问窗
     *@param title 标题
     *@param demo 询问语句
     *@param timeoutSecond  超时秒数  0表示长期
     *@param yesRun  确定时执行线程
     *@param cancelRun 取消或关闭窗口时执行线程
     *@param timeoutRun 超时时执行线程
     **/
    default void openConfirmDialog(final String title,     //有确定、否定、超时，弹窗可以关闭
                                  final String demo,
                                  final int timeoutSecond,
                                  final  Runnable yesRun,
                                  final  Runnable cancelRun,
                                  final  Runnable timeoutRun){

        openConfirmDialog(title,
                demo,
                timeoutSecond,
                yesRun,
                null,
                cancelRun,
                timeoutRun);
    }
    private void openConfirmDialog(final String title,   //有确定、否定、取消和超时，弹窗可以关闭
                                   final String demo,
                                   final int timeoutSecond,
                                   final  Runnable yesRun,
                                   final  Runnable noRun,
                                   final  Runnable cancelRun,
                                   final  Runnable timeoutRun){

        openConfirmDialog(getFrame(),  title,   //有确定、否定、取消和超时，弹窗可以关闭
                demo,
                timeoutSecond,
                yesRun,
                noRun,
                cancelRun,
                timeoutRun);
    }



    /**
     * 文件保存对话框
     *@param fKeeper 文件信息保存者
     *@param title 标题
     *@param demo 询问语句
     *@param timeoutSecond  超时秒数  0表示长期
     *@param yesRun  确定时执行线程
     *@param cancelRun 取消或关闭窗口时执行线程
     *@param timeoutRun 超时时执行线程
     **/
    default void openFileSaveDialog(  final FileKeeper fKeeper,
                                     final String title,
                                     final String demo,
                                     final FFCFileFilter filter,
                                     final int timeoutSecond,
                                     final  Runnable yesRun,
                                     final  Runnable cancelRun,
                                     final  Runnable timeoutRun) {
        List<FFCFileFilter> filters;
        if(filter==null)filters=getCommonFileFilters();
        else{
            filters = new ArrayList<FFCFileFilter>(1);
            filters.add(filter);
        }
        openFileSaveDialog(fKeeper,title, demo, filters, timeoutSecond, yesRun,  cancelRun, timeoutRun, 2);
    }
    private void openFileSaveDialog(  final FileKeeper fKeeper,
                                      final String title,
                                      final String demo,
                                      final List<FFCFileFilter> filters,
                                      final int timeoutSecond,
                                      final  Runnable yesRun,
                                      final  Runnable cancelRun,
                                      final  Runnable timeoutRun,
                                      final int allType) {
        String _title = null;
        if (Const.sysFileOn) {  //打开系统选择器
            final StringHandler handler = new StringHandler() {  //接收文件名称并处理
                public void handle(String result) {
                    display("select result="+result);
                    //System.out.println("select result="+result);
                    if (result == null || "".equals(result = result.trim()) || "[]".equals(result) || result.startsWith(">")) {
                        display("file:"+result);
                        //System.out.println("file:" + result);  //测试
                        return;
                    }
                    fKeeper.file = new File(result);
                    if (yesRun != null) PubConst.es.submit(yesRun);
                }
            };
            _title = getDialogTitle(  title,    demo, MainFrameProps.fileChooserSaveDlgTitle );
            final int mode = 3;  //0选择1个，1选择多个，2选择文件，3保存
            final String exportName = getExportName(fKeeper);  //保存预设文件名称，不含路径
            final String openPath = fKeeper.dir;  //打开的文件夹
            //final int allType = 1; //文件过滤栏目中全部文件所在，0无，1前，2后。
            try {
                GetFile.openSystemFile(getFrame(), _title, mode, exportName, openPath, handler, filters, allType);
                return;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        openFileSaveDialogUI( fKeeper, _title==null?getDialogTitle(  title,    demo, MainFrameProps.fileChooserSaveDlgTitle ):_title,
                filters, timeoutSecond, yesRun, cancelRun, timeoutRun,allType);
    }
    default String getDialogTitle(final String title, final String demo, final String defaultTitle){
        if(title==null && demo==null)return defaultTitle;
        if(demo==null)return title;
        return new StringBuilder() .append("【 ")
                .append( title )
                .append("】")
                .append(demo).toString();
    }
    private String getExportName(final FileKeeper fKeeper){
        File af=fKeeper.file;
        if(af==null ){
            if(fKeeper.fullName!=null)return new File(fKeeper.fullName).getName();
            else if(fKeeper.shortName!=null){
                String str;
                if(fKeeper.extName==null)return fKeeper.shortName;
                else return new StringBuilder().append(fKeeper.shortName).append(".").append(fKeeper.extName).toString();
            }else{
                return null;
            }
        }else return af.getName();
    }
    //获取普通文件过滤器
    private List<FFCFileFilter> getCommonFileFilters(){
        return getCommonFileFilters(true);
    }
    private List<FFCFileFilter> getCommonFileFilters(final boolean isMedia){
        final ArrayList<FFCFileFilter> fts = new ArrayList<FFCFileFilter>(4);
        if(isMedia){
            fts.add(new FFCFileFilter(MainFrameProps.fileTyeName_media, MediaConst.mediaTypes));
            fts.add(new FFCFileFilter(MainFrameProps.fileTyeName_image, MediaConst.imageTypes));
            fts.add(new FFCFileFilter(MainFrameProps.fileTyeName_music, MediaConst.musicTypes));
            fts.add(new FFCFileFilter(MainFrameProps.fileTyeName_text, Const.textTypes));
        }else {
            fts.add(new FFCFileFilter(MainFrameProps.fileTyeName_text, Const.textTypes));
            fts.add(new FFCFileFilter(MainFrameProps.fileTyeName_media, MediaConst.mediaTypes));
            fts.add(new FFCFileFilter(MainFrameProps.fileTyeName_image, MediaConst.imageTypes));
            fts.add(new FFCFileFilter(MainFrameProps.fileTyeName_music, MediaConst.musicTypes));
        }
        return fts;
    }
    void openFileSaveDialogUI(  final FileKeeper fKeeper,
                                final String title,
                                final List<FFCFileFilter> filters,
                                final int timeoutSecond,
                                final  Runnable yesRun,
                                final  Runnable cancelRun,
                                final  Runnable timeoutRun,
                                final  int allType  );

    /**
     * 文件选择对话框
     *@param fKeeper 文件信息保存者
     *@param title 标题
     *@param demo 询问语句
     *@param isMulti 是否多选
     *@param timeoutSecond  超时秒数  0表示长期
     *@param yesRun  确定时执行线程
     *@param cancelRun 取消或关闭窗口时执行线程
     *@timeoutRun 超时时执行线程
     *@param allType 全部类型
     **/
     default void openFileSelectDialog(final FileKeeper fKeeper,
                                     final String title,
                                     final String demo,
                                     final FFCFileFilter filter,
                                     final boolean isMulti,
                                     final int timeoutSecond,
                                     final  Runnable yesRun,
                                     final  Runnable cancelRun,
                                     final  Runnable timeoutRun,
                                     final int allType) {
         List<FFCFileFilter> filters;
         if(filter==null)filters=getCommonFileFilters();
         else{
             filters = new ArrayList<FFCFileFilter>(1);
             filters.add(filter);
         }
         openFileSelectDialog(  fKeeper,  title,   demo, filters, isMulti,timeoutSecond, yesRun,  cancelRun, timeoutRun, allType);
     }
    private void openFileSelectDialog(final FileKeeper fKeeper,
                                     final String title,
                                     final String demo,
                                     final List<FFCFileFilter> filters,
                                     final boolean isMulti,
                                     final int timeoutSecond,
                                     final  Runnable yesRun,
                                     final  Runnable cancelRun,
                                     final  Runnable timeoutRun,
                                     final int allType) {
        String _title = null;
        if (Const.sysFileOn) {  //打开系统选择器
            final StringHandler handler = new StringHandler() {
                public void handle(String result) {
                    System.out.println("select result="+result);
                    if (result == null || "".equals(result = result.trim()) || "[]".equals(result) || result.startsWith(">")) {
                        if(result.startsWith(">"))System.out.println("Error:"+ result);
                        else{
                            if(cancelRun!=null)PubConst.es.submit(cancelRun);
                        }
                        return;
                    }
                    if(isMulti){
                        // System.out.println("File:"+ result);  //测试
                        result = StringUtil.unicode2String(result);
                        //System.out.println("File2:"+ result);  //测试
                        final String[] sts = StringUtil.strTokenizer(result.substring(2,result.length()-2), "\",\"");
                        fKeeper.files = new File[sts.length];
                        for(int i=0;i<sts.length;i++)fKeeper.files[i]=new File(sts[i]);
                        if (yesRun != null) PubConst.es.submit(yesRun);
                    }else {
                        final File f = new File(result);
                        if (f.isFile()) {
                            if (fKeeper.maxLength > 0 && f.length() > fKeeper.maxLength) {
                                openMsgDialog(3, MainFrameProps.fileUploadLimit[0], MainFrameProps.fileUploadLimit[1] + fKeeper.maxLength + MainFrameProps.fileUploadLimit[2]);
                            } else {
                                fKeeper.file = f;
                                fKeeper.files = new File[]{f};  //调用者可能不区分，这个字段一定要有
                                if (yesRun != null) PubConst.es.submit(yesRun);
                            }
                        }
                    }
                }
            };
            _title = getDialogTitle(title, demo, MainFrameProps.fileChooserSelectDlgTitle);
            final int mode = isMulti?1:0;  //0选择1个，1选择多个，2选择文件夹，3保存
            final String exportName = getExportName(fKeeper);  //保存预设文件名称，不含路径
            final String openPath = fKeeper.dir;  //打开的文件夹
            //final int allType = 2; //文件过滤栏目中全部文件所在，0无，1前，2后。
            try {
                GetFile.openSystemFile(getFrame(), _title, mode, exportName, openPath, handler, filters, allType);
                return;
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        openFileSelectDialogUI(  fKeeper, title==null?getDialogTitle(  title,    demo, MainFrameProps.fileChooserSelectDlgTitle ):_title,
                filters, isMulti, timeoutSecond, yesRun, cancelRun, timeoutRun,allType);
    }
    void openFileSelectDialogUI(final FileKeeper fKeeper,
                                                   final String title,
                                                   final List<FFCFileFilter> filters,
                                                   final boolean isMulti,
                                                   final int timeoutSecond,
                                                   final  Runnable yesRun,
                                                   final  Runnable cancelRun,
                                                   final  Runnable timeoutRun,
                                                   final int allType);

    /**
     *创建对话框
     *&isActive 如果弹窗已经关闭，isActive[0]=false;
     *&lst 列表
     *&title 标题
     *&modal 模态，true表示锁定
     *&demo 提示标签
     *&btnYesCaption 确定按钮标题
     *&timeoutSecond 超时秒数
     **/
    void openListDisplayDialog(final Object frame, final Object[] rtDlg,
                               final boolean[] isActive,
                               final Object component,
                               final String title,
                               final boolean modal,
                               final String demo,
                               final List<ButtonAction> buttons,
                               final Runnable timeoutRun,
                               final int timeoutSecond);
}
