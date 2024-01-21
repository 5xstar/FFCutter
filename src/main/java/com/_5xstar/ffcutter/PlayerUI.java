package com._5xstar.ffcutter;

/**
*播放器界面实现接口
*庞海文 2024-1-19
**/
public interface PlayerUI
{

    /**
     * 显示窗体
     * @param visible
     */
    void setVisible(boolean visible);

    /**
     * 打开媒体
     * @param strMrl
     */
    void play(final String strMrl);

    /**
     * 预播放媒体片段
     * @param begin 开始
     * @param end  结束
     */
    void prePlayAction(long begin, long end);

    /**
     * 清理连接资源
     */
    void clearup();

    
}
