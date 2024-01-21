package com._5xstar.ffcutter;

import com._5xstar.ffcutter.domains.MediaPart;

import java.util.List;

/**
 * 用播放播放器生成MediaPart 的状态
 * 庞海文 2024-1-19
 */
public interface MediaPartFactoryStatus {

    /**
     * 获取剪接器
     * @return
     */
    FFCutter getCutter();
    void setCutter(FFCutter ffCutter);

    /**
     * 获取播放器
     * @return
     */
    Player getPlayer();
    void setPlayer(Player player);

    /**
     *true 初始化未完成
     * @return
     */
    boolean getNoReady();
    void setNoReady(boolean noReady);

    /**
     * 阅览
     */
    boolean getPrePlayOn();
    void setPrePlayOn(boolean prePlayOn);

    /**
     * 剪切器关闭
     * @param isCutterClosed
     */
    void setIsCutterClosed(boolean isCutterClosed);

    /**
     * 接收媒体片段
     * @param mp 媒体片段
    */
    void setMediaPart(MediaPart mp);
    MediaPart getMediaPart();


}
