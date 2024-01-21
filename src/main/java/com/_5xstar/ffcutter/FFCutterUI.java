package com._5xstar.ffcutter;

import com._5xstar.ffcutter.domains.FileMediaInfo;

/**
 * 剪切器界面
 * 庞海文 2024-1-19
 */
public interface FFCutterUI{

    /**
     *  更新列表界面
     */
    void updateMediaPartsChooserUI();


    /**
     * 重置媒体片段选择器
     */
    void resetMediaPartsChooserUI();

    /**
     * 重置文件选择器
     */
    void resetFileChooserUI();

    /**
     * 更新确定按钮标签
     */
    void updateYesCaption(final String caption);

    /**
     * 增加1个项目
     * @param fmi
     */
    void addItem(final FileMediaInfo fmi);

    /**
     * 更新文件选择器，阻止事件生成
     */
    void updateFileChooserUI();

    /**
     * 改变预览按钮状态
     * @param preViewCurrent
     * @param preView
     * @param preViewStop
     */
    void setEnabled(final boolean preViewCurrent, final boolean preView, final boolean preViewStop );/*
                     btnPreViewCurrent.setEnabled(preViewCurrent);
                     btnPreView.setEnabled(preView);
                     btnPreViewStop.setEnabled(preViewStop);
   */


    /**
     * 关闭窗口
     */
     void dispose();//dlg.dispose();


    /**
     * 创建媒体片段发生器
     */
    MediaPartFactory createMediaPartFactory();

    /**
     * 界面实现，创建四个文本框
     * @return
     */
    FFCTextField[] create4FFCTextFieldsBegins();

    /**
     * 界面实现，创建四个文本框
     * @return
     */
    FFCTextField[] create4FFCTextFieldsEnds();

    /**
     * 更新标题
     */
    void updateTitleUI(final String newTitle);

}
