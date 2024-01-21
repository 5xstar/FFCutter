package com._5xstar.ffcutter.cutter;

import com._5xstar.ffcutter.*;
import com._5xstar.ffcutter.domains.FileMediaInfo;
import com._5xstar.ffcutter.domains.MediaPart;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * FFCutter的抽象实现，保留应用的Status
 * 庞海文 2024-1-19
 */
public abstract class FFCutterAbstractImpl implements FFCutter {

    //////////////   implements MedisStatus begin  /////////////
    private MainFrame client;  //主窗体
    @Override
    public MainFrame getClient() {
        return this.client;
    }

    @Override
    public void setClient(MainFrame client) {
        this.client=client;
    }

    //多行命令
    final private List<List<String>> coms =new ArrayList<List<String>>(20);  //这时coms是空的
    /**
     * 多行命令
     */
    @Override
    public List<List<String>> getComs() {
        return coms;
    }

    /**
     * 用户源文件，由于只能获取1个文件，正流程采用fileMediaInfos代替，保留由setIOFile使用
     */
    private File srcFile=null;
    @Override
    public File getSrcFile() {
        return this.srcFile;
    }

    /**
     *处理完是否播放
     *@autoPlay 播放
     **/
    private boolean autoPlay=true;    //默认是true
    @Override
    public void setAutoPlay(boolean autoPlay) {
         this.autoPlay=autoPlay;
    }
    @Override
    public boolean getAutoPlay() {
        return this.autoPlay;
    }

    /**
     *实现完成方法，拷贝结果文件和清理临时文件
     **/
    private boolean hasFinished=false;
    @Override
    public boolean getHasFinished() {
        return this.hasFinished;
    }
    @Override
    public void setHasFinished(boolean hasFinished) {
        this.hasFinished=hasFinished;
    }

    //是否打包为一个文件，默认值true
    private boolean toOne=true;
    @Override
    public boolean getToOne() {
        return this.toOne;
    }
    @Override
    public void setToOne(boolean toOne) {
        this.toOne=toOne;
    }

    //系统目标文件，该文件在tempFiles中，工作完成后被清理
    private File sysTarFile=null;
    @Override
    public File getSysTarFile() {
        return this.sysTarFile;
    }
    @Override
    public void setSysTarFile(File sysTarFile) {
        this.sysTarFile=sysTarFile;
    }

    //用户目标文件
    private File tarFile=null;
    @Override
    public File getTarFile() {
        return this.tarFile;
    }
    @Override
    public void setTarFile(File tarFile) {
        this.tarFile=tarFile;
    }

    //临时文件收集器，工作完成后执行清理
    private List<File> tempFiles=new ArrayList<File>(10);
    @Override
    public List<File> getTempFiles() {
        return this.tempFiles;
    }

    //运行完成后执行的外加线程
    private Runnable okRun=null;
    @Override
    public Runnable getOkRun() {
        return this.okRun;
    }
    @Override
    public void setOkRun(Runnable okRun) {
        this.okRun=okRun;
    }

    /**
     * 是否单选
     */
    private boolean fromOne=true;
    @Override
    public boolean getFromOne() {
        return this.fromOne;
    }
    @Override
    public void setFromOne(boolean fromOne){
        this.fromOne=fromOne;
    }

    //媒体文件与信息
    final private List<FileMediaInfo>  fileMediaInfos=new ArrayList<>(20);
    @Override
    public List<FileMediaInfo> getFileMediaInfos() {
        return this.fileMediaInfos;
    }

    //////////////   implements MedisStatus end  /////////////
    //////////////   implements FFCutterStatus begin  /////////////

    //编辑状态
    private int editType=addType;
    @Override
    public int getEditType() {
        return this.editType;
    }
    @Override
    public void setEditType(int editType) {
        this.editType=editType;
    }

    /**
     * 获取媒体片段发生器
     * @return
     */
    private MediaPartFactory mediaPartFactory=null;
    @Override
    public MediaPartFactory getMediaPartFactory() {
        return this.mediaPartFactory;
    }
    @Override
    public void setMediaPartFactory(MediaPartFactory mediaPartFactory) {
       this.mediaPartFactory=mediaPartFactory;
    }

    /**
     * 与临时片段对应的片段
     */
    private  MediaPart tmpPartSrc=null;
    @Override
    public MediaPart getTmpPartSrc() {
        return this.tmpPartSrc;
    }
    @Override
    public void setTmpPartSrc(MediaPart tmpPartSrc) {
        this.tmpPartSrc=tmpPartSrc;
    }

    //媒体片段
    final private Vector<MediaPart> parts=new Vector<MediaPart>(20);
    @Override
    public Vector<MediaPart> getParts() {
        return this.parts;
    }

    //当前媒体片段编号
    private int currentIndex=-1;
    @Override
    public void setCurrentIndex(int currentIndex) {
        this.currentIndex=currentIndex;
    }
    @Override
    public int getCurrentIndex() {
        return this.currentIndex;
    }

    //媒体索引
    private int index=0;
    @Override
    public void setIndex(int index) {
        this.index=index;
    }
    @Override
    public int getIndex() {
        return this.index;
    }


    //开始时间输入组件
    final private FFCTextField[] begins=createBegins();
    @Override
    public FFCTextField[] getBegins() {
        return this.begins;
    }

    //结束时间输入组件
    final private FFCTextField[] ends=createEnds();
    @Override
    public FFCTextField[] getEnds() {
        return this.ends;
    }

    //描述
    private String desc=FFCutterProps.defaultDesc;
    @Override
    public String getDesc() {
        return this.desc;
    }

    //标题
    private String title= FFCutterProps.defaultTitle;
    @Override
    public String getSrcTitle() {
        return this.title;
    }

    //////////////   implements FFCutterStatus end  /////////////
    ///////////////  implements AutoClosable   //////////////
    private boolean hasCloesed = false;
    @Override
    public void close() throws Exception {
        synchronized (synObject) {
            if (hasCloesed) return;
            hasCloesed = true;
            finished();
        }
    }

    protected FFCutterAbstractImpl(final MainFrame client){  //初始化
        super();
        this.client=client;
        setFromOne(false);
    }
}
