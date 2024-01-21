package com._5xstar.ffcutter.cutter.swing;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.text.Highlighter;
import java.awt.*;


/**
 *代码文本显示调整
 *庞海文 2021-2-14
**/
public class HighlightAdapter<T extends JTextPane> extends DefaultHighlightPainter
                                                        implements KeywordDisplayAdapter<T>
{
    /**
     *构造函数
     @color 高亮颜色
    **/
    public HighlightAdapter(Color color) {
        super(color);
    }

    /**
     * 文本组件的显示调整:高亮处理
     *@textComp 要调整的文本组件
     *@text 要处理的文本
     *@start 开始位置
     *@end 结束位置
     *@keyword 要处理的关键字
    **/
    public void adapter(final T textComp, String text, final int start, final int end, final String keyword){
        Highlighter hilite = textComp.getHighlighter();
        try{
           hilite.addHighlight(start, end, this);
        }catch(BadLocationException e){
          e.printStackTrace();
        }
    }

    /**
     * 文本组件的显示调整:高亮处理清除
     *@textComp 要调整的文本组件
    **/
    public void removeAllAdapters(final T textComp) {
        //System.out.println("removeAllAdapters action");
        Highlighter hilite = textComp.getHighlighter();
        Highlighter.Highlight[] hilites = hilite.getHighlights();
        for (int i = 0; i < hilites.length; i++) {
            if (hilites[i].getPainter() instanceof HighlightAdapter) {
                hilite.removeHighlight(hilites[i]);
            }
        }
    }




}
