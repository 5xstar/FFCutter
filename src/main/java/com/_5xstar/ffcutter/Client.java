package com._5xstar.ffcutter;

/**
 *获取保存客户端
 *庞海文 2024-1-19
**/
public interface Client
{
    /**
     * 主窗口
     * @return
     */
    MainFrame getClient();
    /**
     *设置客户端
     *@client 客户端
     **/
    void setClient(final MainFrame client);

}
