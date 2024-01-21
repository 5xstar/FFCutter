package com._5xstar.ffcutter.cutter.swing;

/**
 *一类关键词显示调整
 *庞海文 2021-2-15
**/
public interface KeywordDisplayAdapter<T>{

    /**
     * 文本组件的显示调整
     *
     *&textComp 要调整的文本组件
     *&text 要处理的文本
     *&start 开始位置
     *&end 结束位置
     *&keyword 要处理的关键字
    **/
    void adapter(final T textComp, String text, final int start, final int end, final String keyword);


    /**
     *清除文本所有调整
     *&textComp 要调整的文本组件
    **/
    void removeAllAdapters(final T textComp);

}
