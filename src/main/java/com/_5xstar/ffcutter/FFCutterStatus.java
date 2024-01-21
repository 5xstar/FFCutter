package com._5xstar.ffcutter;

import com._5xstar.ffcutter.domains.MediaPart;
import java.util.Vector;

/**
 * 剪切器接口，用于与媒体片段发生器沟通
 * 庞海文 2024-1-17改
 */
public interface FFCutterStatus{

    //编辑状态
    int addType=0;  //添加
    int insertType=1;   //插入
    int updateType=2;   //更新
    int getEditType();
    void setEditType(int editType);

    /**
     * 获取媒体片段发生器
     * @return
     */
    MediaPartFactory getMediaPartFactory();
    void setMediaPartFactory(MediaPartFactory factory);

    /**
     * 与临时片段对应的片段
     */
     MediaPart getTmpPartSrc();
     void setTmpPartSrc(MediaPart mp);

    /**
     * 媒体片段列表
     * @return
     */
    Vector<MediaPart> getParts();

    /**
     * 当前媒体片段编号
     * @param currentIndex
     */
    void setCurrentIndex(int currentIndex);
    int getCurrentIndex();

    /**
     * 媒体索引
     * @param index
     */
    void setIndex(int index);
    int getIndex();


    ////////////////    FFCutterBase        ///////////////////
    /**
     * 开始时间组件
     */
    FFCTextField[] getBegins();

    /**
     * 结束时间组件
     */
    FFCTextField[] getEnds();

    //////////////////////    FFCutter           //////////////////////////

    //获取帮助描述
    String getDesc();

    /**
     * 获取实例标题
     * @return
     */
    String getSrcTitle();


}
