package com._5xstar.ffcutter;


import com._5xstar.ffcutter.constant.MediaConst;
import com._5xstar.ffcutter.constant.PubConst;
import com._5xstar.ffcutter.domains.FileKeeper;
import com._5xstar.ffcutter.domains.FileMediaInfo;
import com._5xstar.ffcutter.domains.MediaInfo;
import com._5xstar.ffcutter.file.FFCFileFilter;
import com._5xstar.ffcutter.file.MyFileService;
import com._5xstar.ffcutter.util.MediaUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *媒体T 客户端 C 工具操作容器 
 *庞海文 2020-3-20
**/
public interface Media extends MediaStatus, Client
{


    /////////////  ExtMediaAbstractImpl  abstract implements Media ///////////////////////

    /**
     *执行
     *@throws NullPointerException
     **/
    default void exec()throws NullPointerException
    {
        MainFrame client = getClient();
        if(client==null)throw new NullPointerException(MediaProps.execMsg[0]);
        prepare(new Runnable(){
            public void run(){
                List<List<String>> coms = getComs();
                if(!coms.isEmpty()){
                    try{
                        for(int i=0;i<coms.size();i++){
                            MediaUtil.exec(client,coms.get(i));
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                        client.display(MediaProps.execMsg[1],  Display.DEBUG_TEXT);
                    }
                }
                finished();  //保证调用释放资源
            }
        });  //准备资源，准备好了执行线程
    }

    /**
     *准备资源，完成执行线程，由子类实现
     @execRun 准备好了执行线程
     **/
    private void prepare(final Runnable execRun){
        File sf = getSrcFile();
        if (sf != null) {  //setIOFile()方式运行，否则在选择文件之前，srcFile一定为空
            tempFilesAndParasPrepare(execRun);
        }else{
            filesPrepare(execRun);
        }
    }

    //////////////////////    FilesFFMpeg        ///////////////////

    /**
     *实现完成方法，拷贝结果文件和清理临时文件
     * 完成释放资源，由子类实现，临时输出文件不会重复
     * 设置一个boolean变量，synchronized 保证只调用一次。
     **/
    default void finished(){
        boolean hf = getHasFinished();
        if(hf)return;  // synchronized+hasFinished保证该方法最多只调用1次
        setHasFinished(true);
        //hasFinished=true;
        boolean to = getToOne();
        boolean ap = getAutoPlay();
        File tf = getTarFile();
        File sf = getSysTarFile();
        if(to) {
            boolean isok = false;
            if (sf != null && sf.exists()) {
                try {
                    MyFileService.copy(sf, tf);  //有把目录压缩成zip功能
                    isok = true;
                    if (ap) {
                        PubConst.es.submit(new Runnable() {
                            public void run() {
                                MediaUtil.play(getClient(), tf);  //方法阻塞
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (isok) {
                if (!ap)
                    getClient().openMsgDialog(3, MediaProps.mediaTitle, MediaProps.finishedMsg[0] + tf.getAbsolutePath());
            } else {
                getClient().openMsgDialog(3, MediaProps.mediaTitle, MediaProps.finishedMsg[1] + tf.getAbsolutePath());
            }
        }else{
            getClient().openMsgDialog(3, MediaProps.mediaTitle, MediaProps.finishedMsg[2] + tf.getAbsolutePath());
        }
        List<File> tfs = getTempFiles();
        if(!tfs.isEmpty()){
            File f;
            for(int i=0;i<tfs.size();i++){
                f=tfs.get(i);
                try{
                    MyFileService.delTreeFiles(f);  //可以删除临时文件夹
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        if(sf!=null && sf.exists()){
            try{sf.delete();}catch(Exception e){}
        }
        Runnable orn = getOkRun();
        if(orn!=null)PubConst.es.submit(orn);
    }

    /////////////////////   UnorderedFilesFFMpeg      //////////////////

    /**
     *文件准备，完成后调用 tempFilesAndParasPrepare
     **/
    private void filesPrepare(final Runnable execRun){
        //
        final FileKeeper fKeeper=new FileKeeper();
        fKeeper.dir= MediaConst.extMediaDir.getAbsolutePath();
        //目标文件确认
        final Runnable yesRun2=new Runnable(){
            public void run(){
                try{
                    File tf = fKeeper.file;
                    setTarFile(tf);
                    //tarFile=fKeeper.file;
                    if(tf.exists()){  //是否覆盖
                        if(tf.isFile())
                            getClient().openConfirmDialog(MediaProps.coverFileText,     //有确定、否定、超时，弹窗可以关闭
                                    new StringBuilder().append(MediaProps.filesPrepareMsg[0]).append(tf.getAbsolutePath()).toString(),
                                    0,
                                    new Runnable(){ public void run(){tempFilesAndParasPrepare( execRun);}},
                                    new Runnable(){ public void run(){}},
                                    null);
                        else
                            getClient().openConfirmDialog(MediaProps.coverFileText,     //有确定、否定、超时，弹窗可以关闭
                                    new StringBuilder().append(MediaProps.filesPrepareMsg[1])
                                            .append(tf.getAbsolutePath()).append(MediaProps.filesPrepareMsg[2]).toString(),
                                    0,
                                    new Runnable(){ public void run(){tempFilesAndParasPrepare( execRun);}},
                                    new Runnable(){ public void run(){}},
                                    null);
                    }else{
                        tempFilesAndParasPrepare( execRun);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        //目标文件
        final Runnable yesRun1=new Runnable(){
            public void run(){
                if(!MediaUtil.checkSame(fKeeper.files)){
                    getClient().openMsgDialog(1, MediaProps.mediaTitle, MediaProps.filesPrepareMsg[3]);
                    return;
                }
                try{
                    long d=-1L;
                    final  List<FileMediaInfo> fmis = checkFile(fKeeper.files);
                    if(fmis!=null && !fmis.isEmpty()){
                        getFileMediaInfos().addAll(fmis);
                        //获取输出文件
                        fKeeper.dir=MediaConst.extMediaDir.getAbsolutePath();
                        fKeeper.file=new File(MediaConst.extMediaDir, fKeeper.files[0].getName());
                        getClient().openFileSaveDialog(fKeeper,
                                MediaProps.filesPrepareMsg[4],
                                MediaProps.filesPrepareMsg[5],
                                new FFCFileFilter(MediaProps.filesPrepareMsg[6], MediaConst.mediaTypes),
                                0,
                                yesRun2,
                                null,
                                null);
                    }else{
                        getClient().openMsgDialog(1, MediaProps.mediaTitle, MediaProps.filesPrepareMsg[7]);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        //源文件
        getClient().openFileSelectDialog(fKeeper,
                MediaProps.mediaTitle,
                MediaProps.filesPrepareMsg[8],
                new FFCFileFilter(MediaProps.filesPrepareMsg[9], MediaConst.mediaTypes),
                !getFromOne(),
                0,
                yesRun1,
                null,
                null,
                2);


    }
    /**
     * 检查媒体文件
     */
    private List<FileMediaInfo> checkFile(final File[] mediaFiles){
        if(mediaFiles==null || mediaFiles.length==0)return null;
        final List<FileMediaInfo> fms= new ArrayList<>(mediaFiles.length);
        FileMediaInfo fmi;
        int idx=0;
        for(int i=0;i<mediaFiles.length;i++){
            fmi = checkFile(0, mediaFiles[i]);
            if(fmi!=null){
                fms.add(new FileMediaInfo(idx++,fmi.file,fmi.mediaInfo));
            }
        }
        return fms;
    }

    /**
     * 检查生成媒体Keeper标准方法
     */
    default FileMediaInfo checkFile(final int sn, final File mediaFile){
        if(mediaFile==null || !mediaFile.exists()  || !mediaFile.isFile())return null;
        final MediaInfo mi = MediaUtil.getMediaInfo(getClient(), mediaFile);
        if(mi==null)return null;
        final long d = MediaUtil.getDuration(mi);
        if(d<=0)return null;
        mi.duration.longDuration=d;
        return new FileMediaInfo(sn, mediaFile, mi);
    }

    /**
     *文件准备好后，临时文件及参数准备工作，由子类实现
     **/
    void tempFilesAndParasPrepare(final Runnable execRun);

}
