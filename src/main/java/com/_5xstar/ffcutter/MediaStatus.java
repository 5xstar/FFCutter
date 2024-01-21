package com._5xstar.ffcutter;

import com._5xstar.ffcutter.domains.FileMediaInfo;

import java.io.File;
import java.util.List;

/**
 *媒体的状态
 *庞海文 2024-1-19
**/
public interface MediaStatus
{

    /////////////  ExtMediaAbstractImpl  abstract implements Media ///////////////////////

    /**
     * 多行命令
     */
    List<List<String>> getComs();

    /**
     * 用户源文件，由于只能获取1个文件，正流程采用fileMediaInfos代替，保留由setIOFile使用
     */
    File getSrcFile();


    /**
     *处理完是否播放
     *@autoPlay 播放
     **/
    void setAutoPlay(final boolean autoPlay);
    boolean getAutoPlay();    //默认是true

    //////////////////////    FilesFFMpeg        ///////////////////

    /**
     *实现完成方法，拷贝结果文件和清理临时文件
     **/
    boolean getHasFinished();
    void setHasFinished(boolean hasFinished);

    //是否打包为一个文件，默认值true
    boolean getToOne();
    void setToOne(boolean toOne);

    //系统目标文件，该文件在tempFiles中，工作完成后被清理
    File getSysTarFile();
    void setSysTarFile(File sysTarFile);

    //用户目标文件
    File getTarFile();
    void setTarFile(File tarFile);

    //临时文件收集器，工作完成后执行清理
    List<File> getTempFiles();


    //运行完成后执行的外加线程
    Runnable getOkRun();
    void setOkRun(Runnable okRun);


    /////////////////////   UnorderedFilesFFMpeg      //////////////////
    /**
     * 是否单选
     */
    boolean getFromOne();
    void setFromOne(boolean fromOne);

    /**
     * 媒体文件与信息
     * @return
     */
    List<FileMediaInfo> getFileMediaInfos();


}
